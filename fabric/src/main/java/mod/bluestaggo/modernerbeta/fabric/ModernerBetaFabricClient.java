package mod.bluestaggo.modernerbeta.fabric;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.color.BlockColorSampler;
import mod.bluestaggo.modernerbeta.client.color.BlockColors;
import mod.bluestaggo.modernerbeta.fabric.client.resource.ModernBetaFabricColormapResource;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.block.Blocks;
import net.minecraft.resource.ResourceType;

public class ModernerBetaFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerBlockColors();

        ResourceManagerHelper resourceManager = ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES);
        resourceManager.registerReloadListener(new ModernBetaFabricColormapResource(
                ModernerBeta.createId("water_colormap"),
                "textures/colormap/water.png",
                BlockColorSampler.INSTANCE.colormapWater::setColormap
        ));

        resourceManager.registerReloadListener(new ModernBetaFabricColormapResource(
                ModernerBeta.createId("underwater_colormap"),
                "textures/colormap/underwater.png",
                BlockColorSampler.INSTANCE.colormapUnderwater::setColormap
        ));
    }
    
    private static void registerBlockColors() {
        // Grass blocks
        ColorProviderRegistry.BLOCK.register(
            BlockColorSampler.INSTANCE::getGrassColor,
            Blocks.GRASS_BLOCK
        );

        // Short grass blocks
        ColorProviderRegistry.BLOCK.register(
            BlockColorSampler.INSTANCE::getShortGrassColor,
            BlockColors.SHORT_GRASS_BLOCKS
        );

        // Tall grass blocks
        ColorProviderRegistry.BLOCK.register(
            BlockColorSampler.INSTANCE::getTallGrassColor,
            BlockColors.TALL_GRASS_BLOCKS
        );

        // Petal blocks
        ColorProviderRegistry.BLOCK.register(
            BlockColorSampler.INSTANCE::getPetalColor,
            BlockColors.PETAL_BLOCKS
        );

        // Foliage blocks
        ColorProviderRegistry.BLOCK.register(
            BlockColorSampler.INSTANCE::getFoliageColor,
            BlockColors.FOLIAGE_BLOCKS
        );

        // Sugar cane
        ColorProviderRegistry.BLOCK.register(
            BlockColorSampler.INSTANCE::getSugarCaneColor,
            Blocks.SUGAR_CANE
        );

        // Water blocks
        ColorProviderRegistry.BLOCK.register(
            BlockColorSampler.INSTANCE::getWaterColor,
            BlockColors.WATER_BLOCKS
        );
    }
}
