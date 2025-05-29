package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates.BiomePredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ConditionalOverlayLayer extends SingleParentLayer {
    public static final MapCodec<ConditionalOverlayLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(instance.group(
                BiomePredicate.BASE_CODEC.fieldOf("predicate").forGetter(layer -> layer.predicate),
                Codec.STRING.fieldOf("onMatch").forGetter(layer -> layer.onMatch),
                Codec.STRING.fieldOf("otherwise").forGetter(layer -> layer.otherwise)
            ))
            .apply(instance, ConditionalOverlayLayer::new)
    );

    private final BiomePredicate predicate;
    private final String onMatch;
    private final String otherwise;
    private transient Layer onMatchLayer;
    private transient Layer otherwiseLayer;

    public ConditionalOverlayLayer(String id, long seed, String parent, BiomePredicate predicate, String onMatch, String otherwise) {
        super(id, seed, parent);
        this.predicate = predicate;
        this.onMatch = onMatch;
        this.otherwise = otherwise;
    }

    @Override
    public void configure(Function<String, Layer> layerMap) {
        super.configure(layerMap);
        this.onMatchLayer = !this.onMatch.isEmpty() ? layerMap.apply(this.onMatch) : null;
        this.otherwiseLayer = !this.otherwise.isEmpty() ? layerMap.apply(this.otherwise) : null;
        if (this.onMatchLayer == this.parentLayer) {
            this.onMatchLayer = null;
        }
        if (this.otherwiseLayer == this.parentLayer) {
            this.otherwiseLayer = null;
        }
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.CONDITIONAL_OVERLAY;
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
        Layer layer = this.predicate.matches(biome, this.parentLayer, Suppliers.memoize(() -> this.getRandom(x, z)), x, z)
            ? this.onMatchLayer : this.otherwiseLayer;
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
