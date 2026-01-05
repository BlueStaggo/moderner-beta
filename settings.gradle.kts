@file:Suppress("LocalVariableName")

pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        maven {
            name = "Architectury"
            url = uri("https://maven.architectury.dev/")
        }
        maven {
            name = "NeoForged"
            url = uri("https://maven.neoforged.net/releases")
        }
        maven {
            url = uri("https://maven.kikugie.dev/releases")
        }
        maven {
            url = uri("https://maven.kikugie.dev/snapshots")
        }
        gradlePluginPortal()
    }

    val loom_version: String by extra
    val mdg_version: String by extra
    val stonecutter_version: String by extra
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.startsWith("net.fabricmc.fabric-loom")) {
                useVersion(loom_version)
            }

            if (requested.id.id.startsWith("net.neoforged.moddev")) {
                useVersion(mdg_version)
            }

            if (requested.id.id == "dev.kikugie.stonecutter") {
                useVersion(stonecutter_version)
            }
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("dev.kikugie.stonecutter")
}

stonecutter {
    kotlinController = true
    centralScript = "build.gradle.kts"

    create(getRootProject()) {
        versions("1.20.1", "1.21.1", "1.21.6", "1.21.9", "1.21.11")
        versions("26.1").buildscript("build-unobf.gradle.kts")
        branch("fabric")
        branch("forgelike") {
            versions("1.20.1").buildscript("build-lexforge.gradle.kts")
            versions("1.21.1", "1.21.6", "1.21.9", "1.21.11", "26.1")
        }

        vcsVersion = "1.21.6"
    }
}