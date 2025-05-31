package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;

public class SupplyRandomLayer extends Layer {
    public static final MapCodec<SupplyRandomLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillLayerFields(instance)
            .and(Codec.INT.fieldOf("range").forGetter(layer -> layer.range))
            .apply(instance, SupplyRandomLayer::new)
    );

    private final int range;

    public SupplyRandomLayer(String id, long seed, int range) {
        super(id, seed);
        this.range = range;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.SUPPLY_RANDOM;
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        return ExtendedBiomeId.RANDOM.withExt(String.valueOf(this.getRandom(x, z).nextInt(this.range)));
    }
}
