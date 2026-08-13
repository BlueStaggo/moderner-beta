package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.world.level.biome.Biome;

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
    protected ExtendedHolder<Biome> generate(int x, int z) {
        ExtendedHolder<Biome>[] n = this.parentLayer.sampleNeighbors(x, z);
        boolean n01 = n[0].is(n[1]);
        boolean n23 = n[2].is(n[3]);
        return n01 && n23 ? n[this.getRandom(x, z).nextInt(2) * 2]
            : n23 ? n[2]
            : n01 ? n[0]
            : this.parentLayer.sample(x, z);
    }
}
