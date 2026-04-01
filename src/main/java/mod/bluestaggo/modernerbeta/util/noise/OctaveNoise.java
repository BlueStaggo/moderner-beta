package mod.bluestaggo.modernerbeta.util.noise;

public interface OctaveNoise {
    double[] sampleArray(
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
    );
    double sampleXY(double x, double y, double lacunarity);
    double sample(double x, double y, double z, double lacunarity);
    double sampleXZ(double x, double z, double scaleX, double scaleZ, double lacunarity);
    double sample(double x, double y, double z, double scaleX, double scaleY, double scaleZ, double lacunarity);

    default double[] sampleArray(
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
        return this.sampleArray(x, y, z, sizeX, sizeY, sizeZ, scaleX, scaleY, scaleZ, 0.5D);
    }

    default double sampleXY(double x, double y) {
        return this.sampleXY(x, y, 0.5D);
    }

    default double sample(double x, double y, double z) {
        return this.sample(x, y, z, 0.5D);
    }

    default double sampleXZ(double x, double z, double scaleX, double scaleZ) {
        return this.sampleXZ(x, z, scaleX, scaleZ, 0.5D);
    }

    default double sample(double x, double y, double z, double scaleX, double scaleY, double scaleZ) {
        return this.sample(x, y, z, scaleX, scaleY, scaleZ, 0.5D);
    }
}
