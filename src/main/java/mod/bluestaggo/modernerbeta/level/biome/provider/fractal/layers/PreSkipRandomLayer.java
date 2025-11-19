package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.util.VersionCompat;

public class PreSkipRandomLayer extends LayerWrapperLayer {
    public static final com.mojang.serialization.MapCodec<PreSkipRandomLayer> CODEC = VersionCompat.createMaybeMapCodec(
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
        super.init(worldSeed);
        this.layer.setInitialSkip(this.skipAmount);
    }

    @Override
    public void initUnsalted() {
        super.initUnsalted();
        this.layer.setInitialSkip(this.skipAmount);
    }

    @Override
    public String toString() {
        return this.layer.toString() + " (skip " + this.skipAmount + " rng)";
    }
}
