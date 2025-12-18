plugins {
    id("multiloader-loader").apply(false)
    id("net.neoforged.moddev")
    kotlin("jvm")
    id("com.google.devtools.ksp")
    id("dev.kikugie.fletching-table")
    id("com.modrinth.minotaur")
}

project.ext["loader"] = "neoforge"
project.ext["supported_loaders"] = "neoforge"

apply(plugin = "multiloader-loader")

stonecutter.constants.put("forge", false)
stonecutter.constants.put("neoforge", true)

neoForge {
    enable {
        version = commonMod.prop("forgelike_loader_version")
        //English 101, brought to you by Kotlin DSL
        isDisableRecompilation = true
    }
}

dependencies {
}

fletchingTable {
    accessConverter.register("main") {
        add("moderner_beta.accesswidener")
    }

    mixins.create("main") {
        mixin("default", "moderner_beta-forgelike.mixins.json")
    }
}

neoForge {
//    val at = project.file("build/resources/main/META-INF/accesstransformer.cfg");
//
//    accessTransformers.from(at.absolutePath)
//    validateAccessTransformers = true

    runs {
        register("client") {
            client()
            ideName = "Minecraft Client (${project.path})"
            gameDirectory = project.file("../run")
        }

        register("server") {
            server()
            ideName = "Minecraft Server (${project.path})"
            gameDirectory = project.file("../run")
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

modrinth {
    uploadFile.set(tasks.jar)
}