package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProviderFinite;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelLoadingScreen.class)
public abstract class LevelLoadingScreenMixin extends Screen {
    protected LevelLoadingScreenMixin(Component title) {
        super(title);
    }

    @Inject(
        method =
            //? if >=1.21.9 {
            /*" <init>(Lnet/minecraft/client/multiplayer/LevelLoadTracker;Lnet/minecraft/client/gui/screens/LevelLoadingScreen$Reason;)V",
            *///?} else {
            " <init>(Lnet/minecraft/server/level/progress/StoringChunkProgressListener;)V",
            //?}
        at = @At("TAIL")
    )
    private void injectInit(CallbackInfo info) {
        ChunkProviderFinite.resetPhase();
    }
    
    @Inject(method = "render", at = @At("TAIL"))
    private void injectRender(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo info) {
        String phase = ChunkProviderFinite.getPhase();
        
        if (!phase.isBlank()) {
            graphics.drawCenteredString(
                this.font,
                phase,
                this.width / 2,
                (this.height / 2) + 90,
                0xFFFFFFFF
            );
        }
    }
}
