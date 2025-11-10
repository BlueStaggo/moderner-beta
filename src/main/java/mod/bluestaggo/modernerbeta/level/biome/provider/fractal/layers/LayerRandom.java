package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import net.minecraft.util.LinearCongruentialGenerator;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.MarsagliaPolarGaussian;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

import java.util.List;

public class LayerRandom implements RandomSource {
    private final long baseSeed;
    private long seed;
    private MarsagliaPolarGaussian gaussianGenerator;

    public LayerRandom(long seed) {
        this.baseSeed = seed;
    }

    public void init(long x, long z) {
        this.seed = this.baseSeed;
        for (int i = 0; i < 2; i++) {
            this.seed = LinearCongruentialGenerator.next(this.seed, x);
            this.seed = LinearCongruentialGenerator.next(this.seed, z);
        }
    }

    @Override
    public RandomSource fork() {
        LayerRandom random = new LayerRandom(0);
        random.seed = this.seed;
        return random;
    }

    @Override
    public PositionalRandomFactory forkPositional() {
        return null;
    }

    @Override
    public void setSeed(long seed) {
        this.seed = seed;
    }

    @Override
    public int nextInt() {
        int result = (int)(this.seed >> 24);
        this.seed = LinearCongruentialGenerator.next(this.seed, this.baseSeed);
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
        this.seed = LinearCongruentialGenerator.next(this.seed, this.baseSeed);
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
            this.gaussianGenerator = new MarsagliaPolarGaussian(this);
        }
        return this.gaussianGenerator.nextGaussian();
    }

    @Override
    public void consumeCount(int count) {
        for (int i = 0; i < count; i++) {
            this.seed = LinearCongruentialGenerator.next(this.seed, this.baseSeed);
        }
    }

    public <T> T nextItem(List<T> list) {
        return list.get(this.nextInt(list.size()));
    }
}
