//~dotLocation
package mod.bluestaggo.modernerbeta.level.biome.provider;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.api.level.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.api.level.biome.BiomeResolverBlock;
import mod.bluestaggo.modernerbeta.api.level.biome.BiomeResolverExtendedIdStepped;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeIds;
import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ConfiguredLayers;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkCache;
import mod.bluestaggo.modernerbeta.util.function.BiIntegerFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;

import java.util.*;
import java.util.stream.Collectors;

public class BiomeProviderFractal extends BiomeProvider implements BiomeResolverBlock, BiomeResolverExtendedIdStepped, BiomeManager.NoiseBiomeSource {
	protected final ConfiguredLayers configuredLayers;
	protected final List<Layer> pipeline;

	private final BiomeManager biomeAccess;
	private final Set<Holder<Biome>> allBiomes;
	private final Layer layer;
    private final Layer heightLayer;

	private final ChunkCache<FractalCache> chunkCacheBiomes;

    public BiomeProviderFractal(ModernBetaSettings settings, HolderGetter<Biome> biomeRegistry, long seed) {
		super(settings, biomeRegistry, seed);

		this.biomeAccess = new BiomeManager(this, seed);

		this.configuredLayers = this.settings.getOrThrow(SettingsComponentTypes.FRACTAL_LAYERS).copy(biomeRegistry);
		this.pipeline = this.configuredLayers.getPipeline();

		boolean use32BitSeed = this.settings.getOrDefault(SettingsComponentTypes.USE_32BIT_LAYER_SEED);
		if (use32BitSeed)
			seed &= 0xFFFFFFFFL;

        Set<ExtendedHolder<Biome>> allExtendedBiomes = new HashSet<>();

		this.layer = this.configuredLayers.getOutputOrThrow(ModernBetaBuiltInTypes.LayerOutput.BIOME.id);
        this.layer.addPossibleBiomesRecursive(allExtendedBiomes);

		this.heightLayer = this.configuredLayers.getOutput(ModernBetaBuiltInTypes.LayerOutput.HEIGHT.id)
			.orElse(this.layer);

		this.chunkCacheBiomes = new ChunkCache<>(
			"biomes",
			(chunkX, chunkZ) -> new FractalCache(chunkX, chunkZ, this.layer::sample, this.heightLayer::sample)
		);

        this.allBiomes = allExtendedBiomes.stream()
			.map(ExtendedHolder::base)
			.collect(Collectors.toSet());
	}

	/**
	 * Initializes the biome provider
	 */
	@Override
	public void init() {
		this.layer.init(seed);
		this.heightLayer.init(seed);
	}

	@Override
	public Holder<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
		FractalCache cache = this.chunkCacheBiomes.get(biomeX >> 2, biomeZ >> 2);
        return cache.getBiomeAt(biomeX, biomeZ).base();
	}

    @Override
	public ExtendedHolder<Biome> getExtendedBiomeId(int biomeX, int biomeY, int biomeZ) {
		FractalCache cache = this.chunkCacheBiomes.get(biomeX >> 2, biomeZ >> 2);
		return cache.getHeightAt(biomeX, biomeZ);
	}

	@Override
	public Holder<Biome> getBiomeBlock(int x, int y, int z) {
		return this.biomeAccess.getBiome(new BlockPos(x, y, z));
	}

	@Override
	public Set<Holder<Biome>> getBiomes() {
		return allBiomes;
	}

	@Override
	public Holder<Biome> getNoiseBiome(int biomeX, int biomeY, int biomeZ) {
		return this.getBiome(biomeX, biomeY, biomeZ);
	}

	@Override
	public Holder<Biome> getBiomeForStep(int biomeX, int biomeY, int biomeZ, int step) {
		return this.getExtendedBiomeIdForStep(biomeX, biomeY, biomeZ, step).base();
	}

	@Override
	public ExtendedHolder<Biome> getExtendedBiomeIdForStep(int biomeX, int biomeY, int biomeZ, int step) {
		return this.pipeline.get(step).sample(biomeX, biomeZ);
	}

	@Override
	public Component getBiomeName(int biomeX, int biomeY, int biomeZ) {
		return this.getExtendedBiomeName(this.layer.sample(biomeX, biomeZ));
	}

	@Override
	public Component getBiomeNameForStep(int biomeX, int biomeY, int biomeZ, int step) {
		return this.getExtendedBiomeName(this.getExtendedBiomeIdForStep(biomeX, biomeY, biomeZ, step));
	}

	private Component getExtendedBiomeName(ExtendedHolder<Biome> extendedBiome) {
		Component text = extendedBiome.unwrapKey()
			.map(key -> Component.translatable(key.identifier().toLanguageKey("biome")))
			.orElse(Component.literal("[unregistered]"));

		if (!extendedBiome.ext().isEmpty()) {
			text = Component.translatable(ExtendedBiomeIds.TRANSLATION_KEY, text, Component.literal(extendedBiome.ext()));
		}

		return text;
	}

	@Override
	public int getStepCount() {
		return this.pipeline.size();
	}

	@Override
	public Component getStepName(int step) {
		return Component.literal(this.pipeline.get(step).toString());
	}

	@SuppressWarnings("unchecked")
	private static class FractalCache {
		private static final int CACHE_SIZE = 4 * 4;

		private final ExtendedHolder<Biome>[] baseCache = new ExtendedHolder[CACHE_SIZE];
		private final ExtendedHolder<Biome>[] heightCache = new ExtendedHolder[CACHE_SIZE];

		public FractalCache(int chunkX, int chunkZ, BiIntegerFunction<ExtendedHolder<Biome>> biomeFunc, BiIntegerFunction<ExtendedHolder<Biome>> heightFunc) {
			int startX = chunkX << 2;
			int startZ = chunkZ << 2;

			int i = 0;
			for (int x = startX; x < startX + 4; x++) {
				for (int z = startZ; z < startZ + 4; z++) {
					this.baseCache[i] = biomeFunc.apply(x, z);
					this.heightCache[i] = heightFunc.apply(x, z);

					i++;
				}
			}
		}

		public ExtendedHolder<Biome> getBiomeAt(int biomeX, int biomeZ) {
			return this.baseCache[(biomeX & 3) << 2 | (biomeZ & 3)];
		}

		public ExtendedHolder<Biome> getHeightAt(int biomeX, int biomeZ) {
			return this.heightCache[(biomeX & 3) << 2 | (biomeZ & 3)];
		}
	}
}
