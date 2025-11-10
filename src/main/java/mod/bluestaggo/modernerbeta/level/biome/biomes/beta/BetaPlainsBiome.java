package mod.bluestaggo.modernerbeta.level.biome.biomes.beta;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeColors;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeFeatures;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeMobs;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class BetaPlainsBiome {
    public static Biome create(HolderGetter<PlacedFeature> featureLookup, HolderGetter<ConfiguredWorldCarver<?>> carverLookup) {
        MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
        ModernBetaBiomeMobs.addPlainsMobs(spawnSettings);
        ModernBetaBiomeMobs.addSquid(spawnSettings);
        ModernBetaBiomeMobs.addTurtles(spawnSettings);
        
        BiomeGenerationSettings.Builder genSettings = new BiomeGenerationSettings.Builder(featureLookup, carverLookup);
        ModernBetaBiomeFeatures.addPlainsFeatures(genSettings, false, false);
        
        return VersionCompat.buildBiomeWithColors(new Biome.BiomeBuilder(),
                ModernBetaBiomeColors.BETA_WARM_SKY_COLOR,
                ModernBetaBiomeColors.BETA_FOG_COLOR,
                ModernBetaBiomeColors.VANILLA_WATER_COLOR,
                ModernBetaBiomeColors.VANILLA_WATER_FOG_COLOR)
            .hasPrecipitation(true)
//            .temperature(0.8F)
            .temperature(1.0F)
            .downfall(0.4F)
            .mobSpawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
