package mod.bluestaggo.modernerbeta.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import mod.bluestaggo.modernerbeta.compat.ModCompat;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static mod.bluestaggo.modernerbeta.level.chunk.ModernBetaNoiseGeneratorSettings.useModernBetaSurfaceRules;

@Mixin(SurfaceRuleData.class)
public class SurfaceRuleDataMixin {
    @ModifyExpressionValue(
        method = "overworldLike",
        at = @At(
            value = "CONSTANT",
            args = "intValue=97"
        )
    )
    private static int modifyWoodedBadlandsHeight(int constant) {
        if (useModernBetaSurfaceRules()) {
            return 86;
        }
        return constant;
    }

    @ModifyExpressionValue(
        method = "overworldLike",
        at = @At(
            value = "CONSTANT",
            args = "intValue=74"
        )
    )
    private static int modifyTerracottaStripesHeight(int constant) {
        if (useModernBetaSurfaceRules()) {
            return 66;
        }
        return constant;
    }

    @SuppressWarnings("rawtypes")
    @WrapOperation(method = "overworldLike",
        at = {
            @At(
                value = "INVOKE",
                target = "Lcom/google/common/collect/ImmutableList$Builder;add(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList$Builder;",
                ordinal = 0,
                remap = false
            ),
            @At(
                value = "INVOKE",
                target = "Lcom/google/common/collect/ImmutableList$Builder;add(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList$Builder;",
                ordinal = 1,
                remap = false
            ),
            @At(
                value = "INVOKE",
                target = "Lcom/google/common/collect/ImmutableList$Builder;add(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList$Builder;",
                ordinal = 3,
                remap = false
            )
        }
    )
    private static ImmutableList.Builder removeWorldBottomRulesIfModernBeta(ImmutableList.Builder instance, Object element, Operation<ImmutableList.Builder> original) {
        if (useModernBetaSurfaceRules()) {
            return null;
        }

        return original.call(instance, element);
    }

    @Inject(method = "overworldLike", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/SurfaceRules;abovePreliminarySurface()Lnet/minecraft/world/level/levelgen/SurfaceRules$ConditionSource;"))
    private static void addCustomRulesIfPossible(CallbackInfoReturnable<SurfaceRules.RuleSource> cir, @Local(ordinal = 8) LocalRef<SurfaceRules.RuleSource> ruleSource) {
        if (useModernBetaSurfaceRules()) {
            List<SurfaceRules.RuleSource> rules = ModCompat.getCustomRules();

            if (rules.isEmpty())
                return;

            ImmutableList.Builder<SurfaceRules.RuleSource> newRules = ImmutableList.builder();
            newRules.addAll(rules);
            newRules.add(ruleSource.get());
            ruleSource.set(SurfaceRules.sequence(newRules.build().toArray(SurfaceRules.RuleSource[]::new)));
        }
    }
}
