package com.bespectacled.modernbeta.world.biome.inf;

import com.bespectacled.modernbeta.world.biome.OldBiomeColors;
import com.bespectacled.modernbeta.world.biome.OldBiomeFeatures;
import com.bespectacled.modernbeta.world.feature.OldConfiguredFeatures;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.SpawnSettings.SpawnEntry;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

public class Infdev227 {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings);
        
        spawnSettings.spawn(SpawnGroup.WATER_CREATURE, new SpawnSettings.SpawnEntry(EntityType.SQUID, 10, 1, 4));
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.WOLF, 5, 4, 4));
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        genSettings.surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
        
        OldBiomeFeatures.addDefaultFeatures(genSettings, false, InfBiomes.ADD_LAKES_INF_227, InfBiomes.ADD_SPRINGS_INF_227);
        OldBiomeFeatures.addOres(genSettings);
        DefaultBiomeFeatures.addFrozenTopLayer(genSettings);
        
        genSettings.structureFeature(ConfiguredStructureFeatures.VILLAGE_PLAINS);
        genSettings.structureFeature(ConfiguredStructureFeatures.PILLAGER_OUTPOST);
        genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_CACTUS_ALPHA);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.FLOWER_INFDEV_227);
        
        OldBiomeFeatures.addVegetalPatches(genSettings);
        
        OldBiomeFeatures.addCarvers(genSettings, false);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.RAIN)
            .category(Biome.Category.FOREST)
            .depth(0.37F)
            .scale(0.4F)
            .temperature(0.6F)
            .downfall(0.6F)
            .effects((new BiomeEffects.Builder())
                .grassColor(OldBiomeColors.OLD_GRASS_COLOR)
                .foliageColor(OldBiomeColors.OLD_FOLIAGE_COLOR)
                .skyColor(OldBiomeColors.INFDEV_227_SKY_COLOR)
                .fogColor(OldBiomeColors.INFDEV_227_FOG_COLOR)
                .waterColor(OldBiomeColors.OLD_WATER_COLOR)
                .waterFogColor(OldBiomeColors.OLD_WATER_FOG_COLOR)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}