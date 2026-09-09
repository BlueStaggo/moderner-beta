package mod.bluestaggo.modernerbeta.imixin;

import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProvider;

public interface ModernBetaSurfaceContext {
    ChunkProvider modernerBeta$getChunkProvider();
    int modernerBeta$getBlockY();
    int modernerBeta$getStoneDepthAbove();
    int modernerBeta$getSurfaceDepth();
}
