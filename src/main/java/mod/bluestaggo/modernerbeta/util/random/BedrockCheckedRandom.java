package mod.bluestaggo.modernerbeta.util.random;

import com.google.common.annotations.VisibleForTesting;
import mod.bluestaggo.modernerbeta.util.random.mersenne.MersenneTwister;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.ThreadingDetector;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * An RNG implementation equivalent to that of Bedrock Edition.
 * <p>
 * This is a version of the M19937 Mersenne Twister algorithm.
 * This version contains the improved initialization from http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/MT2002/CODES/mt19937ar.c
 * It also contains an optimization to only generate some of the MT array when the RNG is first initialized.
 * <p>
 * Partially based on implementation by Earthcomputer, licensed under MIT license.
 */
public class BedrockCheckedRandom extends LegacyRandomSource {
    private static final int UPPER_MASK = 0x80000000;
    private static final double TWO_POW_M32 = 1.0 / (1L << 32);

    private final AtomicInteger seed = new AtomicInteger();
    private final MersenneTwister mt;
    private boolean haveNextNextGaussian;
    private float nextNextGaussian;

    private final boolean valid; // Hackfix for setSeed being called too early in the superconstructor

    public BedrockCheckedRandom(long seed) {
        super(0);
        mt = new MersenneTwister((int) seed);
        valid = true;
        setSeed(seed);
    }

    public int getSeed() {
        return seed.get();
    }

    @Override
    public RandomSource fork() {
        return new BedrockCheckedRandom(this.nextInt());
    }

    @Override
    public PositionalRandomFactory forkPositional() {
        return new Splitter(this.nextInt());
    }

    /**
     * This overload exists to override the method in the base class.
     * Although it accepts a 64-bit long, it will be cast to a 32-bit int.
     */
    @Override
    public void setSeed(long seed) {
        if (valid)
            setSeed((int) seed);
    }

    private void setSeed(int seed) {
        if (!this.seed.compareAndSet(this.seed.get(), seed)) {
            throw ThreadingDetector.makeThreadingException("BedrockCheckedRandom", null);
        } else {
            this.haveNextNextGaussian = false;
            this.nextNextGaussian = 0;
            this.mt.setSeed(seed);
        }
    }

    /**
     * Generates a non-negative signed integer
     */
    @Override
    public int nextInt() {
        return mt.genRandInt32() >>> 1;
    }

    @Override
    public int nextInt(int bound) {
        if (bound > 0)
            return (int) (Integer.toUnsignedLong(mt.genRandInt32()) % bound);
        else
            return 0;
    }

    @Override
    public boolean nextBoolean() {
        return (mt.genRandInt32() & UPPER_MASK) != 0;
    }

    /**
     * Generates a uniform random float k such that 0 <= k < 1
     */
    @Override
    public float nextFloat() {
        return (float) genRandReal2();
    }

    /**
     * Generates a uniform random double k such that 0 <= k < 1.
     * <p>
     * Note that unlike the Java RNG, there are only 2^32 possible return values for this function
     */
    @Override
    public double nextDouble() {
        return genRandReal2();
    }

    /**
     * Generates a Gaussian distributed float with mean 0 and standard deviation 1.
     * <p>
     * This method returns a double to override the method in the base class, but it is a
     * float that's generated, and it can be safely cast back to float without loss of precision.
     */
    @Override
    public double nextGaussian() {
        if (haveNextNextGaussian) {
            haveNextNextGaussian = false;
            return nextNextGaussian;
        }

        float v1, v2, s;
        do {
            v1 = nextFloat() * 2 - 1;
            v2 = nextFloat() * 2 - 1;
            s = v1 * v1 + v2 * v2;
        } while (s == 0 || s > 1);

        float multiplier = (float) Math.sqrt(-2 * (float) Math.log(s) / s);
        nextNextGaussian = v2 * multiplier;
        haveNextNextGaussian = true;
        return v1 * multiplier;
    }

    @Override
    public int next(int bits) {
        return mt.genRandInt32() >>> (32 - bits);
    }

    private double genRandReal2() {
        return Integer.toUnsignedLong(mt.genRandInt32()) * TWO_POW_M32;
    }

    public static class Splitter implements PositionalRandomFactory {
        private final int seed;

        public Splitter(int seed) {
            this.seed = seed;
        }

        @Override
        public RandomSource at(int x, int y, int z) {
            long posHash = Mth.getSeed(x, y, z);
            long seed = posHash ^ this.seed;
            return new BedrockCheckedRandom((int) seed);
        }

        @Override
        public RandomSource fromHashOf(String seed) {
            int i = seed.hashCode();
            return new BedrockCheckedRandom(i ^ this.seed);
        }

        //? if >=1.21 {
        @Override
        public RandomSource fromSeed(long seed) {
            return new BedrockCheckedRandom((int) seed);
        }
        //?}

        @VisibleForTesting
        @Override
        public void parityConfigString(StringBuilder info) {
            info.append("BedrockCheckedRandom.Splitter{").append(this.seed).append("}");
        }
    }
}