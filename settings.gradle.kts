pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

rootProject.name = "openai-kt"

include(":core")
project(":core").name = "openai-core"
