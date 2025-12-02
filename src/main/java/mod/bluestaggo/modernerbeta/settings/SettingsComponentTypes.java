package mod.bluestaggo.modernerbeta.settings;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.TemperatureHeightScaling;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaNoiseGeneratorSettings;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.settings.component.*;
import mod.bluestaggo.modernerbeta.level.biome.provider.climate.ClimateMapping;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ConfiguredLayers;
import mod.bluestaggo.modernerbeta.level.biome.voronoi.VoronoiPointBiome;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaNoiseSettings;
import net.minecraft.core.Holder;
import mod.bluestaggo.modernerbeta.settings.component.validation.ComponentValidator;
import mod.bluestaggo.modernerbeta.settings.component.validation.ValidationResult;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class SettingsComponentTypes {
    private static IRegistryHandler<SettingsComponentType<?>> registryHandler;

    public static SettingsComponentType<ResourceLocation> PRESET;
    public static SettingsComponentType<ResourceLocation> PROVIDER;

    // Chunk provider
    public static SettingsComponentType<DeepslateGeneration> DEEPSLATE_GENERATION;
    public static SettingsComponentType<Boolean> USE_SURFACE_RULES;
    public static SettingsComponentType<Integer> SEA_LEVEL_OFFSET;
    public static SettingsComponentType<CaveGeneration> CAVE_GENERATION;
    public static SettingsComponentType<Holder<NoiseGeneratorSettings>> NOISE_GENERATOR_SETTINGS;
    public static SettingsComponentType<NoiseSettings> NOISE_SETTINGS;
    public static SettingsComponentType<Noise3DSettings> NOISE_3D_SETTINGS;
    public static SettingsComponentType<NoiseScale> NOISE_SCALE;
    public static SettingsComponentType<NoiseSlide> NOISE_SLIDE;
    public static SettingsComponentType<NoiseLandmass> NOISE_LANDMASS;
    public static SettingsComponentType<ForcedBiomeHeight> FORCED_BIOME_HEIGHT;
    public static SettingsComponentType<SurfaceProperties> SURFACE_PROPERTIES;
    public static SettingsComponentType<Infdev227Structures> INFDEV_227_STRUCTURES;
    public static SettingsComponentType<FiniteLevelProperties> FINITE_LEVEL_PROPERTIES;
    public static SettingsComponentType<FiniteCaveGeneration> FINITE_CAVE_GENERATION;
    public static SettingsComponentType<FiniteNoise> FINITE_NOISE;
    public static SettingsComponentType<FiniteBeaches> FINITE_BEACHES;
    public static SettingsComponentType<FinitePools> FINITE_POOLS;
    public static SettingsComponentType<Boolean> SPAWN_INDEV_HOUSE;
    public static SettingsComponentType<IslesProperties> ISLES_PROPERTIES;

    // Biome provider
    public static SettingsComponentType<ResourceLocation> SINGLE_BIOME;
    public static SettingsComponentType<ClimateScale> CLIMATE_SCALE;
    public static SettingsComponentType<Map<String, ClimateMapping>> CLIMATE_MAPPINGS;
    public static SettingsComponentType<ClimateDistribution> CLIMATE_DISTRIBUTION;
    public static SettingsComponentType<List<VoronoiPointBiome>> VORONOI_POINTS;
    public static SettingsComponentType<ConfiguredLayers> FRACTAL_LAYERS;
    public static SettingsComponentType<Boolean> USE_32BIT_LAYER_SEED;
    public static SettingsComponentType<Boolean> USE_OCEAN_BIOMES;
    public static SettingsComponentType<TemperatureHeightScaling> TEMPERATURE_HEIGHT_SCALING;

    // Cave biome provider
    public static SettingsComponentType<CaveBiomeVoronoi> CAVE_BIOME_VORONOI;

    // Config
    public static SettingsComponentType<ClimaticBiomeColors> CONFIG_BETA_CLIMATIC_COLORS;
    public static SettingsComponentType<ClimaticBiomeColors> CONFIG_PE_CLIMATIC_COLORS;
    public static SettingsComponentType<ClimaticBiomeColors> CONFIG_BETA_FRACTAL_CLIMATIC_COLORS;
    public static SettingsComponentType<Map<String, Integer>> CONFIG_BIOME_PREVIEW_COLORS;
    public static SettingsComponentType<MiscConfig> CONFIG_MISCELLANEOUS;

    private static <T> SettingsComponentType<T> register(ResourceLocation id, Codec<T> codec, T defaultValue, ComponentValidator<T> validator) {
        return registryHandler.register(id, new SettingsComponentType<>(codec, defaultValue, validator));
    }

    private static <T> SettingsComponentType<T> registerWithDefaultGetter(
        ResourceLocation id,
        Codec<T> codec,
        Function<RegistryOps.RegistryInfoLookup, T> defaultValueGetter,
        ComponentValidator<T> validator
    ) {
        return registryHandler.register(id, new SettingsComponentType<>(codec, defaultValueGetter, validator));
    }

    @SuppressWarnings("unchecked")
    public static void init(IRegistryHandler<?> handler) {
        registryHandler = (IRegistryHandler<SettingsComponentType<?>>) handler;

        PRESET = register(
            ModernBetaBuiltInTypes.SettingsComponentType.PRESET.id,
            ResourceLocation.CODEC,
            null,
            ValidationResult.Valid::new);
        PROVIDER = register(
            ModernBetaBuiltInTypes.SettingsComponentType.PROVIDER.id,
            ResourceLocation.CODEC,
            null,
            ValidationResult.Valid::new);

        // Chunk provider
        DEEPSLATE_GENERATION = register(
            ModernBetaBuiltInTypes.SettingsComponentType.DEEPSLATE_GENERATION.id,
            DeepslateGeneration.CODEC,
            DeepslateGeneration.DEFAULT,
            ValidationResult.Valid::new);
        USE_SURFACE_RULES = register(
            ModernBetaBuiltInTypes.SettingsComponentType.USE_SURFACE_RULES.id,
            Codec.BOOL,
            false,
            ValidationResult.Valid::new);
        SEA_LEVEL_OFFSET = register(
            ModernBetaBuiltInTypes.SettingsComponentType.SEA_LEVEL_OFFSET.id,
            Codec.INT,
            0,
            ValidationResult.Valid::new);
        CAVE_GENERATION = register(
            ModernBetaBuiltInTypes.SettingsComponentType.CAVE_GENERATION.id,
            CaveGeneration.CODEC,
            CaveGeneration.DEFAULT,
            ValidationResult.Valid::new);
        NOISE_GENERATOR_SETTINGS = registerWithDefaultGetter(
            ModernBetaBuiltInTypes.SettingsComponentType.NOISE_GENERATOR_SETTINGS.id,
            NoiseGeneratorSettings.CODEC,
            registry ->
                registry.lookup(Registries.NOISE_SETTINGS).orElseThrow().getter()
                    .getOrThrow(ModernBetaNoiseGeneratorSettings.NOISE_3D),
            ValidationResult.Valid::new);
        NOISE_SETTINGS = register(
            ModernBetaBuiltInTypes.SettingsComponentType.NOISE_SETTINGS.id,
            NoiseSettings.CODEC,
            ModernBetaNoiseSettings.OVERWORLD_128,
            ValidationResult.Valid::new);
        NOISE_3D_SETTINGS = register(
            ModernBetaBuiltInTypes.SettingsComponentType.NOISE_3D_SETTINGS.id,
            Noise3DSettings.CODEC,
            Noise3DSettings.DEFAULT,
            ValidationResult.Valid::new);
        NOISE_SCALE = register(
            ModernBetaBuiltInTypes.SettingsComponentType.NOISE_SCALE.id,
            NoiseScale.CODEC,
            NoiseScale.DEFAULT,
            ValidationResult.Valid::new);
        NOISE_SLIDE = register(
            ModernBetaBuiltInTypes.SettingsComponentType.NOISE_SLIDE.id,
            NoiseSlide.CODEC,
            NoiseSlide.DEFAULT,
            ValidationResult.Valid::new);
        NOISE_LANDMASS = register(
            ModernBetaBuiltInTypes.SettingsComponentType.NOISE_LANDMASS.id,
            NoiseLandmass.CODEC,
            NoiseLandmass.DEFAULT,
            ValidationResult.Valid::new);
        FORCED_BIOME_HEIGHT = register(
            ModernBetaBuiltInTypes.SettingsComponentType.FORCED_BIOME_HEIGHT.id,
            ForcedBiomeHeight.CODEC,
            ForcedBiomeHeight.DEFAULT,
            ValidationResult.Valid::new);
        SURFACE_PROPERTIES = register(
            ModernBetaBuiltInTypes.SettingsComponentType.SURFACE_PROPERTIES.id,
            SurfaceProperties.CODEC,
            SurfaceProperties.DEFAULT,
            ValidationResult.Valid::new);
        INFDEV_227_STRUCTURES = register(
            ModernBetaBuiltInTypes.SettingsComponentType.INFDEV_227_STRUCTURES.id,
            Infdev227Structures.CODEC,
            Infdev227Structures.DEFAULT,
            ValidationResult.Valid::new);
        FINITE_LEVEL_PROPERTIES = register(
            ModernBetaBuiltInTypes.SettingsComponentType.FINITE_LEVEL_PROPERTIES.id,
            FiniteLevelProperties.CODEC,
            FiniteLevelProperties.DEFAULT,
            ValidationResult.Valid::new);
        FINITE_CAVE_GENERATION = register(
            ModernBetaBuiltInTypes.SettingsComponentType.FINITE_CAVE_GENERATION.id,
            FiniteCaveGeneration.CODEC,
            FiniteCaveGeneration.DEFAULT,
            ValidationResult.Valid::new);
        FINITE_NOISE = register(
            ModernBetaBuiltInTypes.SettingsComponentType.FINITE_NOISE.id,
            FiniteNoise.CODEC,
            FiniteNoise.DEFAULT,
            ValidationResult.Valid::new);
        FINITE_BEACHES = register(
            ModernBetaBuiltInTypes.SettingsComponentType.FINITE_BEACHES.id,
            FiniteBeaches.CODEC,
            FiniteBeaches.DEFAULT,
            ValidationResult.Valid::new);
        FINITE_POOLS = register(
            ModernBetaBuiltInTypes.SettingsComponentType.FINITE_POOLS.id,
            FinitePools.CODEC,
            FinitePools.DEFAULT,
            ValidationResult.Valid::new);
        SPAWN_INDEV_HOUSE = register(
            ModernBetaBuiltInTypes.SettingsComponentType.SPAWN_INDEV_HOUSE.id,
            Codec.BOOL,
            true,
            ValidationResult.Valid::new);
        ISLES_PROPERTIES = register(
            ModernBetaBuiltInTypes.SettingsComponentType.ISLES_PROPERTIES.id,
            IslesProperties.CODEC,
            IslesProperties.DEFAULT,
            ValidationResult.Valid::new);

        // Biome provider
        SINGLE_BIOME = register(
            ModernBetaBuiltInTypes.SettingsComponentType.SINGLE_BIOME.id,
            ResourceLocation.CODEC,
            ModernerBeta.createId("beta_plains"),
            ValidationResult.Valid::new);
        CLIMATE_SCALE = register(
            ModernBetaBuiltInTypes.SettingsComponentType.CLIMATE_SCALE.id,
            ClimateScale.CODEC,
            ClimateScale.DEFAULT,
            ValidationResult.Valid::new);
        CLIMATE_MAPPINGS = register(
            ModernBetaBuiltInTypes.SettingsComponentType.CLIMATE_MAPPINGS.id,
            ClimateMapping.MAP_CODEC,
            ClimateMapping.DEFAULT_MAPPINGS,
            component -> {
                for (Map.Entry<String, ClimateMapping> entry : component.entrySet()) {
                    String base = entry.getKey();
                    ClimateMapping mapping = entry.getValue();

                    if (mapping.biome() == null) {
                        return new ValidationResult.Invalid<>(Component.literal("Biome mapping %s contains no biome value!"));
                    }

                    if (mapping.oceanBiome() == null) {
                        return new ValidationResult.Invalid<>(Component.literal("Biome mapping %s contains no ocean biome value!"));
                    }

                    if (mapping.deepOceanBiome() == null) {
                        return new ValidationResult.Invalid<>(Component.literal("Biome mapping %s contains no deep ocean biome value!"));
                    }
                }

                return new ValidationResult.Valid<>(component);
            });
        CLIMATE_DISTRIBUTION = register(
            ModernBetaBuiltInTypes.SettingsComponentType.CLIMATE_DISTRIBUTION.id,
            ClimateDistribution.CODEC,
            ClimateDistribution.DEFAULT,
            ValidationResult.Valid::new);
        VORONOI_POINTS = register(
            ModernBetaBuiltInTypes.SettingsComponentType.VORONOI_POINTS.id,
            VoronoiPointBiome.CODEC.listOf(),
            List.of(),
            component -> {
                if (component.isEmpty())
                    return new ValidationResult.Invalid<>(Component.literal("Voronoi points list is empty!"));

                return new ValidationResult.Valid<>(component);
            });
        FRACTAL_LAYERS = register(
            ModernBetaBuiltInTypes.SettingsComponentType.FRACTAL_LAYERS.id,
            ConfiguredLayers.CODEC,
            ConfiguredLayers.DEFAULT,
            component -> {
                if (component.getPipeline().isEmpty())
                    return new ValidationResult.Invalid<>(Component.literal("Layer pipeline is empty!"));

                return new ValidationResult.Valid<>(component);
            });
        USE_32BIT_LAYER_SEED = register(
            ModernBetaBuiltInTypes.SettingsComponentType.USE_32BIT_LAYER_SEED.id,
            Codec.BOOL,
            false,
            ValidationResult.Valid::new);
        USE_OCEAN_BIOMES = register(
            ModernBetaBuiltInTypes.SettingsComponentType.USE_OCEAN_BIOMES.id,
            Codec.BOOL,
            false,
            ValidationResult.Valid::new);
        TEMPERATURE_HEIGHT_SCALING = register(
            ModernBetaBuiltInTypes.SettingsComponentType.TEMPERATURE_HEIGHT_SCALING.id,
            StringRepresentable.fromEnum(TemperatureHeightScaling::values),
            TemperatureHeightScaling.NONE,
            ValidationResult.Valid::new);

        // Cave biome provider
        CAVE_BIOME_VORONOI = register(
            ModernBetaBuiltInTypes.SettingsComponentType.CAVE_BIOME_VORONOI.id,
            CaveBiomeVoronoi.CODEC,
            CaveBiomeVoronoi.DEFAULT,
            ValidationResult.Valid::new);

        // Config
        CONFIG_BETA_CLIMATIC_COLORS = register(
            ModernBetaBuiltInTypes.SettingsComponentType.CONFIG_BETA_CLIMATIC_COLORS.id,
            ClimaticBiomeColors.CODEC,
            new ClimaticBiomeColors(true, true, false),
            ValidationResult.Valid::new);
        CONFIG_PE_CLIMATIC_COLORS = register(
            ModernBetaBuiltInTypes.SettingsComponentType.CONFIG_PE_CLIMATIC_COLORS.id,
            ClimaticBiomeColors.CODEC,
            new ClimaticBiomeColors(false, false, false),
            ValidationResult.Valid::new);
        CONFIG_BETA_FRACTAL_CLIMATIC_COLORS = register(
            ModernBetaBuiltInTypes.SettingsComponentType.CONFIG_BETA_FRACTAL_CLIMATIC_COLORS.id,
            ClimaticBiomeColors.CODEC,
            new ClimaticBiomeColors(true, true, false),
            ValidationResult.Valid::new);

        // Colors sourced from Cubiomes (https://github.com/Cubitect/cubiomes/blob/e61f90580cbdd883214a8054670dacae655e59c0/util.c#L316)
        Map<String, Integer> biomePreviewColors = new HashMap<>();
        biomePreviewColors.put("minecraft:ocean", 0x000070);
        biomePreviewColors.put("minecraft:plains", 0x8db360);
        biomePreviewColors.put("moderner_beta:late_beta_plains", 0x8db360);
        biomePreviewColors.put("minecraft:desert", 0xfa9418);
        biomePreviewColors.put("minecraft:windswept_hills", 0x606060);
        biomePreviewColors.put("moderner_beta:late_beta_extreme_hills", 0x606060);
        biomePreviewColors.put("moderner_beta:early_release_extreme_hills", 0x606060);
        biomePreviewColors.put("minecraft:forest", 0x056621);
        biomePreviewColors.put("minecraft:taiga", 0x0b6a5f);
        biomePreviewColors.put("moderner_beta:late_beta_taiga", 0x0b6a5f);
        biomePreviewColors.put("moderner_beta:early_release_taiga", 0x0b6a5f);
        biomePreviewColors.put("minecraft:swamp", 0x07f9b2);
        biomePreviewColors.put("moderner_beta:late_beta_swampland", 0x07f9b2);
        biomePreviewColors.put("moderner_beta:early_release_swampland", 0x07f9b2);
        biomePreviewColors.put("minecraft:river", 0x0000ff);
        biomePreviewColors.put("minecraft:river*region_a", 0x7f00ff);
        biomePreviewColors.put("minecraft:river*region_b", 0x007fff);
        biomePreviewColors.put("minecraft:nether_wastes", 0x572526);
        biomePreviewColors.put("minecraft:the_end", 0x8080ff);
        biomePreviewColors.put("minecraft:frozen_ocean", 0x7070d6);
        biomePreviewColors.put("minecraft:frozen_river", 0xa0a0ff);
        biomePreviewColors.put("minecraft:snowy_plains", 0xffffff);
        biomePreviewColors.put("moderner_beta:late_beta_ice_plains", 0xffffff);
        biomePreviewColors.put("moderner_beta:early_release_ice_plains", 0xffffff);
        biomePreviewColors.put("minecraft:snowy_plains*hills", 0xa0a0a0);
        biomePreviewColors.put("moderner_beta:late_beta_ice_plains*hills", 0xa0a0a0);
        biomePreviewColors.put("moderner_beta:early_release_ice_plains*hills", 0xa0a0a0);
        biomePreviewColors.put("minecraft:mushroom_fields", 0xff00ff);
        biomePreviewColors.put("minecraft:mushroom_fields*shore", 0xa000ff);
        biomePreviewColors.put("minecraft:beach", 0xfade55);
        biomePreviewColors.put("minecraft:desert*hills", 0xd25f12);
        biomePreviewColors.put("minecraft:forest*hills", 0x22551c);
        biomePreviewColors.put("minecraft:taiga*hills", 0x163933);
        biomePreviewColors.put("moderner_beta:late_beta_taiga*hills", 0x163933);
        biomePreviewColors.put("moderner_beta:early_release_taiga*hills", 0x163933);
        biomePreviewColors.put("minecraft:windswept_hills*edge", 0x72789a);
        biomePreviewColors.put("moderner_beta:late_beta_extreme_hills*edge", 0x72789a);
        biomePreviewColors.put("moderner_beta:early_release_extreme_hills*edge", 0x72789a);
        biomePreviewColors.put("minecraft:jungle", 0x507b0a);
        biomePreviewColors.put("minecraft:jungle*hills", 0x2c4205);
        biomePreviewColors.put("minecraft:sparse_jungle", 0x60930f);
        biomePreviewColors.put("minecraft:deep_ocean", 0x000030);
        biomePreviewColors.put("minecraft:stony_shore", 0xa2a284);
        biomePreviewColors.put("minecraft:snowy_beach", 0xfaf0c0);
        biomePreviewColors.put("minecraft:birch_forest", 0x307444);
        biomePreviewColors.put("minecraft:birch_forest*hills", 0x1f5f32);
        biomePreviewColors.put("minecraft:dark_forest", 0x40511a);
        biomePreviewColors.put("minecraft:snowy_taiga", 0x31554a);
        biomePreviewColors.put("minecraft:snowy_taiga*hills", 0x243f36);
        biomePreviewColors.put("minecraft:old_growth_pine_taiga", 0x596651);
        biomePreviewColors.put("minecraft:old_growth_pine_taiga*hills", 0x454f3e);
        biomePreviewColors.put("minecraft:windswept_forest", 0x5b7352);
        biomePreviewColors.put("minecraft:savanna", 0xbdb25f);
        biomePreviewColors.put("minecraft:savanna_plateau", 0xa79d64);
        biomePreviewColors.put("minecraft:badlands", 0xd94515);
        biomePreviewColors.put("minecraft:wooded_badlands", 0xb09765);
        biomePreviewColors.put("minecraft:badlands*plateau", 0xca8c65);
        biomePreviewColors.put("minecraft:small_end_islands", 0x4b4bab);
        biomePreviewColors.put("minecraft:end_midlands", 0xc9c959);
        biomePreviewColors.put("minecraft:end_highlands", 0xb5b536);
        biomePreviewColors.put("minecraft:end_barrens", 0x7070cc);
        biomePreviewColors.put("minecraft:warm_ocean", 0x0000ac);
        biomePreviewColors.put("minecraft:lukewarm_ocean", 0x000090);
        biomePreviewColors.put("minecraft:cold_ocean", 0x202070);
        biomePreviewColors.put("minecraft:deep_lukewarm_ocean", 0x000040);
        biomePreviewColors.put("minecraft:deep_cold_ocean", 0x202038);
        biomePreviewColors.put("minecraft:deep_frozen_ocean", 0x404090);
        biomePreviewColors.put("minecraft:the_void", 0x000000);
        biomePreviewColors.put("minecraft:the_void*mutation", 0xff00ff);
        biomePreviewColors.put("minecraft:sunflower_plains", 0xb5db88);
        biomePreviewColors.put("minecraft:desert*lakes", 0xffbc40);
        biomePreviewColors.put("minecraft:windswept_gravelly_hills", 0x888888);
        biomePreviewColors.put("minecraft:flower_forest", 0x2d8e49);
        biomePreviewColors.put("minecraft:taiga*mountains", 0x339287);
        biomePreviewColors.put("minecraft:swamp*hills", 0x2fffda);
        biomePreviewColors.put("minecraft:ice_spikes", 0xb4dcdc);
        biomePreviewColors.put("minecraft:jungle*modified", 0x78a332);
        biomePreviewColors.put("minecraft:sparse_jungle*modified", 0x88bb37);
        biomePreviewColors.put("minecraft:old_growth_birch_forest", 0x589c6c);
        biomePreviewColors.put("minecraft:old_growth_birch_forest*hills", 0x47875a);
        biomePreviewColors.put("minecraft:dark_forest*hills", 0x687942);
        biomePreviewColors.put("minecraft:snowy_taiga*mountains", 0x597d72);
        biomePreviewColors.put("minecraft:old_growth_spruce_taiga", 0x818e79);
        biomePreviewColors.put("minecraft:old_growth_spruce_taiga*hills", 0x6d7766);
        biomePreviewColors.put("minecraft:windswept_gravelly_hills*modified", 0x839b7a);
        biomePreviewColors.put("minecraft:windswept_savanna", 0xe5da87);
        biomePreviewColors.put("minecraft:windswept_savanna*plateau", 0xcfc58c);
        biomePreviewColors.put("minecraft:eroded_badlands", 0xff6d3d);
        biomePreviewColors.put("minecraft:wooded_badlands*modified", 0xd8bf8d);
        biomePreviewColors.put("minecraft:badlands*modified_plateau", 0xf2b48d);
        biomePreviewColors.put("minecraft:bamboo_jungle", 0x849500);
        biomePreviewColors.put("minecraft:bamboo_jungle*hills", 0x5c6c04);
        biomePreviewColors.put("minecraft:soul_sand_valley", 0x4d3a2e);
        biomePreviewColors.put("minecraft:crimson_forest", 0x981a11);
        biomePreviewColors.put("minecraft:warped_forest", 0x49907b);
        biomePreviewColors.put("minecraft:basalt_deltas", 0x645f63);
        biomePreviewColors.put("minecraft:dripstone_caves", 0x4e3012);
        biomePreviewColors.put("minecraft:lush_caves", 0x283c00);
        biomePreviewColors.put("minecraft:meadow", 0x60a445);
        biomePreviewColors.put("minecraft:grove", 0x47726c);
        biomePreviewColors.put("minecraft:snowy_slopes", 0xc4c4c4);
        biomePreviewColors.put("minecraft:jagged_peaks", 0xdcdcc8);
        biomePreviewColors.put("minecraft:frozen_peaks", 0xb0b3ce);
        biomePreviewColors.put("minecraft:stony_peaks", 0x7b8f74);
        biomePreviewColors.put("minecraft:deep_dark", 0x031f29);
        biomePreviewColors.put("minecraft:mangrove_swamp", 0x2ccc8e);
        biomePreviewColors.put("minecraft:cherry_grove", 0xff91c8);
        //? if >=1.21.4
        biomePreviewColors.put("minecraft:pale_garden", 0x696d95);
        biomePreviewColors.put("moderner_beta:beta_rainforest", 0x08fa36);
        biomePreviewColors.put("moderner_beta:beta_swampland", 0x07f9b2);
        biomePreviewColors.put("moderner_beta:beta_seasonal_forest", 0x9be023);
        biomePreviewColors.put("moderner_beta:beta_forest", 0x056621);
        biomePreviewColors.put("moderner_beta:beta_oak_forest", 0x056621);
        biomePreviewColors.put("moderner_beta:beta_savanna", 0xd9e023);
        biomePreviewColors.put("moderner_beta:beta_shrubland", 0xa1ad20);
        biomePreviewColors.put("moderner_beta:beta_taiga", 0x2eb153);
        biomePreviewColors.put("moderner_beta:beta_oak_taiga", 0x2eb153);
        biomePreviewColors.put("moderner_beta:beta_desert", 0xfa9418);
        biomePreviewColors.put("moderner_beta:beta_plains", 0xffd910);
        biomePreviewColors.put("moderner_beta:beta_ice_desert", 0xffed93);
        biomePreviewColors.put("moderner_beta:beta_tundra", 0x57eb59);
        biomePreviewColors.put("moderner_beta:beta_sky", 0x8080ff);
        biomePreviewColors.put("moderner_beta:beta_warm_ocean", 0x0000ac);
        biomePreviewColors.put("moderner_beta:beta_lukewarm_ocean", 0x000090);
        biomePreviewColors.put("moderner_beta:beta_ocean", 0x000070);
        biomePreviewColors.put("moderner_beta:beta_cold_ocean", 0x202070);
        biomePreviewColors.put("moderner_beta:beta_frozen_ocean", 0x7070d6);
        biomePreviewColors.put("moderner_beta:pe_rainforest", 0x08fa36);
        biomePreviewColors.put("moderner_beta:pe_swampland", 0x07f9b2);
        biomePreviewColors.put("moderner_beta:pe_seasonal_forest", 0x9be023);
        biomePreviewColors.put("moderner_beta:pe_forest", 0x056621);
        biomePreviewColors.put("moderner_beta:pe_savanna", 0xd9e023);
        biomePreviewColors.put("moderner_beta:pe_shrubland", 0xa1ad20);
        biomePreviewColors.put("moderner_beta:pe_taiga", 0x2eb153);
        biomePreviewColors.put("moderner_beta:pe_desert", 0xfa9418);
        biomePreviewColors.put("moderner_beta:pe_plains", 0xffd910);
        biomePreviewColors.put("moderner_beta:pe_ice_desert", 0xffed93);
        biomePreviewColors.put("moderner_beta:pe_tundra", 0x57eb59);
        biomePreviewColors.put("moderner_beta:pe_warm_ocean", 0x0000ac);
        biomePreviewColors.put("moderner_beta:pe_lukewarm_ocean", 0x000090);
        biomePreviewColors.put("moderner_beta:pe_ocean", 0x000070);
        biomePreviewColors.put("moderner_beta:pe_cold_ocean", 0x202070);
        biomePreviewColors.put("moderner_beta:pe_frozen_ocean", 0x7070d6);

        CONFIG_BIOME_PREVIEW_COLORS = register(
            ModernBetaBuiltInTypes.SettingsComponentType.CONFIG_BIOME_PREVIEW_COLORS.id,
            Codec.unboundedMap(Codec.STRING, /*? if >=1.21.11 {*/ /*net.minecraft.util.ExtraCodecs.STRING_RGB_COLOR *//*? } else {*/ Codec.INT /*? }*/),
            biomePreviewColors,
            ValidationResult.Valid::new);
        CONFIG_MISCELLANEOUS = register(
            ModernBetaBuiltInTypes.SettingsComponentType.CONFIG_MISCELLANEOUS.id,
            MiscConfig.CODEC,
            new MiscConfig(true, ModernerBeta.createId("beta")),
            ValidationResult.Valid::new);
    }
}
