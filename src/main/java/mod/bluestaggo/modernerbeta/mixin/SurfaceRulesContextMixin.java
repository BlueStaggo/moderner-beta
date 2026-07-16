package mod.bluestaggo.modernerbeta.mixin;

import mod.bluestaggo.modernerbeta.imixin.ModernBetaSurfaceSystem;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.SurfaceSystem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.level.levelgen.SurfaceRules$Context")
public class SurfaceRulesContextMixin {
    @Shadow @Final private SurfaceSystem system;
    @Shadow @Final private NoiseChunk noiseChunk;

    @Shadow private int blockX;
    @Shadow private int blockZ;

    @Shadow private int surfaceDepth;

    @Inject(method = "getMinSurfaceLevel", at = @At("HEAD"), cancellable = true)
    private void usePreciseSurfaceLevel(CallbackInfoReturnable<Integer> cir) {
        if (!(system instanceof ModernBetaSurfaceSystem mbSurfaceSystem))
            return;

        if (mbSurfaceSystem.modernerBeta$getContext() == null)
            return;

        //bypass all interpolation stuff if in Moderner Beta
        //~ if >=26.3 'preliminarySurfaceLevel' -> 'computePreliminarySurfaceLevel'
        int blockHeight = noiseChunk.preliminarySurfaceLevel(blockX, blockZ);
        cir.setReturnValue(blockHeight + surfaceDepth - 8);
    }
}
