package mod.bluestaggo.modernerbeta.world.biome.biomes.latebeta;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeColors;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class LateBetaTaigaBiome {
    public static Biome create(HolderGetter<PlacedFeature> featureLookup, HolderGetter<ConfiguredWorldCarver<?>> carverLookup) {
        MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.farmAnimals(spawnSettings);
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.CREATURE, EntityType.WOLF, 8, 4, 4);
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.CREATURE, EntityType.RABBIT, 4, 2, 3);
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.CREATURE, EntityType.FOX, 8, 2, 4);
        BiomeDefaultFeatures.commonSpawns(spawnSettings);

        BiomeGenerationSettings.Builder genSettings = new BiomeGenerationSettings.Builder(featureLookup, carverLookup);
        BiomeDefaultFeatures.addDefaultCarversAndLakes(genSettings);
        BiomeDefaultFeatures.addDefaultCrystalFormations(genSettings);
        BiomeDefaultFeatures.addDefaultMonsterRoom(genSettings);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(genSettings);
        BiomeDefaultFeatures.addDefaultSprings(genSettings);
        BiomeDefaultFeatures.addSurfaceFreezing(genSettings);
        BiomeDefaultFeatures.addDefaultOres(genSettings);
        BiomeDefaultFeatures.addDefaultSoftDisks(genSettings);
        BiomeDefaultFeatures.addTaigaTrees(genSettings);
        BiomeDefaultFeatures.addDefaultFlowers(genSettings);
        BiomeDefaultFeatures.addDefaultGrass(genSettings);
        BiomeDefaultFeatures.addDefaultExtraVegetation(genSettings /*? if >=1.21.5 {*/, true/*?}*/);
        BiomeDefaultFeatures.addCommonBerryBushes(genSettings);

        return (new Biome.BiomeBuilder())
            .hasPrecipitation(true)
            .temperature(0.2F)
            .downfall(0.8F)
            .specialEffects((new BiomeSpecialEffects.Builder())
                .skyColor(OverworldBiomes.calculateSkyColor(0.2F))
                .fogColor(ModernBetaBiomeColors.BETA_FOG_COLOR)
                .waterColor(ModernBetaBiomeColors.VANILLA_WATER_COLOR)
                .waterFogColor(ModernBetaBiomeColors.VANILLA_WATER_FOG_COLOR)
                .build())
            .mobSpawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
