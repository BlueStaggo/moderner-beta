package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import mod.bluestaggo.modernerbeta.util.VersionCompat;

public class UnsaltedLayer extends LayerWrapperLayer {
    public static final com.mojang.serialization./*Map*/Codec<UnsaltedLayer> CODEC = VersionCompat.createMaybeMapCodec(
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
