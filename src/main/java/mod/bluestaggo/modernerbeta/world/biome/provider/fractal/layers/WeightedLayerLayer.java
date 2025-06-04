package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import net.minecraft.util.collection.Pool;

import java.util.Set;
import java.util.function.Function;

public class WeightedLayerLayer extends Layer {
    public static final com.mojang.serialization.MapCodec<WeightedLayerLayer> CODEC = RecordCodecBuilder.mapCodec(
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
        //? if >=1.21.5 {
        this.layerRefs = this.layers.transform(layerMap);
        //?} else {
        /*Pool.Builder<Layer> poolBuilder = Pool.builder();
        for (Weighted.Present<String> entry : this.layers.getEntries()) {
            poolBuilder.add(layerMap.apply(VersionCompat.getWeightedValue(entry)), entry.getWeight().getValue());
        }
        this.layerRefs = poolBuilder.build();
        *///?}
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        return VersionCompat.accessPool(this.layerRefs, this.getRandom(x, z)).sample(x, z);
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedBiomeId> biomes) {
        VersionCompat.forEachValueInPool(this.layerRefs, layer -> layer.addPossibleBiomes(biomes));
    }
}
