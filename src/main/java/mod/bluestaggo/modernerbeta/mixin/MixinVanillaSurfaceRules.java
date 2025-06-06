package mod.bluestaggo.modernerbeta.mixin;

import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import static mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGeneratorSettings.useModernBetaSurfaceRules;

@Mixin(VanillaSurfaceRules.class)
public class MixinVanillaSurfaceRules {
    @ModifyConstant(
        method = "createDefaultRule",
        constant = @Constant(intValue = 97)
    )
    private static int modifyWoodedBadlandsHeight(int constant) {
        if (useModernBetaSurfaceRules()) {
            return 86;
        }
        return constant;
    }

    @ModifyConstant(
        method = "createDefaultRule",
        constant = @Constant(intValue = 74)
    )
    private static int modifyTerracottaStripesHeight(int constant) {
        if (useModernBetaSurfaceRules()) {
            return 66;
        }
        return constant;
    }

    @Redirect(
        method = "createDefaultRule",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/gen/surfacebuilder/MaterialRules;aboveYWithStoneDepth(Lnet/minecraft/world/gen/YOffset;I)Lnet/minecraft/world/gen/surfacebuilder/MaterialRules$MaterialCondition;"
        )
    )
    private static MaterialRules.MaterialCondition modifyAboveYWithStoneDepth(YOffset anchor, int runDepthMultiplier) {
        if (useModernBetaSurfaceRules()) {
            return MaterialRules.aboveY(anchor, runDepthMultiplier);
        }
        return MaterialRules.aboveYWithStoneDepth(anchor, runDepthMultiplier);
    }

    @Redirect(
        method = "createDefaultRule",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/gen/surfacebuilder/MaterialRules;waterWithStoneDepth(II)Lnet/minecraft/world/gen/surfacebuilder/MaterialRules$MaterialCondition;"
        )
    )
    private static MaterialRules.MaterialCondition modifyWaterWithStoneDepth(int offset, int runDepthMultiplier) {
        if (useModernBetaSurfaceRules()) {
            return MaterialRules.water(offset, runDepthMultiplier);
        }
        return MaterialRules.waterWithStoneDepth(offset, runDepthMultiplier);
    }
}
