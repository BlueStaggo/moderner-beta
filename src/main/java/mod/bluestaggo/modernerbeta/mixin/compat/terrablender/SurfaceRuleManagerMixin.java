package mod.bluestaggo.modernerbeta.mixin.compat.terrablender;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.bluestaggo.modernerbeta.compat.ModCompat;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("UnresolvedMixinReference")
@Pseudo
@Mixin(targets = "terrablender.api.SurfaceRuleManager")
public abstract class SurfaceRuleManagerMixin {
    @WrapOperation(
        method = "getDefaultSurfaceRules",
        at = @At(
            value = "INVOKE",
            target = "Lterrablender/worldgen/TBSurfaceRuleData;overworld()Lnet/minecraft/world/level/levelgen/SurfaceRules$RuleSource;"
        )
    )
    private static SurfaceRules.RuleSource useModernBetaOverworldSurfaceRules(Operation<SurfaceRules.RuleSource> original) {
        //How anticlimactic...
        if (ModCompat.useModernBetaSurfaceRules) {
            return SurfaceRuleData.overworld();
        }

        return original.call();
    }
}
