package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeId;

import java.util.List;
import java.util.Set;

public class RandomBiomeLayer extends Layer {
    public static final com.mojang.serialization.MapCodec<RandomBiomeLayer> CODEC = VersionCompat.createMaybeMapCodec(
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
