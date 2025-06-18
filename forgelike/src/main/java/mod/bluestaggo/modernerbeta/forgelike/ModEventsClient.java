package mod.bluestaggo.modernerbeta.forgelike;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.ModernerBetaClient;
import mod.bluestaggo.modernerbeta.client.color.BlockColorSampler;
import mod.bluestaggo.modernerbeta.client.color.BlockColors;
import mod.bluestaggo.modernerbeta.client.gui.screen.config.ModernBetaGraphicalConfigSettingsScreen;
import mod.bluestaggo.modernerbeta.client.registry.ModernBetaClientRegistries;
import mod.bluestaggo.modernerbeta.client.resource.ModernBetaColormapResource;
import mod.bluestaggo.modernerbeta.forgelike.registry.RegistryHelperImpl;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.registry.IRegistryHelper;
import net.minecraft.registry.Registry;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;
//? if neoforge {
import mod.bluestaggo.modernerbeta.registry.VanillaRegistryHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
//? if >=1.21.4 {
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
//?} else if >=1.21.2 {
/*import net.neoforged.neoforge.client.event.AddReloadListenerEvent;
*///?} else {
/*import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
*///?}
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
//?} else {
/*import mod.bluestaggo.modernerbeta.forgelike.registry.ForgeRegistryHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegisterEvent;
*///?}

import java.util.function.BiConsumer;
import java.util.function.Consumer;

//? if neoforge {
@EventBusSubscriber(
 //?} else {
/*@Mod.EventBusSubscriber(
*///?}
        modid = ModernerBeta.MOD_ID,
        //? if neoforge {
        bus = EventBusSubscriber.Bus.MOD,
        //?} else {
        /*bus = Mod.EventBusSubscriber.Bus.MOD,
        *///?}
        value = Dist.CLIENT
)
public class ModEventsClient {
    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(
                //? if neoforge {
                IConfigScreenFactory.class,
                () ->
                //?} else {
                /*ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory
                *///?}
                ((mc, parent) -> new ModernBetaGraphicalConfigSettingsScreen(parent, FMLPaths.CONFIGDIR.get()))
        );
    }

    private static final Consumer<IRegistryHandler<?>> NONE = h -> {};

    @SubscribeEvent
    public static void registerToRegistries(RegisterEvent event) {
        //? if neoforge {
        Registry<?> registry = event.getRegistry();
        VanillaRegistryHandler<?> registryHandler = new VanillaRegistryHandler<>(registry);
        //?} else {
        /*ForgeRegistryHandler<?> registryHandler = new ForgeRegistryHandler<>(event);
        *///?}
        ModernerBetaClient.CUSTOM_REGISTRY_HANDLERS.stream()
            .filter(pair -> pair.getLeft().getKey().equals(event.getRegistryKey()))
            .forEach(pair -> pair.getRight().accept(registryHandler));
    }

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        IRegistryHelper registryHelper = new RegistryHelperImpl(event);
        ModernBetaClientRegistries.makeRegistries(registryHelper);
        ModernerBetaClient.setupCustomRegistryHandlers();
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        BlockColors.register(event::register);
    }

    @SubscribeEvent
    public static void addClientReloadListeners(
        //? if neoforge && >=1.21.4 {
        AddClientReloadListenersEvent event
         //?} else if neoforge && >=1.21.2 {
        /*AddReloadListenerEvent event
         *///?} else {
        /*RegisterClientReloadListenersEvent event
        *///?}
    ) {
        BiConsumer<Identifier, ResourceReloader> addListener = (id, resourceReloader) -> {
            //? if neoforge && >=1.21.4 {
            event.addListener(id, resourceReloader);
             //?} else if neoforge && >=1.21.2 {
            /*event.addListener(resourceReloader);
             *///?} else {
            /*event.registerReloadListener(resourceReloader);
            *///?}
        };

        addListener.accept(ModernerBeta.createId("water_colormap"), new ModernBetaColormapResource(
                "textures/colormap/water.png",
                BlockColorSampler.INSTANCE.colormapWater::setColormap
        ));

        addListener.accept(ModernerBeta.createId("underwater_colormap"), new ModernBetaColormapResource(
                "textures/colormap/underwater.png",
                BlockColorSampler.INSTANCE.colormapUnderwater::setColormap
        ));
    }
}
