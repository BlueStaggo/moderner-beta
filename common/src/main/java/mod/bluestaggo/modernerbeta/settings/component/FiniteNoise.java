package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;

public record FiniteNoise(
    float heightNoiseScale,
    float selectorScale,
    float minHeightDamp,
    float minHeightBoost,
    float maxHeightDamp,
    float maxHeightBoost,
    int selectorOctaves,
    float heightUnderDamp
) {
    public static final Codec<FiniteNoise> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.FLOAT.fieldOf("heightNoiseScale").orElse(1.3f).forGetter(FiniteNoise::heightNoiseScale),
            Codec.FLOAT.fieldOf("selectorScale").orElse(1.0f).forGetter(FiniteNoise::selectorScale),
            Codec.FLOAT.fieldOf("minHeightDamp").orElse(6.0f).forGetter(FiniteNoise::minHeightDamp),
            Codec.FLOAT.fieldOf("minHeightBoost").orElse(-4.0f).forGetter(FiniteNoise::minHeightBoost),
            Codec.FLOAT.fieldOf("maxHeightDamp").orElse(5.0f).forGetter(FiniteNoise::maxHeightDamp),
            Codec.FLOAT.fieldOf("maxHeightBoost").orElse(6.0f).forGetter(FiniteNoise::maxHeightBoost),
            Codec.INT.fieldOf("selectorOctaves").orElse(6).forGetter(FiniteNoise::selectorOctaves),
            Codec.FLOAT.fieldOf("heightUnderDamp").orElse(1.25f).forGetter(FiniteNoise::heightUnderDamp)
        ).apply(instance, FiniteNoise::new)
    );
    public static final FiniteNoise DEFAULT = CodecUtil.getDefaultByMap(CODEC);
}
