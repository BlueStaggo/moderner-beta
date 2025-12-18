plugins {
    id("multiloader-common")
    id("net.fabricmc.fabric-loom")
}

loom {
    mixin {
        useLegacyMixinAp = false
    }

    accessWidenerPath = getRootProject().file("src/main/resources/moderner_beta.accesswidener")

    decompilers {
        named("vineflower") { // Adds names to lambdas - useful for mixins
            options.put("mark-corresponding-synthetics", "1")
        }
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