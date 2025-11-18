package mod.bluestaggo.modernerbeta.client.resource;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.RawTextureDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ModernBetaColormapResource implements SynchronousResourceReloader {
    private final Identifier id;
    private final Consumer<int[]> consumer;
    
    // CHANGED: Now accepts Identifier instead of String
    public ModernBetaColormapResource(Identifier id, Consumer<int[]> consumer) {
        this.id = id;
        this.consumer = consumer;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void reload(ResourceManager resourceManager) {
        int[] map;

        try {
            map = RawTextureDataLoader.loadRawTextureData(resourceManager, this.id);
        } catch (IOException exception) {
            throw new IllegalStateException("[Modern Beta] Failed to load colormap texture: " + this.id, exception);
        }

        this.consumer.accept(map);
    }
}
