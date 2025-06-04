package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerRandom;
import net.minecraft.util.StringIdentifiable;

import java.util.function.Supplier;

public class InteriorBiomePredicate extends BiomePredicate {
    public static final com.mojang.serialization.MapCodec<InteriorBiomePredicate> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance
            .group(
                StringIdentifiable.createCodec(Type::values).fieldOf("type").orElse(Type.INTERIOR).forGetter(predicate -> predicate.type)
            )
            .apply(instance, InteriorBiomePredicate::new)
    );

    private final Type type;

    public InteriorBiomePredicate(Type type) {
        this.type = type;
    }

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.INTERIOR;
    }

    @Override
    public boolean matches(ExtendedBiomeId biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        return this.type.matches(biome, layer, x, z);
    }

    public enum Type implements StringIdentifiable {
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

        public boolean matches(ExtendedBiomeId biome, Layer layer, int x, int z) {
            ExtendedBiomeId[] neighbors = this.diagonal ? layer.sampleDiagonalNeighbors(x, z) : layer.sampleNeighbors(x, z);
            return this.border != Layer.allNeighborsEqual(neighbors, biome);
        }

        @Override
        public String asString() {
            return this.id;
        }
    }
}
