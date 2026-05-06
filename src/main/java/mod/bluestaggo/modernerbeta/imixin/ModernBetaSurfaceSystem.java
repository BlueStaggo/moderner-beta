package mod.bluestaggo.modernerbeta.imixin;

import mod.bluestaggo.modernerbeta.api.level.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProvider;

public interface ModernBetaSurfaceSystem {
    void modernerBeta$setupChunkContext(ChunkProvider chunkProvider);
    void modernerBeta$setupBiomeContext(BiomeProvider biomeProvider);
    ChunkProvider modernerBeta$getContext();
}
