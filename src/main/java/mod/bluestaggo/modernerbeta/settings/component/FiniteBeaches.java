package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;

public record FiniteBeaches(
    float sandThreshold,
    boolean sandUnderAir,
    boolean sandUnderFluid,
    float gravelThreshold,
    boolean gravelUnderAir,
    boolean gravelUnderFluid,
    boolean prioritizeGravelBeaches
) {
    public static final Codec<FiniteBeaches> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.FLOAT.fieldOf("sandThreshold").orElse(8.0f).forGetter(FiniteBeaches::sandThreshold),
            Codec.BOOL.fieldOf("sandUnderAir").orElse(true).forGetter(FiniteBeaches::sandUnderAir),
            Codec.BOOL.fieldOf("sandUnderFluid").orElse(false).forGetter(FiniteBeaches::sandUnderFluid),
            Codec.FLOAT.fieldOf("gravelThreshold").orElse(12.0f).forGetter(FiniteBeaches::gravelThreshold),
            Codec.BOOL.fieldOf("gravelUnderAir").orElse(true).forGetter(FiniteBeaches::gravelUnderAir),
            Codec.BOOL.fieldOf("gravelUnderFluid").orElse(true).forGetter(FiniteBeaches::gravelUnderFluid),
            Codec.BOOL.fieldOf("prioritizeGravelBeaches").orElse(false).forGetter(FiniteBeaches::prioritizeGravelBeaches)
        ).apply(instance, FiniteBeaches::new)
    );
    public static final FiniteBeaches DEFAULT = CodecUtil.getDefaultByMap(CODEC);
}
