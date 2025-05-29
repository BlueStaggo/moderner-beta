package mod.bluestaggo.modernerbeta;

import mod.bluestaggo.modernerbeta.api.world.BlockSourceCreator;
import mod.bluestaggo.modernerbeta.api.world.provider.BiomeProviderCreator;
import mod.bluestaggo.modernerbeta.api.world.provider.CaveBiomeProviderCreator;
import mod.bluestaggo.modernerbeta.api.world.provider.ChunkProviderCreator;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.api.world.chunk.noise.NoisePostProcessor;
import mod.bluestaggo.modernerbeta.api.world.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresets;
import mod.bluestaggo.modernerbeta.world.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.world.biome.provider.*;
import mod.bluestaggo.modernerbeta.world.blocksource.BlockSourceDeepslate;
import mod.bluestaggo.modernerbeta.world.cavebiome.provider.CaveBiomeProviderNone;
import mod.bluestaggo.modernerbeta.world.cavebiome.provider.CaveBiomeProviderSingle;
import mod.bluestaggo.modernerbeta.world.cavebiome.provider.CaveBiomeProviderVoronoi;
import mod.bluestaggo.modernerbeta.world.chunk.provider.*;

/*
 * Registration of built-in providers for various things.
 *  
 */
@SuppressWarnings("unchecked")
public class ModernBetaBuiltInProviders {
    
    // Register default chunk providers
    public static void registerChunkProviders(IRegistryHandler<?> handler) {
        IRegistryHandler<ChunkProviderCreator> registryHandler = (IRegistryHandler<ChunkProviderCreator>) handler;

        registryHandler.register(ModernBetaBuiltInTypes.Chunk.BETA.id, ChunkProviderBeta::new);
        registryHandler.register(ModernBetaBuiltInTypes.Chunk.SKYLANDS.id, ChunkProviderSky::new);
        registryHandler.register(ModernBetaBuiltInTypes.Chunk.ALPHA.id, ChunkProviderAlpha::new);
        registryHandler.register(ModernBetaBuiltInTypes.Chunk.INFDEV_611.id, ChunkProviderInfdev611::new);
        registryHandler.register(ModernBetaBuiltInTypes.Chunk.INFDEV_420.id, ChunkProviderInfdev420::new);
        registryHandler.register(ModernBetaBuiltInTypes.Chunk.INFDEV_415.id, ChunkProviderInfdev415::new);
        registryHandler.register(ModernBetaBuiltInTypes.Chunk.INFDEV_227.id, ChunkProviderInfdev227::new);
        registryHandler.register(ModernBetaBuiltInTypes.Chunk.INDEV.id, ChunkProviderIndev::new);
        registryHandler.register(ModernBetaBuiltInTypes.Chunk.CLASSIC_0_30.id, ChunkProviderClassic030::new);
        registryHandler.register(ModernBetaBuiltInTypes.Chunk.PE.id, ChunkProviderPE::new);
        registryHandler.register(ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id, ChunkProviderEarlyRelease::new);
        registryHandler.register(ModernBetaBuiltInTypes.Chunk.MAJOR_RELEASE.id, ChunkProviderMajorRelease::new);
    }
    
    // Register default biome providers
    public static void registerBiomeProviders(IRegistryHandler<?> handler) {
        IRegistryHandler<BiomeProviderCreator<?>> registryHandler = (IRegistryHandler<BiomeProviderCreator<?>>) handler;

        registryHandler.register(ModernBetaBuiltInTypes.Biome.BETA.id, new BiomeProviderCreator<>(BiomeProviderBeta::new, BiomeProviderBeta.class));
        registryHandler.register(ModernBetaBuiltInTypes.Biome.SINGLE.id, new BiomeProviderCreator<>(BiomeProviderSingle::new, BiomeProviderSingle.class));
        registryHandler.register(ModernBetaBuiltInTypes.Biome.PE.id, new BiomeProviderCreator<>(BiomeProviderPE::new, BiomeProviderPE.class));
        registryHandler.register(ModernBetaBuiltInTypes.Biome.VORONOI.id, new BiomeProviderCreator<>(BiomeProviderVoronoi::new, BiomeProviderVoronoi.class));
        registryHandler.register(ModernBetaBuiltInTypes.Biome.FRACTAL.id, new BiomeProviderCreator<>(BiomeProviderFractal::new, BiomeProviderFractal.class));
    }
    
