package mod.bluestaggo.modernerbeta.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGeneratorSettings.useModernBetaSurfaceRules;

@Mixin(VanillaSurfaceRules.class)
public class MixinVanillaSurfaceRules {
    @ModifyExpressionValue(
        method = "createDefaultRule",
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
        method = "createDefaultRule",
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

    @SuppressWarnings({"InvalidInjectorMethodSignature", "rawtypes"})
    @WrapOperation(method = "createDefaultRule",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/ImmutableList$Builder;add(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList$Builder;",
            ordinal = 3,
            remap = false
        )
    )
    private static ImmutableList.Builder removeDeepslateIfModernBeta(ImmutableList.Builder instance, Object element, Operation<ImmutableList.Builder> original) {
        if (useModernBetaSurfaceRules()) {
            return null;
        }

        return original.call(instance, element);
    }
}
