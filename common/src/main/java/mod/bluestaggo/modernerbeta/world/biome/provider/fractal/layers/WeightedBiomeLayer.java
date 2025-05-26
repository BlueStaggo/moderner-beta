package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.LayerType;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.collection.Weighted;

import java.util.Set;

public class WeightedBiomeLayer extends Layer {
    public static final MapCodec<WeightedBiomeLayer> CODEC = RecordCodecBuilder.mapCodec(
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
    protected LayerType<?> getType() {
        return LayerType.WEIGHTED_BIOME;
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        return this.biomes.get(this.getRandom(x, z));
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedBiomeId> biomes) {
        this.biomes.getEntries().stream()
            .map(Weighted::value)
            .forEach(biomes::add);
    }
}
