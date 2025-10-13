//? if >=1.21.2 {
package mod.bluestaggo.modernerbeta.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaWorld;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(WeatherEffectRenderer.class)
public class MixinWeatherRendering {
    @WrapOperation(
        method = "getPrecipitationAt",
        at = @At(
            value = "INVOKE",
            target = VersionCompat.BIOME_GET_PRECIPITATION_TARGET
        )
    )
    public Biome.Precipitation modifyTickPrecipitation(
        Biome biome, BlockPos blockPos
        /*if >=1.21.2 */, int seaLevel/**/
        , Operation<Biome.Precipitation> original,
        @Local(argsOnly = true) Level world
    ) {
        ModernBetaWorld modernBetaWorld = (ModernBetaWorld)world;

        if (!modernBetaWorld.modernerBeta$isModded()) {
            return original.call(biome, blockPos, seaLevel);
        }

        return modernBetaWorld.modernerBeta$samplePrecipitation(biome, blockPos);
    }
}
//?}
