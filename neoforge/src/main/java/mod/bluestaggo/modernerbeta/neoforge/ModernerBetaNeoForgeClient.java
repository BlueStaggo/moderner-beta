package mod.bluestaggo.modernerbeta.neoforge;

import me.shedaniel.autoconfig.AutoConfig;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.color.BlockColorSampler;
import mod.bluestaggo.modernerbeta.client.color.BlockColors;
import mod.bluestaggo.modernerbeta.client.resource.ModernBetaColormapResource;
import mod.bluestaggo.modernerbeta.config.ModernBetaConfig;
import net.minecraft.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber(modid = ModernerBeta.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModernerBetaNeoForgeClient {
    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class, () -> (mc, screen) -> AutoConfig.getConfigScreen(ModernBetaConfig.class, screen).get());
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        // Grass blocks
        event.register(
            BlockColorSampler.INSTANCE::getGrassColor,
            Blocks.GRASS_BLOCK
        );

        // Short grass blocks
        event.register(
            BlockColorSampler.INSTANCE::getShortGrassColor,
            BlockColors.SHORT_GRASS_BLOCKS
        );

        // Tall grass blocks
        event.register(
            BlockColorSampler.INSTANCE::getTallGrassColor,
            BlockColors.TALL_GRASS_BLOCKS
        );

        // Petal blocks
        event.register(
            BlockColorSampler.INSTANCE::getPetalColor,
            BlockColors.PETAL_BLOCKS
        );

        // Foliage blocks
        event.register(
            BlockColorSampler.INSTANCE::getFoliageColor,
            BlockColors.FOLIAGE_BLOCKS
        );

        // Sugar cane
        event.register(
            BlockColorSampler.INSTANCE::getSugarCaneColor,
            Blocks.SUGAR_CANE
        );

        // Water blocks
        event.register(
            BlockColorSampler.INSTANCE::getWaterColor,
            BlockColors.WATER_BLOCKS
        );
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
