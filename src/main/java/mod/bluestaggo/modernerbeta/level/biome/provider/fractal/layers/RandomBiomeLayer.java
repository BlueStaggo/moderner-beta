package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeResolver;
import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.Set;

public class RandomBiomeLayer extends Layer {
    public static final com.mojang.serialization.MapCodec<RandomBiomeLayer> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillLayerFields(instance)
            .and(ExtendedIdentifier.CODEC.listOf().fieldOf("biomes").forGetter(layer -> layer.biomes))
            .apply(instance, RandomBiomeLayer::new)
    );

    private final List<ExtendedIdentifier> biomes;
    private transient List<ExtendedHolder<Biome>> resolvedBiomes;

    public RandomBiomeLayer(String id, long seed, List<ExtendedIdentifier> biomes) {
        super(id, seed);
        this.biomes = biomes;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.RANDOM_BIOME;
    }

    @Override
    protected ExtendedHolder<Biome> generate(int x, int z) {
        LayerRandom random = this.getRandom(x, z);
        return random.nextItem(this.resolvedBiomes);
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedHolder<Biome>> biomes) {
        biomes.addAll(this.resolvedBiomes);
    }

    @Override
    protected void bindOwnBiomes(ExtendedBiomeResolver biomeResolver) {
        this.resolvedBiomes = this.biomes.stream()
            .map(biomeResolver::resolve)
            .toList();
    }
}
