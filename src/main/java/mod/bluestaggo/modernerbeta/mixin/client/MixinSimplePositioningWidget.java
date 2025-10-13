package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.imixin.ModernBetaClearableWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.layouts.FrameLayout;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(FrameLayout.class)
public class MixinSimplePositioningWidget implements ModernBetaClearableWidget {
    @Shadow @Final private List<?> children;

    @Override
    public void modernBeta$clear() {
        this.children.clear();
    }
}
