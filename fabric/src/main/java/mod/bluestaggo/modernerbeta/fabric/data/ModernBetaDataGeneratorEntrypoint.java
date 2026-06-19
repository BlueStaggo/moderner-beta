package mod.bluestaggo.modernerbeta.fabric.data;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.fabric.data.provider.*;
import mod.bluestaggo.modernerbeta.fabric.data.provider.tag.*;
import mod.bluestaggo.modernerbeta.fabric.data.reduced_height.provider.ModernBetaDeepslateBlobsDataProvider;
import mod.bluestaggo.modernerbeta.fabric.data.reduced_height.provider.ModernBetaReducedHeightDataProvider;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomes;
import mod.bluestaggo.modernerbeta.level.carver.configured.ModernBetaConfiguredCarvers;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaNoiseGeneratorSettings;
import mod.bluestaggo.modernerbeta.level.feature.configured.ModernBetaConfiguredFeatures;
import mod.bluestaggo.modernerbeta.level.feature.placed.ModernBetaPlacedFeatures;
import mod.bluestaggo.modernerbeta.level.preset.ModernBetaWorldPresets;
import mod.bluestaggo.modernerbeta.level.structure.ModernBetaStructures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;
import java.util.List;

public class ModernBetaDataGeneratorEntrypoint implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        ModernerBeta.GENERATING_DATA = true;

        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        List<Path> inputs = fabricDataGenerator.getModContainer().getRootPaths();
        
        pack.addProvider((FabricDataGenerator.Pack.Factory<ModernBetaStructureDatafixProvider>)
                outputx -> new ModernBetaStructureDatafixProvider(outputx, inputs));
        pack.addProvider(ModernBetaWorldGenProvider::new);
        pack.addProvider(ModernBetaTagProviderBiome::new);
        pack.addProvider(ModernBetaTagProviderBiomeSource::new);
        //? if >=26.1
        pack.addProvider(ModernBetaTagProviderConfiguredFeature::new);
        pack.addProvider(ModernBetaTagProviderBlock::new);
        pack.addProvider(ModernBetaTagProviderWorldPreset::new);
        pack.addProvider(ModernBetaTagProviderSettingsPreset::new);
        pack.addProvider(ModernBetaTagProviderSettingsPresetCategory::new);

        FabricDataGenerator.Pack reducedHeightPack = fabricDataGenerator.createBuiltinResourcePack(ModernerBeta.createId("reduced_height"));
        reducedHeightPack.addProvider(ModernBetaReducedHeightDataProvider::new);
        reducedHeightPack.addProvider((FabricDataGenerator.Pack.Factory<PackMetadataGenerator>) output -> PackMetadataGenerator.forFeaturePack(
                output, Component.translatable("dataPack.moderner_beta.reduced_height.desc")));

        FabricDataGenerator.Pack deepslateBlobsPack = fabricDataGenerator.createBuiltinResourcePack(ModernerBeta.createId("deepslate_blobs"));
        deepslateBlobsPack.addProvider(ModernBetaDeepslateBlobsDataProvider::new);
        deepslateBlobsPack.addProvider((FabricDataGenerator.Pack.Factory<PackMetadataGenerator>) output -> PackMetadataGenerator.forFeaturePack(
                output, Component.translatable("dataPack.moderner_beta.deepslate_blobs.desc")));
    }
    
    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.PLACED_FEATURE, ModernBetaPlacedFeatures::bootstrap);
        registryBuilder.add(Registries.CONFIGURED_FEATURE, ModernBetaConfiguredFeatures::bootstrap);
        registryBuilder.add(Registries.CONFIGURED_CARVER, ModernBetaConfiguredCarvers::bootstrap);
        registryBuilder.add(Registries.BIOME, ModernBetaBiomes::bootstrap);
        registryBuilder.add(Registries.NOISE_SETTINGS, ModernBetaNoiseGeneratorSettings::bootstrap);
        registryBuilder.add(Registries.STRUCTURE, ModernBetaStructures::bootstrap);
        registryBuilder.add(Registries.WORLD_PRESET, ModernBetaWorldPresets::bootstrap);
        registryBuilder.add(ModernBetaResourceKeys.SETTINGS_PRESET, ModernBetaSettingsPresets::bootstrap);
        registryBuilder.add(ModernBetaResourceKeys.SETTINGS_PRESET_CATEGORY, ModernBetaSettingsPresetCategories::bootstrap);
        registryBuilder.add(ModernBetaResourceKeys.SURFACE_CONFIG, ModernBetaSurfaceConfigs::bootstrap);
    }
}
