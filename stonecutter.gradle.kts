plugins {
    id("dev.kikugie.stonecutter")
    id("dev.architectury.loom").apply(false)
}

plugins.apply("dev.kikugie.stonecutter")
stonecutter.active("1.21.6")

stonecutter.parameters {
    replacements {
        regex {
            direction = eval(metadata.version, "<1.21.5")
            replace("""\bPool\b""", "DataPool")
            reverse("""\bDataPool\b""", "Pool")
            phase = "LAST"
        }

        string {
            direction = eval(metadata.version, "<1.21.2")
            replace(".getOrThrow(ModernBetaRegistryKeys.", ".get(ModernBetaRegistryKeys.")
            phase = "LAST"
        }

        string {
            direction = eval(metadata.version, "<1.21.2")
            replace(".getOrThrow(RegistryKeys.", ".get(RegistryKeys.")
            phase = "LAST"
        }

        string {
            direction = eval(metadata.version, ">=1.21.6")
            replace("this.getOrCreateTagBuilder(", "this.builder(")
            phase = "LAST"
        }

        string {
            direction = eval(metadata.version, "<1.20.5")
            replace("com.mojang.serialization.MapCodec<", "com.mojang.serialization./*Map*/Codec<")
            phase = "LAST"
        }

        string {
            direction = eval(metadata.version, "<1.20.5")
            replace("com.mojang.serialization.MapCodec.", "com.mojang.serialization./*Map*/Codec.")
            phase = "LAST"
        }
    }
}
