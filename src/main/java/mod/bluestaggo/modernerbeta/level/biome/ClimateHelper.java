//~minBuild
package mod.bluestaggo.modernerbeta.level.biome;

import mod.bluestaggo.modernerbeta.api.level.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.TemperatureHeightScaling;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaLevel;
import mod.bluestaggo.modernerbeta.mixin.BiomeAccessor;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class ClimateHelper {
    public static boolean shouldFreeze(
        LevelReader level,
        BlockPos blockPos,
        boolean doWaterCheck,
        double temp,
        double coldThreshold,
        TemperatureHeightScaling heightType
    ) {
        if (warmEnoughToRain(blockPos, temp, coldThreshold, heightType)) {
            return false;
        }

        if (blockPos.getY() >= level.getMinY() &&
            blockPos.getY() < VersionCompat.getTopYExclusive(level) &&
            level.getBrightness(LightLayer.BLOCK, blockPos) < 10
        ) {
            BlockState blockState = level.getBlockState(blockPos);
            FluidState fluidState = level.getFluidState(blockPos);

            if (fluidState.getType() == Fluids.WATER && blockState.getBlock() instanceof LiquidBlock) {
                if (!doWaterCheck) {
                    return true;
                }

                boolean submerged =
                    level.isWaterAt(blockPos.west()) &&
                    level.isWaterAt(blockPos.east()) &&
                    level.isWaterAt(blockPos.north()) &&
                    level.isWaterAt(blockPos.south());

                return !submerged;
            }
        }

        return false;
    }

    public static boolean coldEnoughToSnow(Biome biome, BlockPos blockPos, LevelReader level) {
        return !warmEnoughToRain(biome, blockPos, level);
    }

    public static boolean coldEnoughToSnow(
        BlockPos blockPos,
        double temp,
        double coldThreshold,
        TemperatureHeightScaling heightType
    ) {
        return !warmEnoughToRain(blockPos, temp, coldThreshold, heightType);
    }

    public static boolean warmEnoughToRain(Biome biome, BlockPos blockPos, LevelReader level) {
        if (!(level instanceof ModernBetaLevel modernBetaLevel))
            return biome.warmEnoughToRain(blockPos /*? >=1.21.3 {*/, level.getSeaLevel()/*?}*/);

        ClimateSampler climateSampler = modernBetaLevel.modernerBeta$getClimateSampler();
        if (climateSampler == null)
            return biome.warmEnoughToRain(blockPos /*? >=1.21.3 {*/, level.getSeaLevel()/*?}*/);

        double temp = climateSampler.sampleModifiedTemperature(blockPos,
                ((BiomeAccessor)(Object) biome).getClimateSettings().temperatureModifier());
        return warmEnoughToRain(blockPos, temp, climateSampler.getSnowThreshold(), climateSampler.getHeightType());
    }

    public static boolean warmEnoughToRain(
        BlockPos blockPos,
        double temp,
        double coldThreshold,
        TemperatureHeightScaling heightType
    ) {
        return heightType.modifyTemperature(blockPos, temp) >= coldThreshold;
    }

    public static boolean shouldSnow(
        LevelReader level,
        BlockPos blockPos,
        double temp,
        double coldThreshold,
        TemperatureHeightScaling heightType
    ) {
        if (warmEnoughToRain(blockPos, temp, coldThreshold, heightType)) {
            return false;
        }

        if (blockPos.getY() >= 0 && blockPos.getY() < 256 && level.getBrightness(LightLayer.BLOCK, blockPos) < 10) {
            BlockState blockState = level.getBlockState(blockPos);

            return blockState.isAir() && Blocks.SNOW.defaultBlockState().canSurvive(level, blockPos);
        }

        return false;
    }
}
