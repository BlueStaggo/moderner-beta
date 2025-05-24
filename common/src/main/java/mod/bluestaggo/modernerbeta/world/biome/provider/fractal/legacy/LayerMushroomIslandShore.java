package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.legacy;

import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.BiomeInfo;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class LayerMushroomIslandShore extends Layer {
	private final BiomeInfo mushroomIsland, ocean;

	public LayerMushroomIslandShore(Layer parent, RegistryEntry<Biome> mushroomIsland, RegistryEntry<Biome> ocean) {
		super(0, parent);
		this.mushroomIsland = BiomeInfo.of(mushroomIsland);
		this.ocean = BiomeInfo.of(ocean);
	}

	@Override
	protected BiomeInfo[] getNewBiomes(int x, int z, int width, int length) {
		return forEachWithNeighbors(x, z, width, length, (b, ix, iz, n) -> b.equals(mushroomIsland) && neighborsContain(n, ocean) ? b.asSpecial() : b);
	}
}
