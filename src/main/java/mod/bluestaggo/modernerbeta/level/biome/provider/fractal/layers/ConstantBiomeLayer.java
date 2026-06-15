package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.util.VersionCompat;

import java.util.Set;

public class ConstantBiomeLayer extends Layer {
    public static final com.mojang.serialization.MapCodec<ConstantBiomeLayer> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillLayerFields(instance)
            .and(ExtendedIdentifier.CODEC.fieldOf("biome").forGetter(layer -> layer.biome))
            .apply(instance, ConstantBiomeLayer::new)
    );

    private final ExtendedIdentifier biome;

    public ConstantBiomeLayer(String id, long seed, ExtendedIdentifier biome) {
        super(id, seed);
        this.biome = biome;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.CONSTANT_BIOME;
    }

    @Override
    protected ExtendedIdentifier generate(int x, int z) {
        return this.biome;
    }

    @Override
    public ExtendedIdentifier sample(int x, int z) {
        return this.biome;
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedIdentifier> biomes) {
        biomes.add(this.biome);
    }
}
