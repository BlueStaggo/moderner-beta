package mod.bluestaggo.modernerbeta.level.biome.biomes.indev;

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

public class Classic14a08Biome {
    public static Biome create(HolderGetter<PlacedFeature> featureLookup, HolderGetter<ConfiguredWorldCarver<?>> carverLookup) {
        MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
        ModernBetaBiomeMobs.addCommonMobs(spawnSettings);
        ModernBetaBiomeMobs.addSquid(spawnSettings);

        BiomeGenerationSettings.Builder genSettings = new BiomeGenerationSettings.Builder(featureLookup, carverLookup);
        ModernBetaBiomeFeatures.addClassic14a08Features(genSettings);
        
        return VersionCompat.buildBiomeWithColors(new Biome.BiomeBuilder(),
                ModernBetaBiomeColors.OLD_GRASS_COLOR,
                ModernBetaBiomeColors.OLD_FOLIAGE_COLOR,
                ModernBetaBiomeColors.INDEV_NORMAL_SKY_COLOR,
                ModernBetaBiomeColors.INDEV_NORMAL_FOG_COLOR,
                ModernBetaBiomeColors.OLD_WATER_COLOR,
                ModernBetaBiomeColors.OLD_WATER_FOG_COLOR)
            .hasPrecipitation(true)
            .temperature(0.6F)
            .downfall(0.6F)
            .mobSpawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
