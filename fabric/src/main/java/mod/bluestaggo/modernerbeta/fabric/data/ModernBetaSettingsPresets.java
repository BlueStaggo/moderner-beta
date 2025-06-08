package mod.bluestaggo.modernerbeta.fabric.data;

import com.google.common.collect.ImmutableMap;
import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.TemperatureHeightScaling;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistryKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.component.*;
import mod.bluestaggo.modernerbeta.world.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomes;
import mod.bluestaggo.modernerbeta.world.biome.provider.climate.ClimateMapping;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ConfiguredLayers;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.*;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates.BiomePredicate;
import mod.bluestaggo.modernerbeta.world.biome.voronoi.VoronoiPointBiome;
import mod.bluestaggo.modernerbeta.world.chunk.provider.indev.IndevTheme;
import mod.bluestaggo.modernerbeta.world.chunk.provider.indev.IndevType;
import mod.bluestaggo.modernerbeta.world.chunk.provider.island.IslandShape;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.Pool;
import net.minecraft.world.biome.BiomeKeys;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes.*;

public final class ModernBetaSettingsPresets {
    public static final ModernBetaSettingsPreset DEFAULT_BETA = presetBeta(false);
    public static final ModernBetaSettingsPreset DEFAULT_MAJOR = preset1122(0);

    public static void bootstrap(Registerable<ModernBetaSettingsPreset> presetRegisterable) {
        presetRegisterable.register(keyOf("beta"), DEFAULT_BETA);
        presetRegisterable.register(keyOf("beta_1_1_02"), presetBeta(true));
        presetRegisterable.register(keyOf("alpha"), presetAlpha());
        presetRegisterable.register(keyOf("skylands"), presetSkylands());
        presetRegisterable.register(keyOf("infdev_415"), presetInfdev415());
        presetRegisterable.register(keyOf("infdev_420"), presetInfdev420());
        presetRegisterable.register(keyOf("infdev_611"), presetInfdev611());
        presetRegisterable.register(keyOf("infdev_325"), presetInfdev325());
        presetRegisterable.register(keyOf("infdev_227"), presetInfdev227());
        presetRegisterable.register(keyOf("indev"), presetIndev());
        presetRegisterable.register(keyOf("classic"), presetClassic());
        presetRegisterable.register(keyOf("classic_14a_08"), presetClassic14a08());
        presetRegisterable.register(keyOf("pe"), presetPE());
        presetRegisterable.register(keyOf("beta_1_8_1"), presetBeta181(0));
        presetRegisterable.register(keyOf("beta_1_9_pre_3"), presetBeta19Pre3(0));
        presetRegisterable.register(keyOf("release_1_0_0"), preset100(0));
        presetRegisterable.register(keyOf("release_1_1"), preset11(0));
        presetRegisterable.register(keyOf("release_1_2_5"), preset125(0));
        presetRegisterable.register(keyOf("release_1_6_4"), preset164(0));
        presetRegisterable.register(keyOf("release_1_12_2"), DEFAULT_MAJOR);
        presetRegisterable.register(keyOf("release_1_17_1"), preset1171(0));
        presetRegisterable.register(keyOf("beta_skylands"), presetBetaSkylands());
        presetRegisterable.register(keyOf("beta_isles"), presetIsles(DEFAULT_BETA));
        presetRegisterable.register(keyOf("beta_water_world"), presetWaterWorld(DEFAULT_BETA));
        presetRegisterable.register(keyOf("beta_isle_land"), presetIsleLand(DEFAULT_BETA));
        presetRegisterable.register(keyOf("beta_cave_delight"), presetCaveDelight(DEFAULT_BETA));
        presetRegisterable.register(keyOf("beta_mountain_madness"), presetMountainMadness(DEFAULT_BETA, false));
        presetRegisterable.register(keyOf("beta_drought"), presetDrought(DEFAULT_BETA));
        presetRegisterable.register(keyOf("beta_cave_chaos"), presetCaveChaos(DEFAULT_BETA));
        presetRegisterable.register(keyOf("beta_large_biomes"), presetBetaLargeBiomes());
        presetRegisterable.register(keyOf("beta_xbox_legacy"), presetBetaXboxLegacy());
        presetRegisterable.register(keyOf("beta_survival_island"), presetBetaSurvivalIsland());
        presetRegisterable.register(keyOf("beta_vanilla"), presetBetaVanilla());
        presetRegisterable.register(keyOf("release_hybrid"), presetReleaseHybrid(0));
        presetRegisterable.register(keyOf("snow_aint_snowier"), presetSnowAintSnowier(0));
        presetRegisterable.register(keyOf("alpha_winter"), presetAlphaWinter());
        presetRegisterable.register(keyOf("indev_paradise"), presetIndevParadise());
        presetRegisterable.register(keyOf("indev_woods"), presetIndevWoods());
        presetRegisterable.register(keyOf("indev_hell"), presetIndevHell());
        presetRegisterable.register(keyOf("water_world"), presetWaterWorld(DEFAULT_MAJOR));
        presetRegisterable.register(keyOf("isle_land"), presetIsleLand(DEFAULT_MAJOR));
        presetRegisterable.register(keyOf("cave_delight"), presetCaveDelight(DEFAULT_MAJOR));
        presetRegisterable.register(keyOf("mountain_madness"), presetMountainMadness(DEFAULT_MAJOR, true));
        presetRegisterable.register(keyOf("drought"), presetDrought(DEFAULT_MAJOR));
        presetRegisterable.register(keyOf("cave_chaos"), presetCaveChaos(DEFAULT_MAJOR));
        presetRegisterable.register(keyOf("beta_1_8_1_large_biomes"), presetBeta181(2));
        presetRegisterable.register(keyOf("beta_1_9_pre_3_large_biomes"), presetBeta19Pre3(2));
        presetRegisterable.register(keyOf("release_1_0_0_large_biomes"), preset100(2));
        presetRegisterable.register(keyOf("release_1_1_large_biomes"), preset11(2));
        presetRegisterable.register(keyOf("release_1_2_5_large_biomes"), preset125(2));
        presetRegisterable.register(keyOf("release_1_6_4_large_biomes"), preset164(2));
        presetRegisterable.register(keyOf("release_1_12_2_large_biomes"), preset1122(2));
        presetRegisterable.register(keyOf("release_1_17_1_large_biomes"), preset1171(2));
        presetRegisterable.register(keyOf("release_hybrid_large_biomes"), presetReleaseHybrid(2));
        presetRegisterable.register(keyOf("snow_aint_snowier_large_biomes"), presetSnowAintSnowier(2));
    }

    private static RegistryKey<ModernBetaSettingsPreset> keyOf(String id) {
        return RegistryKey.of(ModernBetaRegistryKeys.SETTINGS_PRESET, ModernerBeta.createId(id));
    }

    private static ModernBetaSettingsPreset presetBeta() {
        return presetBeta(false);
    }

