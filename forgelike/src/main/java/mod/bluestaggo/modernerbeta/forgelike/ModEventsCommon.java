package mod.bluestaggo.modernerbeta.forgelike;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.forgelike.network.NetworkHelperImpl;
import mod.bluestaggo.modernerbeta.forgelike.registry.RegistryHelperImpl;
import mod.bluestaggo.modernerbeta.network.BiomeProviderInfoPayload;
import mod.bluestaggo.modernerbeta.network.S2CPacketHandlers;
import mod.bluestaggo.modernerbeta.registry.IRegistryHelper;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.util.Tuple;
//? if neoforge {
import mod.bluestaggo.modernerbeta.registry.VanillaRegistryHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
//?} else {
/*import mod.bluestaggo.modernerbeta.forgelike.registry.ForgeRegistryHandler;
import net.minecraft.resource.DirectoryResourcePack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegisterEvent;

import java.nio.file.Path;
import java.util.Optional;
*///?}

//? if neoforge {
@EventBusSubscriber(
 //?} else {
/*@Mod(ModernerBeta.MOD_ID)
@Mod.EventBusSubscriber(
*///?}
        modid = ModernerBeta.MOD_ID
        //? if neoforge && <1.21.6 {
        /*, bus = EventBusSubscriber.Bus.MOD
        *///?} else if forge {
        /*, bus = Mod.EventBusSubscriber.Bus.MOD
        *///?}
)
public class ModEventsCommon {
    @SubscribeEvent
    public static void commonInit(FMLConstructModEvent event) {
        ModernerBeta.init();

        if (!FMLLoader/*? >=1.21.9 {*//*.getCurrent()*//*?}*/.isProduction())
            ModernerBeta.DEV_ENV = true;

        //? if forge {
        /*NetworkHelperImpl networkHelper = new NetworkHelperImpl();
        ModernerBeta.networkHelper = networkHelper;

        int id = 0;
        networkHelper.channel.registerMessage(
            ++id,
            BiomeProviderInfoPayload.class,
            BiomeProviderInfoPayload::write,
            BiomeProviderInfoPayload::fromPacketByteBuf,
            (payload, context) -> {
                context.get().enqueueWork(() -> {
                    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        S2CPacketHandlers.onBiomeProviderInfo(
                            LogicalSidedProvider.CLIENTWORLD.get(
                                context.get().getDirection().getReceptionSide())
                                .orElseThrow(),
                            payload
                        ));
                });
                context.get().setPacketHandled(true);
            },
            Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
        *///?}
    }

    @SubscribeEvent
    public static void postInit(FMLLoadCompleteEvent event) {
        ModernerBeta.loadConfig(FMLPaths.CONFIGDIR.get());
    }

    @SubscribeEvent
    public static void registerToRegistries(RegisterEvent event) {
        //? if neoforge {
        Registry<?> registry = event.getRegistry();
        VanillaRegistryHandler<?> registryHandler = new VanillaRegistryHandler<>(registry);
        //?} else {
        /*ForgeRegistryHandler<?> registryHandler = new ForgeRegistryHandler<>(event);
        *///?}
        ModernerBeta.REGISTRY_HANDLERS.stream()
            .filter(pair -> pair.getA().key().equals(event.getRegistryKey()))
            .forEach(pair -> pair.getB().accept(registryHandler));
        ModernerBeta.CUSTOM_REGISTRY_HANDLERS.stream()
            .filter(pair -> pair.getA().key().equals(event.getRegistryKey()))
            .forEach(pair -> pair.getB().accept(registryHandler));
    }

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        IRegistryHelper registryHelper = new RegistryHelperImpl(event);
        ModernBetaRegistries.makeRegistries(registryHelper);
        ModernerBeta.setupCustomRegistryHandlers();
    }

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        ModernerBeta.setupCustomDynamicRegistries();
        for (Tuple<ResourceKey<?>, Codec<?>> dynamicRegistry : ModernerBeta.CUSTOM_DYNAMIC_REGISTRIES) {
            event.dataPackRegistry((ResourceKey<Registry<Object>>)dynamicRegistry.getA(), (Codec<Object>)dynamicRegistry.getB());
        }
    }

    //? if neoforge {
    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        ModernerBeta.networkHelper = new NetworkHelperImpl();

        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                BiomeProviderInfoPayload.ID,
                BiomeProviderInfoPayload.CODEC,
                (payload, context) -> {
                    S2CPacketHandlers.onBiomeProviderInfo(context.player()
                            /*? >=1.21.9 {*//*.level()*//*?} else {*/.getWorld()/*?}*/, payload);
                }
        );
    }
    //?}

    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.SERVER_DATA) {
            PackSource source = new PackSource() {
                @Override
                public Component decorate(Component packDisplayName) {
                    return Component.translatable("pack.nameAndSource", packDisplayName,
                            Component.translatable("pack.source.builtin")).withStyle(ChatFormatting.GRAY);
                }

                @Override
                public boolean shouldAddAutomatically() {
                    return false;
                }
            };

            for (String pack : ModernerBeta.BUILT_IN_PACKS) {
                Component title = Component.translatable("dataPack.moderner_beta." + pack + ".name");
                //? if neoforge {
                event.addPackFinders(
                    ModernerBeta.createId("resourcepacks/" + pack),
                    PackType.SERVER_DATA,
                    title,
                    source,
                    false,
                    Pack.Position.TOP
                );
                //?} else {
                /*Path resourcePath = ModList.get().getModFileById(ModernerBeta.MOD_ID).getFile().findResource("resourcepacks/" + pack);
                ResourcePackProfile packProfile = ResourcePackProfile.create(
                    "moderner_beta/" + pack,
                    title,
                    false,
                    path -> new DirectoryResourcePack(path, resourcePath, false),
                    ResourceType.SERVER_DATA,
                    ResourcePackProfile.InsertionPosition.TOP,
                    source
                );

                event.addRepositorySource(consumer -> consumer.accept(packProfile));
                *///?}
            }
        }
    }
}
