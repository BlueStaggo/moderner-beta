package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import net.minecraft.util.math.random.GaussianGenerator;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.random.RandomSplitter;
import net.minecraft.world.biome.source.SeedMixer;

import java.util.List;

public class LayerRandom implements Random {
    private final long baseSeed;
    private long seed;
    private GaussianGenerator gaussianGenerator;

    public LayerRandom(long seed) {
        this.baseSeed = seed;
    }

    public void init(long x, long z) {
        this.seed = this.baseSeed;
        for (int i = 0; i < 2; i++) {
            this.seed = SeedMixer.mixSeed(this.seed, x);
            this.seed = SeedMixer.mixSeed(this.seed, z);
        }
    }

    @Override
    public Random split() {
        LayerRandom random = new LayerRandom(0);
        random.seed = this.seed;
        return random;
    }

    @Override
    public RandomSplitter nextSplitter() {
        return null;
    }

    @Override
    public void setSeed(long seed) {
        this.seed = seed;
    }

    @Override
    public int nextInt() {
        int result = (int)(this.seed >> 24);
        this.seed = SeedMixer.mixSeed(this.seed, this.baseSeed);
        return result;
    }

    public int nextInt(int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("Bound must be positive");
        }

        int result = (int)((this.seed >> 24) % (long)bound);
        if (result < 0) {
            result += bound;
        }
        this.seed = SeedMixer.mixSeed(this.seed, this.baseSeed);
        return result;
    }

    @Override
    public long nextLong() {
        return (long)this.nextInt() << 32 | (long)this.nextInt();
    }

    @Override
    public boolean nextBoolean() {
        return this.nextInt(2) == 1;
    }

    @Override
    public float nextFloat() {
        return (this.nextInt() >>> 8) * 5.9604645E-8F;
    }

    @Override
    public double nextDouble() {
        return (this.nextInt() >>> 11) * 1.110223E-16F;
    }

    @Override
    public double nextGaussian() {
        if (this.gaussianGenerator == null) {
            this.gaussianGenerator = new GaussianGenerator(this);
        }
        return this.gaussianGenerator.next();
    }

    public <T> T nextItem(List<T> list) {
        return list.get(this.nextInt(list.size()));
    }
}
