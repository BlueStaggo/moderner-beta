package mod.bluestaggo.modernerbeta.registry;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.api.level.BlockSourceCreator;
import mod.bluestaggo.modernerbeta.api.level.provider.BiomeProviderType;
import mod.bluestaggo.modernerbeta.api.level.provider.CaveBiomeProviderType;
import mod.bluestaggo.modernerbeta.api.level.provider.ChunkProviderType;
import mod.bluestaggo.modernerbeta.level.biome.injection.injector.BiomeInjectorType;
import mod.bluestaggo.modernerbeta.level.biome.injection.predicates.InjectionPredicateType;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentType;
import mod.bluestaggo.modernerbeta.level.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.LayerType;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.predicates.BiomePredicateType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

public final class ModernBetaRegistries {
    private static IRegistryHelper registryHelper;

    public static Registry<SettingsComponentType<?>> SETTINGS_COMPONENT_TYPE;
    public static Registry<ChunkProviderType<?>> CHUNK;
    public static Registry<BiomeProviderType<?>> BIOME;
    public static Registry<CaveBiomeProviderType<?>> CAVE_BIOME;
    public static Registry<HeightConfig> HEIGHT_CONFIG;
    public static Registry<BlockSourceCreator> BLOCKSOURCE;
    public static Registry<LayerType<?>> FRACTAL_LAYER;
    public static Registry<BiomePredicateType<?>> BIOME_PREDICATE;
    public static Registry<BiomeInjectorType<?>> BIOME_INJECTOR;
    public static Registry<InjectionPredicateType<?>> INJECTION_PREDICATE;

    private static <T> Registry<T> register(ResourceKey<Registry<T>> key) {
        return registryHelper.createSimple(key).build();
    }

    private static <T> Registry<T> registerDefaulted(ResourceKey<Registry<T>> key, Identifier defaultKey) {
        return registryHelper.createDefaulted(key, defaultKey).build();
    }

    public static void makeRegistries(IRegistryHelper helper) {
        registryHelper = helper;

        SETTINGS_COMPONENT_TYPE = register(ModernBetaResourceKeys.SETTINGS_COMPONENT_TYPE);
        CHUNK = registerDefaulted(ModernBetaResourceKeys.CHUNK, ModernBetaBuiltInTypes.Chunk.NOISE_3D.id);
        BIOME = registerDefaulted(ModernBetaResourceKeys.BIOME, ModernBetaBuiltInTypes.Biome.BETA.id);
        CAVE_BIOME = registerDefaulted(ModernBetaResourceKeys.CAVE_BIOME, ModernBetaBuiltInTypes.CaveBiome.NONE.id);
        HEIGHT_CONFIG = registerDefaulted(ModernBetaResourceKeys.HEIGHT_CONFIG, ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_DEFAULT.id);
        BLOCKSOURCE = register(ModernBetaResourceKeys.BLOCKSOURCE);
        FRACTAL_LAYER = register(ModernBetaResourceKeys.FRACTAL_LAYER);
        BIOME_PREDICATE = register(ModernBetaResourceKeys.BIOME_PREDICATE);
        BIOME_INJECTOR = register(ModernBetaResourceKeys.BIOME_INJECTOR);
        INJECTION_PREDICATE = register(ModernBetaResourceKeys.INJECTION_PREDICATE);
    }
}
