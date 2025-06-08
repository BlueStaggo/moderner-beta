package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;

import java.util.Set;

public class ConstantBiomeLayer extends Layer {
    public static final com.mojang.serialization.MapCodec<ConstantBiomeLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillLayerFields(instance)
            .and(ExtendedBiomeId.CODEC.fieldOf("biome").forGetter(layer -> layer.biome))
            .apply(instance, ConstantBiomeLayer::new)
    );

    private final ExtendedBiomeId biome;

    public ConstantBiomeLayer(String id, long seed, ExtendedBiomeId biome) {
        super(id, seed);
        this.biome = biome;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.CONSTANT_BIOME;
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        return this.biome;
    }

    @Override
    public ExtendedBiomeId sample(int x, int z) {
        return this.biome;
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedBiomeId> biomes) {
        biomes.add(this.biome);
    }
}
