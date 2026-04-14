//~dotLocation
package mod.bluestaggo.modernerbeta.fabric.data;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.TemperatureHeightScaling;
import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionRules;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeIds;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.*;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaNoiseGeneratorSettings;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.component.*;
import mod.bluestaggo.modernerbeta.level.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomes;
import mod.bluestaggo.modernerbeta.level.biome.provider.climate.ClimateMapping;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ConfiguredLayers;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.LayerTarget;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.predicates.BiomePredicate;
import mod.bluestaggo.modernerbeta.level.biome.voronoi.VoronoiPointBiome;
import mod.bluestaggo.modernerbeta.level.chunk.provider.indev.IndevTheme;
import mod.bluestaggo.modernerbeta.level.chunk.provider.indev.IndevType;
import mod.bluestaggo.modernerbeta.level.chunk.provider.island.IslandShape;
import mod.bluestaggo.modernerbeta.util.BootstrapDataContextInfoLookup;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes.*;

public final class ModernBetaSettingsPresets {
    public static final ResourceKey<ModernBetaSettingsPreset> BETA_1_7_3 = keyOf("beta");
    public static final ResourceKey<ModernBetaSettingsPreset> BETA_1_1_02 = keyOf("beta_1_1_02");
    public static final ResourceKey<ModernBetaSettingsPreset> SKYLANDS = keyOf("skylands");
    public static final ResourceKey<ModernBetaSettingsPreset> ALPHA_1_1_2_01 = keyOf("alpha");
    public static final ResourceKey<ModernBetaSettingsPreset> INFDEV_611 = keyOf("infdev_611");
    public static final ResourceKey<ModernBetaSettingsPreset> INFDEV_420 = keyOf("infdev_420");
    public static final ResourceKey<ModernBetaSettingsPreset> INFDEV_415 = keyOf("infdev_415");
    public static final ResourceKey<ModernBetaSettingsPreset> INFDEV_325 = keyOf("infdev_325");
    public static final ResourceKey<ModernBetaSettingsPreset> INFDEV_227 = keyOf("infdev_227");
    public static final ResourceKey<ModernBetaSettingsPreset> INDEV = keyOf("indev");
    public static final ResourceKey<ModernBetaSettingsPreset> CLASSIC_0_30 = keyOf("classic_0_30");
    public static final ResourceKey<ModernBetaSettingsPreset> CLASSIC_0_0_14A_08 = keyOf("classic_0_0_14a_08");
    public static final ResourceKey<ModernBetaSettingsPreset> PE = keyOf("pe");
    public static final ResourceKey<ModernBetaSettingsPreset> BETA_1_8_1 = keyOf("beta_1_8_1");
    public static final ResourceKey<ModernBetaSettingsPreset> BETA_1_9_PRE_3 = keyOf("beta_1_9_pre_3");
    public static final ResourceKey<ModernBetaSettingsPreset> RELEASE_1_0_0 = keyOf("release_1_0_0");
    public static final ResourceKey<ModernBetaSettingsPreset> RELEASE_1_1 = keyOf("release_1_1");
    public static final ResourceKey<ModernBetaSettingsPreset> RELEASE_1_2_5 = keyOf("release_1_2_5");
    public static final ResourceKey<ModernBetaSettingsPreset> RELEASE_1_6_4 = keyOf("release_1_6_4");
    public static final ResourceKey<ModernBetaSettingsPreset> RELEASE_1_12_2 = keyOf("release_1_12_2");
    public static final ResourceKey<ModernBetaSettingsPreset> RELEASE_1_17_1 = keyOf("release_1_17_1");
    public static final ResourceKey<ModernBetaSettingsPreset> BEDROCK_1_2 = keyOf("bedrock_1_2");
    public static final ResourceKey<ModernBetaSettingsPreset> BEDROCK_1_17 = keyOf("bedrock_1_17");
    public static final ResourceKey<ModernBetaSettingsPreset> BETA_SKYLANDS = keyOf("beta_skylands");
    public static final ResourceKey<ModernBetaSettingsPreset> BETA_ISLES = keyOf("beta_isles");
    public static final ResourceKey<ModernBetaSettingsPreset> BETA_WATER_WORLD = keyOf("beta_water_world");
    public static final ResourceKey<ModernBetaSettingsPreset> BETA_ISLE_LAND = keyOf("beta_isle_land");
    public static final ResourceKey<ModernBetaSettingsPreset> BETA_CAVE_DELIGHT = keyOf("beta_cave_delight");
    public static final ResourceKey<ModernBetaSettingsPreset> BETA_MOUNTAIN_MADNESS = keyOf("beta_mountain_madness");
    public static final ResourceKey<ModernBetaSettingsPreset> BETA_DROUGHT = keyOf("beta_drought");
    public static final ResourceKey<ModernBetaSettingsPreset> BETA_CAVE_CHAOS = keyOf("beta_cave_chaos");
    public static final ResourceKey<ModernBetaSettingsPreset> BETA_LARGE_BIOMES = keyOf("beta_large_biomes");
    public static final ResourceKey<ModernBetaSettingsPreset> BETA_XBOX_LEGACY = keyOf("beta_xbox_legacy");
    public static final ResourceKey<ModernBetaSettingsPreset> BETA_SURVIVAL_ISLAND = keyOf("beta_survival_island");
    public static final ResourceKey<ModernBetaSettingsPreset> BETA_VANILLA = keyOf("beta_vanilla");
    public static final ResourceKey<ModernBetaSettingsPreset> LEGACY_CONSOLE_CLASSIC = keyOf("legacy_console_classic");
    public static final ResourceKey<ModernBetaSettingsPreset> LEGACY_CONSOLE_SMALL = keyOf("legacy_console_small");
    public static final ResourceKey<ModernBetaSettingsPreset> LEGACY_CONSOLE_MEDIUM = keyOf("legacy_console_medium");
    public static final ResourceKey<ModernBetaSettingsPreset> LEGACY_CONSOLE_LARGE = keyOf("legacy_console_large");
    public static final ResourceKey<ModernBetaSettingsPreset> RELEASE_HYBRID = keyOf("release_hybrid");
    public static final ResourceKey<ModernBetaSettingsPreset> SNOW_AINT_SNOWIER = keyOf("snow_aint_snowier");
    public static final ResourceKey<ModernBetaSettingsPreset> ALPHA_WINTER = keyOf("alpha_winter");
    public static final ResourceKey<ModernBetaSettingsPreset> INDEV_PARADISE = keyOf("indev_paradise");
    public static final ResourceKey<ModernBetaSettingsPreset> INDEV_WOODS = keyOf("indev_woods");
    public static final ResourceKey<ModernBetaSettingsPreset> INDEV_HELL = keyOf("indev_hell");
    public static final ResourceKey<ModernBetaSettingsPreset> WATER_WORLD = keyOf("water_world");
    public static final ResourceKey<ModernBetaSettingsPreset> ISLE_LAND = keyOf("isle_land");
    public static final ResourceKey<ModernBetaSettingsPreset> CAVE_DELIGHT = keyOf("cave_delight");
    public static final ResourceKey<ModernBetaSettingsPreset> MOUNTAIN_MADNESS = keyOf("mountain_madness");
    public static final ResourceKey<ModernBetaSettingsPreset> DROUGHT = keyOf("drought");
    public static final ResourceKey<ModernBetaSettingsPreset> CAVE_CHAOS = keyOf("cave_chaos");
    public static final ResourceKey<ModernBetaSettingsPreset> BETA_1_8_1_LARGE_BIOMES = keyOf("beta_1_8_1_large_biomes");
    public static final ResourceKey<ModernBetaSettingsPreset> BETA_1_9_PRE_3_LARGE_BIOMES = keyOf("beta_1_9_pre_3_large_biomes");
    public static final ResourceKey<ModernBetaSettingsPreset> RELEASE_1_0_0_LARGE_BIOMES = keyOf("release_1_0_0_large_biomes");
    public static final ResourceKey<ModernBetaSettingsPreset> RELEASE_1_1_LARGE_BIOMES = keyOf("release_1_1_large_biomes");
    public static final ResourceKey<ModernBetaSettingsPreset> RELEASE_1_2_5_LARGE_BIOMES = keyOf("release_1_2_5_large_biomes");
    public static final ResourceKey<ModernBetaSettingsPreset> RELEASE_1_6_4_LARGE_BIOMES = keyOf("release_1_6_4_large_biomes");
    public static final ResourceKey<ModernBetaSettingsPreset> RELEASE_1_12_2_LARGE_BIOMES = keyOf("release_1_12_2_large_biomes");
    public static final ResourceKey<ModernBetaSettingsPreset> RELEASE_1_17_1_LARGE_BIOMES = keyOf("release_1_17_1_large_biomes");
    public static final ResourceKey<ModernBetaSettingsPreset> RELEASE_HYBRID_LARGE_BIOMES = keyOf("release_hybrid_large_biomes");
    public static final ResourceKey<ModernBetaSettingsPreset> SNOW_AINT_SNOWIER_LARGE_BIOMES = keyOf("snow_aint_snowier_large_biomes");
    public static final ResourceKey<ModernBetaSettingsPreset> BETA_1_8_1_AMPLIFIED = keyOf("beta_1_8_1_amplified");
    public static final ResourceKey<ModernBetaSettingsPreset> BETA_1_9_PRE_3_AMPLIFIED = keyOf("beta_1_9_pre_3_amplified");
    public static final ResourceKey<ModernBetaSettingsPreset> RELEASE_1_0_0_AMPLIFIED = keyOf("release_1_0_0_amplified");
    public static final ResourceKey<ModernBetaSettingsPreset> RELEASE_1_1_AMPLIFIED = keyOf("release_1_1_amplified");
    public static final ResourceKey<ModernBetaSettingsPreset> RELEASE_1_2_5_AMPLIFIED = keyOf("release_1_2_5_amplified");
    public static final ResourceKey<ModernBetaSettingsPreset> RELEASE_1_6_4_AMPLIFIED = keyOf("release_1_6_4_amplified");
    public static final ResourceKey<ModernBetaSettingsPreset> RELEASE_1_12_2_AMPLIFIED = keyOf("release_1_12_2_amplified");
    public static final ResourceKey<ModernBetaSettingsPreset> RELEASE_1_17_1_AMPLIFIED = keyOf("release_1_17_1_amplified");
    public static final ResourceKey<ModernBetaSettingsPreset> RELEASE_HYBRID_AMPLIFIED = keyOf("release_hybrid_amplified");
    public static final ResourceKey<ModernBetaSettingsPreset> SNOW_AINT_SNOWIER_AMPLIFIED = keyOf("snow_aint_snowier_amplified");
    
    public static ModernBetaSettingsPreset DEFAULT_BETA;
    public static ModernBetaSettingsPreset DEFAULT_MAJOR;

    public static void bootstrap(BootstrapContext<ModernBetaSettingsPreset> context) {
        ResourceLocation betaId = ModernerBeta.createId("beta");
        ResourceLocation majorId = ModernerBeta.createId("release_1_12_2");

        DEFAULT_BETA = presetBeta(context, false);
        DEFAULT_MAJOR = preset1122(context, false, 0, false);

        register(context, BETA_1_7_3, DEFAULT_BETA);
        register(context, BETA_1_1_02, presetBeta(context, true));
        register(context, ALPHA_1_1_2_01, presetAlpha(context));
        register(context, SKYLANDS, presetSkylands(context));
        register(context, INFDEV_415, presetInfdev415(context));
        register(context, INFDEV_420, presetInfdev420(context));
        register(context, INFDEV_611, presetInfdev611(context));
        register(context, INFDEV_325, presetInfdev325(context));
        register(context, INFDEV_227, presetInfdev227(context));
        register(context, INDEV, presetIndev(context));
        register(context, CLASSIC_0_30, presetClassic(context));
        register(context, CLASSIC_0_0_14A_08, presetClassic14a08(context));
        register(context, PE, presetPE(context));
        register(context, BETA_1_8_1, presetBeta181(false, 0));
        register(context, BETA_1_9_PRE_3, presetBeta19Pre3(false, 0));
        register(context, RELEASE_1_0_0, preset100(false, 0));
        register(context, RELEASE_1_1, preset11(false, 0));
        register(context, RELEASE_1_2_5, preset125(false, 0));
        register(context, RELEASE_1_6_4, preset164(false, 0));
        register(context, RELEASE_1_12_2, DEFAULT_MAJOR);
        register(context, RELEASE_1_17_1, preset1171(context, false, 0, false));
        register(context, BEDROCK_1_2, preset1122(context, false, 0, true));
        register(context, BEDROCK_1_17, preset1171(context, false, 0, true));
        register(context, BETA_SKYLANDS, presetBetaSkylands(context));
        register(context, BETA_ISLES, presetIsles(betaId));
        register(context, BETA_WATER_WORLD, presetWaterWorld(DEFAULT_BETA, betaId));
        register(context, BETA_ISLE_LAND, presetIsleLand(DEFAULT_BETA, betaId));
        register(context, BETA_CAVE_DELIGHT, presetCaveDelight(DEFAULT_BETA, betaId));
        register(context, BETA_MOUNTAIN_MADNESS, presetMountainMadness(DEFAULT_BETA, betaId, false));
        register(context, BETA_DROUGHT, presetDrought(DEFAULT_BETA, betaId));
        register(context, BETA_CAVE_CHAOS, presetCaveChaos(DEFAULT_BETA, betaId));
        register(context, BETA_LARGE_BIOMES, presetBetaLargeBiomes());
        register(context, BETA_XBOX_LEGACY, presetBetaXboxLegacy(context));
        register(context, BETA_SURVIVAL_ISLAND, presetBetaSurvivalIsland());
        register(context, BETA_VANILLA, presetBetaVanilla(context));
        register(context, LEGACY_CONSOLE_CLASSIC, presetReleaseXboxLegacy(context, 864));
        register(context, LEGACY_CONSOLE_SMALL, presetReleaseXboxLegacy(context, 1024));
        register(context, LEGACY_CONSOLE_MEDIUM, presetReleaseXboxLegacy(context, 3072));
        register(context, LEGACY_CONSOLE_LARGE, presetReleaseXboxLegacy(context, 5120));
        register(context, RELEASE_HYBRID, presetReleaseHybrid(false, 0));
        register(context, SNOW_AINT_SNOWIER, presetSnowAintSnowier(context, false, 0));
        register(context, ALPHA_WINTER, presetAlphaWinter(context));
        register(context, INDEV_PARADISE, presetIndevParadise(context));
        register(context, INDEV_WOODS, presetIndevWoods(context));
        register(context, INDEV_HELL, presetIndevHell(context));
        register(context, WATER_WORLD, presetWaterWorld(DEFAULT_MAJOR, majorId));
        register(context, ISLE_LAND, presetIsleLand(DEFAULT_MAJOR, majorId));
        register(context, CAVE_DELIGHT, presetCaveDelight(DEFAULT_MAJOR, majorId));
        register(context, MOUNTAIN_MADNESS, presetMountainMadness(DEFAULT_MAJOR, majorId, true));
        register(context, DROUGHT, presetDrought(DEFAULT_MAJOR, majorId));
        register(context, CAVE_CHAOS, presetCaveChaos(DEFAULT_MAJOR, majorId));
        register(context, BETA_1_8_1_LARGE_BIOMES, presetBeta181(false, 2));
        register(context, BETA_1_9_PRE_3_LARGE_BIOMES, presetBeta19Pre3(false, 2));
        register(context, RELEASE_1_0_0_LARGE_BIOMES, preset100(false, 2));
        register(context, RELEASE_1_1_LARGE_BIOMES, preset11(false, 2));
        register(context, RELEASE_1_2_5_LARGE_BIOMES, preset125(false, 2));
        register(context, RELEASE_1_6_4_LARGE_BIOMES, preset164(false, 2));
        register(context, RELEASE_1_12_2_LARGE_BIOMES, preset1122(context, false, 2, false));
        register(context, RELEASE_1_17_1_LARGE_BIOMES, preset1171(context, false, 2, false));
        register(context, RELEASE_HYBRID_LARGE_BIOMES, presetReleaseHybrid(false, 2));
        register(context, SNOW_AINT_SNOWIER_LARGE_BIOMES, presetSnowAintSnowier(context, false, 2));
        register(context, BETA_1_8_1_AMPLIFIED, presetBeta181(true, 0));
        register(context, BETA_1_9_PRE_3_AMPLIFIED, presetBeta19Pre3(true, 0));
        register(context, RELEASE_1_0_0_AMPLIFIED, preset100(true, 0));
        register(context, RELEASE_1_1_AMPLIFIED, preset11(true, 0));
        register(context, RELEASE_1_2_5_AMPLIFIED, preset125(true, 0));
        register(context, RELEASE_1_6_4_AMPLIFIED, preset164(true, 0));
        register(context, RELEASE_1_12_2_AMPLIFIED, preset1122(context, true, 0, false));
        register(context, RELEASE_1_17_1_AMPLIFIED, preset1171(context, true, 0, false));
        register(context, RELEASE_HYBRID_AMPLIFIED, presetReleaseHybrid(true, 0));
        register(context, SNOW_AINT_SNOWIER_AMPLIFIED, presetSnowAintSnowier(context, true, 0));
    }

