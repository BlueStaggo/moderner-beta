package mod.bluestaggo.modernerbeta.settings;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.world.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomes;
import mod.bluestaggo.modernerbeta.world.biome.provider.climate.ClimateMapping;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ConfiguredLayers;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.*;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates.BiomePredicate;
import mod.bluestaggo.modernerbeta.world.biome.voronoi.VoronoiPointBiome;
import mod.bluestaggo.modernerbeta.world.biome.voronoi.VoronoiPointCaveBiome;
import mod.bluestaggo.modernerbeta.world.chunk.provider.indev.IndevTheme;
import mod.bluestaggo.modernerbeta.world.chunk.provider.indev.IndevType;
import mod.bluestaggo.modernerbeta.world.chunk.provider.island.IslandShape;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.collection.Weighted;
import net.minecraft.world.biome.BiomeKeys;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModernBetaSettingsPresets {
    public static final ModernBetaSettingsPreset PRESET_BETA_1_7_3 = presetBeta(false);
    public static final ModernBetaSettingsPreset PRESET_BETA_1_1_02 = presetBeta(true);
    public static final ModernBetaSettingsPreset PRESET_ALPHA = presetAlpha();
    public static final ModernBetaSettingsPreset PRESET_SKYLANDS = presetSkylands();
    public static final ModernBetaSettingsPreset PRESET_INFDEV_415 = presetInfdev415();
    public static final ModernBetaSettingsPreset PRESET_INFDEV_420 = presetInfdev420();
    public static final ModernBetaSettingsPreset PRESET_INFDEV_611 = presetInfdev611();
    public static final ModernBetaSettingsPreset PRESET_INFDEV_325 = presetInfdev325();
    public static final ModernBetaSettingsPreset PRESET_INFDEV_227 = presetInfdev227();
    public static final ModernBetaSettingsPreset PRESET_INDEV = presetIndev();
    public static final ModernBetaSettingsPreset PRESET_CLASSIC = presetClassic();
    public static final ModernBetaSettingsPreset PRESET_CLASSIC_14A_08 = presetClassic14a08();
    public static final ModernBetaSettingsPreset PRESET_PE = presetPE();
    public static final ModernBetaSettingsPreset PRESET_BETA_1_8_1 = presetBeta181(0);
    public static final ModernBetaSettingsPreset PRESET_BETA_1_9_PRE_3 = presetBeta19Pre3(0);
    public static final ModernBetaSettingsPreset PRESET_RELEASE_1_0_0 = preset100(0);
    public static final ModernBetaSettingsPreset PRESET_RELEASE_1_1 = preset11(0);
    public static final ModernBetaSettingsPreset PRESET_RELEASE_1_2_5 = preset125(0);
    public static final ModernBetaSettingsPreset PRESET_RELEASE_1_6_4 = preset164(0);
    public static final ModernBetaSettingsPreset PRESET_RELEASE_1_12_2 = preset1122(0);
    public static final ModernBetaSettingsPreset PRESET_RELEASE_1_17_1 = preset1171(0);
    public static final ModernBetaSettingsPreset PRESET_BETA_SKYLANDS = presetBetaSkylands();
    public static final ModernBetaSettingsPreset PRESET_BETA_ISLES = presetIsles(PRESET_BETA_1_7_3);
    public static final ModernBetaSettingsPreset PRESET_BETA_WATER_WORLD = presetWaterWorld(PRESET_BETA_1_7_3);
    public static final ModernBetaSettingsPreset PRESET_BETA_ISLE_LAND = presetIsleLand(PRESET_BETA_1_7_3);
    public static final ModernBetaSettingsPreset PRESET_BETA_CAVE_DELIGHT = presetCaveDelight(PRESET_BETA_1_7_3);
    public static final ModernBetaSettingsPreset PRESET_BETA_MOUNTAIN_MADNESS = presetMountainMadness(PRESET_BETA_1_7_3);
    public static final ModernBetaSettingsPreset PRESET_BETA_DROUGHT = presetDrought(PRESET_BETA_1_7_3);
    public static final ModernBetaSettingsPreset PRESET_BETA_CAVE_CHAOS = presetCaveChaos(PRESET_BETA_1_7_3);
    public static final ModernBetaSettingsPreset PRESET_BETA_LARGE_BIOMES = presetBetaLargeBiomes();
    public static final ModernBetaSettingsPreset PRESET_BETA_XBOX_LEGACY = presetBetaXboxLegacy();
    public static final ModernBetaSettingsPreset PRESET_BETA_SURVIVAL_ISLAND = presetBetaSurvivalIsland();
    public static final ModernBetaSettingsPreset PRESET_BETA_VANILLA = presetBetaVanilla();
    public static final ModernBetaSettingsPreset PRESET_RELEASE_HYBRID = presetReleaseHybrid(0);
    public static final ModernBetaSettingsPreset PRESET_SNOW_AINT_SNOWIER = presetSnowAintSnowier(0);
    public static final ModernBetaSettingsPreset PRESET_ALPHA_WINTER = presetAlphaWinter();
    public static final ModernBetaSettingsPreset PRESET_INDEV_PARADISE = presetIndevParadise();
    public static final ModernBetaSettingsPreset PRESET_INDEV_WOODS = presetIndevWoods();
    public static final ModernBetaSettingsPreset PRESET_INDEV_HELL = presetIndevHell();
    public static final ModernBetaSettingsPreset PRESET_WATER_WORLD = presetWaterWorld(PRESET_RELEASE_1_12_2);
    public static final ModernBetaSettingsPreset PRESET_ISLE_LAND = presetIsleLand(PRESET_RELEASE_1_12_2);
    public static final ModernBetaSettingsPreset PRESET_CAVE_DELIGHT = presetCaveDelight(PRESET_RELEASE_1_12_2);
    public static final ModernBetaSettingsPreset PRESET_MOUNTAIN_MADNESS = presetMountainMadness(PRESET_RELEASE_1_12_2);
    public static final ModernBetaSettingsPreset PRESET_DROUGHT = presetDrought(PRESET_RELEASE_1_12_2);
    public static final ModernBetaSettingsPreset PRESET_CAVE_CHAOS = presetCaveChaos(PRESET_RELEASE_1_12_2);
    public static final ModernBetaSettingsPreset PRESET_BETA_1_8_1_LARGE_BIOMES = presetBeta181(2);
    public static final ModernBetaSettingsPreset PRESET_BETA_1_9_PRE_3_LARGE_BIOMES = presetBeta19Pre3(2);
    public static final ModernBetaSettingsPreset PRESET_RELEASE_1_0_0_LARGE_BIOMES = preset100(2);
    public static final ModernBetaSettingsPreset PRESET_RELEASE_1_1_LARGE_BIOMES = preset11(2);
    public static final ModernBetaSettingsPreset PRESET_RELEASE_1_2_5_LARGE_BIOMES = preset125(2);
    public static final ModernBetaSettingsPreset PRESET_RELEASE_1_6_4_LARGE_BIOMES = preset164(2);
    public static final ModernBetaSettingsPreset PRESET_RELEASE_1_12_2_LARGE_BIOMES = preset1122(2);
    public static final ModernBetaSettingsPreset PRESET_RELEASE_1_17_1_LARGE_BIOMES = preset1171(2);
    public static final ModernBetaSettingsPreset PRESET_RELEASE_HYBRID_LARGE_BIOMES = presetSnowAintSnowier(2);
    public static final ModernBetaSettingsPreset PRESET_SNOW_AINT_SNOWIER_LARGE_BIOMES = presetSnowAintSnowier(2);

    private static ModernBetaSettingsPreset presetBeta() {
        return presetBeta(false);
    }

    private static ModernBetaSettingsPreset presetBeta(boolean oakBiomes) {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.BETA.id;
        settingsChunk.useDeepslate = true;
        settingsChunk.deepslateMinY = 0;
        settingsChunk.deepslateMaxY = 8;
        settingsChunk.deepslateBlock = "minecraft:deepslate";
        settingsChunk.noiseCoordinateScale = 684.412f;
        settingsChunk.noiseHeightScale = 684.412f;
        settingsChunk.noiseUpperLimitScale = 512f;
        settingsChunk.noiseLowerLimitScale = 512f;
        settingsChunk.noiseDepthNoiseScaleX = 200;
        settingsChunk.noiseDepthNoiseScaleZ = 200;
        settingsChunk.noiseMainNoiseScaleX = 80f;
        settingsChunk.noiseMainNoiseScaleY = 160f;
        settingsChunk.noiseMainNoiseScaleZ = 80f;
        settingsChunk.noiseBaseSize = 8.5f;
        settingsChunk.noiseStretchY = 12.0f;
        settingsChunk.noiseTopSlideTarget = -10;
        settingsChunk.noiseTopSlideSize = 3;
        settingsChunk.noiseTopSlideOffset = 0;
        settingsChunk.noiseBottomSlideTarget = 15;
        settingsChunk.noiseBottomSlideSize = 3;
        settingsChunk.noiseBottomSlideOffset = 0;
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.BETA.id;
        settingsBiome.climateTempNoiseScale = 0.025f;
        settingsBiome.climateRainNoiseScale = 0.05f;
        settingsBiome.climateDetailNoiseScale = 0.25f;
        settingsBiome.climateMappings = ModernBetaSettingsBiome.Builder.createClimateMapping(
            new ClimateMapping(
                ModernBetaBiomes.BETA_DESERT.getValue().toString(),
                ModernBetaBiomes.BETA_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                (oakBiomes ? ModernBetaBiomes.BETA_OAK_FOREST : ModernBetaBiomes.BETA_FOREST).getValue().toString(),
                ModernBetaBiomes.BETA_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.BETA_TUNDRA.getValue().toString(),
                ModernBetaBiomes.BETA_FROZEN_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.BETA_PLAINS.getValue().toString(),
                ModernBetaBiomes.BETA_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.BETA_RAINFOREST.getValue().toString(),
                ModernBetaBiomes.BETA_WARM_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.BETA_SAVANNA.getValue().toString(),
                ModernBetaBiomes.BETA_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.BETA_SHRUBLAND.getValue().toString(),
                ModernBetaBiomes.BETA_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.BETA_SEASONAL_FOREST.getValue().toString(),
                ModernBetaBiomes.BETA_LUKEWARM_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.BETA_SWAMPLAND.getValue().toString(),
                ModernBetaBiomes.BETA_COLD_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                (oakBiomes ? ModernBetaBiomes.BETA_OAK_TAIGA : ModernBetaBiomes.BETA_TAIGA).getValue().toString(),
                ModernBetaBiomes.BETA_FROZEN_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.BETA_TUNDRA.getValue().toString(),
                ModernBetaBiomes.BETA_FROZEN_OCEAN.getValue().toString()
            )
        );
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.VORONOI.id;
        settingsCaveBiome.voronoiHorizontalNoiseScale = 32.0f;
        settingsCaveBiome.voronoiVerticalNoiseScale = 16.0f;
        settingsCaveBiome.voronoiDepthMinY = -64;
        settingsCaveBiome.voronoiDepthMaxY = 64;
        settingsCaveBiome.voronoiPoints = List.of(
            new VoronoiPointCaveBiome("", 0.0, 0.5, 0.75),
            new VoronoiPointCaveBiome("minecraft:lush_caves", 0.1, 0.5, 0.75),
            new VoronoiPointCaveBiome("", 0.5, 0.5, 0.75),
            new VoronoiPointCaveBiome("minecraft:dripstone_caves", 0.9, 0.5, 0.75),
            new VoronoiPointCaveBiome("", 1.0, 0.5, 0.75),

            new VoronoiPointCaveBiome("", 0.0, 0.5, 0.25),
            new VoronoiPointCaveBiome("minecraft:lush_caves", 0.2, 0.5, 0.25),
            new VoronoiPointCaveBiome("", 0.4, 0.5, 0.25),
            new VoronoiPointCaveBiome("minecraft:deep_dark", 0.5, 0.5, 0.25),
            new VoronoiPointCaveBiome("", 0.6, 0.5, 0.25),
            new VoronoiPointCaveBiome("minecraft:dripstone_caves", 0.8, 0.5, 0.25),
            new VoronoiPointCaveBiome("", 1.0, 0.5, 0.25)
        );
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }

    private static ModernBetaSettingsPreset presetAlpha() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.ALPHA.id;
        settingsChunk.useDeepslate = false;
        settingsChunk.noiseCoordinateScale = 684.412f;
        settingsChunk.noiseHeightScale = 684.412f;
        settingsChunk.noiseUpperLimitScale = 512f;
        settingsChunk.noiseLowerLimitScale = 512f;
        settingsChunk.noiseDepthNoiseScaleX = 100;
        settingsChunk.noiseDepthNoiseScaleZ = 100;
        settingsChunk.noiseMainNoiseScaleX = 80f;
        settingsChunk.noiseMainNoiseScaleY = 160f;
        settingsChunk.noiseMainNoiseScaleZ = 80f;
        settingsChunk.noiseBaseSize = 8.5f;
        settingsChunk.noiseStretchY = 12.0f;
        settingsChunk.noiseTopSlideTarget = -10;
        settingsChunk.noiseTopSlideSize = 3;
        settingsChunk.noiseTopSlideOffset = 0;
        settingsChunk.noiseBottomSlideTarget = 15;
        settingsChunk.noiseBottomSlideSize = 3;
        settingsChunk.noiseBottomSlideOffset = 0;
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
        settingsBiome.singleBiome = ModernBetaBiomes.ALPHA.getValue().toString();
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetSkylands() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.SKYLANDS.id;
        settingsChunk.useDeepslate = false;
        settingsChunk.noiseCoordinateScale = 1368.824f;
        settingsChunk.noiseHeightScale = 684.412f;
        settingsChunk.noiseUpperLimitScale = 512f;
        settingsChunk.noiseLowerLimitScale = 512f;
        settingsChunk.noiseDepthNoiseScaleX = 100;
        settingsChunk.noiseDepthNoiseScaleZ = 100;
        settingsChunk.noiseMainNoiseScaleX = 80f;
        settingsChunk.noiseMainNoiseScaleY = 160f;
        settingsChunk.noiseMainNoiseScaleZ = 80f;
        settingsChunk.noiseBaseSize = 8.5f;
        settingsChunk.noiseStretchY = 12.0f;
        settingsChunk.noiseTopSlideTarget = -30;
        settingsChunk.noiseTopSlideSize = 31;
        settingsChunk.noiseTopSlideOffset = 0;
        settingsChunk.noiseBottomSlideTarget = -30;
        settingsChunk.noiseBottomSlideSize = 7;
        settingsChunk.noiseBottomSlideOffset = 1;
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
        settingsBiome.singleBiome = ModernBetaBiomes.BETA_SKY.getValue().toString();
        settingsBiome.useOceanBiomes = false;
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
         );
    }
    
    private static ModernBetaSettingsPreset presetInfdev415() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.INFDEV_415.id;
        settingsChunk.useDeepslate = false;
        settingsChunk.useCaves = false;
        settingsChunk.noiseCoordinateScale = 684.412f;
        settingsChunk.noiseHeightScale = 984.412f;
        settingsChunk.noiseUpperLimitScale = 512f;
        settingsChunk.noiseLowerLimitScale = 512f;
        settingsChunk.noiseMainNoiseScaleX = 80f;
        settingsChunk.noiseMainNoiseScaleY = 400f;
        settingsChunk.noiseMainNoiseScaleZ = 80f;
        settingsChunk.noiseTopSlideTarget = 0;
        settingsChunk.noiseTopSlideSize = 0;
        settingsChunk.noiseTopSlideOffset = 0;
        settingsChunk.noiseBottomSlideTarget = 0;
        settingsChunk.noiseBottomSlideSize = 0;
        settingsChunk.noiseBottomSlideOffset = 0;
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
        settingsBiome.singleBiome = ModernBetaBiomes.INFDEV_415.getValue().toString();
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
         );
    }
    
    private static ModernBetaSettingsPreset presetInfdev420() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.INFDEV_420.id;
        settingsChunk.useDeepslate = false;
        settingsChunk.noiseCoordinateScale = 684.412f;
        settingsChunk.noiseHeightScale = 684.412f;
        settingsChunk.noiseUpperLimitScale = 512f;
        settingsChunk.noiseLowerLimitScale = 512f;
        settingsChunk.noiseMainNoiseScaleX = 80f;
        settingsChunk.noiseMainNoiseScaleY = 160f;
        settingsChunk.noiseMainNoiseScaleZ = 80f;
        settingsChunk.noiseBaseSize = 8.5f;
        settingsChunk.noiseStretchY = 12.0f;
        settingsChunk.noiseTopSlideTarget = 0;
        settingsChunk.noiseTopSlideSize = 0;
        settingsChunk.noiseTopSlideOffset = 0;
        settingsChunk.noiseBottomSlideTarget = 0;
        settingsChunk.noiseBottomSlideSize = 0;
        settingsChunk.noiseBottomSlideOffset = 0;
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
        settingsBiome.singleBiome = ModernBetaBiomes.INFDEV_420.getValue().toString();
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
         );
    }
    
    private static ModernBetaSettingsPreset presetInfdev611() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.INFDEV_611.id;
        settingsChunk.useDeepslate = false;
        settingsChunk.noiseCoordinateScale = 684.412f;
        settingsChunk.noiseHeightScale = 684.412f;
        settingsChunk.noiseUpperLimitScale = 512f;
        settingsChunk.noiseLowerLimitScale = 512f;
        settingsChunk.noiseDepthNoiseScaleX = 100;
        settingsChunk.noiseDepthNoiseScaleZ = 100;
        settingsChunk.noiseMainNoiseScaleX = 80f;
        settingsChunk.noiseMainNoiseScaleY = 160f;
        settingsChunk.noiseMainNoiseScaleZ = 80f;
        settingsChunk.noiseBaseSize = 8.5f;
        settingsChunk.noiseStretchY = 12.0f;
        settingsChunk.noiseTopSlideTarget = -10;
        settingsChunk.noiseTopSlideSize = 3;
        settingsChunk.noiseTopSlideOffset = 0;
        settingsChunk.noiseBottomSlideTarget = 15;
        settingsChunk.noiseBottomSlideSize = 3;
        settingsChunk.noiseBottomSlideOffset = 0;
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
        settingsBiome.singleBiome = ModernBetaBiomes.INFDEV_611.getValue().toString();
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }

    private static ModernBetaSettingsPreset presetInfdev325() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.INFDEV_227.id;
        settingsChunk.useDeepslate = false;
        settingsChunk.useCaves = false;
        settingsChunk.infdevUsePyramid = true;
        settingsChunk.infdevUseWall = false;

        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
        settingsBiome.singleBiome = ModernBetaBiomes.INFDEV_325.getValue().toString();

        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;

        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetInfdev227() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.INFDEV_227.id;
        settingsChunk.useDeepslate = false;
        settingsChunk.useCaves = false;
        settingsChunk.infdevUsePyramid = true;
        settingsChunk.infdevUseWall = true;
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
        settingsBiome.singleBiome = ModernBetaBiomes.INFDEV_227.getValue().toString();
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }

    private static ModernBetaSettingsPreset presetIndev() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.INDEV.id;
        settingsChunk.useDeepslate = false;
        settingsChunk.useCaves = false;
        settingsChunk.indevLevelTheme = IndevTheme.NORMAL.getId();
        settingsChunk.indevLevelType = IndevType.ISLAND.getId();
        settingsChunk.indevLevelWidth = 256;
        settingsChunk.indevLevelLength = 256;
        settingsChunk.indevLevelHeight = 128;
        settingsChunk.indevCaveRadius = 1.0f;
        settingsChunk.indevUseCaves = true;
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
        settingsBiome.singleBiome = ModernBetaBiomes.INDEV_NORMAL.getValue().toString();
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetClassic() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.CLASSIC_0_30.id;
        settingsChunk.useDeepslate = false;
        settingsChunk.useCaves = false;
        settingsChunk.indevLevelWidth = 256;
        settingsChunk.indevLevelLength = 256;
        settingsChunk.indevLevelHeight = 128;
        settingsChunk.indevCaveRadius = 1.0f;
        settingsChunk.indevUseCaves = true;
        settingsChunk.indevGravelBeachUnderAir = false;
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
        settingsBiome.singleBiome = ModernBetaBiomes.INDEV_NORMAL.getValue().toString();
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }

    private static ModernBetaSettingsPreset presetClassic14a08() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.CLASSIC_0_30.id;
        settingsChunk.useDeepslate = false;
        settingsChunk.useCaves = false;
        settingsChunk.indevLevelWidth = 256;
        settingsChunk.indevLevelLength = 256;
        settingsChunk.indevLevelHeight = 128;
        settingsChunk.indevCaveRadius = 1.0f;
        settingsChunk.indevUseCaves = true;
        settingsChunk.indevMinHeightDamp = 8.0f;
        settingsChunk.indevMinHeightBoost = -8.0f;
        settingsChunk.indevMaxHeightDamp = 6.0f;
        settingsChunk.indevMaxHeightBoost = 6.0f;
        settingsChunk.indevHeightUnderDamp = 2.0f;
        settingsChunk.indevCaveRarity = 16384;
        settingsChunk.indevSandBeachUnderAir = true;
        settingsChunk.indevSandBeachUnderFluid = false;
        settingsChunk.indevGravelBeachUnderAir = true;
        settingsChunk.indevGravelBeachUnderFluid = false;

        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
        settingsBiome.singleBiome = ModernBetaBiomes.CLASSIC_14A_08.getValue().toString();

        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;

        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetPE() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.PE.id;
        settingsChunk.useDeepslate = false;
        settingsChunk.useCaves = false;
        settingsChunk.noiseCoordinateScale = 684.412f;
        settingsChunk.noiseHeightScale = 684.412f;
        settingsChunk.noiseUpperLimitScale = 512f;
        settingsChunk.noiseLowerLimitScale = 512f;
        settingsChunk.noiseDepthNoiseScaleX = 200;
        settingsChunk.noiseDepthNoiseScaleZ = 200;
        settingsChunk.noiseMainNoiseScaleX = 80f;
        settingsChunk.noiseMainNoiseScaleY = 160f;
        settingsChunk.noiseMainNoiseScaleZ = 80f;
        settingsChunk.noiseBaseSize = 8.5f;
        settingsChunk.noiseStretchY = 12.0f;
        settingsChunk.noiseTopSlideTarget = -10;
        settingsChunk.noiseTopSlideSize = 3;
        settingsChunk.noiseTopSlideOffset = 0;
        settingsChunk.noiseBottomSlideTarget = 15;
        settingsChunk.noiseBottomSlideSize = 3;
        settingsChunk.noiseBottomSlideOffset = 0;
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.PE.id;
        settingsBiome.climateTempNoiseScale = 0.025f;
        settingsBiome.climateRainNoiseScale = 0.05f;
        settingsBiome.climateDetailNoiseScale = 0.25f;
        settingsBiome.climateMappings = ModernBetaSettingsBiome.Builder.createClimateMapping(
            new ClimateMapping(
                ModernBetaBiomes.PE_DESERT.getValue().toString(),
                ModernBetaBiomes.PE_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.PE_FOREST.getValue().toString(),
                ModernBetaBiomes.PE_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.PE_TUNDRA.getValue().toString(),
                ModernBetaBiomes.PE_FROZEN_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.PE_PLAINS.getValue().toString(),
                ModernBetaBiomes.PE_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.PE_RAINFOREST.getValue().toString(),
                ModernBetaBiomes.PE_WARM_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.PE_SAVANNA.getValue().toString(),
                ModernBetaBiomes.PE_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.PE_SHRUBLAND.getValue().toString(),
                ModernBetaBiomes.PE_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.PE_SEASONAL_FOREST.getValue().toString(),
                ModernBetaBiomes.PE_LUKEWARM_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.PE_SWAMPLAND.getValue().toString(),
                ModernBetaBiomes.PE_COLD_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.PE_TAIGA.getValue().toString(),
                ModernBetaBiomes.PE_FROZEN_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.PE_TUNDRA.getValue().toString(),
                ModernBetaBiomes.PE_FROZEN_OCEAN.getValue().toString()
            )
        );
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetBetaSkylands() {
        ModernBetaSettingsPreset initial = presetSkylands();
        
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        NbtCompound compoundBiome = initial.settingsBiome().toCompound();
        NbtCompound compoundCaveBiome = initial.settingsCaveBiome().toCompound();
        
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.BETA.id;
        settingsBiome.useOceanBiomes = false;
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.VORONOI.id;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetIsles(ModernBetaSettingsPreset initial) {
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);

        settingsChunk.islesUseIslands = true;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            initial.settingsBiome(),
            initial.settingsCaveBiome()
        );
    }

    private static ModernBetaSettingsPreset presetWaterWorld(ModernBetaSettingsPreset initial) {
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);

        settingsChunk.seaLevelOffset = 192;
        settingsChunk.noiseMainNoiseScaleX = 5000.0f;
        settingsChunk.noiseMainNoiseScaleY = 1000.0f;
        settingsChunk.noiseMainNoiseScaleZ = 5000.0f;
        settingsChunk.noiseStretchY = 8.0f;
        settingsChunk.releaseBiomeDepthWeight = 2.0f;
        settingsChunk.releaseBiomeDepthOffset = 0.5f;
        settingsChunk.releaseBiomeScaleWeight = 2.0f;
        settingsChunk.releaseBiomeScaleOffset = 0.375f;

        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            initial.settingsBiome(),
            initial.settingsCaveBiome()
        );
    }

    private static ModernBetaSettingsPreset presetIsleLand(ModernBetaSettingsPreset initial) {
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        

        settingsChunk.noiseCoordinateScale = 3000.0f;
        settingsChunk.noiseHeightScale = 6000.0f;
        settingsChunk.noiseStretchY = 10.0f;
        settingsChunk.noiseUpperLimitScale = 250.0f;
        settingsChunk.noiseLowerLimitScale = 512.0f;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            initial.settingsBiome(),
            initial.settingsCaveBiome()
        );
    }
    

    private static ModernBetaSettingsPreset presetCaveDelight(ModernBetaSettingsPreset initial) {
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        

        settingsChunk.noiseMainNoiseScaleX = 5000.0f;
        settingsChunk.noiseMainNoiseScaleY = 1000.0f;
        settingsChunk.noiseMainNoiseScaleZ = 5000.0f;
        settingsChunk.noiseStretchY = 5.0f;
        settingsChunk.releaseBiomeDepthWeight = 2.0f;
        settingsChunk.releaseBiomeDepthOffset = 1.0f;
        settingsChunk.releaseBiomeScaleWeight = 4.0f;
        settingsChunk.releaseBiomeScaleOffset = 1.0f;

        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            initial.settingsBiome(),
            initial.settingsCaveBiome()
        );
    }

    private static ModernBetaSettingsPreset presetMountainMadness(ModernBetaSettingsPreset initial) {
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);

        settingsChunk.noiseMainNoiseScaleX = 1355.9908f;
        settingsChunk.noiseMainNoiseScaleY = 745.5343f;
        settingsChunk.noiseMainNoiseScaleZ = 1183.464f;
        settingsChunk.noiseDepthNoiseScaleX = 374.93652f;
        settingsChunk.noiseDepthNoiseScaleZ = 288.65228f;
        settingsChunk.noiseBaseSize = 1.8758626f;
        settingsChunk.noiseCoordinateScale = 738.41864f;
        settingsChunk.noiseHeightScale = 157.69133f;
        settingsChunk.noiseStretchY = 1.7137525f;
        settingsChunk.noiseUpperLimitScale = 801.4267f;
        settingsChunk.noiseLowerLimitScale = 1254.1643f;
        settingsChunk.releaseBiomeDepthWeight = 1.7553768f;
        settingsChunk.releaseBiomeDepthOffset = 3.4701107f;
        settingsChunk.releaseBiomeScaleWeight = 1.0f;
        settingsChunk.releaseBiomeScaleOffset = 2.535211f;

        if (settingsChunk.chunkProvider.equals("beta")) {
            settingsChunk.noiseBaseSize = 8.5f;
        }

        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            initial.settingsBiome(),
            initial.settingsCaveBiome()
        );
    }

    private static ModernBetaSettingsPreset presetDrought(ModernBetaSettingsPreset initial) {
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);

        settingsChunk.seaLevelOffset = -43;
        settingsChunk.noiseMainNoiseScaleX = 1000.0f;
        settingsChunk.noiseMainNoiseScaleY = 3000.0f;
        settingsChunk.noiseMainNoiseScaleZ = 1000.0f;
        settingsChunk.noiseStretchY = 10.0f;

        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            initial.settingsBiome(),
            initial.settingsCaveBiome()
        );
    }

    private static ModernBetaSettingsPreset presetCaveChaos(ModernBetaSettingsPreset initial) {
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);

        settingsChunk.seaLevelOffset = -57;
        settingsChunk.noiseUpperLimitScale = 2.0f;
        settingsChunk.noiseLowerLimitScale = 64.0f;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            initial.settingsBiome(),
            initial.settingsCaveBiome()
        );
    }
    
    private static ModernBetaSettingsPreset presetBetaLargeBiomes() {
        ModernBetaSettingsPreset initial = presetBeta();
        
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        NbtCompound compoundBiome = initial.settingsBiome().toCompound();
        NbtCompound compoundCaveBiome = initial.settingsCaveBiome().toCompound();
        
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);
        
        settingsBiome.climateTempNoiseScale = 0.025f / 4.0f;
        settingsBiome.climateRainNoiseScale = 0.05f / 4.0f;
        settingsBiome.climateDetailNoiseScale = 0.25f / 2.0f;
        
        settingsCaveBiome.voronoiHorizontalNoiseScale = 32.0f * 4.0f;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetBetaXboxLegacy() {
        ModernBetaSettingsPreset initial = presetBeta();
        
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        NbtCompound compoundBiome = initial.settingsBiome().toCompound();
        NbtCompound compoundCaveBiome = initial.settingsCaveBiome().toCompound();
        
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);
        
        settingsChunk.islesUseIslands = true;
        settingsChunk.islesUseOuterIslands = false;
        settingsChunk.islesCenterIslandShape = IslandShape.SQUARE.getId();
        settingsChunk.islesCenterIslandRadius = 25;
        settingsChunk.islesCenterIslandFalloffDistance = 2;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetBetaSurvivalIsland() {
        ModernBetaSettingsPreset initial = presetBeta();
        
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        NbtCompound compoundBiome = initial.settingsBiome().toCompound();
        NbtCompound compoundCaveBiome = initial.settingsCaveBiome().toCompound();
        
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);
        
        settingsChunk.islesUseIslands = true;
        settingsChunk.islesUseOuterIslands = false;
        settingsChunk.islesCenterIslandRadius = 1;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetBetaVanilla() {
        ModernBetaSettingsPreset initial = presetBeta();
        
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        NbtCompound compoundBiome = initial.settingsBiome().toCompound();
        NbtCompound compoundCaveBiome = initial.settingsCaveBiome().toCompound();
        
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);

        settingsChunk.useSurfaceRules = true;
        settingsChunk.useFixedCaves = true;

        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.VORONOI.id;
        settingsBiome.climateTempNoiseScale = 0.025f / 3.0f;
        settingsBiome.climateRainNoiseScale = 0.05f / 3.0f;
        settingsBiome.climateDetailNoiseScale = 0.25f / 1.5f;
        settingsBiome.voronoiPoints = List.of(
                // Standard Biomes

               new VoronoiPointBiome(
                   BiomeKeys.DESERT.getValue().toString(),
                   BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                   0.9, 0.1, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.PLAINS.getValue().toString(),
                   BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                   0.9, 0.3, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.FOREST.getValue().toString(),
                   BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                   0.9, 0.5, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.FOREST.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   0.9, 0.7, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.JUNGLE.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   0.9, 0.9, 0.5
               ),

               new VoronoiPointBiome(
                   BiomeKeys.SAVANNA.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.1, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.PLAINS.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.3, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.FOREST.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.5, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.FOREST.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.7, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.FOREST.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.9, 0.5
               ),

               new VoronoiPointBiome(
                   BiomeKeys.PLAINS.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.1, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.PLAINS.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.3, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.BIRCH_FOREST.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.5, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.BIRCH_FOREST.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.7, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SWAMP.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.9, 0.5
               ),

               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.3, 0.1, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.TAIGA.getValue().toString(),
                   BiomeKeys.COLD_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_COLD_OCEAN.getValue().toString(),
                   0.3, 0.3, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.TAIGA.getValue().toString(),
                   BiomeKeys.COLD_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_COLD_OCEAN.getValue().toString(),
                   0.3, 0.5, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_TAIGA.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.3, 0.7, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_TAIGA.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.3, 0.9, 0.5
               ),

               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.1, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.3, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.5, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.7, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.9, 0.5
               ),
               
               // Mutated Biomes

               new VoronoiPointBiome(
                   BiomeKeys.DESERT.getValue().toString(),
                   BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                   0.9, 0.1, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SUNFLOWER_PLAINS.getValue().toString(),
                   BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                   0.9, 0.3, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.DARK_FOREST.getValue().toString(),
                   BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                   0.9, 0.5, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.DARK_FOREST.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   0.9, 0.7, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.BAMBOO_JUNGLE.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   0.9, 0.9, 0.2
               ),

               new VoronoiPointBiome(
                   BiomeKeys.SAVANNA.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.1, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.MEADOW.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.3, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.FLOWER_FOREST.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.5, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.FLOWER_FOREST.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.7, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.FLOWER_FOREST.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.9, 0.2
               ),

               new VoronoiPointBiome(
                   BiomeKeys.MEADOW.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.1, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.MEADOW.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.3, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.CHERRY_GROVE.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.5, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.CHERRY_GROVE.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.7, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.MANGROVE_SWAMP.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.9, 0.2
               ),

               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.3, 0.1, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.OLD_GROWTH_PINE_TAIGA.getValue().toString(),
                   BiomeKeys.COLD_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_COLD_OCEAN.getValue().toString(),
                   0.3, 0.3, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.OLD_GROWTH_PINE_TAIGA.getValue().toString(),
                   BiomeKeys.COLD_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_COLD_OCEAN.getValue().toString(),
                   0.3, 0.5, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.GROVE.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.3, 0.7, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.GROVE.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.3, 0.9, 0.2
               ),

               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.1, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.3, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.5, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_SLOPES.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.7, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_SLOPES.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.9, 0.2
               ),
               
               // Mutated Biomes 2

               new VoronoiPointBiome(
                   BiomeKeys.BADLANDS.getValue().toString(),
                   BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                   0.9, 0.1, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.PLAINS.getValue().toString(),
                   BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                   0.9, 0.3, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SPARSE_JUNGLE.getValue().toString(),
                   BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                   0.9, 0.5, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SPARSE_JUNGLE.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   0.9, 0.7, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.MUSHROOM_FIELDS.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   0.9, 0.9, 0.8
               ),

               new VoronoiPointBiome(
                   BiomeKeys.SAVANNA.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.1, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.PLAINS.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.3, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.PALE_GARDEN.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.5, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.PALE_GARDEN.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.7, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.PALE_GARDEN.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.9, 0.8
               ),

               new VoronoiPointBiome(
                   BiomeKeys.PLAINS.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.1, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.PLAINS.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.3, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.OLD_GROWTH_BIRCH_FOREST.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.5, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.OLD_GROWTH_BIRCH_FOREST.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.7, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.MANGROVE_SWAMP.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.9, 0.8
               ),

               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.3, 0.1, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA.getValue().toString(),
                   BiomeKeys.COLD_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_COLD_OCEAN.getValue().toString(),
                   0.3, 0.3, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA.getValue().toString(),
                   BiomeKeys.COLD_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_COLD_OCEAN.getValue().toString(),
                   0.3, 0.5, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.GROVE.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.3, 0.7, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.GROVE.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.3, 0.9, 0.8
               ),

               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.1, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.3, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.5, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.ICE_SPIKES.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.7, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.ICE_SPIKES.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.9, 0.8
               )
           );
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetAlphaWinter() {
        ModernBetaSettingsPreset initial = presetAlpha();
        
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        NbtCompound compoundBiome = initial.settingsBiome().toCompound();
        NbtCompound compoundCaveBiome = initial.settingsCaveBiome().toCompound();
        
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);
        
        settingsBiome.singleBiome = ModernBetaBiomes.ALPHA_WINTER.getValue().toString();
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetIndevParadise() {
        ModernBetaSettingsPreset initial = presetIndev();
        
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        NbtCompound compoundBiome = initial.settingsBiome().toCompound();
        NbtCompound compoundCaveBiome = initial.settingsCaveBiome().toCompound();
        
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);
        
        settingsChunk.indevLevelTheme = IndevTheme.PARADISE.getId();
        
        settingsBiome.singleBiome = ModernBetaBiomes.INDEV_PARADISE.getValue().toString();
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetIndevWoods() {
        ModernBetaSettingsPreset initial = presetIndev();
        
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        NbtCompound compoundBiome = initial.settingsBiome().toCompound();
        NbtCompound compoundCaveBiome = initial.settingsCaveBiome().toCompound();
        
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);
        
        settingsChunk.indevLevelTheme = IndevTheme.WOODS.getId();
        
        settingsBiome.singleBiome = ModernBetaBiomes.INDEV_WOODS.getValue().toString();
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetIndevHell() {
        ModernBetaSettingsPreset initial = presetIndev();
        
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        NbtCompound compoundBiome = initial.settingsBiome().toCompound();
        NbtCompound compoundCaveBiome = initial.settingsCaveBiome().toCompound();
        
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);
        
        settingsChunk.indevLevelTheme = IndevTheme.HELL.getId();
        
        settingsBiome.singleBiome = ModernBetaBiomes.INDEV_HELL.getValue().toString();
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }

    private static ModernBetaSettingsPreset presetBeta181(int biomeScale) {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id;
        settingsChunk.useFixedCaves = true;
        settingsChunk.releaseBiomeHeightConfigs = Map.ofEntries(
            Map.entry("minecraft:ocean", "-1.0;0.5")
        );

        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.FRACTAL.id;
        settingsBiome.fractalLayers = new ConfiguredLayers(Arrays.asList(
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
            new ModalZoomLayer("land", 1000, "land"),
            AddLandLayer.forBeta("land", 3, "land"),
            StackedZoomLayer.modal("land", 1001, "land", 3 + biomeScale),
            new SmoothLayer("land", 1000, "land"),
            MixRiverLayer.forEarlyRelease("land", 0, "land", "river")
        ));

        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }

    private static ConfiguredLayers configuredLayers100Era(int biomeScale, ExtendedBiomeId icePlains) {
        return new ConfiguredLayers(Arrays.asList(
            new InitLandLayer("land", 1),
            new FuzzyZoomLayer("land", 2000, "land"),
            AddLandLayer.forIslandScale("land", 1, "land"),
            new ModalZoomLayer("land", 2001, "land"),
            AddLandLayer.forIslandScale("land", 2, "land"),
            new WeightedBiomeLayer("snow", 2, Pool.of(
                new Weighted<>(ExtendedBiomeId.SNOWY_PLAINS, 1),
                new Weighted<>(ExtendedBiomeId.NULL, 4)
            )),
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
            new ModalZoomLayer("land", 1000, "land"),
            AddLandLayer.forEarlyRelease("land", 3, "land", icePlains),
            new ConditionalBiomeOverlayLayer("land", 0, "land",
                PredicateOverlayLayer.Target.MUSHROOM_SHORE.predicate(),
                ExtendedBiomeId.MUSHROOM_SHORE, ExtendedBiomeId.NULL),
            StackedZoomLayer.modal("land", 1001, "land", 3 + biomeScale),
            new SmoothLayer("land", 1000, "land"),
            MixRiverLayer.forEarlyRelease("land", 0, "land", "river")
        ));
    }

    private static ModernBetaSettingsPreset presetBeta19Pre3(int biomeScale) {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id;
        settingsChunk.useFixedCaves = true;

        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.FRACTAL.id;
        settingsBiome.fractalLayers = configuredLayers100Era(biomeScale, ExtendedBiomeId.of(ModernBetaBiomes.LATE_BETA_ICE_PLAINS));

        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }

    private static ModernBetaSettingsPreset preset100(int biomeScale) {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id;
        settingsChunk.useFixedCaves = true;

        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.FRACTAL.id;
        settingsBiome.fractalLayers = configuredLayers100Era(biomeScale, ExtendedBiomeId.of(ModernBetaBiomes.EARLY_RELEASE_ICE_PLAINS));

        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
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

        Layer swampLakesLayer = new WeightedBiomeLayer("swamp_lakes", 1000, Pool.of(
            new Weighted<>(ExtendedBiomeId.RIVER, 1),
            new Weighted<>(ExtendedBiomeId.NULL, 5)
        ));
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

        List<Layer> layers = Arrays.asList(
            new InitLandLayer("land", 1),
            new FuzzyZoomLayer("land", 2000, "land"),
            AddLandLayer.forIslandScale("land", 1, "land"),
            new ModalZoomLayer("land", 2001, "land"),
            AddLandLayer.forIslandScale("land", 2, "land"),
            new WeightedBiomeLayer("snow", 2, Pool.of(
                new Weighted<>(ExtendedBiomeId.SNOWY_PLAINS, 1),
                new Weighted<>(ExtendedBiomeId.NULL, 4)
            )),
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
            new ModalZoomLayer("land", 1000, "land"),
            AddLandLayer.forEarlyRelease("land", 3, "land", icePlains),
            new ModalZoomLayer("land", 1001, "land"),
            new PredicateOverlayLayer("land", 0, "land", List.of(
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
            new PredicateOverlayLayer("land", 1000, "land", lakeOverlays),
            StackedZoomLayer.modal("land", 1002, "land", 2 + biomeScale),
            new SmoothLayer("land", 1000, "land"),
            MixRiverLayer.forEarlyRelease("land", 0, "land", "river")
        );
        if (addJungles) {
            Layer jungleLakesLayer = new WeightedBiomeLayer("jungle_lakes", 1000, Pool.of(
                new Weighted<>(ExtendedBiomeId.RIVER, 1),
                new Weighted<>(ExtendedBiomeId.NULL, 7)
            ));

            layers = new ArrayList<>(layers);
            layers.add(layers.lastIndexOf(swampLakesLayer), jungleLakesLayer);
        }

        return new ConfiguredLayers(layers);
    }

    private static ModernBetaSettingsPreset preset11(int biomeScale) {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id;
        settingsChunk.useFixedCaves = true;

        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.FRACTAL.id;
        settingsBiome.fractalLayers = configuredLayers11Era(biomeScale, false, false);

        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }

    private static ModernBetaSettingsPreset preset125(int biomeScale) {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id;
        settingsChunk.useFixedCaves = true;

        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.FRACTAL.id;
        settingsBiome.fractalLayers = configuredLayers11Era(biomeScale, true, false);

        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }

    private static ModernBetaSettingsPreset preset164(int biomeScale) {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id;
        settingsChunk.useFixedCaves = true;
        settingsChunk.releaseBiomeHeightConfigs = Map.ofEntries(
            Map.entry("minecraft:desert*hills", "0.3;0.8"),
            Map.entry("minecraft:forest*hills", "0.3;0.7"),
            Map.entry("moderner_beta:early_release_extreme_hills", "0.3;1.5"),
            Map.entry("moderner_beta:early_release_ice_plains*hills", "0.3;1.3"),
            Map.entry("minecraft:jungle*hills", "1.8;0.5"),
            Map.entry("moderner_beta:early_release_taiga*hills", "0.3;0.8")
        );

        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.FRACTAL.id;
        settingsBiome.fractalLayers = configuredLayers11Era(biomeScale, true, true);

        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
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
            ExtendedBiomeId.of("minecraft:dark_forest").mapTo("minecraft:pale_garden"),
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
            new WeightedBiomeLayer("climate", 2, Pool.of(
                new Weighted<>(ExtendedBiomeId.CLIMATE_SNOWY, 1),
                new Weighted<>(ExtendedBiomeId.CLIMATE_COOL, 1),
                new Weighted<>(ExtendedBiomeId.CLIMATE_WARM, 4)
            )),
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

        return new ConfiguredLayers(layers);
    }

    private static ModernBetaSettingsPreset preset1122(int biomeScale) {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.MAJOR_RELEASE.id;
        settingsChunk.releaseBiomeHeightConfigs = HeightConfig.MAJOR_RELEASE_CONFIGS;
        settingsChunk.useSurfaceRules = true;
        settingsChunk.useFixedCaves = true;
        settingsChunk.forceBetaCaves = false;

        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.FRACTAL.id;
        settingsBiome.fractalLayers = configuredLayers1710Era(biomeScale, false, false, false, false);

        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }

    private static ModernBetaSettingsPreset preset1171(int biomeScale) {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.MAJOR_RELEASE.id;
        settingsChunk.releaseBiomeHeightConfigs = HeightConfig.MAJOR_RELEASE_CONFIGS;
        settingsChunk.useSurfaceRules = true;
        settingsChunk.useFixedCaves = true;
        settingsChunk.forceBetaCaves = false;

        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.FRACTAL.id;
        settingsBiome.fractalLayers = configuredLayers1710Era(biomeScale, true, true, true, false);

        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }

    private static ModernBetaSettingsPreset presetSnowAintSnowier(int biomeScale) {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.MAJOR_RELEASE.id;
        settingsChunk.releaseBiomeHeightConfigs = HeightConfig.MAJOR_RELEASE_CONFIGS;
        settingsChunk.useSurfaceRules = true;
        settingsChunk.useFixedCaves = true;
        settingsChunk.forceBetaCaves = false;

        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.FRACTAL.id;
        settingsBiome.fractalLayers = configuredLayers1710Era(biomeScale, true, true, true, true);

        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }

    private static ModernBetaSettingsPreset presetReleaseHybrid(int biomeScale) {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id;
        settingsChunk.useFixedCaves = true;
        settingsChunk.releaseBiomeHeightConfigs = Map.ofEntries(
            Map.entry("minecraft:desert*hills", "0.3;0.8"),
            Map.entry("minecraft:forest*hills", "0.3;0.7"),
            Map.entry("minecraft:taiga*hills", "0.3;0.8"),
            Map.entry("minecraft:dark_forest*hills", "0.3;0.7"),
            Map.entry("minecraft:pale_garden*hills", "0.3;0.7"),
            Map.entry("minecraft:birch_forest*hills", "0.3;0.7"),
            Map.entry("minecraft:old_growth_birch_forest*hills", "0.3;0.7"),
            Map.entry("minecraft:flower_forest*hills", "0.3;0.7"),
            Map.entry("minecraft:old_growth_spruce_taiga*hills", "0.3;0.8"),
            Map.entry("minecraft:snowy_taiga*hills", "0.3;0.8"),
            Map.entry("minecraft:snowy_plains*hills", "0.3;1.3"),
            Map.entry("minecraft:jungle*hills", "1.8;0.5"),
            Map.entry("minecraft:badlands*plateau", "1.8;0.2"),
            Map.entry("minecraft:wooded_badlands", "1.8;0.2"),
            Map.entry("minecraft:cherry_grove", "1.8;0.5"),
            Map.entry("minecraft:cherry_grove*edge", "0.8;0.3"),
            Map.entry("minecraft:windswept_hills", "0.3;1.5"),
            Map.entry("minecraft:windswept_forest", "0.3;1.5"),
            Map.entry("minecraft:windswept_gravelly_hills", "0.3;1.5"),
            Map.entry("minecraft:meadow", "1.0;1.0"),
            Map.entry("minecraft:stony_shore", "0.1;1.6"),
            Map.entry("minecraft:ice_spikes", "0.3;0.8"),
            Map.entry("minecraft:windswept_savanna", "0.3;1.5"),
            Map.entry("minecraft:windswept_savanna*plateau", "1.0;1.0")
        );
        settingsChunk.useSurfaceRules = true;

        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.FRACTAL.id;

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
            ExtendedBiomeId.of("minecraft:badlands").mapTo("*minecraft:windswept_savanna"),
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

        settingsBiome.fractalLayers = new ConfiguredLayers(Arrays.asList(
            new InitLandLayer("land", 1),
            new FuzzyZoomLayer("land", 2000, "land"),
            AddLandLayer.forIslandScale("land", 1, "land"),
            new ModalZoomLayer("land", 2001, "land"),
            AddLandLayer.forIslandScale("land", 2, "land"),
            new WeightedBiomeLayer("snow", 2, Pool.of(
                new Weighted<>(ExtendedBiomeId.SNOWY_PLAINS, 1),
                new Weighted<>(ExtendedBiomeId.NULL, 4)
            )),
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
                "minecraft:badlands*plateau",
                "minecraft:badlands*plateau",
                "minecraft:savanna",

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
                "minecraft:badlands*plateau",
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
                BiomePredicate.simpleHills(hillsVariants.keySet()), "hills", "land"),
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
                PredicateOverlayLayer.Target.inclusiveBeach(
                    ExtendedBiomeId.setOf(
                        "minecraft:ocean",
                        "minecraft:river",
                        "minecraft:windswept_hills",
                        "minecraft:windswept_forest",
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
        ));

        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
}