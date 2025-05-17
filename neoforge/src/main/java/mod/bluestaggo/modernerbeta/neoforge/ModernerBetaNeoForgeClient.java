package mod.bluestaggo.modernerbeta.neoforge;

import me.shedaniel.autoconfig.AutoConfig;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.color.BlockColorSampler;
import mod.bluestaggo.modernerbeta.client.resource.ModernBetaColormapResource;
import mod.bluestaggo.modernerbeta.config.ModernBetaConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber(modid = ModernerBeta.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModernerBetaNeoForgeClient {
    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent event) {
        ModernerBeta.clientInit();

        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class, () -> (mc, screen) -> AutoConfig.getConfigScreen(ModernBetaConfig.class, screen).get());
    }

    @SubscribeEvent
    public static void addClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new ModernBetaColormapResource(
                "textures/colormap/water.png",
                BlockColorSampler.INSTANCE.colormapWater::setColormap
        ));

        event.registerReloadListener(new ModernBetaColormapResource(
                "textures/colormap/underwater.png",
                BlockColorSampler.INSTANCE.colormapUnderwater::setColormap
        ));
    }
}
