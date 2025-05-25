package mod.bluestaggo.modernerbeta.world.biome.provider;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import mod.bluestaggo.modernerbeta.api.world.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.api.world.biome.BiomeResolverBlock;
import mod.bluestaggo.modernerbeta.api.world.biome.BiomeResolverExtendedId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.legacy.BiomeInfo;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.Layer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BiomeProviderFractal extends BiomeProvider implements BiomeResolverBlock, BiomeResolverExtendedId, BiomeAccess.Storage {
	private final BiomeAccess biomeAccess;
	private final List<RegistryEntry<Biome>> allBiomes;
	private final Layer layer;

	public BiomeProviderFractal(NbtCompound settings, RegistryEntryLookup<Biome> biomeRegistry, long seed) {
		super(settings, biomeRegistry, seed);

		this.biomeAccess = new BiomeAccess(this, seed);
		this.layer = this.settings.fractalLayers.getFinalLayer();
		this.layer.init(seed);

		Set<ExtendedBiomeId> allExtendedBiomes = new HashSet<>();
		this.layer.addPossibleBiomesRecursive(allExtendedBiomes);
		this.allBiomes = allExtendedBiomes.stream()
			.map(biome -> this.getBiomeEntry(biome.baseId()))
			.distinct()
			.toList();
	}

	private RegistryEntry<Biome> getBiomeEntry(Identifier id) {
		RegistryKey<Biome> key = RegistryKey.of(RegistryKeys.BIOME, id);
		return biomeRegistry.getOrThrow(key);
	}

	@Override
	public RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
		return this.getBiomeEntry(this.getExtendedBiomeId(biomeX, biomeY, biomeZ).baseId());
	}

	@Override
	public ExtendedBiomeId getExtendedBiomeId(int biomeX, int biomeY, int biomeZ) {
		return this.layer.sample(biomeX, biomeZ);
	}

	@Override
	public RegistryEntry<Biome> getBiomeBlock(int x, int y, int z) {
		return this.biomeAccess.getBiome(new BlockPos(x, y, z));
	}

	@Override
	public List<RegistryEntry<Biome>> getBiomes() {
		return allBiomes;
	}

	private void cleanCache(Long2ObjectMap<BiomeInfo[]> cache, int x, int z, int dist) {
		if (cache.size() < 256) {
			return;
		}
		cache.clear();
	}

	@Override
	public RegistryEntry<Biome> getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
		return this.getBiome(biomeX, biomeY, biomeZ);
	}
}
