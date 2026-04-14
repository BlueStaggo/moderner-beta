package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.predicates;

import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.LayerRandom;
import net.minecraft.util.StringRepresentable;

import java.util.function.Supplier;

public record InteriorBiomePredicate(Type type) implements BiomePredicate {
    public static final com.mojang.serialization.MapCodec<InteriorBiomePredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            StringRepresentable.fromEnum(Type::values).fieldOf("type").orElse(Type.INTERIOR).forGetter(predicate -> predicate.type)
        ).apply(instance, InteriorBiomePredicate::new)
    );

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.INTERIOR;
    }

    @Override
    public boolean matches(ExtendedIdentifier biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        return this.type.matches(biome, layer, x, z);
    }

    public enum Type implements StringRepresentable {
        INTERIOR("interior", false, false),
        DIAGONAL_INTERIOR("diagonal_interior", false, true),
        BORDER("border", true, false),
        DIAGONAL_BORDER("diagonal_border", true, true);

        public final String id;
        private final boolean border;
        private final boolean diagonal;

        Type(String id, boolean border, boolean diagonal) {
            this.id = id;
            this.border = border;
            this.diagonal = diagonal;
        }

        public boolean matches(ExtendedIdentifier biome, Layer layer, int x, int z) {
            ExtendedIdentifier[] neighbors = this.diagonal ? layer.sampleDiagonalNeighbors(x, z) : layer.sampleNeighbors(x, z);
            return this.border != Layer.allNeighborsEqual(neighbors, biome);
        }

        @Override
        public String getSerializedName() {
            return this.id;
        }
    }
}
