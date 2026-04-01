package mod.bluestaggo.modernerbeta.util.random;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;

public class BedrockWorldgenRandom extends WorldgenRandom {
    public BedrockWorldgenRandom(RandomSource baseRandom) {
        super(baseRandom);
    }

    @Override
    public void setLargeFeatureSeed(long worldSeed, int chunkX, int chunkZ) {
        this.setSeed((int) worldSeed);
        int randX = this.nextInt() | 1;
        int randZ = this.nextInt() | 1;
        int seed = chunkX * randX ^ chunkZ * randZ ^ (int) worldSeed;
        this.setSeed(seed);
    }
}
