package mod.bespectacled.modernbeta.world.biome.provider;

import it.unimi.dsi.fastutil.longs.*;
import mod.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import mod.bespectacled.modernbeta.api.world.biome.BiomeResolverBlock;
import mod.bespectacled.modernbeta.api.world.biome.BiomeResolverInfo;
import mod.bespectacled.modernbeta.world.biome.provider.fractal.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;

import java.util.*;
import java.util.stream.Collectors;

public class BiomeProviderFractal extends BiomeProvider implements BiomeResolverBlock, BiomeResolverInfo {
	private final List<RegistryEntry<Biome>> allBiomes;

	private final Layer biomeGenLayer;
	private final Layer biomeBlockLayer;

	private final Long2ObjectMap<BiomeInfo[]> genCache = Long2ObjectMaps.synchronize(new Long2ObjectOpenHashMap<>());
	private final Long2ObjectMap<BiomeInfo[]> blockCache = Long2ObjectMaps.synchronize(new Long2ObjectOpenHashMap<>());
	private final Object genCacheLock = new Object();
	private final Object blockCacheLock = new Object();

	public BiomeProviderFractal(NbtCompound settings, RegistryEntryLookup<Biome> biomeRegistry, long seed) {
		super(settings, biomeRegistry, seed);

		List<BiomeInfo> selectedBiomes = this.settings.fractalBiomes.stream().map(b -> BiomeInfo.fromId(b, biomeRegistry)).toList();
		List<ClimaticBiomeList<BiomeInfo>> climaticBiomes = qualifyClimaticBiomeLists(biomeRegistry, this.settings.fractalClimaticBiomes);
		Map<BiomeInfo, BiomeInfo> hillVariants = qualifyBiomeMap(biomeRegistry, this.settings.fractalHillVariants);
		Map<BiomeInfo, BiomeInfo> edgeVariants = qualifyBiomeMap(biomeRegistry, this.settings.fractalEdgeVariants);
		Map<BiomeInfo, BiomeInfo> mutatedVariants = qualifyBiomeMap(biomeRegistry, this.settings.fractalMutatedVariants);
		Map<BiomeInfo, BiomeInfo> veryRareVariants = qualifyBiomeMap(biomeRegistry, this.settings.fractalVeryRareVariants);
		Map<BiomeInfo, List<BiomeInfo>> subVariants = qualifySubVariants(biomeRegistry, this.settings.fractalSubVariants);

		var allBiomes = new HashSet<RegistryEntry<Biome>>();
		allBiomes.add(getBiomeEntry("minecraft:ocean"));
		allBiomes.add(getBiomeEntry(this.settings.fractalPlains));
		allBiomes.add(getBiomeEntry("minecraft:river"));
		if (this.settings.fractalAddSnow) {
			allBiomes.add(getBiomeEntry("minecraft:frozen_ocean"));
			allBiomes.add(getBiomeEntry(this.settings.fractalIcePlains));
			allBiomes.add(getBiomeEntry("minecraft:frozen_river"));
		}
		if (this.settings.fractalAddSnow) allBiomes.add(getBiomeEntry("minecraft:ocean"));
		if (this.settings.fractalAddMushroomIslands) allBiomes.add(getBiomeEntry("minecraft:mushroom_fields"));
		if (this.settings.fractalAddBeaches) allBiomes.add(getBiomeEntry("minecraft:beach"));
		if (this.settings.fractalUseClimaticBiomes) {
			for (ClimaticBiomeList<BiomeInfo> climate : climaticBiomes) {
				climate.normalBiomes().forEach(b -> allBiomes.add(b.biome()));
				climate.rareBiomes().forEach(b -> allBiomes.add(b.biome()));
			}
		} else {
			selectedBiomes.stream().map(BiomeInfo::biome).forEach(allBiomes::add);
		}
		subVariants.values().forEach(v -> v.forEach(b -> allBiomes.add(b.biome())));
		this.allBiomes = allBiomes.stream().toList();

		var fractalSettings = new FractalSettings.Builder();
		fractalSettings.biomes = selectedBiomes;
		fractalSettings.climaticBiomes = climaticBiomes;
		fractalSettings.hillVariants = hillVariants;
		fractalSettings.edgeVariants = edgeVariants;
		fractalSettings.mutatedVariants = mutatedVariants;
		fractalSettings.veryRareVariants = veryRareVariants;
		fractalSettings.subVariants = subVariants;
		fractalSettings.plains = getBiomeEntry(this.settings.fractalPlains);
		fractalSettings.icePlains = getBiomeEntry(this.settings.fractalIcePlains);
		fractalSettings.biomeScale = this.settings.fractalBiomeScale;
		fractalSettings.hillScale = this.settings.fractalHillScale;
		fractalSettings.subVariantScale = this.settings.fractalSubVariantScale;
		fractalSettings.terrainType = FractalSettings.TerrainType.fromString(this.settings.fractalTerrainType);
		fractalSettings.oceans = this.settings.fractalOceans;
		fractalSettings.addRivers = this.settings.fractalAddRivers;
		fractalSettings.addSnow = this.settings.fractalAddSnow;
		fractalSettings.addMushroomIslands = this.settings.fractalAddMushroomIslands;
		fractalSettings.addBeaches = this.settings.fractalAddBeaches;
		fractalSettings.addHills = this.settings.fractalAddHills;
		fractalSettings.addSwampRivers = this.settings.fractalAddSwampRivers;
		fractalSettings.addDeepOceans = this.settings.fractalAddDeepOceans;
		fractalSettings.addMutations = this.settings.fractalAddMutations;
		fractalSettings.useClimaticBiomes = this.settings.fractalUseClimaticBiomes;

		biomeGenLayer = Layer.getLayer(biomeRegistry, seed, fractalSettings.build());
		biomeBlockLayer = new LayerVoronoiZoom(10, biomeGenLayer);
		biomeBlockLayer.setWorldSeed(seed);
	}

