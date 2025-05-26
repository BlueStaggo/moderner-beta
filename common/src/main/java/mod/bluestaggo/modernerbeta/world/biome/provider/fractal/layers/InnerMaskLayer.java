package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.datafixers.Products;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.LayerType;

import java.util.*;

public class InnerMaskLayer extends MaskLayer {
    public static final MapCodec<InnerMaskLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillInnerMaskLayerFields(instance)
            .apply(instance, InnerMaskLayer::new)
    );

    protected static <L extends InnerMaskLayer> Products.P5<
        RecordCodecBuilder.Mu<L>,
        String,
        Long,
        String,
        Map<ExtendedBiomeId, String>,
        List<Set<ExtendedBiomeId>>
    > fillInnerMaskLayerFields(RecordCodecBuilder.Instance<L> instance) {
        return fillMaskLayerFields(instance)
            .and(CodecUtil.set(ExtendedBiomeId.CODEC).listOf().fieldOf("mergedRegions").forGetter(layer -> layer.mergedRegions));
    }

    protected final List<Set<ExtendedBiomeId>> mergedRegions;
    protected transient final Map<ExtendedBiomeId, List<Set<ExtendedBiomeId>>> mergedRegionMap;

    public InnerMaskLayer(String id, long seed, String parent, Map<ExtendedBiomeId, String> targets, List<Set<ExtendedBiomeId>> mergedRegions) {
        super(id, seed, parent, targets);
        this.mergedRegions = mergedRegions;

        Map<ExtendedBiomeId, List<Set<ExtendedBiomeId>>> mergedRegionMap = new HashMap<>();
        for (Set<ExtendedBiomeId> mergedRegion : mergedRegions) {
            for (ExtendedBiomeId biome : mergedRegion) {
                mergedRegionMap.computeIfAbsent(biome, b -> new ArrayList<>()).add(mergedRegion);
            }
        }
        this.mergedRegionMap = mergedRegionMap;
    }

    public InnerMaskLayer(String id, long seed, String parent, Map<ExtendedBiomeId, String> targets) {
        this(id, seed, parent, targets, List.of());
    }

    @Override
    protected LayerType<?> getType() {
        return LayerType.INNER_MASK;
    }

    @Override
    protected Layer getTargetLayer(ExtendedBiomeId biome, int x, int z) {
        ExtendedBiomeId[] neighbors = this.getParentNeighbors(x, z);
        if (allNeighborsEqual(neighbors, biome)) {
            return super.getTargetLayer(biome, x, z);
        }

        List<Set<ExtendedBiomeId>> mergedRegions = this.mergedRegionMap.get(biome);
        if (mergedRegions == null) {
            return null;
        }

        for (Set<ExtendedBiomeId> mergedRegion : mergedRegions) {
            if (allNeighborsInSet(neighbors, mergedRegion)) {
                return super.getTargetLayer(biome, x, z);
            }
        }

        return null;
    }

    protected ExtendedBiomeId[] getParentNeighbors(int x, int z) {
        return this.parentLayer.sampleNeighbors(x, z);
    }
}
