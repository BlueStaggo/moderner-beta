package mod.bluestaggo.modernerbeta.fabric;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.color.BlockColorSampler;
import mod.bluestaggo.modernerbeta.client.color.BlockColors;
import mod.bluestaggo.modernerbeta.fabric.client.resource.ModernBetaFabricColormapResource;
import mod.bluestaggo.modernerbeta.network.BiomeProviderInfoPayload;
import mod.bluestaggo.modernerbeta.network.S2CPacketHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceType;

public class ModernerBetaFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockColors.register(ColorProviderRegistry.BLOCK::register);

        ResourceManagerHelper resourceManager = ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES);
        resourceManager.registerReloadListener(new ModernBetaFabricColormapResource(
                ModernerBeta.createId("water_colormap"),
                "textures/colormap/water.png",
                BlockColorSampler.INSTANCE.colormapWater::setColormap
        ));

        resourceManager.registerReloadListener(new ModernBetaFabricColormapResource(
                ModernerBeta.createId("underwater_colormap"),
                "textures/colormap/underwater.png",
                BlockColorSampler.INSTANCE.colormapUnderwater::setColormap
        ));

        ClientPlayNetworking.registerGlobalReceiver(BiomeProviderInfoPayload.ID, (payload, context) -> {
            @SuppressWarnings("resource")
            MinecraftClient client = context.client();
            client.execute(() -> S2CPacketHandlers.onBiomeProviderInfo(client.world, payload));
        });
    }
}
