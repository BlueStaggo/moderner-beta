package mod.bluestaggo.modernerbeta.fabric;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.command.DebugProviderSettingsCommand;
import mod.bluestaggo.modernerbeta.compat.ModCompat;
import mod.bluestaggo.modernerbeta.fabric.network.NetworkHelperImpl;
import mod.bluestaggo.modernerbeta.fabric.registry.RegistryHelperImpl;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.level.ModernBetaLevelInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
//? if >=1.20.2 {
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import mod.bluestaggo.modernerbeta.network.BiomeProviderInfoPayload;
//?}
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;

public class ModernerBetaFabric implements ModInitializer {
    @Override
    @SuppressWarnings("unchecked")
    public void onInitialize() {
        // Register mod stuff
        ModernBetaRegistries.makeRegistries(new RegistryHelperImpl());
        ModernerBeta.setupCustomRegistryHandlers();

        ModContainer modContainer = FabricLoader.getInstance().getModContainer(ModernerBeta.MOD_ID).orElseThrow();
        for (String pack : ModernerBeta.BUILT_IN_PACKS) {
            //? if >=1.21.11 {
            net.fabricmc.fabric.api.resource.v1.ResourceLoader.registerBuiltinPack(
            //? } else {
            /*net.fabricmc.fabric.api.resource.ResourceManagerHelper.registerBuiltinResourcePack(
            *///? }
                ModernerBeta.createId(pack),
                modContainer,
                Component.translatable("dataPack.moderner_beta." + pack + ".name"),
                //? if >=1.21.11 {
                net.fabricmc.fabric.api.resource.v1.pack.PackActivationType.NORMAL
                //? } else {
                /*net.fabricmc.fabric.api.resource.ResourcePackActivationType.NORMAL
                *///? }
            );
        }

        ModernerBeta.init();
        ModCompat.initialise(FabricLoader.getInstance()::isModLoaded);
        ModernerBetaInitializer.setupRegistryHandlers(ModernerBeta.REGISTRY_HANDLERS);
        ModernerBetaInitializer.setupRegistryHandlers(ModernerBeta.CUSTOM_REGISTRY_HANDLERS);
        ModernerBeta.loadConfig(FabricLoader.getInstance().getConfigDir());

        ModernerBeta.setupCustomDynamicRegistries();
        for (Pair<ResourceKey<?>, Codec<?>> dynamicRegistry : ModernerBeta.CUSTOM_DYNAMIC_REGISTRIES) {
            DynamicRegistries.register((ResourceKey<Registry<Object>>)dynamicRegistry.getFirst(), (Codec<Object>)dynamicRegistry.getSecond());
        }

        if (ModernerBeta.DEV_ENV) {
            CommandRegistrationCallback.EVENT.register(DebugProviderSettingsCommand::register);
        }

        ServerLifecycleEvents.SERVER_STARTING.register(ModernBetaLevelInitializer::initStarting);
        ServerLifecycleEvents.SERVER_STARTED.register(ModernBetaLevelInitializer::initStarted);

        ModernerBeta.networkHelper = new NetworkHelperImpl();

        //? if >=1.20.2 {
        PayloadTypeRegistry.clientboundPlay().register(BiomeProviderInfoPayload.ID, BiomeProviderInfoPayload.CODEC);
        //?}
    }
}
