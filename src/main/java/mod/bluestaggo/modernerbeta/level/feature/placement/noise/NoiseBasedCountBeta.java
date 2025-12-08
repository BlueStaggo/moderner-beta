package mod.bluestaggo.modernerbeta.level.feature.placement.noise;


import mod.bluestaggo.modernerbeta.settings.component.PerlinNoiseSettings;
import mod.bluestaggo.modernerbeta.util.noise.PerlinOctaveNoise;
import net.minecraft.util.RandomSource;

public class NoiseBasedCountBeta implements NoiseBasedCount {
    private final PerlinOctaveNoise noiseSampler;
    
    public NoiseBasedCountBeta(RandomSource random) {
        this.noiseSampler = new PerlinOctaveNoise(new java.util.Random(random.nextLong()), 8, PerlinNoiseSettings.DEFAULT);
    }
    
    public NoiseBasedCountBeta(PerlinOctaveNoise noiseSampler) {
        this.noiseSampler = noiseSampler;
    }

    @Override
    public int sample(int chunkX, int chunkZ, RandomSource random) {
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        
        double scale = 0.5D;

        return (int) ((this.noiseSampler.sampleXY(startX * scale, startZ * scale) / 8D + random.nextDouble() * 4D + 4D) / 3D);
    }
}
