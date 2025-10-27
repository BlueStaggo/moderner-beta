package mod.bluestaggo.modernerbeta.world.chunk.provider;

import mod.bluestaggo.modernerbeta.api.world.spawn.SpawnLocator;
import mod.bluestaggo.modernerbeta.util.random.mersenne.MTRandom;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bluestaggo.modernerbeta.world.spawn.SpawnLocatorRelease;

import java.util.Random;

public class ChunkProviderEarlyBedrock extends ChunkProviderNoise3D {
    public ChunkProviderEarlyBedrock(ModernBetaChunkGenerator chunkGenerator, long seed) {
        super(chunkGenerator, seed);
    }

    @Override
    public SpawnLocator getSpawnLocator() {
        return new SpawnLocatorRelease(this, new MTRandom(this.seed));
    }

    /*
     * MCPE uses different values to seed random surface generation.
     */
    @Override
    protected Random createSurfaceRandom(int chunkX, int chunkZ) {
        long seed = (long)chunkX * 0x14609048 + (long)chunkZ * 0x7ebe2d5;

        return new MTRandom(seed);
    }

    @Override
    protected int getHeightSampleRadius() {
        return 1;
    }

    @Override
    protected float calculateBiomeHeightWeight(int x, int z) {
        return super.calculateBiomeHeightWeight(-1, z);
    }
}
