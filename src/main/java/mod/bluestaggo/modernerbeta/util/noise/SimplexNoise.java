package mod.bluestaggo.modernerbeta.util.noise;

import java.util.Random;

/*
 * Reference: http://weber.itn.liu.se/~stegu/simplexnoise/simplexnoise.pdf
 * 
 * Tested output range, on 100000 * 100000 sample: -0.885539/0.885539
 */
public class SimplexNoise {
    private static final int[][] GRADIENTS = new int[][] { { 1, 1, 0 }, { -1, 1, 0 }, { 1, -1, 0 }, { -1, -1, 0 }, { 1, 0, 1 }, { -1, 0, 1 }, { 1, 0, -1 }, { -1, 0, -1 }, { 0, 1, 1 }, { 0, -1, 1 }, { 0, 1, -1 }, { 0, -1, -1 } };
    private final int[] permutations;
    
    public double xOrigin;
    public double yOrigin;
    public double zOrigin;
    
    private static final double UNSKEW_FACTOR_2D = (3.0 - Math.sqrt(3.0)) / 6.0;
    private static final double SKEW_FACTOR_2D = 0.5 * (Math.sqrt(3.0) - 1.0);

    private static final double UNSKEW_FACTOR_3D = 1.0 / 6.0;
    private static final double SKEW_FACTOR_3D = 1.0 / 3.0;

    public SimplexNoise() {
        this(new Random());
    }
    
    public SimplexNoise(Random random) {
        this.permutations = new int[512];
        this.xOrigin = random.nextDouble() * 256.0;
        this.yOrigin = random.nextDouble() * 256.0;
        this.zOrigin = random.nextDouble() * 256.0;
        
        for (int i = 0; i < 256; ++i) {
            this.permutations[i] = i;
        }
        
        for (int i = 0; i < 256; ++i) {
            int permNdx = random.nextInt(256 - i) + i;
            int perm = this.permutations[i];
            
            this.permutations[i] = this.permutations[permNdx];
            this.permutations[permNdx] = perm;
            this.permutations[i + 256] = this.permutations[i];
        }
    }
    
    private static int fastFloor(double double1) {
        return (double1 > 0.0) ? ((int)double1) : ((int)double1 - 1);
    }
    
    private static double dot(int[] gradient, double x, double y) {
        return gradient[0] * x + gradient[1] * y;
    }

    private static double dot(int[] gradient, double x, double y, double z) {
        return gradient[0] * x + gradient[1] * y + gradient[2] * z;
    }
    
    public void sample(double[] arr, double x, double y, int sizeX, int sizeY, double scaleX, double scaleY, double amplitude) {
        int ndx = 0;
        
        for (int sX = 0; sX < sizeX; ++sX) {
            for (int sY = 0; sY < sizeY; ++sY) {
                arr[ndx++] += this.sample(x + sX, y + sY, scaleX, scaleY) * amplitude;
            }
        }
    }

    public void sample(
        double[] arr,
        double x,
        double y,
        double z,
        int sizeX,
        int sizeY,
        double sizeZ,
        double scaleX,
        double scaleY,
        double scaleZ,
        double amplitude
    ) {
        int ndx = 0;

        for (int sX = 0; sX < sizeX; ++sX) {
            for (int sZ = 0; sZ < sizeZ; ++sZ) {
                for (int sY = 0; sY < sizeY; ++sY) {
                    arr[ndx++] += this.sample(x + sX, y + sY, z + sZ, scaleX, scaleY, scaleZ) * amplitude;
                }
            }
        }
    }
    
    public double sample(double x, double y, double scaleX, double scaleY) {
        x = x * scaleX + this.xOrigin;
        y = y * scaleY + this.yOrigin;
        
        double s = (x + y) * SKEW_FACTOR_2D;
        int i = fastFloor(x + s);
        int j = fastFloor(y + s);
        
        double t = (i + j) * UNSKEW_FACTOR_2D;
        double x0 = i - t;
        double y0 = j - t;
        double xDist = x - x0;
        double yDist = y - y0;
        
        int offsetI;
        int offsetJ;
        if (xDist > yDist) {
            offsetI = 1;
            offsetJ = 0;
        } else {
            offsetI = 0;
            offsetJ = 1;
        }
        
        double offsetMidX = xDist - offsetI + UNSKEW_FACTOR_2D;
        double offsetMidY = yDist - offsetJ + UNSKEW_FACTOR_2D;
        double offsetLastX = xDist - 1.0 + 2.0 * UNSKEW_FACTOR_2D;
        double offsetLastY = yDist - 1.0 + 2.0 * UNSKEW_FACTOR_2D;
        
        int hash0 = i & 0xFF;
        int hash1 = j & 0xFF;
        int gradNdx0 = this.permutations[hash0 + this.permutations[hash1]] % 12;
        int gradNdx1 = this.permutations[hash0 + offsetI + this.permutations[hash1 + offsetJ]] % 12;
        int gradNdx2 = this.permutations[hash0 + 1 + this.permutations[hash1 + 1]] % 12;
        
        double t0 = 0.5 - xDist * xDist - yDist * yDist;
        double contrib0;
        if (t0 < 0.0) {
            contrib0 = 0.0;
        } else {
            t0 *= t0;
            contrib0 = t0 * t0 * dot(GRADIENTS[gradNdx0], xDist, yDist);
        }
        
        double t1 = 0.5 - offsetMidX * offsetMidX - offsetMidY * offsetMidY;
        double contrib1;
        if (t1 < 0.0) {
            contrib1 = 0.0;
        } else {
            t1 *= t1;
            contrib1 = t1 * t1 * dot(GRADIENTS[gradNdx1], offsetMidX, offsetMidY);
        }
        
        double t2 = 0.5 - offsetLastX * offsetLastX - offsetLastY * offsetLastY;
        double contrib2;
        if (t2 < 0.0) {
            contrib2 = 0.0;
        } else {
            t2 *= t2;
            contrib2 = t2 * t2 * dot(GRADIENTS[gradNdx2], offsetLastX, offsetLastY);
        }
        
        return 70.0 * (contrib0 + contrib1 + contrib2);
    }

