package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.api.registry.ModernBetaBuiltInRegistries;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerRandom;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public abstract class BiomePredicate {
    public static final Codec<BiomePredicate> BASE_CODEC = ModernBetaBuiltInRegistries.BIOME_PREDICATE.getCodec()
        .dispatch("condition", BiomePredicate::getType, BiomePredicateType::codec);

    public static BiomePredicate allOf(BiomePredicate... predicates) {
        return new AllOfBiomePredicate(List.of(predicates));
    }

    public static BiomePredicate anyOf(BiomePredicate... predicates) {
        return new AnyOfBiomePredicate(List.of(predicates));
    }

    public static BiomePredicate anyOf(ExtendedBiomeId... biomes) {
        return new InSetBiomePredicate(Set.of(biomes));
    }

    public static BiomePredicate border() {
        return new InteriorBiomePredicate(InteriorBiomePredicate.Type.BORDER);
    }

    public static BiomePredicate diagonalBorder() {
        return new InteriorBiomePredicate(InteriorBiomePredicate.Type.DIAGONAL_BORDER);
    }

    public static BiomePredicate diagonalInterior() {
        return new InteriorBiomePredicate(InteriorBiomePredicate.Type.DIAGONAL_INTERIOR);
    }

    public static BiomePredicate interior() {
        return new InteriorBiomePredicate(InteriorBiomePredicate.Type.INTERIOR);
    }

    public static BiomePredicate of(ExtendedBiomeId biome) {
        return new SingleMatchBiomePredicate(biome);
    }

    public static BiomePredicate oneIn(int chance) {
        return new RandomChanceBiomePredicate(1, chance);
    }

    public static BiomePredicate randomChance(int numerator, int denominator) {
        return new RandomChanceBiomePredicate(numerator, denominator);
    }

    public BiomePredicate and(BiomePredicate other) {
        return new AllOfBiomePredicate(List.of(this, other));
    }

    public BiomePredicate or(BiomePredicate other) {
        return new AnyOfBiomePredicate(List.of(this, other));
    }

    public BiomePredicate invert() {
        return new InvertedBiomePredicate(this);
    }

    public BiomePredicate specificCase(ExtendedBiomeId... biomes) {
        return new SpecificCaseBiomePredicate(this, Set.of(biomes));
    }

    public abstract BiomePredicateType<?> getType();

    public abstract boolean satisfies(ExtendedBiomeId biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z);
}