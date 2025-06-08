//? if <1.21.6 {
package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.Clime;
import mod.bluestaggo.modernerbeta.client.FogUtils;
import mod.bluestaggo.modernerbeta.client.color.BlockColorSampler;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaWorld;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

//? if >=1.21.2 {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//?} else {
/*import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
*///?}

@Environment(EnvType.CLIENT)
@Mixin(BackgroundRenderer.class)
public abstract class MixinBackgroundRenderer {
    @Unique private static final String GET_FOG_COLOR_METHOD =
        //? if >=1.21.2 {
        "getFogColor";
        //?} else {
        /*"render";
        *///?}

    @Unique private static Vec3d modernBeta_pos;
    @Unique private static int modernBeta_renderDistance = 16;
    @Unique private static float modernBeta_fogWeight = FogUtils.calculateFogWeight(16);
    @Unique private static boolean modernBeta_isModernBetaWorld = false;

    //? if >=1.20.2 {
    @WrapOperation(
    //?} else {
    /*@Redirect(
    *///?}
        method = GET_FOG_COLOR_METHOD,
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/biome/Biome;getWaterFogColor()I"
        )
    )
    private static int modifyWaterFogColor(Biome instance
            //? if >=1.20.2
            , Operation<Integer> original
    ) {
        if (BlockColorSampler.INSTANCE.useWaterColor()) {
            int x = (int)modernBeta_pos.getX();
            int z = (int)modernBeta_pos.getZ();
            
            Clime clime = BlockColorSampler.INSTANCE.getClimateSampler().sample(x, z);
            return BlockColorSampler.INSTANCE.colormapUnderwater.getColor(clime.temp(), clime.rain());
        }

        //? if >=1.20.2 {
        return original.call(instance);
        //?} else {
        /*return instance.getWaterFogColor();
        *///?}
    }
    
    @Inject(method = GET_FOG_COLOR_METHOD, at = @At("HEAD"))
    private static void captureVars(Camera camera, float tickDelta, ClientWorld world, int renderDistance, float skyDarkness,
                                    //? if >=1.21.2 {
                                    CallbackInfoReturnable<Vector4f> cir
                                    //?} else {
                                    /*CallbackInfo ci
                                    *///?}
    ) {
        modernBeta_pos = camera.getPos();

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
            target = "Lnet/minecraft/client/world/ClientWorld;getSkyColor(Lnet/minecraft/util/math/Vec3d;F)I"
            //?} else {
            /*target = "Lnet/minecraft/client/world/ClientWorld;getSkyColor(Lnet/minecraft/util/math/Vec3d;F)Lnet/minecraft/util/math/Vec3d;"
            *///?}
        ),
        index = /*? if >=1.21.2 {*/10/*?} else {*/ /*7 *//*?}*/
    )
    private static float modifyFogWeighting(float weight) {
        return modernBeta_isModernBetaWorld && ModernerBeta.CONFIG.useOldFogColor ? modernBeta_fogWeight : weight;
    }
}
//?}