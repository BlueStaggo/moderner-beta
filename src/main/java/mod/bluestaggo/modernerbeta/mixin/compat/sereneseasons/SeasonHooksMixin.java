package mod.bluestaggo.modernerbeta.mixin.compat.sereneseasons;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.TemperatureHeightScaling;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaLevel;
import mod.bluestaggo.modernerbeta.level.biome.ClimateHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnresolvedMixinReference")
@Pseudo
@Mixin(targets = "sereneseasons.season.SeasonHooks")
public abstract class SeasonHooksMixin {
    @Unique
    private static Level modernerBeta$level;

    @Shadow
    private static float getBiomeTemperature(LevelReader level, Holder<Biome> biome, BlockPos pos /*? >=1.21.3 {*/, int seaLevel/*?}*/) {
        throw new AssertionError("Failed to mixin.");
    }

    @WrapOperation(
        method = "shouldSnowHook",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/biome/Biome;warmEnoughToRain(Lnet/minecraft/core/BlockPos;"/*? >=1.21.3 {*/ + "I" /*?}*/ + ")Z"
        )
    )
    private static boolean wrapWarmEnoughToRainSnow(
        Biome biome, BlockPos pos, /*? >=1.21.3 {*/ int seaLevel, /*?}*/ Operation<Boolean> original,
        Biome biome2, LevelReader level, BlockPos pos2 /*? >=1.21.3 {*/, int i2/*?}*/
    ) {
        return ClimateHelper.warmEnoughToRain(biome, pos, level);
    }

    @WrapOperation(
        method = "shouldFreezeWarmEnoughToRainHook",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/biome/Biome;warmEnoughToRain(Lnet/minecraft/core/BlockPos;"/*? >=1.21.3 {*/ + "I" /*?}*/ + ")Z"
        )
    )
    private static boolean wrapWarmEnoughToRainFrost(
        Biome biome, BlockPos pos, /*? >=1.21.3 {*/ int seaLevel, /*?}*/ Operation<Boolean> original,
        Biome biome2, BlockPos pos2, /*? >=1.21.3 {*/ int i2, /*?}*/ LevelReader level
    ) {
        return ClimateHelper.warmEnoughToRain(biome, pos, level);
    }

    @Inject(
        method = "isRainingAtHook",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/Holder;value()Ljava/lang/Object;",
            ordinal = 0
        ),
        cancellable = true
    )
    private static void replaceWarmEnoughToRainCheck(Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        Biome biome = level.getBiome(pos).value();

        Biome.Precipitation precipitation = biome.getPrecipitationAt(pos /*? >=1.21.3 {*/, level.getSeaLevel() /*?}*/);
        cir.setReturnValue(precipitation == Biome.Precipitation.RAIN && ClimateHelper.warmEnoughToRain(biome, pos, level));
    }

    @WrapOperation(
        method = "getPrecipitationAtTickIceAndSnowHook",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/biome/Biome;coldEnoughToSnow(Lnet/minecraft/core/BlockPos;"/*? >=1.21.3 {*/ + "I" /*?}*/ + ")Z"
        )
    )
    private static boolean wrapColdEnoughToSnowTick(
        Biome biome, BlockPos pos, /*? >=1.21.3 {*/ int seaLevel, /*?}*/ Operation<Boolean> original,
        LevelReader level, Biome biome2, BlockPos pos2 /*? >=1.21.3 {*/, int i2 /*?}*/
    ) {
        return ClimateHelper.coldEnoughToSnow(biome, pos, level);
    }

    @Inject(
        target = @Desc(
            value = "warmEnoughToRainSeasonal",
            args = { LevelReader.class, Holder.class, BlockPos.class /*? >=1.21.3 {*/, int.class /*?}*/ },
            ret = boolean.class
        ),
        at = @At("HEAD"),
        cancellable = true
    )
    private static void warmEnoughToRainForSeasonalModernBeta(LevelReader level, Holder<Biome> biome, BlockPos pos, /*? >=1.21.3 {*/ int seaLevel, /*?}*/ CallbackInfoReturnable<Boolean> cir) {
        if (!(level instanceof ModernBetaLevel modernBetaLevel))
            return;

        ClimateSampler climateSampler = modernBetaLevel.modernerBeta$getClimateSampler();
        if (climateSampler == null)
            return;

        cir.setReturnValue(getBiomeTemperature(level, biome, pos /*? >=1.21.3 {*/, seaLevel /*?}*/) >= climateSampler.getSnowThreshold());
    }

    @Inject(
        target = @Desc(
            value = "getBiomeTemperature",
            args = { Level.class, Holder.class, BlockPos.class /*? >=1.21.3 {*/, int.class /*?}*/ },
            ret = float.class
        ),
        at = @At("HEAD")
    )
    private static void captureLevel(Level level, Holder<Biome> holder, BlockPos pos, /*? >=1.21.3 {*/ int seaLevel, /*?}*/ CallbackInfoReturnable<Float> ci) {
        modernerBeta$level = level;
    }

    @WrapOperation(
        method = "getBiomeTemperatureInSeason",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/biome/Biome;getTemperature(Lnet/minecraft/core/BlockPos;"/*? >=1.21.3 {*/ + "I" /*?}*/ + ")F"
        )
    )
    private static float wrapGetTemperature(Biome biome, BlockPos pos, /*? >=1.21.3 {*/ int seaLevel, /*?}*/ Operation<Float> original) {
        if (!(modernerBeta$level instanceof ModernBetaLevel modernBetaLevel))
            return original.call(biome, pos /*? >=1.21.3 {*/, seaLevel /*?}*/);

        if (modernBetaLevel.modernerBeta$getTemperatureHeightScaling() == TemperatureHeightScaling.BETA) {
            pos = pos.atY(64);
        }

        return (float) modernBetaLevel.modernerBeta$sampleTemperature(biome, pos);
    }
}
