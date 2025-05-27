package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerRandom;

import java.util.function.Supplier;

public class NeighborMatchBiomePredicate extends BiomePredicate {
    public static final MapCodec<NeighborMatchBiomePredicate> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance
            .group(
                BiomePredicate.BASE_CODEC.fieldOf("neighborPredicate").forGetter(predicate -> predicate.neighborPredicate),
                Codec.BOOL.fieldOf("requireAll").orElse(false).forGetter(predicate -> predicate.requireAll),
                Codec.BOOL.fieldOf("diagonal").orElse(false).forGetter(predicate -> predicate.diagonal)
            )
            .apply(instance, NeighborMatchBiomePredicate::new)
    );

    private static final int[] NEIGHBOR_X_COORDS = {-1, 1, 0, 0};
    private static final int[] NEIGHBOR_Z_COORDS = {0, 0, -1, 1};
    private static final int[] DIAGONAL_NEIGHBOR_X_COORDS = {-1, 1, -1, 1};
    private static final int[] DIAGONAL_NEIGHBOR_Z_COORDS = {-1, -1, 1, 1};

    private final BiomePredicate neighborPredicate;
    private final boolean requireAll;
    private final boolean diagonal;

    public NeighborMatchBiomePredicate(BiomePredicate neighborPredicate, boolean requireAll, boolean diagonal) {
        this.neighborPredicate = neighborPredicate;
        this.requireAll = requireAll;
        this.diagonal = diagonal;
    }

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.NEIGHBOR_MATCH;
    }

    @Override
    public boolean matches(ExtendedBiomeId biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        int[] xCoords = this.diagonal ? DIAGONAL_NEIGHBOR_X_COORDS : NEIGHBOR_X_COORDS;
        int[] zCoords = this.diagonal ? DIAGONAL_NEIGHBOR_Z_COORDS : NEIGHBOR_Z_COORDS;

        for (int i = 0; i < 4; i++) {
            int nx = x + xCoords[i];
            int nz = z + zCoords[i];
            ExtendedBiomeId neighbor = layer.sample(nx, nz);
            boolean match = this.neighborPredicate.matches(neighbor, layer, randomSupplier, nx, nz);
            if (this.requireAll && !match) {
                return false;
            }
            if (!this.requireAll && match) {
                return true;
            }
        }

        return this.requireAll;
    }
}
