package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates;

import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerRandom;

import java.util.function.Supplier;

public final class TrueBiomePredicate implements BiomePredicate {
    public static final TrueBiomePredicate INSTANCE = new TrueBiomePredicate();
    public static final com.mojang.serialization./*Map*/Codec<TrueBiomePredicate> CODEC = com.mojang.serialization./*Map*/Codec.unit(INSTANCE);

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
