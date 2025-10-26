package mod.bluestaggo.modernerbeta.fabric.client.resource;

import mod.bluestaggo.modernerbeta.client.resource.ModernBetaColormapResource;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class ModernBetaFabricColormapResource extends ModernBetaColormapResource implements IdentifiableResourceReloadListener {
    private final ResourceLocation id;

    public ModernBetaFabricColormapResource(ResourceLocation id, String path, Consumer<int[]> consumer) {
        super(path, consumer);
        this.id = id;
    }

    @Override
    public ResourceLocation getFabricId() {
        return id;
    }
}
