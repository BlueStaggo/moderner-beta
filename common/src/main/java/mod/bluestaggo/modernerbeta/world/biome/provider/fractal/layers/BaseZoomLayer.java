package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.LayerRandom;

public abstract class BaseZoomLayer extends SingleParentLayer {
    public BaseZoomLayer(String id, long seed, String parent) {
        super(id, seed, parent);
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        int xHalf = x & 1;
        int zHalf = z & 1;

        int halfX = x >> 1;
        int halfZ = z >> 1;
        ExtendedBiomeId biome00 = this.parentLayer.sample(halfX, halfZ);
        if (xHalf == 0 && zHalf == 0) {
            return biome00;
        }

        LayerRandom random = this.getRandom(halfX << 1, halfZ << 1);
        if (xHalf == 1 && zHalf == 0) {
            ExtendedBiomeId biome10 = this.parentLayer.sample(halfX + 1, halfZ);
            return this.interpolate(random, biome00, biome10);
        } else if (xHalf == 0) {
            ExtendedBiomeId biome01 = this.parentLayer.sample(halfX, halfZ + 1);
            return this.interpolate(random, biome00, biome01);
        }

        ExtendedBiomeId biome01 = this.parentLayer.sample(halfX, halfZ + 1);
        ExtendedBiomeId biome10 = this.parentLayer.sample(halfX + 1, halfZ);
        ExtendedBiomeId biome11 = this.parentLayer.sample(halfX + 1, halfZ + 1);
        return this.interpolate(random, biome00, biome01, biome10, biome11);
    }

    protected abstract ExtendedBiomeId interpolate(LayerRandom random, ExtendedBiomeId a, ExtendedBiomeId b);

    protected ExtendedBiomeId interpolate(LayerRandom random, ExtendedBiomeId a, ExtendedBiomeId b, ExtendedBiomeId c, ExtendedBiomeId d) {
        return this.interpolate(random, this.interpolate(random, a, b), this.interpolate(random, c, d));
    }
}
