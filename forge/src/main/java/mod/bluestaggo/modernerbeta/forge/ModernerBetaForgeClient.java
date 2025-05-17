package mod.bluestaggo.modernerbeta.forge;

import me.shedaniel.autoconfig.AutoConfig;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.color.BlockColorSampler;
import mod.bluestaggo.modernerbeta.client.resource.ModernBetaColormapResource;
import mod.bluestaggo.modernerbeta.config.ModernBetaConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ModernerBeta.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModernerBetaForgeClient {
    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent event) {
        ModernerBeta.clientInit();

        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> AutoConfig.getConfigScreen(ModernBetaConfig.class, screen).get()));
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
