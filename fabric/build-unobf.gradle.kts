plugins {
    `multiloader-loader`
    id("net.fabricmc.fabric-loom")
    alias(ft.plugins.mixin)
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
    mixins.configure(sourceSets.main) {
        mixin("moderner_beta-fabric.mixins.json", "default")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${commonMod.mc}")

    implementation("net.fabricmc:fabric-loader:${commonMod.prop("fabric_loader_version")}")
    api("net.fabricmc.fabric-api:fabric-api:${commonMod.prop("fabric_api_version")}")

    if (commonMod.prop("mod_menu_supported").toBoolean()) {
        implementation("com.terraformersmc:modmenu:${commonMod.prop("mod_menu_version")}")
    } else {
        compileOnly("com.terraformersmc:modmenu:${commonMod.prop("mod_menu_version")}")
    }
}

tasks.processResources {
    exclude("assets/moderner_beta/banner.png")
}

publishMods {
    file.set(tasks.jar.get().archiveFile)
    modrinth {
        requires("fabric-api")
    }

    curseforge {
        requires("fabric-api")
    }
}