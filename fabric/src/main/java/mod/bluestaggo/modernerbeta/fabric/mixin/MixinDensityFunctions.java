package mod.bluestaggo.modernerbeta.fabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import mod.bluestaggo.modernerbeta.fabric.data.reduced_height.ModernBetaReducedHeightDataProvider;
import net.minecraft.world.gen.densityfunction.DensityFunctions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DensityFunctions.class)
public class MixinDensityFunctions {
    @ModifyExpressionValue(
        method = "applySurfaceSlides",
        at = @At(
            value = "CONSTANT",
            args = "intValue=-64"
        )
    )
    private static int modifyBottomForReducedHeight(int original) {
        if (ModernBetaReducedHeightDataProvider.isGeneratingData())
            return 0;

        return original;
    }

    @ModifyExpressionValue(
        method = "applySurfaceSlides",
        at = @At(
            value = "CONSTANT",
            args = "intValue=384"
        )
    )
    private static int modifyTopForReducedHeight(int original) {
        if (ModernBetaReducedHeightDataProvider.isGeneratingData())
            return 320;

        return original;
    }
}
