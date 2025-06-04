//? if >=1.21.6 {
/*package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.FogUtils;
import mod.bluestaggo.modernerbeta.client.world.ModernBetaClientWorld;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.fog.StandardFogModifier;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(StandardFogModifier.class)
public abstract class MixinStandardFogModifier {
    @Unique private static int modernBeta_renderDistance = 16;
    @Unique private static float modernBeta_fogWeight = FogUtils.calculateFogWeight(16);
    @Unique private static boolean modernBeta_isModernBetaWorld = false;

    @Inject(method = "getFogColor", at = @At("HEAD"))
    private void captureVars(ClientWorld world, Camera camera, int renderDistance, float skyDarkness, CallbackInfoReturnable<Integer> cir) {
        if (modernBeta_renderDistance != renderDistance) {
            modernBeta_renderDistance = renderDistance;
            modernBeta_fogWeight = FogUtils.calculateFogWeight(renderDistance);
        }

        // Track whether current client world is Modern Beta world,
        // old fog weighting won't be used if not.
        modernBeta_isModernBetaWorld = ((ModernBetaClientWorld)world).isModernBetaWorld();
    }

    @SuppressWarnings("DiscouragedShift")
    @ModifyVariable(
            method = "getFogColor",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;pow(DD)D",
                    remap = false,
                    shift = At.Shift.BY,
                    by = 4
            ),
            index = 16
    )
    private float modifyFogWeighting(float weight) {
        return modernBeta_isModernBetaWorld && ModernerBeta.CONFIG.useOldFogColor ? modernBeta_fogWeight : weight;
    }
}
*///?}