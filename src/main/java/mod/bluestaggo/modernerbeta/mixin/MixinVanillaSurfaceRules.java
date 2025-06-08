package mod.bluestaggo.modernerbeta.mixin;

import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules;
import org.spongepowered.asm.mixin.Mixin;

//? if >=1.20.2 {
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
//?} else {
/*import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
*///?}

import static mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGeneratorSettings.useModernBetaSurfaceRules;

@Mixin(VanillaSurfaceRules.class)
public class MixinVanillaSurfaceRules {
    //? if >=1.20.2 {
    @ModifyExpressionValue(
        method = "createDefaultRule",
        at = @At(
            value = "CONSTANT",
            args = "intValue=97"
        )
    )
    //?} else {
    /*@ModifyConstant(
        method = "createDefaultRule",
        constant = @Constant(intValue = 97)
    )
    *///?}
    private static int modifyWoodedBadlandsHeight(int constant) {
        if (useModernBetaSurfaceRules()) {
            return 86;
        }
        return constant;
    }

    //? if >=1.20.2 {
    @ModifyExpressionValue(
        method = "createDefaultRule",
        at = @At(
            value = "CONSTANT",
            args = "intValue=74"
        )
    )
    //?} else {
    /*@ModifyConstant(
        method = "createDefaultRule",
        constant = @Constant(intValue = 74)
    )
    *///?}
    private static int modifyTerracottaStripesHeight(int constant) {
        if (useModernBetaSurfaceRules()) {
            return 66;
        }
        return constant;
    }
}
