package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.predicates;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.LayerRandom;

import java.util.function.Supplier;

public class IdenticalNeighborBiomePredicate extends NeighborComparisonPredicate {
    public static final com.mojang.serialization.MapCodec<IdenticalNeighborBiomePredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillNeighborComparisonFields(instance)
            .apply(instance, IdenticalNeighborBiomePredicate::new)
    );

    public IdenticalNeighborBiomePredicate(int requiredCount, boolean diagonal) {
        super(requiredCount, diagonal);
    }

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.IDENTICAL_NEIGHBOR;
    }

    @Override
    protected boolean neighborMatches(ExtendedBiomeId centre, ExtendedBiomeId neighbor, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z, int nx, int nz) {
        return neighbor.equals(centre);
    }
}
