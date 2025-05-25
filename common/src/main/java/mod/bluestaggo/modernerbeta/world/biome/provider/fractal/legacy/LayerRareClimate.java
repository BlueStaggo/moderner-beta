package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.legacy;

import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.DummyBiome;

public class LayerRareClimate extends Layer {
    public LayerRareClimate(long seed, Layer parent) {
        super(seed, parent);
    }

    @Override
    protected BiomeInfo[] getNewBiomes(int x, int z, int width, int length) {
        return forEachWithNeighbors(x, z, width, length, (input, ix, iz, neighbors) -> {
            if (!input.biome().equals(DummyBiome.CLIMATE) || input.type() == 0) return input;
            if (this.nextInt(13) == 0)
                return input.withType(input.type() | (1 + this.nextInt(15) << 8 & 0xF00));
            return input;
        });
    }
}
