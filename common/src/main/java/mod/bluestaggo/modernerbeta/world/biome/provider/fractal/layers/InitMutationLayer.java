package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;

public class InitMutationLayer extends SingleParentLayer {
    public static final MapCodec<InitMutationLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(Codec.BOOL.fieldOf("wideRandom").orElse(false).forGetter(layer -> layer.wideRandom))
            .apply(instance, InitMutationLayer::new)
    );

    private final boolean wideRandom;

    public InitMutationLayer(String id, long seed, String parent, boolean wideRandom) {
        super(id, seed, parent);
        this.wideRandom = wideRandom;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.INIT_MUTATION;
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        ExtendedBiomeId baseBiome = this.parentLayer.sample(x, z);
        if (baseBiome.baseId().equals(ExtendedBiomeId.OCEAN.baseId())) {
            return baseBiome;
        }

        LayerRandom random = this.getRandom(x, z);
        return random.nextInt(299999) % 29 == 0 ? ExtendedBiomeId.MUTATION : ExtendedBiomeId.NULL;
    }
}
