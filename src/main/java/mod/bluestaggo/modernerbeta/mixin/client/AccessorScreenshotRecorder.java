package mod.bluestaggo.modernerbeta.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.ScreenshotRecorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.io.File;

@Environment(EnvType.CLIENT)
@Mixin(ScreenshotRecorder.class)
public interface AccessorScreenshotRecorder {
    @Invoker
    static File invokeGetScreenshotFilename(File directory) {
        throw new AssertionError();
    }
}
