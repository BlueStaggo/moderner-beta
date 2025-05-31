package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.collection.Weighted;

import java.util.Set;
import java.util.function.Function;

public class WeightedLayerLayer extends Layer {
    public static final MapCodec<WeightedLayerLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillLayerFields(instance)
            .and(Pool.createCodec(Codec.STRING).fieldOf("layers").forGetter(layer -> layer.layers))
            .apply(instance, WeightedLayerLayer::new)
    );

    private final Pool<String> layers;
    private transient Pool<Layer> layerRefs;

    public WeightedLayerLayer(String id, long seed, Pool<String> layers) {
        super(id, seed);
        this.layers = layers;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.WEIGHTED_LAYER;
    }

    @Override
    public void configure(Function<String, Layer> layerMap) {
        this.layerRefs = layers.transform(layerMap);
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        return this.layerRefs.get(this.getRandom(x, z)).sample(x, z);
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedBiomeId> biomes) {
        for (Weighted<Layer> layer : this.layerRefs.getEntries()) {
            layer.value().addPossibleBiomes(biomes);
        }
    }
}
