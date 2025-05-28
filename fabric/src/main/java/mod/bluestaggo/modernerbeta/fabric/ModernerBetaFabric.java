package mod.bluestaggo.modernerbeta.fabric;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.command.DebugProviderSettingsCommand;
import mod.bluestaggo.modernerbeta.fabric.network.NetworkHelperImpl;
import mod.bluestaggo.modernerbeta.fabric.registry.RegistryHelperImpl;
import mod.bluestaggo.modernerbeta.network.BiomeProviderInfoPayload;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.registry.VanillaRegistryHandler;
import mod.bluestaggo.modernerbeta.world.ModernBetaWorldInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.registry.Registry;

import java.util.Map;
import java.util.function.Consumer;

public class ModernerBetaFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // Register mod stuff
        ModernBetaRegistries.makeRegistries(new RegistryHelperImpl());
        ModernerBeta.setupCustomRegistryHandlers();

        registerDataPacks();
        ModernerBeta.init();

        setupRegistryHandlers(ModernerBeta.REGISTRY_HANDLERS);
        setupRegistryHandlers(ModernerBeta.CUSTOM_REGISTRY_HANDLERS);

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            CommandRegistrationCallback.EVENT.register(DebugProviderSettingsCommand::register);
            ModernerBeta.DEV_ENV = true;
        }

        ServerLifecycleEvents.SERVER_STARTING.register(ModernBetaWorldInitializer::init);

        ModernerBeta.networkHelper = new NetworkHelperImpl();
        PayloadTypeRegistry.playS2C().register(BiomeProviderInfoPayload.ID, BiomeProviderInfoPayload.CODEC);
    }

    private static void registerDataPacks() {
        ModContainer modContainer = FabricLoader.getInstance().getModContainer(ModernerBeta.MOD_ID).orElseThrow();
        ResourceManagerHelper.registerBuiltinResourcePack(ModernerBeta.createId("reduced_height"), modContainer, ResourcePackActivationType.NORMAL);
    }

    private static void setupRegistryHandlers(Map<Registry<?>, Consumer<IRegistryHandler<?>>> map) {
        for (Map.Entry<Registry<?>, Consumer<IRegistryHandler<?>>> handler : map.entrySet()) {
            Registry<?> registry = handler.getKey();
            IRegistryHandler<?> registryHandler = new VanillaRegistryHandler<>(registry);

            handler.getValue().accept(registryHandler);
        }
    }
}
