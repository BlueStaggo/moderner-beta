package mod.bluestaggo.modernerbeta.mixin;

import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaWorld;
import mod.bluestaggo.modernerbeta.world.feature.BetaFreezeTopLayerFeature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public abstract class MixinBiome {
    @Shadow @Final private Biome.Weather weather;

    @Inject(
        method = "canSetIce(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;Z)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void canSetIceWithModernBetaClimate(WorldView world, BlockPos pos, boolean doWaterCheck, CallbackInfoReturnable<Boolean> cir) {
        if (!(world instanceof ModernBetaWorld serverWorld))
            return;

        ClimateSampler climateSampler = serverWorld.modernerBeta$getClimateSampler();
        if (climateSampler == null)
            return;

        cir.setReturnValue(BetaFreezeTopLayerFeature.canSetIce(
            world,
            pos,
            doWaterCheck,
            climateSampler.sampleModifiedTemperature(pos, this.weather.temperatureModifier()),
            climateSampler.getSnowThreshold(),
            climateSampler.getHeightType()
        ));
    }

    @Inject(
        method = "canSetSnow",
        at = @At("HEAD"),
        cancellable = true
    )
    private void canSetSnowWithModernBetaClimate(WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!(world instanceof ModernBetaWorld serverWorld))
            return;

        ClimateSampler climateSampler = serverWorld.modernerBeta$getClimateSampler();
        if (climateSampler == null)
            return;

        cir.setReturnValue(BetaFreezeTopLayerFeature.canSetSnow(
            world,
            pos,
            climateSampler.sampleModifiedTemperature(pos, this.weather.temperatureModifier()),
            climateSampler.getSnowThreshold(),
            climateSampler.getHeightType()
        ));
    }
}
