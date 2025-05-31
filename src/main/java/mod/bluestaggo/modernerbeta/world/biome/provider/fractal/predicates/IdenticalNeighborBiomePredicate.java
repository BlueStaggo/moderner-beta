package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerRandom;

import java.util.function.Supplier;

public class IdenticalNeighborBiomePredicate extends NeighborComparisonPredicate {
    public static final MapCodec<IdenticalNeighborBiomePredicate> CODEC = RecordCodecBuilder.mapCodec(
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
