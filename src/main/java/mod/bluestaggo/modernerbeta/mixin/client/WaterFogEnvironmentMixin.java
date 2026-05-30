//? if >=1.21.6 {
package mod.bluestaggo.modernerbeta.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.Clime;
import mod.bluestaggo.modernerbeta.client.color.block.BlockColorSampler;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.environment.WaterFogEnvironment;
//? if >=1.21.11 {
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeProbe;
//? } else {
/*import net.minecraft.world.level.biome.Biome;
*///? }
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WaterFogEnvironment.class)
public abstract class WaterFogEnvironmentMixin {
    @Unique
    private static Vec3 modernBeta_pos;

    //? if >=1.21.11
    @SuppressWarnings("rawtypes")
    @WrapOperation(
        method = "getBaseColor",
        at = @At(
            value = "INVOKE",
            target =
                //? if >=1.21.11 {
                "Lnet/minecraft/world/attribute/EnvironmentAttributeProbe;getValue(Lnet/minecraft/world/attribute/EnvironmentAttribute;F)Ljava/lang/Object;"
                //? } else {
                /*"Lnet/minecraft/world/level/biome/Biome;getWaterFogColor()I"
                *///? }
        )
    )
    private /*? >=1.21.11 {*/Object/*?} else {*//*int*//*?}*/ modifyWaterFogColor(
        //? if >=1.21.11 {
        EnvironmentAttributeProbe instance,
        EnvironmentAttribute environmentAttribute,
        float f,
        Operation original
        //? } else {
        /*Biome instance, Operation<Integer> original
        *///? }
    ) {
        if (BlockColorSampler.INSTANCE.useWaterColor()) {
            int x = (int)modernBeta_pos.x();
            int z = (int)modernBeta_pos.z();

            Clime clime = BlockColorSampler.INSTANCE.getClimateSampler().sample(x, z);

            return BlockColorSampler.INSTANCE.colormapUnderwater.getColor(clime.temp(), clime.rain());
        }

        return original.call(instance /*? >=1.21.11{*/, environmentAttribute, f/*?}*/);
    }

    @Inject(method = "getBaseColor", at = @At("HEAD"))
    private void captureVars(ClientLevel level, Camera camera, int viewDistance, float skyDarkness, CallbackInfoReturnable<Integer> cir) {
        modernBeta_pos = camera/*? >=1.21.11 {*/.position()/*?} else {*//*.getPosition()*//*?}*/;
    }
}
//?}
