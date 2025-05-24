package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.LayerType;

import java.util.Set;

public class DirtyZoomLayer extends Layer {
    public static final MapCodec<DirtyZoomLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillLayerFields(instance)
            .and(Codec.INT.fieldOf("level").orElse(1).forGetter(layer -> layer.level))
            .apply(instance, DirtyZoomLayer::new)
    );

    private final int level;

    public DirtyZoomLayer(long seed, int level) {
        super(seed);
        this.level = level;
    }

    @Override
    protected LayerType<?> getType() {
        return LayerType.DIRTY_ZOOM;
    }

    @Override
    protected ExtendedBiomeId generateBiome(int x, int z) {
        return this.parent.getBiome(x >> this.level, z >> this.level);
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedBiomeId> biomes) {
    }
}
