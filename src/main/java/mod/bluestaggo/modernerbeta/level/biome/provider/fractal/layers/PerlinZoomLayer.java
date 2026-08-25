package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.settings.component.PerlinNoiseSettings;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.util.noise.PerlinNoise;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.biome.Biome;

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
        RandomSource noiseRandom = new LegacyRandomSource(this.getSaltedSeed());
        this.xOffsetNoise = new PerlinNoise(noiseRandom, PerlinNoiseSettings.RELEASE);
        this.zOffsetNoise = new PerlinNoise(noiseRandom, PerlinNoiseSettings.RELEASE);
    }

    public void initUnsalted() {
        super.initUnsalted();
        RandomSource noiseRandom = new LegacyRandomSource(this.getSaltedSeed());
        this.xOffsetNoise = new PerlinNoise(noiseRandom, PerlinNoiseSettings.RELEASE);
        this.zOffsetNoise = new PerlinNoise(noiseRandom, PerlinNoiseSettings.RELEASE);
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.PERLIN_ZOOM;
    }

    @Override
    protected ExtendedHolder<Biome> generate(int x, int z) {
        int zoomedX = (int)Math.round((x / this.scale) + this.xOffsetNoise.sample(x * this.variation / this.scale, z * this.variation / this.scale) * this.strength);
        int zoomedZ = (int)Math.round((z / this.scale) + this.zOffsetNoise.sample(x * this.variation / this.scale, z * this.variation / this.scale) * this.strength);
        return this.parentLayer.sample(zoomedX, zoomedZ);
    }
}
