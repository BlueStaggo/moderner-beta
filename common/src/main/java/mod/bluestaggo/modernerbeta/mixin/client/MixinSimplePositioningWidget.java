package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.imixin.ModernBetaClearableWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(SimplePositioningWidget.class)
public class MixinSimplePositioningWidget implements ModernBetaClearableWidget {
    @Shadow @Final private List<?> elements;

    @Override
    public void modernBeta$clear() {
        this.elements.clear();
    }
}
