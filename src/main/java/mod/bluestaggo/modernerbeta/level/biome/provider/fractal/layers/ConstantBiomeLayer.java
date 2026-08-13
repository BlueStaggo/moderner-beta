package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeResolver;
import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.world.level.biome.Biome;

import java.util.Set;

public class ConstantBiomeLayer extends Layer {
    public static final com.mojang.serialization.MapCodec<ConstantBiomeLayer> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillLayerFields(instance)
            .and(ExtendedIdentifier.CODEC.fieldOf("biome").forGetter(layer -> layer.biome))
            .apply(instance, ConstantBiomeLayer::new)
    );

    private final ExtendedIdentifier biome;
    private transient ExtendedHolder<Biome> resolvedBiome;

    public ConstantBiomeLayer(String id, long seed, ExtendedIdentifier biome) {
        super(id, seed);
        this.biome = biome;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.CONSTANT_BIOME;
    }

    @Override
    protected ExtendedHolder<Biome> generate(int x, int z) {
        return this.resolvedBiome;
    }

    @Override
    public ExtendedHolder<Biome> sample(int x, int z) {
        return this.resolvedBiome;
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedHolder<Biome>> biomes) {
        biomes.add(this.resolvedBiome);
    }

    @Override
    protected void bindOwnBiomes(ExtendedBiomeResolver biomeResolver) {
        this.resolvedBiome = biomeResolver.resolve(this.biome);
    }
}
