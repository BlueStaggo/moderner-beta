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

public class BetaSkyBiome {
    public static Biome create(HolderGetter<PlacedFeature> featureLookup, HolderGetter<ConfiguredWorldCarver<?>> carverLookup) {
        MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
        ModernBetaBiomeMobs.addSkyMobs(spawnSettings);
        
        BiomeGenerationSettings.Builder genSettings = new BiomeGenerationSettings.Builder(featureLookup, carverLookup);
        ModernBetaBiomeFeatures.addSkyFeatures(genSettings);
        
        return VersionCompat.buildBiomeWithColors(new Biome.BiomeBuilder(),
                ModernBetaBiomeColors.SKYLANDS_SKY_COLOR,
                ModernBetaBiomeColors.SKYLANDS_FOG_COLOR,
                ModernBetaBiomeColors.VANILLA_WATER_COLOR,
                ModernBetaBiomeColors.VANILLA_WATER_FOG_COLOR)
            .hasPrecipitation(false)
            .temperature(0.5F)
            .downfall(0.0F)
            .mobSpawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
