@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    explicitApi()
    targetHierarchy.default()

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-receivers")
    }

    jvmToolchain(17)

    jvm()
    androidTarget()

    // macos
    macosX64()
    macosArm64()

    // ios
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    // wasm
    wasmJs {
        d8()
    }

    // Access source sets configured in the module.yaml:
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kotlinx.io)
                api(libs.serialization.json)
                implementation(libs.serialization.core)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.auth)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.serialization.json)
                implementation(libs.kotlinx.datetime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }

        val appleMain by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        val wasmJsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }
    }
}

// Configure existing tasks or plugins
android {
    namespace = "com.example.lib"
    compileSdkVersion = "android-31"
}