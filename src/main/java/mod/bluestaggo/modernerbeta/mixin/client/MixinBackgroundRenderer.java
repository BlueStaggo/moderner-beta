//? if <1.21.6 {
/*package mod.bluestaggo.modernerbeta.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.Clime;
import mod.bluestaggo.modernerbeta.client.FogUtils;
import mod.bluestaggo.modernerbeta.client.color.BlockColorSampler;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaWorld;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

//? if >=1.21.2 {
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//?} else {
/^import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
^///?}

@Environment(EnvType.CLIENT)
@Mixin(FogRenderer.class)
public abstract class MixinBackgroundRenderer {
    @Unique private static final String GET_FOG_COLOR_METHOD =
            //? if >=1.21.2 {
            "computeFogColor";
            //?} else {
            /^"setupColor";
            ^///?}

    @Unique private static Vec3 modernBeta_pos;
    @Unique private static int modernBeta_renderDistance = 16;
    @Unique private static float modernBeta_fogWeight = FogUtils.calculateFogWeight(16);
    @Unique private static boolean modernBeta_isModernBetaWorld = false;

    @WrapOperation(
        method = GET_FOG_COLOR_METHOD,
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/biome/Biome;getWaterFogColor()I"
        )
    )
    private static int modifyWaterFogColor(Biome instance, Operation<Integer> original) {
        if (BlockColorSampler.INSTANCE.useWaterColor()) {
            int x = (int)modernBeta_pos.x();
            int z = (int)modernBeta_pos.z();

            Clime clime = BlockColorSampler.INSTANCE.getClimateSampler().sample(x, z);
            return BlockColorSampler.INSTANCE.colormapUnderwater.getColor(clime.temp(), clime.rain());
        }

        return original.call(instance);
    }

    @Inject(method = GET_FOG_COLOR_METHOD, at = @At("HEAD"))
    private static void captureVars(Camera camera, float tickDelta, ClientLevel world, int renderDistance, float skyDarkness,
                                    //? if >=1.21.2 {
                                    CallbackInfoReturnable<Vector4f> cir
                                    //?} else {
                                    /^CallbackInfo ci
                                    ^///?}
    ) {
        modernBeta_pos = camera.getPosition();

        if (modernBeta_renderDistance != renderDistance) {
            modernBeta_renderDistance = renderDistance;
            modernBeta_fogWeight = FogUtils.calculateFogWeight(renderDistance);
        }

        // Track whether current client world is Modern Beta world,
        // old fog weighting won't be used if not.
        modernBeta_isModernBetaWorld = ((ModernBetaWorld)world).modernerBeta$isModded();
    }

    @ModifyVariable(
        method = GET_FOG_COLOR_METHOD,
        at = @At(
            value = "INVOKE",
            //? if >=1.21.2 {
            target = "Lnet/minecraft/client/multiplayer/ClientLevel;getSkyColor(Lnet/minecraft/world/phys/Vec3;F)I"
            //?} else {
            /^target = "Lnet/minecraft/client/multiplayer/ClientLevel;getSkyColor(Lnet/minecraft/world/phys/Vec3;F)Lnet/minecraft/world/phys/Vec3;"
            ^///?}
        ),
        index = /^? if >=1.21.2 {^/10/^?} else {^/ /^7 ^//^?}^/
    )
    private static float modifyFogWeighting(float weight) {
        return modernBeta_isModernBetaWorld && ModernerBeta.config.getOrDefault(SettingsComponentTypes.CONFIG_MISCELLANEOUS).oldFogColorWeighting() ? modernBeta_fogWeight : weight;
    }
}
*///?}
