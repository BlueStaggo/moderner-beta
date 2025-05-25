package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.LayerRandom;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.LayerType;

import static mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId.*;

public class InitRiverLayer extends SingleParentLayer {
    public static final MapCodec<InitRiverLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(Codec.BOOL.fieldOf("wideRandom").orElse(false).forGetter(layer -> layer.wideRandom))
            .apply(instance, InitRiverLayer::new)
    );

    private final boolean wideRandom;

    public InitRiverLayer(String id, long seed, String parent, boolean wideRandom) {
        super(id, seed, parent);
        this.wideRandom = wideRandom;
    }

    @Override
    protected LayerType<?> getType() {
        return LayerType.INIT_RIVER;
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        ExtendedBiomeId baseBiome = this.parentLayer.sample(x, z);
        if (baseBiome.baseId().equals(OCEAN.baseId())) {
            return baseBiome;
        }

        LayerRandom random = this.getRandom(x, z);
        boolean useRegionB = this.wideRandom
            ? random.nextInt(299999) % 2 == 0
            : random.nextInt(2) == 0;
        return useRegionB ? RIVER_REGION_B : RIVER_REGION_A;
    }
}
