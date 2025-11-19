package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeId;

public class PointZoomLayer extends SingleParentLayer {
    public static final com.mojang.serialization.MapCodec<PointZoomLayer> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(Codec.INT.fieldOf("scale").orElse(2).forGetter(layer -> layer.scale))
            .apply(instance, PointZoomLayer::new)
    );

    private final int scale;

    public PointZoomLayer(String id, long seed, String parent, int scale) {
        super(id, seed, parent);
        this.scale = scale;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.POINT_ZOOM;
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        return this.parentLayer.sample(Math.floorDiv(x, this.scale), Math.floorDiv(z, this.scale));
    }
}
