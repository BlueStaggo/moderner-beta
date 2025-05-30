package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;

public record NoiseSlide(
    int topTarget,
    int topSize,
    int topOffset,
    int bottomTarget,
    int bottomSize,
    int bottomOffset
) {
    public static final Codec<NoiseSlide> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.INT.fieldOf("topTarget").orElse(-10).forGetter(NoiseSlide::topTarget),
            Codec.INT.fieldOf("topSize").orElse(3).forGetter(NoiseSlide::topSize),
            Codec.INT.fieldOf("topOffset").orElse(0).forGetter(NoiseSlide::topOffset),
            Codec.INT.fieldOf("bottomTarget").orElse(15).forGetter(NoiseSlide::bottomTarget),
            Codec.INT.fieldOf("bottomSize").orElse(3).forGetter(NoiseSlide::bottomSize),
            Codec.INT.fieldOf("bottomOffset").orElse(0).forGetter(NoiseSlide::bottomOffset)
        ).apply(instance, NoiseSlide::new)
    );
    public static final NoiseSlide DEFAULT = CodecUtil.getDefaultByMap(CODEC);
    public static final NoiseSlide DISABLED = new NoiseSlide(0, 0, 0, 0, 0, 0);
}
