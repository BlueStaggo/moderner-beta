package mod.bluestaggo.modernerbeta.world.biome.biomes.pe;

import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeColors;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeFeatures;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeMobs;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class BiomePETundra {
    public static Biome create(HolderGetter<PlacedFeature> featureLookup, HolderGetter<ConfiguredWorldCarver<?>> carverLookup) {
        MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
        ModernBetaBiomeMobs.addTundraMobs(spawnSettings);
        ModernBetaBiomeMobs.addSquid(spawnSettings);
        
        BiomeGenerationSettings.Builder genSettings = new BiomeGenerationSettings.Builder(featureLookup, carverLookup);
        ModernBetaBiomeFeatures.addTundraFeatures(genSettings, true);
        
        return (new Biome.BiomeBuilder())
            .hasPrecipitation(true)
            .temperature(0.0F)
            .downfall(0.5F)
            .specialEffects((new BiomeSpecialEffects.Builder())
                .skyColor(ModernBetaBiomeColors.PE_SKY_COLOR)
                .fogColor(ModernBetaBiomeColors.PE_FOG_COLOR)
                .waterColor(ModernBetaBiomeColors.OLD_WATER_COLOR)
                .waterFogColor(ModernBetaBiomeColors.OLD_WATER_FOG_COLOR)
                .grassColorOverride(ModernBetaBiomeColors.PE_GRASS_COLOR)
                .foliageColorOverride(ModernBetaBiomeColors.PE_FOLIAGE_COLOR)
                .build())
            .mobSpawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
