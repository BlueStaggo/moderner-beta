plugins {
    id("multiloader-loader").apply(false)
    id("net.neoforged.moddev")
    alias(ft.plugins.mixin)
    id("me.modmuss50.mod-publish-plugin")
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
    /*accessConverter.register("main") {
        add("moderner_beta.accesswidener")
    }*/

    mixins.configure(sourceSets.main) {
        mixin("moderner_beta-forgelike.mixins.json", "default")
    }
}

neoForge {
    val at = rootProject.file("forgelike/src/main/resources/META-INF/at.cfg") //project.file("build/resources/main/META-INF/accesstransformer.cfg")

    accessTransformers.from(at.absolutePath)
    validateAccessTransformers = true

    runs {
        register("client") {
            client()
            ideName = "Minecraft Client ($path)"
            gameDirectory = file("../../../run")
        }

        register("server") {
            server()
            ideName = "Minecraft Server ($path)"
            gameDirectory = file("../../../run")
        }
    }

    commonMod.propOrNull("parchment_mappings")?.let {
        parchment {
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

tasks.processResources {
    filesMatching("META-INF/mods.toml") {
        if (stonecutter.eval(stonecutter.current.version, ">=1.20.5")) {
            name = "neoforge.mods.toml"
        }
    }

    filesMatching("META-INF/at.cfg") {
        name = "accesstransformer.cfg"
    }

    if (stonecutter.eval(stonecutter.current.version, "<26.2")) {
        exclude("assets/moderner_beta/icon.png")
    }
    exclude("moderner_beta*.accesswidener")
    exclude("META-INF/at-forge.cfg")
}

publishMods {
    file.set(tasks.jar.get().archiveFile)
}