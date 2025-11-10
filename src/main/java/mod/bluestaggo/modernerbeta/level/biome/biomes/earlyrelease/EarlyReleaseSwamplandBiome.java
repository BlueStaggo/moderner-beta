package mod.bluestaggo.modernerbeta.level.biome.biomes.earlyrelease;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeColors;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeFeatures;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeMobs;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class EarlyReleaseSwamplandBiome {
    public static Biome create(HolderGetter<PlacedFeature> featureLookup, HolderGetter<ConfiguredWorldCarver<?>> carverLookup) {
        MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
        ModernBetaBiomeMobs.addCommonMobs(spawnSettings);
        ModernBetaBiomeMobs.addSwamplandMobs(spawnSettings);

        BiomeGenerationSettings.Builder genSettings = new BiomeGenerationSettings.Builder(featureLookup, carverLookup);
        ModernBetaBiomeFeatures.addAdventureSwamplandFeatures(genSettings, true);

        return VersionCompat.buildBiomeWithColors(new Biome.BiomeBuilder(),
                0x5C694E,
                0x496137,
                OverworldBiomes.calculateSkyColor(0.8F),
                ModernBetaBiomeColors.BETA_FOG_COLOR,
                ModernBetaBiomeColors.VANILLA_SWAMP_WATER_COLOR,
                ModernBetaBiomeColors.VANILLA_SWAMP_WATER_FOG_COLOR)
            .hasPrecipitation(true)
            .temperature(0.8F)
            .downfall(0.9F)
            .mobSpawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
