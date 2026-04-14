package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.util.VersionCompat;

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
    protected ExtendedIdentifier generate(int x, int z) {
        return this.parentLayer.generate(x, z);
    }

    @Override
    public ExtendedIdentifier sample(int x, int z) {
        return this.parentLayer.sample(x, z);
    }
}
