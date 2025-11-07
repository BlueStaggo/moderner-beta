package mod.bluestaggo.modernerbeta.world.biome.biomes.indev;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeColors;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeFeatures;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeMobs;
import net.minecraft.core.HolderGetter;
//? if >=1.21.11
/*import net.minecraft.world.attribute.*;*/
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class IndevParadiseBiome {
    public static Biome create(HolderGetter<PlacedFeature> featureLookup, HolderGetter<ConfiguredWorldCarver<?>> carverLookup) {
        MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
        ModernBetaBiomeMobs.addCommonMobs(spawnSettings);
        ModernBetaBiomeMobs.addSquid(spawnSettings);
        
        BiomeGenerationSettings.Builder genSettings = new BiomeGenerationSettings.Builder(featureLookup, carverLookup);
        ModernBetaBiomeFeatures.addIndevParadiseFeatures(genSettings);
        
        return VersionCompat.buildBiomeWithColors(new Biome.BiomeBuilder(),
                ModernBetaBiomeColors.OLD_GRASS_COLOR,
                ModernBetaBiomeColors.OLD_FOLIAGE_COLOR,
                ModernBetaBiomeColors.INDEV_PARADISE_SKY_COLOR,
                ModernBetaBiomeColors.INDEV_PARADISE_FOG_COLOR,
                ModernBetaBiomeColors.OLD_WATER_COLOR,
                ModernBetaBiomeColors.OLD_WATER_FOG_COLOR)
            .hasPrecipitation(false)
            .temperature(0.6F)
            .downfall(0.6F)
            .mobSpawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            //? if >=1.21.11 {
            /*.putAttributes(EnvironmentAttributeMap.builder()
                .set(EnvironmentAttributes.CLOUD_COLOR, 0xFFEEEEFF)
                .build())
            *///? }
            .build();
    }
}
