package mod.bluestaggo.modernerbeta.util.random.mersenne;

/**
 * Implementation of Mersenne Twister MT19937.
 * 
 * Based on:
 * - http://www.math.sci.hiroshima-u.ac.jp/m-mat/MT/VERSIONS/C-LANG/980409/mt19937int.c
 * - http://www.math.sci.hiroshima-u.ac.jp/m-mat/MT/MT2002/CODES/mt19937ar.c
 * - http://www.java2s.com/Code/Java/Development-Class/AJavaimplementationoftheMT19937MersenneTwisterpseudorandomnumbergeneratoralgorithm.htm
 *
 */
public class MersenneTwister {
    private static final int N = 624;
    private static final int M = 397;
    private static final int MATRIX_A = 0x9908b0df;
    private static final int UPPER_MASK = 0x80000000;
    private static final int LOWER_MASK = 0x7fffffff;
    private static final int DEFAULT_SEED = 4357;
    private static final int[] MAG_01 = { 0x0, MATRIX_A };
    
    private final int[] mt;
    public int mti;
    private int mtiFast;
    
    public MersenneTwister() {
        this(DEFAULT_SEED);
    }
    
    public MersenneTwister(int seed) {
        this.mt = new int[N];
        this.setSeed(seed);
    }

    public synchronized void setSeed(int seed) {
        this.mti = N + 1; // uninitialized
        this.initFast(seed);
    }
    
    public synchronized int genRandInt32() {
        if (this.mti == N) {
            this.mti = 0;
        } else if (this.mti > N) {
            init(5489);
            this.mti = 0;
        }

        if (this.mti >= N - M) {
            if (this.mti >= N - 1) {
                this.mt[N - 1] = MAG_01[this.mt[0] & 1]
                        ^ ((this.mt[0] & LOWER_MASK | this.mt[N - 1] & UPPER_MASK) >>> 1)
                        ^ this.mt[M - 1];
            } else {
                this.mt[this.mti] = MAG_01[this.mt[this.mti + 1] & 1]
                        ^ ((this.mt[this.mti + 1] & LOWER_MASK | this.mt[this.mti] & UPPER_MASK) >>> 1)
                        ^ this.mt[this.mti - (N - M)];
            }
        } else {
            this.mt[this.mti] = MAG_01[this.mt[this.mti + 1] & 1]
                    ^ ((this.mt[this.mti + 1] & LOWER_MASK | this.mt[this.mti] & UPPER_MASK) >>> 1)
                    ^ this.mt[this.mti + M];

            if (this.mtiFast < N) {
                this.mt[this.mtiFast] = 1812433253
                        * ((this.mt[this.mtiFast - 1] >>> 30) ^ this.mt[this.mtiFast - 1])
                        + this.mtiFast;
                this.mtiFast++;
            }
        }

        int ret = this.mt[this.mti++];
        ret = ((ret ^ (ret >>> 11)) << 7) & 0x9d2c5680 ^ ret ^ (ret >>> 11);
        ret = (ret << 15) & 0xefc60000 ^ ret ^ (((ret << 15) & 0xefc60000 ^ ret) >>> 18);
        return ret;
    }
    
    private synchronized void init(int seed) {
        this.mt[0] = seed;
        for (this.mti = 1; this.mti < N; this.mti++) {
            this.mt[this.mti] = (1812433253 * (this.mt[this.mti-1] ^ (this.mt[this.mti-1] >>> 30)) + this.mti);
        }
        this.mtiFast = N;
    }

    public synchronized void initFast(int seed) {
        this.mt[0] = seed;
        for (this.mtiFast = 1; this.mtiFast <= M; this.mtiFast++) {
            this.mt[this.mtiFast] = (1812433253 * ((this.mt[this.mtiFast - 1] >>> 30) ^ this.mt[this.mtiFast - 1]) + this.mtiFast);
        }
        this.mti = N;
    }
}