    public double sample(double x, double y, double z, double scaleX, double scaleY, double scaleZ) {
        x = x * scaleX + this.xOrigin;
        y = y * scaleY + this.yOrigin;
        z = z * scaleZ + this.zOrigin;

        double s = (x + y + z) * SKEW_FACTOR_3D;
        int i = fastFloor(x + s);
        int j = fastFloor(y + s);
        int k = fastFloor(z + s);
        
        double t = (i + j + k) * UNSKEW_FACTOR_3D;
        double x0 = i - t;
        double y0 = j - t;
        double z0 = k - t;
        double xDist = x - x0;
        double yDist = y - y0;
        double zDist = z - z0;

        int offsetI0;
        int offsetJ0;
        int offsetK0;
        int offsetI1;
        int offsetJ1;
        int offsetK1;
        if (xDist >= yDist) {
            if (yDist >= zDist) {
                offsetI0 = 1;
                offsetJ0 = 0;
                offsetK0 = 0;
                offsetI1 = 1;
                offsetJ1 = 1;
                offsetK1 = 0;
            } else if (xDist >= zDist) {
                offsetI0 = 1;
                offsetJ0 = 0;
                offsetK0 = 0;
                offsetI1 = 1;
                offsetJ1 = 0;
                offsetK1 = 1;
            } else {
                offsetI0 = 0;
                offsetJ0 = 0;
                offsetK0 = 1;
                offsetI1 = 1;
                offsetJ1 = 0;
                offsetK1 = 1;
            }
        } else if (yDist < zDist) {
            offsetI0 = 0;
            offsetJ0 = 0;
            offsetK0 = 1;
            offsetI1 = 0;
            offsetJ1 = 1;
            offsetK1 = 1;
        } else if (xDist < zDist) {
            offsetI0 = 0;
            offsetJ0 = 1;
            offsetK0 = 0;
            offsetI1 = 0;
            offsetJ1 = 1;
            offsetK1 = 1;
        } else {
            offsetI0 = 0;
            offsetJ0 = 1;
            offsetK0 = 0;
            offsetI1 = 1;
            offsetJ1 = 1;
            offsetK1 = 0;
        }

        double offsetFirstX = xDist - offsetI0 + UNSKEW_FACTOR_3D;
        double offsetFirstY = yDist - offsetJ0 + UNSKEW_FACTOR_3D;
        double offsetFirstZ = zDist - offsetK0 + UNSKEW_FACTOR_3D;
        double offsetMidX = xDist - offsetI1 + SKEW_FACTOR_3D;
        double offsetMidY = yDist - offsetJ1 + SKEW_FACTOR_3D;
        double offsetMidZ = zDist - offsetK1 + SKEW_FACTOR_3D;
        double offsetLastX = xDist - 1.0 + 0.5;
        double offsetLastY = yDist - 1.0 + 0.5;
        double offsetLastZ = zDist - 1.0 + 0.5;
        
        int hash0 = i & 0xFF;
        int hash1 = j & 0xFF;
        int hash2 = k & 0xFF;
        int gradNdx0 = this.permutations[hash0 + this.permutations[hash1 + this.permutations[hash2]]] % 12;
        int gradNdx1 = this.permutations[hash0 + offsetI0 + this.permutations[hash1 + offsetJ0 + this.permutations[hash2 + offsetK0]]] % 12;
        int gradNdx2 = this.permutations[hash0 + offsetI1 + this.permutations[hash1 + offsetJ1 + this.permutations[hash2 + offsetK1]]] % 12;
        int gradNdx3 = this.permutations[hash0 + 1 + this.permutations[hash1 + 1 + this.permutations[hash2 + 1]]] % 12;
        
        double t0 = 0.6 - xDist * xDist - yDist * yDist - zDist * zDist;
        double contrib0;
        if (t0 < 0.0) {
            contrib0 = 0.0;
        } else {
            t0 *= t0;
            contrib0 = t0 * t0 * dot(GRADIENTS[gradNdx0], xDist, yDist, zDist);
        }

        double t1 = 0.6 - offsetFirstX * offsetFirstX - offsetFirstY * offsetFirstY - offsetFirstZ * offsetFirstZ;
        double contrib1;
        if (t1 < 0.0) {
            contrib1 = 0.0;
        } else {
            t1 *= t1;
            contrib1 = t1 * t1 * dot(GRADIENTS[gradNdx1], offsetFirstX, offsetFirstY, offsetFirstZ);
        }

        double t2 = 0.6 - offsetMidX * offsetMidX - offsetMidY * offsetMidY - offsetMidZ * offsetMidZ;
        double contrib2;
        if (t2 < 0.0) {
            contrib2 = 0.0;
        } else {
            t2 *= t2;
            contrib2 = t2 * t2 * dot(GRADIENTS[gradNdx2], offsetMidX, offsetMidY, offsetMidZ);
        }

        double t3 = 0.6 - offsetLastX * offsetLastX - offsetLastY * offsetLastY - offsetLastZ * offsetLastZ;
        double contrib3;
        if (t3 < 0.0) {
            contrib3 = 0.0;
        } else {
            t3 *= t3;
            contrib3 = t3 * t3 * dot(GRADIENTS[gradNdx3], offsetLastX, offsetLastY, offsetLastZ);
        }

        return 32.0 * (contrib0 + contrib1 + contrib2 + contrib3);
    }
}