    private static void register(BootstrapContext<ModernBetaSettingsPreset> context, ResourceKey<ModernBetaSettingsPreset> key, ModernBetaSettingsPreset preset) {
        context.register(key, preset.withNameAndDesc(key.location()));
    }

    private static ResourceKey<ModernBetaSettingsPreset> keyOf(String id) {
        return ResourceKey.create(ModernBetaResourceKeys.SETTINGS_PRESET, ModernerBeta.createId(id));
    }

    private static ModernBetaSettingsPreset presetBeta(BootstrapContext<ModernBetaSettingsPreset> context, boolean oakBiomes) {
        BootstrapDataContextInfoLookup<?> lookup = new BootstrapDataContextInfoLookup<>(context);
        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder(lookup)
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.NOISE_3D.id)
                .add(NOISE_GENERATOR_SETTINGS, context.lookup(Registries.NOISE_SETTINGS)
                        .get(ModernBetaNoiseGeneratorSettings.OVERWORLD_128).orElseThrow())
                .add(CAVE_GENERATION, CaveGeneration.BETA)
                .addDefault(DEEPSLATE_GENERATION, USE_SURFACE_RULES, NOISE_3D_SETTINGS, PERLIN_NOISE_SETTINGS, NOISE_SCALE, NOISE_SLIDE, NOISE_LANDMASS, SURFACE_PROPERTIES, STRUCTURE_MODIFERS)
                .build(),
            ModernBetaSettings.builder(lookup)
                .add(PROVIDER, ModernBetaBuiltInTypes.Biome.BETA.id)
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.BETA)
                .add(CLIMATE_DISTRIBUTION, ClimateDistribution.BETA)
                .add(CLIMATE_MAPPINGS, Map.ofEntries(
                    Map.entry("desert", new ClimateMapping(ModernBetaBiomes.BETA_DESERT.location())),
                    Map.entry("forest", new ClimateMapping((oakBiomes ? ModernBetaBiomes.BETA_OAK_FOREST : ModernBetaBiomes.BETA_FOREST).location())),
                    Map.entry("ice_desert", new ClimateMapping(ModernBetaBiomes.BETA_TUNDRA.location())),
                    Map.entry("plains", new ClimateMapping(ModernBetaBiomes.BETA_PLAINS.location())),
                    Map.entry("rainforest", new ClimateMapping(ModernBetaBiomes.BETA_RAINFOREST.location())),
                    Map.entry("savanna", new ClimateMapping(ModernBetaBiomes.BETA_SAVANNA.location())),
                    Map.entry("shrubland", new ClimateMapping(ModernBetaBiomes.BETA_SHRUBLAND.location())),
                    Map.entry("seasonal_forest", new ClimateMapping(ModernBetaBiomes.BETA_SEASONAL_FOREST.location())),
                    Map.entry("swampland", new ClimateMapping(ModernBetaBiomes.BETA_SWAMPLAND.location())),
                    Map.entry("taiga", new ClimateMapping((oakBiomes ? ModernBetaBiomes.BETA_OAK_TAIGA : ModernBetaBiomes.BETA_TAIGA).location())),
                    Map.entry("tundra", new ClimateMapping(ModernBetaBiomes.BETA_TUNDRA.location() ))
                ))
                .add(BIOME_INJECTION_RULES, BiomeInjectionRules.standardRules(lookup,
                        BiomeInjectionRules.makeBetaOceanRule(lookup, false)))
                .addDefault(CLIMATE_SCALE)
                .build(),
            ModernBetaSettings.builder(lookup)
                .add(PROVIDER, ModernBetaBuiltInTypes.CaveBiome.VORONOI.id)
                .addDefault(CAVE_BIOME_VORONOI)
                .build()
        );
    }

    private static ModernBetaSettingsPreset presetAlpha(BootstrapContext<ModernBetaSettingsPreset> context) {
        RegistryOps.RegistryInfoLookup lookup = new BootstrapDataContextInfoLookup<>(context);
        
        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder(lookup)
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.NOISE_3D.id)
                .add(NOISE_GENERATOR_SETTINGS, context.lookup(Registries.NOISE_SETTINGS)
                        .get(ModernBetaNoiseGeneratorSettings.OVERWORLD_128).orElseThrow())
                .add(DEEPSLATE_GENERATION, DeepslateGeneration.DISABLED)
                .add(CAVE_GENERATION, CaveGeneration.BETA)
                .add(NOISE_3D_SETTINGS, Noise3DSettings.ALPHA)
                .add(PERLIN_NOISE_SETTINGS, PerlinNoiseSettings.ALPHA)
                .add(NOISE_SCALE, NoiseScale.ALPHA)
                .add(NOISE_LANDMASS, NoiseLandmass.ALPHA)
                .add(SURFACE_PROPERTIES, SurfaceProperties.ALPHA)
                .addDefault(USE_SURFACE_RULES, NOISE_SLIDE, STRUCTURE_MODIFERS)
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.ALPHA)
                .add(BIOME_INJECTION_RULES, BiomeInjectionRules.emptyRules(lookup))
                .build(),
            ModernBetaSettings.noCaveBiomes()
        );
    }
    
    private static ModernBetaSettingsPreset presetSkylands(BootstrapContext<ModernBetaSettingsPreset> context) {
        RegistryOps.RegistryInfoLookup lookup = new BootstrapDataContextInfoLookup<>(context);

        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder(lookup)
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.NOISE_3D.id)
                .add(NOISE_GENERATOR_SETTINGS, context.lookup(Registries.NOISE_SETTINGS)
                        .get(ModernBetaNoiseGeneratorSettings.SKY_128).orElseThrow())
                .add(DEEPSLATE_GENERATION, DeepslateGeneration.DISABLED)
                .add(CAVE_GENERATION, CaveGeneration.BETA)
                .add(NOISE_3D_SETTINGS, Noise3DSettings.SKYLANDS)
                .add(NOISE_SCALE, NoiseScale.SKYLANDS)
                .add(NOISE_LANDMASS, NoiseLandmass.SKYLANDS)
                .add(NOISE_SLIDE, new NoiseSlide(
                    -30,
                    31,
                    0,
                    -30,
                    7,
                    1
                ))
                .add(SURFACE_PROPERTIES, SurfaceProperties.SKYLANDS)
                .add(STRUCTURE_MODIFERS, StructureModifiers.vanillaWithExceptions(
                        /*? >=1.21 {*/net.minecraft.world.level.levelgen.structure.BuiltinStructureSets.TRIAL_CHAMBERS/*?}*/))
                .addDefault(USE_SURFACE_RULES, PERLIN_NOISE_SETTINGS)
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.BETA_SKY)
                .add(BIOME_INJECTION_RULES, BiomeInjectionRules.emptyRules(lookup))
                .build(),
            ModernBetaSettings.noCaveBiomes()
        );
    }
    
    private static ModernBetaSettingsPreset presetInfdev415(BootstrapContext<ModernBetaSettingsPreset> context) {
        RegistryOps.RegistryInfoLookup lookup = new BootstrapDataContextInfoLookup<>(context);

        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder(lookup)
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.NOISE_3D.id)
                .add(NOISE_GENERATOR_SETTINGS, context.lookup(Registries.NOISE_SETTINGS)
                        .get(ModernBetaNoiseGeneratorSettings.INFDEV_415).orElseThrow())
                .add(DEEPSLATE_GENERATION, DeepslateGeneration.DISABLED)
                .add(CAVE_GENERATION, CaveGeneration.DISABLED)
                .add(NOISE_3D_SETTINGS, Noise3DSettings.INFDEV_415)
                .add(PERLIN_NOISE_SETTINGS, PerlinNoiseSettings.INFDEV_415)
                .add(NOISE_SCALE, NoiseScale.INFDEV_415)
                .add(NOISE_SLIDE, NoiseSlide.DISABLED)
                .add(NOISE_LANDMASS, NoiseLandmass.DISABLED)
                .add(SURFACE_PROPERTIES, SurfaceProperties.ALPHA)
                .addDefault(USE_SURFACE_RULES, STRUCTURE_MODIFERS)
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.INFDEV_415)
                .add(BIOME_INJECTION_RULES, BiomeInjectionRules.emptyRules(lookup))
                .build(),
            ModernBetaSettings.noCaveBiomes()
        );
    }
    
    private static ModernBetaSettingsPreset presetInfdev420(BootstrapContext<ModernBetaSettingsPreset> context) {
        RegistryOps.RegistryInfoLookup lookup = new BootstrapDataContextInfoLookup<>(context);

        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder(lookup)
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.NOISE_3D.id)
                .add(NOISE_GENERATOR_SETTINGS, context.lookup(Registries.NOISE_SETTINGS)
                        .get(ModernBetaNoiseGeneratorSettings.OVERWORLD_128).orElseThrow())
                .add(DEEPSLATE_GENERATION, DeepslateGeneration.DISABLED)
                .add(CAVE_GENERATION, CaveGeneration.DISABLED)
                .add(NOISE_3D_SETTINGS, Noise3DSettings.INFDEV_611)
                .add(PERLIN_NOISE_SETTINGS, PerlinNoiseSettings.ALPHA)
                .add(NOISE_SCALE, NoiseScale.INFDEV_420)
                .add(NOISE_SLIDE, NoiseSlide.DISABLED)
                .add(NOISE_LANDMASS, NoiseLandmass.DISABLED)
                .add(SURFACE_PROPERTIES, SurfaceProperties.ALPHA)
                .addDefault(USE_SURFACE_RULES, STRUCTURE_MODIFERS)
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.INFDEV_420)
                .add(BIOME_INJECTION_RULES, BiomeInjectionRules.emptyRules(lookup))
                .build(),
            ModernBetaSettings.noCaveBiomes()
        );
    }
    
    private static ModernBetaSettingsPreset presetInfdev611(BootstrapContext<ModernBetaSettingsPreset> context) {
        RegistryOps.RegistryInfoLookup lookup = new BootstrapDataContextInfoLookup<>(context);

        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder(lookup)
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.NOISE_3D.id)
                .add(NOISE_GENERATOR_SETTINGS, context.lookup(Registries.NOISE_SETTINGS)
                        .get(ModernBetaNoiseGeneratorSettings.OVERWORLD_128).orElseThrow())
                .add(DEEPSLATE_GENERATION, DeepslateGeneration.DISABLED)
                .add(CAVE_GENERATION, CaveGeneration.BETA)
                .add(NOISE_3D_SETTINGS, Noise3DSettings.INFDEV_611)
                .add(PERLIN_NOISE_SETTINGS, PerlinNoiseSettings.ALPHA)
                .add(NOISE_SCALE, NoiseScale.ALPHA)
                .add(NOISE_SLIDE, NoiseSlide.DISABLED)
                .add(NOISE_LANDMASS, NoiseLandmass.INFDEV_611)
                .add(SURFACE_PROPERTIES, SurfaceProperties.ALPHA)
                .addDefault(USE_SURFACE_RULES, STRUCTURE_MODIFERS)
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.INFDEV_611)
                .add(BIOME_INJECTION_RULES, BiomeInjectionRules.emptyRules(lookup))
                .build(),
            ModernBetaSettings.noCaveBiomes()
        );
    }

    private static ModernBetaSettingsPreset presetInfdev325(BootstrapContext<ModernBetaSettingsPreset> context) {
        RegistryOps.RegistryInfoLookup lookup = new BootstrapDataContextInfoLookup<>(context);

        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder(lookup)
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.INFDEV_227.id)
                .add(NOISE_GENERATOR_SETTINGS, context.lookup(Registries.NOISE_SETTINGS)
                        .get(ModernBetaNoiseGeneratorSettings.OVERWORLD_128).orElseThrow())
                .add(PERLIN_NOISE_SETTINGS, PerlinNoiseSettings.INFDEV_415)
                .add(DEEPSLATE_GENERATION, DeepslateGeneration.DISABLED)
                .add(USE_SURFACE_RULES, false)
                .add(CAVE_GENERATION, CaveGeneration.DISABLED)
                .add(INFDEV_227_STRUCTURES, new Infdev227Structures(true, false))
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.INFDEV_325)
                .add(BIOME_INJECTION_RULES, BiomeInjectionRules.emptyRules(lookup))
                .build(),
            ModernBetaSettings.noCaveBiomes()
        );
    }
    
    private static ModernBetaSettingsPreset presetInfdev227(BootstrapContext<ModernBetaSettingsPreset> context) {
        RegistryOps.RegistryInfoLookup lookup = new BootstrapDataContextInfoLookup<>(context);

        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder(lookup)
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.INFDEV_227.id)
                .add(NOISE_GENERATOR_SETTINGS, context.lookup(Registries.NOISE_SETTINGS)
                        .get(ModernBetaNoiseGeneratorSettings.OVERWORLD_128).orElseThrow())
                .add(PERLIN_NOISE_SETTINGS, PerlinNoiseSettings.INFDEV_415)
                .add(DEEPSLATE_GENERATION, DeepslateGeneration.DISABLED)
                .add(USE_SURFACE_RULES, false)
                .add(CAVE_GENERATION, CaveGeneration.DISABLED)
                .add(INFDEV_227_STRUCTURES, new Infdev227Structures(true, true))
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.INFDEV_227)
                .add(BIOME_INJECTION_RULES, BiomeInjectionRules.emptyRules(lookup))
                .build(),
            ModernBetaSettings.noCaveBiomes()
        );
    }

    private static ModernBetaSettingsPreset presetIndev(BootstrapContext<ModernBetaSettingsPreset> context) {
        RegistryOps.RegistryInfoLookup lookup = new BootstrapDataContextInfoLookup<>(context);

        HolderGetter<Structure> structureRegistry = context.lookup(Registries.STRUCTURE);
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);

        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder(lookup)
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.FINITE_2D.id)
                .add(NOISE_GENERATOR_SETTINGS, context.lookup(Registries.NOISE_SETTINGS)
                        .get(ModernBetaNoiseGeneratorSettings.FINITE_2D).orElseThrow())
                .add(PERLIN_NOISE_SETTINGS, PerlinNoiseSettings.CLASSIC)
                .add(DEEPSLATE_GENERATION, DeepslateGeneration.DISABLED)
                .add(CAVE_GENERATION, CaveGeneration.DISABLED)
                .add(WORLD_BORDER, WorldBorderLocation.indev(256, 64))
                .add(STRUCTURE_MODIFERS, StructureModifiers.getFinite(structureRegistry, biomeRegistry, 256, 256, false, true))
                .addDefault(FINITE_LEVEL_PROPERTIES, FINITE_CAVE_GENERATION, FINITE_NOISE, FINITE_BEACHES, FINITE_POOLS)
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.INDEV_NORMAL)
                .add(BIOME_INJECTION_RULES, BiomeInjectionRules.emptyRules(lookup))
                .build(),
            ModernBetaSettings.noCaveBiomes()
        );
    }
    
    private static ModernBetaSettingsPreset presetClassic(BootstrapContext<ModernBetaSettingsPreset> context) {
        RegistryOps.RegistryInfoLookup lookup = new BootstrapDataContextInfoLookup<>(context);

        HolderGetter<Structure> structureRegistry = context.lookup(Registries.STRUCTURE);
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);

        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder(lookup)
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.FINITE_2D.id)
                .add(NOISE_GENERATOR_SETTINGS, context.lookup(Registries.NOISE_SETTINGS)
                        .get(ModernBetaNoiseGeneratorSettings.FINITE_2D).orElseThrow())
                .add(PERLIN_NOISE_SETTINGS, PerlinNoiseSettings.CLASSIC)
                .add(FINITE_LEVEL_PROPERTIES, new FiniteLevelProperties(
                    IndevType.CLASSIC,
                    IndevTheme.NORMAL,
                    256,
                    256,
                    128
                ))
                .add(WORLD_BORDER, WorldBorderLocation.indev(256, 64))
                .add(DEEPSLATE_GENERATION, DeepslateGeneration.DISABLED)
                .add(CAVE_GENERATION, CaveGeneration.DISABLED)
                .add(FINITE_BEACHES, new FiniteBeaches(
                    FiniteBeaches.DEFAULT.sandThreshold(),
                    true,
                    false,
                    FiniteBeaches.DEFAULT.gravelThreshold(),
                    false,
                    true,
                    false
                ))
                .add(SPAWN_INDEV_HOUSE, false)
                .add(STRUCTURE_MODIFERS, StructureModifiers.getFinite(structureRegistry, biomeRegistry, 256, 256, false, true))
                .addDefault(FINITE_CAVE_GENERATION, FINITE_NOISE, FINITE_POOLS)
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.INDEV_NORMAL)
                .add(BIOME_INJECTION_RULES, BiomeInjectionRules.emptyRules(lookup))
                .build(),
            ModernBetaSettings.noCaveBiomes()
        );
    }

    private static ModernBetaSettingsPreset presetClassic14a08(BootstrapContext<ModernBetaSettingsPreset> context) {
        RegistryOps.RegistryInfoLookup lookup = new BootstrapDataContextInfoLookup<>(context);

        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder(lookup)
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.FINITE_2D.id)
                .add(NOISE_GENERATOR_SETTINGS, context.lookup(Registries.NOISE_SETTINGS)
                        .get(ModernBetaNoiseGeneratorSettings.FINITE_2D).orElseThrow())
                .add(PERLIN_NOISE_SETTINGS, PerlinNoiseSettings.CLASSIC)
                .add(FINITE_LEVEL_PROPERTIES, new FiniteLevelProperties(
                    IndevType.CLASSIC,
                    IndevTheme.NORMAL,
                    256,
                    256,
                    128
                ))
                .add(WORLD_BORDER, WorldBorderLocation.indev(256, 64))
                .add(DEEPSLATE_GENERATION, DeepslateGeneration.DISABLED)
                .add(CAVE_GENERATION, CaveGeneration.DISABLED)
                .add(FINITE_CAVE_GENERATION, new FiniteCaveGeneration(
                    true,
                    true,
                    16384,
                    1.0f,
                    75.0f
                ))
                .add(FINITE_NOISE, new FiniteNoise(
                    1.3f,
                    1.0f,
                    8.0f,
                    -8.0f,
                    6.0f,
                    6.0f,
                    8,
                    2.0f
                ))
                .add(FINITE_BEACHES, new FiniteBeaches(
                    FiniteBeaches.DEFAULT.sandThreshold(),
                    true,
                    false,
                    FiniteBeaches.DEFAULT.gravelThreshold(),
                    true,
                    false,
                    true
                ))
                .add(FINITE_POOLS, new FinitePools(
                    200,
                    10000,
                    true
                ))
                .add(SPAWN_INDEV_HOUSE, false)
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.CLASSIC_14A_08)
                .add(BIOME_INJECTION_RULES, BiomeInjectionRules.emptyRules(lookup))
                .build(),
            ModernBetaSettings.noCaveBiomes()
        );
    }
    
    private static ModernBetaSettingsPreset presetPE(BootstrapContext<ModernBetaSettingsPreset> context) {
        RegistryOps.RegistryInfoLookup lookup = new BootstrapDataContextInfoLookup<>(context);

        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(NOISE_3D_SETTINGS, Noise3DSettings.PE)
                .add(DEEPSLATE_GENERATION, DeepslateGeneration.DISABLED)
                .build(),
            DEFAULT_BETA.biomeSettings().extend()
                .add(PROVIDER, ModernBetaBuiltInTypes.Biome.PE.id)
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.BETA)
                .add(CLIMATE_DISTRIBUTION, ClimateDistribution.BETA)
                .add(CLIMATE_MAPPINGS, Map.ofEntries(
                    Map.entry("desert", new ClimateMapping(ModernBetaBiomes.PE_DESERT.location())),
                    Map.entry("forest", new ClimateMapping(ModernBetaBiomes.PE_FOREST.location())),
                    Map.entry("ice_desert", new ClimateMapping(ModernBetaBiomes.PE_TUNDRA.location())),
                    Map.entry("plains", new ClimateMapping(ModernBetaBiomes.PE_PLAINS.location() )),
                    Map.entry("rainforest", new ClimateMapping(ModernBetaBiomes.PE_RAINFOREST.location())),
                    Map.entry("savanna", new ClimateMapping(ModernBetaBiomes.PE_SAVANNA.location())),
                    Map.entry("shrubland", new ClimateMapping(ModernBetaBiomes.PE_SHRUBLAND.location())),
                    Map.entry("seasonal_forest", new ClimateMapping(ModernBetaBiomes.PE_SEASONAL_FOREST.location())),
                    Map.entry("swampland", new ClimateMapping(ModernBetaBiomes.PE_SWAMPLAND.location())),
                    Map.entry("taiga", new ClimateMapping(ModernBetaBiomes.PE_TAIGA.location())),
                    Map.entry("tundra", new ClimateMapping(ModernBetaBiomes.PE_TUNDRA.location()))
                ))
                .add(BIOME_INJECTION_RULES, BiomeInjectionRules.emptyRules(lookup))
                .build(),
            ModernBetaSettings.noCaveBiomes()
        );
    }
    
    private static ModernBetaSettingsPreset presetBetaSkylands(BootstrapContext<ModernBetaSettingsPreset> context) {
        RegistryOps.RegistryInfoLookup lookup = new BootstrapDataContextInfoLookup<>(context);

        return new ModernBetaSettingsPreset(
            presetSkylands(context).chunkSettings(),
            DEFAULT_BETA.biomeSettings().extend()
                .add(BIOME_INJECTION_RULES, BiomeInjectionRules.standardRules(lookup))
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }
    
    private static ModernBetaSettingsPreset presetIsles(ResourceLocation initialId) {
        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder()
                .add(PRESET, initialId)
                .add(ISLES_PROPERTIES, IslesProperties.ENABLED)
                .build(),
            ModernBetaSettings.builder()
                .add(PRESET, initialId)
                .build(),
            ModernBetaSettings.builder()
                .add(PRESET, initialId)
                .build()
        );
    }

    private static ModernBetaSettingsPreset presetWaterWorld(ModernBetaSettingsPreset initialSettings, ResourceLocation initialId) {
        NoiseScale baseNoiseScale = initialSettings.chunkSettings().getOrDefault(NOISE_SCALE);
        Map<ExtendedIdentifier, HeightConfig> baseHeightOverrides = initialSettings.chunkSettings().getOrDefault(FORCED_BIOME_HEIGHT).heightOverrides();
        boolean baseForcedBiomeHeightEnabled = initialSettings.chunkSettings().getOrDefault(FORCED_BIOME_HEIGHT).enabled();

        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder()
                .add(PRESET, initialId)
                .add(SEA_LEVEL, 63 + 192)
                .add(NOISE_SCALE, new NoiseScale(
                    baseNoiseScale.coordinate(),
                    baseNoiseScale.height(),
                    baseNoiseScale.upperLimit(),
                    baseNoiseScale.lowerLimit(),
                    baseNoiseScale.depthNoiseX(),
                    baseNoiseScale.depthNoiseZ(),
                    5000.0f,
                    1000.0f,
                    5000.0f,
                    baseNoiseScale.baseSize(),
                    8.0f,
                    baseNoiseScale.densityUnderdamp(),
                    baseNoiseScale.limitBlending(),
                    baseNoiseScale.useFixedOffset(),
                    baseNoiseScale.fixedOffset(),
                    baseNoiseScale.forestNoiseOctaves()
                ))
                .add(FORCED_BIOME_HEIGHT, new ForcedBiomeHeight(
                    baseForcedBiomeHeightEnabled,
                    baseHeightOverrides,
                    2.0f,
                    0.5f,
                    2.0f,
                    0.375f,
                    false
                ))
                .build(),
            ModernBetaSettings.builder()
                .add(PRESET, initialId)
                .build(),
            ModernBetaSettings.builder()
                .add(PRESET, initialId)
                .build()
        );
    }

    private static ModernBetaSettingsPreset presetIsleLand(ModernBetaSettingsPreset initialSettings, ResourceLocation initialId) {
        NoiseScale baseNoiseScale = initialSettings.chunkSettings().getOrDefault(NOISE_SCALE);

        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder()
                .add(PRESET, initialId)
                .add(NOISE_SCALE, new NoiseScale(
                    3000.0f,
                    6000.0f,
                    250.0f,
                    512.0f,
                    baseNoiseScale.depthNoiseX(),
                    baseNoiseScale.depthNoiseZ(),
                    baseNoiseScale.mainNoiseX(),
                    baseNoiseScale.mainNoiseY(),
                    baseNoiseScale.mainNoiseZ(),
                    baseNoiseScale.baseSize(),
                    10.0f,
                    baseNoiseScale.densityUnderdamp(),
                    baseNoiseScale.limitBlending(),
                    baseNoiseScale.useFixedOffset(),
                    baseNoiseScale.fixedOffset(),
                    baseNoiseScale.forestNoiseOctaves()
                ))
                .build(),
            ModernBetaSettings.builder()
                .add(PRESET, initialId)
                .build(),
            ModernBetaSettings.builder()
                .add(PRESET, initialId)
                .build()
        );
    }
    

    private static ModernBetaSettingsPreset presetCaveDelight(ModernBetaSettingsPreset initialSettings, ResourceLocation initialId) {
        NoiseScale baseNoiseScale = initialSettings.chunkSettings().getOrDefault(NOISE_SCALE);
        Map<ExtendedIdentifier, HeightConfig> baseHeightOverrides = initialSettings.chunkSettings().getOrDefault(FORCED_BIOME_HEIGHT).heightOverrides();
        boolean baseForcedBiomeHeightEnabled = initialSettings.chunkSettings().getOrDefault(FORCED_BIOME_HEIGHT).enabled();

        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder()
                .add(PRESET, initialId)
                .add(NOISE_SCALE, new NoiseScale(
                    baseNoiseScale.coordinate(),
                    baseNoiseScale.height(),
                    baseNoiseScale.upperLimit(),
                    baseNoiseScale.lowerLimit(),
                    baseNoiseScale.depthNoiseX(),
                    baseNoiseScale.depthNoiseZ(),
                    5000.0f,
                    1000.0f,
                    5000.0f,
                    baseNoiseScale.baseSize(),
                    5.0f,
                    baseNoiseScale.densityUnderdamp(),
                    baseNoiseScale.limitBlending(),
                    baseNoiseScale.useFixedOffset(),
                    baseNoiseScale.fixedOffset(),
                    baseNoiseScale.forestNoiseOctaves()
                ))
                .add(FORCED_BIOME_HEIGHT, new ForcedBiomeHeight(
                    baseForcedBiomeHeightEnabled,
                    baseHeightOverrides,
                    2.0f,
                    1.0f,
                    4.0f,
                    1.0f,
                    false
                ))
                .build(),
            ModernBetaSettings.builder()
                .add(PRESET, initialId)
                 .build(),
            ModernBetaSettings.builder()
                .add(PRESET, initialId)
                .build()
        );
    }

    private static ModernBetaSettingsPreset presetMountainMadness(ModernBetaSettingsPreset initialSettings, ResourceLocation initialId, boolean modifyBaseSize) {
        NoiseScale baseNoiseScale = initialSettings.chunkSettings().getOrDefault(NOISE_SCALE);
        Map<ExtendedIdentifier, HeightConfig> baseHeightOverrides = initialSettings.chunkSettings().getOrDefault(FORCED_BIOME_HEIGHT).heightOverrides();
        boolean baseForcedBiomeHeightEnabled = initialSettings.chunkSettings().getOrDefault(FORCED_BIOME_HEIGHT).enabled();

        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder()
                .add(PRESET, initialId)
                .add(NOISE_SCALE, new NoiseScale(
                    738.41864f,
                    157.69133f,
                    801.4267f,
                    1254.1643f,
                    374.93652f,
                    288.65228f,
                    1355.9908f,
                    745.5343f,
                    1183.464f,
                    modifyBaseSize ? 1.8758626f : baseNoiseScale.baseSize(),
                    1.7137525f,
                    baseNoiseScale.densityUnderdamp(),
                    baseNoiseScale.limitBlending(),
                    baseNoiseScale.useFixedOffset(),
                    baseNoiseScale.fixedOffset(),
                    baseNoiseScale.forestNoiseOctaves()
                ))
                .add(FORCED_BIOME_HEIGHT, new ForcedBiomeHeight(
                    baseForcedBiomeHeightEnabled,
                    baseHeightOverrides,
                    1.7553768f,
                    3.4701107f,
                    1.0f,
                    2.535211f,
                    false
                ))
                .build(),
            ModernBetaSettings.builder()
                .add(PRESET, initialId)
                .build(),
            ModernBetaSettings.builder()
                .add(PRESET, initialId)
                .build()
        );
    }

    private static ModernBetaSettingsPreset presetDrought(ModernBetaSettingsPreset initialSettings, ResourceLocation initialId) {
        NoiseScale baseNoiseScale = initialSettings.chunkSettings().getOrDefault(NOISE_SCALE);

        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder()
                .add(PRESET, initialId)
                .add(SEA_LEVEL, 63 - 43)
                .add(NOISE_SCALE, new NoiseScale(
                    baseNoiseScale.coordinate(),
                    baseNoiseScale.height(),
                    baseNoiseScale.upperLimit(),
                    baseNoiseScale.lowerLimit(),
                    baseNoiseScale.depthNoiseX(),
                    baseNoiseScale.depthNoiseZ(),
                    1000.0f,
                    3000.0f,
                    1000.0f,
                    baseNoiseScale.baseSize(),
                    10.0f,
                    baseNoiseScale.densityUnderdamp(),
                    baseNoiseScale.limitBlending(),
                    baseNoiseScale.useFixedOffset(),
                    baseNoiseScale.fixedOffset(),
                    baseNoiseScale.forestNoiseOctaves()
                ))
                .build(),
            ModernBetaSettings.builder()
                .add(PRESET, initialId)
                .build(),
            ModernBetaSettings.builder()
                .add(PRESET, initialId)
                .build()
        );
    }

    private static ModernBetaSettingsPreset presetCaveChaos(ModernBetaSettingsPreset initialSettings, ResourceLocation initialId) {
        NoiseScale baseNoiseScale = initialSettings.chunkSettings().getOrDefault(NOISE_SCALE);

        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder()
                .add(PRESET, initialId)
                .add(SEA_LEVEL, 63 - 57)
                .add(NOISE_SCALE, new NoiseScale(
                    baseNoiseScale.coordinate(),
                    baseNoiseScale.height(),
                    2.0f,
                    64.0f,
                    baseNoiseScale.depthNoiseX(),
                    baseNoiseScale.depthNoiseZ(),
                    baseNoiseScale.mainNoiseX(),
                    baseNoiseScale.mainNoiseY(),
                    baseNoiseScale.mainNoiseZ(),
                    baseNoiseScale.baseSize(),
                    8.0f,
                    baseNoiseScale.densityUnderdamp(),
                    baseNoiseScale.limitBlending(),
                    baseNoiseScale.useFixedOffset(),
                    baseNoiseScale.fixedOffset(),
                    baseNoiseScale.forestNoiseOctaves()
                ))
                .build(),
            ModernBetaSettings.builder()
                .add(PRESET, initialId)
                .build(),
            ModernBetaSettings.builder()
                .add(PRESET, initialId)
                .build()
        );
    }
    
    private static ModernBetaSettingsPreset presetBetaLargeBiomes() {
        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings(),
            DEFAULT_BETA.biomeSettings().extend()
                .add(CLIMATE_SCALE, new ClimateScale(
                    0.025f / 4.0f,
                    0.05f / 4.0f,
                    0.25f / 2.0f,
                    0.003125f / 4.0f
                ))
                .build(),
            ModernBetaSettings.builder()
                .add(PROVIDER, ModernBetaBuiltInTypes.CaveBiome.VORONOI.id)
                .add(CAVE_BIOME_VORONOI, new CaveBiomeVoronoi(
                    128.0f,
                    16.0f,
                    -64,
                    64,
                    CaveBiomeVoronoi.DEFAULT.points()
                ))
                .build()
        );
    }
    
    private static ModernBetaSettingsPreset presetBetaXboxLegacy(BootstrapContext<ModernBetaSettingsPreset> context) {
        HolderGetter<Structure> structureRegistry = context.lookup(Registries.STRUCTURE);
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);

        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(WORLD_BORDER, WorldBorderLocation.xboxLegacy(864))
                .add(STRUCTURE_MODIFERS, StructureModifiers.getFinite(structureRegistry, biomeRegistry, 864, 864))
                .build(),
            DEFAULT_BETA.biomeSettings(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }
    
    private static ModernBetaSettingsPreset presetBetaSurvivalIsland() {
        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(ISLES_PROPERTIES, new IslesProperties(
                    true,
                    false,
                    -200.0f,
                    IslandShape.CIRCLE,
                    1,
                    8,
                    64,
                    16,
                    300.0f,
                    0.25f
                ))
                .build(),
            DEFAULT_BETA.biomeSettings(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }
    
    private static ModernBetaSettingsPreset presetBetaVanilla(BootstrapContext<ModernBetaSettingsPreset> context) {
        RegistryOps.RegistryInfoLookup lookup = new BootstrapDataContextInfoLookup<>(context);

        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(USE_SURFACE_RULES, true)
                .add(CAVE_GENERATION, CaveGeneration.EARLY_RELEASE)
                .build(),
            ModernBetaSettings.builder()
                .add(PROVIDER, ModernBetaBuiltInTypes.Biome.VORONOI.id)
                .add(CLIMATE_SCALE, new ClimateScale(
                    0.025f / 3.0f,
                    0.05f / 3.0f,
                    0.25f / 1.5f,
                    0.003125f
                ))
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.MAJOR_RELEASE)
                .add(VORONOI_POINTS, List.of(
                    // Standard Biomes

                    new VoronoiPointBiome(Biomes.DESERT.location(), 0.9, 0.1, 0.5),
                    new VoronoiPointBiome(Biomes.PLAINS.location(), 0.9, 0.3, 0.5),
                    new VoronoiPointBiome(Biomes.FOREST.location(), 0.9, 0.5, 0.5),
                    new VoronoiPointBiome(Biomes.FOREST.location(), 0.9, 0.7, 0.5),
                    new VoronoiPointBiome(Biomes.JUNGLE.location(), 0.9, 0.9, 0.5),

                    new VoronoiPointBiome(Biomes.SAVANNA.location(), 0.7, 0.1, 0.5),
                    new VoronoiPointBiome(Biomes.PLAINS.location(), 0.7, 0.3, 0.5),
                    new VoronoiPointBiome(Biomes.FOREST.location(), 0.7, 0.5, 0.5),
                    new VoronoiPointBiome(Biomes.FOREST.location(), 0.7, 0.7, 0.5),
                    new VoronoiPointBiome(Biomes.FOREST.location(), 0.7, 0.9, 0.5),

                    new VoronoiPointBiome(Biomes.PLAINS.location(), 0.5, 0.1, 0.5),
                    new VoronoiPointBiome(Biomes.PLAINS.location(), 0.5, 0.3, 0.5),
                    new VoronoiPointBiome(Biomes.BIRCH_FOREST.location(), 0.5, 0.5, 0.5),
                    new VoronoiPointBiome(Biomes.BIRCH_FOREST.location(), 0.5, 0.7, 0.5),
                    new VoronoiPointBiome(Biomes.SWAMP.location(), 0.5, 0.9, 0.5),

                    new VoronoiPointBiome(Biomes.SNOWY_PLAINS.location(), 0.3, 0.1, 0.5),
                    new VoronoiPointBiome(Biomes.TAIGA.location(), 0.3, 0.3, 0.5),
                    new VoronoiPointBiome(Biomes.TAIGA.location(), 0.3, 0.5, 0.5),
                    new VoronoiPointBiome(Biomes.SNOWY_TAIGA.location(), 0.3, 0.7, 0.5),
                    new VoronoiPointBiome(Biomes.SNOWY_TAIGA.location(), 0.3, 0.9, 0.5),

                    new VoronoiPointBiome(Biomes.SNOWY_PLAINS.location(), 0.1, 0.1, 0.5),
                    new VoronoiPointBiome(Biomes.SNOWY_PLAINS.location(), 0.1, 0.3, 0.5),
                    new VoronoiPointBiome(Biomes.SNOWY_PLAINS.location(), 0.1, 0.5, 0.5),
                    new VoronoiPointBiome(Biomes.SNOWY_PLAINS.location(), 0.1, 0.7, 0.5),
                    new VoronoiPointBiome(Biomes.SNOWY_PLAINS.location(), 0.1, 0.9, 0.5),

                    // Mutated Biomes

                    new VoronoiPointBiome(Biomes.DESERT.location(), 0.9, 0.1, 0.2),
                    new VoronoiPointBiome(Biomes.SUNFLOWER_PLAINS.location(), 0.9, 0.3, 0.2),
                    new VoronoiPointBiome(Biomes.DARK_FOREST.location(), 0.9, 0.5, 0.2),
                    new VoronoiPointBiome(Biomes.DARK_FOREST.location(), 0.9, 0.7, 0.2),
                    new VoronoiPointBiome(Biomes.BAMBOO_JUNGLE.location(), 0.9, 0.9, 0.2),

                    new VoronoiPointBiome(Biomes.SAVANNA.location(), 0.7, 0.1, 0.2),
                    new VoronoiPointBiome(Biomes.MEADOW.location(), 0.7, 0.3, 0.2),
                    new VoronoiPointBiome(Biomes.FLOWER_FOREST.location(), 0.7, 0.5, 0.2),
                    new VoronoiPointBiome(Biomes.FLOWER_FOREST.location(), 0.7, 0.7, 0.2),
                    new VoronoiPointBiome(Biomes.FLOWER_FOREST.location(), 0.7, 0.9, 0.2),

                    new VoronoiPointBiome(Biomes.MEADOW.location(), 0.5, 0.1, 0.2),
                    new VoronoiPointBiome(Biomes.MEADOW.location(), 0.5, 0.3, 0.2),
                    new VoronoiPointBiome(Biomes.CHERRY_GROVE.location(), 0.5, 0.5, 0.2),
                    new VoronoiPointBiome(Biomes.CHERRY_GROVE.location(), 0.5, 0.7, 0.2),
                    new VoronoiPointBiome(Biomes.MANGROVE_SWAMP.location(), 0.5, 0.9, 0.2),

                    new VoronoiPointBiome(Biomes.SNOWY_PLAINS.location(), 0.3, 0.1, 0.2),
                    new VoronoiPointBiome(Biomes.OLD_GROWTH_PINE_TAIGA.location(), 0.3, 0.3, 0.2),
                    new VoronoiPointBiome(Biomes.OLD_GROWTH_PINE_TAIGA.location(), 0.3, 0.5, 0.2),
                    new VoronoiPointBiome(Biomes.GROVE.location(), 0.3, 0.7, 0.2),
                    new VoronoiPointBiome(Biomes.GROVE.location(), 0.3, 0.9, 0.2),

                    new VoronoiPointBiome(Biomes.SNOWY_PLAINS.location(), 0.1, 0.1, 0.2),
                    new VoronoiPointBiome(Biomes.SNOWY_PLAINS.location(), 0.1, 0.3, 0.2),
                    new VoronoiPointBiome(Biomes.SNOWY_PLAINS.location(), 0.1, 0.5, 0.2),
                    new VoronoiPointBiome(Biomes.SNOWY_SLOPES.location(), 0.1, 0.7, 0.2),
                    new VoronoiPointBiome(Biomes.SNOWY_SLOPES.location(), 0.1, 0.9, 0.2),

                    // Mutated Biomes 2

                    new VoronoiPointBiome(Biomes.BADLANDS.location(), 0.9, 0.1, 0.8),
                    new VoronoiPointBiome(Biomes.PLAINS.location(), 0.9, 0.3, 0.8),
                    new VoronoiPointBiome(Biomes.SPARSE_JUNGLE.location(), 0.9, 0.5, 0.8),
                    new VoronoiPointBiome(Biomes.SPARSE_JUNGLE.location(), 0.9, 0.7, 0.8),
                    new VoronoiPointBiome(Biomes.MUSHROOM_FIELDS.location(), 0.9, 0.9, 0.8),

                    new VoronoiPointBiome(Biomes.SAVANNA.location(), 0.7, 0.1, 0.8),
                    new VoronoiPointBiome(Biomes.PLAINS.location(), 0.7, 0.3, 0.8),
                    //? if >=1.21.4 {
                    new VoronoiPointBiome(Biomes.PALE_GARDEN.location(), 0.7, 0.5, 0.8),
                    new VoronoiPointBiome(Biomes.PALE_GARDEN.location(), 0.7, 0.7, 0.8),
                    new VoronoiPointBiome(Biomes.PALE_GARDEN.location(), 0.7, 0.9, 0.8),
                    //?}

                    new VoronoiPointBiome(Biomes.PLAINS.location(), 0.5, 0.1, 0.8),
                    new VoronoiPointBiome(Biomes.PLAINS.location(), 0.5, 0.3, 0.8),
                    new VoronoiPointBiome(Biomes.OLD_GROWTH_BIRCH_FOREST.location(), 0.5, 0.5, 0.8),
                    new VoronoiPointBiome(Biomes.OLD_GROWTH_BIRCH_FOREST.location(), 0.5, 0.7, 0.8),
                    new VoronoiPointBiome(Biomes.MANGROVE_SWAMP.location(), 0.5, 0.9, 0.8),

                    new VoronoiPointBiome(Biomes.SNOWY_PLAINS.location(), 0.3, 0.1, 0.8),
                    new VoronoiPointBiome(Biomes.OLD_GROWTH_SPRUCE_TAIGA.location(), 0.3, 0.3, 0.8),
                    new VoronoiPointBiome(Biomes.OLD_GROWTH_SPRUCE_TAIGA.location(), 0.3, 0.5, 0.8),
                    new VoronoiPointBiome(Biomes.GROVE.location(), 0.3, 0.7, 0.8),
                    new VoronoiPointBiome(Biomes.GROVE.location(), 0.3, 0.9, 0.8),

                    new VoronoiPointBiome(Biomes.SNOWY_PLAINS.location(), 0.1, 0.1, 0.8),
                    new VoronoiPointBiome(Biomes.SNOWY_PLAINS.location(), 0.1, 0.3, 0.8),
                    new VoronoiPointBiome(Biomes.SNOWY_PLAINS.location(), 0.1, 0.5, 0.8),
                    new VoronoiPointBiome(Biomes.ICE_SPIKES.location(), 0.1, 0.7, 0.8),
                    new VoronoiPointBiome(Biomes.ICE_SPIKES.location(), 0.1, 0.9, 0.8)
                ))
                .add(BIOME_INJECTION_RULES, BiomeInjectionRules.standardRules(lookup,
                        BiomeInjectionRules.makeModernOceanRule(lookup)))
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }
    
    private static ModernBetaSettingsPreset presetAlphaWinter(BootstrapContext<ModernBetaSettingsPreset> context) {
        RegistryOps.RegistryInfoLookup lookup = new BootstrapDataContextInfoLookup<>(context);

        ModernBetaSettingsPreset basePreset = presetAlpha(context);
        return new ModernBetaSettingsPreset(
            basePreset.chunkSettings(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.ALPHA_WINTER)
                .add(BIOME_INJECTION_RULES, BiomeInjectionRules.emptyRules(lookup))
                .build(),
            basePreset.caveBiomeSettings()
        );
    }
    
    private static ModernBetaSettingsPreset presetIndevParadise(BootstrapContext<ModernBetaSettingsPreset> context) {
        RegistryOps.RegistryInfoLookup lookup = new BootstrapDataContextInfoLookup<>(context);

        ModernBetaSettingsPreset basePreset = presetIndev(context);
        return new ModernBetaSettingsPreset(
            basePreset.chunkSettings().extend()
                .add(FINITE_LEVEL_PROPERTIES, new FiniteLevelProperties(
                    IndevType.ISLAND,
                    IndevTheme.PARADISE,
                    256,
                    256,
                    128
                ))
                .add(WORLD_BORDER, WorldBorderLocation.indev(256, 64))
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.INDEV_PARADISE)
                .add(BIOME_INJECTION_RULES, BiomeInjectionRules.emptyRules(lookup))
                .build(),
            basePreset.caveBiomeSettings()
        );
    }
    
    private static ModernBetaSettingsPreset presetIndevWoods(BootstrapContext<ModernBetaSettingsPreset> context) {
        RegistryOps.RegistryInfoLookup lookup = new BootstrapDataContextInfoLookup<>(context);

        ModernBetaSettingsPreset basePreset = presetIndev(context);
        return new ModernBetaSettingsPreset(
            basePreset.chunkSettings().extend()
                .add(FINITE_LEVEL_PROPERTIES, new FiniteLevelProperties(
                    IndevType.ISLAND,
                    IndevTheme.WOODS,
                    256,
                    256,
                    128
                ))
                .add(WORLD_BORDER, WorldBorderLocation.indev(256, 64))
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.INDEV_WOODS)
                .add(BIOME_INJECTION_RULES, BiomeInjectionRules.emptyRules(lookup))
                .build(),
            basePreset.caveBiomeSettings()
        );
    }
    
    private static ModernBetaSettingsPreset presetIndevHell(BootstrapContext<ModernBetaSettingsPreset> context) {
        RegistryOps.RegistryInfoLookup lookup = new BootstrapDataContextInfoLookup<>(context);

        ModernBetaSettingsPreset basePreset = presetIndev(context);
        return new ModernBetaSettingsPreset(
            basePreset.chunkSettings().extend()
                .add(FINITE_LEVEL_PROPERTIES, new FiniteLevelProperties(
                    IndevType.ISLAND,
                    IndevTheme.HELL,
                    256,
                    256,
                    128
                ))
                .add(WORLD_BORDER, WorldBorderLocation.indev(256, 64))
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.INDEV_HELL)
                .add(BIOME_INJECTION_RULES, BiomeInjectionRules.emptyRules(lookup))
                .build(),
            basePreset.caveBiomeSettings()
        );
    }

    private static Map<ResourceLocation, String> earlyReleaseLayerOutputs(int biomeScale) {
        ImmutableMap.Builder<ResourceLocation, String> builder = new ImmutableMap.Builder<>();
        builder.put(ModernBetaBuiltInTypes.LayerOutput.BIOME.id, "land");
        for (int i = 0; i < 4 + biomeScale; i++) {
            builder.put(ModernerBeta.createId("climate_" + i), "land_" + i);
        }
        return builder.build();
    }

    private static ModernBetaSettingsPreset presetBeta181(boolean amplified, int biomeScale) {
        List<Layer> layers = new ArrayList<>(List.of(
            new InitLandLayer("land", 1),
            new FuzzyZoomLayer("land", 2000, "land"),
            AddLandLayer.forIslandScaleBeta("land", 1, "land"),
            new ModalZoomLayer("land", 2001, "land"),
            AddLandLayer.forIslandScaleBeta("land", 2, "land"),
            new ModalZoomLayer("land", 2002, "land"),
            AddLandLayer.forIslandScaleBeta("land", 3, "land"),
            new ModalZoomLayer("land", 2003, "land"),
            AddLandLayer.forIslandScaleBeta("land", 3, "land"),
            new ModalZoomLayer("land", 2004, "land"),
            AddLandLayer.forIslandScaleBeta("land", 3, "land"),
            new InitRiverLayer("river", 100, "land"),
            StackedZoomLayer.modal("river", 1000, "river", 6 + biomeScale),
            new ComputeRiverLayer("river", 0, "river", true),
            new SmoothLayer("river", 1000, "river"),
            new RandomBiomeLayer("biome_pool", 200, ExtendedIdentifier.listOf(
                "minecraft:desert",
                "minecraft:forest",
                "moderner_beta:late_beta_extreme_hills",
                "moderner_beta:late_beta_swampland",
                "moderner_beta:late_beta_plains",
                "moderner_beta:late_beta_taiga"
            )),
            new BiomeReplacementLayer("land", 0, "land", Map.of(ExtendedBiomeIds.PLAINS, LayerTarget.layer("biome_pool"))),
            StackedZoomLayer.modal("land", 1000, "land", 2),
            new ModalZoomLayer("land_0", 1000, "land"),
            AddLandLayer.forBeta("land_0", 3, "land_0")
        ));
        for (int i = 0; i < 3 + biomeScale; i++) {
            layers.add(new ModalZoomLayer("land_" + (1 + i), 1001 + i, "land_" + i));
        }
        layers.add(new SmoothLayer("land", 1000, "land_" + (3 + biomeScale)));
        layers.add(MixRiverLayer.forEarlyRelease("land", 0, "land", "river"));

        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(CAVE_GENERATION, CaveGeneration.EARLY_RELEASE)
                .add(FORCED_BIOME_HEIGHT, ForcedBiomeHeight.overridesOnly(Map.of(
                    ExtendedBiomeIds.OCEAN, new HeightConfig(-1.0f, 0.5f)
                ), amplified))
                .add(SEA_LEVEL, 63)
                .add(NOISE_3D_SETTINGS, Noise3DSettings.EARLY_RELEASE)
                .add(PERLIN_NOISE_SETTINGS, PerlinNoiseSettings.RELEASE)
                .add(NOISE_LANDMASS, NoiseLandmass.RELEASE)
                .add(SURFACE_PROPERTIES, SurfaceProperties.EARLY_RELEASE)
                .build(),
            ModernBetaSettings.betaFractalLayers(earlyReleaseLayerOutputs(biomeScale), ClimateDistribution.BETA, layers)
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.NONE)
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }

    private static ConfiguredLayers configuredLayers100Era(int biomeScale, ExtendedIdentifier icePlains) {
        List<Layer> layers = new ArrayList<>(List.of(
            new InitLandLayer("land", 1),
            new FuzzyZoomLayer("land", 2000, "land"),
            AddLandLayer.forIslandScale("land", 1, "land"),
            new ModalZoomLayer("land", 2001, "land"),
            AddLandLayer.forIslandScale("land", 2, "land"),
            new WeightedPoolLayer("snow", 2, WeightedList.<LayerTarget>builder()
                .add(LayerTarget.biome(ExtendedBiomeIds.SNOWY_PLAINS), 1)
                .add(LayerTarget.none(), 4)
                .build()),
            new BiomeReplacementLayer("land", 0, "land", Map.of(ExtendedBiomeIds.PLAINS, LayerTarget.layer("snow"))),
            new ModalZoomLayer("land", 2002, "land"),
            AddLandLayer.forIslandScale("land", 3, "land"),
            new ModalZoomLayer("land", 2003, "land"),
            AddLandLayer.forIslandScale("land", 4, "land"),
            ConditionalOverlayLayer.mushroomIslands(),
            new InitRiverLayer("river", 100, "land"),
            StackedZoomLayer.modal("river", 1000, "river", 6 + biomeScale),
            new ComputeRiverLayer("river", 0, "river", true),
            new SmoothLayer("river", 1000, "river"),
            new RandomBiomeLayer("biome_pool", 200, ExtendedIdentifier.listOf(
                "minecraft:desert",
                "minecraft:forest",
                "moderner_beta:late_beta_extreme_hills",
                "moderner_beta:early_release_swampland",
                "moderner_beta:late_beta_plains",
                "moderner_beta:late_beta_taiga"
            )),
            new BiomeReplacementLayer("land", 0, "land", Map.of(
                ExtendedBiomeIds.PLAINS, LayerTarget.layer("biome_pool"),
                ExtendedBiomeIds.FROZEN_OCEAN, LayerTarget.biome(icePlains),
                ExtendedBiomeIds.SNOWY_PLAINS, LayerTarget.biome(icePlains)
            )),
            StackedZoomLayer.modal("land", 1000, "land", 2),
            new ModalZoomLayer("land_0", 1000, "land"),
            AddLandLayer.forEarlyRelease("land_0", 3, "land_0", icePlains),
            new ConditionalOverlayLayer(
                "land_0", 0, "land_0",
                PredicateOverlayLayer.Target.MUSHROOM_SHORE.predicate(),
                LayerTarget.biome(ExtendedBiomeIds.MUSHROOM_SHORE), LayerTarget.none()
            )
        ));
        for (int i = 0; i < 3 + biomeScale; i++) {
            layers.add(new ModalZoomLayer("land_" + (1 + i), 1001 + i, "land_" + i));
        }
        layers.add(new SmoothLayer("land", 1000, "land_" + (3 + biomeScale)));
        layers.add(MixRiverLayer.forEarlyRelease("land", 0, "land", "river"));

        return new ConfiguredLayers(layers, earlyReleaseLayerOutputs(biomeScale));
    }

    private static ModernBetaSettingsPreset presetBeta19Pre3(boolean amplified, int biomeScale) {
        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(CAVE_GENERATION, CaveGeneration.EARLY_RELEASE)
                .add(FORCED_BIOME_HEIGHT, amplified ? ForcedBiomeHeight.AMPLIFIED : ForcedBiomeHeight.ENABLED)
                .add(SEA_LEVEL, 63)
                .add(NOISE_3D_SETTINGS, Noise3DSettings.EARLY_RELEASE)
                .add(PERLIN_NOISE_SETTINGS, PerlinNoiseSettings.RELEASE)
                .add(NOISE_LANDMASS, NoiseLandmass.RELEASE)
                .add(SURFACE_PROPERTIES, SurfaceProperties.EARLY_RELEASE)
                .build(),
            ModernBetaSettings.betaFractalLayers(configuredLayers100Era(biomeScale, ExtendedIdentifier.of(ModernBetaBiomes.LATE_BETA_ICE_PLAINS)), ClimateDistribution.RELEASE_1_0)
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.NONE)
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }

    private static ModernBetaSettingsPreset preset100(boolean amplified, int biomeScale) {
        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(CAVE_GENERATION, CaveGeneration.EARLY_RELEASE)
                .add(FORCED_BIOME_HEIGHT, amplified ? ForcedBiomeHeight.AMPLIFIED : ForcedBiomeHeight.ENABLED)
                .add(SEA_LEVEL, 63)
                .add(NOISE_3D_SETTINGS, Noise3DSettings.EARLY_RELEASE)
                .add(PERLIN_NOISE_SETTINGS, PerlinNoiseSettings.RELEASE)
                .add(NOISE_LANDMASS, NoiseLandmass.RELEASE)
                .add(SURFACE_PROPERTIES, SurfaceProperties.EARLY_RELEASE)
                .build(),
            ModernBetaSettings.betaFractalLayers(configuredLayers100Era(biomeScale, ExtendedIdentifier.of(ModernBetaBiomes.EARLY_RELEASE_ICE_PLAINS)), ClimateDistribution.RELEASE_1_0)
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.NONE)
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }

    private static ConfiguredLayers configuredLayers11Era(int biomeScale, boolean addJungles, boolean taigasInIcePlains) {
        ExtendedIdentifier icePlains = ExtendedIdentifier.of(ModernBetaBiomes.EARLY_RELEASE_ICE_PLAINS);

        List<ExtendedIdentifier> biomePool = ExtendedIdentifier.listOf(
            "minecraft:desert",
            "minecraft:forest",
            "moderner_beta:early_release_extreme_hills",
            "moderner_beta:early_release_swampland",
            "moderner_beta:late_beta_plains",
            "moderner_beta:early_release_taiga"
        );
        if (addJungles) {
            biomePool = new ArrayList<>(biomePool);
            biomePool.add(ExtendedIdentifier.of(Biomes.JUNGLE));
        }

        Map<ExtendedIdentifier, ExtendedIdentifier> hillsVariants = Map.ofEntries(
            ExtendedIdentifier.of("minecraft:desert").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:forest").mapTo("*hills"),
            ExtendedIdentifier.of("moderner_beta:late_beta_plains").mapTo("minecraft:forest"),
            ExtendedIdentifier.of("moderner_beta:early_release_taiga").mapTo("*hills"),
            ExtendedIdentifier.of("moderner_beta:early_release_ice_plains").mapTo("*hills")
        );
        if (addJungles) {
            hillsVariants = new HashMap<>(hillsVariants);
            hillsVariants.put(
                ExtendedIdentifier.of("minecraft:jungle"),
                ExtendedIdentifier.of("minecraft:jungle*hills")
            );
        }

        Layer swampLakesLayer = new WeightedPoolLayer("swamp_lakes", 1000, WeightedList.<LayerTarget>builder()
            .add(LayerTarget.biome(ExtendedBiomeIds.RIVER), 1)
            .add(LayerTarget.none(), 5)
            .build());
        List<PredicateOverlayLayer.Target> lakeOverlays = List.of(
            PredicateOverlayLayer.Target.layer(
                BiomePredicate.of(ExtendedIdentifier.of("~moderner_beta:early_release_swampland")),
                "swamp_lakes"
            )
        );
        if (addJungles) {
            lakeOverlays = new ArrayList<>(lakeOverlays);
            lakeOverlays.add(
                PredicateOverlayLayer.Target.layer(
                    BiomePredicate.of(ExtendedIdentifier.of("~minecraft:jungle")),
                    "jungle_lakes"
                )
            );
        }

        Layer icePlainsLayer = taigasInIcePlains
            ? new RandomBiomeLayer("ice_plains", 200, biomePool.stream()
                .map(biome -> biome.isOf(ModernBetaBiomes.EARLY_RELEASE_TAIGA) ? biome : icePlains)
                .toList())
            : new ConstantBiomeLayer("ice_plains", 0, icePlains);

        List<Layer> layers = new ArrayList<>(Arrays.asList(
            new InitLandLayer("land", 1),
            new FuzzyZoomLayer("land", 2000, "land"),
            AddLandLayer.forIslandScale("land", 1, "land"),
            new ModalZoomLayer("land", 2001, "land"),
            AddLandLayer.forIslandScale("land", 2, "land"),
            new WeightedPoolLayer("snow", 2, WeightedList.<LayerTarget>builder()
                .add(LayerTarget.biome(ExtendedBiomeIds.SNOWY_PLAINS), 1)
                .add(LayerTarget.none(), 4)
                .build()),
            new BiomeReplacementLayer("land", 0, "land", Map.of(ExtendedBiomeIds.PLAINS, LayerTarget.layer("snow"))),
            new ModalZoomLayer("land", 2002, "land"),
            AddLandLayer.forIslandScale("land", 3, "land"),
            new ModalZoomLayer("land", 2003, "land"),
            AddLandLayer.forIslandScale("land", 4, "land"),
            ConditionalOverlayLayer.mushroomIslands(),
            new InitRiverLayer("river", 100, "land"),
            StackedZoomLayer.modal("river", 1000, "river", 6 + biomeScale),
            new ComputeRiverLayer("river", 0, "river", true),
            new SmoothLayer("river", 1000, "river"),
            new RandomBiomeLayer("biome_pool", 200, biomePool),
            icePlainsLayer,
            new BiomeReplacementLayer("land", 0, "land", Map.of(
                ExtendedBiomeIds.PLAINS, LayerTarget.layer("biome_pool"),
                ExtendedBiomeIds.FROZEN_OCEAN, LayerTarget.layer("ice_plains"),
                ExtendedBiomeIds.SNOWY_PLAINS, LayerTarget.layer("ice_plains")
            )),
            StackedZoomLayer.modal("land", 1000, "land", 2),
            BiomeReplacementLayer.toBiomes("hills", 0, "land", hillsVariants),
            new ConditionalOverlayLayer(
                "land", 1000, "land",
                BiomePredicate.simpleHills(hillsVariants.keySet()),
                LayerTarget.layer("hills"), LayerTarget.none()
            ),
            new ModalZoomLayer("land_0", 1000, "land"),
            AddLandLayer.forEarlyRelease("land_0", 3, "land_0", icePlains),
            new ModalZoomLayer("land_1", 1001, "land_0"),
            new PredicateOverlayLayer("land_1", 0, "land_1", List.of(
                PredicateOverlayLayer.Target.MUSHROOM_SHORE,
                PredicateOverlayLayer.Target.inclusiveBeach(
                    ExtendedIdentifier.setOf(
                        "minecraft:ocean",
                        "minecraft:river",
                        "moderner_beta:early_release_extreme_hills",
                        "moderner_beta:early_release_swampland"
                    ),
                    ExtendedBiomeIds.BEACH
                ),
                PredicateOverlayLayer.Target.biome(
                    BiomePredicate.of(ExtendedIdentifier.of("moderner_beta:early_release_extreme_hills"))
                        .and(BiomePredicate.border()),
                    ExtendedIdentifier.of("moderner_beta:early_release_extreme_hills*edge")
                )
            )),
            swampLakesLayer,
            new PredicateOverlayLayer("land_1", 1000, "land_1", lakeOverlays)
        ));
        for (int i = 0; i < 2 + biomeScale; i++) {
            layers.add(new ModalZoomLayer("land_" + (2 + i), 1001 + i, "land_" + (1 + i)));
        }
        layers.add(new SmoothLayer("land", 1000, "land_" + (3 + biomeScale)));
        layers.add(MixRiverLayer.forEarlyRelease("land", 0, "land", "river"));

        //StackedZoomLayer.modal("land", 1002, "land", 2 + biomeScale),
        //    new SmoothLayer("land", 1000, "land"),
        //    MixRiverLayer.forEarlyRelease("land", 0, "land", "river")

        if (addJungles) {
            Layer jungleLakesLayer = new WeightedPoolLayer("jungle_lakes", 1000, WeightedList.<LayerTarget>builder()
                .add(LayerTarget.biome(ExtendedBiomeIds.RIVER), 1)
                .add(LayerTarget.none(), 7)
                .build());

            layers = new ArrayList<>(layers);
            layers.add(layers.lastIndexOf(swampLakesLayer), jungleLakesLayer);
        }

        return new ConfiguredLayers(layers, earlyReleaseLayerOutputs(biomeScale));
    }

    private static ModernBetaSettingsPreset preset11(boolean amplified, int biomeScale) {
        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(CAVE_GENERATION, CaveGeneration.EARLY_RELEASE)
                .add(FORCED_BIOME_HEIGHT, amplified ? ForcedBiomeHeight.AMPLIFIED : ForcedBiomeHeight.ENABLED)
                .add(SEA_LEVEL, 63)
                .add(NOISE_3D_SETTINGS, Noise3DSettings.EARLY_RELEASE)
                .add(PERLIN_NOISE_SETTINGS, PerlinNoiseSettings.RELEASE)
                .add(NOISE_LANDMASS, NoiseLandmass.RELEASE)
                .add(SURFACE_PROPERTIES, SurfaceProperties.EARLY_RELEASE)
                .build(),
            ModernBetaSettings.betaFractalLayers(configuredLayers11Era(biomeScale, false, false), ClimateDistribution.RELEASE_1_1)
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.NONE)
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }

    private static ModernBetaSettingsPreset preset125(boolean amplified, int biomeScale) {
        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(CAVE_GENERATION, CaveGeneration.EARLY_RELEASE)
                .add(FORCED_BIOME_HEIGHT, amplified ? ForcedBiomeHeight.AMPLIFIED : ForcedBiomeHeight.ENABLED)
                .add(SEA_LEVEL, 63)
                .add(NOISE_3D_SETTINGS, Noise3DSettings.EARLY_RELEASE)
                .add(PERLIN_NOISE_SETTINGS, PerlinNoiseSettings.RELEASE)
                .add(NOISE_LANDMASS, NoiseLandmass.RELEASE)
                .add(SURFACE_PROPERTIES, SurfaceProperties.EARLY_RELEASE)
                .build(),
            ModernBetaSettings.fractalLayers(configuredLayers11Era(biomeScale, true, false))
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.NONE)
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }

    private static ModernBetaSettingsPreset preset164(boolean amplified, int biomeScale) {
        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(CAVE_GENERATION, CaveGeneration.EARLY_RELEASE)
                .add(FORCED_BIOME_HEIGHT, ForcedBiomeHeight.overridesOnly(Map.of(
                    ExtendedIdentifier.of("minecraft:desert*hills"), new HeightConfig(0.3f, 0.8f),
                    ExtendedIdentifier.of("minecraft:forest*hills"), new HeightConfig(0.3f, 0.7f),
                    ExtendedIdentifier.of("moderner_beta:early_release_extreme_hills"), new HeightConfig(0.3f, 1.5f),
                    ExtendedIdentifier.of("moderner_beta:early_release_ice_plains*hills"), new HeightConfig(0.3f, 1.3f),
                    ExtendedIdentifier.of("minecraft:jungle*hills"), new HeightConfig(1.8f, 0.5f),
                    ExtendedIdentifier.of("moderner_beta:early_release_taiga*hills"), new HeightConfig(0.3f, 0.8f)
                ), amplified))
                .add(SEA_LEVEL, 63)
                .add(NOISE_3D_SETTINGS, Noise3DSettings.EARLY_RELEASE)
                .add(PERLIN_NOISE_SETTINGS, PerlinNoiseSettings.RELEASE)
                .add(NOISE_LANDMASS, NoiseLandmass.RELEASE)
                .add(SURFACE_PROPERTIES, SurfaceProperties.EARLY_RELEASE)
                .build(),
            ModernBetaSettings.fractalLayers(configuredLayers11Era(biomeScale, true, true))
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.NONE)
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }

    private static ConfiguredLayers configuredLayers1710Era(int biomeScale, int finiteSize, boolean bedrock, boolean saltedMutation, boolean climaticOceans, boolean bambooJungles, boolean strongBadlandsCategories, boolean modernBiomes) {
        if (bedrock) {
            saltedMutation = false;
            strongBadlandsCategories = false;
        }

        Set<ExtendedIdentifier> oceans = ExtendedIdentifier.setOf("minecraft:ocean", "minecraft:deep_ocean");
        BiomePredicate oceansPredicate = BiomePredicate.inSet(oceans);

        var hillVariants = Map.ofEntries(
            ExtendedIdentifier.of("minecraft:desert").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:forest").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:birch_forest").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:taiga").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:snowy_taiga").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:snowy_plains").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:jungle").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:bamboo_jungle").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:plains").mapTo("minecraft:forest"),
            ExtendedIdentifier.of("minecraft:windswept_hills").mapTo("minecraft:windswept_forest"),
            ExtendedIdentifier.of("minecraft:dark_forest").mapTo("minecraft:plains"),
            ExtendedIdentifier.of("minecraft:old_growth_pine_taiga").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:old_growth_spruce_taiga").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:savanna").mapTo("minecraft:savanna_plateau"),
            ExtendedIdentifier.of("minecraft:badlands*plateau").mapTo("minecraft:badlands"),
            ExtendedIdentifier.of("minecraft:wooded_badlands").mapTo("minecraft:badlands")
        );
        var mutatedVariants = Map.ofEntries(
            ExtendedIdentifier.of("minecraft:plains").mapTo("minecraft:sunflower_plains"),
            ExtendedIdentifier.of("minecraft:desert").mapTo("*lakes"),
            ExtendedIdentifier.of("minecraft:forest").mapTo("minecraft:flower_forest"),
            ExtendedIdentifier.of("minecraft:taiga").mapTo("*mountains"),
            ExtendedIdentifier.of("minecraft:swamp").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:mangrove_swamp").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:jungle").mapTo("*modified"),
            ExtendedIdentifier.of("minecraft:sparse_jungle").mapTo("*modified"),
            ExtendedIdentifier.of("minecraft:snowy_taiga").mapTo("*mountains"),
            ExtendedIdentifier.of("minecraft:snowy_plains").mapTo("minecraft:ice_spikes"),
            ExtendedIdentifier.of("minecraft:savanna").mapTo("minecraft:windswept_savanna"),
            ExtendedIdentifier.of("minecraft:savanna_plateau").mapTo("minecraft:windswept_savanna*plateau"),
            ExtendedIdentifier.of("minecraft:badlands").mapTo("minecraft:eroded_badlands"),
            ExtendedIdentifier.of("minecraft:wooded_badlands").mapTo("*modified"),
            ExtendedIdentifier.of("minecraft:badlands*plateau").mapTo("*modified_plateau"),
            ExtendedIdentifier.of("minecraft:birch_forest").mapTo("minecraft:old_growth_birch_forest"),
            ExtendedIdentifier.of("minecraft:birch_forest*hills").mapTo("minecraft:old_growth_birch_forest*hills"),
            ExtendedIdentifier.of("minecraft:dark_forest").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:old_growth_pine_taiga").mapTo("minecraft:old_growth_spruce_taiga"),
            ExtendedIdentifier.of("minecraft:old_growth_pine_taiga*hills").mapTo("minecraft:old_growth_spruce_taiga*hills"),
            ExtendedIdentifier.of("minecraft:windswept_hills").mapTo("minecraft:windswept_gravelly_hills"),
            ExtendedIdentifier.of("minecraft:windswept_forest").mapTo("minecraft:windswept_gravelly_hills")
        );
        var modernVariants = Map.ofEntries(
            //? if >=1.21.4
            ExtendedIdentifier.of("minecraft:dark_forest").mapTo("minecraft:pale_garden"),
            ExtendedIdentifier.of("minecraft:windswept_hills").mapTo("minecraft:meadow"),
            ExtendedIdentifier.of("minecraft:taiga").mapTo("minecraft:cherry_grove"),
            ExtendedIdentifier.of("minecraft:swamp").mapTo("minecraft:mangrove_swamp")
        );

        Map<String, Set<ExtendedIdentifier>> biomeCategories = Map.ofEntries(
            Map.entry("beach", ExtendedIdentifier.setOf(
                "~minecraft:beach",
                "~minecraft:snowy_beach"
            )),
            Map.entry("desert", ExtendedIdentifier.setOf(
                "~minecraft:desert"
            )),
            Map.entry("windswept_hills", ExtendedIdentifier.setOf(
                "~minecraft:windswept_gravelly_hills",
                "~minecraft:windswept_hills",
                "~minecraft:windswept_forest",
                "~minecraft:meadow"
            )),
            Map.entry("forest", ExtendedIdentifier.setOf(
                "~minecraft:birch_forest",
                "~minecraft:dark_forest",
                "~minecraft:flower_forest",
                "~minecraft:forest",
                "~minecraft:old_growth_birch_forest",
                //? if >=1.21.4
                "~minecraft:pale_garden",
                "~minecraft:cherry_grove"
            )),
            Map.entry("snowy_plains", ExtendedIdentifier.setOf(
                "~minecraft:ice_spikes",
                "~minecraft:snowy_plains"
            )),
            Map.entry("jungle", ExtendedIdentifier.setOf(
                "~minecraft:bamboo_jungle",
                "~minecraft:jungle",
                "~minecraft:sparse_jungle"
            )),
            Map.entry("badlands", ExtendedIdentifier.setOf(
                "minecraft:badlands",
                "minecraft:eroded_badlands",
                "minecraft:badlands*modified_plateau",
                "minecraft:wooded_badlands*modified"
            )),
            Map.entry("badlands_plateau", ExtendedIdentifier.setOf(
                "minecraft:badlands*plateau",
                "minecraft:wooded_badlands"
            )),
            Map.entry("badlands_all", ExtendedIdentifier.setOf(
                "~minecraft:badlands",
                "~minecraft:eroded_badlands",
                "~minecraft:wooded_badlands"
            )),
            Map.entry("mushroom_fields", ExtendedIdentifier.setOf(
                "~minecraft:mushroom_fields"
            )),
            Map.entry("ocean", oceans),
            Map.entry("plains", ExtendedIdentifier.setOf(
                "~minecraft:plains",
                "~minecraft:sunflower_plains"
            )),
            Map.entry("river", ExtendedIdentifier.setOf(
                "~minecraft:frozen_river",
                "~minecraft:river"
            )),
            Map.entry("savanna", ExtendedIdentifier.setOf(
                "~minecraft:savanna",
                "~minecraft:savanna_plateau",
                "~minecraft:windswept_savanna"
            )),
            Map.entry("swamp", ExtendedIdentifier.setOf(
                "~minecraft:swamp",
                "~minecraft:mangrove_swamp"
            )),
            Map.entry("taiga", ExtendedIdentifier.setOf(
                "~minecraft:old_growth_spurce_taiga",
                "~minecraft:old_growth_pine_taiga",
                "~minecraft:snowy_taiga",
                "~minecraft:taiga"
            )),
            Map.entry("jungle_like", ExtendedIdentifier.setOf(
                "~minecraft:bamboo_jungle",
                "~minecraft:jungle",
                "~minecraft:sparse_jungle",
                "minecraft:forest",
                "minecraft:taiga",
                "minecraft:ocean",
                "minecraft:deep_ocean"
            )),
            Map.entry("snowy", ExtendedIdentifier.setOf(
                "~minecraft:ice_spikes",
                "~minecraft:snowy_plains",
                "~minecraft:snowy_taiga"
            ))
        );
        List<Set<ExtendedIdentifier>> hillyCategories = List.of(
            biomeCategories.get("ocean"),
            biomeCategories.get("forest"),
            biomeCategories.get("taiga"),
            biomeCategories.get("plains"),
            biomeCategories.get("snowy_plains"),
            biomeCategories.get("savanna"),
            biomeCategories.get("desert"),
            biomeCategories.get("windswept_hills"),
            biomeCategories.get("jungle"),
            biomeCategories.get("badlands"),
            biomeCategories.get("badlands_plateau")
        );

        BiomePredicate hillPredicate;

        Set<ExtendedIdentifier> hillTargetBiomeSet = hillyCategories.stream()
            .flatMap(Set::stream)
            .collect(Collectors.toSet());
        hillPredicate = BiomePredicate.inSet(hillTargetBiomeSet)
            .and(BiomePredicate.neighborsMatch(hillyCategories, 3));
        if (!strongBadlandsCategories) {
            hillPredicate = hillPredicate.or(BiomePredicate.inSet(biomeCategories.get("badlands_plateau"))
                .and(BiomePredicate.neighborsMatch(
                    BiomePredicate.inSet(biomeCategories.get("badlands_all")), 3)));
        }

        boolean usesBiomeScale = finiteSize <= 0 || finiteSize >= 3072;

        List<Layer> layers = Stream.of(
            new InitLandLayer("land", 1),
            new FuzzyZoomLayer("land", 2000, "land"),
            AddLandLayer.forIslandScaleMajor("land", 1, "land"),
            new ModalZoomLayer("land", 2001, "land"),
            AddLandLayer.forIslandScaleMajor("land", 2, "land"),
            AddLandLayer.forIslandScaleMajor("land", 50, "land"),
            AddLandLayer.forIslandScaleMajor("land", 70, "land"),
            // RemoveTooMuchOcean
            new ConditionalOverlayLayer(
                "land", 2, "land",
                BiomePredicate.of(ExtendedBiomeIds.OCEAN)
                    .and(BiomePredicate.interior())
                    .and(BiomePredicate.oneIn(2)),
                LayerTarget.biome(ExtendedBiomeIds.PLAINS), LayerTarget.none()
            ),
            // region AddSnowLayer
            new WeightedPoolLayer("climate", 2, WeightedList.<LayerTarget>builder()
                .add(LayerTarget.biome(ExtendedBiomeIds.CLIMATE_SNOWY), 1)
                .add(LayerTarget.biome(ExtendedBiomeIds.CLIMATE_COOL), 1)
                .add(LayerTarget.biome(ExtendedBiomeIds.CLIMATE_WARM), 4)
                .build()),
            new BiomeReplacementLayer("land", 0, "land", Map.of(ExtendedBiomeIds.PLAINS, LayerTarget.layer("climate"))),
            // endregion AddSnowLayer
            AddLandLayer.forIslandScaleMajor("land", 3, "land"),
            // AddEdgeLayer.CoolWarm
            new ConditionalOverlayLayer(
                "land", 0, "land",
                BiomePredicate.of(ExtendedBiomeIds.CLIMATE_WARM)
                    .and(BiomePredicate.neighborsMatch(
                        BiomePredicate.inSet(
                            ExtendedBiomeIds.CLIMATE_COOL,
                            ExtendedBiomeIds.CLIMATE_SNOWY
                        ), 1
                    )),
                LayerTarget.biome(ExtendedBiomeIds.CLIMATE_TEMPERATE), LayerTarget.none()
            ),
            // AddEdgeLayer.HeatIce
            new ConditionalOverlayLayer(
                "land", 0, "land",
                BiomePredicate.of(ExtendedBiomeIds.CLIMATE_SNOWY)
                    .and(BiomePredicate.neighborsMatch(
                        BiomePredicate.inSet(
                            ExtendedBiomeIds.CLIMATE_TEMPERATE,
                            ExtendedBiomeIds.CLIMATE_WARM
                        ), 1
                    )),
                LayerTarget.biome(ExtendedBiomeIds.CLIMATE_COOL), LayerTarget.none()
            ),
            // region AddEdgeLayer.Special
            new RandomBiomeLayer("climate_warm_rare", 3, ExtendedBiomeIds.CLIMATE_WARM_RARE).skipRandom(1),
            new RandomBiomeLayer("climate_temperate_rare", 3, ExtendedBiomeIds.CLIMATE_TEMPERATE_RARE).skipRandom(1),
            new RandomBiomeLayer("climate_cool_rare", 3, ExtendedBiomeIds.CLIMATE_COOL_RARE).skipRandom(1),
            new RandomBiomeLayer("climate_snowy_rare", 3, ExtendedBiomeIds.CLIMATE_SNOWY_RARE).skipRandom(1),
            new BiomeReplacementLayer("rare_climates", 0, "land", Map.ofEntries(
                Map.entry(ExtendedBiomeIds.CLIMATE_WARM, LayerTarget.layer("climate_warm_rare")),
                Map.entry(ExtendedBiomeIds.CLIMATE_TEMPERATE, LayerTarget.layer("climate_temperate_rare")),
                Map.entry(ExtendedBiomeIds.CLIMATE_COOL, LayerTarget.layer("climate_cool_rare")),
                Map.entry(ExtendedBiomeIds.CLIMATE_SNOWY, LayerTarget.layer("climate_snowy_rare"))
            )),
            new PredicateOverlayLayer("land", 3, "land", List.of(
                PredicateOverlayLayer.Target.layer(
                    BiomePredicate.oneIn(13),
                    "rare_climates"
                )
            )),
            // endregion
            new ModalZoomLayer("land", 2002, "land"),
            new ModalZoomLayer("land", 2003, "land"),
            AddLandLayer.forIslandScaleMajor("land", 4, "land"),
            usesBiomeScale ? ConditionalOverlayLayer.mushroomIslands() : null,
            new ConditionalOverlayLayer(
                "land", 0, "land",
                BiomePredicate.of(ExtendedBiomeIds.OCEAN)
                    .and(BiomePredicate.interior()),
                LayerTarget.biome(ExtendedBiomeIds.DEEP_OCEAN), LayerTarget.none()
            ),
            new SupplyRandomLayer("mutation", 100, 299999),
            new ConditionalOverlayLayer(
                "mutation", 0, "land", BiomePredicate.of(ExtendedBiomeIds.OCEAN),
                LayerTarget.none(), LayerTarget.layer("mutation")
            ),
            bedrock ? StackedZoomLayer.modal("river", 1001, "mutation", 2, 0) : StackedZoomLayer.modal("river", 1000, "mutation", 2),
            bedrock ? StackedZoomLayer.modal("river", 1001, "river", 4 + biomeScale, 0) : StackedZoomLayer.modal("river", 1000, "river", 4 + biomeScale),
            saltedMutation
                ? StackedZoomLayer.modal("mutation", 1000, "mutation", 2)
                : StackedZoomLayer.modal("mutation", 1000, "mutation", 2).unsalted(),
            new ComputeRiverLayer("river", 0, "river", false),
            new SmoothLayer("river", 1000, "river"),
            // region BiomeInitLayer
            new RandomBiomeLayer("biome_pool_warm", 200, ExtendedIdentifier.listOf(
                "minecraft:desert",
                "minecraft:desert",
                "minecraft:desert",
                "minecraft:savanna",
                "minecraft:savanna",
                "minecraft:plains"
            )),
            new RandomBiomeLayer("biome_pool_warm_rare", 200, ExtendedIdentifier.listOf(
                "minecraft:badlands*plateau",
                "minecraft:wooded_badlands",
                "minecraft:wooded_badlands"
            )),
            new RandomBiomeLayer("biome_pool_temperate", 200,
                bedrock
                    ? ExtendedIdentifier.listOf(
                        "minecraft:forest",
                        "minecraft:dark_forest",
                        "minecraft:windswept_hills",
                        "minecraft:plains",
                        "minecraft:plains",
                        "minecraft:plains",
                        "minecraft:birch_forest",
                        "minecraft:swamp"
                    )
                    : ExtendedIdentifier.listOf(
                        "minecraft:forest",
                        "minecraft:dark_forest",
                        "minecraft:windswept_hills",
                        "minecraft:plains",
                        "minecraft:birch_forest",
                        "minecraft:swamp"
                    )
            ),
            new ConstantBiomeLayer("biome_pool_temperate_rare", 200, ExtendedIdentifier.of("minecraft:jungle")),
            new RandomBiomeLayer("biome_pool_cool", 200, ExtendedIdentifier.listOf(
                "minecraft:forest",
                "minecraft:windswept_hills",
                "minecraft:taiga",
                "minecraft:plains"
            )),
            new ConstantBiomeLayer("biome_pool_cool_rare", 200, ExtendedIdentifier.of("minecraft:old_growth_pine_taiga")),
            new RandomBiomeLayer("biome_pool_snowy", 200, ExtendedIdentifier.listOf(
                "minecraft:snowy_plains",
                "minecraft:snowy_plains",
                "minecraft:snowy_plains",
                "minecraft:snowy_taiga"
            )),
            new BiomeReplacementLayer("land", 0, "land", Map.of(
                ExtendedBiomeIds.CLIMATE_WARM, LayerTarget.layer("biome_pool_warm"),
                ExtendedBiomeIds.CLIMATE_WARM_RARE.get(0).asWeak(), LayerTarget.layer("biome_pool_warm_rare"),
                ExtendedBiomeIds.CLIMATE_TEMPERATE, LayerTarget.layer("biome_pool_temperate"),
                ExtendedBiomeIds.CLIMATE_TEMPERATE_RARE.get(0).asWeak(), LayerTarget.layer("biome_pool_temperate_rare"),
                ExtendedBiomeIds.CLIMATE_COOL, LayerTarget.layer("biome_pool_cool"),
                ExtendedBiomeIds.CLIMATE_COOL_RARE.get(0).asWeak(), LayerTarget.layer("biome_pool_cool_rare"),
                ExtendedBiomeIds.CLIMATE_SNOWY, LayerTarget.layer("biome_pool_snowy"),
                ExtendedBiomeIds.CLIMATE_SNOWY_RARE.get(0).asWeak(), LayerTarget.layer("biome_pool_snowy")
            )),
            // endregion BiomeInitLayer
            modernBiomes ? BiomeReplacementLayer.toBiomes("modern_land", 3000, "land", modernVariants) : null,
            modernBiomes ? new ConditionalOverlayLayer(
                "land", 1003, "land", BiomePredicate.oneIn(5),
                LayerTarget.layer("modern_land"), LayerTarget.none()
            ) : null,
            bambooJungles ? new ConditionalOverlayLayer(
                "land", 1001, "land",
                BiomePredicate.of(ExtendedIdentifier.of("minecraft:jungle"))
                    .and(BiomePredicate.oneIn(10)),
                LayerTarget.biome("minecraft:bamboo_jungle"), LayerTarget.none()
            ) : null,
            bedrock ? StackedZoomLayer.modal("land", 1001, "land", 2, 0)
                : StackedZoomLayer.modal("land", 1000, "land", finiteSize <= 0 || finiteSize >= 5120 ? 2 : finiteSize >= 3072 ? 1 : 0),
            // BiomeTransitionLayer
            new PredicateOverlayLayer("land", 0, "land", Stream.of(
                // Mountain edge has been omitted because it ends up just not generating at all
                PredicateOverlayLayer.Target.biome(
                    BiomePredicate.inSet(
                        ExtendedIdentifier.of("minecraft:wooded_badlands"),
                        ExtendedIdentifier.of("minecraft:badlands*plateau")
                    ).and(BiomePredicate.neighborsMatch(
                        BiomePredicate.inSet(biomeCategories.get("badlands_all")), 4).invert()),
                    ExtendedIdentifier.of("minecraft:badlands")
                ),
                PredicateOverlayLayer.Target.borderTransition(
                    ExtendedIdentifier.of("minecraft:old_growth_pine_taiga"),
                    biomeCategories.get("taiga"),
                    ExtendedIdentifier.of("minecraft:taiga")
                ),
                PredicateOverlayLayer.Target.biome(
                    BiomePredicate.of(ExtendedIdentifier.of("minecraft:desert"))
                        .and(BiomePredicate.neighborsMatch(ExtendedIdentifier.of("minecraft:snowy_plains"), 1)),
                    ExtendedIdentifier.of("minecraft:windswept_forest")
                ),
                PredicateOverlayLayer.Target.biome(
                    BiomePredicate.of(ExtendedIdentifier.of("minecraft:swamp"))
                        .and(BiomePredicate.neighborsMatch(ExtendedIdentifier.of("minecraft:jungle"), 1)),
                    ExtendedIdentifier.of("minecraft:sparse_jungle")
                ),
                PredicateOverlayLayer.Target.biome(
                    BiomePredicate.of(ExtendedIdentifier.of("minecraft:swamp"))
                        .and(BiomePredicate.neighborsMatch(
                            BiomePredicate.inSet(
                                ExtendedIdentifier.of("minecraft:desert"),
                                ExtendedIdentifier.of("minecraft:snowy_taiga"),
                                ExtendedIdentifier.of("minecraft:snowy_plains")
                            ), 1)),
                    ExtendedIdentifier.of("minecraft:plains")
                )
                //? if >=1.21.4 {
                , modernBiomes ? PredicateOverlayLayer.Target.biome(
                    BiomePredicate.of(ExtendedIdentifier.of("minecraft:pale_garden"))
                        .and(BiomePredicate.border()),
                    ExtendedIdentifier.of("minecraft:dark_forest*hills")
                ) : null
                //?}
            ).filter(Objects::nonNull).toList()),
            // region RegionHillsLayer
            BiomeReplacementLayer.toBiomes("hills", 0, "land", hillVariants),
            new RandomBiomeLayer("deep_ocean_islands", 1000, ExtendedIdentifier.listOf(
                "minecraft:plains",
                "minecraft:forest"
            )).skipRandom(2),
            new PredicateOverlayLayer("hills", 1000, "hills", List.of(
                PredicateOverlayLayer.Target.biome(
                    BiomePredicate.of(ExtendedIdentifier.of("minecraft:forest"))
                        .and(BiomePredicate.oneIn(1))
                        .and(BiomePredicate.oneIn(3)),
                    ExtendedIdentifier.of("minecraft:forest*hills")
                ),
                PredicateOverlayLayer.Target.layer(
                    BiomePredicate.of(ExtendedIdentifier.of("minecraft:deep_ocean"))
                        .and(BiomePredicate.oneIn(1))
                        .and(BiomePredicate.oneIn(3)),
                    "deep_ocean_islands"
                ),
                PredicateOverlayLayer.Target.biome(
                    BiomePredicate.of(ExtendedIdentifier.of("minecraft:ocean")),
                    ExtendedIdentifier.of("minecraft:deep_ocean")
                )
            )),
            new ConditionalOverlayLayer(
                "hills", 1000, "mutation", BiomePredicate.oneIn(3).or(BiomePredicate.wrappedIntMatch(29, 0)),
                LayerTarget.layer("hills"), LayerTarget.layer("land")
            ),
            BiomeReplacementLayer.toBiomes("mutated_hills", 0, "hills", mutatedVariants),
            new ConditionalOverlayLayer(
                "hills", 0, "mutation", BiomePredicate.wrappedIntMatch(29, 0),
                LayerTarget.layer("mutated_hills"), LayerTarget.layer("hills")
            ),
            new ConditionalOverlayLayer(
                "land_with_hills", 0, "land", hillPredicate,
                LayerTarget.layer("hills"), LayerTarget.layer("land")
            ),
            BiomeReplacementLayer.toBiomes("mutated_land", 0, "land", mutatedVariants),
            new ConditionalOverlayLayer(
                "mutated_land", 0, "mutation", BiomePredicate.wrappedIntMatch(29, 1),
                LayerTarget.layer("mutated_land"), LayerTarget.layer("land_with_hills")
            ),
            new ConditionalOverlayLayer(
                "land", 0, "land", BiomePredicate.of(ExtendedBiomeIds.OCEAN).invert(),
                LayerTarget.layer("mutated_land"), LayerTarget.layer("land_with_hills")
            ),
            // endregion RegionHillsLayer
            new ConditionalOverlayLayer(
                "land", 1001, "land",
                BiomePredicate.of(ExtendedBiomeIds.PLAINS)
                    .and(BiomePredicate.oneIn(57)),
                LayerTarget.biome("sunflower_plains"), LayerTarget.none()
            ),
            new ModalZoomLayer("land", 1000, "land"),
            AddLandLayer.forMajorRelease("land", 3, "land"),
            !usesBiomeScale ? ConditionalOverlayLayer.mushroomIslands() : null,
            new ModalZoomLayer("land", 1001, "land"),
            // GrowMushroomIslandLayer (LCE)
            !usesBiomeScale ? new ConditionalOverlayLayer(
                "land", 0, "land",
                BiomePredicate.diagonalNeighborsMatch(ExtendedBiomeIds.MUSHROOM_ISLAND, 1),
                LayerTarget.biome(ExtendedBiomeIds.MUSHROOM_ISLAND),
                LayerTarget.none()
            ) : null,
            // ShoreLayer
            new PredicateOverlayLayer("land", 0, "land", List.of(
                PredicateOverlayLayer.Target.MUSHROOM_SHORE,
                PredicateOverlayLayer.Target.biome(
                    BiomePredicate.inSet(biomeCategories.get("jungle"))
                        .and(BiomePredicate.neighborsMatch(
                            BiomePredicate.inSet(biomeCategories.get("jungle_like")).invert(), 1)),
                    ExtendedIdentifier.of("minecraft:sparse_jungle")
                ),
                PredicateOverlayLayer.Target.exclusiveBeach(
                    Stream.of(
                        ExtendedIdentifier.of("minecraft:windswept_hills"),
                        ExtendedIdentifier.of("minecraft:windswept_forest"),
                        modernBiomes ? ExtendedIdentifier.of("minecraft:meadow") : null,
                        modernBiomes ? ExtendedIdentifier.of("minecraft:cherry_grove") : null
                    ).filter(Objects::nonNull).collect(Collectors.toSet()),
                    oceansPredicate,
                    ExtendedIdentifier.of("minecraft:stony_shore")
                ),
                PredicateOverlayLayer.Target.exclusiveBeach(
                    biomeCategories.get("snowy"),
                    oceansPredicate,
                    ExtendedIdentifier.of("minecraft:snowy_beach")
                ),
                PredicateOverlayLayer.Target.biome(
                    BiomePredicate.inSet(ExtendedIdentifier.setOf(
                        "minecraft:badlands",
                        "minecraft:wooded_badlands"
                    ))
                        .and(BiomePredicate.neighborsMatch(oceansPredicate, 1).invert())
                        .and(BiomePredicate.neighborsMatch(
                            BiomePredicate.inSet(biomeCategories.get("badlands_all")).invert(), 1)),
                    ExtendedIdentifier.of("minecraft:desert")
                ),
                PredicateOverlayLayer.Target.inclusiveBeach(
                    ExtendedIdentifier.setOf(
                        "minecraft:ocean",
                        "minecraft:deep_ocean",
                        "minecraft:river",
                        "minecraft:swamp",
                        "minecraft:mangrove_swamp",
                        "minecraft:mushroom_fields",
                        "minecraft:badlands",
                        "minecraft:wooded_badlands"
                    ),
                    oceansPredicate,
                    ExtendedBiomeIds.BEACH
                ),
                PredicateOverlayLayer.Target.biome(
                    BiomePredicate.of(ExtendedIdentifier.of("moderner_beta:early_release_extreme_hills"))
                        .and(BiomePredicate.border()),
                    ExtendedIdentifier.of("moderner_beta:early_release_extreme_hills*edge")
                )
            )),
            StackedZoomLayer.modal("land", 1002, "land", 2 + biomeScale),
            new SmoothLayer("land", 1000, "land"),
            MixRiverLayer.forMajorRelease("land", 0, "land", "river"),
            climaticOceans
                ? bedrock
                    ? new WeightedPoolLayer("ocean_climate", 2, WeightedList.<LayerTarget>builder()
                        .add(LayerTarget.biome(ExtendedBiomeIds.WARM_OCEAN), 8)
                        .add(LayerTarget.biome(ExtendedBiomeIds.LUKEWARM_OCEAN), 32)
                        .add(LayerTarget.biome(ExtendedBiomeIds.OCEAN), 28)
                        .add(LayerTarget.biome(ExtendedBiomeIds.COLD_OCEAN), 27)
                        .add(LayerTarget.biome(ExtendedBiomeIds.FROZEN_OCEAN), 5)
                        .build())
                    : new MappedNoiseLayer("ocean_climate", 2, List.of(
                        new MappedNoiseLayer.Entry(0.4, ExtendedBiomeIds.WARM_OCEAN),
                        new MappedNoiseLayer.Entry(0.2, ExtendedBiomeIds.LUKEWARM_OCEAN),
                        new MappedNoiseLayer.Entry(0.0, ExtendedBiomeIds.OCEAN),
                        new MappedNoiseLayer.Entry(-0.2, ExtendedBiomeIds.COLD_OCEAN),
                        new MappedNoiseLayer.Entry(-0.4, ExtendedBiomeIds.FROZEN_OCEAN)
                    ), 8.0, DoubleList.of(1), false)
                : null,
            climaticOceans && bedrock ? new ConditionalOverlayLayer("ocean_climate", 2, "ocean_climate",
                BiomePredicate.anyOf(
                    BiomePredicate.of(ExtendedBiomeIds.WARM_OCEAN)
                        .and(BiomePredicate.neighborsMatch(ExtendedBiomeIds.FROZEN_OCEAN, 1)),
                    BiomePredicate.of(ExtendedBiomeIds.FROZEN_OCEAN)
                        .and(BiomePredicate.neighborsMatch(ExtendedBiomeIds.WARM_OCEAN, 1))
                ),
                LayerTarget.biome(ExtendedBiomeIds.OCEAN),
                LayerTarget.none()
            ) : null,
            climaticOceans
                ? bedrock
                    ? StackedZoomLayer.modal("ocean_climate", 2002, "ocean_climate", 6, 0)
                    : StackedZoomLayer.modal("ocean_climate", 2001, "ocean_climate", 6)
                : null,
            climaticOceans ? new ApplyOceanClimateLayer("land", 0, "land", "ocean_climate", !bedrock) : null
        ).filter(Objects::nonNull).toList();

        return new ConfiguredLayers(layers, Map.of(ModernBetaBuiltInTypes.LayerOutput.BIOME.id, "land"));
    }

    private static ModernBetaSettingsPreset preset1122(BootstrapContext<ModernBetaSettingsPreset> context, boolean amplified, int biomeScale, boolean bedrock) {
        Map<ExtendedIdentifier, HeightConfig> heightOverrides = HeightConfig.MAJOR_RELEASE_CONFIGS;
        if (bedrock) {
            heightOverrides = new HashMap<>(heightOverrides);
            heightOverrides.put(ExtendedIdentifier.of("minecraft:wooded_badlands"), heightOverrides.get(ExtendedIdentifier.of("minecraft:badlands")));
        }

        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.NOISE_3D.id)
                .add(NOISE_GENERATOR_SETTINGS, context.lookup(Registries.NOISE_SETTINGS)
                        .get(bedrock ? ModernBetaNoiseGeneratorSettings.OVERWORLD_128 : ModernBetaNoiseGeneratorSettings.OVERWORLD_256).orElseThrow())
                .add(USE_SURFACE_RULES, true)
                .add(CAVE_GENERATION, bedrock ? CaveGeneration.BEDROCK : CaveGeneration.RELEASE_1_12_2)
                .add(FORCED_BIOME_HEIGHT, ForcedBiomeHeight.overridesOnly(heightOverrides, amplified))
                .add(SEA_LEVEL, 63)
                .add(NOISE_3D_SETTINGS, bedrock ? Noise3DSettings.BEDROCK : Noise3DSettings.MAJOR_RELEASE)
                .add(PERLIN_NOISE_SETTINGS, PerlinNoiseSettings.RELEASE)
                .add(NOISE_LANDMASS, NoiseLandmass.RELEASE)
                .add(SURFACE_PROPERTIES, SurfaceProperties.MAJOR_RELEASE)
                .add(STRUCTURE_MODIFERS, StructureModifiers.VANILLA)
                .build(),
            ModernBetaSettings.fractalLayers(configuredLayers1710Era(biomeScale, 0, bedrock, false, false, false, false, false))
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.MAJOR_RELEASE)
                .add(USE_32BIT_LAYER_SEED, bedrock)
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }

    private static ModernBetaSettingsPreset preset1171(BootstrapContext<ModernBetaSettingsPreset> context, boolean amplified, int biomeScale, boolean bedrock) {
        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.NOISE_3D.id)
                .add(NOISE_GENERATOR_SETTINGS, context.lookup(Registries.NOISE_SETTINGS)
                        .get(bedrock ? ModernBetaNoiseGeneratorSettings.OVERWORLD_128 : ModernBetaNoiseGeneratorSettings.OVERWORLD_256).orElseThrow())
                .add(USE_SURFACE_RULES, true)
                .add(CAVE_GENERATION, bedrock ? CaveGeneration.BEDROCK : CaveGeneration.RELEASE_1_17_1)
                .add(FORCED_BIOME_HEIGHT, ForcedBiomeHeight.overridesOnly(HeightConfig.MAJOR_RELEASE_CONFIGS, amplified))
                .add(SEA_LEVEL, 63)
                .add(NOISE_3D_SETTINGS, bedrock ? Noise3DSettings.BEDROCK : Noise3DSettings.MAJOR_RELEASE)
                .add(PERLIN_NOISE_SETTINGS, PerlinNoiseSettings.RELEASE)
                .add(NOISE_LANDMASS, NoiseLandmass.RELEASE)
                .add(SURFACE_PROPERTIES, SurfaceProperties.MAJOR_RELEASE)
                .add(STRUCTURE_MODIFERS, StructureModifiers.VANILLA)
                .build(),
            ModernBetaSettings.fractalLayers(configuredLayers1710Era(biomeScale, 0, bedrock, true, true, true, true, false))
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.MAJOR_RELEASE)
                .add(USE_32BIT_LAYER_SEED, bedrock)
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }

    private static ModernBetaSettingsPreset presetReleaseXboxLegacy(BootstrapContext<ModernBetaSettingsPreset> context, int finiteSize) {
        HolderGetter<Structure> structureRegistry = context.lookup(Registries.STRUCTURE);
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);

        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.NOISE_3D.id)
                .add(NOISE_GENERATOR_SETTINGS, context.lookup(Registries.NOISE_SETTINGS)
                        .get(ModernBetaNoiseGeneratorSettings.OVERWORLD_256).orElseThrow())
                .add(USE_SURFACE_RULES, true)
                .add(CAVE_GENERATION, CaveGeneration.EARLY_RELEASE)
                .add(FORCED_BIOME_HEIGHT, ForcedBiomeHeight.overridesOnly(HeightConfig.MAJOR_RELEASE_CONFIGS))
                .add(SEA_LEVEL, 63)
                .add(NOISE_3D_SETTINGS, Noise3DSettings.MAJOR_RELEASE)
                .add(PERLIN_NOISE_SETTINGS, PerlinNoiseSettings.RELEASE)
                .add(NOISE_LANDMASS, NoiseLandmass.RELEASE)
                .add(SURFACE_PROPERTIES, SurfaceProperties.MAJOR_RELEASE)
                .add(WORLD_BORDER, WorldBorderLocation.xboxLegacy(finiteSize))
                .add(STRUCTURE_MODIFERS, StructureModifiers.getFinite(structureRegistry, biomeRegistry, finiteSize, finiteSize, false, false))
                .build(),
            ModernBetaSettings.fractalLayers(configuredLayers1710Era(0, finiteSize, false, false, false, false, false, false))
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.MAJOR_RELEASE)
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }

    private static ModernBetaSettingsPreset presetSnowAintSnowier(BootstrapContext<ModernBetaSettingsPreset> context, boolean amplified, int biomeScale) {
        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.NOISE_3D.id)
                .add(NOISE_GENERATOR_SETTINGS, context.lookup(Registries.NOISE_SETTINGS)
                        .get(ModernBetaNoiseGeneratorSettings.OVERWORLD_256).orElseThrow())
                .add(USE_SURFACE_RULES, true)
                .add(CAVE_GENERATION, CaveGeneration.RELEASE_1_17_1)
                .add(FORCED_BIOME_HEIGHT, ForcedBiomeHeight.overridesOnly(HeightConfig.MAJOR_RELEASE_CONFIGS, amplified))
                .add(SEA_LEVEL, 63)
                .add(NOISE_3D_SETTINGS, Noise3DSettings.MAJOR_RELEASE)
                .add(PERLIN_NOISE_SETTINGS, PerlinNoiseSettings.RELEASE)
                .add(NOISE_LANDMASS, NoiseLandmass.RELEASE)
                .add(SURFACE_PROPERTIES, SurfaceProperties.MAJOR_RELEASE)
                .add(STRUCTURE_MODIFERS, StructureModifiers.VANILLA)
                .build(),
            ModernBetaSettings.fractalLayers(configuredLayers1710Era(biomeScale, 0, false, true, true, true, true, true))
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.MAJOR_RELEASE)
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }

    private static ModernBetaSettingsPreset presetReleaseHybrid(boolean amplified, int biomeScale) {
        Map<ExtendedIdentifier, ExtendedIdentifier> hillsVariants = Map.ofEntries(
            ExtendedIdentifier.of("minecraft:desert").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:forest").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:windswept_hills").mapTo("minecraft:windswept_forest"),
            ExtendedIdentifier.of("minecraft:swamp").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:plains").mapTo("minecraft:forest"),
            ExtendedIdentifier.of("minecraft:taiga").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:jungle").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:snowy_taiga").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:snowy_plains").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:savanna").mapTo("minecraft:savanna_plateau"),
            ExtendedIdentifier.of("minecraft:dark_forest").mapTo("minecraft:plains"),
            ExtendedIdentifier.of("minecraft:birch_forest").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:old_growth_birch_forest").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:wooded_badlands").mapTo("minecraft:badlands"),
            ExtendedIdentifier.of("minecraft:mangrove_swamp").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:flower_forest").mapTo("*hills"),
            ExtendedIdentifier.of("minecraft:sparse_jungle").mapTo("minecraft:jungle"),
            ExtendedIdentifier.of("minecraft:badlands*plateau").mapTo("minecraft:badlands")
        );
        Map<ExtendedIdentifier, ExtendedIdentifier> mutatedVariants = Map.ofEntries(
            ExtendedIdentifier.of("minecraft:plains").mapTo("minecraft:sunflower_plains"),
            ExtendedIdentifier.of("minecraft:forest").mapTo("minecraft:flower_forest"),
            ExtendedIdentifier.of("minecraft:forest*hills").mapTo("minecraft:flower_forest*hills"),
            ExtendedIdentifier.of("minecraft:swamp").mapTo("minecraft:swamp*hills"),
            ExtendedIdentifier.of("minecraft:savanna").mapTo("minecraft:windswept_savanna"),
            ExtendedIdentifier.of("minecraft:savanna_plateau").mapTo("minecraft:windswept_savanna*plateau"),
            ExtendedIdentifier.of("minecraft:badlands*plateau").mapTo("minecraft:wooded_badlands"),
            ExtendedIdentifier.of("minecraft:birch_forest").mapTo("minecraft:old_growth_birch_forest"),
            ExtendedIdentifier.of("minecraft:birch_forest*hills").mapTo("minecraft:old_growth_birch_forest*hills"),
            //? if >=1.21.4 {
            ExtendedIdentifier.of("minecraft:dark_forest").mapTo("minecraft:pale_garden"),
            ExtendedIdentifier.of("minecraft:dark_forest*hills").mapTo("minecraft:pale_garden*hills"),
            //?}
            ExtendedIdentifier.of("minecraft:old_growth_pine_taiga").mapTo("minecraft:old_growth_spruce_taiga"),
            ExtendedIdentifier.of("minecraft:old_growth_pine_taiga*hills").mapTo("minecraft:old_growth_spruce_taiga*hills"),
            ExtendedIdentifier.of("minecraft:windswept_hills").mapTo("minecraft:windswept_gravelly_hills"),
            ExtendedIdentifier.of("minecraft:windswept_forest").mapTo("minecraft:windswept_gravelly_hills"),
            ExtendedIdentifier.of("minecraft:snowy_plains").mapTo("minecraft:ice_spikes")
        );

        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(USE_SURFACE_RULES, true)
                .add(CAVE_GENERATION, CaveGeneration.EARLY_RELEASE)
                .add(FORCED_BIOME_HEIGHT, ForcedBiomeHeight.overridesOnly(
                    Map.ofEntries(
                        Map.entry(ExtendedIdentifier.of("minecraft:desert*hills"), new HeightConfig(0.3f, 0.8f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:forest*hills"), new HeightConfig(0.3f, 0.7f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:taiga*hills"), new HeightConfig(0.3f, 0.8f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:dark_forest*hills"), new HeightConfig(0.3f, 0.7f)),
                        //? if >=1.21.4
                        Map.entry(ExtendedIdentifier.of("minecraft:pale_garden*hills"), new HeightConfig(0.3f, 0.7f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:birch_forest*hills"), new HeightConfig(0.3f, 0.7f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:old_growth_birch_forest"), new HeightConfig(0.1f, 0.8f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:old_growth_birch_forest*hills"), new HeightConfig(0.3f, 1.3f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:flower_forest"), new HeightConfig(0.1f, 0.8f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:flower_forest*hills"), new HeightConfig(0.3f, 1.3f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:old_growth_spruce_taiga*hills"), new HeightConfig(0.3f, 0.8f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:snowy_taiga*hills"), new HeightConfig(0.3f, 0.8f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:snowy_plains*hills"), new HeightConfig(0.3f, 1.3f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:jungle*hills"), new HeightConfig(1.8f, 0.5f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:badlands*plateau"), new HeightConfig(1.8f, 0.2f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:wooded_badlands"), new HeightConfig(1.8f, 0.2f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:cherry_grove"), new HeightConfig(1.8f, 0.5f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:cherry_grove*edge"), new HeightConfig(0.8f, 0.3f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:windswept_hills"), new HeightConfig(0.3f, 1.5f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:windswept_forest"), new HeightConfig(0.3f, 1.5f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:windswept_gravelly_hills"), new HeightConfig(0.3f, 1.5f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:meadow"), new HeightConfig(1.0f, 1.0f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:stony_shore"), new HeightConfig(0.1f, 1.6f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:ice_spikes"), new HeightConfig(0.3f, 0.8f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:windswept_savanna"), new HeightConfig(0.3f, 1.5f)),
                        Map.entry(ExtendedIdentifier.of("minecraft:windswept_savanna*plateau"), new HeightConfig(1.0f, 1.0f))
                    ), amplified
                ))
                .add(SEA_LEVEL, 63)
                .add(NOISE_3D_SETTINGS, Noise3DSettings.EARLY_RELEASE)
                .add(PERLIN_NOISE_SETTINGS, PerlinNoiseSettings.RELEASE)
                .add(NOISE_LANDMASS, NoiseLandmass.RELEASE)
                .add(SURFACE_PROPERTIES, SurfaceProperties.EARLY_RELEASE)
                .build(),
            ModernBetaSettings.fractalLayers(
                Map.of(ModernBetaBuiltInTypes.LayerOutput.BIOME.id, "land"),
                new InitLandLayer("land", 1),
                new FuzzyZoomLayer("land", 2000, "land"),
                AddLandLayer.forIslandScale("land", 1, "land"),
                new ModalZoomLayer("land", 2001, "land"),
                AddLandLayer.forIslandScale("land", 2, "land"),
                new WeightedPoolLayer("snow", 2, WeightedList.<LayerTarget>builder()
                    .add(LayerTarget.biome(ExtendedBiomeIds.SNOWY_PLAINS), 1)
                    .add(LayerTarget.none(), 4)
                    .build()),
                new BiomeReplacementLayer("land", 0, "land", Map.of(ExtendedBiomeIds.PLAINS, LayerTarget.layer("snow"))),
                new ModalZoomLayer("land", 2002, "land"),
                AddLandLayer.forIslandScale("land", 3, "land"),
                new ModalZoomLayer("land", 2003, "land"),
                AddLandLayer.forIslandScale("land", 4, "land"),
                ConditionalOverlayLayer.mushroomIslands(),
                new InitRiverLayer("river", 100, "land"),
                StackedZoomLayer.modal("river", 1000, "river", 6 + biomeScale),
                new ComputeRiverLayer("river", 0, "river", true),
                new SmoothLayer("river", 1000, "river"),
                new RandomBiomeLayer("biome_pool", 200, ExtendedIdentifier.listOf(
                    // Deserts
                    "minecraft:desert",
                    "minecraft:desert",
                    "minecraft:desert",
                    "minecraft:desert",
                    "minecraft:badlands*plateau",
                    "minecraft:badlands*plateau",

                    // Forests
                    "minecraft:forest",
                    "minecraft:forest",
                    "minecraft:forest",
                    "minecraft:dark_forest",
                    "minecraft:birch_forest",
                    "minecraft:cherry_grove",

                    // Extreme Hills
                    "minecraft:windswept_hills",
                    "minecraft:windswept_hills",
                    "minecraft:windswept_hills",
                    "minecraft:windswept_hills",
                    "minecraft:meadow",
                    "minecraft:meadow",

                    // Swamps
                    "minecraft:swamp",
                    "minecraft:swamp",
                    "minecraft:swamp",
                    "minecraft:swamp",
                    "minecraft:mangrove_swamp",
                    "minecraft:mangrove_swamp",

                    // Plains
                    "minecraft:plains",
                    "minecraft:plains",
                    "minecraft:plains",
                    "minecraft:plains",
                    "minecraft:savanna",
                    "minecraft:savanna",

                    // Taigas
                    "minecraft:taiga",
                    "minecraft:taiga",
                    "minecraft:taiga",
                    "minecraft:taiga",
                    "minecraft:old_growth_spruce_taiga",
                    "minecraft:old_growth_spruce_taiga",

                    // Jungles
                    "minecraft:jungle",
                    "minecraft:jungle",
                    "minecraft:jungle",
                    "minecraft:jungle",
                    "minecraft:jungle",
                    "minecraft:sparse_jungle"
                )),
                new RandomBiomeLayer("snowy_biome_pool", 200, ExtendedIdentifier.listOf(
                    "minecraft:snowy_plains",
                    "minecraft:snowy_plains",
                    "minecraft:snowy_plains",
                    "minecraft:snowy_taiga"
                )),
                new BiomeReplacementLayer("land", 0, "land", Map.of(
                    ExtendedBiomeIds.PLAINS, LayerTarget.layer("biome_pool"),
                    ExtendedBiomeIds.FROZEN_OCEAN, LayerTarget.layer("snowy_biome_pool"),
                    ExtendedBiomeIds.SNOWY_PLAINS, LayerTarget.layer("snowy_biome_pool")
                )),
                StackedZoomLayer.modal("land", 1000, "land", 2),
                BiomeReplacementLayer.toBiomes("hills", 0, "land", hillsVariants),
                new ConditionalOverlayLayer(
                    "land", 1000, "land",
                    BiomePredicate.inSet(hillsVariants.keySet())
                        .and(BiomePredicate.identicalNeighbors(3, false))
                        .and(BiomePredicate.oneIn(3)),
                    LayerTarget.layer("hills"), LayerTarget.none()
                ),
                BiomeReplacementLayer.toBiomes("mutated_land", 0, "land", mutatedVariants),
                new MappedNoiseLayer("mutation", 7, List.of(
                    new MappedNoiseLayer.Entry(-1.0 / 3.0, ExtendedIdentifier.of("minecraft:the_void*mutation")),
                    new MappedNoiseLayer.Entry(0.0, ExtendedBiomeIds.NULL)
                ), 2, DoubleList.of(1), true),
                StackedZoomLayer.modal("mutation", 2005, "mutation", 2),
                new ConditionalOverlayLayer(
                    "land", 1000, "mutation",
                    BiomePredicate.of(ExtendedIdentifier.of("minecraft:the_void*mutation")),
                    LayerTarget.layer("mutated_land"), LayerTarget.layer("land")
                ),
                new ModalZoomLayer("land", 1000, "land"),
                AddLandLayer.forEarlyRelease("land", 3, "land", ExtendedIdentifier.of(Biomes.SNOWY_PLAINS)),
                new ModalZoomLayer("land", 1001, "land"),
                new PredicateOverlayLayer("land", 0, "land", List.of(
                    PredicateOverlayLayer.Target.MUSHROOM_SHORE,
                    PredicateOverlayLayer.Target.exclusiveBeach(
                        ExtendedIdentifier.setOf(
                            "minecraft:meadow",
                            "minecraft:cherry_grove"
                        ),
                        ExtendedIdentifier.of("minecraft:stony_shore")
                    ),
                    PredicateOverlayLayer.Target.biome(
                        BiomePredicate.of(ExtendedIdentifier.of("minecraft:cherry_grove"))
                            .and(BiomePredicate.border()),
                        ExtendedIdentifier.of("minecraft:cherry_grove*edge")
                    ),
                    PredicateOverlayLayer.Target.biome(
                        BiomePredicate.inSet(
                            ExtendedIdentifier.of("minecraft:badlands*plateau"),
                            ExtendedIdentifier.of("minecraft:wooded_badlands")
                        )
                            .and(BiomePredicate.neighborsMatch(
                                BiomePredicate.inSet(
                                    ExtendedIdentifier.of("~minecraft:badlands"),
                                    ExtendedIdentifier.of("~minecraft:wooded_badlands"),
                                    ExtendedIdentifier.of("~minecraft:eroded_badlands")
                                ), 4).invert()),
                        ExtendedIdentifier.of("minecraft:badlands")
                    ),
                    PredicateOverlayLayer.Target.inclusiveBeach(
                        ExtendedIdentifier.setOf(
                            "minecraft:ocean",
                            "minecraft:river",
                            "minecraft:windswept_hills",
                            "minecraft:windswept_forest",
                            "minecraft:windswept_gravelly_hills",
                            "minecraft:meadow",
                            "minecraft:badlands",
                            "minecraft:swamp",
                            "minecraft:mangrove_swamp"
                        ),
                        ExtendedBiomeIds.BEACH
                    )
                )),
                StackedZoomLayer.modal("land", 1002, "land", 2 + biomeScale),
                new SmoothLayer("land", 1000, "land"),
                MixRiverLayer.forEarlyRelease("land", 0, "land", "river"),
                new MappedNoiseLayer("ocean_climate", 2, List.of(
                    new MappedNoiseLayer.Entry(0.4, ExtendedBiomeIds.WARM_OCEAN),
                    new MappedNoiseLayer.Entry(0.2, ExtendedBiomeIds.LUKEWARM_OCEAN),
                    new MappedNoiseLayer.Entry(0.0, ExtendedBiomeIds.OCEAN),
                    new MappedNoiseLayer.Entry(-0.2, ExtendedBiomeIds.COLD_OCEAN),
                    new MappedNoiseLayer.Entry(-0.4, ExtendedBiomeIds.FROZEN_OCEAN)
                ), 8.0, DoubleList.of(1), false),
                StackedZoomLayer.modal("ocean_climate", 2001, "ocean_climate", 6),
                new ApplyOceanClimateLayer("land", 0, "land", "ocean_climate", true)
            )
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.MAJOR_RELEASE)
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }
}
