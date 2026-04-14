package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.predicates;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.LayerRandom;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;

import java.util.function.Supplier;

public abstract class NeighborComparisonPredicate implements BiomePredicate {
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
    public boolean matches(ExtendedIdentifier biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        int[] xCoords = this.diagonal ? DIAGONAL_NEIGHBOR_X_COORDS : NEIGHBOR_X_COORDS;
        int[] zCoords = this.diagonal ? DIAGONAL_NEIGHBOR_Z_COORDS : NEIGHBOR_Z_COORDS;

        int matches = 0;
        for (int i = 0; i < 4; i++) {
            int nx = x + xCoords[i];
            int nz = z + zCoords[i];
            ExtendedIdentifier neighbor = layer.sample(nx, nz);
            boolean match = this.neighborMatches(biome, neighbor, layer, randomSupplier, x, z, nx, nz);
            if (match && ++matches >= this.requiredCount) {
                return true;
            }
        }

        return false;
    }

    protected abstract boolean neighborMatches(ExtendedIdentifier centre, ExtendedIdentifier neighbor, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z, int nx, int nz);
}
