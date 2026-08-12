package mod.bluestaggo.modernerbeta.fabric.mixin;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnstableApiUsage")
@Mixin(FabricDataGenHelper.class)
public class FabricDataGenHelperMixin {
    @Inject(method = "runInternal", at = @At("TAIL"))
    private static void stopProcess(CallbackInfo ci) {
        if (ModernerBeta.GENERATING_DATA) {
            System.exit(0);
        }
    }
}
