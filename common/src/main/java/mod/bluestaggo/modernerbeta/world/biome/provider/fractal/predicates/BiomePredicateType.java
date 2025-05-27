package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates;

import com.mojang.serialization.MapCodec;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import net.minecraft.registry.Registry;

public record BiomePredicateType<F extends BiomePredicate>(MapCodec<F> codec) {
    public static final BiomePredicateType<AllOfBiomePredicate> ALL_OF = register("all_of", AllOfBiomePredicate.CODEC);
    public static final BiomePredicateType<AnyOfBiomePredicate> ANY_OF = register("any_of", AnyOfBiomePredicate.CODEC);
    public static final BiomePredicateType<InSetBiomePredicate> IN_SET = register("in_set", InSetBiomePredicate.CODEC);
    public static final BiomePredicateType<InteriorBiomePredicate> INTERIOR = register("interior", InteriorBiomePredicate.CODEC);
    public static final BiomePredicateType<InvertedBiomePredicate> INVERTED = register("inverted", InvertedBiomePredicate.CODEC);
    public static final BiomePredicateType<NeighborMatchBiomePredicate> NEIGHBOR_MATCH = register("neighbor_match", NeighborMatchBiomePredicate.CODEC);
    public static final BiomePredicateType<RandomChanceBiomePredicate> RANDOM_CHANCE = register("random_chance", RandomChanceBiomePredicate.CODEC);
    public static final BiomePredicateType<SingleMatchBiomePredicate> SINGLE_MATCH = register("single_match", SingleMatchBiomePredicate.CODEC);

    private static <F extends BiomePredicate> BiomePredicateType<F> register(String id, MapCodec<F> codec) {
        BiomePredicateType<F> filterType = new BiomePredicateType<>(codec);
        Registry.register(ModernBetaRegistries.BIOME_PREDICATE, ModernerBeta.createId(id), filterType);
        return filterType;
    }

    public static void init() {
    }
}
