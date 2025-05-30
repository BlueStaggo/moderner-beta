package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;

public record NoiseScale(
    float coordinate,
    float height,
    float upperLimit,
    float lowerLimit,
    float depthNoiseX,
    float depthNoiseZ,
    float mainNoiseX,
    float mainNoiseY,
    float mainNoiseZ,
    float baseSize,
    float stretchY
) {
    public static final Codec<NoiseScale> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.FLOAT.fieldOf("coordinate").orElse(684.412f).forGetter(NoiseScale::coordinate),
            Codec.FLOAT.fieldOf("height").orElse(684.412f).forGetter(NoiseScale::height),
            Codec.FLOAT.fieldOf("upperLimit").orElse(512f).forGetter(NoiseScale::upperLimit),
            Codec.FLOAT.fieldOf("lowerLimit").orElse(512f).forGetter(NoiseScale::lowerLimit),
            Codec.FLOAT.fieldOf("depthNoiseX").orElse(200f).forGetter(NoiseScale::depthNoiseX),
            Codec.FLOAT.fieldOf("depthNoiseZ").orElse(200f).forGetter(NoiseScale::depthNoiseZ),
            Codec.FLOAT.fieldOf("mainNoiseX").orElse(80f).forGetter(NoiseScale::mainNoiseX),
            Codec.FLOAT.fieldOf("mainNoiseY").orElse(160f).forGetter(NoiseScale::mainNoiseY),
            Codec.FLOAT.fieldOf("mainNoiseZ").orElse(80f).forGetter(NoiseScale::mainNoiseZ),
            Codec.FLOAT.fieldOf("baseSize").orElse(8.5f).forGetter(NoiseScale::baseSize),
            Codec.FLOAT.fieldOf("stretchY").orElse(12.0f).forGetter(NoiseScale::stretchY)
        ).apply(instance, NoiseScale::new)
    );
    public static final NoiseScale DEFAULT = CodecUtil.getDefaultByMap(CODEC);
}
