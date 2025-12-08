package mod.bluestaggo.modernerbeta.level.feature.placement.noise;


import mod.bluestaggo.modernerbeta.settings.component.PerlinNoiseSettings;
import mod.bluestaggo.modernerbeta.util.noise.PerlinOctaveNoise;
import net.minecraft.util.RandomSource;

public class NoiseBasedCountInfdev325 implements NoiseBasedCount {
    private final PerlinOctaveNoise noiseSampler;

    public NoiseBasedCountInfdev325(RandomSource random) {
        this.noiseSampler = new PerlinOctaveNoise(new java.util.Random(random.nextLong()), 5, PerlinNoiseSettings.INFDEV_415);
    }

    public NoiseBasedCountInfdev325(PerlinOctaveNoise noiseSampler) {
        this.noiseSampler = noiseSampler;
    }

    @Override
    public int sample(int chunkX, int chunkZ, RandomSource random) {
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        
        double scale = 0.0625D;

        return (int) this.noiseSampler.sampleXY(startX * scale, startZ * scale) << 3;
    }
}
