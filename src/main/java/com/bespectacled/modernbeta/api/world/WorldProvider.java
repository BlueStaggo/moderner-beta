package com.bespectacled.modernbeta.api.world;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.gui.WorldScreen;
import com.bespectacled.modernbeta.api.registry.ProviderRegistries;
import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public final class WorldProvider {
    private final String chunkProvider;
    private final String chunkGenSettings;
    private final String worldScreen;

    private final String biomeProvider;
    private final String caveBiomeProvider;
    private final String singleBiome;

    public WorldProvider(
        String chunkProvider,
        String chunkGenSettings,
        String biomeProvider,
        String caveBiomeProvider,
        String singleBiome,
        String worldScreen
    ) {
        this.chunkProvider = chunkProvider;
        this.chunkGenSettings = chunkGenSettings;
        this.worldScreen = worldScreen;
        
        this.biomeProvider = biomeProvider;
        this.caveBiomeProvider = caveBiomeProvider;
        this.singleBiome = singleBiome;
    }
    
    public String getChunkProvider() {
        return this.chunkProvider;
    }
    
    public String getChunkGenSettings() {
        return this.chunkGenSettings;
    }
    
    public String getBiomeProvider() {
        return this.biomeProvider;
    }
    
    public String getCaveBiomeProvider() {
        return this.caveBiomeProvider;
    }
    
    public String getSingleBiome() {
        return this.singleBiome;
    }
    
    public String getWorldScreen() {
        return this.worldScreen;
    }
    
    public ChunkProvider createChunkProvider(
        long seed, 
        ChunkGenerator chunkGenerator, 
        Supplier<ChunkGeneratorSettings> generatorSettings, 
        NbtCompound providerSettings
    ) {
        return ProviderRegistries.CHUNK
            .get(this.chunkProvider)
            .apply(seed, chunkGenerator, generatorSettings, providerSettings);
    }
    
    public WorldScreen createLevelScreen(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager,
        WorldSettings worldSettings,
        Consumer<WorldSettings> consumer
    ) {
        return ProviderRegistries.WORLD_SCREEN
            .getOrDefault(this.worldScreen)
            .apply(parent, registryManager, worldSettings, consumer);
    }
}