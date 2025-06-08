package mod.bluestaggo.modernerbeta.registry;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.world.BlockSourceCreator;
import mod.bluestaggo.modernerbeta.api.world.chunk.noise.NoisePostProcessor;
import mod.bluestaggo.modernerbeta.api.world.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.api.world.provider.BiomeProviderType;
import mod.bluestaggo.modernerbeta.api.world.provider.CaveBiomeProviderType;
import mod.bluestaggo.modernerbeta.api.world.provider.ChunkProviderType;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentType;
import mod.bluestaggo.modernerbeta.world.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerType;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates.BiomePredicateType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public final class ModernBetaRegistryKeys {
    public static final RegistryKey<Registry<ChunkProviderType<?>>> CHUNK = of("chunk_provider");
    public static final RegistryKey<Registry<BiomeProviderType<?>>> BIOME = of("biome_provider");
    public static final RegistryKey<Registry<CaveBiomeProviderType<?>>> CAVE_BIOME = of("cave_biome_provider");
    public static final RegistryKey<Registry<SurfaceConfig>> SURFACE_CONFIG = of("surface_config");
    public static final RegistryKey<Registry<HeightConfig>> HEIGHT_CONFIG = of("height_config");
    public static final RegistryKey<Registry<BlockSourceCreator>> BLOCKSOURCE = of("blocksource");
    public static final RegistryKey<Registry<ModernBetaSettingsPreset>> SETTINGS_PRESET = of("settings_preset");
    public static final RegistryKey<Registry<ModernBetaSettingsPresetCategory>> SETTINGS_PRESET_CATEGORY = of("settings_preset_category");
    public static final RegistryKey<Registry<LayerType<?>>> FRACTAL_LAYER = of("fractal_layer");
    public static final RegistryKey<Registry<BiomePredicateType<?>>> BIOME_PREDICATE = of("biome_predicate_type");
    public static final RegistryKey<Registry<SettingsComponentType<?>>> SETTINGS_COMPONENT_TYPE = of("settings_component_type");

    private static <T> RegistryKey<Registry<T>> of(String id) {
        return RegistryKey.ofRegistry(ModernerBeta.createId(id));
    }
}
