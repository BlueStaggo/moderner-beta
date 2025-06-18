package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ClimaticBiomeColors(
    boolean sky,
    boolean vegetation,
    boolean water
) {
    public static final Codec<ClimaticBiomeColors> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.BOOL.fieldOf("sky").orElse(false).forGetter(ClimaticBiomeColors::sky),
            Codec.BOOL.fieldOf("vegetation").orElse(false).forGetter(ClimaticBiomeColors::vegetation),
            Codec.BOOL.fieldOf("water").orElse(false).forGetter(ClimaticBiomeColors::water)
        ).apply(instance, ClimaticBiomeColors::new)
    );
}
