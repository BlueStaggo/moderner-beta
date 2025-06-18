package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;

public class ProxyLayer extends SingleParentLayer {
    public static final com.mojang.serialization.MapCodec<ProxyLayer> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .apply(instance, ProxyLayer::new)
    );

    public ProxyLayer(String id, long seed, String parent) {
        super(id, seed, parent);
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.PROXY_LAYER;
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        return this.parentLayer.generate(x, z);
    }

    @Override
    public ExtendedBiomeId sample(int x, int z) {
        return this.parentLayer.sample(x, z);
    }
}
