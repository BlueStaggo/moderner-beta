package com.bespectacled.modernbeta;

import com.bespectacled.modernbeta.api.registry.*;
import com.bespectacled.modernbeta.gui.screen.world.*;
import com.bespectacled.modernbeta.gui.screen.biome.*;
import com.bespectacled.modernbeta.world.BuiltInWorldProviders;
import com.bespectacled.modernbeta.world.biome.provider.*;
import com.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import com.bespectacled.modernbeta.world.cavebiome.provider.*;
import com.bespectacled.modernbeta.world.gen.provider.*;
import com.bespectacled.modernbeta.world.gen.provider.settings.*;

/*
 * Registration of built-in providers for various things.
 *  
 */
public class ModernBetaDefaultProviders {
    
    // Register default chunk providers
    public static void registerChunkProviders() {
        ProviderRegistries.CHUNK.register(BuiltInTypes.DEFAULT_ID, BetaChunkProvider::new);
        ProviderRegistries.CHUNK.register(BuiltInTypes.Chunk.BETA.name, BetaChunkProvider::new);
        ProviderRegistries.CHUNK.register(BuiltInTypes.Chunk.SKYLANDS.name, SkylandsChunkProvider::new);
        ProviderRegistries.CHUNK.register(BuiltInTypes.Chunk.ALPHA.name, AlphaChunkProvider::new);
        ProviderRegistries.CHUNK.register(BuiltInTypes.Chunk.INFDEV_611.name, Infdev611ChunkProvider::new);
        ProviderRegistries.CHUNK.register(BuiltInTypes.Chunk.INFDEV_415.name, Infdev415ChunkProvider::new);
        ProviderRegistries.CHUNK.register(BuiltInTypes.Chunk.INFDEV_227.name, Infdev227ChunkProvider::new);
        ProviderRegistries.CHUNK.register(BuiltInTypes.Chunk.INDEV.name, IndevChunkProvider::new);
        ProviderRegistries.CHUNK.register(BuiltInTypes.Chunk.BETA_ISLANDS.name, BetaIslandsChunkProvider::new);
    }
    
