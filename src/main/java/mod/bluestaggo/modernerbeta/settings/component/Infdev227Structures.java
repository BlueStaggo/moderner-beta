package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;

public record Infdev227Structures(
    boolean brickPyramids,
    boolean obsidianWalls
) {
    public static final Codec<Infdev227Structures> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.BOOL.fieldOf("brickPyramids").orElse(true).forGetter(Infdev227Structures::brickPyramids),
            Codec.BOOL.fieldOf("obsidianWalls").orElse(false).forGetter(Infdev227Structures::obsidianWalls)
        ).apply(instance, Infdev227Structures::new)
    );
    public static final Infdev227Structures DEFAULT = CodecUtil.getDefaultByMap(CODEC);

    public static final Infdev227Structures DISABLED = new Infdev227Structures(false, false);
}
