package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerRandom;

import java.util.function.Supplier;

public class RandomChanceBiomePredicate extends BiomePredicate {
    public static final MapCodec<RandomChanceBiomePredicate> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance
            .group(
                Codec.INT.fieldOf("numerator").orElse(1).forGetter(predicate -> predicate.numerator),
                Codec.INT.fieldOf("denominator").forGetter(predicate -> predicate.denominator)
            )
            .apply(instance, RandomChanceBiomePredicate::new)
    );

    private final int numerator;
    private final int denominator;

    public RandomChanceBiomePredicate(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.RANDOM_CHANCE;
    }

    @Override
    public boolean matches(ExtendedBiomeId biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        return randomSupplier.get().nextInt(this.denominator) < this.numerator;
    }
}
