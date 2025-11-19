package mod.bluestaggo.modernerbeta.client.resource;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.RawTextureDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ModernBetaColormapResource implements SynchronousResourceReloader {
    // REFACTOR: Use SLF4J Logger for professional error tracking
    private static final Logger LOGGER = LoggerFactory.getLogger("moderner_beta");
    
    private final Identifier id;
    private final Consumer<int[]> consumer;

    // REFACTOR: Removed String constructor to enforce strict namespace usage.
    public ModernBetaColormapResource(Identifier id, Consumer<int[]> consumer) {
        this.id = id;
        this.consumer = consumer;
    }

    @Override
    public void reload(ResourceManager resourceManager) {
        int[] map;

        try {
            map = RawTextureDataLoader.loadRawTextureData(resourceManager, this.id);
        } catch (IOException exception) {
            // REFACTOR: Catch race condition errors gracefully.
            // This prevents the "First Launch" crash by falling back to a default color.
            LOGGER.warn("[Modern Beta] Failed to load colormap '{}' during reload. Using fallback blue texture.", this.id);
            
            // Create a fallback blank map (Blue) to prevent crash
            map = new int[256 * 256]; 
            Arrays.fill(map, 0xFF0000FF); // AARRGGBB (Blue)
        }

        this.consumer.accept(map);
    }
}
