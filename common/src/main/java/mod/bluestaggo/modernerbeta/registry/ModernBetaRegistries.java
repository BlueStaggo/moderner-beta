package mod.bluestaggo.modernerbeta.registry;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.api.world.BlockSourceCreator;
import mod.bluestaggo.modernerbeta.api.world.chunk.noise.NoisePostProcessor;
import mod.bluestaggo.modernerbeta.api.world.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.api.world.provider.BiomeProviderCreator;
import mod.bluestaggo.modernerbeta.api.world.provider.CaveBiomeProviderCreator;
import mod.bluestaggo.modernerbeta.api.world.provider.ChunkProviderCreator;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import mod.bluestaggo.modernerbeta.world.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerType;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates.BiomePredicateType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public final class ModernBetaRegistries {
    private static RegistryHelper registryHelper;
    public static Registry<ChunkProviderCreator> CHUNK;
    public static Registry<BiomeProviderCreator> BIOME;
    public static Registry<CaveBiomeProviderCreator> CAVE_BIOME;
    public static Registry<NoisePostProcessor> NOISE_POST_PROCESSOR;
    public static Registry<SurfaceConfig> SURFACE_CONFIG;
    public static Registry<HeightConfig> HEIGHT_CONFIG;
    public static Registry<BlockSourceCreator> BLOCKSOURCE;
    public static Registry<ModernBetaSettingsPreset> SETTINGS_PRESET;
    public static Registry<ModernBetaSettingsPresetCategory> SETTINGS_PRESET_CATEGORY;
    public static Registry<LayerType<?>> FRACTAL_LAYER;
    public static Registry<BiomePredicateType<?>> BIOME_PREDICATE;

    private static <T> Registry<T> register(RegistryKey<Registry<T>> key) {
        return registryHelper.createSimple(key).build();
    }

    private static <T> Registry<T> registerDefaulted(RegistryKey<Registry<T>> key, Identifier defaultKey) {
        return registryHelper.createDefaulted(key, defaultKey).build();
    }

    public static void makeRegistries(RegistryHelper helper) {
        registryHelper = helper;

        CHUNK = registerDefaulted(ModernBetaRegistryKeys.CHUNK, ModernBetaBuiltInTypes.Chunk.BETA.id);
        BIOME = registerDefaulted(ModernBetaRegistryKeys.BIOME, ModernBetaBuiltInTypes.Biome.BETA.id);
        CAVE_BIOME = registerDefaulted(ModernBetaRegistryKeys.CAVE_BIOME, ModernBetaBuiltInTypes.CaveBiome.NONE.id);
        NOISE_POST_PROCESSOR = register(ModernBetaRegistryKeys.NOISE_POST_PROCESSOR);
        SURFACE_CONFIG = register(ModernBetaRegistryKeys.SURFACE_CONFIG);
        HEIGHT_CONFIG = registerDefaulted(ModernBetaRegistryKeys.HEIGHT_CONFIG, ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_DEFAULT.id);
        BLOCKSOURCE = register(ModernBetaRegistryKeys.BLOCKSOURCE);
        SETTINGS_PRESET = registerDefaulted(ModernBetaRegistryKeys.SETTINGS_PRESET, ModernBetaBuiltInTypes.Preset.BETA_1_7_3.id);
        SETTINGS_PRESET_CATEGORY = registerDefaulted(ModernBetaRegistryKeys.SETTINGS_PRESET_CATEGORY, ModernBetaBuiltInTypes.PresetCategory.BETA.id);
        FRACTAL_LAYER = register(ModernBetaRegistryKeys.FRACTAL_LAYER);
        BIOME_PREDICATE = register(ModernBetaRegistryKeys.BIOME_PREDICATE);
    }
}
