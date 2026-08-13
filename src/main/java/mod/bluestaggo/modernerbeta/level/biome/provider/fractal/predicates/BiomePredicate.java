package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.predicates;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.LayerRandom;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public interface BiomePredicate {
    Codec<BiomePredicate> BASE_CODEC = ModernBetaRegistries.BIOME_PREDICATE.byNameCodec()
        .dispatch("condition", BiomePredicate::getType, BiomePredicateType::codec);

    static BiomePredicate allOf(BiomePredicate... predicates) {
        return new AllOfBiomePredicate(List.of(predicates));
    }

    static BiomePredicate anyOf(BiomePredicate... predicates) {
        return new AnyOfBiomePredicate(List.of(predicates));
    }

    static BiomePredicate anyOf(List<BiomePredicate> predicates) {
        return new AnyOfBiomePredicate(predicates);
    }

    static BiomePredicate border() {
        return new UniqueNeighborBiomePredicate(1, false);
    }

    static BiomePredicate diagonalBorder() {
        return new UniqueNeighborBiomePredicate(1, true);
    }

    static BiomePredicate diagonalInterior() {
        return new IdenticalNeighborBiomePredicate(4, true);
    }

    static BiomePredicate diagonalNeighborsMatch(BiomePredicate predicate, int neighborCount) {
        return new NeighborMatchBiomePredicate(neighborCount, true, predicate);
    }

    static BiomePredicate diagonalNeighborsMatch(ExtendedIdentifier biome, int neighborCount) {
        return diagonalNeighborsMatch(of(biome), neighborCount);
    }

    static BiomePredicate diagonalNeighborsMatch(List<Set<ExtendedIdentifier>> categories, int neighborCount) {
        return new CategorizedNeighborBiomePredicate(neighborCount, true, categories);
    }

    static BiomePredicate identicalNeighbors(int count, boolean diagonal) {
        return new IdenticalNeighborBiomePredicate(count, diagonal);
    }

    static BiomePredicate inGrid(int size, int spacing, int offset) {
        return new InGridBiomePredicate(size, spacing, offset);
    }

    static BiomePredicate inRange(int centerX, int centerZ, int width, int length, boolean evenSize, InRangeBiomePredicate.Shape shape) {
        return new InRangeBiomePredicate(centerX, centerZ, width, length, evenSize, shape);
    }

    static BiomePredicate inSet(ExtendedIdentifier... biomes) {
        return new InSetBiomePredicate(Set.of(biomes));
    }

    static BiomePredicate inSet(Set<ExtendedIdentifier> biomes) {
        return new InSetBiomePredicate(biomes);
    }

    static BiomePredicate interior() {
        return new IdenticalNeighborBiomePredicate(4, false);
    }

    static BiomePredicate neighborsMatch(BiomePredicate predicate, int neighborCount) {
        return new NeighborMatchBiomePredicate(neighborCount, false, predicate);
    }

    static BiomePredicate neighborsMatch(ExtendedIdentifier biome, int neighborCount) {
        return neighborsMatch(of(biome), neighborCount);
    }

    static BiomePredicate neighborsMatch(List<Set<ExtendedIdentifier>> categories, int neighborCount) {
        return new CategorizedNeighborBiomePredicate(neighborCount, false, categories);
    }

    static BiomePredicate noneOf(BiomePredicate... predicates) {
        return anyOf(predicates).invert();
    }

    static BiomePredicate noneOf(List<BiomePredicate> predicates) {
        return anyOf(predicates).invert();
    }

    static BiomePredicate noneInSet(ExtendedIdentifier... biomes) {
        return inSet(biomes).invert();
    }

    static BiomePredicate noneInSet(Set<ExtendedIdentifier> biomes) {
        return inSet(biomes).invert();
    }

    static BiomePredicate of(ExtendedIdentifier biome) {
        return new SingleMatchBiomePredicate(biome);
    }

    static BiomePredicate ofTrue() {
        return TrueBiomePredicate.INSTANCE;
    }

    static BiomePredicate oneIn(int chance) {
        return new RandomChanceBiomePredicate(1, chance);
    }

    static BiomePredicate randomChance(int numerator, int denominator) {
        return new RandomChanceBiomePredicate(numerator, denominator);
    }

    static BiomePredicate simpleHills(Set<ExtendedIdentifier> affectedBiomes) {
        return BiomePredicate.inSet(affectedBiomes)
            .and(BiomePredicate.interior())
            .and(BiomePredicate.oneIn(3));
    }

    static BiomePredicate uniqueNeighbors(int count, boolean diagonal) {
        return new UniqueNeighborBiomePredicate(count, diagonal);
    }

    static BiomePredicate wrappedIntMatch(int range, int match) {
        return new WrappedIntMatchBiomePredicate(range, match);
    }

    default BiomePredicate and(BiomePredicate other) {
        return new AllOfBiomePredicate(List.of(this, other));
    }

    default BiomePredicate or(BiomePredicate other) {
        return new AnyOfBiomePredicate(List.of(this, other));
    }

    default BiomePredicate invert() {
        return new InvertedBiomePredicate(this);
    }

    BiomePredicateType<?> getType();

    boolean matches(ExtendedHolder<Biome> biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z);
}
