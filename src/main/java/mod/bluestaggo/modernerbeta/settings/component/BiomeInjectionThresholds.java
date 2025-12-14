package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;

public record BiomeInjectionThresholds(
    int oceanDepth,
    int deepOceanDepth,
    int caveDepth
) {
    public static final Codec<BiomeInjectionThresholds> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.INT.fieldOf("oceanDepth").orElse(4).forGetter(BiomeInjectionThresholds::oceanDepth),
            Codec.INT.fieldOf("deepOceanDepth").orElse(16).forGetter(BiomeInjectionThresholds::deepOceanDepth),
            Codec.INT.fieldOf("caveDepth").orElse(8).forGetter(BiomeInjectionThresholds::caveDepth)
        ).apply(instance, BiomeInjectionThresholds::new)
    );
    public static final BiomeInjectionThresholds DEFAULT = CodecUtil.getDefaultByMap(CODEC);
}
