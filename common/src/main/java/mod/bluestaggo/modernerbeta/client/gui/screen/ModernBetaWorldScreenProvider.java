package mod.bluestaggo.modernerbeta.client.gui.screen;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsChunk;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class ModernBetaWorldScreenProvider {
    public static GeneratorOptionsHolder.RegistryAwareModifier createModifier(
        NbtCompound chunkSettings,
        NbtCompound biomeSettings,
        NbtCompound caveBiomeSettings
    ) {
        return (dynamicRegistryManager, dimensionsRegistryHolder) -> {
            ModernBetaSettingsChunk modernBetaSettingsChunk = ModernBetaSettingsChunk.fromCompound(chunkSettings);
            RegistryKey<ChunkGeneratorSettings> modernBetaSettings = keyOfSettings(modernBetaSettingsChunk.chunkProvider);
            
            Registry<ChunkGeneratorSettings> registrySettings = dynamicRegistryManager.getOrThrow(RegistryKeys.CHUNK_GENERATOR_SETTINGS);
            RegistryEntry.Reference<ChunkGeneratorSettings> settings = registrySettings.getOrThrow(modernBetaSettings);
            RegistryEntryLookup<Biome> registryBiome = dynamicRegistryManager.getOrThrow(RegistryKeys.BIOME);
            
            ModernBetaChunkGenerator chunkGenerator = new ModernBetaChunkGenerator(
                new ModernBetaBiomeSource(
                    registryBiome,
                    biomeSettings,
                    caveBiomeSettings
                ),
                settings,
                chunkSettings
            );
            
            return dimensionsRegistryHolder.with(dynamicRegistryManager, chunkGenerator);
        };
    }
    
    private static RegistryKey<ChunkGeneratorSettings> keyOfSettings(String id) {
        return RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernerBeta.createId(id));
    }
}
