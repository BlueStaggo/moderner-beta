plugins {
    `multiloader-loader`
    id("net.fabricmc.fabric-loom-remap")
    kotlin("jvm")
    id("com.google.devtools.ksp")
    id("dev.kikugie.fletching-table")
}

loom {
    accessWidenerPath = stonecutter.process(commonProject.file("../../src/main/resources/moderner_beta.accesswidener"), "build/dev.aw")

    runConfigs.all {
        generateRunConfig = true
        runDirectory.set(project.file("../../../run"))
    }

    runs {
        register("datagen") {
            server()
            displayName = "Data Generation"
            jvmArguments.add("-Dfabric-api.datagen")
            jvmArguments.add("-Dfabric-api.datagen.output-dir=${commonProject.file("src/main/generated")}")
            jvmArguments.add("-Dfabric-api.datagen.modid=moderner_beta")
        }
    }
}

fletchingTable {
    mixins.create("main") {
        mixin("default", "moderner_beta-fabric.mixins.json")
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

    modImplementation("net.fabricmc:fabric-loader:${commonMod.prop("fabric_loader_version")}")
    modApi("net.fabricmc.fabric-api:fabric-api:${commonMod.prop("fabric_api_version")}")

    if (commonMod.prop("mod_menu_supported").toBoolean()) {
        modImplementation("com.terraformersmc:modmenu:${commonMod.prop("mod_menu_version")}")
    } else {
        modCompileOnly("com.terraformersmc:modmenu:${commonMod.prop("mod_menu_version")}")
    }
}

tasks {
    processResources {
        exclude("assets/moderner_beta/banner.png")
    }

    remapJar {
        destinationDirectory = rootProject.layout.buildDirectory.dir("libs/$loader")
    }
}

publishMods {
    file.set(tasks.remapJar.get().archiveFile)

    modrinth {
        requires("fabric-api")
    }

    curseforge {
        requires("fabric-api")
    }
}