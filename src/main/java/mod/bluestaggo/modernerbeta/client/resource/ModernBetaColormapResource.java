package mod.bluestaggo.modernerbeta.client.resource;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.client.resources.LegacyStuffWrapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.io.IOException;
import java.util.function.Consumer;

public class ModernBetaColormapResource implements ResourceManagerReloadListener {
    private final ResourceLocation id;
    private final Consumer<int[]> consumer;
    
    public ModernBetaColormapResource(String path, Consumer<int[]> consumer) {
        this.id = ModernerBeta.createId(path);
        this.consumer = consumer;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onResourceManagerReload(ResourceManager resourceManager) {
        int[] map;

        try {
            map = LegacyStuffWrapper.getPixels(resourceManager, this.id);
        } catch (IOException exception) {
            throw new IllegalStateException("[Modern Beta] Failed to load colormap texture!", exception);
        }

        this.consumer.accept(map);
    }
}
