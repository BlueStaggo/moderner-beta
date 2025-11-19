package mod.bluestaggo.modernerbeta.client.resource;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.client.resources.LegacyStuffWrapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.Consumer;

public class ModernBetaColormapReloadListener extends SimplePreparableReloadListener<int[]> {
    private final ResourceLocation id;
    private final Consumer<int[]> consumer;
    
    public ModernBetaColormapReloadListener(String path, Consumer<int[]> consumer) {
        this.id = ModernerBeta.createId(path);
        this.consumer = consumer;
    }

    @Override
    @SuppressWarnings("deprecation")
    protected @NotNull int[] prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        try {
            return LegacyStuffWrapper.getPixels(resourceManager, this.id);
        } catch (IOException exception) {
            throw new IllegalStateException("[Modern Beta] Failed to load colormap texture!", exception);
        }
    }

    @Override
    protected void apply(int[] object, ResourceManager resourceManager, ProfilerFiller profiler) {
        consumer.accept(object);
    }
}
