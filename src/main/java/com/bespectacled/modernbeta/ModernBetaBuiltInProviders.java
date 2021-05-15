package com.bespectacled.modernbeta;

import com.bespectacled.modernbeta.api.registry.*;
import com.bespectacled.modernbeta.gui.screen.world.*;
import com.bespectacled.modernbeta.gui.screen.biome.*;
import com.bespectacled.modernbeta.world.BuiltInWorldProviders;
import com.bespectacled.modernbeta.world.biome.provider.*;
import com.bespectacled.modernbeta.world.cavebiome.provider.*;
import com.bespectacled.modernbeta.world.gen.provider.*;

/*
 * Registration of built-in providers for various things.
 *  
 */
public class ModernBetaBuiltInProviders {
    
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
    
    // Register default biome providers
    public static void registerBiomeProviders() {
        ProviderRegistries.BIOME.register(BuiltInTypes.DEFAULT_ID, BetaBiomeProvider::new);
        ProviderRegistries.BIOME.register(BuiltInTypes.Biome.BETA.name, BetaBiomeProvider::new);
        ProviderRegistries.BIOME.register(BuiltInTypes.Biome.SINGLE.name, SingleBiomeProvider::new);
        ProviderRegistries.BIOME.register(BuiltInTypes.Biome.VANILLA.name, VanillaBiomeProvider::new);
    }
    
    // Register default cave biome providers
    public static void registerCaveBiomeProvider() {
        ProviderRegistries.CAVE_BIOME.register(BuiltInTypes.DEFAULT_ID, VanillaCaveBiomeProvider::new);
        ProviderRegistries.CAVE_BIOME.register(BuiltInTypes.CaveBiome.NONE.name, NoCaveBiomeProvider::new);
        ProviderRegistries.CAVE_BIOME.register(BuiltInTypes.CaveBiome.BETA.name, BetaCaveBiomeProvider::new);
        ProviderRegistries.CAVE_BIOME.register(BuiltInTypes.CaveBiome.SINGLE.name, SingleCaveBiomeProvider::new);
        ProviderRegistries.CAVE_BIOME.register(BuiltInTypes.CaveBiome.VANILLA.name, VanillaCaveBiomeProvider::new);
    }
    
    // Register default world screens
    public static void registerWorldScreens() {
        ProviderRegistries.WORLD_SCREEN.register(BuiltInTypes.DEFAULT_ID, BaseWorldScreen::new);
        ProviderRegistries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.BASE.name, BaseWorldScreen::new);
        ProviderRegistries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.INF.name, InfWorldScreen::new);
        ProviderRegistries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.INFDEV_227.name, Infdev227WorldScreen::new);
        ProviderRegistries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.INDEV.name, IndevWorldScreen::new);
        ProviderRegistries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.ISLAND.name, IslandWorldScreen::new);
    }
    
    // Register default biome settings screens (Note: Match identifiers with biome ids!)
    public static void registerBiomeScreens() {
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