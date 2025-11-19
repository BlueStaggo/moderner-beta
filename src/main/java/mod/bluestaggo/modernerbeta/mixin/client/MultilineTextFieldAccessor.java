package mod.bluestaggo.modernerbeta.mixin.client;

import net.minecraft.client.gui.components.MultilineTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MultilineTextField.class)
public interface MultilineTextFieldAccessor {
    @Accessor("width")
    int getWidth();

    @Mutable
    @Accessor("width")
    void setWidth(int width);

    @Invoker("reflowDisplayLines")
    void invokeReflowDisplayLines();
}
