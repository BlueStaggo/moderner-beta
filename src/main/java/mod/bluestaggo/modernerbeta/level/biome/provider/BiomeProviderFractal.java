//~dotLocation
package mod.bluestaggo.modernerbeta.level.biome.provider;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.api.level.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.api.level.biome.BiomeResolverBlock;
import mod.bluestaggo.modernerbeta.api.level.biome.BiomeResolverExtendedIdStepped;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeIds;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ConfiguredLayers;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkCache;
import mod.bluestaggo.modernerbeta.util.function.BiIntegerFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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

		this.configuredLayers = this.settings.getOrThrow(SettingsComponentTypes.FRACTAL_LAYERS);
		this.pipeline = this.configuredLayers.getPipeline();

		boolean use32BitSeed = this.settings.getOrDefault(SettingsComponentTypes.USE_32BIT_LAYER_SEED);
		if (use32BitSeed)
			seed &= 0xFFFFFFFFL;

        Set<ExtendedIdentifier> allExtendedBiomes = new HashSet<>();

		this.layer = this.configuredLayers.getOutputOrThrow(ModernBetaBuiltInTypes.LayerOutput.BIOME.id);
        this.layer.addPossibleBiomesRecursive(allExtendedBiomes);

		this.heightLayer = this.configuredLayers.getOutput(ModernBetaBuiltInTypes.LayerOutput.HEIGHT.id)
			.orElse(this.layer);

		this.chunkCacheBiomes = new ChunkCache<>(
			"biomes",
			(chunkX, chunkZ) -> new FractalCache(chunkX, chunkZ, this.layer::sample, this.heightLayer::sample)
		);

        this.allBiomes = allExtendedBiomes.stream()
			.map(biome -> this.getBiomeEntry(biome.baseId()))
			.filter(Optional::isPresent)
			.map(Optional::get)
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

	@SuppressWarnings("unchecked")
    private Optional<Holder<Biome>> getBiomeEntry(ResourceLocation id) {
		ResourceKey<Biome> key = ResourceKey.create(Registries.BIOME, id);
		return (Optional<Holder<Biome>>)(Object)this.biomeRegistry.get(key);
	}

    private Holder<Biome> getBiomeHolderFromId(ResourceLocation id) {
        return this.getBiomeEntry(id)
            .orElseThrow(() -> new NoSuchElementException("Biome \"" + id + "\" does not exist."));
    }

	@Override
	public Holder<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
		FractalCache cache = this.chunkCacheBiomes.get(biomeX >> 2, biomeZ >> 2);
        return this.getBiomeHolderFromId(cache.getBiomeAt(biomeX, biomeZ).baseId());
	}

    @Override
	public ExtendedIdentifier getExtendedBiomeId(int biomeX, int biomeY, int biomeZ) {
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
		ResourceLocation baseId = this.getExtendedBiomeIdForStep(biomeX, biomeY, biomeZ, step).baseId();
		return this.getBiomeEntry(baseId)
			.orElseThrow(() -> new NoSuchElementException("Biome \"" + baseId + "\" does not exist."));
	}

	@Override
	public ExtendedIdentifier getExtendedBiomeIdForStep(int biomeX, int biomeY, int biomeZ, int step) {
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

	private Component getExtendedBiomeName(ExtendedIdentifier extendedBiomeId) {
		Component text = this.getBiomeEntry(extendedBiomeId.baseId())
			.map(entry -> entry.unwrapKey()
				.map(key -> Component.translatable(key.location().toLanguageKey("biome")))
				.orElse(Component.literal("[unregistered]")))
			.orElse(Component.literal("[unregistered]"));

		if (!extendedBiomeId.ext().isEmpty()) {
			text = Component.translatable(ExtendedBiomeIds.TRANSLATION_KEY, text, Component.literal(extendedBiomeId.ext()));
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

	private static class FractalCache {
		private static final int CACHE_SIZE = 4 * 4;

		private final ExtendedIdentifier[] baseCache = new ExtendedIdentifier[CACHE_SIZE];
		private final ExtendedIdentifier[] heightCache = new ExtendedIdentifier[CACHE_SIZE];

		public FractalCache(int chunkX, int chunkZ, BiIntegerFunction<ExtendedIdentifier> biomeFunc, BiIntegerFunction<ExtendedIdentifier> heightFunc) {
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

		public ExtendedIdentifier getBiomeAt(int biomeX, int biomeZ) {
			return this.baseCache[(biomeX & 3) << 2 | (biomeZ & 3)];
		}

		public ExtendedIdentifier getHeightAt(int biomeX, int biomeZ) {
			return this.heightCache[(biomeX & 3) << 2 | (biomeZ & 3)];
		}
	}
}
