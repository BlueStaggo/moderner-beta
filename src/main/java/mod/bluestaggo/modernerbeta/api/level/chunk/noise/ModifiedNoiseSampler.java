package mod.bluestaggo.modernerbeta.api.level.chunk.noise;

public class ModifiedNoiseSampler extends NoiseSampler {
    private final int startNoiseX;
    private final int startNoiseZ;
    private final NoiseProviderBase.DensityModifier densityModifier;

    private int subChunkX;
    private int subChunkY;
    private int subChunkZ;

    private double deltaX;
    private double deltaY;
    private double deltaZ;

    public ModifiedNoiseSampler(int noiseSizeX, int noiseSizeY, int noiseSizeZ, double[] noise,
                                int startNoiseX, int startNoiseZ, NoiseProviderBase.DensityModifier densityModifier) {
        super(noiseSizeX, noiseSizeY, noiseSizeZ, noise);
        this.startNoiseX = startNoiseX;
        this.startNoiseZ = startNoiseZ;
        this.densityModifier = densityModifier;
    }

    @Override
    public void sampleNoiseCorners(int subChunkX, int subChunkY, int subChunkZ) {
        super.sampleNoiseCorners(subChunkX, subChunkY, subChunkZ);
        this.subChunkX = subChunkX;
        this.subChunkY = subChunkY;
        this.subChunkZ = subChunkZ;
    }

    @Override
    public void sampleNoiseY(double deltaY) {
        super.sampleNoiseY(deltaY);
        this.deltaY = deltaY;
    }

    @Override
    public void sampleNoiseX(double deltaX) {
        super.sampleNoiseX(deltaX);
        this.deltaX = deltaX;
    }

    @Override
    public void sampleNoiseZ(double deltaZ) {
        super.sampleNoiseZ(deltaZ);
        this.deltaZ = deltaZ;
    }

    public double sample() {
        return this.densityModifier.modify(
            super.sample(),
            this.startNoiseX + this.subChunkX + this.deltaX,
            this.subChunkY + this.deltaY,
            this.startNoiseZ + this.subChunkZ + this.deltaZ
        );
    }
}
