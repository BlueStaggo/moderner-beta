package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates;

import com.mojang.serialization.MapCodec;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;

public record BiomePredicateType<P extends BiomePredicate>(MapCodec<P> codec) {
    private static IRegistryHandler<BiomePredicateType<?>> registryHandler;

    public static BiomePredicateType<AllOfBiomePredicate> ALL_OF;
    public static BiomePredicateType<AnyOfBiomePredicate> ANY_OF;
    public static BiomePredicateType<CategorizedNeighborBiomePredicate> CATEGORIZED_NEIGHBOR;
    public static BiomePredicateType<IdenticalNeighborBiomePredicate> IDENTICAL_NEIGHBOR;
    public static BiomePredicateType<InSetBiomePredicate> IN_SET;
    public static BiomePredicateType<InteriorBiomePredicate> INTERIOR;
    public static BiomePredicateType<InvertedBiomePredicate> INVERTED;
    public static BiomePredicateType<WrappedIntMatchBiomePredicate> MUTATION;
    public static BiomePredicateType<NeighborMatchBiomePredicate> NEIGHBOR_MATCH;
    public static BiomePredicateType<UniqueNeighborBiomePredicate> UNIQUE_NEIGHBOR;
    public static BiomePredicateType<RandomChanceBiomePredicate> RANDOM_CHANCE;
    public static BiomePredicateType<SingleMatchBiomePredicate> SINGLE_MATCH;
    public static BiomePredicateType<TrueBiomePredicate> TRUE;
    public static BiomePredicateType<WrappedIntMatchBiomePredicate> WRAPPED_INT_MATCH;

    private static <P extends BiomePredicate> BiomePredicateType<P> register(String id, MapCodec<P> codec) {
        return registryHandler.register(ModernerBeta.createId(id), new BiomePredicateType<>(codec));
    }

    @SuppressWarnings("unchecked")
    public static void init(IRegistryHandler<?> handler) {
        registryHandler = (IRegistryHandler<BiomePredicateType<?>>) handler;

        ALL_OF = register("all_of", AllOfBiomePredicate.CODEC);
        ANY_OF = register("any_of", AnyOfBiomePredicate.CODEC);
        CATEGORIZED_NEIGHBOR = register("categorized_neighbor", CategorizedNeighborBiomePredicate.CODEC);
        IDENTICAL_NEIGHBOR = register("identical_neighbor", IdenticalNeighborBiomePredicate.CODEC);
        IN_SET = register("in_set", InSetBiomePredicate.CODEC);
        INTERIOR = register("interior", InteriorBiomePredicate.CODEC);
        INVERTED = register("inverted", InvertedBiomePredicate.CODEC);
        MUTATION = register("mutation", WrappedIntMatchBiomePredicate.CODEC);
        NEIGHBOR_MATCH = register("neighbor_match", NeighborMatchBiomePredicate.CODEC);
        UNIQUE_NEIGHBOR = register("unique_neighbor", UniqueNeighborBiomePredicate.CODEC);
        RANDOM_CHANCE = register("random_chance", RandomChanceBiomePredicate.CODEC);
        SINGLE_MATCH = register("single_match", SingleMatchBiomePredicate.CODEC);
        TRUE = register("true", TrueBiomePredicate.CODEC);
        WRAPPED_INT_MATCH = register("wrapped_int_match", WrappedIntMatchBiomePredicate.CODEC);
    }
}
