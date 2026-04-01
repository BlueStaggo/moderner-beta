package mod.bluestaggo.modernerbeta.util.noise;

import mod.bluestaggo.modernerbeta.settings.component.PerlinNoiseSettings;
import net.minecraft.util.Mth;

import java.util.Random;

public class PerlinOctaveNoise {
    private final PerlinNoiseSettings settings;
    private final PerlinNoise[] noises;
    private final int octaves;
    
    public PerlinOctaveNoise(Random random, int octaves, PerlinNoiseSettings settings) {
        this.noises = new PerlinNoise[octaves];
        this.octaves = octaves;
        this.settings = settings;
        
        for (int i = 0; i < octaves; i++) {
            this.noises[i] = new PerlinNoise(random, settings);
        }
    }

    /*
     * Generic 3D array noise sampler.
     */
    public double[] sampleArray(
        double x,
        double y,
        double z, 
        int sizeX,
        int sizeY,
        int sizeZ, 
        double scaleX,
        double scaleY,
        double scaleZ
    ) {
        double[] noise = new double[sizeX * sizeY * sizeZ];
        double frequency = 1.0;
        
        for (int i = 0; i < octaves; i++) {
            double offX = x;
            double offZ = z;

            if (settings.wrapped()) {
                offX *= frequency * scaleX;
                offZ *= frequency * scaleZ;
                long offXCoord = Mth.lfloor(offX);
                long offZCoord = Mth.lfloor(offZ);
                offX -= offXCoord;
                offZ -= offZCoord;
                offXCoord %= 16777216L;
                offZCoord %= 16777216L;
                offX += offXCoord;
                offZ += offZCoord;
                offX /= frequency * scaleX;
                offZ /= frequency * scaleX;
            }

            if (!settings.alpha2DSampling() && sizeY == 1) {
                int ndx = 0;
                for (int sX = 0; sX < sizeX; sX++) {
                    for (int sZ = 0; sZ < sizeZ; sZ++) {
                        double curX = (x + (double)sX) * scaleX * frequency;
                        double curZ = (z + (double)sZ) * scaleZ * frequency;

                        noise[ndx++] += this.noises[i].sampleXZ(curX, curZ, frequency);
                    }
                }
            } else {
                this.noises[i].sample(
                    noise,
                    offX,
                    y,
                    offZ,
                    sizeX,
                    sizeY,
                    sizeZ,
                    scaleX * frequency,
                    scaleY * frequency,
                    scaleZ * frequency,
                    frequency
                );
            }

            frequency /= 2.0;
        }
        
        return noise;
    }

    /*
     * Standard 2D Perlin noise sampler.
     */
    public final double sampleXY(double x, double y) {
        double total = 0.0;
        double frequency = 1.0;
        
        for (int i = 0; i < this.octaves; ++i) {
            total += this.noises[i].sample(x / frequency, y / frequency) * frequency;
            frequency *= 2.0;
        }
        
        return total;
    }

    /*
     * Standard 3D Perlin noise sampler.
     */
    public final double sample(double x, double y, double z) {
        double total = 0.0;
        double frequency = 1.0;

        for (int i = 0; i < this.octaves; ++i) {
            double offX = x / frequency;
            double offZ = z / frequency;

            if (settings.wrapped()) {
                long offXCoord = Mth.lfloor(offX);
                long offZCoord = Mth.lfloor(offZ);
                offX -= offXCoord;
                offZ -= offZCoord;
                offXCoord %= 16777216L;
                offZCoord %= 16777216L;
                offX += offXCoord;
                offZ += offZCoord;
            }

            total += this.noises[i].sample(offX, y / frequency, offZ) * frequency;
            frequency *= 2.0;
        }

        return total;
    }

    /*
     * 2D noise sampler. This noise sampler does not overflow.
     */
    public final double sampleXZ(double x, double z, double scaleX, double scaleZ) {
        double total = 0.0;
        double frequency = 1.0;

        for (int i = 0; i < this.octaves; ++i) {
            double offX = x * scaleX * frequency;
            double offZ = z * scaleZ * frequency;

            if (settings.wrapped()) {
                long offXCoord = Mth.lfloor(offX);
                long offZCoord = Mth.lfloor(offZ);
                offX -= offXCoord;
                offZ -= offZCoord;
                offXCoord %= 16777216L;
                offZCoord %= 16777216L;
                offX += offXCoord;
                offZ += offZCoord;
            }

            total += this.noises[i].sampleXZ(
                offX,
                offZ,
                frequency
            );
            frequency /= 2.0;
        }

        return total;
    }

    /*
     * 3D noise sampler. This noise sampler does not overflow horizontally.
     */
    public final double sample(double x, double y, double z, double scaleX, double scaleY, double scaleZ) {
        if (settings.infdevNoiseScaling()) {
            return sample(x * scaleX, y * scaleY, z * scaleZ);
        }

        double total = 0.0;
        double frequency = 1.0;

        for (int i = 0; i < this.octaves; ++i) {
            double offX = x * scaleX * frequency;
            double offZ = z * scaleZ * frequency;

            if (settings.wrapped()) {
                long offXCoord = Mth.lfloor(offX);
                long offZCoord = Mth.lfloor(offZ);
                offX -= offXCoord;
                offZ -= offZCoord;
                offXCoord %= 16777216L;
                offZCoord %= 16777216L;
                offX += offXCoord;
                offZ += offZCoord;
            }

            total += this.noises[i].sampleXYZ(
                offX,
                y * scaleY * frequency,
                offZ,
                scaleY * frequency,
                y * scaleY * frequency
            ) / frequency;

            frequency /= 2.0;
        }

        return total;
    }
}