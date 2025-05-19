package mod.bluestaggo.modernerbeta.fabric;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.command.DebugProviderSettingsCommand;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.registry.VanillaRegistryHandler;
import mod.bluestaggo.modernerbeta.world.ModernBetaWorldInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
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
        registerDataPacks();

        ModernerBeta.init();

        for (Map.Entry<Registry<?>, Consumer<IRegistryHandler<?>>> handler : ModernerBeta.REGISTRY_HANDLERS.entrySet()) {
            Registry<?> registry = handler.getKey();
            IRegistryHandler<?> registryHandler = new VanillaRegistryHandler<>(registry);

            handler.getValue().accept(registryHandler);
        }

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            CommandRegistrationCallback.EVENT.register(DebugProviderSettingsCommand::register);
            ModernerBeta.DEV_ENV = true;
        }

        ServerLifecycleEvents.SERVER_STARTING.register(ModernBetaWorldInitializer::init);
    }

    private static void registerDataPacks() {
        ModContainer modContainer = FabricLoader.getInstance().getModContainer(ModernerBeta.MOD_ID).orElseThrow();
        ResourceManagerHelper.registerBuiltinResourcePack(ModernerBeta.createId("reduced_height"), modContainer, ResourcePackActivationType.NORMAL);
    }
}
