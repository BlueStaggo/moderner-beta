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

    public static BiomePredicate allOf(BiomePredicate... predicates) {
        return new AllOfBiomePredicate(List.of(predicates));
    }

    public static BiomePredicate anyOf(BiomePredicate... predicates) {
        return new AnyOfBiomePredicate(List.of(predicates));
    }

    public static BiomePredicate anyOf(List<BiomePredicate> predicates) {
        return new AnyOfBiomePredicate(predicates);
    }

    public static BiomePredicate border() {
        return new UniqueNeighborBiomePredicate(1, false);
    }

    public static BiomePredicate diagonalBorder() {
        return new UniqueNeighborBiomePredicate(1, true);
    }

    public static BiomePredicate diagonalInterior() {
        return new IdenticalNeighborBiomePredicate(4, false);
    }

    public static BiomePredicate diagonalNeighborsMatch(BiomePredicate predicate, int neighborCount) {
        return new NeighborMatchBiomePredicate(neighborCount, true, predicate);
    }

    public static BiomePredicate diagonalNeighborsMatch(ExtendedBiomeId biome, int neighborCount) {
        return diagonalNeighborsMatch(of(biome), neighborCount);
    }

    public static BiomePredicate identicalNeighbors(int count, boolean diagonal) {
        return new IdenticalNeighborBiomePredicate(count, diagonal);
    }

    public static BiomePredicate inSet(ExtendedBiomeId... biomes) {
        return new InSetBiomePredicate(Set.of(biomes));
    }

    public static BiomePredicate inSet(Set<ExtendedBiomeId> biomes) {
        return new InSetBiomePredicate(biomes);
    }

    public static BiomePredicate interior() {
        return new IdenticalNeighborBiomePredicate(4, false);
    }

    public static BiomePredicate of(ExtendedBiomeId biome) {
        return new SingleMatchBiomePredicate(biome);
    }

    public static BiomePredicate ofTrue() {
        return TrueBiomePredicate.INSTANCE;
    }

    public static BiomePredicate oneIn(int chance) {
        return new RandomChanceBiomePredicate(1, chance);
    }

    public static BiomePredicate neighborsMatch(BiomePredicate predicate, int neighborCount) {
        return new NeighborMatchBiomePredicate(neighborCount, false, predicate);
    }

    public static BiomePredicate neighborsMatch(ExtendedBiomeId biome, int neighborCount) {
        return neighborsMatch(of(biome), neighborCount);
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

    public static BiomePredicate uniqueNeighbors(int count, boolean diagonal) {
        return new UniqueNeighborBiomePredicate(count, diagonal);
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