package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;

public class PointZoomLayer extends SingleParentLayer {
    public static final com.mojang.serialization.MapCodec<PointZoomLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(Codec.INT.fieldOf("scale").orElse(1).forGetter(layer -> layer.scale))
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
        return this.parentLayer.sample(x * this.scale, z * this.scale);
    }
}
