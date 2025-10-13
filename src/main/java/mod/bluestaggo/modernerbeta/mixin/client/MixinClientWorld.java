package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.client.color.SkyColorSampler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Environment(EnvType.CLIENT)
@Mixin(value = ClientLevel.class, priority = 1)
public abstract class MixinClientWorld {
    @ModifyVariable(
        method = "getSkyColor",
        at = @At(
            value = "INVOKE_ASSIGN",  
            target = "Lnet/minecraft/util/CubicSampler;gaussianSampleVec3(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/util/CubicSampler$Vec3Fetcher;)Lnet/minecraft/world/phys/Vec3;"
        ),
        index = /*? if >=1.21.2 {*/5/*?} else {*/ /*6 *//*?}*/
    )
    private Vec3 injectSkyColor(Vec3 skyColorVec, Vec3 cameraPos) {
        return SkyColorSampler.INSTANCE.getSkyColor(cameraPos, skyColorVec);
    }
}

