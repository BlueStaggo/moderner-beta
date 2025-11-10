package mod.bluestaggo.modernerbeta.client.gui.screen;

import mod.bluestaggo.modernerbeta.api.level.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaChunkGenerator;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public class ModernBetaWorldScreenProvider {
    public static WorldCreationContext.DimensionsUpdater createModifier(
        CompoundTag chunkSettingsCompound,
        CompoundTag biomeSettingsCompound,
        CompoundTag caveBiomeSettingsCompound
    ) {
        return (dynamicRegistryManager, dimensionsRegistryHolder) -> {
            HolderGetter<ModernBetaSettingsPreset> registryPreset = dynamicRegistryManager.lookupOrThrow(ModernBetaResourceKeys.SETTINGS_PRESET);
            HolderGetter<SurfaceConfig> registrySurfaceConfig = dynamicRegistryManager.lookupOrThrow(ModernBetaResourceKeys.SURFACE_CONFIG);

            ModernBetaSettings chunkSettings = ModernBetaSettings.fromCompound(chunkSettingsCompound)
                .mapPreset(registryPreset, ModernBetaSettingsPreset::chunkSettings);
            ResourceKey<NoiseGeneratorSettings> modernBetaSettings = keyOfSettings(chunkSettings.getProvider());

            Registry<NoiseGeneratorSettings> registrySettings = dynamicRegistryManager
                //? if >=1.21.2 {
                .lookupOrThrow
                //? } else {
                /*.registryOrThrow
                *///? }
                    (Registries.NOISE_SETTINGS);
            Holder.Reference<NoiseGeneratorSettings> settings = registrySettings
                //? if >=1.21.2 {
                .get
                 //? } else {
                /*.getHolder
                *///? }
                    (modernBetaSettings)
                .orElseThrow();
            HolderGetter<Biome> registryBiome = dynamicRegistryManager.lookupOrThrow(Registries.BIOME);

            ModernBetaChunkGenerator chunkGenerator = new ModernBetaChunkGenerator(
                new ModernBetaBiomeSource(
                    registryBiome,
                    registryPreset,
                    biomeSettingsCompound,
                    caveBiomeSettingsCompound
                ),
                registryPreset,
                registrySurfaceConfig,
                settings,
                chunkSettingsCompound
            );

            return dimensionsRegistryHolder.replaceOverworldGenerator(dynamicRegistryManager, chunkGenerator);
        };
    }
    
    private static ResourceKey<NoiseGeneratorSettings> keyOfSettings(ResourceLocation id) {
        return ResourceKey.create(Registries.NOISE_SETTINGS, id);
    }
}
