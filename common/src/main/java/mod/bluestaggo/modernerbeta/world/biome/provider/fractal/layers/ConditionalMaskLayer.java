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
                Codec.STRING.fieldOf("ifSatisfied").forGetter(layer -> layer.ifSatisfied),
                Codec.STRING.fieldOf("otherwise").forGetter(layer -> layer.otherwise)
            ))
            .apply(instance, ConditionalMaskLayer::new)
    );

    private final Set<ExtendedBiomeId> filter;
    private final String ifSatisfied;
    private final String otherwise;
    private transient Layer ifSatisfiedLayer;
    private transient Layer otherwiseLayer;

    public ConditionalMaskLayer(String id, long seed, String parent, Set<ExtendedBiomeId> filter, String ifSatisfied, String otherwise) {
        super(id, seed, parent);
        this.filter = filter;
        this.ifSatisfied = ifSatisfied;
        this.otherwise = otherwise;
    }

    @Override
    public void configure(Function<String, Layer> layerMap) {
        super.configure(layerMap);
        this.ifSatisfiedLayer = !this.ifSatisfied.isEmpty() ? layerMap.apply(this.ifSatisfied) : null;
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
        if (this.ifSatisfiedLayer != null) {
            parents.add(this.ifSatisfiedLayer);
        }
        if (this.otherwiseLayer != null) {
            parents.add(this.otherwiseLayer);
        }
        return parents;
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        ExtendedBiomeId biome = this.parentLayer.sample(x, z);
        Layer layer = this.filter.contains(biome) ? this.ifSatisfiedLayer : this.otherwiseLayer;
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
