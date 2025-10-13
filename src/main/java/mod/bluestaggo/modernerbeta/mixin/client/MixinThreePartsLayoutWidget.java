package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.imixin.ModernBetaClearableWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(HeaderAndFooterLayout.class)
public class MixinThreePartsLayoutWidget implements ModernBetaClearableWidget {
    @Shadow @Final private FrameLayout headerFrame;
    @Shadow @Final private FrameLayout contentsFrame;
    @Shadow @Final private FrameLayout footerFrame;

    @Override
    public void modernBeta$clear() {
        ((ModernBetaClearableWidget)this.headerFrame).modernBeta$clear();
        ((ModernBetaClearableWidget)this.contentsFrame).modernBeta$clear();
        ((ModernBetaClearableWidget)this.footerFrame).modernBeta$clear();
    }
}
