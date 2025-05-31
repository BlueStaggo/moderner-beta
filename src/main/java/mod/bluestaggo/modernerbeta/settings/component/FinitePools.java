package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;

public record FinitePools(
    int waterRarity,
    int lavaRarity,
    boolean uniformLavaHeights
) {
    public static final Codec<FinitePools> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.INT.fieldOf("waterRarity").orElse(8000).forGetter(FinitePools::waterRarity),
            Codec.INT.fieldOf("lavaRarity").orElse(20000).forGetter(FinitePools::lavaRarity),
            Codec.BOOL.fieldOf("uniformLavaHeights").orElse(false).forGetter(FinitePools::uniformLavaHeights)
        ).apply(instance, FinitePools::new)
    );
    public static final FinitePools DEFAULT = CodecUtil.getDefaultByMap(CODEC);
}
