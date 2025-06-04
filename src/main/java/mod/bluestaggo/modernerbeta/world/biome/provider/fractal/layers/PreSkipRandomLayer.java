package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class PreSkipRandomLayer extends LayerWrapperLayer {
    public static final com.mojang.serialization.MapCodec<PreSkipRandomLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillLayerWrapperFields(instance)
            .and(Codec.INT.fieldOf("skipAmount").forGetter(layer -> layer.skipAmount))
            .apply(instance, PreSkipRandomLayer::new)
    );

    private final int skipAmount;

    public PreSkipRandomLayer(String id, long seed, Layer layer, int skipAmount) {
        super(id, seed, layer);
        this.skipAmount = skipAmount;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.PRE_SKIP_RANDOM;
    }

    @Override
    public void init(long worldSeed) {
        this.layer.init(worldSeed);
        this.layer.getRandom(0, 0).setInitialSkip(this.skipAmount);
    }

    @Override
    public void initUnsalted() {
        this.layer.initUnsalted();
        this.layer.getRandom(0, 0).setInitialSkip(this.skipAmount);
    }

    @Override
    public String toString() {
        return this.layer.toString() + " (skip " + this.skipAmount + " rng)";
    }
}
