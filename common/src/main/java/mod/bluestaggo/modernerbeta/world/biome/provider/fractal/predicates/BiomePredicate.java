package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerRandom;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public abstract class BiomePredicate {
    public static final Codec<BiomePredicate> BASE_CODEC = ModernBetaRegistries.BIOME_PREDICATE.getCodec()
        .dispatch("condition", BiomePredicate::getType, BiomePredicateType::codec);

    public static BiomePredicate allDiagonalNeighborsMatch(BiomePredicate predicate) {
        return new NeighborMatchBiomePredicate(predicate, true, true);
    }

    public static BiomePredicate allDiagonalNeighborsMatch(ExtendedBiomeId biome) {
        return allDiagonalNeighborsMatch(of(biome));
    }

    public static BiomePredicate allNeighborsMatch(BiomePredicate predicate) {
        return new NeighborMatchBiomePredicate(predicate, true, false);
    }

    public static BiomePredicate allNeighborsMatch(ExtendedBiomeId biome) {
        return allNeighborsMatch(of(biome));
    }

    public static BiomePredicate allOf(BiomePredicate... predicates) {
        return new AllOfBiomePredicate(List.of(predicates));
    }

    public static BiomePredicate anyDiagonalNeighborMatches(BiomePredicate predicate) {
        return new NeighborMatchBiomePredicate(predicate, false, true);
    }

    public static BiomePredicate anyDiagonalNeighborMatches(ExtendedBiomeId biome) {
        return anyDiagonalNeighborMatches(of(biome));
    }

    public static BiomePredicate anyNeighborMatches(BiomePredicate predicate) {
        return new NeighborMatchBiomePredicate(predicate, false, false);
    }

    public static BiomePredicate anyNeighborMatches(ExtendedBiomeId biome) {
        return anyNeighborMatches(of(biome));
    }

    public static BiomePredicate anyOf(BiomePredicate... predicates) {
        return new AnyOfBiomePredicate(List.of(predicates));
    }

    public static BiomePredicate anyOf(List<BiomePredicate> predicates) {
        return new AnyOfBiomePredicate(predicates);
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

    public static BiomePredicate inSet(ExtendedBiomeId... biomes) {
        return new InSetBiomePredicate(Set.of(biomes));
    }

    public static BiomePredicate inSet(Set<ExtendedBiomeId> biomes) {
        return new InSetBiomePredicate(biomes);
    }

    public static BiomePredicate interior() {
        return new InteriorBiomePredicate(InteriorBiomePredicate.Type.INTERIOR);
    }

    public static BiomePredicate oneIn(int chance) {
        return new RandomChanceBiomePredicate(1, chance);
    }

    public static BiomePredicate noneOf(BiomePredicate... predicates) {
        return anyOf(predicates).invert();
    }

    public static BiomePredicate noneOf(List<BiomePredicate> predicates) {
        return anyOf(predicates).invert();
    }

    public static BiomePredicate noneInSet(ExtendedBiomeId... biomes) {
        return inSet(biomes).invert();
    }

    public static BiomePredicate noneInSet(Set<ExtendedBiomeId> biomes) {
        return inSet(biomes).invert();
    }

    public static BiomePredicate randomChance(int numerator, int denominator) {
        return new RandomChanceBiomePredicate(numerator, denominator);
    }

    public static BiomePredicate of(ExtendedBiomeId biome) {
        return new SingleMatchBiomePredicate(biome);
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

    public abstract BiomePredicateType<?> getType();

    public abstract boolean matches(ExtendedBiomeId biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z);
}