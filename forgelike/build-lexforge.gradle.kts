plugins {
    `multiloader-loader`
    id("net.neoforged.moddev.legacyforge")
}

project.extra["loader"] = "forge"
project.extra["supported_loaders"] = "forge"

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
    accessConverter.register("main") {
        add("moderner_beta.accesswidener")
    }

//    mixins.create("main") {
//        mixin("default", "moderner_beta-forgelike.mixins.json")
//    }
}

mixin {
    add(sourceSets.main.get(), "moderner_beta.mixin.refmap.json")

    config("moderner_beta-common.mixin.json")
    config("moderner_beta-forgelike.mixin.json")
}

legacyForge {
    val at = project.file("build/resources/main/META-INF/accesstransformer.cfg");

    accessTransformers.from(at.absolutePath)
    validateAccessTransformers = true

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

tasks {
    jar {
        finalizedBy("reobfJar")

        manifest.attributes(mapOf(
            "MixinConfigs" to "moderner_beta-common.mixins.json,moderner_beta-forgelike.mixins.json"
        ))
    }
}