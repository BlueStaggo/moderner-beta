package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;

public record FiniteCaveGeneration(
    boolean useCaves,
    boolean use14aCaves,
    int rarity,
    float radius,
    float length
) {
    public static final Codec<FiniteCaveGeneration> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.BOOL.fieldOf("useCaves").orElse(true).forGetter(FiniteCaveGeneration::useCaves),
            Codec.BOOL.fieldOf("use14aCaves").orElse(false).forGetter(FiniteCaveGeneration::use14aCaves),
            Codec.INT.fieldOf("rarity").orElse(8192).forGetter(FiniteCaveGeneration::rarity),
            Codec.FLOAT.fieldOf("radius").orElse(1.0f).forGetter(FiniteCaveGeneration::radius),
            Codec.FLOAT.fieldOf("length").orElse(200.0f).forGetter(FiniteCaveGeneration::length)
        ).apply(instance, FiniteCaveGeneration::new)
    );
    public static final FiniteCaveGeneration DEFAULT = CodecUtil.getDefaultByMap(CODEC);
}
