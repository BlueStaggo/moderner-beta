package mod.bluestaggo.modernerbeta.fabric.data;

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
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class ModernBetaDataGeneratorEntrypoint implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        
        pack.addProvider(ModernBetaWorldGenProvider::new);
        pack.addProvider(ModernBetaTagProviderBiome::new);
        pack.addProvider(ModernBetaTagProviderStructure::new);
        pack.addProvider(ModernBetaTagProviderBlock::new);
        pack.addProvider(ModernBetaTagProviderWorldPreset::new);
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
    }
}
