package mod.bluestaggo.modernerbeta.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.data.worldgen.SurfaceRuleData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

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
}
