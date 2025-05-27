package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerRandom;
import net.minecraft.util.StringIdentifiable;

import java.util.function.Supplier;

public class InteriorBiomePredicate extends BiomePredicate {
    public static final MapCodec<InteriorBiomePredicate> CODEC = RecordCodecBuilder.mapCodec(
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
    public boolean satisfies(ExtendedBiomeId biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        return this.type.satisfies(biome, layer, x, z);
    }

    public enum Type implements StringIdentifiable {
        INTERIOR("interior") {
            @Override
            public boolean satisfies(ExtendedBiomeId biome, Layer layer, int x, int z) {
                ExtendedBiomeId[] neighbors = layer.sampleNeighbors(x, z);
                return Layer.allNeighborsEqual(neighbors, biome);
            }
        },
        DIAGONAL_INTERIOR("diagonal_interior") {
            @Override
            public boolean satisfies(ExtendedBiomeId biome, Layer layer, int x, int z) {
                ExtendedBiomeId[] neighbors = layer.sampleDiagonalNeighbors(x, z);
                return Layer.allNeighborsEqual(neighbors, biome);
            }
        },
        BORDER("border") {
            @Override
            public boolean satisfies(ExtendedBiomeId biome, Layer layer, int x, int z) {
                ExtendedBiomeId[] neighbors = layer.sampleNeighbors(x, z);
                return !Layer.allNeighborsEqual(neighbors, biome);
            }
        },
        DIAGONAL_BORDER("diagonal_border") {
            @Override
            public boolean satisfies(ExtendedBiomeId biome, Layer layer, int x, int z) {
                ExtendedBiomeId[] neighbors = layer.sampleDiagonalNeighbors(x, z);
                return !Layer.allNeighborsEqual(neighbors, biome);
            }
        };

        public final String id;

        Type(String id) {
            this.id = id;
        }

        public abstract boolean satisfies(ExtendedBiomeId biome, Layer layer, int x, int z);

        @Override
        public String asString() {
            return this.id;
        }
    }
}
