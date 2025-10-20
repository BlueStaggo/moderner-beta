package mod.bluestaggo.modernerbeta.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.bluestaggo.modernerbeta.client.color.SkyColorSampler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
//? if >=1.21.11 {
/*import net.minecraft.client.Camera;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeProbe;
*///? } else {
import net.minecraft.util.CubicSampler;
import net.minecraft.world.phys.Vec3;
//? }
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(value = ClientLevel.class, priority = 1)
public abstract class ClientLevelMixin {
    //? if >=1.21.11
    /*@SuppressWarnings("rawtypes")*/
    @WrapOperation(
        method = "getSkyColor",
        at = @At(
            value = "INVOKE",
            target =
                //? if >=1.21.11 {
                /*"Lnet/minecraft/world/attribute/EnvironmentAttributeProbe;getValue(Lnet/minecraft/world/attribute/EnvironmentAttribute;F)Ljava/lang/Object;"
                *///? } else {
                "Lnet/minecraft/util/CubicSampler;gaussianSampleVec3(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/util/CubicSampler$Vec3Fetcher;)Lnet/minecraft/world/phys/Vec3;"
                //? }
        )
    )
    private /*? >=1.21.11 {*//*Object*//*?} else {*/Vec3/*?}*/ injectGetSkyColor(
        //? if >=1.21.11 {
        /*EnvironmentAttributeProbe instance,
        EnvironmentAttribute environmentAttribute,
        float f,
        Operation original,
        Camera camera,
        float f2
        *///? } else {
        Vec3 instance, CubicSampler.Vec3Fetcher j, Operation<Vec3> original, Vec3 pos, float f2
        //? }
    ) {
        //? if >=1.21.11 {
        /*return SkyColorSampler.INSTANCE.getSkyColor(camera.position(), original.call(environmentAttribute, f));
        *///? } else {
        return SkyColorSampler.INSTANCE.getSkyColor(pos, original.call(instance, j));
        //? }
    }
}

