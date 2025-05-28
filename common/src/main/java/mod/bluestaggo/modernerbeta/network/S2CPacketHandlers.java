package mod.bluestaggo.modernerbeta.network;

import mod.bluestaggo.modernerbeta.api.world.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSamplerSky;
import mod.bluestaggo.modernerbeta.api.world.provider.BiomeProviderCreator;
import mod.bluestaggo.modernerbeta.client.color.BlockColorSampler;
import mod.bluestaggo.modernerbeta.client.color.SkyColorSampler;
import mod.bluestaggo.modernerbeta.client.world.ModernBetaClientWorld;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import net.minecraft.world.World;

public class S2CPacketHandlers {
    public static void onBiomeProviderInfo(World world, BiomeProviderInfoPayload payload) {
        BlockColorSampler.INSTANCE.setClimateSampler(null);
        SkyColorSampler.INSTANCE.setClimateSampler(null);

        if (payload.isModernBetaWorld()) {
            if (world != null) {
                ((ModernBetaClientWorld) world).setModernBetaWorld(true);
            }

            if (!payload.hasBiomeProvider())
                return;

            BiomeProviderCreator<?> providerCreator = ModernBetaRegistries.BIOME.get(payload.providerId().orElseThrow());
            BiomeProvider provider = providerCreator.apply(payload.settings().orElseThrow(), null, payload.seed().orElseThrow());

            if (provider instanceof ClimateSampler climateSampler) {
                BlockColorSampler.INSTANCE.setClimateSampler(climateSampler);
            }

            if (provider instanceof ClimateSamplerSky climateSamplerSky) {
                SkyColorSampler.INSTANCE.setClimateSampler(climateSamplerSky);
            }
        } else if (world != null) {
            ((ModernBetaClientWorld) world).setModernBetaWorld(false);
        }
    }
}
