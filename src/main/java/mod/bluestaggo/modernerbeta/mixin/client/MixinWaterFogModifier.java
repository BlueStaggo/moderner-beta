//? if >=1.21.6 {
/*package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.api.world.biome.climate.Clime;
import mod.bluestaggo.modernerbeta.client.color.BlockColorSampler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.fog.WaterFogModifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(WaterFogModifier.class)
public abstract class MixinWaterFogModifier {
    @Unique
    private static Vec3d modernBeta_pos;

    @Redirect(
            method = "getFogColor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/biome/Biome;getWaterFogColor()I"
            )
    )
    private int modifyWaterFogColor(Biome instance) {
        if (BlockColorSampler.INSTANCE.useWaterColor()) {
            int x = (int)modernBeta_pos.getX();
            int z = (int)modernBeta_pos.getZ();

            Clime clime = BlockColorSampler.INSTANCE.getClimateSampler().sample(x, z);

            return BlockColorSampler.INSTANCE.colormapUnderwater.getColor(clime.temp(), clime.rain());
        }

        return instance.getWaterFogColor();
    }

    @Inject(method = "getFogColor", at = @At("HEAD"))
    private void captureVars(ClientWorld world, Camera camera, int viewDistance, float skyDarkness, CallbackInfoReturnable<Integer> cir) {
        modernBeta_pos = camera.getPos();
    }
}
*///?}