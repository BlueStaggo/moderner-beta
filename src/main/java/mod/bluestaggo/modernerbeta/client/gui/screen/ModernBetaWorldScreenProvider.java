package mod.bluestaggo.modernerbeta.client.gui.screen;

import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

@Environment(EnvType.CLIENT)
public class ModernBetaWorldScreenProvider {
    public static GeneratorOptionsHolder.RegistryAwareModifier createModifier(
        NbtCompound chunkSettingsCompound,
        NbtCompound biomeSettingsCompound,
        NbtCompound caveBiomeSettingsCompound
    ) {
        return (dynamicRegistryManager, dimensionsRegistryHolder) -> {
            ModernBetaSettings chunkSettings = ModernBetaSettings.fromCompound(chunkSettingsCompound);
            RegistryKey<ChunkGeneratorSettings> modernBetaSettings = keyOfSettings(chunkSettings.getProvider());
            
            Registry<ChunkGeneratorSettings> registrySettings = dynamicRegistryManager.getOrThrow(RegistryKeys.CHUNK_GENERATOR_SETTINGS);
            RegistryEntry.Reference<ChunkGeneratorSettings> settings = registrySettings
                //? if >=1.21.2 {
                .getOrThrow(modernBetaSettings);
                //?} else {
                /*.getEntry(modernBetaSettings)
                .orElseThrow();
                *///?}
            RegistryEntryLookup<Biome> registryBiome = dynamicRegistryManager
                //? if >=1.21.2 {
                .getOrThrow(RegistryKeys.BIOME);
                //?} else {
                /*.getWrapperOrThrow(RegistryKeys.BIOME);
                *///?}

            ModernBetaChunkGenerator chunkGenerator = new ModernBetaChunkGenerator(
                new ModernBetaBiomeSource(
                    registryBiome,
                    biomeSettingsCompound,
                    caveBiomeSettingsCompound
                ),
                settings,
                chunkSettingsCompound
            );
            
            return dimensionsRegistryHolder.with(dynamicRegistryManager, chunkGenerator);
        };
    }
    
    private static RegistryKey<ChunkGeneratorSettings> keyOfSettings(Identifier id) {
        return RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, id);
    }
}
