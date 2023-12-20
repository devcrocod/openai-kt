@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    explicitApi()

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-receivers")
    }

    jvmToolchain(17)

    jvm()
    androidTarget()
    macosX64()
    macosArm64()
    linuxX64()
    linuxArm64()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
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

            }
        }
    }
}

// Configure existing tasks or plugins
android {
    namespace = "com.example.lib"
    compileSdkVersion = "android-31"
}