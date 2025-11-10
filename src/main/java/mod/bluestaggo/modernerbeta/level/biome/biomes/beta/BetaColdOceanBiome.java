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

public class BetaColdOceanBiome {
    public static Biome create(HolderGetter<PlacedFeature> featureLookup, HolderGetter<ConfiguredWorldCarver<?>> carverLookup) {
        MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
        ModernBetaBiomeMobs.addColdOceanMobs(spawnSettings);
        
        BiomeGenerationSettings.Builder genSettings = new BiomeGenerationSettings.Builder(featureLookup, carverLookup);
        ModernBetaBiomeFeatures.addColdOceanFeatures(genSettings, false);
        
        return VersionCompat.buildBiomeWithColors(new Biome.BiomeBuilder(),
                ModernBetaBiomeColors.BETA_COOL_SKY_COLOR,
                ModernBetaBiomeColors.BETA_FOG_COLOR,
                ModernBetaBiomeColors.USE_DEBUG_OCEAN_COLOR ? 16777215 : ModernBetaBiomeColors.VANILLA_COLD_WATER_COLOR,
                ModernBetaBiomeColors.VANILLA_COLD_WATER_FOG_COLOR)
            .hasPrecipitation(true)
            .temperature(0.5F)
            .downfall(1.0F)
            .mobSpawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
