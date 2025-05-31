package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;

import java.util.List;
import java.util.Set;

public class RandomBiomeLayer extends Layer {
    public static final MapCodec<RandomBiomeLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillLayerFields(instance)
            .and(ExtendedBiomeId.CODEC.listOf().fieldOf("biomes").forGetter(layer -> layer.biomes))
            .apply(instance, RandomBiomeLayer::new)
    );

    private final List<ExtendedBiomeId> biomes;

    public RandomBiomeLayer(String id, long seed, List<ExtendedBiomeId> biomes) {
        super(id, seed);
        this.biomes = biomes;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.RANDOM_BIOME;
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        LayerRandom random = this.getRandom(x, z);
        return random.nextItem(this.biomes);
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedBiomeId> biomes) {
        biomes.addAll(this.biomes);
    }
}
