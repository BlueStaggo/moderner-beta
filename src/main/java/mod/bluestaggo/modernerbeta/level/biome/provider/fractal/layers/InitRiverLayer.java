package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeId;

import static mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeId.*;

public class InitRiverLayer extends SingleParentLayer {
    public static final com.mojang.serialization.MapCodec<InitRiverLayer> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .apply(instance, InitRiverLayer::new)
    );

    public InitRiverLayer(String id, long seed, String parent) {
        super(id, seed, parent);
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.INIT_RIVER;
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        ExtendedBiomeId baseBiome = this.parentLayer.sample(x, z);
        if (baseBiome.baseId().equals(OCEAN.baseId())) {
            return baseBiome;
        }

        LayerRandom random = this.getRandom(x, z);
        return random.nextInt(2) == 0 ? RIVER_REGION_B : RIVER_REGION_A;
    }
}
