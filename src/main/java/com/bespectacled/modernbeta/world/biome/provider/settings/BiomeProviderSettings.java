package com.bespectacled.modernbeta.world.biome.provider.settings;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.nbt.NbtCompound;

public class BiomeProviderSettings {
    public static NbtCompound createBiomeSettingsBase(String biomeType) {
        NbtCompound settings = new NbtCompound();
        
        settings.putString("biomeType", biomeType);
        
        return settings;
    }
    
    public static NbtCompound createBiomeSettingsBeta(String biomeType) {
        NbtCompound settings = createBiomeSettingsBase(biomeType);
        
        settings.putString("desert", ModernBeta.BIOME_CONFIG.betaDesertBiome);
        settings.putString("forest", ModernBeta.BIOME_CONFIG.betaForestBiome);
        settings.putString("ice_desert", ModernBeta.BIOME_CONFIG.betaIceDesertBiome);
        settings.putString("plains", ModernBeta.BIOME_CONFIG.betaPlainsBiome);
        settings.putString("rainforest", ModernBeta.BIOME_CONFIG.betaRainforestBiome);
        settings.putString("savanna", ModernBeta.BIOME_CONFIG.betaSavannaBiome);
        settings.putString("shrubland", ModernBeta.BIOME_CONFIG.betaShrublandBiome);
        settings.putString("seasonal_forest", ModernBeta.BIOME_CONFIG.betaSeasonalForestBiome);
        settings.putString("swampland", ModernBeta.BIOME_CONFIG.betaSwamplandBiome);
        settings.putString("taiga", ModernBeta.BIOME_CONFIG.betaTaigaBiome);
        settings.putString("tundra", ModernBeta.BIOME_CONFIG.betaTundraBiome);
        
        settings.putString("ocean", ModernBeta.BIOME_CONFIG.betaOceanBiome);
        settings.putString("cold_ocean", ModernBeta.BIOME_CONFIG.betaColdOceanBiome);
        settings.putString("frozen_ocean", ModernBeta.BIOME_CONFIG.betaFrozenOceanBiome);
        settings.putString("lukewarm_ocean", ModernBeta.BIOME_CONFIG.betaLukewarmOceanBiome);
        settings.putString("warm_ocean", ModernBeta.BIOME_CONFIG.betaWarmOceanBiome);
        
        return settings;
    }
    
    public static NbtCompound createBiomeSettingsSingle(String biomeType) {
        NbtCompound settings = createBiomeSettingsBase(biomeType);
        
        settings.putString("singleBiome", ModernBeta.BIOME_CONFIG.singleBiome);

        return settings;
    }
    
    public static NbtCompound createBiomeSettingsVanilla(String biomeType) {
        NbtCompound settings = createBiomeSettingsBase(biomeType);
        
        settings.putInt("vanillaBiomeSize", ModernBeta.BIOME_CONFIG.vanillaBiomeSize);
        settings.putInt("vanillaOceanBiomeSize", ModernBeta.BIOME_CONFIG.vanillaOceanBiomeSize);
        
        return settings;
    }
}
