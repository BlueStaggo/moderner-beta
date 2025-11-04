//? if <1.21.11 {
package mod.bluestaggo.modernerbeta.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.bluestaggo.modernerbeta.client.color.SkyColorSampler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.CubicSampler;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ClientLevel.class, priority = 1)
public abstract class ClientLevelMixin {
    @WrapOperation(
        method = "getSkyColor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/CubicSampler;gaussianSampleVec3(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/util/CubicSampler$Vec3Fetcher;)Lnet/minecraft/world/phys/Vec3;"
        )
    )
    private Vec3 injectGetSkyColor(Vec3 instance, CubicSampler.Vec3Fetcher j, Operation<Vec3> original, Vec3 pos, float f2) {
        SkyColorSampler sampler = SkyColorSampler.INSTANCE;

        Vec3 originalColor = original.call(instance, j);

        if (sampler.useSkyColor())
            return SkyColorSampler.INSTANCE.getSkyColor(pos);

        return originalColor;
    }
}
//? }