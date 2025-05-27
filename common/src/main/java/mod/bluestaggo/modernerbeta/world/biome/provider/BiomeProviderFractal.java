package mod.bluestaggo.modernerbeta.world.biome.provider;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import mod.bluestaggo.modernerbeta.api.world.biome.*;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class BiomeProviderFractal extends BiomeProvider implements BiomeResolverBlock, BiomeResolverExtendedIdStepped, BiomeAccess.Storage {
	private final Supplier<RegistryEntry<Biome>> baseBiome;
	private final BiomeAccess biomeAccess;
	private final List<RegistryEntry<Biome>> allBiomes;
	private final List<Layer> allLayers;
	private final Layer layer;

	public BiomeProviderFractal(NbtCompound settings, RegistryEntryLookup<Biome> biomeRegistry, long seed) {
		super(settings, biomeRegistry, seed);

		this.baseBiome = Suppliers.memoize(() -> this.getBiomeEntry(Identifier.of(this.settings.singleBiome)).orElseThrow());
		this.biomeAccess = new BiomeAccess(this, seed);
		this.allLayers = this.settings.fractalLayers.getAllLayers();
		this.layer = this.settings.fractalLayers.getFinalLayer();
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
		return (Optional<RegistryEntry<Biome>>)(Object)biomeRegistry.getOptional(key);
	}

	@Override
	public RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
		return this.getBiomeEntry(this.getExtendedBiomeId(biomeX, biomeY, biomeZ).baseId()).orElse(this.baseBiome.get());
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
		return this.getBiomeEntry(this.getExtendedBiomeIdForStep(biomeX, biomeY, biomeZ, step).baseId()).orElseThrow();
	}

	@Override
	public ExtendedBiomeId getExtendedBiomeIdForStep(int biomeX, int biomeY, int biomeZ, int step) {
		return this.allLayers.get(step).sample(biomeX, biomeZ);
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
		return this.allLayers.size();
	}

	@Override
	public Text getStepName(int step) {
		return Text.literal(this.allLayers.get(step).toString());
	}
}
