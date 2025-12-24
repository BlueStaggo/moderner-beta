package mod.bluestaggo.modernerbeta.api.level.chunk.noise;

public class NoiseProviderBase extends NoiseProvider {
    private final BaseColumnSampler bufferSampler;
    private final DensityModifier densityModifier;

    private int startNoiseX;
    private int startNoiseZ;

    protected double[] heightmapNoise;

    public NoiseProviderBase(
        int noiseSizeX, 
        int noiseSizeY, 
        int noiseSizeZ,
        BaseColumnSampler bufferSampler,
        DensityModifier densityModifier
    ) {
        super(noiseSizeX, noiseSizeY, noiseSizeZ);

        this.bufferSampler = bufferSampler;
        this.densityModifier = densityModifier;
    }

    @Override
    public NoiseSampler getSampler() {
        if (this.densityModifier == null) {
            return super.getSampler();
        }
        return new ModifiedNoiseSampler(this.noiseSizeX, this.noiseSizeY, this.noiseSizeZ, this.noise,
            this.startNoiseX, this.startNoiseZ, this.densityModifier);
    }

    public NoiseSampler getSamplerForHeightmap() {
        if (this.densityModifier == null) {
            return new NoiseSampler(this.noiseSizeX, this.noiseSizeY, this.noiseSizeZ, this.heightmapNoise);
        }
        return new ModifiedNoiseSampler(this.noiseSizeX, this.noiseSizeY, this.noiseSizeZ, this.heightmapNoise,
            this.startNoiseX, this.startNoiseZ, this.densityModifier);
    }

    @Override
    protected double[] sampleNoise(int startNoiseX, int startNoiseZ) {
        this.startNoiseX = startNoiseX;
        this.startNoiseZ = startNoiseZ;

        double[] primaryBuffer = new double[this.noiseResY];
        double[] heightmapBuffer = new double[this.noiseResY];
        
        double[] noise = new double[this.noiseSize];
        double[] heightmapNoise = new double[this.noiseSize];
        
        int ndx = 0;
        for (int localNoiseX = 0; localNoiseX < this.noiseResX; ++localNoiseX) {
            for (int localNoiseZ = 0; localNoiseZ < this.noiseResZ; ++localNoiseZ) {
                this.bufferSampler.sampleColumn(primaryBuffer, heightmapBuffer, startNoiseX, startNoiseZ, localNoiseX, localNoiseZ);

                System.arraycopy(primaryBuffer, 0, noise, ndx, this.noiseResY);
                System.arraycopy(heightmapBuffer, 0, heightmapNoise, ndx, this.noiseResY);
                ndx += this.noiseResY;
            }
        }
        
        this.heightmapNoise = heightmapNoise;
        
        return noise;
    }

    @FunctionalInterface
    public interface BaseColumnSampler {
        void sampleColumn(double[] primaryBuffer, double[] heightmapBuffer, int startNoiseX, int startNoiseZ, int localNoiseX, int localNoiseZ);
    }

    @FunctionalInterface
    public interface DensityModifier {
        double modify(double density, double x, double y, double z);
    }
}
