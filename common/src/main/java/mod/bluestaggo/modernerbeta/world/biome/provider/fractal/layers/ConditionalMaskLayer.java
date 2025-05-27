package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class ConditionalMaskLayer extends SingleParentLayer {
    public static final MapCodec<ConditionalMaskLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(instance.group(
                CodecUtil.set(ExtendedBiomeId.CODEC).fieldOf("filter").forGetter(layer -> layer.filter),
                Codec.STRING.fieldOf("onMatch").forGetter(layer -> layer.onMatch),
                Codec.STRING.fieldOf("otherwise").forGetter(layer -> layer.otherwise)
            ))
            .apply(instance, ConditionalMaskLayer::new)
    );

    private final Set<ExtendedBiomeId> filter;
    private final String onMatch;
    private final String otherwise;
    private transient Layer onMatchLayer;
    private transient Layer otherwiseLayer;

    public ConditionalMaskLayer(String id, long seed, String parent, Set<ExtendedBiomeId> filter, String onMatch, String otherwise) {
        super(id, seed, parent);
        this.filter = filter;
        this.onMatch = onMatch;
        this.otherwise = otherwise;
    }

    @Override
    public void configure(Function<String, Layer> layerMap) {
        super.configure(layerMap);
        this.onMatchLayer = !this.onMatch.isEmpty() ? layerMap.apply(this.onMatch) : null;
        this.otherwiseLayer = !this.otherwise.isEmpty() ? layerMap.apply(this.otherwise) : null;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.CONDITIONAL_MASK;
    }

    @Override
    protected List<Layer> getParents() {
        List<Layer> parents = new ArrayList<>();
        parents.add(this.parentLayer);
        if (this.onMatchLayer != null) {
            parents.add(this.onMatchLayer);
        }
        if (this.otherwiseLayer != null) {
            parents.add(this.otherwiseLayer);
        }
        return parents;
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        ExtendedBiomeId biome = this.parentLayer.sample(x, z);
        Layer layer = this.filter.contains(biome) ? this.onMatchLayer : this.otherwiseLayer;
        if (layer == null) {
            return biome;
        }

        ExtendedBiomeId output = layer.sample(x, z);
        if (ExtendedBiomeId.NULL.equals(output)) {
            return biome;
        }

        return output;
    }
}
