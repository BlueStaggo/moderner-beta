package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class UnsaltedLayer extends LayerWrapperLayer {
    public static final MapCodec<UnsaltedLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillLayerWrapperFields(instance)
            .apply(instance, UnsaltedLayer::new)
    );

    public UnsaltedLayer(String id, long seed, Layer layer) {
        super(id, seed, layer);
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.UNSALTED;
    }

    @Override
    public void init(long worldSeed) {
        this.layer.initUnsalted();
    }

    @Override
    public String toString() {
        return this.layer.toString() + " (unsalted)";
    }
}
