package mod.bluestaggo.modernerbeta.network;

import mod.bluestaggo.modernerbeta.api.world.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSamplerSky;
import mod.bluestaggo.modernerbeta.api.world.provider.BiomeProviderType;
import mod.bluestaggo.modernerbeta.client.color.BlockColorSampler;
import mod.bluestaggo.modernerbeta.client.color.SkyColorSampler;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaWorld;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class S2CPacketHandlers {
    public static void onBiomeProviderInfo(World world, BiomeProviderInfoPayload payload) {
        BlockColorSampler.INSTANCE.setClimateSampler(null);
        SkyColorSampler.INSTANCE.setClimateSampler(null);

        if (payload.isModernBetaWorld()) {
            if (world != null) {
                ((ModernBetaWorld) world).modernerBeta$setModded(true);
            }

            if (!payload.hasBiomeProvider())
                return;

            RegistryEntryLookup<Biome> biomeRegistry = world.getRegistryManager()
                //? if >=1.21.2 {
                .getOrThrow(RegistryKeys.BIOME);
                //?} else {
                /*.getWrapperOrThrow(RegistryKeys.BIOME);
                *///?}
            BiomeProviderType<?> providerCreator = ModernBetaRegistries.BIOME.get(payload.providerId().orElseThrow());
            BiomeProvider provider = providerCreator.apply(ModernBetaSettings.fromCompound(payload.settings().orElseThrow()), biomeRegistry, payload.seed().orElseThrow());
            ((ModernBetaWorld) world).modernerBeta$setTemperatureHeightScaling(provider.getTemperatureHeightScaling());

            if (provider instanceof ClimateSampler climateSampler) {
                BlockColorSampler.INSTANCE.setClimateSampler(climateSampler);
                ((ModernBetaWorld) world).modernerBeta$setClimateSampler(climateSampler);
            }

            if (provider instanceof ClimateSamplerSky climateSamplerSky) {
                SkyColorSampler.INSTANCE.setClimateSampler(climateSamplerSky);
            }
        } else if (world != null) {
            ((ModernBetaWorld) world).modernerBeta$setModded(false);
        }
    }
}
