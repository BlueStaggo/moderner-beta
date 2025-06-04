package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import net.minecraft.util.collection.Pool;

import java.util.Set;

public class WeightedBiomeLayer extends Layer {
    public static final com.mojang.serialization.MapCodec<WeightedBiomeLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillLayerFields(instance)
            .and(Pool.createCodec(ExtendedBiomeId.CODEC).fieldOf("biomes").forGetter(layer -> layer.biomes))
            .apply(instance, WeightedBiomeLayer::new)
    );

    private final Pool<ExtendedBiomeId> biomes;

    public WeightedBiomeLayer(String id, long seed, Pool<ExtendedBiomeId> biomes) {
        super(id, seed);
        this.biomes = biomes;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.WEIGHTED_BIOME;
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        return VersionCompat.accessPool(this.biomes, this.getRandom(x, z));
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedBiomeId> biomes) {
        this.biomes.getEntries().stream()
            .map(VersionCompat::getWeightedValue)
            .forEach(biomes::add);
    }
}