    // Register default chunk settings
    public static void registerChunkProviderSettings() {
        ProviderRegistries.CHUNK_SETTINGS.register(BuiltInTypes.DEFAULT_ID, ChunkProviderSettings::createSettingsInf);
        ProviderRegistries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.BETA.name, ChunkProviderSettings::createSettingsInf);
        ProviderRegistries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.SKYLANDS.name, ChunkProviderSettings::createSettingsInfNoOceans);
        ProviderRegistries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.ALPHA.name, ChunkProviderSettings::createSettingsInf);
        ProviderRegistries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.INFDEV_611.name, ChunkProviderSettings::createSettingsInf);
        ProviderRegistries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.INFDEV_415.name, ChunkProviderSettings::createSettingsInf);
        ProviderRegistries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.INFDEV_227.name, ChunkProviderSettings::createSettingsInfdev227);
        ProviderRegistries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.INDEV.name, ChunkProviderSettings::createSettingsIndev);
        ProviderRegistries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.BETA_ISLANDS.name, ChunkProviderSettings::createSettingsIslands);
    }
    
    // Register default biome providers
    public static void registerBiomeProviders() {
        ProviderRegistries.BIOME.register(BuiltInTypes.DEFAULT_ID, BetaBiomeProvider::new);
        ProviderRegistries.BIOME.register(BuiltInTypes.Biome.BETA.name, BetaBiomeProvider::new);
        ProviderRegistries.BIOME.register(BuiltInTypes.Biome.SINGLE.name, SingleBiomeProvider::new);
        ProviderRegistries.BIOME.register(BuiltInTypes.Biome.VANILLA.name, VanillaBiomeProvider::new);
    }
    
    // Register default biome settings
    public static void registerBiomeProviderSettings() {
        ProviderRegistries.BIOME_SETTINGS.register(BuiltInTypes.DEFAULT_ID, BiomeProviderSettings::createBiomeSettingsBase);
        ProviderRegistries.BIOME_SETTINGS.register(BuiltInTypes.Biome.BETA.name, BiomeProviderSettings::createBiomeSettingsBeta);
        ProviderRegistries.BIOME_SETTINGS.register(BuiltInTypes.Biome.SINGLE.name, BiomeProviderSettings::createBiomeSettingsSingle);
        ProviderRegistries.BIOME_SETTINGS.register(BuiltInTypes.Biome.VANILLA.name, BiomeProviderSettings::createBiomeSettingsVanilla);
    }
    
    // Register default cave biome providers
    public static void registerCaveBiomeProvider() {
        ProviderRegistries.CAVE_BIOME.register(BuiltInTypes.DEFAULT_ID, VanillaCaveBiomeProvider::new);
        ProviderRegistries.CAVE_BIOME.register(BuiltInTypes.CaveBiome.NONE.name, NoCaveBiomeProvider::new);
        ProviderRegistries.CAVE_BIOME.register(BuiltInTypes.CaveBiome.SINGLE.name, SingleCaveBiomeProvider::new);
        ProviderRegistries.CAVE_BIOME.register(BuiltInTypes.CaveBiome.VANILLA.name, VanillaCaveBiomeProvider::new);
    }
    
    // Register default screen providers
    public static void registerWorldScreenProviders() {
        ProviderRegistries.WORLD_SCREEN.register(BuiltInTypes.DEFAULT_ID, BaseWorldScreen::new);
        ProviderRegistries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.BASE.name, BaseWorldScreen::new);
        ProviderRegistries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.INF.name, InfWorldScreen::new);
        ProviderRegistries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.INFDEV_227.name, Infdev227WorldScreen::new);
        ProviderRegistries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.INDEV.name, IndevWorldScreen::new);
        ProviderRegistries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.ISLAND.name, IslandWorldScreen::new);
    }
    
    // Register default settings screen actions (Note: Match identifiers with biome ids!)
    public static void registerBiomeScreenProviders() {
        ProviderRegistries.BIOME_SCREEN.register(BuiltInTypes.DEFAULT_ID, screen -> null);
        ProviderRegistries.BIOME_SCREEN.register(BuiltInTypes.Biome.BETA.name, BetaBiomeScreen::create);
        ProviderRegistries.BIOME_SCREEN.register(BuiltInTypes.Biome.SINGLE.name, SingleBiomeScreen::create);
        ProviderRegistries.BIOME_SCREEN.register(BuiltInTypes.Biome.VANILLA.name, VanillaBiomeScreen::create);
    }
    
    // Register default world providers
    public static void registerWorldProviders() {
        ProviderRegistries.WORLD.register(BuiltInTypes.DEFAULT_ID, BuiltInWorldProviders.BETA);
        ProviderRegistries.WORLD.register(BuiltInTypes.Chunk.BETA.name, BuiltInWorldProviders.BETA);
        ProviderRegistries.WORLD.register(BuiltInTypes.Chunk.SKYLANDS.name, BuiltInWorldProviders.SKYLANDS);
        ProviderRegistries.WORLD.register(BuiltInTypes.Chunk.ALPHA.name, BuiltInWorldProviders.ALPHA);
        ProviderRegistries.WORLD.register(BuiltInTypes.Chunk.INFDEV_611.name, BuiltInWorldProviders.INFDEV_611);
        ProviderRegistries.WORLD.register(BuiltInTypes.Chunk.INFDEV_415.name, BuiltInWorldProviders.INFDEV_415);
        ProviderRegistries.WORLD.register(BuiltInTypes.Chunk.INFDEV_227.name, BuiltInWorldProviders.INFDEV_227);
        ProviderRegistries.WORLD.register(BuiltInTypes.Chunk.INDEV.name, BuiltInWorldProviders.INDEV);
        ProviderRegistries.WORLD.register(BuiltInTypes.Chunk.BETA_ISLANDS.name, BuiltInWorldProviders.BETA_ISLANDS);
    }
}
