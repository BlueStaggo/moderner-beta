//? if >=1.21.6 {
package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.FogUtils;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaLevel;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.environment.AirBasedFogEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(AirBasedFogEnvironment.class)
public abstract class AirBasedFogEnvironmentMixin {
    @Unique private static int modernBeta_renderDistance = 16;
    @Unique private static float modernBeta_fogWeight = FogUtils.calculateFogWeight(16);
    @Unique private static boolean modernBeta_isModernBetaLevel = false;

    @Inject(method = "getBaseColor", at = @At("HEAD"))
    private void captureVars(ClientLevel level, Camera camera, int renderDistance, float skyDarkness, CallbackInfoReturnable<Integer> cir) {
        if (modernBeta_renderDistance != renderDistance) {
            modernBeta_renderDistance = renderDistance;
            modernBeta_fogWeight = FogUtils.calculateFogWeight(renderDistance);
        }

        // Track whether current client world is Modern Beta world,
        // old fog weighting won't be used if not.
        modernBeta_isModernBetaLevel = ((ModernBetaLevel)level).modernerBeta$isModded();
    }

    @SuppressWarnings("DiscouragedShift")
    @ModifyVariable(
            method = "getBaseColor",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;pow(DD)D",
                    remap = false,
                    shift = At.Shift.BY,
                    by = 4
            ),
            index = /*? >=1.21.11 {*/ /*14 *//*? } else {*/ 16 /*?}*/
    )
    private float modifyFogWeighting(float weight) {
        return modernBeta_isModernBetaLevel && ModernerBeta.config.getOrDefault(SettingsComponentTypes.CONFIG_MISCELLANEOUS).oldFogColorWeighting() ? modernBeta_fogWeight : weight;
    }
}
//?}
