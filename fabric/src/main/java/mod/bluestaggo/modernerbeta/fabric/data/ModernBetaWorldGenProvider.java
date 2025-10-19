package mod.bluestaggo.modernerbeta.fabric.data;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModernBetaLevelGenProvider extends FabricDynamicRegistryProvider {
    public ModernBetaLevelGenProvider(FabricDataOutput output, CompletableFuture<Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        HolderLookup.RegistryLookup<Biome> registryBiome = provider.lookupOrThrow(Registries.BIOME);
        HolderLookup.RegistryLookup<ConfiguredFeature<?, ?>> registryConfiguredFeature = provider.lookupOrThrow(Registries.CONFIGURED_FEATURE);
        HolderLookup.RegistryLookup<PlacedFeature> registryPlacedFeature = provider.lookupOrThrow(Registries.PLACED_FEATURE);
        HolderLookup.RegistryLookup<ConfiguredWorldCarver<?>> registryConfiguredCarver = provider.lookupOrThrow(Registries.CONFIGURED_CARVER);
        HolderLookup.RegistryLookup<NoiseGeneratorSettings> registrySettings = provider.lookupOrThrow(Registries.NOISE_SETTINGS);
        HolderLookup.RegistryLookup<Structure> registryStructure = provider.lookupOrThrow(Registries.STRUCTURE);
        HolderLookup.RegistryLookup<StructureSet> registryStructureSet = provider.lookupOrThrow(Registries.STRUCTURE_SET);
        HolderLookup.RegistryLookup<WorldPreset> registryWorldPreset = provider.lookupOrThrow(Registries.WORLD_PRESET);
        HolderLookup.RegistryLookup<ModernBetaSettingsPreset> registrySettingsPreset = provider.lookupOrThrow(ModernBetaResourceKeys.SETTINGS_PRESET);
        HolderLookup.RegistryLookup<ModernBetaSettingsPresetCategory> registrySettingsPresetCategory= provider.lookupOrThrow(ModernBetaResourceKeys.SETTINGS_PRESET_CATEGORY);

        entries.addAll(registryBiome);
        entries.addAll(registryConfiguredFeature);
        entries.addAll(registryPlacedFeature);
        entries.addAll(registryConfiguredCarver);
        entries.addAll(registrySettings);
        entries.addAll(registryStructure);
        entries.addAll(registryStructureSet);
        entries.addAll(registryWorldPreset);
        entries.addAll(registrySettingsPreset);
        entries.addAll(registrySettingsPresetCategory);
    }

    @Override
    public @NotNull String getName() {
        return ModernerBeta.MOD_NAME;
    }
}
