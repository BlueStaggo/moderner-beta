package mod.bluestaggo.modernerbeta.util.random;

import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.Random;

public class BedrockChunkRandom extends ChunkRandom {
    public BedrockChunkRandom(Random baseRandom) {
        super(baseRandom);
    }

    @Override
    public void setCarverSeed(long worldSeed, int chunkX, int chunkZ) {
        this.setSeed(worldSeed);
        int randX = this.nextInt();
        int randZ = this.nextInt();
        long seed = (long)chunkX * randX ^ (long)chunkZ * randZ ^ worldSeed;
        this.setSeed(seed);
    }
}
