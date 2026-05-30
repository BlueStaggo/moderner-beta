package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.resources.Identifier;

public record DeepslateGeneration(
    boolean enabled,
    int minY,
    int maxY,
    Identifier block
) {
    public static final Codec<DeepslateGeneration> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.BOOL.fieldOf("enabled").orElse(true).forGetter(DeepslateGeneration::enabled),
            Codec.INT.fieldOf("minY").orElse(0).forGetter(DeepslateGeneration::minY),
            Codec.INT.fieldOf("maxY").orElse(8).forGetter(DeepslateGeneration::maxY),
            Identifier.CODEC.fieldOf("block").orElse(VersionCompat.vanillaId("deepslate")).forGetter(DeepslateGeneration::block)
        ).apply(instance, DeepslateGeneration::new)
    );
    public static final DeepslateGeneration DEFAULT = CodecUtil.getDefaultByMap(CODEC);

    public static final DeepslateGeneration ENABLED = new DeepslateGeneration(true, 0, 8, VersionCompat.vanillaId("deepslate"));
    public static final DeepslateGeneration DISABLED = new DeepslateGeneration(false, 0, 8, VersionCompat.vanillaId("deepslate"));
}
