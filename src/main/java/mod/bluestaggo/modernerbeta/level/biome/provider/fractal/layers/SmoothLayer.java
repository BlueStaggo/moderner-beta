package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeId;

public class SmoothLayer extends SingleParentLayer {
    public static final com.mojang.serialization.MapCodec<SmoothLayer> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .apply(instance, SmoothLayer::new)
    );

    public SmoothLayer(String id, long seed, String parent) {
        super(id, seed, parent);
    }

    @Override
    public LayerType<?> getType() {
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
