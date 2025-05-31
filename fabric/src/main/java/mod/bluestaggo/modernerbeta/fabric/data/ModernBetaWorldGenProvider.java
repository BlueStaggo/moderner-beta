package mod.bluestaggo.modernerbeta.fabric.data;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistryKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.structure.StructureSet;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.structure.Structure;

import java.util.concurrent.CompletableFuture;

public class ModernBetaWorldGenProvider extends FabricDynamicRegistryProvider {
    public ModernBetaWorldGenProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        RegistryWrapper.Impl<Biome> registryBiome = registries.getOrThrow(RegistryKeys.BIOME);
        RegistryWrapper.Impl<ConfiguredFeature<?, ?>> registryConfiguredFeature = registries.getOrThrow(RegistryKeys.CONFIGURED_FEATURE);
        RegistryWrapper.Impl<PlacedFeature> registryPlacedFeature = registries.getOrThrow(RegistryKeys.PLACED_FEATURE);
        RegistryWrapper.Impl<ConfiguredCarver<?>> registryConfiguredCarver = registries.getOrThrow(RegistryKeys.CONFIGURED_CARVER);
        RegistryWrapper.Impl<ChunkGeneratorSettings> registrySettings = registries.getOrThrow(RegistryKeys.CHUNK_GENERATOR_SETTINGS);
        RegistryWrapper.Impl<Structure> registryStructure = registries.getOrThrow(RegistryKeys.STRUCTURE);
        RegistryWrapper.Impl<StructureSet> registryStructureSet = registries.getOrThrow(RegistryKeys.STRUCTURE_SET);
        RegistryWrapper.Impl<WorldPreset> registryWorldPreset = registries.getOrThrow(RegistryKeys.WORLD_PRESET);
        RegistryWrapper.Impl<ModernBetaSettingsPreset> registrySettingsPreset = registries.getOrThrow(ModernBetaRegistryKeys.SETTINGS_PRESET);
        RegistryWrapper.Impl<ModernBetaSettingsPresetCategory> registrySettingsPresetCategory= registries.getOrThrow(ModernBetaRegistryKeys.SETTINGS_PRESET_CATEGORY);

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
