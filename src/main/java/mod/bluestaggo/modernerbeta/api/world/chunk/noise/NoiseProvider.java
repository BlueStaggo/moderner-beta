package mod.bluestaggo.modernerbeta.api.world.chunk.noise;

public abstract class NoiseProvider {
    protected final int noiseSizeX;
    protected final int noiseSizeY;
    protected final int noiseSizeZ;
    
    protected final int noiseResX;
    protected final int noiseResY;
    protected final int noiseResZ;
    
    protected final int noiseSize;
    
    protected double[] noise;

    public NoiseProvider(
        int noiseSizeX, 
        int noiseSizeY, 
        int noiseSizeZ
    ) {
        this.noiseSizeX = noiseSizeX;
        this.noiseSizeY = noiseSizeY;
        this.noiseSizeZ = noiseSizeZ;
        
        this.noiseResX = noiseSizeX + 1;
        this.noiseResY = noiseSizeY + 1;
        this.noiseResZ = noiseSizeZ + 1;
        
        this.noiseSize = this.noiseResX * this.noiseResY * this.noiseResZ;
    }

    public NoiseSampler getSampler() {
        return new NoiseSampler(this.noiseSizeX, this.noiseSizeY, this.noiseSizeZ, this.noise);
    }
    
    public void sampleInitialNoise(int startNoiseX, int startNoiseZ) {
        this.noise = this.sampleNoise(startNoiseX, startNoiseZ);
        
        if (this.noise.length != this.noiseSize)
            throw new IllegalStateException("[Modern Beta] Noise array length is invalid!");
    }
    
    protected abstract double[] sampleNoise(int startNoiseX, int startNoiseZ);
}
