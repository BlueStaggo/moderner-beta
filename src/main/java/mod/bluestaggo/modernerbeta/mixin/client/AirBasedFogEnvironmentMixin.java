//? if >=1.21.6 {
package mod.bluestaggo.modernerbeta.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.FogUtils;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaLevel;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
    //? if >=1.21.11 {
    net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment.class
    //? } else {
    /*net.minecraft.client.renderer.fog.environment.AirBasedFogEnvironment.class
    *///? }
)
public abstract class AirBasedFogEnvironmentMixin {
    //? if <1.21.11 {
    /*@Unique private static int modernBeta_renderDistance = 16;
    @Unique private static float modernBeta_fogWeight = FogUtils.calculateFogWeight(16);
    @Unique private static boolean modernBeta_isModernBetaLevel = false;

    @Inject(method = "getBaseColor", at = @At("HEAD"))
    private void captureVars(ClientLevel level, Camera camera, int renderDistance, float partialTick, CallbackInfoReturnable<Integer> cir) {
        if (modernBeta_renderDistance != renderDistance) {
            modernBeta_renderDistance = renderDistance;
            modernBeta_fogWeight = FogUtils.calculateFogWeight(renderDistance);
        }

        // Track whether current client world is Modern Beta world,
        // old fog weighting won't be used if not.
        modernBeta_isModernBetaLevel = ((ModernBetaLevel)level).modernerBeta$isModded();
    }
    *///? }

    @WrapOperation(
        method = "getBaseColor",
        at = @At(
            value = "INVOKE",
            target = "Ljava/lang/Math;pow(DD)D",
            remap = false
        )
    )
    private double modifyFogWeighting(double a, double b, Operation<Double> original, ClientLevel level, Camera camera, int renderDistance, float partialTick) {
        //? if >=1.21.11 {
        double baseWeight = ((ModernBetaLevel)level).modernerBeta$isModded() && ModernerBeta.config.getOrDefault(SettingsComponentTypes.CONFIG_MISCELLANEOUS).oldFogColorWeighting() ?
                FogUtils.calculateFogWeight(renderDistance, camera, partialTick) : a;
        //? } else {
        /*double baseWeight = modernBeta_isModernBetaLevel && ModernerBeta.config.getOrDefault(SettingsComponentTypes.CONFIG_MISCELLANEOUS).oldFogColorWeighting() ?
                modernBeta_fogWeight : a;
        *///? }
        return original.call(baseWeight, b);
    }
}
//?}
