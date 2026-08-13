package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.predicates;

import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.LayerRandom;
import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Supplier;

public final class TrueBiomePredicate implements BiomePredicate {
    public static final TrueBiomePredicate INSTANCE = new TrueBiomePredicate();
    public static final com.mojang.serialization.MapCodec<TrueBiomePredicate> CODEC = com.mojang.serialization.MapCodec.unit(INSTANCE);

    private TrueBiomePredicate() {
    }

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.TRUE;
    }

    @Override
    public boolean matches(ExtendedHolder<Biome> biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        return true;
    }
}
