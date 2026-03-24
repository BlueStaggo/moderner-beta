//? if >=26.1 {
/*package mod.bluestaggo.modernerbeta.mixin.client;

import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EditBox.class)
public interface EditBoxAccessor {
    @Accessor("highlightPos")
    int getHighlightPos();
    @Accessor("maxLength")
    int getMaxLength();
    @Accessor("value")
    void setValue(String value);

    @Invoker("onValueChange")
    void invokeOnValueChange(String value);
}
*///? }