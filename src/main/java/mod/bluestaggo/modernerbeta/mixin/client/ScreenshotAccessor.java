package mod.bluestaggo.modernerbeta.mixin.client;

import net.minecraft.client.Screenshot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.io.File;

@Mixin(Screenshot.class)
public interface ScreenshotAccessor {
    @Invoker
    static File invokeGetFile(File directory) {
        throw new AssertionError();
    }
}
