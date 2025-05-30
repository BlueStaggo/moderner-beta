package mod.bluestaggo.modernerbeta.fabric.client.resource;

import mod.bluestaggo.modernerbeta.client.resource.ModernBetaColormapResource;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.texture.TextureContents;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ModernBetaFabricColormapResource extends ModernBetaColormapResource implements IdentifiableResourceReloadListener {
    private final Identifier id;

    public ModernBetaFabricColormapResource(Identifier id, String path, Consumer<TextureContents> consumer) {
        super(path, consumer);
        this.id = id;
    }

    @Override
    public Identifier getFabricId() {
        return id;
    }
}
