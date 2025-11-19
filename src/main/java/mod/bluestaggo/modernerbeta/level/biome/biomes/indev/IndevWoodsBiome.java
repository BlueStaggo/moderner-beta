package mod.bluestaggo.modernerbeta.level.biome.biomes.indev;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeColors;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeFeatures;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeMobs;
import net.minecraft.core.HolderGetter;
//? if >=1.21.11
//import net.minecraft.world.attribute.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class IndevWoodsBiome {
    public static Biome create(HolderGetter<PlacedFeature> featureLookup, HolderGetter<ConfiguredWorldCarver<?>> carverLookup) {
        MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
        ModernBetaBiomeMobs.addCommonMobs(spawnSettings);
        ModernBetaBiomeMobs.addSquid(spawnSettings);
        
        BiomeGenerationSettings.Builder genSettings = new BiomeGenerationSettings.Builder(featureLookup, carverLookup);
        ModernBetaBiomeFeatures.addIndevWoodsFeatures(genSettings);

        return VersionCompat.buildBiomeWithColors(new Biome.BiomeBuilder(),
                ModernBetaBiomeColors.OLD_GRASS_COLOR,
                ModernBetaBiomeColors.OLD_FOLIAGE_COLOR,
                ModernBetaBiomeColors.INDEV_WOODS_SKY_COLOR,
                ModernBetaBiomeColors.INDEV_WOODS_FOG_COLOR,
                ModernBetaBiomeColors.OLD_WATER_COLOR,
                ModernBetaBiomeColors.OLD_WATER_FOG_COLOR)
            //? if <1.21.11
            .hasPrecipitation(false)
            .temperature(0.6F)
            .downfall(0.6F)
            .mobSpawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            //? if >=1.21.11 {
            /*.putAttributes(EnvironmentAttributeMap.builder()
                .set(EnvironmentAttributes.SKY_LIGHT_FACTOR, 9.0F / 15.0F)
                .set(EnvironmentAttributes.CLOUD_COLOR, 0xFF4D5A5B)
                .build())
            *///? }
            .build();
    }
}