    private static ModernBetaSettingsPreset presetBeta(boolean oakBiomes) {
        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.BETA.id)
                .addDefault(DEEPSLATE_GENERATION, USE_SURFACE_RULES, SEA_LEVEL_OFFSET, CAVE_GENERATION, NOISE_SCALE, NOISE_SLIDE)
                .build(),
            ModernBetaSettings.builder()
                .add(PROVIDER, ModernBetaBuiltInTypes.Biome.BETA.id)
                .add(USE_OCEAN_BIOMES, true)
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.BETA)
                .add(CLIMATE_DISTRIBUTION, ClimateDistribution.BETA)
                .add(CLIMATE_MAPPINGS, Map.ofEntries(
                    Map.entry("desert", new ClimateMapping(
                        ModernBetaBiomes.BETA_DESERT.getValue(),
                        ModernBetaBiomes.BETA_OCEAN.getValue()
                    )),
                    Map.entry("forest", new ClimateMapping(
                        (oakBiomes ? ModernBetaBiomes.BETA_OAK_FOREST : ModernBetaBiomes.BETA_FOREST).getValue(),
                        ModernBetaBiomes.BETA_OCEAN.getValue()
                    )),
                    Map.entry("ice_desert", new ClimateMapping(
                        ModernBetaBiomes.BETA_TUNDRA.getValue(),
                        ModernBetaBiomes.BETA_FROZEN_OCEAN.getValue()
                    )),
                    Map.entry("plains", new ClimateMapping(
                        ModernBetaBiomes.BETA_PLAINS.getValue(),
                        ModernBetaBiomes.BETA_OCEAN.getValue()
                    )),
                    Map.entry("rainforest", new ClimateMapping(
                        ModernBetaBiomes.BETA_RAINFOREST.getValue(),
                        ModernBetaBiomes.BETA_WARM_OCEAN.getValue()
                    )),
                    Map.entry("savanna", new ClimateMapping(
                        ModernBetaBiomes.BETA_SAVANNA.getValue(),
                        ModernBetaBiomes.BETA_OCEAN.getValue()
                    )),
                    Map.entry("shrubland", new ClimateMapping(
                        ModernBetaBiomes.BETA_SHRUBLAND.getValue(),
                        ModernBetaBiomes.BETA_OCEAN.getValue()
                    )),
                    Map.entry("seasonal_forest", new ClimateMapping(
                        ModernBetaBiomes.BETA_SEASONAL_FOREST.getValue(),
                        ModernBetaBiomes.BETA_LUKEWARM_OCEAN.getValue()
                    )),
                    Map.entry("swampland", new ClimateMapping(
                        ModernBetaBiomes.BETA_SWAMPLAND.getValue(),
                        ModernBetaBiomes.BETA_COLD_OCEAN.getValue()
                    )),
                    Map.entry("taiga", new ClimateMapping(
                        (oakBiomes ? ModernBetaBiomes.BETA_OAK_TAIGA : ModernBetaBiomes.BETA_TAIGA).getValue(),
                        ModernBetaBiomes.BETA_FROZEN_OCEAN.getValue()
                    )),
                    Map.entry("tundra", new ClimateMapping(
                        ModernBetaBiomes.BETA_TUNDRA.getValue(),
                        ModernBetaBiomes.BETA_FROZEN_OCEAN.getValue()
                    ))
                ))
                .addDefault(CLIMATE_SCALE)
                .build(),
            ModernBetaSettings.builder()
                .add(PROVIDER, ModernBetaBuiltInTypes.CaveBiome.VORONOI.id)
                .addDefault(CAVE_BIOME_VORONOI)
                .build()
        );
    }

    private static ModernBetaSettingsPreset presetAlpha() {
        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.ALPHA.id)
                .add(DEEPSLATE_GENERATION, DeepslateGeneration.DISABLED)
                .add(CAVE_GENERATION, CaveGeneration.BETA)
                .add(NOISE_SCALE, new NoiseScale(
                    684.412f,
                    684.412f,
                    512f,
                    512f,
                    100f,
                    100f,
                    80f,
                    160f,
                    80f,
                    8.5f,
                    12.0f
                ))
                .addDefault(USE_SURFACE_RULES, SEA_LEVEL_OFFSET, CAVE_GENERATION, NOISE_SLIDE)
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.ALPHA),
            ModernBetaSettings.noCaveBiomes()
        );
    }
    
    private static ModernBetaSettingsPreset presetSkylands() {
        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.SKYLANDS.id)
                .add(DEEPSLATE_GENERATION, DeepslateGeneration.DISABLED)
                .add(NOISE_SCALE, new NoiseScale(
                    1368.824f,
                    684.412f,
                    512f,
                    512f,
                    100f,
                    100f,
                    80f,
                    160f,
                    80f,
                    8.5f,
                    12.0f
                ))
                .add(NOISE_SLIDE, new NoiseSlide(
                    -30,
                    31,
                    0,
                    -30,
                    7,
                    1
                ))
                .addDefault(USE_SURFACE_RULES, CAVE_GENERATION)
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.BETA_SKY),
            ModernBetaSettings.noCaveBiomes()
        );
    }
    
    private static ModernBetaSettingsPreset presetInfdev415() {
        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.INFDEV_415.id)
                .add(DEEPSLATE_GENERATION, DeepslateGeneration.DISABLED)
                .add(CAVE_GENERATION, CaveGeneration.DISABLED)
                .add(NOISE_SCALE, new NoiseScale(
                    684.412f,
                    984.412f,
                    512f,
                    512f,
                    100f,
                    100f,
                    80f,
                    400f,
                    80f,
                    8.5f,
                    12.0f
                ))
                .add(NOISE_SLIDE, NoiseSlide.DISABLED)
                .addDefault(USE_SURFACE_RULES, SEA_LEVEL_OFFSET)
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.INFDEV_415),
            ModernBetaSettings.noCaveBiomes()
        );
    }
    
    private static ModernBetaSettingsPreset presetInfdev420() {
        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.INFDEV_420.id)
                .add(DEEPSLATE_GENERATION, DeepslateGeneration.DISABLED)
                .add(CAVE_GENERATION, CaveGeneration.DISABLED)
                .add(NOISE_SCALE, new NoiseScale(
                    684.412f,
                    684.412f,
                    512f,
                    512f,
                    100f,
                    100f,
                    80f,
                    160f,
                    80f,
                    8.5f,
                    12.0f
                ))
                .add(NOISE_SLIDE, NoiseSlide.DISABLED)
                .addDefault(USE_SURFACE_RULES, SEA_LEVEL_OFFSET)
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.INFDEV_420),
            ModernBetaSettings.noCaveBiomes()
        );
    }
    
    private static ModernBetaSettingsPreset presetInfdev611() {
        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.INFDEV_611.id)
                .add(DEEPSLATE_GENERATION, DeepslateGeneration.DISABLED)
                .add(CAVE_GENERATION, CaveGeneration.BETA)
                .add(NOISE_SCALE, new NoiseScale(
                    684.412f,
                    684.412f,
                    512f,
                    512f,
                    100f,
                    100f,
                    80f,
                    160f,
                    80f,
                    8.5f,
                    12.0f
                ))
                .add(NOISE_SLIDE, NoiseSlide.DISABLED)
                .addDefault(USE_SURFACE_RULES, SEA_LEVEL_OFFSET)
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.INFDEV_611),
            ModernBetaSettings.noCaveBiomes()
        );
    }

    private static ModernBetaSettingsPreset presetInfdev325() {
        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.INFDEV_227.id)
                .add(DEEPSLATE_GENERATION, DeepslateGeneration.DISABLED)
                .add(USE_SURFACE_RULES, false)
                .add(CAVE_GENERATION, CaveGeneration.DISABLED)
                .add(INFDEV_227_STRUCTURES, new Infdev227Structures(true, false))
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.INFDEV_325),
            ModernBetaSettings.noCaveBiomes()
        );
    }
    
    private static ModernBetaSettingsPreset presetInfdev227() {
        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.INFDEV_227.id)
                .add(DEEPSLATE_GENERATION, DeepslateGeneration.DISABLED)
                .add(USE_SURFACE_RULES, false)
                .add(CAVE_GENERATION, CaveGeneration.DISABLED)
                .add(INFDEV_227_STRUCTURES, new Infdev227Structures(true, true))
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.INFDEV_227),
            ModernBetaSettings.noCaveBiomes()
        );
    }

    private static ModernBetaSettingsPreset presetIndev() {
        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.INDEV.id)
                .add(DEEPSLATE_GENERATION, DeepslateGeneration.DISABLED)
                .add(CAVE_GENERATION, CaveGeneration.DISABLED)
                .addDefault(FINITE_LEVEL_PROPERTIES, FINITE_CAVE_GENERATION, FINITE_NOISE, FINITE_BEACHES, FINITE_POOLS)
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.INDEV_NORMAL),
            ModernBetaSettings.noCaveBiomes()
        );
    }
    
    private static ModernBetaSettingsPreset presetClassic() {
        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.CLASSIC_0_30.id)
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
                .addDefault(FINITE_LEVEL_PROPERTIES, FINITE_CAVE_GENERATION, FINITE_NOISE, FINITE_POOLS, SPAWN_INDEV_HOUSE)
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.INDEV_NORMAL),
            ModernBetaSettings.noCaveBiomes()
        );
    }

    private static ModernBetaSettingsPreset presetClassic14a08() {
        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.CLASSIC_0_30.id)
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
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.CLASSIC_14A_08),
            ModernBetaSettings.noCaveBiomes()
        );
    }
    
    private static ModernBetaSettingsPreset presetPE() {
        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.PE.id)
                .add(DEEPSLATE_GENERATION, DeepslateGeneration.DISABLED)
                .build(),
            DEFAULT_BETA.biomeSettings().extend()
                .add(PROVIDER, ModernBetaBuiltInTypes.Biome.PE.id)
                .add(USE_OCEAN_BIOMES, false)
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.BETA)
                .add(CLIMATE_DISTRIBUTION, ClimateDistribution.BETA)
                .add(CLIMATE_MAPPINGS, Map.ofEntries(
                    Map.entry("desert", new ClimateMapping(
                        ModernBetaBiomes.PE_DESERT.getValue(),
                        ModernBetaBiomes.PE_OCEAN.getValue()
                    )),
                    Map.entry("forest", new ClimateMapping(
                        ModernBetaBiomes.PE_FOREST.getValue(),
                        ModernBetaBiomes.PE_OCEAN.getValue()
                    )),
                    Map.entry("ice_desert", new ClimateMapping(
                        ModernBetaBiomes.PE_TUNDRA.getValue(),
                        ModernBetaBiomes.PE_FROZEN_OCEAN.getValue()
                    )),
                    Map.entry("plains", new ClimateMapping(
                        ModernBetaBiomes.PE_PLAINS.getValue(),
                        ModernBetaBiomes.PE_OCEAN.getValue()
                    )),
                    Map.entry("rainforest", new ClimateMapping(
                        ModernBetaBiomes.PE_RAINFOREST.getValue(),
                        ModernBetaBiomes.PE_WARM_OCEAN.getValue()
                    )),
                    Map.entry("savanna", new ClimateMapping(
                        ModernBetaBiomes.PE_SAVANNA.getValue(),
                        ModernBetaBiomes.PE_OCEAN.getValue()
                    )),
                    Map.entry("shrubland", new ClimateMapping(
                        ModernBetaBiomes.PE_SHRUBLAND.getValue(),
                        ModernBetaBiomes.PE_OCEAN.getValue()
                    )),
                    Map.entry("seasonal_forest", new ClimateMapping(
                        ModernBetaBiomes.PE_SEASONAL_FOREST.getValue(),
                        ModernBetaBiomes.PE_LUKEWARM_OCEAN.getValue()
                    )),
                    Map.entry("swampland", new ClimateMapping(
                        ModernBetaBiomes.PE_SWAMPLAND.getValue(),
                        ModernBetaBiomes.PE_COLD_OCEAN.getValue()
                    )),
                    Map.entry("taiga", new ClimateMapping(
                        ModernBetaBiomes.PE_TAIGA.getValue(),
                        ModernBetaBiomes.PE_FROZEN_OCEAN.getValue()
                    )),
                    Map.entry("tundra", new ClimateMapping(
                        ModernBetaBiomes.PE_TUNDRA.getValue(),
                        ModernBetaBiomes.PE_FROZEN_OCEAN.getValue()
                    ))
                ))
                .build(),
            ModernBetaSettings.noCaveBiomes()
        );
    }
    
    private static ModernBetaSettingsPreset presetBetaSkylands() {
        return new ModernBetaSettingsPreset(
            presetSkylands().chunkSettings(),
            DEFAULT_BETA.biomeSettings().extend()
                .add(USE_OCEAN_BIOMES, false)
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }
    
    private static ModernBetaSettingsPreset presetIsles(ModernBetaSettingsPreset initial) {
        return new ModernBetaSettingsPreset(
            initial.chunkSettings().extend()
                .add(ISLES_PROPERTIES, IslesProperties.ENABLED)
                .build(),
            initial.biomeSettings(),
            initial.caveBiomeSettings()
        );
    }

    private static ModernBetaSettingsPreset presetWaterWorld(ModernBetaSettingsPreset initial) {
        return new ModernBetaSettingsPreset(
            initial.chunkSettings().extend()
                .add(SEA_LEVEL_OFFSET, 192)
                .replace(NOISE_SCALE, base -> new NoiseScale(
                    base.coordinate(),
                    base.height(),
                    base.upperLimit(),
                    base.lowerLimit(),
                    base.depthNoiseX(),
                    base.depthNoiseZ(),
                    5000.0f,
                    1000.0f,
                    5000.0f,
                    base.baseSize(),
                    8.0f
                ))
                .replace(FORCED_BIOME_HEIGHT, base -> new ForcedBiomeHeight(
                    base.heightOverrides(),
                    2.0f,
                    0.5f,
                    2.0f,
                    0.375f
                ))
                .build(),
            initial.biomeSettings(),
            initial.caveBiomeSettings()
        );
    }

    private static ModernBetaSettingsPreset presetIsleLand(ModernBetaSettingsPreset initial) {
        return new ModernBetaSettingsPreset(
            initial.chunkSettings().extend()
                .replace(NOISE_SCALE, base -> new NoiseScale(
                    3000.0f,
                    6000.0f,
                    250.0f,
                    512.0f,
                    base.depthNoiseX(),
                    base.depthNoiseZ(),
                    base.mainNoiseX(),
                    base.mainNoiseY(),
                    base.mainNoiseZ(),
                    base.baseSize(),
                    10.0f
                ))
                .build(),
            initial.biomeSettings(),
            initial.caveBiomeSettings()
        );
    }
    

    private static ModernBetaSettingsPreset presetCaveDelight(ModernBetaSettingsPreset initial) {
        return new ModernBetaSettingsPreset(
            initial.chunkSettings().extend()
                .replace(NOISE_SCALE, base -> new NoiseScale(
                    base.coordinate(),
                    base.height(),
                    base.upperLimit(),
                    base.lowerLimit(),
                    base.depthNoiseX(),
                    base.depthNoiseZ(),
                    5000.0f,
                    1000.0f,
                    5000.0f,
                    base.baseSize(),
                    5.0f
                ))
                .replace(FORCED_BIOME_HEIGHT, base -> new ForcedBiomeHeight(
                    base.heightOverrides(),
                    2.0f,
                    1.0f,
                    4.0f,
                    1.0f
                ))
                .build(),
            initial.biomeSettings(),
            initial.caveBiomeSettings()
        );
    }

    private static ModernBetaSettingsPreset presetMountainMadness(ModernBetaSettingsPreset initial, boolean modifyBaseSize) {
        return new ModernBetaSettingsPreset(
            initial.chunkSettings().extend()
                .replace(NOISE_SCALE, base -> new NoiseScale(
                    738.41864f,
                    157.69133f,
                    801.4267f,
                    1254.1643f,
                    374.93652f,
                    288.65228f,
                    1355.9908f,
                    745.5343f,
                    1183.464f,
                    modifyBaseSize ? 1.8758626f : base.baseSize(),
                    1.7137525f
                ))
                .replace(FORCED_BIOME_HEIGHT, base -> new ForcedBiomeHeight(
                    base.heightOverrides(),
                    1.7553768f,
                    3.4701107f,
                    1.0f,
                    2.535211f
                ))
                .build(),
            initial.biomeSettings(),
            initial.caveBiomeSettings()
        );
    }

    private static ModernBetaSettingsPreset presetDrought(ModernBetaSettingsPreset initial) {
        return new ModernBetaSettingsPreset(
            initial.chunkSettings().extend()
                .add(SEA_LEVEL_OFFSET, -43)
                .replace(NOISE_SCALE, base -> new NoiseScale(
                    base.coordinate(),
                    base.height(),
                    base.upperLimit(),
                    base.lowerLimit(),
                    base.depthNoiseX(),
                    base.depthNoiseZ(),
                    1000.0f,
                    3000.0f,
                    1000.0f,
                    base.baseSize(),
                    10.0f
                ))
                .build(),
            initial.biomeSettings(),
            initial.caveBiomeSettings()
        );
    }

    private static ModernBetaSettingsPreset presetCaveChaos(ModernBetaSettingsPreset initial) {
        return new ModernBetaSettingsPreset(
            initial.chunkSettings().extend()
                .add(SEA_LEVEL_OFFSET, -57)
                .replace(NOISE_SCALE, base -> new NoiseScale(
                    base.coordinate(),
                    base.height(),
                    2.0f,
                    64.0f,
                    base.depthNoiseX(),
                    base.depthNoiseZ(),
                    base.mainNoiseX(),
                    base.mainNoiseY(),
                    base.mainNoiseZ(),
                    base.baseSize(),
                    8.0f
                ))
                .build(),
            initial.biomeSettings(),
            initial.caveBiomeSettings()
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
    
    private static ModernBetaSettingsPreset presetBetaXboxLegacy() {
        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(ISLES_PROPERTIES, new IslesProperties(
                    true,
                    false,
                    -200.0f,
                    IslandShape.SQUARE,
                    25,
                    2,
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
    
    private static ModernBetaSettingsPreset presetBetaVanilla() {
        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(USE_SURFACE_RULES, true)
                .add(CAVE_GENERATION, CaveGeneration.MODERN_BETA)
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

                    new VoronoiPointBiome(
                        BiomeKeys.DESERT.getValue(),
                        BiomeKeys.LUKEWARM_OCEAN.getValue(),
                        BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue(),
                        0.9, 0.1, 0.5
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.PLAINS.getValue(),
                        BiomeKeys.LUKEWARM_OCEAN.getValue(),
                        BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue(),
                        0.9, 0.3, 0.5
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.FOREST.getValue(),
                        BiomeKeys.LUKEWARM_OCEAN.getValue(),
                        BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue(),
                        0.9, 0.5, 0.5
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.FOREST.getValue(),
                        BiomeKeys.WARM_OCEAN.getValue(),
                        BiomeKeys.WARM_OCEAN.getValue(),
                        0.9, 0.7, 0.5
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.JUNGLE.getValue(),
                        BiomeKeys.WARM_OCEAN.getValue(),
                        BiomeKeys.WARM_OCEAN.getValue(),
                        0.9, 0.9, 0.5
                    ),

                    new VoronoiPointBiome(
                        BiomeKeys.SAVANNA.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.7, 0.1, 0.5
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.PLAINS.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.7, 0.3, 0.5
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.FOREST.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.7, 0.5, 0.5
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.FOREST.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.7, 0.7, 0.5
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.FOREST.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.7, 0.9, 0.5
                    ),

                    new VoronoiPointBiome(
                        BiomeKeys.PLAINS.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.5, 0.1, 0.5
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.PLAINS.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.5, 0.3, 0.5
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.BIRCH_FOREST.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.5, 0.5, 0.5
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.BIRCH_FOREST.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.5, 0.7, 0.5
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.SWAMP.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.5, 0.9, 0.5
                    ),

                    new VoronoiPointBiome(
                        BiomeKeys.SNOWY_PLAINS.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.3, 0.1, 0.5
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.TAIGA.getValue(),
                        BiomeKeys.COLD_OCEAN.getValue(),
                        BiomeKeys.DEEP_COLD_OCEAN.getValue(),
                        0.3, 0.3, 0.5
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.TAIGA.getValue(),
                        BiomeKeys.COLD_OCEAN.getValue(),
                        BiomeKeys.DEEP_COLD_OCEAN.getValue(),
                        0.3, 0.5, 0.5
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.SNOWY_TAIGA.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.3, 0.7, 0.5
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.SNOWY_TAIGA.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.3, 0.9, 0.5
                    ),

                    new VoronoiPointBiome(
                        BiomeKeys.SNOWY_PLAINS.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.1, 0.1, 0.5
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.SNOWY_PLAINS.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.1, 0.3, 0.5
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.SNOWY_PLAINS.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.1, 0.5, 0.5
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.SNOWY_PLAINS.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.1, 0.7, 0.5
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.SNOWY_PLAINS.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.1, 0.9, 0.5
                    ),

                    // Mutated Biomes

                    new VoronoiPointBiome(
                        BiomeKeys.DESERT.getValue(),
                        BiomeKeys.LUKEWARM_OCEAN.getValue(),
                        BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue(),
                        0.9, 0.1, 0.2
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.SUNFLOWER_PLAINS.getValue(),
                        BiomeKeys.LUKEWARM_OCEAN.getValue(),
                        BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue(),
                        0.9, 0.3, 0.2
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.DARK_FOREST.getValue(),
                        BiomeKeys.LUKEWARM_OCEAN.getValue(),
                        BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue(),
                        0.9, 0.5, 0.2
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.DARK_FOREST.getValue(),
                        BiomeKeys.WARM_OCEAN.getValue(),
                        BiomeKeys.WARM_OCEAN.getValue(),
                        0.9, 0.7, 0.2
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.BAMBOO_JUNGLE.getValue(),
                        BiomeKeys.WARM_OCEAN.getValue(),
                        BiomeKeys.WARM_OCEAN.getValue(),
                        0.9, 0.9, 0.2
                    ),

                    new VoronoiPointBiome(
                        BiomeKeys.SAVANNA.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.7, 0.1, 0.2
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.MEADOW.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.7, 0.3, 0.2
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.FLOWER_FOREST.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.7, 0.5, 0.2
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.FLOWER_FOREST.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.7, 0.7, 0.2
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.FLOWER_FOREST.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.7, 0.9, 0.2
                    ),

                    new VoronoiPointBiome(
                        BiomeKeys.MEADOW.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.5, 0.1, 0.2
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.MEADOW.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.5, 0.3, 0.2
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.CHERRY_GROVE.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.5, 0.5, 0.2
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.CHERRY_GROVE.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.5, 0.7, 0.2
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.MANGROVE_SWAMP.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.5, 0.9, 0.2
                    ),

                    new VoronoiPointBiome(
                        BiomeKeys.SNOWY_PLAINS.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.3, 0.1, 0.2
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.OLD_GROWTH_PINE_TAIGA.getValue(),
                        BiomeKeys.COLD_OCEAN.getValue(),
                        BiomeKeys.DEEP_COLD_OCEAN.getValue(),
                        0.3, 0.3, 0.2
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.OLD_GROWTH_PINE_TAIGA.getValue(),
                        BiomeKeys.COLD_OCEAN.getValue(),
                        BiomeKeys.DEEP_COLD_OCEAN.getValue(),
                        0.3, 0.5, 0.2
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.GROVE.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.3, 0.7, 0.2
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.GROVE.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.3, 0.9, 0.2
                    ),

                    new VoronoiPointBiome(
                        BiomeKeys.SNOWY_PLAINS.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.1, 0.1, 0.2
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.SNOWY_PLAINS.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.1, 0.3, 0.2
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.SNOWY_PLAINS.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.1, 0.5, 0.2
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.SNOWY_SLOPES.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.1, 0.7, 0.2
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.SNOWY_SLOPES.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.1, 0.9, 0.2
                    ),

                    // Mutated Biomes 2

                    new VoronoiPointBiome(
                        BiomeKeys.BADLANDS.getValue(),
                        BiomeKeys.LUKEWARM_OCEAN.getValue(),
                        BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue(),
                        0.9, 0.1, 0.8
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.PLAINS.getValue(),
                        BiomeKeys.LUKEWARM_OCEAN.getValue(),
                        BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue(),
                        0.9, 0.3, 0.8
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.SPARSE_JUNGLE.getValue(),
                        BiomeKeys.LUKEWARM_OCEAN.getValue(),
                        BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue(),
                        0.9, 0.5, 0.8
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.SPARSE_JUNGLE.getValue(),
                        BiomeKeys.WARM_OCEAN.getValue(),
                        BiomeKeys.WARM_OCEAN.getValue(),
                        0.9, 0.7, 0.8
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.MUSHROOM_FIELDS.getValue(),
                        BiomeKeys.WARM_OCEAN.getValue(),
                        BiomeKeys.WARM_OCEAN.getValue(),
                        0.9, 0.9, 0.8
                    ),

                    new VoronoiPointBiome(
                        BiomeKeys.SAVANNA.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.7, 0.1, 0.8
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.PLAINS.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.7, 0.3, 0.8
                    ),
                    //? if >=1.21.4 {
                    new VoronoiPointBiome(
                        BiomeKeys.PALE_GARDEN.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.7, 0.5, 0.8
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.PALE_GARDEN.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.7, 0.7, 0.8
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.PALE_GARDEN.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.7, 0.9, 0.8
                    ),
                    //?}

                    new VoronoiPointBiome(
                        BiomeKeys.PLAINS.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.5, 0.1, 0.8
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.PLAINS.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.5, 0.3, 0.8
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.OLD_GROWTH_BIRCH_FOREST.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.5, 0.5, 0.8
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.OLD_GROWTH_BIRCH_FOREST.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.5, 0.7, 0.8
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.MANGROVE_SWAMP.getValue(),
                        BiomeKeys.OCEAN.getValue(),
                        BiomeKeys.DEEP_OCEAN.getValue(),
                        0.5, 0.9, 0.8
                    ),

                    new VoronoiPointBiome(
                        BiomeKeys.SNOWY_PLAINS.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.3, 0.1, 0.8
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA.getValue(),
                        BiomeKeys.COLD_OCEAN.getValue(),
                        BiomeKeys.DEEP_COLD_OCEAN.getValue(),
                        0.3, 0.3, 0.8
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA.getValue(),
                        BiomeKeys.COLD_OCEAN.getValue(),
                        BiomeKeys.DEEP_COLD_OCEAN.getValue(),
                        0.3, 0.5, 0.8
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.GROVE.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.3, 0.7, 0.8
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.GROVE.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.3, 0.9, 0.8
                    ),

                    new VoronoiPointBiome(
                        BiomeKeys.SNOWY_PLAINS.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.1, 0.1, 0.8
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.SNOWY_PLAINS.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.1, 0.3, 0.8
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.SNOWY_PLAINS.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.1, 0.5, 0.8
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.ICE_SPIKES.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.1, 0.7, 0.8
                    ),
                    new VoronoiPointBiome(
                        BiomeKeys.ICE_SPIKES.getValue(),
                        BiomeKeys.FROZEN_OCEAN.getValue(),
                        BiomeKeys.DEEP_FROZEN_OCEAN.getValue(),
                        0.1, 0.9, 0.8
                    )
                ))
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }
    
    private static ModernBetaSettingsPreset presetAlphaWinter() {
        ModernBetaSettingsPreset basePreset = presetAlpha();
        return new ModernBetaSettingsPreset(
            basePreset.chunkSettings(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.ALPHA_WINTER),
            basePreset.caveBiomeSettings()
        );
    }
    
    private static ModernBetaSettingsPreset presetIndevParadise() {
        ModernBetaSettingsPreset basePreset = presetIndev();
        return new ModernBetaSettingsPreset(
            basePreset.chunkSettings().extend()
                .add(FINITE_LEVEL_PROPERTIES, new FiniteLevelProperties(
                    IndevType.ISLAND,
                    IndevTheme.PARADISE,
                    256,
                    256,
                    128
                ))
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.INDEV_PARADISE),
            basePreset.caveBiomeSettings()
        );
    }
    
    private static ModernBetaSettingsPreset presetIndevWoods() {
        ModernBetaSettingsPreset basePreset = presetIndev();
        return new ModernBetaSettingsPreset(
            basePreset.chunkSettings().extend()
                .add(FINITE_LEVEL_PROPERTIES, new FiniteLevelProperties(
                    IndevType.ISLAND,
                    IndevTheme.WOODS,
                    256,
                    256,
                    128
                ))
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.INDEV_WOODS),
            basePreset.caveBiomeSettings()
        );
    }
    
    private static ModernBetaSettingsPreset presetIndevHell() {
        ModernBetaSettingsPreset basePreset = presetIndev();
        return new ModernBetaSettingsPreset(
            basePreset.chunkSettings().extend()
                .add(FINITE_LEVEL_PROPERTIES, new FiniteLevelProperties(
                    IndevType.ISLAND,
                    IndevTheme.HELL,
                    256,
                    256,
                    128
                ))
                .build(),
            ModernBetaSettings.singleBiome(ModernBetaBiomes.INDEV_HELL),
            basePreset.caveBiomeSettings()
        );
    }

    private static Map<Identifier, String> earlyReleaseLayerOutputs(int biomeScale) {
        ImmutableMap.Builder<Identifier, String> builder = new ImmutableMap.Builder<>();
        builder.put(ModernBetaBuiltInTypes.LayerOutput.BIOME.id, "land");
        for (int i = 0; i < 4 + biomeScale; i++) {
            builder.put(ModernerBeta.createId("climate_" + i), "land_" + i);
        }
        return builder.build();
    }

    private static ModernBetaSettingsPreset presetBeta181(int biomeScale) {
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
            new RandomBiomeLayer("biome_pool", 200, ExtendedBiomeId.listOf(
                "minecraft:desert",
                "minecraft:forest",
                "moderner_beta:late_beta_extreme_hills",
                "moderner_beta:late_beta_swampland",
                "moderner_beta:late_beta_plains",
                "moderner_beta:late_beta_taiga"
            )),
            new BiomeToLayerOverlayLayer("land", 0, "land", Map.of(ExtendedBiomeId.PLAINS, "biome_pool")),
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
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id)
                .add(CAVE_GENERATION, CaveGeneration.EARLY_RELEASE)
                .add(FORCED_BIOME_HEIGHT, ForcedBiomeHeight.overridesOnly(Map.of(
                    ExtendedBiomeId.OCEAN, new HeightConfig(-1.0f, 0.5f)
                )))
                .build(),
            ModernBetaSettings.betaFractalLayers(earlyReleaseLayerOutputs(biomeScale), ClimateDistribution.BETA, layers)
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.NONE)
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }

    private static ConfiguredLayers configuredLayers100Era(int biomeScale, ExtendedBiomeId icePlains) {
        List<Layer> layers = new ArrayList<>(List.of(
            new InitLandLayer("land", 1),
            new FuzzyZoomLayer("land", 2000, "land"),
            AddLandLayer.forIslandScale("land", 1, "land"),
            new ModalZoomLayer("land", 2001, "land"),
            AddLandLayer.forIslandScale("land", 2, "land"),
            new WeightedBiomeLayer("snow", 2, Pool.<ExtendedBiomeId>builder()
                .add(ExtendedBiomeId.SNOWY_PLAINS, 1)
                .add(ExtendedBiomeId.NULL, 4)
                .build()),
            new BiomeToLayerOverlayLayer("land", 0, "land", Map.of(ExtendedBiomeId.PLAINS, "snow")),
            new ModalZoomLayer("land", 2002, "land"),
            AddLandLayer.forIslandScale("land", 3, "land"),
            new ModalZoomLayer("land", 2003, "land"),
            AddLandLayer.forIslandScale("land", 4, "land"),
            new ConditionalBiomeOverlayLayer("land", 5, "land",
                BiomePredicate.of(ExtendedBiomeId.OCEAN)
                    .and(BiomePredicate.diagonalInterior())
                    .and(BiomePredicate.oneIn(100)),
                ExtendedBiomeId.MUSHROOM_ISLAND, ExtendedBiomeId.NULL
            ),
            new InitRiverLayer("river", 100, "land"),
            StackedZoomLayer.modal("river", 1000, "river", 6 + biomeScale),
            new ComputeRiverLayer("river", 0, "river", true),
            new SmoothLayer("river", 1000, "river"),
            new RandomBiomeLayer("biome_pool", 200, ExtendedBiomeId.listOf(
                "minecraft:desert",
                "minecraft:forest",
                "moderner_beta:late_beta_extreme_hills",
                "moderner_beta:early_release_swampland",
                "moderner_beta:late_beta_plains",
                "moderner_beta:late_beta_taiga"
            )),
            new ConstantBiomeLayer("ice_plains", 0, icePlains),
            new BiomeToLayerOverlayLayer("land", 0, "land", Map.of(
                ExtendedBiomeId.PLAINS, "biome_pool",
                ExtendedBiomeId.FROZEN_OCEAN, "ice_plains",
                ExtendedBiomeId.SNOWY_PLAINS, "ice_plains"
            )),
            StackedZoomLayer.modal("land", 1000, "land", 2),
            new ModalZoomLayer("land_0", 1000, "land"),
            AddLandLayer.forEarlyRelease("land_0", 3, "land_0", icePlains),
            new ConditionalBiomeOverlayLayer("land_0", 0, "land_0",
                PredicateOverlayLayer.Target.MUSHROOM_SHORE.predicate(),
                ExtendedBiomeId.MUSHROOM_SHORE, ExtendedBiomeId.NULL)
        ));
        for (int i = 0; i < 3 + biomeScale; i++) {
            layers.add(new ModalZoomLayer("land_" + (1 + i), 1001 + i, "land_" + i));
        }
        layers.add(new SmoothLayer("land", 1000, "land_" + (3 + biomeScale)));
        layers.add(MixRiverLayer.forEarlyRelease("land", 0, "land", "river"));

        return new ConfiguredLayers(layers, earlyReleaseLayerOutputs(biomeScale));
    }

    private static ModernBetaSettingsPreset presetBeta19Pre3(int biomeScale) {
        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id)
                .add(CAVE_GENERATION, CaveGeneration.EARLY_RELEASE)
                .addDefault(FORCED_BIOME_HEIGHT)
                .build(),
            ModernBetaSettings.betaFractalLayers(configuredLayers100Era(biomeScale, ExtendedBiomeId.of(ModernBetaBiomes.LATE_BETA_ICE_PLAINS)), ClimateDistribution.RELEASE_1_0)
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.NONE)
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }

    private static ModernBetaSettingsPreset preset100(int biomeScale) {
        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id)
                .add(CAVE_GENERATION, CaveGeneration.EARLY_RELEASE)
                .addDefault(FORCED_BIOME_HEIGHT)
                .build(),
            ModernBetaSettings.betaFractalLayers(configuredLayers100Era(biomeScale, ExtendedBiomeId.of(ModernBetaBiomes.EARLY_RELEASE_ICE_PLAINS)), ClimateDistribution.RELEASE_1_0)
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.NONE)
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }

    private static ConfiguredLayers configuredLayers11Era(int biomeScale, boolean addJungles, boolean taigasInIcePlains) {
        ExtendedBiomeId icePlains = ExtendedBiomeId.of(ModernBetaBiomes.EARLY_RELEASE_ICE_PLAINS);

        List<ExtendedBiomeId> biomePool = ExtendedBiomeId.listOf(
            "minecraft:desert",
            "minecraft:forest",
            "moderner_beta:early_release_extreme_hills",
            "moderner_beta:early_release_swampland",
            "moderner_beta:late_beta_plains",
            "moderner_beta:early_release_taiga"
        );
        if (addJungles) {
            biomePool = new ArrayList<>(biomePool);
            biomePool.add(ExtendedBiomeId.of(BiomeKeys.JUNGLE));
        }

        Map<ExtendedBiomeId, ExtendedBiomeId> hillsVariants = Map.ofEntries(
            ExtendedBiomeId.of("minecraft:desert").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:forest").mapTo("*hills"),
            ExtendedBiomeId.of("moderner_beta:late_beta_plains").mapTo("minecraft:forest"),
            ExtendedBiomeId.of("moderner_beta:early_release_taiga").mapTo("*hills"),
            ExtendedBiomeId.of("moderner_beta:early_release_ice_plains").mapTo("*hills")
        );
        if (addJungles) {
            hillsVariants = new HashMap<>(hillsVariants);
            hillsVariants.put(
                ExtendedBiomeId.of("minecraft:jungle"),
                ExtendedBiomeId.of("minecraft:jungle*hills")
            );
        }

        Layer swampLakesLayer = new WeightedBiomeLayer("swamp_lakes", 1000, Pool.<ExtendedBiomeId>builder()
            .add(ExtendedBiomeId.RIVER, 1)
            .add(ExtendedBiomeId.NULL, 5)
            .build());
        List<PredicateOverlayLayer.Target> lakeOverlays = List.of(
            PredicateOverlayLayer.Target.layer(
                BiomePredicate.of(ExtendedBiomeId.of("~moderner_beta:early_release_swampland")),
                "swamp_lakes"
            )
        );
        if (addJungles) {
            lakeOverlays = new ArrayList<>(lakeOverlays);
            lakeOverlays.add(
                PredicateOverlayLayer.Target.layer(
                    BiomePredicate.of(ExtendedBiomeId.of("~minecraft:jungle")),
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
            new WeightedBiomeLayer("snow", 2, Pool.<ExtendedBiomeId>builder()
                .add(ExtendedBiomeId.SNOWY_PLAINS, 1)
                .add(ExtendedBiomeId.NULL, 4)
                .build()),
            new BiomeToLayerOverlayLayer("land", 0, "land", Map.of(ExtendedBiomeId.PLAINS, "snow")),
            new ModalZoomLayer("land", 2002, "land"),
            AddLandLayer.forIslandScale("land", 3, "land"),
            new ModalZoomLayer("land", 2003, "land"),
            AddLandLayer.forIslandScale("land", 4, "land"),
            new ConditionalBiomeOverlayLayer("land", 5, "land",
                BiomePredicate.of(ExtendedBiomeId.OCEAN)
                    .and(BiomePredicate.diagonalInterior())
                    .and(BiomePredicate.oneIn(100)),
                ExtendedBiomeId.MUSHROOM_ISLAND, ExtendedBiomeId.NULL
            ),
            new InitRiverLayer("river", 100, "land"),
            StackedZoomLayer.modal("river", 1000, "river", 6 + biomeScale),
            new ComputeRiverLayer("river", 0, "river", true),
            new SmoothLayer("river", 1000, "river"),
            new RandomBiomeLayer("biome_pool", 200, biomePool),
            icePlainsLayer,
            new BiomeToLayerOverlayLayer("land", 0, "land", Map.of(
                ExtendedBiomeId.PLAINS, "biome_pool",
                ExtendedBiomeId.FROZEN_OCEAN, "ice_plains",
                ExtendedBiomeId.SNOWY_PLAINS, "ice_plains"
            )),
            StackedZoomLayer.modal("land", 1000, "land", 2),
            new SimpleBiomeReplacementLayer("hills", 0, "land", hillsVariants),
            new ConditionalLayerOverlayLayer("land", 1000, "land",
                BiomePredicate.simpleHills(hillsVariants.keySet()), "hills", "land"),
            new ModalZoomLayer("land_0", 1000, "land"),
            AddLandLayer.forEarlyRelease("land_0", 3, "land_0", icePlains),
            new ModalZoomLayer("land_1", 1001, "land_0"),
            new PredicateOverlayLayer("land_1", 0, "land_1", List.of(
                PredicateOverlayLayer.Target.MUSHROOM_SHORE,
                PredicateOverlayLayer.Target.inclusiveBeach(
                    ExtendedBiomeId.setOf(
                        "minecraft:ocean",
                        "minecraft:river",
                        "moderner_beta:early_release_extreme_hills",
                        "moderner_beta:early_release_swampland"
                    ),
                    ExtendedBiomeId.BEACH
                ),
                PredicateOverlayLayer.Target.biome(
                    BiomePredicate.of(ExtendedBiomeId.of("moderner_beta:early_release_extreme_hills"))
                        .and(BiomePredicate.border()),
                    ExtendedBiomeId.of("moderner_beta:early_release_extreme_hills*edge")
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
            Layer jungleLakesLayer = new WeightedBiomeLayer("jungle_lakes", 1000, Pool.<ExtendedBiomeId>builder()
                .add(ExtendedBiomeId.RIVER, 1)
                .add(ExtendedBiomeId.NULL, 7)
                .build());

            layers = new ArrayList<>(layers);
            layers.add(layers.lastIndexOf(swampLakesLayer), jungleLakesLayer);
        }

        return new ConfiguredLayers(layers, earlyReleaseLayerOutputs(biomeScale));
    }

    private static ModernBetaSettingsPreset preset11(int biomeScale) {
        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id)
                .add(CAVE_GENERATION, CaveGeneration.EARLY_RELEASE)
                .addDefault(FORCED_BIOME_HEIGHT)
                .build(),
            ModernBetaSettings.betaFractalLayers(configuredLayers11Era(biomeScale, false, false), ClimateDistribution.RELEASE_1_1)
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.NONE)
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }

    private static ModernBetaSettingsPreset preset125(int biomeScale) {
        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id)
                .add(CAVE_GENERATION, CaveGeneration.EARLY_RELEASE)
                .addDefault(FORCED_BIOME_HEIGHT)
                .build(),
            ModernBetaSettings.fractalLayers(configuredLayers11Era(biomeScale, true, false))
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.NONE)
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }

    private static ModernBetaSettingsPreset preset164(int biomeScale) {
        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id)
                .add(CAVE_GENERATION, CaveGeneration.EARLY_RELEASE)
                .add(FORCED_BIOME_HEIGHT, ForcedBiomeHeight.overridesOnly(Map.of(
                    ExtendedBiomeId.of("minecraft:desert*hills"), new HeightConfig(0.3f, 0.8f),
                    ExtendedBiomeId.of("minecraft:forest*hills"), new HeightConfig(0.3f, 0.7f),
                    ExtendedBiomeId.of("moderner_beta:early_release_extreme_hills"), new HeightConfig(0.3f, 1.5f),
                    ExtendedBiomeId.of("moderner_beta:early_release_ice_plains*hills"), new HeightConfig(0.3f, 1.3f),
                    ExtendedBiomeId.of("minecraft:jungle*hills"), new HeightConfig(1.8f, 0.5f),
                    ExtendedBiomeId.of("moderner_beta:early_release_taiga*hills"), new HeightConfig(0.3f, 0.8f)
                )))
                .build(),
            ModernBetaSettings.fractalLayers(configuredLayers11Era(biomeScale, true, true))
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.NONE)
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }

    private static ConfiguredLayers configuredLayers1710Era(int biomeScale, boolean saltedMutation, boolean climaticOceans, boolean bambooJungles, boolean modernBiomes) {
        Set<ExtendedBiomeId> oceans = ExtendedBiomeId.setOf("minecraft:ocean", "minecraft:deep_ocean");
        BiomePredicate oceansPredicate = BiomePredicate.inSet(oceans);

        var hillVariants = Map.ofEntries(
            ExtendedBiomeId.of("minecraft:desert").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:forest").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:birch_forest").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:taiga").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:snowy_taiga").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:snowy_plains").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:jungle").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:bamboo_jungle").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:plains").mapTo("minecraft:forest"),
            ExtendedBiomeId.of("minecraft:windswept_hills").mapTo("minecraft:windswept_forest"),
            ExtendedBiomeId.of("minecraft:dark_forest").mapTo("minecraft:plains"),
            ExtendedBiomeId.of("minecraft:old_growth_pine_taiga").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:old_growth_spruce_taiga").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:savanna").mapTo("minecraft:savanna_plateau"),
            ExtendedBiomeId.of("minecraft:badlands*plateau").mapTo("minecraft:badlands"),
            ExtendedBiomeId.of("minecraft:wooded_badlands").mapTo("minecraft:badlands")
        );
        var mutatedVariants = Map.ofEntries(
            ExtendedBiomeId.of("minecraft:plains").mapTo("minecraft:sunflower_plains"),
            ExtendedBiomeId.of("minecraft:desert").mapTo("*lakes"),
            ExtendedBiomeId.of("minecraft:forest").mapTo("minecraft:flower_forest"),
            ExtendedBiomeId.of("minecraft:taiga").mapTo("*mountains"),
            ExtendedBiomeId.of("minecraft:swamp").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:mangrove_swamp").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:jungle").mapTo("*modified"),
            ExtendedBiomeId.of("minecraft:sparse_jungle").mapTo("*modified"),
            ExtendedBiomeId.of("minecraft:snowy_taiga").mapTo("*mountains"),
            ExtendedBiomeId.of("minecraft:snowy_plains").mapTo("minecraft:ice_spikes"),
            ExtendedBiomeId.of("minecraft:savanna").mapTo("minecraft:windswept_savanna"),
            ExtendedBiomeId.of("minecraft:savanna_plateau").mapTo("minecraft:windswept_savanna*plateau"),
            ExtendedBiomeId.of("minecraft:badlands").mapTo("minecraft:eroded_badlands"),
            ExtendedBiomeId.of("minecraft:wooded_badlands").mapTo("*modified"),
            ExtendedBiomeId.of("minecraft:badlands*plateau").mapTo("*modified_plateau"),
            ExtendedBiomeId.of("minecraft:birch_forest").mapTo("minecraft:old_growth_birch_forest"),
            ExtendedBiomeId.of("minecraft:birch_forest*hills").mapTo("minecraft:old_growth_birch_forest*hills"),
            ExtendedBiomeId.of("minecraft:dark_forest").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:old_growth_pine_taiga").mapTo("minecraft:old_growth_spruce_taiga"),
            ExtendedBiomeId.of("minecraft:windswept_hills").mapTo("minecraft:windswept_gravelly_hills"),
            ExtendedBiomeId.of("minecraft:windswept_forest").mapTo("minecraft:windswept_gravelly_hills")
        );
        var modernVariants = Map.ofEntries(
            //? if >=1.21.4 {
            ExtendedBiomeId.of("minecraft:dark_forest").mapTo("minecraft:pale_garden"),
            //?}
            ExtendedBiomeId.of("minecraft:windswept_hills").mapTo("minecraft:meadow"),
            ExtendedBiomeId.of("minecraft:taiga").mapTo("minecraft:cherry_grove"),
            ExtendedBiomeId.of("minecraft:swamp").mapTo("minecraft:mangrove_swamp")
        );

        Map<String, Set<ExtendedBiomeId>> biomeCategories = Map.ofEntries(
            Map.entry("beach", ExtendedBiomeId.setOf(
                "~minecraft:beach",
                "~minecraft:snowy_beach"
            )),
            Map.entry("desert", ExtendedBiomeId.setOf(
                "~minecraft:desert"
            )),
            Map.entry("windswept_hills", ExtendedBiomeId.setOf(
                "~minecraft:windswept_gravelly_hills",
                "~minecraft:windswept_hills",
                "~minecraft:windswept_forest",
                "~minecraft:meadow"
            )),
            Map.entry("forest", ExtendedBiomeId.setOf(
                "~minecraft:birch_forest",
                "~minecraft:dark_forest",
                "~minecraft:flower_forest",
                "~minecraft:forest",
                "~minecraft:old_growth_birch_forest",
                "~minecraft:pale_garden",
                "~minecraft:cherry_grove"
            )),
            Map.entry("snowy_plains", ExtendedBiomeId.setOf(
                "~minecraft:ice_spikes",
                "~minecraft:snowy_plains"
            )),
            Map.entry("jungle", ExtendedBiomeId.setOf(
                "~minecraft:bamboo_jungle",
                "~minecraft:jungle",
                "~minecraft:sparse_jungle"
            )),
            Map.entry("badlands", ExtendedBiomeId.setOf(
                "minecraft:badlands",
                "minecraft:eroded_badlands",
                "minecraft:badlands*modified_plateau",
                "minecraft:wooded_badlands*modified"
            )),
            Map.entry("badlands_plateau", ExtendedBiomeId.setOf(
                "minecraft:badlands*plateau",
                "minecraft:wooded_badlands"
            )),
            Map.entry("badlands_all", ExtendedBiomeId.setOf(
                "~minecraft:badlands",
                "~minecraft:eroded_badlands",
                "~minecraft:wooded_badlands"
            )),
            Map.entry("mushroom_fields", ExtendedBiomeId.setOf(
                "~minecraft:mushroom_fields"
            )),
            Map.entry("ocean", oceans),
            Map.entry("plains", ExtendedBiomeId.setOf(
                "~minecraft:plains",
                "~minecraft:sunflower_plains"
            )),
            Map.entry("river", ExtendedBiomeId.setOf(
                "~minecraft:frozen_river",
                "~minecraft:river"
            )),
            Map.entry("savanna", ExtendedBiomeId.setOf(
                "~minecraft:savanna",
                "~minecraft:savanna_plateau",
                "~minecraft:windswept_savanna"
            )),
            Map.entry("swamp", ExtendedBiomeId.setOf(
                "~minecraft:swamp",
                "~minecraft:mangrove_swamp"
            )),
            Map.entry("taiga", ExtendedBiomeId.setOf(
                "~minecraft:old_growth_spurce_taiga",
                "~minecraft:old_growth_pine_taiga",
                "~minecraft:snowy_taiga",
                "~minecraft:taiga"
            )),
            Map.entry("jungle_like", ExtendedBiomeId.setOf(
                "~minecraft:bamboo_jungle",
                "~minecraft:jungle",
                "~minecraft:sparse_jungle",
                "minecraft:forest",
                "minecraft:taiga",
                "minecraft:ocean",
                "minecraft:deep_ocean"
            )),
            Map.entry("snowy", ExtendedBiomeId.setOf(
                "~minecraft:ice_spikes",
                "~minecraft:snowy_plains",
                "~minecraft:snowy_taiga"
            ))
        );
        List<Set<ExtendedBiomeId>> hillyCategories = List.of(
            biomeCategories.get("ocean"),
            biomeCategories.get("forest"),
            biomeCategories.get("taiga"),
            biomeCategories.get("plains"),
            biomeCategories.get("snowy_plains"),
            biomeCategories.get("savanna"),
            biomeCategories.get("desert"),
            biomeCategories.get("windswept_hills"),
            biomeCategories.get("jungle"),
            biomeCategories.get("badlands_all")
        );

        BiomePredicate hillPredicate;
