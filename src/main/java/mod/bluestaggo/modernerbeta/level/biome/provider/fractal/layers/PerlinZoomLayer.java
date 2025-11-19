package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.util.noise.PerlinNoise;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeId;

import java.util.Random;

public class PerlinZoomLayer extends SingleParentLayer {
    public static final com.mojang.serialization.MapCodec<PerlinZoomLayer> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(instance.group(
                Codec.DOUBLE.fieldOf("scale").orElse(2.0D).forGetter(layer -> layer.scale),
                Codec.DOUBLE.fieldOf("strength").orElse(0.8D).forGetter(layer -> layer.strength),
                Codec.DOUBLE.fieldOf("variation").orElse(1.2D).forGetter(layer -> layer.variation)
            ))
            .apply(instance, PerlinZoomLayer::new)
    );

    private final double scale;
    private final double strength;
    private final double variation;
    private transient PerlinNoise xOffsetNoise = new PerlinNoise();
    private transient PerlinNoise zOffsetNoise = new PerlinNoise();

    public PerlinZoomLayer(String id, long seed, String parent, double scale, double strength, double variation) {
        super(id, seed, parent);
        this.scale = scale;
        this.strength = strength;
        this.variation = variation;
    }

    @Override
    public void init(long worldSeed) {
        super.init(worldSeed);
        Random noiseRandom = new Random(this.getSaltedSeed());
        this.xOffsetNoise = new PerlinNoise(noiseRandom, true);
        this.zOffsetNoise = new PerlinNoise(noiseRandom, true);
    }

    public void initUnsalted() {
        super.initUnsalted();
        Random noiseRandom = new Random(this.getSaltedSeed());
        this.xOffsetNoise = new PerlinNoise(noiseRandom, true);
        this.zOffsetNoise = new PerlinNoise(noiseRandom, true);
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.PERLIN_ZOOM;
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        int zoomedX = (int)Math.round((x / this.scale) + this.xOffsetNoise.sample(x * this.variation / this.scale, z * this.variation / this.scale) * this.strength);
        int zoomedZ = (int)Math.round((z / this.scale) + this.zOffsetNoise.sample(x * this.variation / this.scale, z * this.variation / this.scale) * this.strength);
        return this.parentLayer.sample(zoomedX, zoomedZ);
    }
}
