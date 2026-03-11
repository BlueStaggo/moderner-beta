plugins {
    id("dev.kikugie.stonecutter")

    (kotlin("jvm") version "2.3.0").apply(false)
    (id("com.google.devtools.ksp") version "2.3.3").apply(false)
    (id("dev.kikugie.fletching-table") version "0.1.0-alpha.22").apply(false)
    id("me.modmuss50.mod-publish-plugin")
}

plugins.apply("dev.kikugie.stonecutter")
stonecutter.active("1.21.6")

publishMods {
    @Suppress("LocalVariableName")
    val mod_version: String by extra
    changelog = rootProject.file("CHANGELOG.md").readText()
    type = STABLE

    github {
        accessToken = providers.environmentVariable("_GITHUB_TOKEN")
        displayName = mod_version
        version = mod_version
        repository = "Nostalgica-Reverie/moderner-beta"
        tagName = providers.environmentVariable("FORGEJO_REF_NAME")
        commitish = ""

        allowEmptyFiles = true
    }

    forgejo {
        accessToken = providers.environmentVariable("FORGEJO_TOKEN")
        host(uri("https://codeberg.org"))
        displayName = mod_version
        version = mod_version
        repository = "Nostalgica-Reverie/moderner-beta"
        tagName = providers.environmentVariable("FORGEJO_REF_NAME")
        commitish = ""

        allowEmptyFiles = true
    }
}

stonecutter.tasks {
    order("runDatagen", filter = { this.branch.id == "fabric" })
    order("build")
    order("publishModrinth", filter = { this.branch.id == "fabric" || this.branch.id == "forgelike" })
    order("publishCurseforge", filter = { this.branch.id == "fabric" || this.branch.id == "forgelike" })
}

stonecutter.parameters {
    replacements {
        string {
            direction = eval(current.version, "<1.20.3")
            replace("net.minecraft.network.chat.ComponentSerialization.CODEC", "net.minecraft.util.ExtraCodecs.COMPONENT")
        }

        string {
            direction = eval(current.version, "<1.20.5")
            replace("com.mojang.serialization.MapCodec<", "com.mojang.serialization./*Map*/Codec<")
            replace("com.mojang.serialization.MapCodec.", "com.mojang.serialization./*Map*/Codec.")
            replace("BootstrapContext", "BootstapContext")
        }

        string {
            direction = eval(current.version, "<1.21")
            replace("gui.screens.options.OptionsSubScreen", "gui.screens.OptionsSubScreen")
        }

        string("registryOr") {
            direction = eval(current.version, "<1.21.2")
            replace(".lookupOrThrow(ModernBetaResourceKeys.", ".registryOrThrow(ModernBetaResourceKeys.")
            replace(".lookupOrThrow(Registries.", ".registryOrThrow(Registries.")
        }

        string("registryGet") {
            direction = eval(current.version, "<1.21.2")
            replace(".getValue(", ".get(")
        }

        string {
            direction = eval(current.version, "<1.21.2")
            replace(".getMinSectionY()", ".getMinSection()")
            replace(".getMaxSectionY()", ".getMaxSection()")

            replace(".listElements()", ".holders()")
        }

        string("minBuild") {
            direction = eval(current.version, "<1.21.2")
            replace(".getMinY()", ".getMinBuildHeight()")
        }

        string {
            direction = eval(current.version, "<1.21.5")
            replace("WeightedList.codec", "SimpleWeightedRandomList.wrappedCodec")
            replace("WeightedList.of", "SimpleWeightedRandomList.create")
            replace("WeightedList", "SimpleWeightedRandomList")
        }

        string {
            direction = eval(current.version, ">=1.21.6")
            replace("this.getOrCreateTagBuilder(", "this.builder(")
        }

        string {
            direction = eval(current.version, ">=1.21.11")
            replace("ResourceLocation", "Identifier")
            replace("ResourceKey::location", "ResourceKey::identifier")
            replace("net.minecraft.Util", "net.minecraft.util.Util")
        }

        string("dotLocation") {
            direction = eval(current.version, ">=1.21.11")
            replace(".location()", ".identifier()")
        }

        string {
            direction = eval(current.version, ">=26.1")
            replace("accessWidener v1 named", "accessWidener v1 official")
            replace("ColorProviderRegistry.BLOCK", "BlockColorRegistry")
            replace("FabricDataOutput", "FabricPackOutput")
            replace("FabricTagProvider", "FabricTagsProvider")
            replace(".BlockTagProvider", ".BlockTagsProvider")
            replace(".playS2C()", ".clientboundPlay()")
            replace("ChunkPos.asLong", "ChunkPos.pack")
            replace("chunk.getPos().x", "chunk.getPos().x()")
            replace("chunk.getPos().z", "chunk.getPos().z()")
            replace("chunkPos.x", "chunkPos.x()")
            replace("chunkPos.z", "chunkPos.z()")
            replace("SnowyDirtBlock", "SnowyBlock")
            replace("net.minecraft.world.level.BlockAndTintGetter", "net.minecraft.client.renderer.block.BlockAndTintGetter")
            replace("DimensionType.CardinalLightType", "net.minecraft.world.level.CardinalLighting.Type")
            replace("net.minecraft.client.color.block.BlockColor", "net.minecraft.client.color.block.BlockTintSource")
            replace("GuiGraphics", "GuiGraphicsExtractor")
            replace(".drawString(", ".text(")
            replace(".drawCenteredString(", ".centeredText(")
            replace("renderWidget", "extractWidgetRenderState")
            replace("void render(", "void extractRenderState(")
            replace(".render(", ".extractRenderState(")
        }

        string("worldGenLevel") {
            direction = eval(current.version, ">=26.1")
            replace("LevelSimulatedReader", "WorldGenLevel")
            replace("setDirtAt", "placeBelowTrunkBlock")
        }
    }
}
