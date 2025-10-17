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

    @Suppress("LocalVariableName")
    val loom_version: String by extra
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "dev.architectury.loom") {
                useVersion(loom_version)
            }
        }
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.8-alpha.6.1"
}

stonecutter {
    kotlinController = true
    centralScript = "build.gradle"

    create(getRootProject()) {
        versions("1.20.1", "1.21.1", "1.21.4", "1.21.5", "1.21.6", "1.21.9")
        branch("fabric")
        branch("forgelike")

        vcsVersion = "1.21.6"
    }
}