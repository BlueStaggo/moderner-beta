package mod.bluestaggo.modernerbeta.level.feature.placement.noise;

import net.minecraft.util.RandomSource;

public interface NoiseBasedCount {
    int sample(int chunkX, int chunkZ, RandomSource random);
}
