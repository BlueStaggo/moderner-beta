package mod.bluestaggo.modernerbeta.mixin;

import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkNoiseSampler;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.gen.surfacebuilder.MaterialRules$AboveYMaterialCondition")
public abstract class MixinAboveYMaterialCondition {
    @Shadow public abstract YOffset anchor();
    @Shadow public abstract int surfaceDepthMultiplier();

    @Inject(
        method = "apply(Lnet/minecraft/world/gen/surfacebuilder/MaterialRules$MaterialRuleContext;)Lnet/minecraft/world/gen/surfacebuilder/MaterialRules$BooleanSupplier;",
        at = @At("HEAD"),
        cancellable = true
    )
    public void applyModernerBeta(MaterialRules.MaterialRuleContext materialRuleContext, CallbackInfoReturnable<MaterialRules.BooleanSupplier> cir) {
        AccessorMaterialRuleContext context = (AccessorMaterialRuleContext)(Object)materialRuleContext;
        assert context != null;
        if (context.getChunkNoiseSampler() instanceof ModernBetaChunkNoiseSampler) {
            cir.setReturnValue(() -> context.getBlockY() >= this.anchor().getY(context.getHeightContext()) + context.getRunDepth() * this.surfaceDepthMultiplier());
        }
    }
}
