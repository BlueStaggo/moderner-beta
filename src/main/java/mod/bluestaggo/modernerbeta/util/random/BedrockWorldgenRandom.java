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
        long randX = this.nextInt() >>> 1;
        long randZ = this.nextInt() >>> 1;
        long seed = chunkX * randX ^ chunkZ * randZ ^ worldSeed;
        this.setSeed(seed);
    }
}
