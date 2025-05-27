package mod.bluestaggo.modernerbeta.api.world.provider;

import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;

@FunctionalInterface
public interface ChunkProviderCreator {
    ChunkProvider apply(ModernBetaChunkGenerator chunkGenerator, long seed);
}
