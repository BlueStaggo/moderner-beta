package mod.bluestaggo.modernerbeta;

import mod.bluestaggo.modernerbeta.api.level.BlockSourceCreator;
import mod.bluestaggo.modernerbeta.api.level.provider.BiomeProviderType;
import mod.bluestaggo.modernerbeta.api.level.provider.CaveBiomeProviderType;
import mod.bluestaggo.modernerbeta.api.level.provider.ChunkProviderType;
import mod.bluestaggo.modernerbeta.level.biome.provider.*;
import mod.bluestaggo.modernerbeta.level.chunk.provider.ChunkProviderFinite2D;
import mod.bluestaggo.modernerbeta.level.chunk.provider.ChunkProviderInfdev227;
import mod.bluestaggo.modernerbeta.level.chunk.provider.ChunkProviderNoise3D;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.level.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.level.blocksource.BlockSourceDeepslate;
import mod.bluestaggo.modernerbeta.level.cavebiome.provider.CaveBiomeProviderNone;
import mod.bluestaggo.modernerbeta.level.cavebiome.provider.CaveBiomeProviderSingle;
import mod.bluestaggo.modernerbeta.level.cavebiome.provider.CaveBiomeProviderVoronoi;

import java.util.Collections;
import java.util.List;

/*
 * Registration of built-in providers for various things.
 *  
 */
@SuppressWarnings("unchecked")
public class ModernBetaBuiltInProviders {
    
    // Register default chunk providers
    public static void registerChunkProviders(IRegistryHandler<?> handler) {
        IRegistryHandler<ChunkProviderType<?>> registryHandler = (IRegistryHandler<ChunkProviderType<?>>) handler;

        registryHandler.register(ModernBetaBuiltInTypes.Chunk.INFDEV_227.id, new ChunkProviderType<>(
            ChunkProviderInfdev227::new, () -> List.of(
                SettingsComponentTypes.DEEPSLATE_GENERATION,
                SettingsComponentTypes.USE_SURFACE_RULES,
                SettingsComponentTypes.SEA_LEVEL,
                SettingsComponentTypes.CAVE_GENERATION,
                SettingsComponentTypes.INFDEV_227_STRUCTURES,
                SettingsComponentTypes.PERLIN_NOISE_SETTINGS
            )
        ));
        registryHandler.register(ModernBetaBuiltInTypes.Chunk.FINITE_2D.id, new ChunkProviderType<>(
            ChunkProviderFinite2D::new, () -> List.of(
                SettingsComponentTypes.DEEPSLATE_GENERATION,
                SettingsComponentTypes.USE_SURFACE_RULES,
                SettingsComponentTypes.CAVE_GENERATION,
                SettingsComponentTypes.FINITE_LEVEL_PROPERTIES,
                SettingsComponentTypes.FINITE_CAVE_GENERATION,
                SettingsComponentTypes.FINITE_NOISE,
                SettingsComponentTypes.FINITE_BEACHES,
                SettingsComponentTypes.FINITE_POOLS,
                SettingsComponentTypes.PERLIN_NOISE_SETTINGS,
                SettingsComponentTypes.SPAWN_INDEV_HOUSE
            )
        ));
        registryHandler.register(ModernBetaBuiltInTypes.Chunk.NOISE_3D.id, new ChunkProviderType<>(
            ChunkProviderNoise3D::new, () -> List.of(
                SettingsComponentTypes.DEEPSLATE_GENERATION,
                SettingsComponentTypes.USE_SURFACE_RULES,
                SettingsComponentTypes.SEA_LEVEL,
                SettingsComponentTypes.CAVE_GENERATION,
                SettingsComponentTypes.NOISE_SETTINGS,
                SettingsComponentTypes.NOISE_3D_SETTINGS,
                SettingsComponentTypes.PERLIN_NOISE_SETTINGS,
                SettingsComponentTypes.NOISE_SCALE,
                SettingsComponentTypes.NOISE_SLIDE,
                SettingsComponentTypes.NOISE_LANDMASS,
                SettingsComponentTypes.SURFACE_PROPERTIES,
                SettingsComponentTypes.FORCED_BIOME_HEIGHT,
                SettingsComponentTypes.ISLES_PROPERTIES,
                SettingsComponentTypes.WORLD_BORDER
            )
        ));
    }
    
