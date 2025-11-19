package mod.bluestaggo.modernerbeta.fabric.client.resource;

import mod.bluestaggo.modernerbeta.client.resource.ModernBetaColormapReloadListener;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class ModernBetaFabricColormapReloadListener extends ModernBetaColormapReloadListener implements IdentifiableResourceReloadListener {
    private final ResourceLocation id;

    public ModernBetaFabricColormapReloadListener(ResourceLocation id, String path, Consumer<int[]> consumer) {
        super(path, consumer);
        this.id = id;
    }

    @Override
    public ResourceLocation getFabricId() {
        return id;
    }
}
