package mod.bluestaggo.modernerbeta.fabric;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.color.BlockColorSampler;
import mod.bluestaggo.modernerbeta.fabric.client.resource.ModernBetaFabricColormapResource;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public class ModernerBetaFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModernerBeta.clientInit();

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
}
