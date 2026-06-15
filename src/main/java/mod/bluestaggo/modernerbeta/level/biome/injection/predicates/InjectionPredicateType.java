package mod.bluestaggo.modernerbeta.level.biome.injection.predicates;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;

public record InjectionPredicateType<P extends InjectionPredicate>(com.mojang.serialization.MapCodec<P> codec) {
    private static IRegistryHandler<InjectionPredicateType<?>> registryHandler;

    public static InjectionPredicateType<AllOfInjectionPredicate> ALL_OF;
    public static InjectionPredicateType<AnyOfInjectionPredicate> ANY_OF;
    public static InjectionPredicateType<BelowSurfaceInjectionPredicate> BELOW_SURFACE;
    public static InjectionPredicateType<BiomeInSetInjectionPredicate> BIOME_IN_SET;
    public static InjectionPredicateType<BiomeIsTagInjectionPredicate> BIOME_IS_TAG;
    public static InjectionPredicateType<InvertedInjectionPredicate> INVERTED;
    public static InjectionPredicateType<OutOfBoundsInjectionPredicate> OUT_OF_BOUNDS;
    public static InjectionPredicateType<SurfaceBelowSeaLevelInjectionPredicate> SURFACE_BELOW_SEA_LEVEL;

    private static <P extends InjectionPredicate> InjectionPredicateType<P> register(String id, com.mojang.serialization.MapCodec<P> codec) {
        return registryHandler.register(ModernerBeta.createId(id), new InjectionPredicateType<>(codec));
    }

    @SuppressWarnings("unchecked")
    public static void init(IRegistryHandler<?> handler) {
        registryHandler = (IRegistryHandler<InjectionPredicateType<?>>) handler;

        ALL_OF = register("all_of", AllOfInjectionPredicate.CODEC);
        ANY_OF = register("any_of", AnyOfInjectionPredicate.CODEC);
        BELOW_SURFACE = register("below_surface", BelowSurfaceInjectionPredicate.CODEC);
        BIOME_IN_SET = register("biome_in_set", BiomeInSetInjectionPredicate.CODEC);
        BIOME_IS_TAG = register("biome_is_tag", BiomeIsTagInjectionPredicate.CODEC);
        INVERTED = register("inverted", InvertedInjectionPredicate.CODEC);
        OUT_OF_BOUNDS = register("out_of_bounds", OutOfBoundsInjectionPredicate.CODEC);
        SURFACE_BELOW_SEA_LEVEL = register("surface_below_sea_level", SurfaceBelowSeaLevelInjectionPredicate.CODEC);
    }
}
