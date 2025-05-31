package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;

public class PointZoomLayer extends SingleParentLayer {
    public static final MapCodec<PointZoomLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(Codec.INT.fieldOf("level").orElse(1).forGetter(layer -> layer.level))
            .apply(instance, PointZoomLayer::new)
    );

    private final int level;

    public PointZoomLayer(String id, long seed, String parent, int level) {
        super(id, seed, parent);
        this.level = level;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.POINT_ZOOM;
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        return this.parentLayer.sample(x * this.level, z * this.level);
    }
}
