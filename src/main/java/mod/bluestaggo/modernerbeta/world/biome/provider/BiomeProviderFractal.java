package mod.bluestaggo.modernerbeta.world.biome.provider;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.api.world.biome.*;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ConfiguredLayers;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;

import java.util.*;

public class BiomeProviderFractal extends BiomeProvider implements BiomeResolverBlock, BiomeResolverExtendedIdStepped, BiomeAccess.Storage {
	protected final ConfiguredLayers configuredLayers;
	protected final List<Layer> pipeline;

	private final Supplier<RegistryEntry<Biome>> baseBiome;
	private final BiomeAccess biomeAccess;
	private final List<RegistryEntry<Biome>> allBiomes;
	private final Layer layer;

	public BiomeProviderFractal(ModernBetaSettings settings, RegistryEntryLookup<Biome> biomeRegistry, long seed) {
		super(settings, biomeRegistry, seed);

		this.baseBiome = Suppliers.memoize(() -> this.getBiomeEntry(this.settings.getOrDefault(SettingsComponentTypes.SINGLE_BIOME)).orElseThrow());
		this.biomeAccess = new BiomeAccess(this, seed);

		this.configuredLayers = this.settings.getOrThrow(SettingsComponentTypes.FRACTAL_LAYERS);
		this.pipeline = this.configuredLayers.getPipeline();

		boolean use32BitSeed = this.settings.getOrThrow(SettingsComponentTypes.USE_32BIT_LAYER_SEED);
		if (use32BitSeed)
			seed &= 0xFFFFFFFFL;

		this.layer = this.configuredLayers.getOutputOrThrow(ModernBetaBuiltInTypes.LayerOutput.BIOME.id);
		this.layer.init(seed);

		Set<ExtendedBiomeId> allExtendedBiomes = new HashSet<>();
		this.layer.addPossibleBiomesRecursive(allExtendedBiomes);
		this.allBiomes = allExtendedBiomes.stream()
			.map(biome -> this.getBiomeEntry(biome.baseId()))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.distinct()
			.toList();
	}

	@SuppressWarnings("unchecked")
    private Optional<RegistryEntry<Biome>> getBiomeEntry(Identifier id) {
		RegistryKey<Biome> key = RegistryKey.of(RegistryKeys.BIOME, id);
		return (Optional<RegistryEntry<Biome>>)(Object)this.biomeRegistry.getOptional(key);
	}

	@Override
	public RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
		Identifier baseId = this.getExtendedBiomeId(biomeX, biomeY, biomeZ).baseId();
		return this.getBiomeEntry(baseId)
			.orElseThrow(() -> new NoSuchElementException("Biome \"" + baseId + "\" does not exist."));
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

	@Override
	public RegistryEntry<Biome> getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
		return this.getBiome(biomeX, biomeY, biomeZ);
	}

	@Override
	public RegistryEntry<Biome> getBiomeForStep(int biomeX, int biomeY, int biomeZ, int step) {
		Identifier baseId = this.getExtendedBiomeIdForStep(biomeX, biomeY, biomeZ, step).baseId();
		return this.getBiomeEntry(baseId)
			.orElseThrow(() -> new NoSuchElementException("Biome \"" + baseId + "\" does not exist."));
	}

	@Override
	public ExtendedBiomeId getExtendedBiomeIdForStep(int biomeX, int biomeY, int biomeZ, int step) {
		return this.pipeline.get(step).sample(biomeX, biomeZ);
	}

	@Override
	public Text getBiomeName(int biomeX, int biomeY, int biomeZ) {
		return this.getExtendedBiomeName(this.getExtendedBiomeId(biomeX, biomeY, biomeZ));
	}

	@Override
	public Text getBiomeNameForStep(int biomeX, int biomeY, int biomeZ, int step) {
		return this.getExtendedBiomeName(this.getExtendedBiomeIdForStep(biomeX, biomeY, biomeZ, step));
	}

	private Text getExtendedBiomeName(ExtendedBiomeId extendedBiomeId) {
		Text text = this.getBiomeEntry(extendedBiomeId.baseId())
			.map(entry -> entry.getKey()
				.map(key -> Text.translatable(key.getValue().toTranslationKey("biome")))
				.orElse(Text.literal("[unregistered]")))
			.orElse(Text.literal("[unregistered]"));

		if (!extendedBiomeId.ext().isEmpty()) {
			text = Text.translatable(ExtendedBiomeId.TRANSLATION_KEY, text, Text.literal(extendedBiomeId.ext()));
		}

		return text;
	}

	@Override
	public int getStepCount() {
		return this.pipeline.size();
	}

	@Override
	public Text getStepName(int step) {
		return Text.literal(this.pipeline.get(step).toString());
	}
}
