package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerRandom;

import java.util.function.Supplier;

public class InvertedBiomePredicate extends BiomePredicate {
    public static final MapCodec<InvertedBiomePredicate> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance
            .group(
                BiomePredicate.BASE_CODEC.fieldOf("term").forGetter(predicate -> predicate.term)
            )
            .apply(instance, InvertedBiomePredicate::new)
    );

    private final BiomePredicate term;

    public InvertedBiomePredicate(BiomePredicate term) {
        this.term = term;
    }

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.INVERTED;
    }

    @Override
    public boolean matches(ExtendedBiomeId biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        return !this.term.matches(biome, layer, randomSupplier, x, z);
    }
}
