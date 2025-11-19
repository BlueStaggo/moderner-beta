package mod.bluestaggo.modernerbeta.level.feature.placement.noise;

import mod.bluestaggo.modernerbeta.util.noise.PerlinOctaveNoise;
import net.minecraft.util.RandomSource;

public class NoiseBasedCountInfdev420 implements NoiseBasedCount {
    private final PerlinOctaveNoise noiseSampler;
    
    public NoiseBasedCountInfdev420(RandomSource random) {
        this.noiseSampler = new PerlinOctaveNoise(new java.util.Random(random.nextLong()), 5, true);
    }
    
    public NoiseBasedCountInfdev420(PerlinOctaveNoise noiseSampler) {
        this.noiseSampler = noiseSampler;
    }

    @Override
    public int sample(int chunkX, int chunkZ, RandomSource random) {
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        
        double scale = 0.05D;
        
        int noiseCount = (int) (this.noiseSampler.sampleXY(startX * scale, startZ * scale) - random.nextDouble());
        
        if (noiseCount < 0) {
            noiseCount = 0;
        }
        
        return noiseCount;
    }
}
