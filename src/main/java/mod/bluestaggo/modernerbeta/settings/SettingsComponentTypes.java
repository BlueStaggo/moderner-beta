package mod.bluestaggo.modernerbeta.settings;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.settings.component.*;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.world.biome.provider.climate.ClimateMapping;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ConfiguredLayers;
import mod.bluestaggo.modernerbeta.world.biome.voronoi.VoronoiPointBiome;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public class SettingsComponentTypes {
    private static IRegistryHandler<SettingsComponentType<?>> registryHandler;

    public static SettingsComponentType<Identifier> PROVIDER;

    // Chunk provider
    public static SettingsComponentType<DeepslateGeneration> DEEPSLATE_GENERATION;
    public static SettingsComponentType<Boolean> USE_SURFACE_RULES;
    public static SettingsComponentType<Integer> SEA_LEVEL_OFFSET;
    public static SettingsComponentType<CaveGeneration> CAVE_GENERATION;
    public static SettingsComponentType<NoiseScale> NOISE_SCALE;
    public static SettingsComponentType<NoiseSlide> NOISE_SLIDE;
    public static SettingsComponentType<ForcedBiomeHeight> FORCED_BIOME_HEIGHT;
    public static SettingsComponentType<Infdev227Structures> INFDEV_227_STRUCTURES;
    public static SettingsComponentType<FiniteLevelProperties> FINITE_LEVEL_PROPERTIES;
    public static SettingsComponentType<FiniteCaveGeneration> FINITE_CAVE_GENERATION;
    public static SettingsComponentType<FiniteNoise> FINITE_NOISE;
    public static SettingsComponentType<FiniteBeaches> FINITE_BEACHES;
    public static SettingsComponentType<FinitePools> FINITE_POOLS;
    public static SettingsComponentType<Boolean> SPAWN_INDEV_HOUSE;
    public static SettingsComponentType<IslesProperties> ISLES_PROPERTIES;

    // Biome provider
    public static SettingsComponentType<Identifier> SINGLE_BIOME;
    public static SettingsComponentType<ClimateScale> CLIMATE_SCALE;
    public static SettingsComponentType<Map<String, ClimateMapping>> CLIMATE_MAPPINGS;
    public static SettingsComponentType<List<VoronoiPointBiome>> VORONOI_POINTS;
    public static SettingsComponentType<ConfiguredLayers> FRACTAL_LAYERS;
    public static SettingsComponentType<Boolean> USE_OCEAN_BIOMES;

    // Cave biome provider
    public static SettingsComponentType<CaveBiomeVoronoi> CAVE_BIOME_VORONOI;

    private static <T> SettingsComponentType<T> register(Identifier id, Codec<T> codec, T defaultValue) {
        return registryHandler.register(id, new SettingsComponentType<>(codec, defaultValue));
    }

    @SuppressWarnings("unchecked")
    public static void init(IRegistryHandler<?> handler) {
        registryHandler = (IRegistryHandler<SettingsComponentType<?>>) handler;

        PROVIDER = register(
            ModernBetaBuiltInTypes.SettingsComponentType.PROVIDER.id,
            Identifier.CODEC,
            null);

        // Chunk provider
        DEEPSLATE_GENERATION = register(
            ModernBetaBuiltInTypes.SettingsComponentType.DEEPSLATE_GENERATION.id,
            DeepslateGeneration.CODEC,
            DeepslateGeneration.DEFAULT);
        USE_SURFACE_RULES = register(
            ModernBetaBuiltInTypes.SettingsComponentType.USE_SURFACE_RULES.id,
            Codec.BOOL,
            false);
        SEA_LEVEL_OFFSET = register(
            ModernBetaBuiltInTypes.SettingsComponentType.SEA_LEVEL_OFFSET.id,
            Codec.INT,
            0);
        CAVE_GENERATION = register(
            ModernBetaBuiltInTypes.SettingsComponentType.CAVE_GENERATION.id,
            CaveGeneration.CODEC,
            CaveGeneration.DEFAULT);
        NOISE_SCALE = register(
            ModernBetaBuiltInTypes.SettingsComponentType.NOISE_SCALE.id,
            NoiseScale.CODEC,
            NoiseScale.DEFAULT);
        NOISE_SLIDE = register(
            ModernBetaBuiltInTypes.SettingsComponentType.NOISE_SLIDE.id,
            NoiseSlide.CODEC,
            NoiseSlide.DEFAULT);
        FORCED_BIOME_HEIGHT = register(
            ModernBetaBuiltInTypes.SettingsComponentType.FORCED_BIOME_HEIGHT.id,
            ForcedBiomeHeight.CODEC,
            ForcedBiomeHeight.DEFAULT);
        INFDEV_227_STRUCTURES = register(
            ModernBetaBuiltInTypes.SettingsComponentType.INFDEV_227_STRUCTURES.id,
            Infdev227Structures.CODEC,
            Infdev227Structures.DEFAULT);
        FINITE_LEVEL_PROPERTIES = register(
            ModernBetaBuiltInTypes.SettingsComponentType.FINITE_LEVEL_PROPERTIES.id,
            FiniteLevelProperties.CODEC,
            FiniteLevelProperties.DEFAULT);
        FINITE_CAVE_GENERATION = register(
            ModernBetaBuiltInTypes.SettingsComponentType.FINITE_CAVE_GENERATION.id,
            FiniteCaveGeneration.CODEC,
            FiniteCaveGeneration.DEFAULT);
        FINITE_NOISE = register(
            ModernBetaBuiltInTypes.SettingsComponentType.FINITE_NOISE.id,
            FiniteNoise.CODEC,
            FiniteNoise.DEFAULT);
        FINITE_BEACHES = register(
            ModernBetaBuiltInTypes.SettingsComponentType.FINITE_BEACHES.id,
            FiniteBeaches.CODEC,
            FiniteBeaches.DEFAULT);
        FINITE_POOLS = register(
            ModernBetaBuiltInTypes.SettingsComponentType.FINITE_POOLS.id,
            FinitePools.CODEC,
            FinitePools.DEFAULT);
        SPAWN_INDEV_HOUSE = register(
            ModernBetaBuiltInTypes.SettingsComponentType.SPAWN_INDEV_HOUSE.id,
            Codec.BOOL,
            true);
        ISLES_PROPERTIES = register(
            ModernBetaBuiltInTypes.SettingsComponentType.ISLES_PROPERTIES.id,
            IslesProperties.CODEC,
            IslesProperties.DEFAULT);

        // Biome provider
        SINGLE_BIOME = register(
            ModernBetaBuiltInTypes.SettingsComponentType.SINGLE_BIOME.id,
            Identifier.CODEC,
            VersionCompat.id("moderner_beta:beta_plains"));
        CLIMATE_SCALE = register(
            ModernBetaBuiltInTypes.SettingsComponentType.CLIMATE_SCALE.id,
            ClimateScale.CODEC,
            ClimateScale.DEFAULT);
        CLIMATE_MAPPINGS = register(
            ModernBetaBuiltInTypes.SettingsComponentType.CLIMATE_MAPPINGS.id,
            ClimateMapping.MAP_CODEC,
            ClimateMapping.DEFAULT_MAPPINGS);
        VORONOI_POINTS = register(
            ModernBetaBuiltInTypes.SettingsComponentType.VORONOI_POINTS.id,
            VoronoiPointBiome.CODEC.listOf(),
            List.of());
        FRACTAL_LAYERS = register(
            ModernBetaBuiltInTypes.SettingsComponentType.FRACTAL_LAYERS.id,
            ConfiguredLayers.CODEC,
            ConfiguredLayers.DEFAULT);
        USE_OCEAN_BIOMES = register(
            ModernBetaBuiltInTypes.SettingsComponentType.USE_OCEAN_BIOMES.id,
            Codec.BOOL,
            false);

        // Cave biome provider
        CAVE_BIOME_VORONOI = register(
            ModernBetaBuiltInTypes.SettingsComponentType.CAVE_BIOME_VORONOI.id,
            CaveBiomeVoronoi.CODEC,
            CaveBiomeVoronoi.DEFAULT);
    }
}
