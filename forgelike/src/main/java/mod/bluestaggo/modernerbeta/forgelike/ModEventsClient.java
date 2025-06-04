package mod.bluestaggo.modernerbeta.forgelike;

import me.shedaniel.autoconfig.AutoConfig;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.ModernerBetaClient;
import mod.bluestaggo.modernerbeta.client.color.BlockColorSampler;
import mod.bluestaggo.modernerbeta.client.color.BlockColors;
import mod.bluestaggo.modernerbeta.client.registry.ModernBetaClientRegistries;
import mod.bluestaggo.modernerbeta.client.resource.ModernBetaColormapResource;
import mod.bluestaggo.modernerbeta.config.ModernBetaConfig;
import mod.bluestaggo.modernerbeta.forgelike.registry.RegistryHelperImpl;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.registry.IRegistryHelper;
import mod.bluestaggo.modernerbeta.registry.VanillaRegistryHandler;
import net.minecraft.registry.Registry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.Consumer;

@EventBusSubscriber(modid = ModernerBeta.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventsClient {
    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class, () -> (mc, screen) -> AutoConfig.getConfigScreen(ModernBetaConfig.class, screen).get());
    }

    private static final Consumer<IRegistryHandler<?>> NONE = h -> {};

    @SubscribeEvent
    public static void registerToRegistries(RegisterEvent event) {
        Registry<?> registry = event.getRegistry();

        VanillaRegistryHandler<?> registryHandler = new VanillaRegistryHandler<>(registry);
        ModernerBetaClient.CUSTOM_REGISTRY_HANDLERS.stream()
            .filter(pair -> pair.getLeft().equals(registry))
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
    public static void addClientReloadListeners(AddClientReloadListenersEvent event) {
        event.addListener(ModernerBeta.createId("water_colormap"), new ModernBetaColormapResource(
                "textures/colormap/water.png",
                BlockColorSampler.INSTANCE.colormapWater::setColormap
        ));

        event.addListener(ModernerBeta.createId("underwater_colormap"), new ModernBetaColormapResource(
                "textures/colormap/underwater.png",
                BlockColorSampler.INSTANCE.colormapUnderwater::setColormap
        ));
    }
}
