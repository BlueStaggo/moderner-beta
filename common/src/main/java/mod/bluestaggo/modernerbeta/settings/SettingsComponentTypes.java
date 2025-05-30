package mod.bluestaggo.modernerbeta.settings;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.settings.component.*;
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
    public static SettingsComponentType<FiniteNoiseScale> FINITE_NOISE_SCALE;
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

    private static <T> SettingsComponentType<T> register(String id, Codec<T> codec, T defaultValue) {
        return registryHandler.register(ModernerBeta.createId(id), new SettingsComponentType<>(codec, defaultValue));
    }

    @SuppressWarnings("unchecked")
    public static void init(IRegistryHandler<?> handler) {
        registryHandler = (IRegistryHandler<SettingsComponentType<?>>) handler;

        PROVIDER = register("provider", Identifier.CODEC, null);

        // Chunk provider
        DEEPSLATE_GENERATION = register("deepslate_generation", DeepslateGeneration.CODEC, DeepslateGeneration.DEFAULT);
        USE_SURFACE_RULES = register("use_surface_rules", Codec.BOOL, false);
        SEA_LEVEL_OFFSET = register("sea_level_offset", Codec.INT, 0);
        CAVE_GENERATION = register("cave_generation", CaveGeneration.CODEC, CaveGeneration.DEFAULT);
        NOISE_SCALE = register("noise_scale", NoiseScale.CODEC, NoiseScale.DEFAULT);
        NOISE_SLIDE = register("noise_slide", NoiseSlide.CODEC, NoiseSlide.DEFAULT);
        FORCED_BIOME_HEIGHT = register("forced_biome_height", ForcedBiomeHeight.CODEC, ForcedBiomeHeight.DEFAULT);
        INFDEV_227_STRUCTURES = register("infdev_227_structures", Infdev227Structures.CODEC, Infdev227Structures.DEFAULT);
        FINITE_LEVEL_PROPERTIES = register("finite_level_properties", FiniteLevelProperties.CODEC, FiniteLevelProperties.DEFAULT);
        FINITE_CAVE_GENERATION = register("finite_cave_generation", FiniteCaveGeneration.CODEC, FiniteCaveGeneration.DEFAULT);
        FINITE_NOISE_SCALE = register("finite_noise_scale", FiniteNoiseScale.CODEC, FiniteNoiseScale.DEFAULT);
        FINITE_BEACHES = register("finite_beaches", FiniteBeaches.CODEC, FiniteBeaches.DEFAULT);
        FINITE_POOLS = register("finite_pools", FinitePools.CODEC, FinitePools.DEFAULT);
        SPAWN_INDEV_HOUSE = register("spawn_indev_house", Codec.BOOL, true);
        ISLES_PROPERTIES = register("isles_properties", IslesProperties.CODEC, IslesProperties.DEFAULT);

        // Biome provider
        SINGLE_BIOME = register("single_biome", Identifier.CODEC, Identifier.of("moderner_beta:beta_plains"));
        CLIMATE_SCALE = register("climate_scale", ClimateScale.CODEC, ClimateScale.DEFAULT);
        CLIMATE_MAPPINGS = register("climate_mappings", ClimateMapping.MAP_CODEC, ClimateMapping.DEFAULT_MAPPINGS);
        VORONOI_POINTS = register("voronoi_points", VoronoiPointBiome.CODEC.listOf(), List.of());
        FRACTAL_LAYERS = register("fractal_layers", ConfiguredLayers.CODEC, ConfiguredLayers.DEFAULT);
        USE_OCEAN_BIOMES = register("use_ocean_biomes", Codec.BOOL, false);

        // Cave biome provider
        CAVE_BIOME_VORONOI = register("cave_biome_voronoi", CaveBiomeVoronoi.CODEC, CaveBiomeVoronoi.DEFAULT);
    }
}
