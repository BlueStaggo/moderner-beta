package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.predicates;

import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.LayerRandom;

import java.util.function.Supplier;

public record InvertedBiomePredicate(BiomePredicate term) implements BiomePredicate {
    public static final com.mojang.serialization.MapCodec<InvertedBiomePredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            BiomePredicate.BASE_CODEC.fieldOf("term").forGetter(predicate -> predicate.term)
        ).apply(instance, InvertedBiomePredicate::new)
    );

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.INVERTED;
    }

    @Override
    public boolean matches(ExtendedIdentifier biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        return !this.term.matches(biome, layer, randomSupplier, x, z);
    }
}
