package mod.bluestaggo.modernerbeta.client.resource;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.TextureContents;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ModernBetaColormapResource implements SynchronousResourceReloader {
    private final Identifier id;
    private final Consumer<TextureContents> consumer;
    
    public ModernBetaColormapResource(String path, Consumer<TextureContents> consumer) {
        this.id = ModernerBeta.createId(path);
        this.consumer = consumer;
    }

    @Override
    public void reload(ResourceManager resourceManager) {
        TextureContents map;

        try {
            map = TextureContents.load(resourceManager, this.id);
        } catch (IOException exception) {
            throw new IllegalStateException("[Modern Beta] Failed to load colormap texture!", exception);
        }

        this.consumer.accept(map);
    }
}
