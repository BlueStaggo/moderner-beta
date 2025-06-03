package mod.bluestaggo.modernerbeta.world.biome.biomes.earlyrelease;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeColors;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;

public class BiomeEarlyReleaseTaiga {
    public static Biome create(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.CREATURE, EntityType.WOLF, 8, 4, 4);
        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.CREATURE, EntityType.RABBIT, 4, 2, 3);
        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.CREATURE, EntityType.FOX, 8, 2, 4);
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings);

        GenerationSettings.LookupBackedBuilder genSettings = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        DefaultBiomeFeatures.addLandCarvers(genSettings);
        DefaultBiomeFeatures.addAmethystGeodes(genSettings);
        DefaultBiomeFeatures.addDungeons(genSettings);
        DefaultBiomeFeatures.addMineables(genSettings);
        DefaultBiomeFeatures.addSprings(genSettings);
        DefaultBiomeFeatures.addFrozenTopLayer(genSettings);
        DefaultBiomeFeatures.addDefaultOres(genSettings);
        DefaultBiomeFeatures.addDefaultDisks(genSettings);
        DefaultBiomeFeatures.addTaigaTrees(genSettings);
        DefaultBiomeFeatures.addDefaultFlowers(genSettings);
        DefaultBiomeFeatures.addDefaultGrass(genSettings);
        DefaultBiomeFeatures.addDefaultVegetation(genSettings /*? if >=1.21.5 {*/, true/*?}*/);
        DefaultBiomeFeatures.addSweetBerryBushesSnowy(genSettings);

        return (new Biome.Builder())
            .precipitation(true)
            .temperature(0.05F)
            .downfall(0.8F)
            .effects((new BiomeEffects.Builder())
                .skyColor(OverworldBiomeCreator.getSkyColor(0.05F))
                .fogColor(ModernBetaBiomeColors.BETA_FOG_COLOR)
                .waterColor(ModernBetaBiomeColors.VANILLA_FROZEN_WATER_COLOR)
                .waterFogColor(ModernBetaBiomeColors.VANILLA_FROZEN_WATER_FOG_COLOR)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
