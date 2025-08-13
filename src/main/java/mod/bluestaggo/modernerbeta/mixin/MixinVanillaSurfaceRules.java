package mod.bluestaggo.modernerbeta.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
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
}
