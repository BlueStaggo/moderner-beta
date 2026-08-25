package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.intlayers;

import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.LayerRandom;

public class IntZoomLayer extends IntLayer {
    private final IntLayer parent;

    public IntZoomLayer(long seed, IntLayer parent) {
        super(seed);
        this.parent = parent;
    }

    @Override
    public void init(long worldSeed) {
        this.parent.init(worldSeed);
        super.init(worldSeed);
    }

    @Override
    public int generate(int x, int z) {
        int xHalf = x & 1;
        int zHalf = z & 1;

        int halfX = x >> 1;
        int halfZ = z >> 1;

        int n00 = this.parent.sample(halfX, halfZ);
        if (xHalf == 0 && zHalf == 0) {
            return n00;
        }

        LayerRandom random = this.getRandom(halfX << 1, halfZ << 1);
        int n01 = this.parent.sample(halfX, halfZ + 1);
        int interpolationResult = n00 + (n01 - n00) * random.nextInt(256) / 256;
        if (xHalf == 0) {
            return interpolationResult;
        }

        int n10 = this.parent.sample(halfX + 1, halfZ);
        interpolationResult = n00 + (n10 - n00) * random.nextInt(256) / 256;
        if (zHalf == 0) {
            return interpolationResult;
        }

        int n11 = this.parent.sample(halfX + 1, halfZ + 1);

        int n0 = n00 + (n01 - n00) * random.nextInt(256) / 256;
        int n1 = n10 + (n11 - n10) * random.nextInt(256) / 256;

        return n0 + (n1 - n0) * random.nextInt(256) / 256;
    }
}
