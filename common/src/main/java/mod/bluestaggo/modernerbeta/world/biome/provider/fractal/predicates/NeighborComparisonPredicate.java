package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerRandom;

import java.util.function.Supplier;

public abstract class NeighborComparisonPredicate extends BiomePredicate {
    private static final int[] NEIGHBOR_X_COORDS = {-1, 1, 0, 0};
    private static final int[] NEIGHBOR_Z_COORDS = {0, 0, -1, 1};
    private static final int[] DIAGONAL_NEIGHBOR_X_COORDS = {-1, 1, -1, 1};
    private static final int[] DIAGONAL_NEIGHBOR_Z_COORDS = {-1, -1, 1, 1};

    protected final int requiredCount;
    protected final boolean diagonal;

    protected static <P extends NeighborComparisonPredicate> Products.P2<
        RecordCodecBuilder.Mu<P>,
        Integer,
        Boolean
    > fillNeighborComparisonFields(RecordCodecBuilder.Instance<P> instance) {
        return instance.group(
            Codec.INT.fieldOf("requiredCount").orElse(1).forGetter(predicate -> predicate.requiredCount),
            Codec.BOOL.fieldOf("diagonal").orElse(false).forGetter(predicate -> predicate.diagonal)
        );
    }

    protected NeighborComparisonPredicate(int requiredCount, boolean diagonal) {
        this.requiredCount = requiredCount;
        this.diagonal = diagonal;
    }

    @Override
    public boolean matches(ExtendedBiomeId biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        int[] xCoords = this.diagonal ? DIAGONAL_NEIGHBOR_X_COORDS : NEIGHBOR_X_COORDS;
        int[] zCoords = this.diagonal ? DIAGONAL_NEIGHBOR_Z_COORDS : NEIGHBOR_Z_COORDS;

        this.prepareMatch(biome, layer, randomSupplier, x, z);

        int matches = 0;
        for (int i = 0; i < 4; i++) {
            int nx = x + xCoords[i];
            int nz = z + zCoords[i];
            ExtendedBiomeId neighbor = layer.sample(nx, nz);
            boolean match = this.neighborMatches(biome, neighbor, layer, randomSupplier, x, z, nx, nz);
            if (match && ++matches >= this.requiredCount) {
                return true;
            }
        }

        return false;
    }

    protected void prepareMatch(ExtendedBiomeId biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
    }

    protected abstract boolean neighborMatches(ExtendedBiomeId centre, ExtendedBiomeId neighbor, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z, int nx, int nz);
}
