package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.predicates;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.LayerRandom;

import java.util.function.Supplier;

public record WrappedIntMatchBiomePredicate(int range, int match) implements BiomePredicate {
    public static final com.mojang.serialization.MapCodec<WrappedIntMatchBiomePredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            Codec.INT.fieldOf("range").forGetter(predicate -> predicate.range),
            Codec.INT.fieldOf("match").orElse(0).forGetter(predicate -> predicate.match)
        ).apply(instance, WrappedIntMatchBiomePredicate::new)
    );

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.MUTATION;
    }

    @Override
    public boolean matches(ExtendedIdentifier biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        try {
            int random = Integer.parseInt(biome.ext());
            return random % this.range == this.match;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
