@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMetadataTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.jreleaser.model.Active

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.dokka)
    alias(libs.plugins.jreleaser)
    `maven-publish`
    signing
}

group = "io.github.devcrocod"
version = "0.1.0"

kotlin {
    explicitApi()

    jvmToolchain(17)

    jvm {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }

    androidTarget {
        publishLibraryVariants("release")
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    // macos
    macosX64()
    macosArm64()

    // ios
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    js {
        browser()
        binaries.library()
    }

    // wasm
    wasmJs {
        nodejs() {
            testTask {
                useMocha {
                    timeout = "30s"
                }
            }
        }
        browser() {
            testTask {
                useMocha {
                    timeout = "30s"
                }
            }
        }
    }

    // Access source sets configured in the module.yaml:
    sourceSets {
        commonMain {
            dependencies {
                api(libs.kotlinx.io)
                api(libs.kotlinx.serialization.core)
                api(libs.kotlinx.serialization.json)
                api(libs.kotlinx.coroutines.core)
                api(libs.ktor.client.core)
                api(libs.ktor.client.auth)
                api(libs.ktor.client.content.negotiation)
                api(libs.ktor.client.serialization.json)
                api(libs.kotlinx.datetime)
                implementation(libs.ktor.client.logging)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        jvmMain {
            dependencies {
                api(libs.ktor.client.okhttp)
            }
        }

        appleMain {
            dependencies {
                api(libs.ktor.client.darwin)
            }
        }

        jsMain {
            dependencies {
                api(libs.ktor.client.js)
            }
        }

        wasmJsMain {
            dependencies {
                api(libs.ktor.client.js)
            }
        }
    }
}

android {
    namespace = "io.github.devcrocod.openai"
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dokka {
    moduleName = "OpenAI Kotlin SDK"

    pluginsConfiguration.html {
        footerMessage = "© 2025 devcrocod. All rights reserved."
    }

    dokkaSourceSets.configureEach {
        sourceLink {
            localDirectory.set(file("src/main/kotlin"))
            remoteUrl("https://github.com/devcrocod/openai-kt")
            remoteLineSuffix.set("#L")
            documentedVisibilities(VisibilityModifier.Public)
        }
    }

    dokkaPublications.html {
        outputDirectory = project.rootProject.layout.projectDirectory.dir("docs")
    }
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    publications.withType(MavenPublication::class).all {
        if (name.contains("jvm", ignoreCase = true)) {
            artifact(javadocJar)
        }
        pom.configureMavenCentralMetadata()
        signPublicationIfKeyPresent()
    }

    repositories {
        maven(url = layout.buildDirectory.dir("staging-deploy"))
    }
}

jreleaser {
    gitRootSearch = true
    strict.set(true)

    signing {
        active.set(Active.ALWAYS)
        armored = true
        artifacts = true
    }

    deploy {
        active.set(Active.ALWAYS)
        maven {
            active.set(Active.ALWAYS)
            mavenCentral {
                val ossrh by creating {
                    active.set(Active.ALWAYS)
                    url.set("https://central.sonatype.com/api/v1/publisher")
                    applyMavenCentralRules = false
                    maxRetries = 240
                    stagingRepository(layout.buildDirectory.dir("staging-deploy").get().asFile.path)
                    // workaround: https://github.com/jreleaser/jreleaser/issues/1784
                    kotlin.targets.forEach { target ->
                        if (target !is KotlinJvmTarget && target !is KotlinAndroidTarget && target !is KotlinMetadataTarget) {
                            val klibArtifactId = if (target.platformType == KotlinPlatformType.wasm) {
                                "${name}-wasm-${target.name.lowercase().substringAfter("wasm")}"
                            } else {
                                "${name}-${target.name.lowercase()}"
                            }
                            artifactOverride {
                                artifactId = klibArtifactId
                                jar = false
                                verifyPom = false
                                sourceJar = false
                                javadocJar = false
                            }
                        }
                    }
                }
            }
        }
    }

    release {
        github {
            skipRelease = true
            skipTag = true
            overwrite = false
            token = "none"
        }
    }

    checksum {
        individual = false
        artifacts = false
        files = false
    }
}

fun MavenPom.configureMavenCentralMetadata() {
    name by project.name
    description by "OpenAI Multiplatform Kotlin SDK"
    url by "https://github.com/devcrocod/openai-kt"

    licenses {
        license {
            name by "The Apache Software License, Version 2.0"
            url by "https://github.com/devcrocod/openai-kt/blob/master/LICENSE"
            distribution by "repo"
        }
    }

    developers {
        developer {
            id by "devcrocod"
            name by "Pavel Gorgulov"
        }
    }

    scm {
        url by "https://github.com/devcrocod/openai-kt"
        connection by "scm:git:git://github.com/devcrocod/openai-kt.git"
        developerConnection by "scm:git:git@github.com/devcrocod/openai-kt.git"
    }
}

fun MavenPublication.signPublicationIfKeyPresent() {
    val signingKey = project.getSensitiveProperty("GPG_SECRET_KEY")
    val signingKeyPassphrase = project.getSensitiveProperty("SIGNING_PASSPHRASE")

    if (!signingKey.isNullOrBlank()) {
        the<SigningExtension>().apply {
            useInMemoryPgpKeys(signingKey, signingKeyPassphrase)

            sign(this@signPublicationIfKeyPresent)
        }
    }
}

fun Project.getSensitiveProperty(name: String?): String? {
    if (name == null) {
        error("Expected not null property '$name' for publication repository config")
    }

    return project.findProperty(name) as? String
        ?: System.getenv(name)
        ?: System.getProperty(name)
}

infix fun <T> Property<T>.by(value: T) {
    set(value)
}
