plugins {
    `multiloader-loader`
    id("net.fabricmc.fabric-loom")
}

loom {
    accessWidenerPath = getRootProject().file("src/main/resources/moderner_beta.accesswidener")

    runConfigs.all {
        ideConfigGenerated(true)
        runDir = "../../../run"
    }

    runs {
        register("datagen") {
            server()
            name("Data Generation")
            vmArg("-Dfabric-api.datagen")
            vmArg("-Dfabric-api.datagen.output-dir=${commonProject.file("src/main/generated")}")
            vmArg("-Dfabric-api.datagen.modid=moderner_beta")
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

    modImplementation("net.fabricmc:fabric-loader:${commonMod.prop("fabric_loader_version")}")
    modApi("net.fabricmc.fabric-api:fabric-api:${commonMod.prop("fabric_api_version")}")

    if (commonMod.prop("mod_menu_supported").toBoolean()) {
        modImplementation("com.terraformersmc:modmenu:${commonMod.prop("mod_menu_version")}")
    } else {
        modCompileOnly("com.terraformersmc:modmenu:${commonMod.prop("mod_menu_version")}")
    }
}

