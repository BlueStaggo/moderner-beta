//? if <26.3 {
package mod.bluestaggo.modernerbeta.mixin.compat.terrablender;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.bluestaggo.modernerbeta.compat.ModCompat;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaSurfaceRuleData;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("UnresolvedMixinReference")
@Pseudo
@Mixin(targets = "terrablender.api.SurfaceRuleManager")
public abstract class SurfaceRuleManagerMixin {
    @WrapOperation(
        //~ if >=26.2 'getDefaultSurfaceRules' -> 'repopulateRules'
        method = "getDefaultSurfaceRules",
        at = @At(
            value = "INVOKE",
            target =
                "Lterrablender/worldgen/TBSurfaceRuleData;overworld(" +
                //? if >=26.2
                //"Lnet/minecraft/core/HolderGetter;" +
                ")Lnet/minecraft/world/level/levelgen/SurfaceRules$RuleSource;"
        )
    )
    private static SurfaceRules.RuleSource useModernBetaOverworldSurfaceRules(
        //? if >=26.2
        //net.minecraft.core.HolderGetter<net.minecraft.world.level.biome.Biome> biomeLookup,
        Operation<SurfaceRules.RuleSource> original
    ) {
        if (ModCompat.useModernBetaSurfaceRules) {
            return ModernBetaSurfaceRuleData.overworldLike(/*? >=26.2 {*//*biomeLookup*//*?} else { */null/*?}*/, true, false, false, false);
        }

        return original.call(/*? >=26.2 {*//*biomeLookup*//*?}*/);
    }
}
//? }