    // Register default biome providers
    public static void registerBiomeProviders(IRegistryHandler<?> handler) {
        IRegistryHandler<BiomeProviderType<?>> registryHandler = (IRegistryHandler<BiomeProviderType<?>>) handler;

        registryHandler.register(ModernBetaBuiltInTypes.Biome.BETA.id, new BiomeProviderType<>(
            BiomeProviderBeta::new, BiomeProviderBeta.class, () -> List.of(
                SettingsComponentTypes.TEMPERATURE_HEIGHT_SCALING,
                SettingsComponentTypes.USE_OCEAN_BIOMES,
                SettingsComponentTypes.BIOME_INJECTION_THRESHOLDS,
                SettingsComponentTypes.CLIMATE_DISTRIBUTION,
                SettingsComponentTypes.CLIMATE_SCALE,
                SettingsComponentTypes.CLIMATE_MAPPINGS
            )
        ));
        registryHandler.register(ModernBetaBuiltInTypes.Biome.SINGLE.id, new BiomeProviderType<>(
            BiomeProviderSingle::new, BiomeProviderSingle.class, () -> List.of(
                SettingsComponentTypes.TEMPERATURE_HEIGHT_SCALING,
                SettingsComponentTypes.SINGLE_BIOME
            )
        ));
        registryHandler.register(ModernBetaBuiltInTypes.Biome.PE.id, new BiomeProviderType<>(
            BiomeProviderPE::new, BiomeProviderPE.class, () -> List.of(
                SettingsComponentTypes.TEMPERATURE_HEIGHT_SCALING,
                SettingsComponentTypes.USE_OCEAN_BIOMES,
                SettingsComponentTypes.BIOME_INJECTION_THRESHOLDS,
                SettingsComponentTypes.CLIMATE_DISTRIBUTION,
                SettingsComponentTypes.CLIMATE_SCALE,
                SettingsComponentTypes.CLIMATE_MAPPINGS
            )
        ));
        registryHandler.register(ModernBetaBuiltInTypes.Biome.VORONOI.id, new BiomeProviderType<>(
            BiomeProviderVoronoi::new, BiomeProviderVoronoi.class, () -> List.of(
                SettingsComponentTypes.TEMPERATURE_HEIGHT_SCALING,
                SettingsComponentTypes.USE_OCEAN_BIOMES,
                SettingsComponentTypes.BIOME_INJECTION_THRESHOLDS,
                SettingsComponentTypes.VORONOI_POINTS,
                SettingsComponentTypes.CLIMATE_SCALE
            )
        ));
        registryHandler.register(ModernBetaBuiltInTypes.Biome.FRACTAL.id, new BiomeProviderType<>(
            BiomeProviderFractal::new, BiomeProviderFractal.class, () -> List.of(
                SettingsComponentTypes.TEMPERATURE_HEIGHT_SCALING,
                SettingsComponentTypes.FRACTAL_LAYERS,
                SettingsComponentTypes.USE_OCEAN_BIOMES,
                SettingsComponentTypes.BIOME_INJECTION_THRESHOLDS
            )
        ));
        registryHandler.register(ModernBetaBuiltInTypes.Biome.BETA_FRACTAL.id, new BiomeProviderType<>(
            BiomeProviderBetaFractal::new, BiomeProviderBetaFractal.class, () -> List.of(
                SettingsComponentTypes.TEMPERATURE_HEIGHT_SCALING,
                SettingsComponentTypes.CLIMATE_DISTRIBUTION,
                SettingsComponentTypes.FRACTAL_LAYERS
            )
        ));
    }
    
    // Register default cave biome providers
    public static void registerCaveBiomeProviders(IRegistryHandler<?> handler) {
        IRegistryHandler<CaveBiomeProviderType<?>> registryHandler = (IRegistryHandler<CaveBiomeProviderType<?>>) handler;

        registryHandler.register(ModernBetaBuiltInTypes.CaveBiome.NONE.id, new CaveBiomeProviderType<>(
            CaveBiomeProviderNone::new,
            Collections::emptyList
        ));
        registryHandler.register(ModernBetaBuiltInTypes.CaveBiome.SINGLE.id, new CaveBiomeProviderType<>(
            CaveBiomeProviderSingle::new, () -> List.of(
                SettingsComponentTypes.SINGLE_BIOME
            )
        ));
        registryHandler.register(ModernBetaBuiltInTypes.CaveBiome.VORONOI.id, new CaveBiomeProviderType<>(
            CaveBiomeProviderVoronoi::new, () -> List.of(
                SettingsComponentTypes.CAVE_BIOME_VORONOI
            )
        ));
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
}
