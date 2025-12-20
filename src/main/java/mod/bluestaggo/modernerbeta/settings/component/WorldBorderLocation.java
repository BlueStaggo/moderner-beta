package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import net.minecraft.util.StringRepresentable;

public record WorldBorderLocation(
    boolean enabled,
    int width,
    CenterType centerType,
    FalloffType falloffType,
    int groundLevel
) {
    public static final Codec<WorldBorderLocation> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.BOOL.fieldOf("enabled").orElse(false).forGetter(WorldBorderLocation::enabled),
            Codec.INT.fieldOf("width").orElse(256).forGetter(WorldBorderLocation::width),
            StringRepresentable.fromEnum(CenterType::values).fieldOf("centerType").orElse(CenterType.ORIGIN).forGetter(WorldBorderLocation::centerType),
            StringRepresentable.fromEnum(FalloffType::values).fieldOf("falloffType").orElse(FalloffType.VOID).forGetter(WorldBorderLocation::falloffType),
            Codec.INT.fieldOf("groundLevel").orElse(32).forGetter(WorldBorderLocation::groundLevel)
        ).apply(instance, WorldBorderLocation::new)
    );
    public static final WorldBorderLocation DEFAULT = CodecUtil.getDefaultByMap(CODEC);

    public int center() {
        return centerType == CenterType.CORNER ? width / 2 : 0;
    }

    public int radius() {
        return width / 2;
    }

    public boolean containsPoint(int x, int z) {
        if (!enabled()) {
            return true;
        }

        int center = center();
        int radius = radius();
        return x >= center - radius && x < center + radius
            && z >= center - radius && z < center + radius;
    }

    public enum CenterType implements StringRepresentable {
        ORIGIN("origin"),
        CORNER("corner")
        ;

        public final String id;

        CenterType(String id) {
            this.id = id;
        }

        @Override
        public String getSerializedName() {
            return this.id;
        }
    }

    public enum FalloffType implements StringRepresentable {
        VOID("void"),
        OCEAN("ocean"),
        SMOOTH_OCEAN("smooth_ocean")
        ;

        public final String id;

        FalloffType(String id) {
            this.id = id;
        }

        @Override
        public String getSerializedName() {
            return this.id;
        }
    }
}
