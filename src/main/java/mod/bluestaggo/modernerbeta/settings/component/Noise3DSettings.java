package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;

public record Noise3DSettings(
    boolean monoliths,
    boolean farlands,
    boolean alphaNoiseSampling,
    boolean climateHeightScaling,
    boolean randomNoiseOffsets,
    boolean arraySurfaceNoise,
    boolean simplexSurfaceNoise
) {
    public static final Codec<Noise3DSettings> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.BOOL.fieldOf("monoliths").orElse(false).forGetter(Noise3DSettings::monoliths),
            Codec.BOOL.fieldOf("farlands").orElse(true).forGetter(Noise3DSettings::farlands),
            Codec.BOOL.fieldOf("alphaNoiseSampling").orElse(false).forGetter(Noise3DSettings::alphaNoiseSampling),
            Codec.BOOL.fieldOf("climateHeightScaling").orElse(true).forGetter(Noise3DSettings::climateHeightScaling),
            Codec.BOOL.fieldOf("randomNoiseOffsets").orElse(true).forGetter(Noise3DSettings::randomNoiseOffsets),
            Codec.BOOL.fieldOf("arraySurfaceNoise").orElse(true).forGetter(Noise3DSettings::arraySurfaceNoise),
            Codec.BOOL.fieldOf("simplexSurfaceNoise").orElse(false).forGetter(Noise3DSettings::simplexSurfaceNoise)
        ).apply(instance, Noise3DSettings::new)
    );
    public static final Noise3DSettings DEFAULT = CodecUtil.getDefaultByMap(CODEC);

    public static final Noise3DSettings INFDEV611 = new Noise3DSettings(
        true,
        true,
        true,
        false,
        true,
        false,
        false
    );
    public static final Noise3DSettings ALPHA = new Noise3DSettings(
        true,
        true,
        true,
        false,
        true,
        true,
        false
    );
    public static final Noise3DSettings BETA = new Noise3DSettings(
        false,
        true,
        false,
        true,
        true,
        true,
        false
    );
    public static final Noise3DSettings EARLY_RELEASE = new Noise3DSettings(
        false,
        false,
        false,
        false,
        true,
        true,
        false
    );
    public static final Noise3DSettings MAJOR_RELEASE = new Noise3DSettings(
        false,
        false,
        false,
        false,
        true,
        false,
        true
    );

    public boolean wrapped() {
        return !this.farlands();
    }
}
