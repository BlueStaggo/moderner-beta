package mod.bluestaggo.modernerbeta.util.random;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;

public class BedrockWorldgenRandom extends WorldgenRandom {
    public BedrockWorldgenRandom(RandomSource baseRandom) {
        super(baseRandom);
    }

    @Override
    public void setLargeFeatureSeed(long worldSeed, int chunkX, int chunkZ) {
        this.setSeed(worldSeed);
        int randX = this.nextInt();
        int randZ = this.nextInt();
        long seed = (long)chunkX * randX ^ (long)chunkZ * randZ ^ worldSeed;
        this.setSeed(seed);
    }
}
