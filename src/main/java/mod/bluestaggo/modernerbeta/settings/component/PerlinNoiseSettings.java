package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;

public record PerlinNoiseSettings(
    boolean wrapped,
    boolean randomNoiseOffsets,
    boolean alpha2DSampling,
    boolean infdevNoiseScaling,
    int failurePoint
) {
    public static final Codec<PerlinNoiseSettings> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.BOOL.fieldOf("wrapped").orElse(false).forGetter(PerlinNoiseSettings::wrapped),
            Codec.BOOL.fieldOf("randomNoiseOffsets").orElse(true).forGetter(PerlinNoiseSettings::randomNoiseOffsets),
            Codec.BOOL.fieldOf("alpha2DSampling").orElse(false).forGetter(PerlinNoiseSettings::alpha2DSampling),
            Codec.BOOL.fieldOf("infdevNoiseScaling").orElse(false).forGetter(PerlinNoiseSettings::infdevNoiseScaling),
            Codec.INT.fieldOf("failurePoint").orElse(Integer.MAX_VALUE).forGetter(PerlinNoiseSettings::failurePoint)
        ).apply(instance, PerlinNoiseSettings::new)
    );

    public static final PerlinNoiseSettings DEFAULT = CodecUtil.getDefaultByMap(CODEC);

    public static final PerlinNoiseSettings NO_OFFSETS = new PerlinNoiseSettings(
        false,
        false,
        false,
        false,
        Integer.MAX_VALUE
    );

    public static final PerlinNoiseSettings INFDEV_415 = new PerlinNoiseSettings(
        false,
        true,
        true,
        true,
        Integer.MAX_VALUE
    );

    public static final PerlinNoiseSettings ALPHA = new PerlinNoiseSettings(
        false,
        true,
        true,
        false,
        Integer.MAX_VALUE
    );

    public static final PerlinNoiseSettings RELEASE = new PerlinNoiseSettings(
        true,
        true,
        false,
        false,
        Integer.MAX_VALUE
    );
}
