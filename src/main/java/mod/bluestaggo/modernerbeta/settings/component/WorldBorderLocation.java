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
            Codec.INT.fieldOf("groundLevel").orElse(54).forGetter(WorldBorderLocation::groundLevel)
        ).apply(instance, WorldBorderLocation::new)
    );
    public static final WorldBorderLocation DEFAULT = CodecUtil.getDefaultByMap(CODEC);

    public static WorldBorderLocation xboxLegacy(int width) {
        return new WorldBorderLocation(true, width, CenterType.ORIGIN, FalloffType.SMOOTH_OCEAN, 54);
    }

    public static WorldBorderLocation pe() {
        return new WorldBorderLocation(false, 256, CenterType.CORNER, FalloffType.VOID, 54);
    }

    public static WorldBorderLocation indev(int width, int seaLevel) {
        return new WorldBorderLocation(true, width, CenterType.ORIGIN, FalloffType.OCEAN, seaLevel - 9);
    }

    public int center() {
        return centerType == CenterType.CORNER ? width / 2 : 0;
    }

    public int radius() {
        return width / 2;
    }

    public boolean containsPoint(int x, int z) {
        return containsPoint(x, z, 0);
    }

    public boolean containsPoint(int x, int z, int margin) {
        if (!enabled()) {
            return true;
        }

        int center = center();
        int radius = radius();
        return x >= center - radius - margin && x < center + radius + margin
            && z >= center - radius - margin && z < center + radius + margin;
    }

    public boolean containsChunk(int x, int z) {
        if (!enabled()) {
            return true;
        }

        int center = center();
        int radius = radius();
        return x * 16 + 15 >= center - radius && x * 16 < center + radius
            && z * 16 + 15 >= center - radius && z * 16 < center + radius;
    }

    public boolean affectsDensity() {
        return this.enabled() && this.falloffType() == FalloffType.SMOOTH_OCEAN;
    }

    public double modifyDensity(double density, int x, int z) {
        if (!this.affectsDensity()) {
            return density;
        }

        int distance = Math.min(this.width() / 2 - Math.abs(x - center()), this.width() / 2 - Math.abs(z - center()));

        double falloff = 0.0;
        if (distance < 32) {
            falloff = (32 - distance) * 4.0;
        }
        return density - falloff;
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
