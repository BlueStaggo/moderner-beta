package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.predicates;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.LayerRandom;

import java.util.function.Supplier;

public class NeighborMatchBiomePredicate extends NeighborComparisonPredicate {
    public static final com.mojang.serialization.MapCodec<NeighborMatchBiomePredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillNeighborComparisonFields(instance)
            .and(BiomePredicate.BASE_CODEC.fieldOf("neighborPredicate").forGetter(predicate -> predicate.neighborPredicate))
            .apply(instance, NeighborMatchBiomePredicate::new)
    );

    private final BiomePredicate neighborPredicate;

    public NeighborMatchBiomePredicate(int requiredCount, boolean diagonal, BiomePredicate neighborPredicate) {
        super(requiredCount, diagonal);
        this.neighborPredicate = neighborPredicate;
    }

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.NEIGHBOR_MATCH;
    }

    @Override
    protected boolean neighborMatches(ExtendedBiomeId centre, ExtendedBiomeId neighbor, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z, int nx, int nz) {
        return this.neighborPredicate.matches(neighbor, layer, randomSupplier, nx, nz);
    }
}
