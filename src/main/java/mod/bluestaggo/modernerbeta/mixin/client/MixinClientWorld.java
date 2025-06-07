package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.client.color.SkyColorSampler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Environment(EnvType.CLIENT)
@Mixin(value = ClientWorld.class, priority = 1)
public abstract class MixinClientWorld {
    @ModifyVariable(
        method = "getSkyColor",
        at = @At(
            value = "INVOKE_ASSIGN",  
            target = "Lnet/minecraft/util/CubicSampler;sampleColor(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/CubicSampler$RgbFetcher;)Lnet/minecraft/util/math/Vec3d;"
        ),
        index = /*? if >=1.21.2 {*/5/*?} else {*/ /*6 *//*?}*/
    )
    private Vec3d injectSkyColor(Vec3d skyColorVec, Vec3d cameraPos) {
        return SkyColorSampler.INSTANCE.getSkyColor(cameraPos, skyColorVec);
    }
}

