package mod.bluestaggo.modernerbeta.forge;

import me.shedaniel.autoconfig.AutoConfig;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.color.BlockColorSampler;
import mod.bluestaggo.modernerbeta.client.color.BlockColors;
import mod.bluestaggo.modernerbeta.client.resource.ModernBetaColormapResource;
import mod.bluestaggo.modernerbeta.config.ModernBetaConfig;
import net.minecraft.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ModernerBeta.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModernerBetaForgeClient {
    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> AutoConfig.getConfigScreen(ModernBetaConfig.class, screen).get()));
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
