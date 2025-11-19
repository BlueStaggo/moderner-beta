package mod.bluestaggo.modernerbeta.imixin;

import mod.bluestaggo.modernerbeta.api.level.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.TemperatureHeightScaling;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;

public interface ModernBetaLevel {
    boolean modernerBeta$isModded();
    void modernerBeta$setModded(boolean toggle);
    ClimateSampler modernerBeta$getClimateSampler();
    void modernerBeta$setClimateSampler(ClimateSampler climateSampler);
    TemperatureHeightScaling modernerBeta$getTemperatureHeightScaling();
    void modernerBeta$setTemperatureHeightScaling(TemperatureHeightScaling temperatureHeightScaling);

    double modernerBeta$sampleTemperature(Biome biome, BlockPos pos);
    Biome.Precipitation modernerBeta$samplePrecipitation(Biome biome, BlockPos pos);
}
