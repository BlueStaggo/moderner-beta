package mod.bluestaggo.modernerbeta.fabric;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.ModernerBetaClient;
import mod.bluestaggo.modernerbeta.client.color.BlockColorSampler;
import mod.bluestaggo.modernerbeta.client.color.BlockColors;
import mod.bluestaggo.modernerbeta.client.registry.ModernBetaClientRegistries;
import mod.bluestaggo.modernerbeta.compat.client.ModCompatClient;
import mod.bluestaggo.modernerbeta.fabric.client.resource.ModernBetaFabricColormapResource;
import mod.bluestaggo.modernerbeta.fabric.registry.RegistryHelperImpl;
import mod.bluestaggo.modernerbeta.network.BiomeProviderInfoPayload;
import mod.bluestaggo.modernerbeta.network.S2CPacketHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.PackType;

public class ModernerBetaFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModernerBetaClient.init();
        ModCompatClient.initialise(FabricLoader.getInstance()::isModLoaded);
        ModernBetaClientRegistries.makeRegistries(new RegistryHelperImpl());
        ModernerBetaClient.setupCustomRegistryHandlers();

        ModernerBetaInitializer.setupRegistryHandlers(ModernerBetaClient.CUSTOM_REGISTRY_HANDLERS);

        BlockColors.register(ColorProviderRegistry.BLOCK::register);

        ResourceManagerHelper resourceManager = ResourceManagerHelper.get(PackType.CLIENT_RESOURCES);
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


        //? if >=1.20.2 {
        ClientPlayNetworking.registerGlobalReceiver(BiomeProviderInfoPayload.ID, (payload, context) -> {
            @SuppressWarnings("resource")
            Minecraft client = context.client();
            client.execute(() -> S2CPacketHandlers.onBiomeProviderInfo(client.level, payload));
        });
        //?} else {
        /*ClientPlayNetworking.registerGlobalReceiver(BiomeProviderInfoPayload.ID, (minecraftClient, clientPlayNetworkHandler, packetByteBuf, packetSender) -> {
            BiomeProviderInfoPayload payload = BiomeProviderInfoPayload.fromFriendlyByteBuf(packetByteBuf);
            minecraftClient.execute(() -> S2CPacketHandlers.onBiomeProviderInfo(minecraftClient.level, payload));
        });
        *///?}
    }
}
