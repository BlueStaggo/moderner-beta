//? if <1.21.9 {
package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.debug.entries.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DebugScreenOverlay.class)
public abstract class DebugScreenOverlayMixin {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "getGameInformation", at = @At("TAIL"))
    private void injectGetGameInformation(CallbackInfoReturnable<List<String>> info) {
        BlockPos pos = this.minecraft.getCameraEntity().blockPosition();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        IntegratedServer integratedServer = this.minecraft.getSingleplayerServer();
        ServerLevel serverWorld = null;

        if (integratedServer != null) {
            serverWorld = integratedServer.getLevel(this.minecraft.level.dimension());
        }

        List<String> lines = info.getReturnValue();
        if (serverWorld != null) {
            moderner_beta$addIfNotEmpty(lines, DebugEntryExtendedBiome.getLine(serverWorld, x, z));

            if (ModernerBeta.DEV_ENV) {
                lines.addAll(DebugEntryClimate.getLines(serverWorld, x, y, z));
                lines.addAll(DebugEntryHeightmap.getLines(serverWorld, x, z));
                moderner_beta$addIfNotEmpty(lines, DebugEntryForcedHeight.getLine(serverWorld, x, z));
                moderner_beta$addIfNotEmpty(lines, DebugEntryInjectedBiome.getLine(serverWorld, x, y, z));
            }
        }
    }

    @Unique
    private void moderner_beta$addIfNotEmpty(List<String> lines, String str) {
        if (!str.isEmpty())
            lines.add(str);
    }
}
//?}
