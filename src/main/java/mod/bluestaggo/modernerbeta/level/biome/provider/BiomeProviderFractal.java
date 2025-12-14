//~dotLocation
package mod.bluestaggo.modernerbeta.level.biome.provider;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
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

	private final Supplier<Holder<Biome>> baseBiome;
	private final BiomeManager biomeAccess;
	private final List<Holder<Biome>> allBiomes;
	private final Layer layer;
    private final Layer heightLayer;
    private final Layer oceanLayer;
    private final Layer deepOceanLayer;

	public BiomeProviderFractal(ModernBetaSettings settings, HolderGetter<Biome> biomeRegistry, long seed) {
		super(settings, biomeRegistry, seed);

		this.baseBiome = Suppliers.memoize(() -> this.getBiomeEntry(this.settings.getOrDefault(SettingsComponentTypes.SINGLE_BIOME)).orElseThrow());
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

        if (this.settings.getOrDefault(SettingsComponentTypes.USE_OCEAN_BIOMES)) {
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
        return this.getBiomeHolderFromId(this.layer.sample(biomeX, biomeZ).baseId());
	}

    @Override
    public Holder<Biome> getOceanBiome(int biomeX, int biomeY, int biomeZ) {
        return this.getBiomeHolderFromId(this.oceanLayer.sample(biomeX, biomeZ).baseId());
    }

    @Override
    public Holder<Biome> getDeepOceanBiome(int biomeX, int biomeY, int biomeZ) {
        return this.getBiomeHolderFromId(this.deepOceanLayer.sample(biomeX, biomeZ).baseId());
    }

    @Override
	public ExtendedBiomeId getExtendedBiomeId(int biomeX, int biomeY, int biomeZ) {
		return this.heightLayer.sample(biomeX, biomeZ);
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
}
