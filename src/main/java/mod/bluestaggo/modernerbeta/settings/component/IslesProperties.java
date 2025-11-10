package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.level.chunk.provider.island.IslandShape;
import net.minecraft.util.StringRepresentable;

public record IslesProperties(
    boolean useIslands,
    boolean useOuterIslands,
    float oceanSlideTarget,
    IslandShape centerIslandShape,
    int centerIslandRadius,
    int centerIslandFalloffDistance,
    int centerOceanRadius,
    int centerOceanFalloffDistance,
    float outerIslandNoiseScale,
    float outerIslandNoiseOffset
) {
    public static final Codec<IslesProperties> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.BOOL.fieldOf("useIslands").orElse(false).forGetter(IslesProperties::useIslands),
            Codec.BOOL.fieldOf("useOuterIslands").orElse(true).forGetter(IslesProperties::useOuterIslands),
            Codec.FLOAT.fieldOf("oceanSlideTarget").orElse(-200.0f).forGetter(IslesProperties::oceanSlideTarget),
            StringRepresentable.fromEnum(IslandShape::values).fieldOf("centerIslandShape").orElse(IslandShape.CIRCLE).forGetter(IslesProperties::centerIslandShape),
            Codec.INT.fieldOf("centerIslandRadius").orElse(16).forGetter(IslesProperties::centerIslandRadius),
            Codec.INT.fieldOf("centerIslandFalloffDistance").orElse(8).forGetter(IslesProperties::centerIslandFalloffDistance),
            Codec.INT.fieldOf("centerOceanRadius").orElse(64).forGetter(IslesProperties::centerOceanRadius),
            Codec.INT.fieldOf("centerOceanFalloffDistance").orElse(16).forGetter(IslesProperties::centerOceanFalloffDistance),
            Codec.FLOAT.fieldOf("outerIslandNoiseScale").orElse(300.0f).forGetter(IslesProperties::outerIslandNoiseScale),
            Codec.FLOAT.fieldOf("outerIslandNoiseOffset").orElse(0.25f).forGetter(IslesProperties::outerIslandNoiseOffset)
        ).apply(instance, IslesProperties::new)
    );
    public static final IslesProperties DEFAULT = CodecUtil.getDefaultByMap(CODEC);

    public static final IslesProperties ENABLED = new IslesProperties(true, true, -200.0f, IslandShape.CIRCLE, 16, 8, 64, 16, 300.0f, 0.25f);

    public static IslesProperties xboxLegacy(int size) {
        return new IslesProperties(
            true,
            false,
            -200.0f,
            IslandShape.SQUARE,
            size / 32 - 2,
            2,
            64,
            16,
            300.0f,
            0.25f
        );
    }
}
