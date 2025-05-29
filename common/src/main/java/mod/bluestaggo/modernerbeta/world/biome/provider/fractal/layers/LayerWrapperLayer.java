package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

public abstract class LayerWrapperLayer extends Layer {
    protected final Layer layer;

    protected static <L extends LayerWrapperLayer> Products.P3<
        RecordCodecBuilder.Mu<L>,
        String,
        Long,
        Layer
    > fillLayerWrapperFields(RecordCodecBuilder.Instance<L> instance) {
        return fillLayerFields(instance)
            .and(Layer.TYPE_CODEC.fieldOf("layer").forGetter(layer -> layer.layer));
    }

    public LayerWrapperLayer(String id, long seed, Layer layer) {
        super(id, seed);
        this.layer = layer;
    }

    @Override
    protected List<Layer> getParents() {
        return List.of(this.layer);
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedBiomeId> biomes) {
        this.layer.addPossibleBiomes(biomes);
    }

    @Override
    public void configure(Function<String, Layer> layerMap) {
        this.layer.configure(layerMap);
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        return this.layer.generate(x, z);
    }

    @Override
    public synchronized ExtendedBiomeId sample(int x, int z) {
        return this.layer.sample(x, z);
    }

    @Override
    public void init(long worldSeed) {
        this.layer.init(worldSeed);
    }

    @Override
    public void initUnsalted() {
        this.layer.initUnsalted();
    }

    @Override
    public String toString() {
        return this.layer.toString();
    }
}
