package mod.bluestaggo.modernerbeta.imixin;

import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;

public interface ModernBetaSurfaceSystem {
    void modernerBeta$setupChunkContext(ChunkProvider chunkProvider);
    void modernerBeta$setupBiomeContext(ModernBetaBiomeSource biomeSource);
    void modernerBeta$beforeSurfaceBuild(ChunkAccess chunk);
    ChunkProvider modernerBeta$getContext();
}
