package mod.bluestaggo.modernerbeta.level.biome.biomes.latebeta;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeColors;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeFeatures;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeMobs;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class LateBetaExtremeHillsBiome {
    public static Biome create(HolderGetter<PlacedFeature> featureLookup, HolderGetter<ConfiguredWorldCarver<?>> carverLookup) {
        MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
        ModernBetaBiomeMobs.addCommonMobs(spawnSettings);
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.CREATURE,
            net.minecraft.world.entity.EntityType.LLAMA, 5, 4, 6);

        BiomeGenerationSettings.Builder genSettings = new BiomeGenerationSettings.Builder(featureLookup, carverLookup);
        ModernBetaBiomeFeatures.addExtremeHillsFeatures(genSettings);

        return VersionCompat.buildBiomeWithColors(new Biome.BiomeBuilder(),
                OverworldBiomes.calculateSkyColor(0.2F),
                ModernBetaBiomeColors.BETA_FOG_COLOR,
                ModernBetaBiomeColors.VANILLA_WATER_COLOR,
                ModernBetaBiomeColors.VANILLA_WATER_FOG_COLOR)
            .hasPrecipitation(true)
            .temperature(0.2F)
            .downfall(0.3F)
            .mobSpawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
