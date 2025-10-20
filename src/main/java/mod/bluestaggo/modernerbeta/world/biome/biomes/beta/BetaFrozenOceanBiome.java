package mod.bluestaggo.modernerbeta.world.biome.biomes.beta;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeColors;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeFeatures;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeMobs;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class BetaFrozenOceanBiome {
    public static Biome create(HolderGetter<PlacedFeature> featureLookup, HolderGetter<ConfiguredWorldCarver<?>> carverLookup) {
        MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
        ModernBetaBiomeMobs.addFrozenOceanMobs(spawnSettings);
        
        BiomeGenerationSettings.Builder genSettings = new BiomeGenerationSettings.Builder(featureLookup, carverLookup);
        ModernBetaBiomeFeatures.addFrozenOceanFeatures(genSettings, false);
        
        return VersionCompat.buildBiomeWithColors(new Biome.BiomeBuilder(),
                ModernBetaBiomeColors.BETA_COLD_SKY_COLOR,
                ModernBetaBiomeColors.BETA_FOG_COLOR,
                ModernBetaBiomeColors.USE_DEBUG_OCEAN_COLOR ? 16777215 : ModernBetaBiomeColors.VANILLA_FROZEN_WATER_COLOR,
                ModernBetaBiomeColors.VANILLA_FROZEN_WATER_FOG_COLOR)
            .hasPrecipitation(true)
            .temperature(0.0F)
            .downfall(0.5F)
            .mobSpawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
