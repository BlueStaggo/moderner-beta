package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class PreSkipRandomLayer extends Layer {
    public static final MapCodec<PreSkipRandomLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillLayerFields(instance)
            .and(instance.group(
                Layer.TYPE_CODEC.fieldOf("layer").forGetter(layer -> layer.layer),
                Codec.INT.fieldOf("skipAmount").forGetter(layer -> layer.skipAmount)
            ))
            .apply(instance, PreSkipRandomLayer::new)
    );

    private final int skipAmount;
    private final Layer layer;

    public PreSkipRandomLayer(String id, long seed, Layer layer, int skipAmount) {
        super(id, seed);
        this.layer = layer;
        this.skipAmount = skipAmount;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.PRE_SKIP_RANDOM;
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
    public void init(long worldSeed) {
        super.init(worldSeed);
        this.layer.getRandom(0, 0).setInitialSkip(this.skipAmount);
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        return this.layer.generate(x, z);
    }

    @Override
    public synchronized ExtendedBiomeId sample(int x, int z) {
        return this.layer.sample(x, z);
    }
}
