package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.world.chunk.provider.indev.IndevTheme;
import mod.bluestaggo.modernerbeta.world.chunk.provider.indev.IndevType;
import net.minecraft.util.StringIdentifiable;

public record FiniteLevelProperties(
    IndevType type,
    IndevTheme theme,
    int width,
    int length,
    int height
) {
    public static final Codec<FiniteLevelProperties> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            StringIdentifiable.createCodec(IndevType::values).fieldOf("type").orElse(IndevType.ISLAND).forGetter(FiniteLevelProperties::type),
            StringIdentifiable.createCodec(IndevTheme::values).fieldOf("theme").orElse(IndevTheme.NORMAL).forGetter(FiniteLevelProperties::theme),
            Codec.INT.fieldOf("width").orElse(256).forGetter(FiniteLevelProperties::width),
            Codec.INT.fieldOf("length").orElse(256).forGetter(FiniteLevelProperties::length),
            Codec.INT.fieldOf("height").orElse(128).forGetter(FiniteLevelProperties::height)
        ).apply(instance, FiniteLevelProperties::new)
    );
    public static final FiniteLevelProperties DEFAULT = CodecUtil.getDefaultByMap(CODEC);
}
