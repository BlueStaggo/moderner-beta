package mod.bluestaggo.modernerbeta.api.world.biome.climate;

import mod.bluestaggo.modernerbeta.mixin.AccessorBiome;
import mod.bluestaggo.modernerbeta.settings.component.ClimateDistribution;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

/**
 * Implemented by a climate sampler to provide temperatures and rainfall values,
 * for use by a biome provider or chunk provider.
 *
 */
public interface ClimateSampler {
    /**
     * Sample temperature/rainfall values in range [0.0, 1.0] given block coordinates.
     * 
     * @param x x-coordinate in block coordinates.
     * @param z z-coordinate in block coordinates.
     * 
     * @return A Clime containing temperature/rainfall values in range [0.0, 1.0] sampled at position.
     */
    Clime sample(int x, int z);

    /**
     * Sample temperature/rainfall values with a modifier.
     *
     * @param blockPos Block coordinates to sample from.
     * @param modifier The modifier to apply.
     *
     * @return A Clime containing temperature/rainfall values in range [0.0, 1.0] sampled at position.
     */
    default double sampleModifiedTemperature(BlockPos blockPos, Biome.TemperatureModifier modifier) {
        double temp = this.sample(blockPos.getX(), blockPos.getZ()).temp();
        if (modifier != Biome.TemperatureModifier.NONE) {
            temp = modifier.getModifiedTemperature(blockPos, (float)temp);
        }
        return temp;
    }

    default Biome.Precipitation samplePrecipitation(Biome biome, BlockPos blockPos) {
        if (!biome.hasPrecipitation()) {
            return Biome.Precipitation.NONE;
        }
        double temperature = this.sampleModifiedTemperature(blockPos,
            ((AccessorBiome)(Object)biome).getWeather().temperatureModifier());
        return temperature < this.getSnowThreshold() ? Biome.Precipitation.SNOW : Biome.Precipitation.RAIN;
    }

    /**
     * Indicate to block colors whether to sample climate values for biome tinting.
     * 
     * @return Whether to use climate values for biome tinting.
     */
    default boolean useBiomeColor() {
        return false;
    }
    
    /**
     * Indicate to block colors whether to sample climate values for water tinting.
     * 
     * @return Whether to use climate values for water tinting.
     */
    default boolean useWaterColor() {
        return false;
    }
    
    /**
     * Indicate whether to sample climate values for features, e.g. freeze top layer.
     * 
     * @return Whether to use climate values for feature generation.
     */
    default boolean useBiomeFeature() {
        return true;
    }

    /**
     * Get the method of distributing climate. Includes fuzzy tall grass and smooth biome borders.
     *
     * @return The climate distribution method.
     */
    default ClimateDistribution getDistribution() {
        return ClimateDistribution.DEFAULT;
    }

    /**
     * Indicate the temperature level below which snow generates.
     *
     * @return The temperature level below which snow generates.
     */
    default double getSnowThreshold() {
        return 0.5;
    }

    /**
     * Indicate the formula to use for calculating temperature at different altitudes.
     *
     * @return The formula to use for calculating temperature at different altitudes.
     */
    default TemperatureHeightScaling getHeightType() {
        return TemperatureHeightScaling.NONE;
    }
}
