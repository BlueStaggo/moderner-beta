//? if <1.21.9 {
package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.debug.hudentry.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(DebugHud.class)
public abstract class MixinDebugHud {
    @Shadow @Final private MinecraftClient client;
    
    @Inject(method = "getLeftText", at = @At("TAIL"))
    private void injectGetLeftText(CallbackInfoReturnable<List<String>> info) {
        BlockPos pos = this.client.getCameraEntity().getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        
        IntegratedServer integratedServer = this.client.getServer();
        ServerWorld serverWorld = null;
        
        if (integratedServer != null) {
            serverWorld = integratedServer.getWorld(this.client.world.getRegistryKey());
        }

        List<String> lines = info.getReturnValue();
        if (serverWorld != null) {
            moderner_beta$addIfNotEmpty(lines, ExtendedBiomeDebugHudEntry.getLine(serverWorld, x, z));

            if (ModernerBeta.DEV_ENV) {
                lines.addAll(ClimateDebugHudEntry.getLines(serverWorld, x, y, z));
                lines.addAll(HeightmapDebugHudEntry.getLines(serverWorld, x, z));
                moderner_beta$addIfNotEmpty(lines, ForcedHeightDebugHudEntry.getLine(serverWorld, x, z));
                moderner_beta$addIfNotEmpty(lines, InjectedBiomeDebugHudEntry.getLine(serverWorld, x, y, z));
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