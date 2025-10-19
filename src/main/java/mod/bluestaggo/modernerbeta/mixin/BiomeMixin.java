package mod.bluestaggo.modernerbeta.mixin;

import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaLevel;
import mod.bluestaggo.modernerbeta.world.feature.BetaSnowAndFreezeFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public abstract class BiomeMixin {
    @Shadow @Final private Biome.ClimateSettings climateSettings;

    @Inject(
        method = "shouldFreeze(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Z)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void canSetIceWithModernBetaClimate(LevelReader level, BlockPos pos, boolean doWaterCheck, CallbackInfoReturnable<Boolean> cir) {
        if (!(level instanceof ModernBetaLevel modernBetaLevel))
            return;

        ClimateSampler climateSampler = modernBetaLevel.modernerBeta$getClimateSampler();
        if (climateSampler == null)
            return;

        cir.setReturnValue(BetaSnowAndFreezeFeature.canSetIce(
            level,
            pos,
            doWaterCheck,
            climateSampler.sampleModifiedTemperature(pos, this.climateSettings.temperatureModifier()),
            climateSampler.getSnowThreshold(),
            climateSampler.getHeightType()
        ));
    }

    @Inject(
        method = "shouldSnow",
        at = @At("HEAD"),
        cancellable = true
    )
    private void canSetSnowWithModernBetaClimate(LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!(level instanceof ModernBetaLevel modernBetaLevel))
            return;

        ClimateSampler climateSampler = modernBetaLevel.modernerBeta$getClimateSampler();
        if (climateSampler == null)
            return;

        cir.setReturnValue(BetaSnowAndFreezeFeature.canSetSnow(
            level,
            pos,
            climateSampler.sampleModifiedTemperature(pos, this.climateSettings.temperatureModifier()),
            climateSampler.getSnowThreshold(),
            climateSampler.getHeightType()
        ));
    }
}
