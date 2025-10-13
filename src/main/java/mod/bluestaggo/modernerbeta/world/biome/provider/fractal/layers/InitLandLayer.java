package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;

import java.util.Set;

import static mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId.OCEAN;
import static mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId.PLAINS;

public class InitLandLayer extends Layer {
    public static final com.mojang.serialization./*Map*/Codec<InitLandLayer> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillLayerFields(instance)
            .and(Codec.INT.fieldOf("landChance").orElse(10).forGetter(layer -> layer.landChance))
            .apply(instance, InitLandLayer::new)
    );

    public final int landChance;

    public InitLandLayer(String id, long seed) {
        this(id, seed, 10);
    }

    public InitLandLayer(String id, long seed, int landChance) {
        super(id, seed);
        this.landChance = landChance;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.INIT_LAND;
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        if (x == 0 && z == 0 || this.getRandom(x, z).nextInt(this.landChance) == 0) {
            return PLAINS;
        }
        return OCEAN;
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedBiomeId> biomes) {
        biomes.add(OCEAN);
        biomes.add(PLAINS);
    }
}
