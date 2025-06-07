package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;

public record ClimateDistribution(
    boolean fuzzyGrass,
    boolean smoothBorders
) {
    public static Codec<ClimateDistribution> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.BOOL.fieldOf("fuzzyGrass").orElse(true).forGetter(ClimateDistribution::fuzzyGrass),
            Codec.BOOL.fieldOf("smoothBorders").orElse(false).forGetter(ClimateDistribution::smoothBorders)
        ).apply(instance, ClimateDistribution::new)
    );
    public static final ClimateDistribution DEFAULT = CodecUtil.getDefaultByMap(CODEC);

    public static final ClimateDistribution BETA = new ClimateDistribution(true, false);
    public static final ClimateDistribution RELEASE_1_0 = new ClimateDistribution(false, false);
    public static final ClimateDistribution RELEASE_1_1 = new ClimateDistribution(false, true);
}
