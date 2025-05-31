package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.world.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;

import java.util.Map;

public record ForcedBiomeHeight(
    Map<ExtendedBiomeId, HeightConfig> heightOverrides,
    float depthWeight,
    float depthOffset,
    float scaleWeight,
    float scaleOffset
) {
    public static final Codec<ForcedBiomeHeight> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.unboundedMap(ExtendedBiomeId.CODEC, HeightConfig.CODEC).fieldOf("heightOverrides").orElse(Map.of()).forGetter(ForcedBiomeHeight::heightOverrides),
            Codec.FLOAT.fieldOf("depthWeight").orElse(1.0f).forGetter(ForcedBiomeHeight::depthWeight),
            Codec.FLOAT.fieldOf("depthOffset").orElse(0.0f).forGetter(ForcedBiomeHeight::depthOffset),
            Codec.FLOAT.fieldOf("scaleWeight").orElse(1.0f).forGetter(ForcedBiomeHeight::scaleWeight),
            Codec.FLOAT.fieldOf("scaleOffset").orElse(0.0f).forGetter(ForcedBiomeHeight::scaleOffset)
        ).apply(instance, ForcedBiomeHeight::new)
    );
    public static final ForcedBiomeHeight DEFAULT = CodecUtil.getDefaultByMap(CODEC);

    public static ForcedBiomeHeight overridesOnly(Map<ExtendedBiomeId, HeightConfig> heightOverrides) {
        return new ForcedBiomeHeight(heightOverrides, 1.0f, 0.0f, 1.0f, 0.0f);
    }
}
