package mod.bespectacled.modernbeta;

import mod.bespectacled.modernbeta.api.registry.Registries;
import mod.bespectacled.modernbeta.api.world.chunk.noise.NoisePostProcessor;
import mod.bespectacled.modernbeta.world.biome.provider.BetaBiomeProvider;
import mod.bespectacled.modernbeta.world.biome.provider.PEBiomeProvider;
import mod.bespectacled.modernbeta.world.biome.provider.SingleBiomeProvider;
import mod.bespectacled.modernbeta.world.cavebiome.provider.NoCaveBiomeProvider;
import mod.bespectacled.modernbeta.world.cavebiome.provider.SingleCaveBiomeProvider;
import mod.bespectacled.modernbeta.world.cavebiome.provider.VoronoiCaveBiomeProvider;
import mod.bespectacled.modernbeta.world.chunk.provider.AlphaChunkProvider;
import mod.bespectacled.modernbeta.world.chunk.provider.BetaChunkProvider;
import mod.bespectacled.modernbeta.world.chunk.provider.Classic030ChunkProvider;
import mod.bespectacled.modernbeta.world.chunk.provider.IndevChunkProvider;
import mod.bespectacled.modernbeta.world.chunk.provider.Infdev227ChunkProvider;
import mod.bespectacled.modernbeta.world.chunk.provider.Infdev415ChunkProvider;
import mod.bespectacled.modernbeta.world.chunk.provider.Infdev420ChunkProvider;
import mod.bespectacled.modernbeta.world.chunk.provider.Infdev611ChunkProvider;
import mod.bespectacled.modernbeta.world.chunk.provider.PEChunkProvider;
import mod.bespectacled.modernbeta.world.chunk.provider.SkylandsChunkProvider;

/*
 * Registration of built-in providers for various things.
 *  
 */
public class ModernBetaBuiltInProviders {
    
    // Register default chunk providers
    public static void registerChunkProviders() {
        Registries.CHUNK.register(ModernBetaBuiltInTypes.DEFAULT_ID, BetaChunkProvider::new);
        Registries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.BETA.name, BetaChunkProvider::new);
        Registries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.SKYLANDS.name, SkylandsChunkProvider::new);
        Registries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.ALPHA.name, AlphaChunkProvider::new);
        Registries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.INFDEV_611.name, Infdev611ChunkProvider::new);
        Registries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.INFDEV_420.name, Infdev420ChunkProvider::new);
        Registries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.INFDEV_415.name, Infdev415ChunkProvider::new);
        Registries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.INFDEV_227.name, Infdev227ChunkProvider::new);
        Registries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.INDEV.name, IndevChunkProvider::new);
        Registries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.CLASSIC_0_30.name, Classic030ChunkProvider::new);
        Registries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.PE.name, PEChunkProvider::new);
    }
    
    // Register default biome providers
    public static void registerBiomeProviders() {
        Registries.BIOME.register(ModernBetaBuiltInTypes.DEFAULT_ID, BetaBiomeProvider::new);
        Registries.BIOME.register(ModernBetaBuiltInTypes.Biome.BETA.name, BetaBiomeProvider::new);
        Registries.BIOME.register(ModernBetaBuiltInTypes.Biome.SINGLE.name, SingleBiomeProvider::new);
        Registries.BIOME.register(ModernBetaBuiltInTypes.Biome.PE.name, PEBiomeProvider::new);
    }
    
    // Register default cave biome providers
    public static void registerCaveBiomeProvider() {
        Registries.CAVE_BIOME.register(ModernBetaBuiltInTypes.DEFAULT_ID, NoCaveBiomeProvider::new);
        Registries.CAVE_BIOME.register(ModernBetaBuiltInTypes.CaveBiome.NONE.name, NoCaveBiomeProvider::new);
        Registries.CAVE_BIOME.register(ModernBetaBuiltInTypes.CaveBiome.SINGLE.name, SingleCaveBiomeProvider::new);
        Registries.CAVE_BIOME.register(ModernBetaBuiltInTypes.CaveBiome.VORONOI.name, VoronoiCaveBiomeProvider::new);
    }
    
    public static void registerNoisePostProcessors() {
        Registries.NOISE_POST_PROCESSORS.register(ModernBetaBuiltInTypes.NoisePostProcessor.NONE.name, NoisePostProcessor.DEFAULT);
    }
}