	private static List<ClimaticBiomeList<BiomeInfo>> qualifyClimaticBiomeLists(RegistryEntryLookup<Biome> biomeRegistry, List<ClimaticBiomeList<String>> lists) {
		return lists.stream().map(c -> ClimaticBiomeList.qualify(c, biomeRegistry)).toList();
	}

	private static Map<BiomeInfo, BiomeInfo> qualifyBiomeMap(RegistryEntryLookup<Biome> biomeRegistry, Map<String, String> map) {
		return map.entrySet().stream()
			.map(kv -> Map.entry(BiomeInfo.fromId(kv.getKey(), biomeRegistry), BiomeInfo.fromId(kv.getValue(), biomeRegistry)))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private static Map<BiomeInfo, List<BiomeInfo>> qualifySubVariants(RegistryEntryLookup<Biome> biomeRegistry, Map<String, List<String>> subVariants) {
		return subVariants.entrySet().stream()
			.map(kv -> Map.entry(BiomeInfo.fromId(kv.getKey(), biomeRegistry), kv.getValue().stream()
				.map(v -> BiomeInfo.fromId(v, biomeRegistry)).toList()))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private RegistryEntry<Biome> getBiomeEntry(String id) {
		RegistryKey<Biome> key = RegistryKey.of(RegistryKeys.BIOME, new Identifier(id));
		return biomeRegistry.getOrThrow(key);
	}

	@Override
	public RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
		return getBiomeInfo(biomeX, biomeY, biomeZ).biome();
	}

	@Override
	public BiomeInfo getBiomeInfo(int biomeX, int biomeY, int biomeZ) {
		int chunkX = biomeX >> 2;
		int chunkZ = biomeZ >> 2;
		long chunkPos = ChunkPos.toLong(chunkX, chunkZ);
		BiomeInfo[] biomes;

		synchronized (genCacheLock) {
			if (!genCache.containsKey(chunkPos)) {
				cleanCache(genCache, chunkX, chunkZ, 16);
				biomes = biomeGenLayer.getBiomes(chunkX << 2, chunkZ << 2, 4, 4);
				genCache.put(chunkPos, biomes);
			} else {
				biomes = genCache.get(chunkPos);
			}
		}

		int subX = biomeX & 3;
		int subZ = biomeZ & 3;
		return biomes[subX + (subZ << 2)];
	}

	@Override
	public RegistryEntry<Biome> getBiomeBlock(int x, int y, int z) {
		return getBiomeInfoBlock(x, z).biome();
	}

	public BiomeInfo getBiomeInfoBlock(int x, int z) {
		int chunkX = x >> 4;
		int chunkZ = z >> 4;
		long chunkPos = ChunkPos.toLong(chunkX, chunkZ);
		BiomeInfo[] biomes;

		synchronized (blockCacheLock) {
			if (!blockCache.containsKey(chunkPos)) {
				cleanCache(blockCache, chunkX, chunkZ, 16);
				biomes = biomeBlockLayer.getBiomes(chunkX << 4, chunkZ << 4, 16, 16);
				blockCache.put(chunkPos, biomes);
			} else {
				biomes = blockCache.get(chunkPos);
			}
		}

		int subX = x & 15;
		int subZ = z & 15;
		return biomes[subX + (subZ << 4)];
	}

	@Override
	public List<RegistryEntry<Biome>> getBiomes() {
		return allBiomes;
	}

	private void cleanCache(Long2ObjectMap<BiomeInfo[]> cache, int x, int z, int dist) {
		if (cache.size() < 256) {
			return;
		}

		long[] drops = cache.keySet().longStream()
			.filter(key -> {
				int keyX = ChunkPos.getPackedX(key);
				int keyZ = ChunkPos.getPackedZ(key);
				return keyX < x - dist || keyX > x + dist || keyZ < z - dist || keyZ > z + dist;
			})
			.toArray();

		for (long key : drops) {
			cache.remove(key);
		}
	}
}
