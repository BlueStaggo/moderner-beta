package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.LayerType;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.SingleParentLayer;

public class DirtyZoomLayer extends SingleParentLayer {
    public static final MapCodec<DirtyZoomLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(Codec.INT.fieldOf("level").orElse(1).forGetter(layer -> layer.level))
            .apply(instance, DirtyZoomLayer::new)
    );

    private final int level;

    public DirtyZoomLayer(String id, long seed, String parent, int level) {
        super(id, seed, parent);
        this.level = level;
    }

    @Override
    protected LayerType<?> getType() {
        return LayerType.DIRTY_ZOOM;
    }

    @Override
    protected ExtendedBiomeId generateBiome(int x, int z) {
        return this.getParent().getBiome(x >> this.level, z >> this.level);
    }
}
