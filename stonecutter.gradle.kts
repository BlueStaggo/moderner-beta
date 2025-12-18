plugins {
    id("dev.kikugie.stonecutter")
}

plugins.apply("dev.kikugie.stonecutter")
stonecutter.active("1.21.6")

stonecutter.tasks {
    order("runDatagen", filter = { this.branch.id == "fabric" })
    order("build")
//    order("modrinth", filter = { this.branch.id == "fabric" || this.branch.id == "forgelike" })
}

stonecutter.parameters {
    replacements {
        string {
            direction = eval(current.version, "<1.21.5")
            replace("WeightedList.codec", "SimpleWeightedRandomList.wrappedCodec")
        }

        string {
            direction = eval(current.version, "<1.21.5")
            replace("WeightedList.of", "SimpleWeightedRandomList.create")
        }

        string {
            direction = eval(current.version, "<1.21.5")
            replace("WeightedList", "SimpleWeightedRandomList")
        }

        string("registryOr") {
            direction = eval(current.version, "<1.21.2")
            replace(".lookupOrThrow(ModernBetaResourceKeys.", ".registryOrThrow(ModernBetaResourceKeys.")
        }

        string("registryOr") {
            direction = eval(current.version, "<1.21.2")
            replace(".lookupOrThrow(Registries.", ".registryOrThrow(Registries.")
        }

        string("registryGet") {
            direction = eval(current.version, "<1.21.2")
            replace(".getValue(", ".get(")
        }

        string {
            direction = eval(current.version, ">=1.21.6")
            replace("this.getOrCreateTagBuilder(", "this.builder(")
        }

        string {
            direction = eval(current.version, "<1.20.5")
            replace("BootstrapContext", "BootstapContext")
        }

        string {
            direction = eval(current.version, "<1.21.2")
            replace(".getMinSectionY()", ".getMinSection()")
        }

        string {
            direction = eval(current.version, "<1.21.2")
            replace(".getMaxSectionY()", ".getMaxSection()")
        }

        string("minBuild") {
            direction = eval(current.version, "<1.21.2")
            replace(".getMinY()", ".getMinBuildHeight()")
        }

        string {
            direction = eval(current.version, "<1.21.2")
            replace(".listElements()", ".holders()")
        }

        string {
            direction = eval(current.version, "<1.21")
            replace("gui.screens.options.OptionsSubScreen", "gui.screens.OptionsSubScreen")
        }

        string {
            direction = eval(current.version, "<1.20.3")
            replace("net.minecraft.network.chat.ComponentSerialization.CODEC", "net.minecraft.util.ExtraCodecs.COMPONENT")
        }

        string {
            direction = eval(current.version, "<1.20.5")
            replace("com.mojang.serialization.MapCodec<", "com.mojang.serialization./*Map*/Codec<")
        }

        string {
            direction = eval(current.version, "<1.20.5")
            replace("com.mojang.serialization.MapCodec.", "com.mojang.serialization./*Map*/Codec.")
        }

        string {
            direction = eval(current.version, ">=1.21.11")
            replace("ResourceLocation", "Identifier")
        }

        string {
            direction = eval(current.version, ">=1.21.11")
            replace("ResourceKey::location", "ResourceKey::identifier")
        }

        string("dotLocation") {
            direction = eval(current.version, ">=1.21.11")
            replace(".location()", ".identifier()")
        }

        string {
            direction = eval(current.version, ">=1.21.11")
            replace("net.minecraft.Util", "net.minecraft.util.Util")
        }
    }
}