//        hillPredicates.add(BiomePredicate.inSet(biomeCategories.get("badlands_plateau"))
//            .and(BiomePredicate.neighborsMatch(
//                BiomePredicate.inSet(biomeCategories.get("badlands_all")), 3)));

        Set<ExtendedBiomeId> hillTargetBiomeSet = hillyCategories.stream()
            .flatMap(Set::stream)
            .collect(Collectors.toSet());
        hillPredicate = BiomePredicate.inSet(hillTargetBiomeSet)
            .and(BiomePredicate.neighborsMatch(hillyCategories, 3));

        List<Layer> layers = Stream.of(
            new InitLandLayer("land", 1),
            new FuzzyZoomLayer("land", 2000, "land"),
            AddLandLayer.forIslandScaleMajor("land", 1, "land"),
            new ModalZoomLayer("land", 2001, "land"),
            AddLandLayer.forIslandScaleMajor("land", 2, "land"),
            AddLandLayer.forIslandScaleMajor("land", 50, "land"),
            AddLandLayer.forIslandScaleMajor("land", 70, "land"),
            // RemoveTooMuchOcean
            new ConditionalBiomeOverlayLayer("land", 2, "land",
                BiomePredicate.of(ExtendedBiomeId.OCEAN)
                    .and(BiomePredicate.interior())
                    .and(BiomePredicate.oneIn(2)),
                ExtendedBiomeId.PLAINS, ExtendedBiomeId.NULL
            ),
            // region AddSnowLayer
            new WeightedBiomeLayer("climate", 2, Pool.<ExtendedBiomeId>builder()
                .add(ExtendedBiomeId.CLIMATE_SNOWY, 1)
                .add(ExtendedBiomeId.CLIMATE_COOL, 1)
                .add(ExtendedBiomeId.CLIMATE_WARM, 4)
                .build()),
            new BiomeToLayerOverlayLayer("land", 0, "land", Map.of(ExtendedBiomeId.PLAINS, "climate")),
            // endregion AddSnowLayer
            AddLandLayer.forIslandScaleMajor("land", 3, "land"),
            // AddEdgeLayer.CoolWarm
            new ConditionalBiomeOverlayLayer("land", 0, "land",
                BiomePredicate.of(ExtendedBiomeId.CLIMATE_WARM)
                    .and(BiomePredicate.neighborsMatch(
                        BiomePredicate.inSet(
                            ExtendedBiomeId.CLIMATE_COOL,
                            ExtendedBiomeId.CLIMATE_SNOWY
                        ), 1
                    )),
                ExtendedBiomeId.CLIMATE_TEMPERATE, ExtendedBiomeId.NULL
            ),
            // AddEdgeLayer.HeatIce
            new ConditionalBiomeOverlayLayer("land", 0, "land",
                BiomePredicate.of(ExtendedBiomeId.CLIMATE_SNOWY)
                    .and(BiomePredicate.neighborsMatch(
                        BiomePredicate.inSet(
                            ExtendedBiomeId.CLIMATE_TEMPERATE,
                            ExtendedBiomeId.CLIMATE_WARM
                        ), 1
                    )),
                ExtendedBiomeId.CLIMATE_COOL, ExtendedBiomeId.NULL
            ),
            // region AddEdgeLayer.Special
            new RandomBiomeLayer("climate_warm_rare", 3, ExtendedBiomeId.CLIMATE_WARM_RARE).skipRandom(1),
            new RandomBiomeLayer("climate_temperate_rare", 3, ExtendedBiomeId.CLIMATE_TEMPERATE_RARE).skipRandom(1),
            new RandomBiomeLayer("climate_cool_rare", 3, ExtendedBiomeId.CLIMATE_COOL_RARE).skipRandom(1),
            new RandomBiomeLayer("climate_snowy_rare", 3, ExtendedBiomeId.CLIMATE_SNOWY_RARE).skipRandom(1),
            new BiomeToLayerOverlayLayer("rare_climates", 0, "land", Map.ofEntries(
                Map.entry(ExtendedBiomeId.CLIMATE_WARM, "climate_warm_rare"),
                Map.entry(ExtendedBiomeId.CLIMATE_TEMPERATE, "climate_temperate_rare"),
                Map.entry(ExtendedBiomeId.CLIMATE_COOL, "climate_cool_rare"),
                Map.entry(ExtendedBiomeId.CLIMATE_SNOWY, "climate_snowy_rare")
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
            new ConditionalBiomeOverlayLayer("land", 5, "land",
                BiomePredicate.of(ExtendedBiomeId.OCEAN)
                    .and(BiomePredicate.diagonalInterior())
                    .and(BiomePredicate.oneIn(100)),
                ExtendedBiomeId.MUSHROOM_ISLAND, ExtendedBiomeId.NULL
            ),
            new ConditionalBiomeOverlayLayer("land", 0, "land",
                BiomePredicate.of(ExtendedBiomeId.OCEAN)
                    .and(BiomePredicate.interior()),
                ExtendedBiomeId.DEEP_OCEAN, ExtendedBiomeId.NULL
            ),
            new SupplyRandomLayer("mutation", 100, 299999),
            new ConditionalLayerOverlayLayer("mutation", 0, "land", BiomePredicate.of(ExtendedBiomeId.OCEAN), "land", "mutation"),
            StackedZoomLayer.modal("river", 1000, "mutation", 2),
            StackedZoomLayer.modal("river", 1000, "river", 4 + biomeScale),
            saltedMutation
                ? StackedZoomLayer.modal("mutation", 1000, "mutation", 2)
                : StackedZoomLayer.modal("mutation", 1000, "mutation", 2).unsalted(),
            new ComputeRiverLayer("river", 0, "river", false),
            new SmoothLayer("river", 1000, "river"),
            // region BiomeInitLayer
            new RandomBiomeLayer("biome_pool_warm", 200, ExtendedBiomeId.listOf(
                "minecraft:desert",
                "minecraft:desert",
                "minecraft:desert",
                "minecraft:savanna",
                "minecraft:savanna",
                "minecraft:plains"
            )),
            new RandomBiomeLayer("biome_pool_warm_rare", 200, ExtendedBiomeId.listOf(
                "minecraft:badlands*plateau",
                "minecraft:wooded_badlands",
                "minecraft:wooded_badlands"
            )),
            new RandomBiomeLayer("biome_pool_temperate", 200, ExtendedBiomeId.listOf(
                "minecraft:forest",
                "minecraft:dark_forest",
                "minecraft:windswept_hills",
                "minecraft:plains",
                "minecraft:birch_forest",
                "minecraft:swamp"
            )),
            new ConstantBiomeLayer("biome_pool_temperate_rare", 200, ExtendedBiomeId.of("minecraft:jungle")),
            new RandomBiomeLayer("biome_pool_cool", 200, ExtendedBiomeId.listOf(
                "minecraft:forest",
                "minecraft:windswept_hills",
                "minecraft:taiga",
                "minecraft:plains"
            )),
            new ConstantBiomeLayer("biome_pool_cool_rare", 200, ExtendedBiomeId.of("minecraft:old_growth_pine_taiga")),
            new RandomBiomeLayer("biome_pool_snowy", 200, ExtendedBiomeId.listOf(
                "minecraft:snowy_plains",
                "minecraft:snowy_plains",
                "minecraft:snowy_plains",
                "minecraft:snowy_taiga"
            )),
            new BiomeToLayerOverlayLayer("land", 0, "land", Map.of(
                ExtendedBiomeId.CLIMATE_WARM, "biome_pool_warm",
                ExtendedBiomeId.CLIMATE_WARM_RARE.get(0).asWeak(), "biome_pool_warm_rare",
                ExtendedBiomeId.CLIMATE_TEMPERATE, "biome_pool_temperate",
                ExtendedBiomeId.CLIMATE_TEMPERATE_RARE.get(0).asWeak(), "biome_pool_temperate_rare",
                ExtendedBiomeId.CLIMATE_COOL, "biome_pool_cool",
                ExtendedBiomeId.CLIMATE_COOL_RARE.get(0).asWeak(), "biome_pool_cool_rare",
                ExtendedBiomeId.CLIMATE_SNOWY, "biome_pool_snowy",
                ExtendedBiomeId.CLIMATE_SNOWY_RARE.get(0).asWeak(), "biome_pool_snowy"
            )),
            // endregion BiomeInitLayer
            modernBiomes ? new SimpleBiomeReplacementLayer("modern_land", 3000, "land", modernVariants) : null,
            modernBiomes ? new ConditionalLayerOverlayLayer("land", 1003, "land",
                BiomePredicate.oneIn(5), "modern_land", "land") : null,
            bambooJungles ? new ConditionalBiomeOverlayLayer("land", 1001, "land",
                BiomePredicate.of(ExtendedBiomeId.of("minecraft:jungle"))
                    .and(BiomePredicate.oneIn(10)),
                ExtendedBiomeId.of("minecraft:bamboo_jungle"), ExtendedBiomeId.NULL
            ) : null,
            StackedZoomLayer.modal("land", 1000, "land", 2),
            // BiomeTransitionLayer
            new PredicateOverlayLayer("land", 0, "land", Stream.of(
                // Mountain edge has been omitted because it ends up just not generating at all
                PredicateOverlayLayer.Target.borderTransition(
                    ExtendedBiomeId.of("minecraft:wooded_badlands"),
                    biomeCategories.get("badlands_all"),
                    ExtendedBiomeId.of("minecraft:badlands")
                ),
                PredicateOverlayLayer.Target.borderTransition(
                    ExtendedBiomeId.of("minecraft:badlands*plateau"),
                    biomeCategories.get("badlands_all"),
                    ExtendedBiomeId.of("minecraft:badlands")
                ),
                PredicateOverlayLayer.Target.borderTransition(
                    ExtendedBiomeId.of("minecraft:old_growth_pine_taiga"),
                    biomeCategories.get("taiga"),
                    ExtendedBiomeId.of("minecraft:taiga")
                ),
                PredicateOverlayLayer.Target.biome(
                    BiomePredicate.of(ExtendedBiomeId.of("minecraft:desert"))
                        .and(BiomePredicate.neighborsMatch(ExtendedBiomeId.of("minecraft:snowy_plains"), 1)),
                    ExtendedBiomeId.of("minecraft:windswept_forest")
                ),
                PredicateOverlayLayer.Target.biome(
                    BiomePredicate.of(ExtendedBiomeId.of("minecraft:swamp"))
                        .and(BiomePredicate.neighborsMatch(ExtendedBiomeId.of("minecraft:jungle"), 1)),
                    ExtendedBiomeId.of("minecraft:sparse_jungle")
                ),
                PredicateOverlayLayer.Target.biome(
                    BiomePredicate.of(ExtendedBiomeId.of("minecraft:swamp"))
                        .and(BiomePredicate.neighborsMatch(ExtendedBiomeId.of("minecraft:jungle"), 1)),
                    ExtendedBiomeId.of("minecraft:sparse_jungle")
                ),
                modernBiomes ? PredicateOverlayLayer.Target.biome(
                    BiomePredicate.of(ExtendedBiomeId.of("minecraft:pale_garden"))
                        .and(BiomePredicate.border()),
                    ExtendedBiomeId.of("minecraft:dark_forest*hills")
                ) : null
            ).filter(Objects::nonNull).toList()),
            // region RegionHillsLayer
            new SimpleBiomeReplacementLayer("hills", 0, "land", hillVariants),
            new RandomBiomeLayer("deep_ocean_islands", 1000, ExtendedBiomeId.listOf(
                "minecraft:plains",
                "minecraft:forest"
            )).skipRandom(2),
            new PredicateOverlayLayer("hills", 1000, "hills", List.of(
                PredicateOverlayLayer.Target.biome(
                    BiomePredicate.of(ExtendedBiomeId.of("minecraft:forest"))
                        .and(BiomePredicate.oneIn(1))
                        .and(BiomePredicate.oneIn(3)),
                    ExtendedBiomeId.of("minecraft:forest*hills")
                ),
                PredicateOverlayLayer.Target.layer(
                    BiomePredicate.of(ExtendedBiomeId.of("minecraft:deep_ocean"))
                        .and(BiomePredicate.oneIn(1))
                        .and(BiomePredicate.oneIn(3)),
                    "deep_ocean_islands"
                ),
                PredicateOverlayLayer.Target.biome(
                    BiomePredicate.of(ExtendedBiomeId.of("minecraft:ocean")),
                    ExtendedBiomeId.of("minecraft:deep_ocean")
                )
            )),
            new ConditionalLayerOverlayLayer("hills", 1000, "mutation", BiomePredicate.oneIn(3).or(BiomePredicate.wrappedIntMatch(29, 0)), "hills", "land"),
            new SimpleBiomeReplacementLayer("mutated_hills", 0, "hills", mutatedVariants),
            new ConditionalLayerOverlayLayer("hills", 0, "mutation", BiomePredicate.wrappedIntMatch(29, 0), "mutated_hills", "hills"),
            new ConditionalLayerOverlayLayer("land_with_hills", 0, "land", hillPredicate, "hills", "land"),
            new SimpleBiomeReplacementLayer("mutated_land", 0, "land", mutatedVariants),
            new ConditionalLayerOverlayLayer("mutated_land", 0, "mutation", BiomePredicate.wrappedIntMatch(29, 1), "mutated_land", "land_with_hills"),
            new ConditionalLayerOverlayLayer("land", 0, "land", BiomePredicate.of(ExtendedBiomeId.OCEAN).invert(), "mutated_land", "land_with_hills"),
            // endregion RegionHillsLayer
            new ConditionalBiomeOverlayLayer("land", 1001, "land",
                BiomePredicate.of(ExtendedBiomeId.PLAINS)
                    .and(BiomePredicate.oneIn(57)),
                ExtendedBiomeId.of("sunflower_plains"), ExtendedBiomeId.NULL),
            new ModalZoomLayer("land", 1000, "land"),
            AddLandLayer.forMajorRelease("land", 3, "land"),
            new ModalZoomLayer("land", 1001, "land"),
            // ShoreLayer
            new PredicateOverlayLayer("land", 0, "land", List.of(
                PredicateOverlayLayer.Target.MUSHROOM_SHORE,
                PredicateOverlayLayer.Target.biome(
                    BiomePredicate.inSet(biomeCategories.get("jungle"))
                        .and(BiomePredicate.neighborsMatch(
                            BiomePredicate.inSet(biomeCategories.get("jungle_like")).invert(), 1)),
                    ExtendedBiomeId.of("minecraft:sparse_jungle")
                ),
                PredicateOverlayLayer.Target.exclusiveBeach(
                    modernBiomes
                        ? Stream.concat(
                            biomeCategories.get("windswept_hills").stream(),
                            Stream.of(ExtendedBiomeId.of("minecraft:cherry_grove"))
                        ).collect(Collectors.toSet())
                        : biomeCategories.get("windswept_hills"),
                    oceansPredicate,
                    ExtendedBiomeId.of("minecraft:stony_shore")
                ),
                PredicateOverlayLayer.Target.exclusiveBeach(
                    biomeCategories.get("snowy"),
                    oceansPredicate,
                    ExtendedBiomeId.of("minecraft:snowy_beach")
                ),
                PredicateOverlayLayer.Target.biome(
                    BiomePredicate.inSet(ExtendedBiomeId.setOf(
                        "minecraft:badlands",
                        "minecraft:wooded_badlands"
                    ))
                        .and(BiomePredicate.neighborsMatch(oceansPredicate, 1).invert())
                        .and(BiomePredicate.neighborsMatch(
                            BiomePredicate.inSet(biomeCategories.get("badlands_all")).invert(), 1)),
                    ExtendedBiomeId.of("minecraft:desert")
                ),
                PredicateOverlayLayer.Target.inclusiveBeach(
                    ExtendedBiomeId.setOf(
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
                    ExtendedBiomeId.BEACH
                ),
                PredicateOverlayLayer.Target.biome(
                    BiomePredicate.of(ExtendedBiomeId.of("moderner_beta:early_release_extreme_hills"))
                        .and(BiomePredicate.border()),
                    ExtendedBiomeId.of("moderner_beta:early_release_extreme_hills*edge")
                )
            )),
            StackedZoomLayer.modal("land", 1002, "land", 2 + biomeScale),
            new SmoothLayer("land", 1000, "land"),
            MixRiverLayer.forMajorRelease("land", 0, "land", "river"),
            climaticOceans ? new MappedNoiseLayer("ocean_climate", 2, List.of(
                new MappedNoiseLayer.Entry(0.4, ExtendedBiomeId.WARM_OCEAN),
                new MappedNoiseLayer.Entry(0.2, ExtendedBiomeId.LUKEWARM_OCEAN),
                new MappedNoiseLayer.Entry(0.0, ExtendedBiomeId.OCEAN),
                new MappedNoiseLayer.Entry(-0.2, ExtendedBiomeId.COLD_OCEAN),
                new MappedNoiseLayer.Entry(-0.4, ExtendedBiomeId.FROZEN_OCEAN)
            ), 8.0, false) : null,
            climaticOceans ? StackedZoomLayer.modal("ocean_climate", 2001, "ocean_climate", 6) : null,
            climaticOceans ? new ApplyOceanClimateLayer("land", 0, "land", "ocean_climate") : null
        ).filter(Objects::nonNull).toList();

        return new ConfiguredLayers(layers, Map.of(ModernBetaBuiltInTypes.LayerOutput.BIOME.id, "land"));
    }

    private static ModernBetaSettingsPreset preset1122(int biomeScale) {
        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.MAJOR_RELEASE.id)
                .add(USE_SURFACE_RULES, true)
                .add(CAVE_GENERATION, CaveGeneration.MAJOR_RELEASE)
                .add(FORCED_BIOME_HEIGHT, ForcedBiomeHeight.overridesOnly(HeightConfig.MAJOR_RELEASE_CONFIGS))
                .build(),
            ModernBetaSettings.fractalLayers(configuredLayers1710Era(biomeScale, false, false, false, false))
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.MAJOR_RELEASE)
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }

    private static ModernBetaSettingsPreset preset1171(int biomeScale) {
        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.MAJOR_RELEASE.id)
                .add(USE_SURFACE_RULES, true)
                .add(CAVE_GENERATION, CaveGeneration.MAJOR_RELEASE)
                .add(FORCED_BIOME_HEIGHT, ForcedBiomeHeight.overridesOnly(HeightConfig.MAJOR_RELEASE_CONFIGS))
                .build(),
            ModernBetaSettings.fractalLayers(configuredLayers1710Era(biomeScale, true, true, true, false))
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.MAJOR_RELEASE)
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }

    private static ModernBetaSettingsPreset presetSnowAintSnowier(int biomeScale) {
        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.MAJOR_RELEASE.id)
                .add(USE_SURFACE_RULES, true)
                .add(CAVE_GENERATION, CaveGeneration.MAJOR_RELEASE)
                .add(FORCED_BIOME_HEIGHT, ForcedBiomeHeight.overridesOnly(HeightConfig.MAJOR_RELEASE_CONFIGS))
                .build(),
            ModernBetaSettings.fractalLayers(configuredLayers1710Era(biomeScale, true, true, true, true))
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.MAJOR_RELEASE)
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }

    private static ModernBetaSettingsPreset presetReleaseHybrid(int biomeScale) {
        Map<ExtendedBiomeId, ExtendedBiomeId> hillsVariants = Map.ofEntries(
            ExtendedBiomeId.of("minecraft:desert").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:forest").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:windswept_hills").mapTo("minecraft:windswept_forest"),
            ExtendedBiomeId.of("minecraft:swamp").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:plains").mapTo("minecraft:forest"),
            ExtendedBiomeId.of("minecraft:taiga").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:jungle").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:snowy_taiga").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:snowy_plains").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:savanna").mapTo("minecraft:savanna_plateau"),
            ExtendedBiomeId.of("minecraft:dark_forest").mapTo("minecraft:plains"),
            ExtendedBiomeId.of("minecraft:birch_forest").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:old_growth_birch_forest").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:wooded_badlands").mapTo("minecraft:badlands"),
            ExtendedBiomeId.of("minecraft:mangrove_swamp").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:flower_forest").mapTo("*hills"),
            ExtendedBiomeId.of("minecraft:sparse_jungle").mapTo("minecraft:jungle"),
            ExtendedBiomeId.of("minecraft:badlands*plateau").mapTo("minecraft:badlands")
        );
        Map<ExtendedBiomeId, ExtendedBiomeId> mutatedVariants = Map.ofEntries(
            ExtendedBiomeId.of("minecraft:plains").mapTo("minecraft:sunflower_plains"),
            ExtendedBiomeId.of("minecraft:forest").mapTo("minecraft:flower_forest"),
            ExtendedBiomeId.of("minecraft:forest*hills").mapTo("minecraft:flower_forest*hills"),
            ExtendedBiomeId.of("minecraft:swamp").mapTo("minecraft:swamp*hills"),
            ExtendedBiomeId.of("minecraft:savanna").mapTo("minecraft:windswept_savanna"),
            ExtendedBiomeId.of("minecraft:savanna_plateau").mapTo("minecraft:windswept_savanna*plateau"),
            ExtendedBiomeId.of("minecraft:badlands*plateau").mapTo("minecraft:wooded_badlands"),
            ExtendedBiomeId.of("minecraft:birch_forest").mapTo("minecraft:old_growth_birch_forest"),
            ExtendedBiomeId.of("minecraft:birch_forest*hills").mapTo("minecraft:old_growth_birch_forest*hills"),
            ExtendedBiomeId.of("minecraft:dark_forest").mapTo("minecraft:pale_garden"),
            ExtendedBiomeId.of("minecraft:dark_forest*hills").mapTo("minecraft:pale_garden*hills"),
            ExtendedBiomeId.of("minecraft:old_growth_pine_taiga").mapTo("minecraft:old_growth_spruce_taiga"),
            ExtendedBiomeId.of("minecraft:old_growth_pine_taiga*hills").mapTo("minecraft:old_growth_spruce_taiga*hills"),
            ExtendedBiomeId.of("minecraft:windswept_hills").mapTo("minecraft:windswept_gravelly_hills"),
            ExtendedBiomeId.of("minecraft:windswept_forest").mapTo("minecraft:windswept_gravelly_hills"),
            ExtendedBiomeId.of("minecraft:snowy_plains").mapTo("minecraft:ice_spikes")
        );

        return new ModernBetaSettingsPreset(
            DEFAULT_BETA.chunkSettings().extend()
                .add(PROVIDER, ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id)
                .add(USE_SURFACE_RULES, true)
                .add(CAVE_GENERATION, CaveGeneration.EARLY_RELEASE)
                .add(FORCED_BIOME_HEIGHT, ForcedBiomeHeight.overridesOnly(
                    Map.ofEntries(
                        Map.entry(ExtendedBiomeId.of("minecraft:desert*hills"), new HeightConfig(0.3f, 0.8f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:forest*hills"), new HeightConfig(0.3f, 0.7f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:taiga*hills"), new HeightConfig(0.3f, 0.8f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:dark_forest*hills"), new HeightConfig(0.3f, 0.7f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:pale_garden*hills"), new HeightConfig(0.3f, 0.7f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:birch_forest*hills"), new HeightConfig(0.3f, 0.7f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:old_growth_birch_forest"), new HeightConfig(0.1f, 0.8f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:old_growth_birch_forest*hills"), new HeightConfig(0.3f, 1.3f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:flower_forest"), new HeightConfig(0.1f, 0.8f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:flower_forest*hills"), new HeightConfig(0.3f, 1.3f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:old_growth_spruce_taiga*hills"), new HeightConfig(0.3f, 0.8f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:snowy_taiga*hills"), new HeightConfig(0.3f, 0.8f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:snowy_plains*hills"), new HeightConfig(0.3f, 1.3f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:jungle*hills"), new HeightConfig(1.8f, 0.5f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:badlands*plateau"), new HeightConfig(1.8f, 0.2f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:wooded_badlands"), new HeightConfig(1.8f, 0.2f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:cherry_grove"), new HeightConfig(1.8f, 0.5f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:cherry_grove*edge"), new HeightConfig(0.8f, 0.3f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:windswept_hills"), new HeightConfig(0.3f, 1.5f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:windswept_forest"), new HeightConfig(0.3f, 1.5f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:windswept_gravelly_hills"), new HeightConfig(0.3f, 1.5f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:meadow"), new HeightConfig(1.0f, 1.0f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:stony_shore"), new HeightConfig(0.1f, 1.6f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:ice_spikes"), new HeightConfig(0.3f, 0.8f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:windswept_savanna"), new HeightConfig(0.3f, 1.5f)),
                        Map.entry(ExtendedBiomeId.of("minecraft:windswept_savanna*plateau"), new HeightConfig(1.0f, 1.0f))
                    )
                ))
                .build(),
            ModernBetaSettings.fractalLayers(
                Map.of(ModernBetaBuiltInTypes.LayerOutput.BIOME.id, "land"),
                new InitLandLayer("land", 1),
                new FuzzyZoomLayer("land", 2000, "land"),
                AddLandLayer.forIslandScale("land", 1, "land"),
                new ModalZoomLayer("land", 2001, "land"),
                AddLandLayer.forIslandScale("land", 2, "land"),
                new WeightedBiomeLayer("snow", 2, Pool.<ExtendedBiomeId>builder()
                    .add(ExtendedBiomeId.SNOWY_PLAINS, 1)
                    .add(ExtendedBiomeId.NULL, 4)
                    .build()),
                new BiomeToLayerOverlayLayer("land", 0, "land", Map.of(ExtendedBiomeId.PLAINS, "snow")),
                new ModalZoomLayer("land", 2002, "land"),
                AddLandLayer.forIslandScale("land", 3, "land"),
                new ModalZoomLayer("land", 2003, "land"),
                AddLandLayer.forIslandScale("land", 4, "land"),
                new ConditionalBiomeOverlayLayer("land", 5, "land",
                    BiomePredicate.of(ExtendedBiomeId.OCEAN)
                        .and(BiomePredicate.diagonalInterior())
                        .and(BiomePredicate.oneIn(100)),
                    ExtendedBiomeId.MUSHROOM_ISLAND, ExtendedBiomeId.NULL
                ),
                new InitRiverLayer("river", 100, "land"),
                StackedZoomLayer.modal("river", 1000, "river", 6 + biomeScale),
                new ComputeRiverLayer("river", 0, "river", true),
                new SmoothLayer("river", 1000, "river"),
                new RandomBiomeLayer("biome_pool", 200, ExtendedBiomeId.listOf(
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
                new RandomBiomeLayer("snowy_biome_pool", 200, ExtendedBiomeId.listOf(
                    "minecraft:snowy_plains",
                    "minecraft:snowy_plains",
                    "minecraft:snowy_plains",
                    "minecraft:snowy_taiga"
                )),
                new BiomeToLayerOverlayLayer("land", 0, "land", Map.of(
                    ExtendedBiomeId.PLAINS, "biome_pool",
                    ExtendedBiomeId.FROZEN_OCEAN, "snowy_biome_pool",
                    ExtendedBiomeId.SNOWY_PLAINS, "snowy_biome_pool"
                )),
                StackedZoomLayer.modal("land", 1000, "land", 2),
                new SimpleBiomeReplacementLayer("hills", 0, "land", hillsVariants),
                new ConditionalLayerOverlayLayer("land", 1000, "land",
                    BiomePredicate.inSet(hillsVariants.keySet())
                        .and(BiomePredicate.identicalNeighbors(3, false))
                        .and(BiomePredicate.oneIn(3)), "hills", "land"),
                new SimpleBiomeReplacementLayer("mutated_land", 0, "land", mutatedVariants),
                new MappedNoiseLayer("mutation", 7, List.of(
                    new MappedNoiseLayer.Entry(-0.2, ExtendedBiomeId.of("minecraft:the_void*mutation")),
                    new MappedNoiseLayer.Entry(0.0, ExtendedBiomeId.NULL)
                ), 2, true),
                StackedZoomLayer.modal("mutation", 2005, "mutation", 2),
                new ConditionalLayerOverlayLayer("land", 1000, "mutation",
                    BiomePredicate.of(ExtendedBiomeId.of("minecraft:the_void*mutation")), "mutated_land", "land"),
                new ModalZoomLayer("land", 1000, "land"),
                AddLandLayer.forEarlyRelease("land", 3, "land", ExtendedBiomeId.of(BiomeKeys.SNOWY_PLAINS)),
                new ModalZoomLayer("land", 1001, "land"),
                new PredicateOverlayLayer("land", 0, "land", List.of(
                    PredicateOverlayLayer.Target.MUSHROOM_SHORE,
                    PredicateOverlayLayer.Target.exclusiveBeach(
                        ExtendedBiomeId.setOf(
                            "minecraft:meadow",
                            "minecraft:cherry_grove"
                        ),
                        ExtendedBiomeId.of("minecraft:stony_shore")
                    ),
                    PredicateOverlayLayer.Target.biome(
                        BiomePredicate.of(ExtendedBiomeId.of("minecraft:cherry_grove"))
                            .and(BiomePredicate.border()),
                        ExtendedBiomeId.of("minecraft:cherry_grove*edge")
                    ),
                    PredicateOverlayLayer.Target.biome(
                        BiomePredicate.inSet(
                            ExtendedBiomeId.of("minecraft:badlands*plateau"),
                            ExtendedBiomeId.of("minecraft:wooded_badlands")
                        )
                            .and(BiomePredicate.neighborsMatch(
                                BiomePredicate.inSet(
                                    ExtendedBiomeId.of("~minecraft:badlands"),
                                    ExtendedBiomeId.of("~minecraft:wooded_badlands"),
                                    ExtendedBiomeId.of("~minecraft:eroded_badlands")
                                ), 4).invert()),
                        ExtendedBiomeId.of("minecraft:badlands")
                    ),
                    PredicateOverlayLayer.Target.inclusiveBeach(
                        ExtendedBiomeId.setOf(
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
                        ExtendedBiomeId.BEACH
                    )
                )),
                StackedZoomLayer.modal("land", 1002, "land", 2 + biomeScale),
                new SmoothLayer("land", 1000, "land"),
                MixRiverLayer.forEarlyRelease("land", 0, "land", "river"),
                new MappedNoiseLayer("ocean_climate", 2, List.of(
                    new MappedNoiseLayer.Entry(0.4, ExtendedBiomeId.WARM_OCEAN),
                    new MappedNoiseLayer.Entry(0.2, ExtendedBiomeId.LUKEWARM_OCEAN),
                    new MappedNoiseLayer.Entry(0.0, ExtendedBiomeId.OCEAN),
                    new MappedNoiseLayer.Entry(-0.2, ExtendedBiomeId.COLD_OCEAN),
                    new MappedNoiseLayer.Entry(-0.4, ExtendedBiomeId.FROZEN_OCEAN)
                ), 8.0, false),
                StackedZoomLayer.modal("ocean_climate", 2001, "ocean_climate", 6),
                new ApplyOceanClimateLayer("land", 0, "land", "ocean_climate")
            )
                .add(TEMPERATURE_HEIGHT_SCALING, TemperatureHeightScaling.MAJOR_RELEASE)
                .build(),
            DEFAULT_BETA.caveBiomeSettings()
        );
    }
}