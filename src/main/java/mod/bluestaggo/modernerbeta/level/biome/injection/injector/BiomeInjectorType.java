package mod.bluestaggo.modernerbeta.level.biome.injection.injector;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;

public record BiomeInjectorType<I extends BiomeInjector>(com.mojang.serialization.MapCodec<I> codec) {
    private static IRegistryHandler<BiomeInjectorType<?>> registryHandler;

    public static BiomeInjectorType<Cache2DBiomeInjector> CACHE_2D;
    public static BiomeInjectorType<CaveBiomeInjector> CAVE_BIOME;
    public static BiomeInjectorType<ConstantBiomeInjector> CONSTANT_BIOME;
    public static BiomeInjectorType<DeepOceanBiomeInjector> DEEP_OCEAN;
    public static BiomeInjectorType<OceanBiomeInjector> OCEAN;
    public static BiomeInjectorType<PredicateBiomeInjector> PREDICATE;

    private static <I extends BiomeInjector> BiomeInjectorType<I> register(String id, com.mojang.serialization.MapCodec<I> codec) {
        return registryHandler.register(ModernerBeta.createId(id), new BiomeInjectorType<>(codec));
    }

    @SuppressWarnings("unchecked")
    public static void init(IRegistryHandler<?> handler) {
        registryHandler = (IRegistryHandler<BiomeInjectorType<?>>) handler;

        CACHE_2D = register("cache_2d", Cache2DBiomeInjector.CODEC);
        CAVE_BIOME = register("cave_biome", CaveBiomeInjector.CODEC);
        CONSTANT_BIOME = register("constant_biome", ConstantBiomeInjector.CODEC);
        DEEP_OCEAN = register("deep_ocean", DeepOceanBiomeInjector.CODEC);
        OCEAN = register("ocean", OceanBiomeInjector.CODEC);
        PREDICATE = register("predicate", PredicateBiomeInjector.CODEC);
    }
}
