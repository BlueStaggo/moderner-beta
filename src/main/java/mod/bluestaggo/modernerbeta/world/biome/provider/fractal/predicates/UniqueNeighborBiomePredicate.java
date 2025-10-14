package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerRandom;

import java.util.function.Supplier;

public class UniqueNeighborBiomePredicate extends NeighborComparisonPredicate {
    public static final com.mojang.serialization.MapCodec<UniqueNeighborBiomePredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillNeighborComparisonFields(instance)
            .apply(instance, UniqueNeighborBiomePredicate::new)
    );

    public UniqueNeighborBiomePredicate(int requiredCount, boolean diagonal) {
        super(requiredCount, diagonal);
    }

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.UNIQUE_NEIGHBOR;
    }

    @Override
    protected boolean neighborMatches(ExtendedBiomeId centre, ExtendedBiomeId neighbor, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z, int nx, int nz) {
        return !neighbor.equals(centre);
    }
}
