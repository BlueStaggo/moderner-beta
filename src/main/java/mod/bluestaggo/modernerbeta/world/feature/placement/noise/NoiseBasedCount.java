package mod.bluestaggo.modernerbeta.world.feature.placement.noise;

import net.minecraft.util.RandomSource;

public interface NoiseBasedCount {
    int sample(int chunkX, int chunkZ, RandomSource random);
}
