package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.level.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeId;

import java.util.Map;

public record ForcedBiomeHeight(
    boolean enabled,
    Map<ExtendedBiomeId, HeightConfig> heightOverrides,
    float depthWeight,
    float depthOffset,
    float scaleWeight,
    float scaleOffset,
    boolean modifyOnlyPositiveDepth
) {
    public static final Codec<ForcedBiomeHeight> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.BOOL.fieldOf("enabled").orElse(false).forGetter(ForcedBiomeHeight::enabled),
            Codec.unboundedMap(ExtendedBiomeId.CODEC, HeightConfig.CODEC).fieldOf("heightOverrides").orElse(Map.of()).forGetter(ForcedBiomeHeight::heightOverrides),
            Codec.FLOAT.fieldOf("depthWeight").orElse(1.0f).forGetter(ForcedBiomeHeight::depthWeight),
            Codec.FLOAT.fieldOf("depthOffset").orElse(0.0f).forGetter(ForcedBiomeHeight::depthOffset),
            Codec.FLOAT.fieldOf("scaleWeight").orElse(1.0f).forGetter(ForcedBiomeHeight::scaleWeight),
            Codec.FLOAT.fieldOf("scaleOffset").orElse(0.0f).forGetter(ForcedBiomeHeight::scaleOffset),
            Codec.BOOL.fieldOf("modifyOnlyPositiveDepth").orElse(false).forGetter(ForcedBiomeHeight::modifyOnlyPositiveDepth)
        ).apply(instance, ForcedBiomeHeight::new)
    );
    public static final ForcedBiomeHeight DEFAULT = CodecUtil.getDefaultByMap(CODEC);
    public static final ForcedBiomeHeight ENABLED = new ForcedBiomeHeight(true, Map.of(), 1.0f, 1.0f, 1.0f, 1.0f, false);
    public static final ForcedBiomeHeight AMPLIFIED = new ForcedBiomeHeight(true, Map.of(), 2.0f, 1.0f, 4.0f, 1.0f, true);

    public static ForcedBiomeHeight overridesOnly(Map<ExtendedBiomeId, HeightConfig> heightOverrides) {
        return overridesOnly(heightOverrides, false);
    }

    public static ForcedBiomeHeight overridesOnly(Map<ExtendedBiomeId, HeightConfig> heightOverrides, boolean amplified) {
        return new ForcedBiomeHeight(
            true,
            heightOverrides,
            amplified ? 2.0f : 1.0f,
            amplified ? 1.0f : 0.0f,
            amplified ? 4.0f : 1.0f,
            amplified ? 1.0f : 0.0f,
            amplified
        );
    }
}
