//? if >=1.21.2 {
package mod.bluestaggo.modernerbeta.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaWorld;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

//? if >=1.20.2 {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.WeatherRendering;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
//?} else {
/*import org.spongepowered.asm.mixin.injection.Redirect;
*///?}

@Environment(EnvType.CLIENT)
@Mixin(WeatherRendering.class)
public class MixinWeatherRendering {
    //? if >=1.20.2 {
    @WrapOperation(
        //?} else {
        /*@Redirect(
        *///?}
        method = "getPrecipitationAt",
        at = @At(
            value = "INVOKE",
            target = VersionCompat.BIOME_GET_PRECIPITATION_TARGET
        )
    )
    public Biome.Precipitation modifyTickPrecipitation(
        Biome biome, BlockPos blockPos
        /*? if >=1.21.2 {*/, int seaLevel/*?}*/
        /*? if >=1.20.2 {*/, Operation<Biome.Precipitation> original/*?}*/
        , @Local(argsOnly = true) World world
    ) {
        ModernBetaWorld modernBetaWorld = (ModernBetaWorld)world;

        if (!modernBetaWorld.modernerBeta$isModded()) {
            //? if >=1.21.2 {
            return original.call(biome, blockPos, seaLevel);
            //?} else if >=1.20.2 {
            /*return original.call(biome, blockPos);
             *///?} else {
            /*return biome.getPrecipitation(blockPos);
            *///?}
        }
        return modernBetaWorld.modernerBeta$samplePrecipitation(biome, blockPos);
    }
}
//?}