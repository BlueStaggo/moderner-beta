package mod.bluestaggo.modernerbeta.client.resource;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.client.util.RawTextureDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.function.Consumer;

public class ModernBetaColormapResource implements SynchronousResourceReloader {
    private final Identifier id;
    private final Consumer<int[]> consumer;
    
    public ModernBetaColormapResource(String path, Consumer<int[]> consumer) {
        this.id = ModernerBeta.createId(path);
        this.consumer = consumer;
    }

    @Override
    public void reload(ResourceManager resourceManager) {
        int[] map;

        try {
            map = RawTextureDataLoader.loadRawTextureData(resourceManager, this.id);
        } catch (IOException exception) {
            throw new IllegalStateException("[Modern Beta] Failed to load colormap texture!", exception);
        }

        this.consumer.accept(map);
    }
}
