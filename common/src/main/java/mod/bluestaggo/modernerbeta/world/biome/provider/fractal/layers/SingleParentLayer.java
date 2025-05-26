package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.Layer;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public abstract class SingleParentLayer extends Layer {
    protected final String parent;
    protected transient Layer parentLayer;

    protected static <L extends SingleParentLayer> Products.P3<
        RecordCodecBuilder.Mu<L>,
        String,
        Long,
        String
    > fillSingleParentLayerFields(RecordCodecBuilder.Instance<L> instance) {
        return fillLayerFields(instance)
            .and(Codec.STRING.fieldOf("parent").forGetter(layer -> layer.parent));
    }

    public SingleParentLayer(String id, long seed, String parent) {
        super(id, seed);
        this.parent = parent;
    }

    @Override
    public void configure(Function<String, Layer> layerMap) {
        this.parentLayer = layerMap.apply(this.parent);
    }

    @Override
    protected List<Layer> getParents() {
        return Collections.singletonList(this.parentLayer);
    }
}
