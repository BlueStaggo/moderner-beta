package mod.bluestaggo.modernerbeta.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.TextWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Environment(EnvType.CLIENT)
@Mixin(TextWidget.class)
public interface AccessorTextWidget {
    @Invoker
    TextWidget invokeAlign(float value);
}
