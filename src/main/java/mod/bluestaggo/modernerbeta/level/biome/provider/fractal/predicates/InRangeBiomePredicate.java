package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.predicates;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.LayerRandom;
import net.minecraft.util.StringRepresentable;

import java.util.function.Supplier;

public record InRangeBiomePredicate(int centerX, int centerZ, int radiusX, int radiusZ, boolean evenSize, Shape shape) implements BiomePredicate {
    public static final com.mojang.serialization.MapCodec<InRangeBiomePredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            Codec.INT.fieldOf("centerX").orElse(0).forGetter(InRangeBiomePredicate::centerX),
            Codec.INT.fieldOf("centerZ").orElse(0).forGetter(InRangeBiomePredicate::centerZ),
            Codec.INT.fieldOf("radiusX").forGetter(InRangeBiomePredicate::radiusX),
            Codec.INT.fieldOf("radiusZ").forGetter(InRangeBiomePredicate::radiusZ),
            Codec.BOOL.fieldOf("evenSize").orElse(false).forGetter(InRangeBiomePredicate::evenSize),
            StringRepresentable.fromEnum(Shape::values).fieldOf("shape").forGetter(InRangeBiomePredicate::shape)
        ).apply(instance, InRangeBiomePredicate::new)
    );

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.IN_RANGE;
    }

    @Override
    public boolean matches(ExtendedBiomeId biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        if (evenSize) {
            if (x - this.centerX < 0) x++;
            if (z - this.centerZ < 0) z++;
        }

        double radiusX = this.radiusX - 1;
        double localX = x - this.centerX;
        if (radiusX <= 0.0) {
            localX *= Double.MAX_VALUE;
        } else {
            if (this.shape == Shape.CIRCLE) {
                radiusX += 0.5;
            }
            localX /= radiusX;
        }

        double radiusZ = this.radiusZ - 1;
        double localZ = z - this.centerZ;
        if (radiusZ <= 0.0) {
            localZ *= Double.MAX_VALUE;
        } else {
            if (this.shape == Shape.CIRCLE) {
                radiusZ += 0.5;
            }
            localZ /= radiusZ;
        }

        return Math.abs(localX) <= 1.0 && Math.abs(localZ) <= 1.0 && this.shape.containsPoint(localX, localZ);
    }

    public enum Shape implements StringRepresentable {
        CIRCLE("circle", (x, z) -> x * x + z * z <= 1.0),
        RECTANGLE("rectangle", (x, z) -> true),
        DIAMOND("diamond", (x, z) -> Math.abs(x) + Math.abs(z) <= 1.0);

        private final String id;
        private final DistancePredicate predicate;

        Shape(String id, DistancePredicate predicate) {
            this.id = id;
            this.predicate = predicate;
        }

        @Override
        public String getSerializedName() {
            return this.id;
        }

        public boolean containsPoint(double x, double z) {
            return this.predicate.apply(x, z);
        }

        @FunctionalInterface
        public interface DistancePredicate {
            boolean apply(double x, double z);
        }
    }
}
