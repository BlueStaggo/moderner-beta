package mod.bluestaggo.modernerbeta.level.biome.biomes.latebeta;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeColors;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeFeatures;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeMobs;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class LateBetaIcePlainsBiome {
    public static Biome create(HolderGetter<PlacedFeature> featureLookup, HolderGetter<ConfiguredWorldCarver<?>> carverLookup) {
		MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.snowySpawns(spawnSettings /*? >=1.21.11 {*//*, true *//*?}*/);
        ModernBetaBiomeMobs.addSquid(spawnSettings);

        BiomeGenerationSettings.Builder genSettings = new BiomeGenerationSettings.Builder(featureLookup, carverLookup);
        ModernBetaBiomeFeatures.addIcePlainsFeatures(genSettings, false);

        return VersionCompat.buildBiomeWithColors(new Biome.BiomeBuilder(),
                OverworldBiomes.calculateSkyColor(0.0F),
                ModernBetaBiomeColors.BETA_FOG_COLOR,
                ModernBetaBiomeColors.VANILLA_FROZEN_WATER_COLOR,
                ModernBetaBiomeColors.VANILLA_FROZEN_WATER_FOG_COLOR)
            .hasPrecipitation(true)
            .temperature(0.0F)
            .downfall(0.5F)
            .mobSpawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
