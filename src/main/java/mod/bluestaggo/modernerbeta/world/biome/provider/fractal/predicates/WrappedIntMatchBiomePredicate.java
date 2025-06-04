package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerRandom;

import java.util.function.Supplier;

public class WrappedIntMatchBiomePredicate extends BiomePredicate {
    public static final com.mojang.serialization.MapCodec<WrappedIntMatchBiomePredicate> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance
            .group(
                Codec.INT.fieldOf("range").forGetter(predicate -> predicate.range),
                Codec.INT.fieldOf("match").orElse(0).forGetter(predicate -> predicate.match)
            )
            .apply(instance, WrappedIntMatchBiomePredicate::new)
    );

    private final int range;
    private final int match;

    public WrappedIntMatchBiomePredicate(int range, int match) {
        this.range = range;
        this.match = match;
    }

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.MUTATION;
    }

    @Override
    public boolean matches(ExtendedBiomeId biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        try {
            int random = Integer.parseInt(biome.ext());
            return random % this.range == this.match;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
