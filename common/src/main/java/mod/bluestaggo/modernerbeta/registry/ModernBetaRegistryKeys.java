package mod.bluestaggo.modernerbeta.registry;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.world.BlockSourceCreator;
import mod.bluestaggo.modernerbeta.api.world.chunk.noise.NoisePostProcessor;
import mod.bluestaggo.modernerbeta.api.world.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.api.world.provider.BiomeProviderCreator;
import mod.bluestaggo.modernerbeta.api.world.provider.CaveBiomeProviderCreator;
import mod.bluestaggo.modernerbeta.api.world.provider.ChunkProviderCreator;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import mod.bluestaggo.modernerbeta.world.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ConfiguredLayers;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerType;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates.BiomePredicateType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public final class ModernBetaRegistryKeys {
    public static final RegistryKey<Registry<ChunkProviderCreator>> CHUNK = RegistryKey.ofRegistry(ModernerBeta.createId("chunk_provider"));
    public static final RegistryKey<Registry<BiomeProviderCreator<?>>> BIOME = RegistryKey.ofRegistry(ModernerBeta.createId("biome_provider"));
    public static final RegistryKey<Registry<CaveBiomeProviderCreator>> CAVE_BIOME = RegistryKey.ofRegistry(ModernerBeta.createId("cave_biome_provider"));
    public static final RegistryKey<Registry<NoisePostProcessor>> NOISE_POST_PROCESSOR = RegistryKey.ofRegistry(ModernerBeta.createId("noise_post_processor"));
    public static final RegistryKey<Registry<SurfaceConfig>> SURFACE_CONFIG = RegistryKey.ofRegistry(ModernerBeta.createId("surface_config"));
    public static final RegistryKey<Registry<HeightConfig>> HEIGHT_CONFIG = RegistryKey.ofRegistry(ModernerBeta.createId("height_config"));
    public static final RegistryKey<Registry<BlockSourceCreator>> BLOCKSOURCE = RegistryKey.ofRegistry(ModernerBeta.createId("blocksource"));
    public static final RegistryKey<Registry<ModernBetaSettingsPreset>> SETTINGS_PRESET = RegistryKey.ofRegistry(ModernerBeta.createId("settings_preset"));
    public static final RegistryKey<Registry<ModernBetaSettingsPresetCategory>> SETTINGS_PRESET_CATEGORY = RegistryKey.ofRegistry(ModernerBeta.createId("settings_preset_category"));
    public static final RegistryKey<Registry<LayerType<?>>> FRACTAL_LAYER = RegistryKey.ofRegistry(ModernerBeta.createId("fractal_layer"));
    public static final RegistryKey<Registry<BiomePredicateType<?>>> BIOME_PREDICATE = RegistryKey.ofRegistry(ModernerBeta.createId("biome_predicate_type"));
}
