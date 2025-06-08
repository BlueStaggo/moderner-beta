package mod.bluestaggo.modernerbeta.fabric;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.command.DebugProviderSettingsCommand;
import mod.bluestaggo.modernerbeta.fabric.network.NetworkHelperImpl;
import mod.bluestaggo.modernerbeta.fabric.registry.RegistryHelperImpl;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.world.ModernBetaWorldInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
//? if >=1.20.2 {
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import mod.bluestaggo.modernerbeta.network.BiomeProviderInfoPayload;
//?}
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;

public class ModernerBetaFabric implements ModInitializer {
    @Override
    @SuppressWarnings("unchecked")
    public void onInitialize() {
        // Register mod stuff
        ModernBetaRegistries.makeRegistries(new RegistryHelperImpl());
        ModernerBeta.setupCustomRegistryHandlers();

        ModContainer modContainer = FabricLoader.getInstance().getModContainer(ModernerBeta.MOD_ID).orElseThrow();
        for (String pack : ModernerBeta.BUILT_IN_PACKS) {
            ResourceManagerHelper.registerBuiltinResourcePack(ModernerBeta.createId(pack), modContainer,
                    Text.translatable("dataPack.moderner_beta." + pack + ".name"), ResourcePackActivationType.NORMAL);
        }

        ModernerBeta.init();

        ModernerBetaInitializer.setupRegistryHandlers(ModernerBeta.REGISTRY_HANDLERS);
        ModernerBetaInitializer.setupRegistryHandlers(ModernerBeta.CUSTOM_REGISTRY_HANDLERS);

        ModernerBeta.setupCustomDynamicRegistries();
        for (Pair<RegistryKey<?>, Codec<?>> dynamicRegistry : ModernerBeta.CUSTOM_DYNAMIC_REGISTRIES) {
            DynamicRegistries.register((RegistryKey<Registry<Object>>)dynamicRegistry.getLeft(), (Codec<Object>)dynamicRegistry.getRight());
        }

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            CommandRegistrationCallback.EVENT.register(DebugProviderSettingsCommand::register);
            ModernerBeta.DEV_ENV = true;
        }

        ServerLifecycleEvents.SERVER_STARTING.register(ModernBetaWorldInitializer::initStarting);
        ServerLifecycleEvents.SERVER_STARTED.register(ModernBetaWorldInitializer::initStarted);

        ModernerBeta.networkHelper = new NetworkHelperImpl();

        //? if >=1.20.2 {
        PayloadTypeRegistry.playS2C().register(BiomeProviderInfoPayload.ID, BiomeProviderInfoPayload.CODEC);
        //?}
    }
}
