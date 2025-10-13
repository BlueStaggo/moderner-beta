plugins {
    id("dev.kikugie.stonecutter")
    id("dev.architectury.loom").apply(false)
}

plugins.apply("dev.kikugie.stonecutter")
stonecutter.active("1.21.6")

stonecutter.parameters {
    replacements {
        string {
            direction = eval(current.version, "<1.21.5")
            replace("WeightedList", "SimpleWeightedRandomList")
        }

        string("registryOr") {
            direction = eval(current.version, "<1.21.2")
            replace(".lookupOrThrow(ModernBetaRegistryKeys.", ".registryOrThrow(ModernBetaRegistryKeys.")
        }

        string("registryOr") {
            direction = eval(current.version, "<1.21.2")
            replace(".lookupOrThrow(Registries.", ".registryOrThrow(Registries.")
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

        string {
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
            direction = eval(current.version, "<1.20.5")
            replace("com.mojang.serialization.MapCodec<", "com.mojang.serialization./*Map*/Codec<")
        }

        string {
            direction = eval(current.version, "<1.20.5")
            replace("com.mojang.serialization.MapCodec.", "com.mojang.serialization./*Map*/Codec.")
        }
    }
}
