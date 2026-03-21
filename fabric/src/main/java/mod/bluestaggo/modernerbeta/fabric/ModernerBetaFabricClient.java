package mod.bluestaggo.modernerbeta.fabric;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.ModernerBetaClient;
import mod.bluestaggo.modernerbeta.client.color.block.BlockColorSampler;
import mod.bluestaggo.modernerbeta.client.color.block.BlockColors;
import mod.bluestaggo.modernerbeta.client.registry.ModernBetaClientRegistries;
import mod.bluestaggo.modernerbeta.client.resource.ModernBetaColormapReloadListener;
import mod.bluestaggo.modernerbeta.compat.client.ModCompatClient;
import mod.bluestaggo.modernerbeta.fabric.registry.RegistryHelperImpl;
import mod.bluestaggo.modernerbeta.network.BiomeProviderInfoPayload;
import mod.bluestaggo.modernerbeta.network.S2CPacketHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;

public class ModernerBetaFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModernerBetaClient.init();
        ModCompatClient.initialise(FabricLoader.getInstance()::isModLoaded);
        ModernBetaClientRegistries.makeRegistries(new RegistryHelperImpl());
        ModernerBetaClient.setupCustomRegistryHandlers();

        ModernerBetaInitializer.setupRegistryHandlers(ModernerBetaClient.CUSTOM_REGISTRY_HANDLERS);

        BlockColors.register(net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry.BLOCK::register);

        registerReloadListener(
            ModernerBeta.createId("water_colormap"),
            new ModernBetaColormapReloadListener(
                "textures/colormap/water.png",
                BlockColorSampler.INSTANCE.colormapWater::setColormap
            )
        );
        registerReloadListener(
            ModernerBeta.createId("underwater_colormap"),
            new ModernBetaColormapReloadListener(
                "textures/colormap/underwater.png",
                BlockColorSampler.INSTANCE.colormapUnderwater::setColormap
            )
        );

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

    private <T> void registerReloadListener(
        ResourceLocation id,
        SimplePreparableReloadListener<T> listener
    ) {
        //? if >=1.21.9 {
        /*net.fabricmc.fabric.api.resource.v1.ResourceLoader resourceManager =
                net.fabricmc.fabric.api.resource.v1.ResourceLoader.get(PackType.CLIENT_RESOURCES);
        //~ if >=26.1 'registerReloader' -> 'registerReloadListener'
        resourceManager.registerReloader(id, listener);
        *///? } else {
        net.fabricmc.fabric.api.resource.ResourceManagerHelper resourceManager =
                net.fabricmc.fabric.api.resource.ResourceManagerHelper.get(PackType.CLIENT_RESOURCES);

        resourceManager.registerReloadListener(new net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return id;
            }

            @Override
            public java.util.concurrent.CompletableFuture<Void> reload(
                PreparationBarrier barrier,
                net.minecraft.server.packs.resources.ResourceManager manager,
                //? if <1.21.2 {
                /*net.minecraft.util.profiling.ProfilerFiller preparationsProfiler,
                net.minecraft.util.profiling.ProfilerFiller reloadProfiler,
                *///? }
                java.util.concurrent.Executor backgroundExecutor,
                java.util.concurrent.Executor gameExecutor
            ) {
                return listener.reload(
                    barrier,
                    manager,
                    //? if <1.21.2 {
                    /*preparationsProfiler,
                    reloadProfiler,
                    *///? }
                    backgroundExecutor,
                    gameExecutor
                );
            }
        });
        //? }
    }
}
