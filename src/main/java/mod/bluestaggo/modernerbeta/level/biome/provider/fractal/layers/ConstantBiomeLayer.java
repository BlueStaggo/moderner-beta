package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeIds;
import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.world.level.biome.Biome;

import java.util.Set;

public class ConstantBiomeLayer extends Layer {
    public static final com.mojang.serialization.MapCodec<ConstantBiomeLayer> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillLayerFields(instance)
            .and(ExtendedBiomeIds.CODEC.fieldOf("biome").forGetter(layer -> layer.biome))
            .apply(instance, ConstantBiomeLayer::new)
    );

    private final ExtendedHolder<Biome> biome;

    public ConstantBiomeLayer(String id, long seed, ExtendedHolder<Biome> biome) {
        super(id, seed);
        this.biome = biome;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.CONSTANT_BIOME;
    }

    @Override
    protected ExtendedHolder<Biome> generate(int x, int z) {
        return this.biome;
    }

    @Override
    public ExtendedHolder<Biome> sample(int x, int z) {
        return this.biome;
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedHolder<Biome>> biomes) {
        biomes.add(this.biome);
    }
}
