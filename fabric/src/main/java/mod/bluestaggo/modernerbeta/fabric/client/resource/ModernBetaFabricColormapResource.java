package mod.bluestaggo.modernerbeta.fabric.client.resource;

import mod.bluestaggo.modernerbeta.client.resource.ModernBetaColormapResource;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ModernBetaFabricColormapResource extends ModernBetaColormapResource implements IdentifiableResourceReloadListener {
    private final Identifier id;

    // REFACTOR: now takes Identifier to match the strict parent class
    public ModernBetaFabricColormapResource(Identifier id, Identifier resourceId, Consumer<int[]> consumer) {
        super(resourceId, consumer);
        this.id = id;
    }

    @Override
    public Identifier getFabricId() {
        return id;
    }
}
