package mod.bluestaggo.modernerbeta.util.noise;

import java.util.Random;

/* 
 * Used for additional reference: https://www.reddit.com/r/proceduralgeneration/comments/6eubj7/how_can_i_add_octaves_persistence_lacunarity/ 
 * 
 * */
public class SimplexOctaveNoise implements OctaveNoise {
    private final SimplexNoise[] noises;
    private final int octaves;
    private final double noiseScale;
    
    public SimplexOctaveNoise(Random random, int octaves) {
        this.noises = new SimplexNoise[octaves];
        this.octaves = octaves;
        this.noiseScale = 1.5D;
        
        for (int i = 0; i < octaves; ++i) {
            this.noises[i] = new SimplexNoise(random);
        }
    }

    @Override
    public double[] sampleArray(
        double x,
        double y,
        double z,
        int sizeX,
        int sizeY,
        int sizeZ,
        double scaleX,
        double scaleY,
        double scaleZ,
        double lacunarity
    ) {
        final double persistence = 0.5D;

        double[] noise = new double[sizeX * sizeY * sizeZ];
        double frequency = 1.0;
        double amplitude = 1.0;

        scaleX /= this.noiseScale;
        scaleY /= this.noiseScale;
        scaleZ /= this.noiseScale;

        for (int i = 0; i < octaves; i++) {
            if (sizeY == 1) {
                int ndx = 0;
                for (int sX = 0; sX < sizeX; sX++) {
                    for (int sZ = 0; sZ < sizeZ; sZ++) {
                        double curX = (x + (double)sX) * frequency;
                        double curZ = (z + (double)sZ) * frequency;

                        noise[ndx++] += this.noises[i].sample(curX, curZ, scaleX, scaleZ) * (0.55 / amplitude);
                    }
                }
            } else {
                this.noises[i].sample(
                    noise,
                    x,
                    y,
                    z,
                    sizeX,
                    sizeY,
                    sizeZ,
                    scaleX * frequency,
                    scaleY * frequency,
                    scaleZ * frequency,
                    0.55 / amplitude
                );
            }

            frequency *= lacunarity;
            amplitude *= persistence;
        }

        return noise;
    }

    @Override
    public double sampleXY(double x, double y, double lacunarity) {
        return this.sampleXZ(x, y, 1.0, 1.0, lacunarity);
    }

    @Override
    public double sample(double x, double y, double z, double lacunarity) {
        return this.sample(x, y, z, 1.0, 1.0, 1.0, lacunarity);
    }

    @Override
    public double sampleXZ(double x, double z, double scaleX, double scaleZ, double lacunarity) {
        final double persistence = 0.5D;

        scaleX /= this.noiseScale;
        scaleZ /= this.noiseScale;

        double total = 0.0;
        double amplitude = 1.0;
        double frequency = 1.0;

        for (int j = 0; j < this.octaves; ++j) {
            total += this.noises[j].sample(x, z, scaleX * frequency, scaleZ * frequency) * (0.55 / amplitude);
            frequency *= lacunarity;
            amplitude *= persistence;
        }

        return total;
    }

    @Override
    public double sample(double x, double y, double z, double scaleX, double scaleY, double scaleZ, double lacunarity) {
        final double persistence = 0.5D;

        scaleX /= this.noiseScale;
        scaleY /= this.noiseScale;
        scaleZ /= this.noiseScale;

        double total = 0.0;
        double amplitude = 1.0;
        double frequency = 1.0;

        for (int j = 0; j < this.octaves; ++j) {
            total += this.noises[j].sample(x, y, z,
                    scaleX * frequency, scaleY * frequency, scaleZ * frequency) * (0.55 / amplitude);
            frequency *= lacunarity;
            amplitude *= persistence;
        }

        return total;
    }
}
