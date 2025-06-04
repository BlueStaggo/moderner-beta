package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates;

import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerRandom;

import java.util.function.Supplier;

public class TrueBiomePredicate extends BiomePredicate {
    public static final TrueBiomePredicate INSTANCE = new TrueBiomePredicate();
    public static final com.mojang.serialization.MapCodec<TrueBiomePredicate> CODEC = com.mojang.serialization.MapCodec.unit(INSTANCE);

    private TrueBiomePredicate() {
    }

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.TRUE;
    }

    @Override
    public boolean matches(ExtendedBiomeId biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        return true;
    }
}
