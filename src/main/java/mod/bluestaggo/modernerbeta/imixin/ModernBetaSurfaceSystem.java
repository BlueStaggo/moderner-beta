package mod.bluestaggo.modernerbeta.imixin;

import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;

public interface ModernBetaSurfaceSystem {
    void modernerBeta$setupChunkContext(ChunkProvider chunkProvider);
    void modernerBeta$setupBiomeContext(ModernBetaBiomeSource biomeSource);
    ChunkProvider modernerBeta$getContext();
}
