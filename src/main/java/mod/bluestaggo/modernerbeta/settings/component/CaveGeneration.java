package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;

public record CaveGeneration(
    boolean useCarvers,
    boolean useNoiseCaves,
    boolean fixCaveBorders,
    boolean forceBetaCaves,
    boolean forceBetaCanyons
) {
    public static final Codec<CaveGeneration> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.BOOL.fieldOf("useCarvers").orElse(true).forGetter(CaveGeneration::useCarvers),
            Codec.BOOL.fieldOf("useNoiseCaves").orElse(false).forGetter(CaveGeneration::useNoiseCaves),
            Codec.BOOL.fieldOf("fixCaveBorders").orElse(false).forGetter(CaveGeneration::fixCaveBorders),
            Codec.BOOL.fieldOf("forceBetaCaves").orElse(true).forGetter(CaveGeneration::forceBetaCaves),
            Codec.BOOL.fieldOf("forceBetaCanyons").orElse(true).forGetter(CaveGeneration::forceBetaCanyons)
        ).apply(instance, CaveGeneration::new)
    );
    public static final CaveGeneration DEFAULT = CodecUtil.getDefaultByMap(CODEC);

    public static final CaveGeneration DISABLED = new CaveGeneration(false, false, false, true, true);
    public static final CaveGeneration BETA = new CaveGeneration(true, false, false, true, true);
    public static final CaveGeneration EARLY_RELEASE = new CaveGeneration(true, false, true, true, true);
    public static final CaveGeneration MAJOR_RELEASE = new CaveGeneration(true, false, true, false, false);
    public static final CaveGeneration MODERN_BETA = new CaveGeneration(true, true, true, true, true);
}
