package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.LayerType;

public class SmoothLayer extends SingleParentLayer {
    public static final MapCodec<SmoothLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .apply(instance, SmoothLayer::new)
    );

    public SmoothLayer(String id, long seed, String parent) {
        super(id, seed, parent);
    }

    @Override
    protected LayerType<?> getType() {
        return LayerType.SMOOTH;
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        ExtendedBiomeId[] n = this.parentLayer.sampleNeighbors(x, z);
        boolean n01 = n[0].equals(n[1]);
        boolean n23 = n[2].equals(n[3]);
        return n01 && n23 ? n[this.getRandom(x, z).nextInt(2) * 2]
            : n23 ? n[2]
            : n01 ? n[0]
            : this.parentLayer.sample(x, z);
    }
}
