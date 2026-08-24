plugins {
    id("multiloader-common")
    id("net.fabricmc.fabric-loom-remap")
    alias(ft.plugins.mixin)
}

loom {
    mixin {
        useLegacyMixinAp = false
    }

    accessWidenerPath = stonecutter.process(file("../../src/main/resources/moderner_beta.accesswidener"), "build/dev.aw")

    decompilers {
        named("vineflower") { // Adds names to lambdas - useful for mixins
            options.put("mark-corresponding-synthetics", "1")
        }
    }
}

fletchingTable {
    mixins.configure(sourceSets.main) {
        mixin("moderner_beta-common.mixins.json", "default") {
            env("MAIN")
            env("CLIENT", "mod.bluestaggo.modernerbeta.mixin.client")
            env("SERVER", "mod.bluestaggo.modernerbeta.mixin.server")
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${commonMod.mc}")
    mappings(loom.layered {
        officialMojangMappings()
        commonMod.propOrNull("parchment_mappings")?.let { parchmentVersion ->
            parchment("org.parchmentmc.data:parchment-$parchmentVersion@zip")
        }
    })

    modCompileOnly("net.fabricmc:fabric-loader:${commonMod.prop("fabric_loader_version")}")
}

val commonJava: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

val commonResources: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

artifacts {
    afterEvaluate {
        val mainSourceSet = sourceSets.main.get()
        mainSourceSet.java.sourceDirectories.files.forEach {
            add(commonJava.name, it)
        }
        mainSourceSet.resources.sourceDirectories.files.forEach {
            add(commonResources.name, it)
        }
    }
}