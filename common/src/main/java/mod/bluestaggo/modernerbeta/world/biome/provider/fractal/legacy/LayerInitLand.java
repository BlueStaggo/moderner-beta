package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.legacy;

import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.DummyBiome;

public class LayerInitLand extends Layer {
	public LayerInitLand(long seed) {
		super(seed);
	}

	@Override
	protected BiomeInfo[] getNewBiomes(int x, int z, int width, int length) {
		BiomeInfo[] output = this.forEach(x, z, width, length, i ->
			nextInt(10) == 0 ? DummyBiome.PLAINS.biomeInfo : DummyBiome.OCEAN.biomeInfo);

		if (x > -width && x <= 0 && z > -length && z <= 0) {
			output[-x + -z * width] = DummyBiome.PLAINS.biomeInfo;
		}

		return output;
	}
}
