package mod.bluestaggo.modernerbeta.fabric.data;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.fabric.data.reduced_height.ModernBetaDeepslateBlobsDataProvider;
import mod.bluestaggo.modernerbeta.fabric.data.reduced_height.ModernBetaReducedHeightDataProvider;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistryKeys;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomes;
import mod.bluestaggo.modernerbeta.world.carver.configured.ModernBetaConfiguredCarvers;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGeneratorSettings;
import mod.bluestaggo.modernerbeta.world.feature.configured.ModernBetaConfiguredFeatures;
import mod.bluestaggo.modernerbeta.world.feature.placed.ModernBetaPlacedFeatures;
import mod.bluestaggo.modernerbeta.world.preset.ModernBetaWorldPresets;
import mod.bluestaggo.modernerbeta.world.structure.ModernBetaStructureSets;
import mod.bluestaggo.modernerbeta.world.structure.ModernBetaStructures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.MetadataProvider;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

import java.nio.file.Path;
import java.util.List;

public class ModernBetaDataGeneratorEntrypoint implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        List<Path> inputs = fabricDataGenerator.getModContainer().getRootPaths();
        
        pack.addProvider((FabricDataGenerator.Pack.Factory<ModernBetaStructureDatafixApplier>)
                outputx -> new ModernBetaStructureDatafixApplier(outputx, inputs));
        pack.addProvider(ModernBetaWorldGenProvider::new);
        pack.addProvider(ModernBetaTagProviderBiome::new);
        pack.addProvider(ModernBetaTagProviderStructure::new);
        pack.addProvider(ModernBetaTagProviderBlock::new);
        pack.addProvider(ModernBetaTagProviderWorldPreset::new);
        pack.addProvider(ModernBetaTagProviderSettingsPresetCategory::new);

        FabricDataGenerator.Pack reducedHeightPack = fabricDataGenerator.createBuiltinResourcePack(ModernerBeta.createId("reduced_height"));
        reducedHeightPack.addProvider(ModernBetaReducedHeightDataProvider::new);
        reducedHeightPack.addProvider((FabricDataGenerator.Pack.Factory<MetadataProvider>) output -> MetadataProvider.create(
                output, Text.translatable("dataPack.moderner_beta.reduced_height.name")));

        FabricDataGenerator.Pack deepslateBlobsPack = fabricDataGenerator.createBuiltinResourcePack(ModernerBeta.createId("deepslate_blobs"));
        deepslateBlobsPack.addProvider(ModernBetaDeepslateBlobsDataProvider::new);
        deepslateBlobsPack.addProvider((FabricDataGenerator.Pack.Factory<MetadataProvider>) output -> MetadataProvider.create(
                output, Text.translatable("dataPack.moderner_beta.deepslate_blobs.name")));
    }
    
    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, ModernBetaPlacedFeatures::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ModernBetaConfiguredFeatures::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_CARVER, ModernBetaConfiguredCarvers::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.BIOME, ModernBetaBiomes::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBetaChunkGeneratorSettings::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.STRUCTURE, ModernBetaStructures::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.STRUCTURE_SET, ModernBetaStructureSets::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.WORLD_PRESET, ModernBetaWorldPresets::bootstrap);
        registryBuilder.addRegistry(ModernBetaRegistryKeys.SETTINGS_PRESET, ModernBetaSettingsPresets::bootstrap);
        registryBuilder.addRegistry(ModernBetaRegistryKeys.SETTINGS_PRESET_CATEGORY, ModernBetaSettingsPresetCategories::bootstrap);
    }
}
