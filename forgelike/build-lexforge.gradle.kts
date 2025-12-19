import org.gradle.kotlin.dsl.accessTransformers
import org.gradle.kotlin.dsl.from

plugins {
    id("multiloader-loader").apply(false)
    id("net.neoforged.moddev.legacyforge")
    kotlin("jvm")
    id("com.google.devtools.ksp")
    id("dev.kikugie.fletching-table")
    id("com.modrinth.minotaur")
}

project.ext["loader"] = "forge"
project.ext["supported_loaders"] = "forge"

apply(plugin = "multiloader-loader")

stonecutter.constants.put("forge", true)
stonecutter.constants.put("neoforge", false)

legacyForge {
    enable {
        forgeVersion = commonMod.prop("forgelike_loader_version")
        //English 101, brought to you by Kotlin DSL
        isDisableRecompilation = true
    }
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.1.0")
    annotationProcessor("org.spongepowered:mixin:0.8.5-SNAPSHOT:processor")

    compileOnly(annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.0") as Any)
    implementation(jarJar("io.github.llamalad7:mixinextras-forge:0.5.0") as Any)
}

fletchingTable {
    /*accessConverter.register("main") {
        add("moderner_beta.accesswidener")
    }*/

    mixins.create("main") {
        mixin("default", "moderner_beta-forgelike.mixins.json")
    }
}

mixin {
    add(sourceSets.main.get(), "moderner_beta.mixin.refmap.json")

    config("moderner_beta-common.mixins.json")
    config("moderner_beta-forgelike.mixins.json")
}

legacyForge {
    val at = rootProject.file("forgelike/src/main/resources/META-INF/at-forge.cfg") //project.file("build/resources/main/META-INF/accesstransformer.cfg")

    accessTransformers.from(at.absolutePath)
    validateAccessTransformers = true

    runs {
        register("client") {
            client()
            ideName = "Minecraft Client (${project.path})"
            gameDirectory = project.file("../../../run")
        }

        register("server") {
            server()
            ideName = "Minecraft Server (${project.path})"
            gameDirectory = project.file("../../../run")
        }
    }

    parchment {
        commonMod.propOrNull("parchment_mappings")?.let {
            val parts = it.split(":")

            minecraftVersion = parts[0]
            mappingsVersion = parts[1]
        }
    }

    mods {
        register(commonMod.id) {
            sourceSet(sourceSets.main.get())
        }
    }
}

tasks {
    jar {
        finalizedBy("reobfJar")

        manifest.attributes(mapOf(
            "MixinConfigs" to "moderner_beta-common.mixins.json,moderner_beta-forgelike.mixins.json"
        ))
    }

    processResources {
        exclude("moderner_beta.accesswidener")
        exclude("META-INF/at.cfg")
    }
}

modrinth {
    uploadFile.set(tasks.jar)
}