package mod.bespectacled.modernbeta.world.biome.provider.fractal;

public class LayerComputeRiver extends Layer {
	public LayerComputeRiver(long seed, Layer parent) {
		super(seed, parent);
	}

	@Override
	protected BiomeInfo[] getNewBiomes(int x, int z, int width, int length) {
		return forEachWithNeighbors(x, z, width, length, (b, ix, iz, n) ->
			b.biome().equals(DummyBiome.RIVER) && b.type() >= 2 && (b.biome().equals(DummyBiome.OCEAN) || neighborsContain(n, DummyBiome.OCEAN.biomeInfo))
				|| neighborsRiverBorder(n, b) ? DummyBiome.RIVER.biomeInfo : BiomeInfo.of(null));
	}

	private static boolean neighborsRiverBorder(BiomeInfo[] neighbors, BiomeInfo match) {
		for (BiomeInfo neighbor : neighbors) {
			if (neighbor.type() % 2 != match.type() % 2) return true;
		}
		return false;
	}
}
