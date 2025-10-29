package mod.bluestaggo.modernerbeta.registry;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.api.world.BlockSourceCreator;
import mod.bluestaggo.modernerbeta.api.world.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.api.world.provider.BiomeProviderType;
import mod.bluestaggo.modernerbeta.api.world.provider.CaveBiomeProviderType;
import mod.bluestaggo.modernerbeta.api.world.provider.ChunkProviderType;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentType;
import mod.bluestaggo.modernerbeta.world.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerType;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates.BiomePredicateType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

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

    private static <T> Registry<T> register(ResourceKey<Registry<T>> key) {
        return registryHelper.createSimple(key).build();
    }

    private static <T> Registry<T> registerDefaulted(ResourceKey<Registry<T>> key, ResourceLocation defaultKey) {
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
    }
}
