package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerRandom;

import java.util.function.Supplier;

public record SingleMatchBiomePredicate(ExtendedBiomeId biome) implements BiomePredicate {
    public static final com.mojang.serialization./*Map*/Codec<SingleMatchBiomePredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            ExtendedBiomeId.CODEC.fieldOf("biome").forGetter(predicate -> predicate.biome)
        ).apply(instance, SingleMatchBiomePredicate::new)
    );

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.SINGLE_MATCH;
    }

    @Override
    public boolean matches(ExtendedBiomeId biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        return this.biome.equals(biome);
    }
}
