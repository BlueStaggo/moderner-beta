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
    float stretchY,
    float densityUnderdamp,
    float limitBlending,
    boolean useFixedOffset,
    float fixedOffset,
    int forestNoiseOctaves
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
            Codec.FLOAT.fieldOf("stretchY").orElse(12.0f).forGetter(NoiseScale::stretchY),
            Codec.FLOAT.fieldOf("densityUnderdamp").orElse(4.0f).forGetter(NoiseScale::densityUnderdamp),
            Codec.FLOAT.fieldOf("limitBlending").orElse(10.0f).forGetter(NoiseScale::limitBlending),
            Codec.BOOL.fieldOf("useFixedOffset").orElse(false).forGetter(NoiseScale::useFixedOffset),
            Codec.FLOAT.fieldOf("fixedOffset").orElse(1.0f).forGetter(NoiseScale::fixedOffset),
            Codec.INT.fieldOf("forestNoiseOctaves").orElse(8).forGetter(NoiseScale::forestNoiseOctaves)
        ).apply(instance, NoiseScale::new)
    );
    public static final NoiseScale DEFAULT = CodecUtil.getDefaultByMap(CODEC);

    public static final NoiseScale INFDEV_415 = new NoiseScale(
        684.412f,
        984.412f,
        512f,
        512f,
        100f,
        100f,
        80f,
        575.332986564f,
        80f,
        16.0f,
        4.0f,
        3.0f,
        1.0f,
        false,
        1.0f,
        5
    );
    public static final NoiseScale INFDEV_420 = new NoiseScale(
        684.412f,
        684.412f,
        512f,
        512f,
        100f,
        100f,
        80f,
        160f,
        80f,
        8.5f,
        12.0f,
        2.0f,
        10.0f,
        false,
        1.0f,
        5
    );
    public static final NoiseScale ALPHA = new NoiseScale(
        684.412f,
        684.412f,
        512f,
        512f,
        100f,
        100f,
        80f,
        160f,
        80f,
        8.5f,
        12.0f,
        4.0f,
        10.0f,
        false,
        1.0f,
        8
    );
    public static final NoiseScale SKYLANDS = new NoiseScale(
        1368.824f,
        684.412f,
        512f,
        512f,
        100f,
        100f,
        80f,
        160f,
        80f,
        8.5f,
        8.0f,
        -1.0f,
        10.0f,
        true,
        8.0f,
        8
    );

    public boolean farlands() {
        return false;
    }
}
