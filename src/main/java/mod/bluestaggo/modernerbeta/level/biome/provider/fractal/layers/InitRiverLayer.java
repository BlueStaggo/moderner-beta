package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.world.level.biome.Biome;

import static mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeIds.*;

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
    protected ExtendedHolder<Biome> generate(int x, int z) {
        ExtendedHolder<Biome> baseBiome = this.parentLayer.sample(x, z);
        if (baseBiome.is(OCEAN.baseId())) {
            return baseBiome;
        }

        LayerRandom random = this.getRandom(x, z);
        return random.nextInt(2) == 0 ? RIVER_REGION_B : RIVER_REGION_A;
    }
}
