package mod.bluestaggo.modernerbeta.level.biome.biomes.pe;

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

public class PELukewarmOceanBiome {
    public static Biome create(HolderGetter<PlacedFeature> featureLookup, HolderGetter<ConfiguredWorldCarver<?>> carverLookup) {
        MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
        ModernBetaBiomeMobs.addLukewarmOceanMobs(spawnSettings);
        
        BiomeGenerationSettings.Builder genSettings = new BiomeGenerationSettings.Builder(featureLookup, carverLookup);
        ModernBetaBiomeFeatures.addLukewarmOceanFeatures(genSettings, true);
        
        return VersionCompat.buildBiomeWithColors(new Biome.BiomeBuilder(),
                ModernBetaBiomeColors.PE_GRASS_COLOR,
                ModernBetaBiomeColors.PE_FOLIAGE_COLOR,
                ModernBetaBiomeColors.PE_SKY_COLOR,
                ModernBetaBiomeColors.PE_FOG_COLOR,
                ModernBetaBiomeColors.OLD_WATER_COLOR,
                ModernBetaBiomeColors.OLD_WATER_FOG_COLOR)
            .hasPrecipitation(true)
            .temperature(1.0F)
            .downfall(0.7F)
            .mobSpawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
