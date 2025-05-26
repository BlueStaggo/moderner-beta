package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.LayerType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class DiagonalInnerMaskLayer extends InnerMaskLayer {
    public static final MapCodec<DiagonalInnerMaskLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillInnerMaskLayerFields(instance)
            .apply(instance, DiagonalInnerMaskLayer::new)
    );

    public DiagonalInnerMaskLayer(String id, long seed, String parent, Map<ExtendedBiomeId, String> targets, List<Set<ExtendedBiomeId>> mergedRegions) {
        super(id, seed, parent, targets, mergedRegions);
    }

    public DiagonalInnerMaskLayer(String id, long seed, String parent, Map<ExtendedBiomeId, String> targets) {
        super(id, seed, parent, targets);
    }

    @Override
    protected LayerType<?> getType() {
        return LayerType.DIAGONAL_INNER_MASK;
    }

    @Override
    protected ExtendedBiomeId[] getParentNeighbors(int x, int z) {
        return this.parentLayer.sampleDiagonalNeighbors(x, z);
    }
}
