@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    id("com.android.library")
    kotlin("multiplatform")
}

kotlin {
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
//             Configure the source set here
        }
    }
}

// Configure existing tasks or plugins
android {
    namespace = "com.example.lib"
    compileSdkVersion = "android-31"
}