    // Register default cave biome providers
    public static void registerCaveBiomeProviders(IRegistryHandler<?> handler) {
        IRegistryHandler<CaveBiomeProviderCreator> registryHandler = (IRegistryHandler<CaveBiomeProviderCreator>) handler;

        registryHandler.register(ModernBetaBuiltInTypes.CaveBiome.NONE.id, CaveBiomeProviderNone::new);
        registryHandler.register(ModernBetaBuiltInTypes.CaveBiome.SINGLE.id, CaveBiomeProviderSingle::new);
        registryHandler.register(ModernBetaBuiltInTypes.CaveBiome.VORONOI.id, CaveBiomeProviderVoronoi::new);
    }
    
    public static void registerNoisePostProcessors(IRegistryHandler<?> handler) {
        IRegistryHandler<NoisePostProcessor> registryHandler = (IRegistryHandler<NoisePostProcessor>) handler;
        registryHandler.register(ModernBetaBuiltInTypes.NoisePostProcessor.NONE.id, NoisePostProcessor.DEFAULT);
    }
    
    public static void registerSurfaceConfigs(IRegistryHandler<?> handler) {
        IRegistryHandler<SurfaceConfig> registryHandler = (IRegistryHandler<SurfaceConfig>) handler;

        registryHandler.register(ModernBetaBuiltInTypes.SurfaceConfig.SAND.id, SurfaceConfig.SAND);
        registryHandler.register(ModernBetaBuiltInTypes.SurfaceConfig.RED_SAND.id, SurfaceConfig.RED_SAND);
        registryHandler.register(ModernBetaBuiltInTypes.SurfaceConfig.BADLANDS.id, SurfaceConfig.BADLANDS);
        registryHandler.register(ModernBetaBuiltInTypes.SurfaceConfig.NETHER.id, SurfaceConfig.NETHER);
        registryHandler.register(ModernBetaBuiltInTypes.SurfaceConfig.WARPED_NYLIUM.id, SurfaceConfig.WARPED_NYLIUM);
        registryHandler.register(ModernBetaBuiltInTypes.SurfaceConfig.CRIMSON_NYLIUM.id, SurfaceConfig.CRIMSON_NYLIUM);
        registryHandler.register(ModernBetaBuiltInTypes.SurfaceConfig.BASALT.id, SurfaceConfig.BASALT);
        registryHandler.register(ModernBetaBuiltInTypes.SurfaceConfig.SOUL_SOIL.id, SurfaceConfig.SOUL_SOIL);
        registryHandler.register(ModernBetaBuiltInTypes.SurfaceConfig.THEEND.id, SurfaceConfig.THEEND);
        registryHandler.register(ModernBetaBuiltInTypes.SurfaceConfig.GRASS.id, SurfaceConfig.GRASS);
        registryHandler.register(ModernBetaBuiltInTypes.SurfaceConfig.MUD.id, SurfaceConfig.MUD);
        registryHandler.register(ModernBetaBuiltInTypes.SurfaceConfig.MYCELIUM.id, SurfaceConfig.MYCELIUM);
        registryHandler.register(ModernBetaBuiltInTypes.SurfaceConfig.PODZOL.id, SurfaceConfig.PODZOL);
        registryHandler.register(ModernBetaBuiltInTypes.SurfaceConfig.STONE.id, SurfaceConfig.STONE);
        registryHandler.register(ModernBetaBuiltInTypes.SurfaceConfig.SNOW.id, SurfaceConfig.SNOW);
        registryHandler.register(ModernBetaBuiltInTypes.SurfaceConfig.SNOW_DIRT.id, SurfaceConfig.SNOW_DIRT);
        registryHandler.register(ModernBetaBuiltInTypes.SurfaceConfig.SNOW_PACKED_ICE.id, SurfaceConfig.SNOW_PACKED_ICE);
        registryHandler.register(ModernBetaBuiltInTypes.SurfaceConfig.SNOW_STONE.id, SurfaceConfig.SNOW_STONE);
    }

