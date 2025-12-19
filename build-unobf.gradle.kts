plugins {
    id("multiloader-common")
    id("net.fabricmc.fabric-loom")
    kotlin("jvm")
    id("com.google.devtools.ksp")
    id("dev.kikugie.fletching-table")
}

loom {
    mixin {
        useLegacyMixinAp = false
    }

    accessWidenerPath = file("../../src/main/resources/moderner_beta.accesswidener")

    decompilers {
        named("vineflower") { // Adds names to lambdas - useful for mixins
            options.put("mark-corresponding-synthetics", "1")
        }
    }
}

fletchingTable {
    mixins.create("main") {
        mixin("default", "moderner_beta-common.mixins.json") {
            env("DEFAULT")
            env("CLIENT", "mod.bluestaggo.modernerbeta.mixin.client")
            env("SERVER", "mod.bluestaggo.modernerbeta.mixin.server")
        }
    }

    j52j.register("main") {
        extension("mcmeta", "resourcepacks/*/pack.json5")
        extension("json", "resourcepacks/*/data/**/*.json5")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${commonMod.mc}")

    modCompileOnly("net.fabricmc:fabric-loader:${commonMod.propOrNull("fabric_loader_version")}")
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