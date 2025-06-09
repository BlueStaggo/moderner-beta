package mod.bluestaggo.modernerbeta.api.world.chunk.noise;

public class NoiseProviderBase extends NoiseProvider {
    private final BaseColumnSampler bufferSampler;
    
    protected double[] heightmapNoise;

    public NoiseProviderBase(
        int noiseSizeX, 
        int noiseSizeY, 
        int noiseSizeZ, 
        BaseColumnSampler bufferSampler
    ) {
        super(noiseSizeX, noiseSizeY, noiseSizeZ);
        
        this.bufferSampler = bufferSampler;
    }

    public NoiseSampler getSamplerForHeightmap() {
        return new NoiseSampler(this.noiseSizeX, this.noiseSizeY, this.noiseSizeZ, this.heightmapNoise);
    }

    @Override
    protected double[] sampleNoise(int startNoiseX, int startNoiseZ) {
        double[] primaryBuffer = new double[this.noiseResY];
        double[] heightmapBuffer = new double[this.noiseResY];
        
        double[] noise = new double[this.noiseSize];
        double[] heightmapNoise = new double[this.noiseSize];
        
        int ndx = 0;
        for (int localNoiseX = 0; localNoiseX < this.noiseResX; ++localNoiseX) {
            for (int localNoiseZ = 0; localNoiseZ < this.noiseResZ; ++localNoiseZ) {
                this.bufferSampler.sampleColumn(primaryBuffer, heightmapBuffer, startNoiseX, startNoiseZ, localNoiseX, localNoiseZ);
                
                for (int nY = 0; nY < this.noiseResY; ++nY) {
                    noise[ndx] = primaryBuffer[nY];
                    heightmapNoise[ndx] = heightmapBuffer[nY];
                            
                    ndx++;
                }
            }
        }
        
        this.heightmapNoise = heightmapNoise;
        
        return noise;
    }

    @FunctionalInterface
    public interface BaseColumnSampler {
        void sampleColumn(double[] primaryBuffer, double[] heightmapBuffer, int startNoiseX, int startNoiseZ, int localNoiseX, int localNoiseZ);
    }
}