    public static void registerHeightConfigs(IRegistryHandler<?> handler) {
        IRegistryHandler<HeightConfig> registryHandler = (IRegistryHandler<HeightConfig>) handler;

        registryHandler.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_DEFAULT.id, HeightConfig.DEFAULT);
        registryHandler.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_OCEAN.id, HeightConfig.OCEAN);
        registryHandler.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_DESERT.id, HeightConfig.DESERT);
        registryHandler.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_EXTREME_HILLS.id, HeightConfig.EXTREME_HILLS);
        registryHandler.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_BETA_HILLS.id, HeightConfig.BETA_HILLS);
        registryHandler.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_TAIGA.id, HeightConfig.TAIGA);
        registryHandler.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_SWAMPLAND.id, HeightConfig.SWAMPLAND);
        registryHandler.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_RIVER.id, HeightConfig.RIVER);
        registryHandler.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_MOUNTAINS.id, HeightConfig.MOUNTAINS);
        registryHandler.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_MUSHROOM_ISLAND.id, HeightConfig.MUSHROOM_ISLAND);
        registryHandler.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_MUSHROOM_ISLAND_SHORE.id, HeightConfig.MUSHROOM_ISLAND_SHORE);
        registryHandler.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_BEACH.id, HeightConfig.BEACH);
        registryHandler.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_HILLS.id, HeightConfig.HILLS);
        registryHandler.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_SHORT_HILLS.id, HeightConfig.SHORT_HILLS);
        registryHandler.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_EXTREME_HILLS_EDGE.id, HeightConfig.EXTREME_HILLS_EDGE);
        registryHandler.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_JUNGLE.id, HeightConfig.JUNGLE);
        registryHandler.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_JUNGLE_HILLS.id, HeightConfig.JUNGLE_HILLS);
        registryHandler.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_PLATEAU.id, HeightConfig.PLATEAU);
        registryHandler.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_SWAMPLAND_HILLS.id, HeightConfig.SWAMPLAND_HILLS);
        registryHandler.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_PLATEAU_HILL.id, HeightConfig.PLATEAU_HILL);
        registryHandler.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_DEEP_OCEAN.id, HeightConfig.DEEP_OCEAN);
    }
    
    public static void registerBlockSources(IRegistryHandler<?> handler) {
        IRegistryHandler<BlockSourceCreator> registryHandler = (IRegistryHandler<BlockSourceCreator>) handler;
        registryHandler.register(ModernBetaBuiltInTypes.BlockSource.DEEPSLATE.id, BlockSourceDeepslate::new);
    }
    
    public static void registerSettingsPresets(IRegistryHandler<?> handler) {
        IRegistryHandler<ModernBetaSettingsPreset> registryHandler = (IRegistryHandler<ModernBetaSettingsPreset>) handler;

        registryHandler.register(ModernBetaBuiltInTypes.Preset.BETA_1_7_3.id, ModernBetaSettingsPresets.PRESET_BETA_1_7_3);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.BETA_1_1_02.id, ModernBetaSettingsPresets.PRESET_BETA_1_1_02);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.SKYLANDS.id, ModernBetaSettingsPresets.PRESET_SKYLANDS);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.ALPHA_1_1_2_01.id, ModernBetaSettingsPresets.PRESET_ALPHA);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.INFDEV_611.id, ModernBetaSettingsPresets.PRESET_INFDEV_611);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.INFDEV_420.id, ModernBetaSettingsPresets.PRESET_INFDEV_420);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.INFDEV_415.id, ModernBetaSettingsPresets.PRESET_INFDEV_415);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.INFDEV_325.id, ModernBetaSettingsPresets.PRESET_INFDEV_325);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.INFDEV_227.id, ModernBetaSettingsPresets.PRESET_INFDEV_227);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.INDEV.id, ModernBetaSettingsPresets.PRESET_INDEV);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.CLASSIC_0_30.id, ModernBetaSettingsPresets.PRESET_CLASSIC);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.CLASSIC_0_0_14A_08.id, ModernBetaSettingsPresets.PRESET_CLASSIC_14A_08);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.PE.id, ModernBetaSettingsPresets.PRESET_PE);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.BETA_1_8_1.id, ModernBetaSettingsPresets.PRESET_BETA_1_8_1);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.BETA_1_9_PRE_3.id, ModernBetaSettingsPresets.PRESET_BETA_1_9_PRE_3);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.RELEASE_1_0_0.id, ModernBetaSettingsPresets.PRESET_RELEASE_1_0_0);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.RELEASE_1_1.id, ModernBetaSettingsPresets.PRESET_RELEASE_1_1);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.RELEASE_1_2_5.id, ModernBetaSettingsPresets.PRESET_RELEASE_1_2_5);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.RELEASE_1_6_4.id, ModernBetaSettingsPresets.PRESET_RELEASE_1_6_4);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.RELEASE_1_12_2.id, ModernBetaSettingsPresets.PRESET_RELEASE_1_12_2);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.RELEASE_1_17_1.id, ModernBetaSettingsPresets.PRESET_RELEASE_1_17_1);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.BETA_SKYLANDS.id, ModernBetaSettingsPresets.PRESET_BETA_SKYLANDS);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.BETA_ISLES.id, ModernBetaSettingsPresets.PRESET_BETA_ISLES);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.BETA_WATER_WORLD.id, ModernBetaSettingsPresets.PRESET_BETA_WATER_WORLD);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.BETA_ISLE_LAND.id, ModernBetaSettingsPresets.PRESET_BETA_ISLE_LAND);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.BETA_CAVE_DELIGHT.id, ModernBetaSettingsPresets.PRESET_BETA_CAVE_DELIGHT);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.BETA_MOUNTAIN_MADNESS.id, ModernBetaSettingsPresets.PRESET_BETA_MOUNTAIN_MADNESS);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.BETA_DROUGHT.id, ModernBetaSettingsPresets.PRESET_BETA_DROUGHT);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.BETA_CAVE_CHAOS.id, ModernBetaSettingsPresets.PRESET_BETA_CAVE_CHAOS);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.BETA_LARGE_BIOMES.id, ModernBetaSettingsPresets.PRESET_BETA_LARGE_BIOMES);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.BETA_XBOX_LEGACY.id, ModernBetaSettingsPresets.PRESET_BETA_XBOX_LEGACY);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.BETA_SURVIVAL_ISLAND.id, ModernBetaSettingsPresets.PRESET_BETA_SURVIVAL_ISLAND);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.BETA_VANILLA.id, ModernBetaSettingsPresets.PRESET_BETA_VANILLA);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.RELEASE_HYBRID.id, ModernBetaSettingsPresets.PRESET_RELEASE_HYBRID);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.SNOW_AINT_SNOWIER.id, ModernBetaSettingsPresets.PRESET_SNOW_AINT_SNOWIER);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.ALPHA_WINTER.id, ModernBetaSettingsPresets.PRESET_ALPHA_WINTER);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.INDEV_PARADISE.id, ModernBetaSettingsPresets.PRESET_INDEV_PARADISE);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.INDEV_WOODS.id, ModernBetaSettingsPresets.PRESET_INDEV_WOODS);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.INDEV_HELL.id, ModernBetaSettingsPresets.PRESET_INDEV_HELL);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.WATER_WORLD.id, ModernBetaSettingsPresets.PRESET_WATER_WORLD);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.ISLE_LAND.id, ModernBetaSettingsPresets.PRESET_ISLE_LAND);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.CAVE_DELIGHT.id, ModernBetaSettingsPresets.PRESET_CAVE_DELIGHT);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.MOUNTAIN_MADNESS.id, ModernBetaSettingsPresets.PRESET_MOUNTAIN_MADNESS);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.DROUGHT.id, ModernBetaSettingsPresets.PRESET_DROUGHT);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.CAVE_CHAOS.id, ModernBetaSettingsPresets.PRESET_CAVE_CHAOS);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.BETA_1_8_1_LARGE_BIOMES.id, ModernBetaSettingsPresets.PRESET_BETA_1_8_1_LARGE_BIOMES);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.BETA_1_9_PRE_3_LARGE_BIOMES.id, ModernBetaSettingsPresets.PRESET_BETA_1_9_PRE_3_LARGE_BIOMES);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.RELEASE_1_0_0_LARGE_BIOMES.id, ModernBetaSettingsPresets.PRESET_RELEASE_1_0_0_LARGE_BIOMES);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.RELEASE_1_1_LARGE_BIOMES.id, ModernBetaSettingsPresets.PRESET_RELEASE_1_1_LARGE_BIOMES);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.RELEASE_1_2_5_LARGE_BIOMES.id, ModernBetaSettingsPresets.PRESET_RELEASE_1_2_5_LARGE_BIOMES);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.RELEASE_1_6_4_LARGE_BIOMES.id, ModernBetaSettingsPresets.PRESET_RELEASE_1_6_4_LARGE_BIOMES);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.RELEASE_1_12_2_LARGE_BIOMES.id, ModernBetaSettingsPresets.PRESET_RELEASE_1_12_2_LARGE_BIOMES);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.RELEASE_1_17_1_LARGE_BIOMES.id, ModernBetaSettingsPresets.PRESET_RELEASE_1_17_1_LARGE_BIOMES);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.RELEASE_HYBRID_LARGE_BIOMES.id, ModernBetaSettingsPresets.PRESET_RELEASE_HYBRID_LARGE_BIOMES);
        registryHandler.register(ModernBetaBuiltInTypes.Preset.SNOW_AINT_SNOWIER_LARGE_BIOMES.id, ModernBetaSettingsPresets.PRESET_SNOW_AINT_SNOWIER_LARGE_BIOMES);
    }

    public static void registerSettingsPresetCategories(IRegistryHandler<?> handler) {
        IRegistryHandler<ModernBetaSettingsPresetCategory> registryHandler = (IRegistryHandler<ModernBetaSettingsPresetCategory>) handler;

        registryHandler.register(ModernBetaBuiltInTypes.PresetCategory.BETA.id, ModernBetaSettingsPresetCategory.BETA);
        registryHandler.register(ModernBetaBuiltInTypes.PresetCategory.ALPHA_INFDEV.id, ModernBetaSettingsPresetCategory.ALPHA_INFDEV);
        registryHandler.register(ModernBetaBuiltInTypes.PresetCategory.FINITE.id, ModernBetaSettingsPresetCategory.FINITE);
        registryHandler.register(ModernBetaBuiltInTypes.PresetCategory.EARLY_RELEASE.id, ModernBetaSettingsPresetCategory.EARLY_RELEASE);
        registryHandler.register(ModernBetaBuiltInTypes.PresetCategory.EARLY_RELEASE_LARGE_BIOMES.id, ModernBetaSettingsPresetCategory.EARLY_RELEASE_LARGE_BIOMES);
        registryHandler.register(ModernBetaBuiltInTypes.PresetCategory.MAJOR_RELEASE.id, ModernBetaSettingsPresetCategory.MAJOR_RELEASE);
        registryHandler.register(ModernBetaBuiltInTypes.PresetCategory.BETA_CUSTOM.id, ModernBetaSettingsPresetCategory.BETA_CUSTOM);
        registryHandler.register(ModernBetaBuiltInTypes.PresetCategory.RELEASE_CUSTOM.id, ModernBetaSettingsPresetCategory.RELEASE_CUSTOM);
    }
}
