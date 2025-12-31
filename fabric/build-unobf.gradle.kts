plugins {
    `multiloader-loader`
    id("net.fabricmc.fabric-loom")
    kotlin("jvm")
    id("com.google.devtools.ksp")
    id("dev.kikugie.fletching-table")
}

loom {
    accessWidenerPath = stonecutter.process(commonProject.file("../../src/main/resources/moderner_beta.accesswidener"), "build/dev.aw")

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

modrinth {
    dependencies {
        required.project("fabric-api")
    }
    uploadFile.set(tasks.jar)
}