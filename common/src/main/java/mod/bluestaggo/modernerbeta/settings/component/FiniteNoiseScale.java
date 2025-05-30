package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;

public record FiniteNoiseScale(
    float heightScale,
    float selectorScale,
    float minHeightDamp,
    float minHeightBoost,
    float maxHeightDamp,
    float maxHeightBoost,
    int mainHeightOctaves,
    float heightUnderDamp
) {
    public static final Codec<FiniteNoiseScale> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.FLOAT.fieldOf("heightScale").orElse(1.3f).forGetter(FiniteNoiseScale::heightScale),
            Codec.FLOAT.fieldOf("selectorScale").orElse(1.0f).forGetter(FiniteNoiseScale::selectorScale),
            Codec.FLOAT.fieldOf("minHeightDamp").orElse(6.0f).forGetter(FiniteNoiseScale::minHeightDamp),
            Codec.FLOAT.fieldOf("minHeightBoost").orElse(-4.0f).forGetter(FiniteNoiseScale::minHeightBoost),
            Codec.FLOAT.fieldOf("maxHeightDamp").orElse(5.0f).forGetter(FiniteNoiseScale::maxHeightDamp),
            Codec.FLOAT.fieldOf("maxHeightBoost").orElse(6.0f).forGetter(FiniteNoiseScale::maxHeightBoost),
            Codec.INT.fieldOf("mainHeightOctaves").orElse(6).forGetter(FiniteNoiseScale::mainHeightOctaves),
            Codec.FLOAT.fieldOf("heightUnderDamp").orElse(1.25f).forGetter(FiniteNoiseScale::heightUnderDamp)
        ).apply(instance, FiniteNoiseScale::new)
    );
    public static final FiniteNoiseScale DEFAULT = CodecUtil.getDefaultByMap(CODEC);
}
