package mod.bluestaggo.modernerbeta.fabric.data;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistryKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
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

import java.util.concurrent.CompletableFuture;

public class ModernBetaWorldGenProvider extends FabricDynamicRegistryProvider {
    public ModernBetaWorldGenProvider(FabricDataOutput output, CompletableFuture<Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        HolderLookup.RegistryLookup<Biome> registryBiome = VersionCompat.getRegistryWrapper(provider, Registries.BIOME);
        HolderLookup.RegistryLookup<ConfiguredFeature<?, ?>> registryConfiguredFeature = VersionCompat.getRegistryWrapper(provider, Registries.CONFIGURED_FEATURE);
        HolderLookup.RegistryLookup<PlacedFeature> registryPlacedFeature = VersionCompat.getRegistryWrapper(provider, Registries.PLACED_FEATURE);
        HolderLookup.RegistryLookup<ConfiguredWorldCarver<?>> registryConfiguredCarver = VersionCompat.getRegistryWrapper(provider, Registries.CONFIGURED_CARVER);
        HolderLookup.RegistryLookup<NoiseGeneratorSettings> registrySettings = VersionCompat.getRegistryWrapper(provider, Registries.NOISE_SETTINGS);
        HolderLookup.RegistryLookup<Structure> registryStructure = VersionCompat.getRegistryWrapper(provider, Registries.STRUCTURE);
        HolderLookup.RegistryLookup<StructureSet> registryStructureSet = VersionCompat.getRegistryWrapper(provider, Registries.STRUCTURE_SET);
        HolderLookup.RegistryLookup<WorldPreset> registryWorldPreset = VersionCompat.getRegistryWrapper(provider, Registries.WORLD_PRESET);
        HolderLookup.RegistryLookup<ModernBetaSettingsPreset> registrySettingsPreset = VersionCompat.getRegistryWrapper(provider, ModernBetaRegistryKeys.SETTINGS_PRESET);
        HolderLookup.RegistryLookup<ModernBetaSettingsPresetCategory> registrySettingsPresetCategory= VersionCompat.getRegistryWrapper(provider, ModernBetaRegistryKeys.SETTINGS_PRESET_CATEGORY);

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
    public String getName() {
        return ModernerBeta.MOD_NAME;
    }
}
