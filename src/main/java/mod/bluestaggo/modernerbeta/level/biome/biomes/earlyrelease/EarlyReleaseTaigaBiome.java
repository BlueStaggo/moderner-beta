package mod.bluestaggo.modernerbeta.level.biome.biomes.earlyrelease;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeColors;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class EarlyReleaseTaigaBiome {
    public static Biome create(HolderGetter<PlacedFeature> featureLookup, HolderGetter<ConfiguredWorldCarver<?>> carverLookup) {
        MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.farmAnimals(spawnSettings);
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.CREATURE,
            net.minecraft.world.entity.EntityType.WOLF, 8, 4, 4);
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.CREATURE,
            net.minecraft.world.entity.EntityType.RABBIT, 4, 2, 3);
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.CREATURE,
            net.minecraft.world.entity.EntityType.FOX, 8, 2, 4);
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
        BiomeDefaultFeatures.addRareBerryBushes(genSettings);

        return VersionCompat.buildBiomeWithColors(new Biome.BiomeBuilder(),
                OverworldBiomes.calculateSkyColor(0.05F),
                ModernBetaBiomeColors.BETA_FOG_COLOR,
                ModernBetaBiomeColors.VANILLA_FROZEN_WATER_COLOR,
                ModernBetaBiomeColors.VANILLA_FROZEN_WATER_FOG_COLOR)
            .hasPrecipitation(true)
            .temperature(0.05F)
            .downfall(0.8F)
            .mobSpawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
