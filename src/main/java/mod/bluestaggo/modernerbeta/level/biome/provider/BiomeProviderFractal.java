//~dotLocation
package mod.bluestaggo.modernerbeta.level.biome.provider;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.api.level.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.api.level.biome.BiomeResolverBlock;
import mod.bluestaggo.modernerbeta.api.level.biome.BiomeResolverExtendedIdStepped;
import mod.bluestaggo.modernerbeta.api.level.biome.*;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ConfiguredLayers;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkCache;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkClimate;
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

public class BiomeProviderFractal extends BiomeProvider implements BiomeResolverBlock, BiomeResolverExtendedIdStepped, BiomeManager.NoiseBiomeSource, BiomeResolverOcean {
	protected final ConfiguredLayers configuredLayers;
	protected final List<Layer> pipeline;

	private final BiomeManager biomeAccess;
	private final List<Holder<Biome>> allBiomes;
	private final Layer layer;
    private final Layer heightLayer;

	private final ChunkCache<FractalCache> chunkCacheBiomes;

    private final Layer oceanLayer;
    private final Layer deepOceanLayer;

	public BiomeProviderFractal(ModernBetaSettings settings, HolderGetter<Biome> biomeRegistry, long seed) {
		super(settings, biomeRegistry, seed);

		this.biomeAccess = new BiomeManager(this, seed);

		this.configuredLayers = this.settings.getOrThrow(SettingsComponentTypes.FRACTAL_LAYERS);
		this.pipeline = this.configuredLayers.getPipeline();

		boolean use32BitSeed = this.settings.getOrDefault(SettingsComponentTypes.USE_32BIT_LAYER_SEED);
		if (use32BitSeed)
			seed &= 0xFFFFFFFFL;

        Set<ExtendedBiomeId> allExtendedBiomes = new HashSet<>();

		this.layer = this.configuredLayers.getOutputOrThrow(ModernBetaBuiltInTypes.LayerOutput.BIOME.id);
		this.layer.init(seed);
        this.layer.addPossibleBiomesRecursive(allExtendedBiomes);

		this.heightLayer = this.configuredLayers.getOutput(ModernBetaBuiltInTypes.LayerOutput.HEIGHT.id)
			.orElse(this.layer);
		this.heightLayer.init(seed);

		this.chunkCacheBiomes = new ChunkCache<>(
			"climate",
			(chunkX, chunkZ) -> new FractalCache(chunkX, chunkZ, this.layer::sample, this.heightLayer::sample)
		);

        if (false/*this.settings.getOrDefault(SettingsComponentTypes.USE_OCEAN_BIOMES)*/) { //TODO
            this.oceanLayer = this.configuredLayers.getOutputOrThrow(ModernBetaBuiltInTypes.LayerOutput.OCEAN.id);
            this.oceanLayer.init(seed);
            this.oceanLayer.addPossibleBiomesRecursive(allExtendedBiomes);

            this.deepOceanLayer = this.configuredLayers.getOutput(ModernBetaBuiltInTypes.LayerOutput.DEEP_OCEAN.id)
                .orElse(this.oceanLayer);
            this.deepOceanLayer.init(seed);
            this.deepOceanLayer.addPossibleBiomesRecursive(allExtendedBiomes);
        } else {
            this.oceanLayer = null;
            this.deepOceanLayer = null;
        }

		this.allBiomes = allExtendedBiomes.stream()
			.map(biome -> this.getBiomeEntry(biome.baseId()))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.distinct()
			.toList();
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

	//TODO
    @Override
    public Holder<Biome> getOceanBiome(int biomeX, int biomeZ) {
        return this.getBiomeHolderFromId(this.oceanLayer.sample(biomeX, biomeZ).baseId());
    }

    @Override
    public Holder<Biome> getDeepOceanBiome(int biomeX, int biomeZ) {
        return this.getBiomeHolderFromId(this.deepOceanLayer.sample(biomeX, biomeZ).baseId());
    }

    @Override
	public ExtendedBiomeId getExtendedBiomeId(int biomeX, int biomeY, int biomeZ) {
		FractalCache cache = this.chunkCacheBiomes.get(biomeX >> 2, biomeZ >> 2);
		return cache.getBiomeAt(biomeX, biomeZ);
	}

	@Override
	public Holder<Biome> getBiomeBlock(int x, int y, int z) {
		return this.biomeAccess.getBiome(new BlockPos(x, y, z));
	}

	@Override
	public List<Holder<Biome>> getBiomes() {
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
	public ExtendedBiomeId getExtendedBiomeIdForStep(int biomeX, int biomeY, int biomeZ, int step) {
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

	private Component getExtendedBiomeName(ExtendedBiomeId extendedBiomeId) {
		Component text = this.getBiomeEntry(extendedBiomeId.baseId())
			.map(entry -> entry.unwrapKey()
				.map(key -> Component.translatable(key.location().toLanguageKey("biome")))
				.orElse(Component.literal("[unregistered]")))
			.orElse(Component.literal("[unregistered]"));

		if (!extendedBiomeId.ext().isEmpty()) {
			text = Component.translatable(ExtendedBiomeId.TRANSLATION_KEY, text, Component.literal(extendedBiomeId.ext()));
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

		private final ExtendedBiomeId[] baseCache = new ExtendedBiomeId[CACHE_SIZE];
		private final ExtendedBiomeId[] heightCache = new ExtendedBiomeId[CACHE_SIZE];

		public FractalCache(int chunkX, int chunkZ, BiIntegerFunction<ExtendedBiomeId> biomeFunc, BiIntegerFunction<ExtendedBiomeId> heightFunc) {
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

		public ExtendedBiomeId getBiomeAt(int biomeX, int biomeZ) {
			return this.baseCache[(biomeX & 3) << 2 | (biomeZ & 3)];
		}

		public ExtendedBiomeId getHeightAt(int biomeX, int biomeZ) {
			return this.heightCache[(biomeX & 3) << 2 | (biomeZ & 3)];
		}
	}
}
