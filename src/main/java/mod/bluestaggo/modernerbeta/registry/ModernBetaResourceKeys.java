package mod.bluestaggo.modernerbeta.registry;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.level.BlockSourceCreator;
import mod.bluestaggo.modernerbeta.api.level.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.api.level.provider.BiomeProviderType;
import mod.bluestaggo.modernerbeta.api.level.provider.CaveBiomeProviderType;
import mod.bluestaggo.modernerbeta.api.level.provider.ChunkProviderType;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentType;
import mod.bluestaggo.modernerbeta.level.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.LayerType;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.predicates.BiomePredicateType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class ModernBetaResourceKeys {
    public static final ResourceKey<Registry<ChunkProviderType<?>>> CHUNK = of("chunk_provider");
    public static final ResourceKey<Registry<BiomeProviderType<?>>> BIOME = of("biome_provider");
    public static final ResourceKey<Registry<CaveBiomeProviderType<?>>> CAVE_BIOME = of("cave_biome_provider");
    public static final ResourceKey<Registry<SurfaceConfig>> SURFACE_CONFIG = of("surface_config");
    public static final ResourceKey<Registry<HeightConfig>> HEIGHT_CONFIG = of("height_config");
    public static final ResourceKey<Registry<BlockSourceCreator>> BLOCKSOURCE = of("blocksource");
    public static final ResourceKey<Registry<ModernBetaSettingsPreset>> SETTINGS_PRESET = of("settings_preset");
    public static final ResourceKey<Registry<ModernBetaSettingsPresetCategory>> SETTINGS_PRESET_CATEGORY = of("settings_preset_category");
    public static final ResourceKey<Registry<LayerType<?>>> FRACTAL_LAYER = of("fractal_layer");
    public static final ResourceKey<Registry<BiomePredicateType<?>>> BIOME_PREDICATE = of("biome_predicate_type");
    public static final ResourceKey<Registry<SettingsComponentType<?>>> SETTINGS_COMPONENT_TYPE = of("settings_component_type");

    private static <T> ResourceKey<Registry<T>> of(String id) {
        return ResourceKey.createRegistryKey(ModernerBeta.createId(id));
    }
}
