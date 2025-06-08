package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;

public record CaveGeneration(
    boolean useCaves,
    boolean useFixedCaves,
    boolean useNoiseCaves,
    boolean forceBetaCaves,
    boolean forceBetaRavines
) {
    public static final Codec<CaveGeneration> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.BOOL.fieldOf("useCaves").orElse(true).forGetter(CaveGeneration::useCaves),
            Codec.BOOL.fieldOf("useFixedCaves").orElse(false).forGetter(CaveGeneration::useFixedCaves),
            Codec.BOOL.fieldOf("useNoiseCaves").orElse(false).forGetter(CaveGeneration::useNoiseCaves),
            Codec.BOOL.fieldOf("forceBetaCaves").orElse(true).forGetter(CaveGeneration::forceBetaCaves),
            Codec.BOOL.fieldOf("forceBetaRavines").orElse(true).forGetter(CaveGeneration::forceBetaRavines)
        ).apply(instance, CaveGeneration::new)
    );
    public static final CaveGeneration DEFAULT = CodecUtil.getDefaultByMap(CODEC);

    public static final CaveGeneration DISABLED = new CaveGeneration(false, false, false, true, true);
    public static final CaveGeneration BETA = new CaveGeneration(true, false, false, true, true);
    public static final CaveGeneration EARLY_RELEASE = new CaveGeneration(true, true, false, true, true);
    public static final CaveGeneration MAJOR_RELEASE = new CaveGeneration(true, true, false, false, false);
    public static final CaveGeneration MODERN_BETA = new CaveGeneration(true, true, true, true, true);
}
