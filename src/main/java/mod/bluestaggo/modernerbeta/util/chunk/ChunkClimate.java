package mod.bluestaggo.modernerbeta.util.chunk;

import mod.bluestaggo.modernerbeta.api.level.biome.climate.Clime;
import mod.bluestaggo.modernerbeta.util.function.BiIntegerFunction;

public class ChunkClimate {
    private final Clime[] climes = new Clime[256];
    
    public ChunkClimate(int chunkX, int chunkZ, BiIntegerFunction<Clime> chunkFunc) {
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        
        int ndx = 0;
        for (int x = startX; x < startX + 16; ++x) {
            for (int z = startZ; z < startZ + 16; ++z) {
                this.climes[ndx++] = chunkFunc.apply(x, z);
            }
        }
    }
    
    public Clime sampleClime(int x, int z) {
        return climes[(x & 0xF) << 4 | (z & 0xF)];
    }
}
