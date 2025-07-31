package mod.bluestaggo.modernerbeta.mixin.client;

import net.minecraft.client.gui.EditBox;
import net.minecraft.client.gui.widget.EditBoxWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EditBoxWidget.class)
public interface AccessorEditBoxWidget {
    @Accessor("editBox")
    EditBox getEditBox();
}
