package mod.bluestaggo.modernerbeta.level.biome.provider;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.TemperatureHeightScaling;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.ClimateSamplerSky;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.Clime;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.ClimateDistribution;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.intlayers.IntClimateSampleLayer;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.intlayers.IntLayer;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.intlayers.IntZoomLayer;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import net.minecraft.core.HolderGetter;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

public class BiomeProviderBetaFractal extends BiomeProviderFractal implements ClimateSampler, ClimateSamplerSky {
    private final IntLayer temperatureLayer;
    private final IntLayer downfallLayer;
    private final ClimateDistribution distribution;

    public BiomeProviderBetaFractal(ModernBetaSettings settings, HolderGetter<Biome> biomeRegistry, long seed) {
        super(settings, biomeRegistry, seed);

        int i = 0;
        IntLayer temperatureLayer = null;
        IntLayer downfallLayer = null;

        while (true) {
            Optional<Layer> layer = this.configuredLayers.getOutput(ModernerBeta.createId("climate_" + i));
            if (layer.isEmpty()) {
                break;
            }

            if (i == 0) {
                temperatureLayer = new IntClimateSampleLayer(layer.get(), false);
                downfallLayer = new IntClimateSampleLayer(layer.get(), true);
            } else {
                temperatureLayer = new IntClimateSampleLayer(layer.get(), false, new IntZoomLayer(999 + i, temperatureLayer), i + 1);
                downfallLayer = new IntClimateSampleLayer(layer.get(), true, new IntZoomLayer(999 + i, downfallLayer), i * 2 + 1);
            }

            i++;
        }

        if (temperatureLayer == null) {
            throw new IllegalArgumentException("No climate sample points provided!");
        }

        this.temperatureLayer = new IntZoomLayer(1001, new IntZoomLayer(1000, temperatureLayer));
        this.downfallLayer = new IntZoomLayer(1001, new IntZoomLayer(1000, downfallLayer));
        this.temperatureLayer.init(seed);
        this.downfallLayer.init(seed);

        this.distribution = settings.getOrDefault(SettingsComponentTypes.CLIMATE_DISTRIBUTION);
    }

    @Override
    public Clime sample(int x, int z) {
        //TODO: investigate if we need to cache this
        int temperature = this.temperatureLayer.sample(this.biomeRegistry, x, z);
        int downfall = this.downfallLayer.sample(this.biomeRegistry, x, z);
        return new Clime(
            Mth.clamp(temperature, 0, 65536) / 65536.0,
            Mth.clamp(downfall, 0, 65536) / 65536.0
        );
    }

    @Override
    public boolean useBiomeColor() {
        return ModernerBeta.config.getOrDefault(SettingsComponentTypes.CONFIG_BETA_FRACTAL_CLIMATIC_COLORS).vegetation();
    }

    @Override
    public boolean useSkyColor() {
        return ModernerBeta.config.getOrDefault(SettingsComponentTypes.CONFIG_BETA_FRACTAL_CLIMATIC_COLORS).sky();
    }

    @Override
    public boolean useWaterColor() {
        return ModernerBeta.config.getOrDefault(SettingsComponentTypes.CONFIG_BETA_FRACTAL_CLIMATIC_COLORS).water();
    }

    @Override
    public ClimateDistribution getDistribution() {
        return this.distribution;
    }

    @Override
    public double sampleSky(int x, int z) {
        return this.temperatureLayer.sample(this.biomeRegistry, x, z) / 65536.0;
    }

    @Override
    public double getSnowThreshold() {
        return 0.15;
    }

    @Override
    public TemperatureHeightScaling getHeightType() {
        return TemperatureHeightScaling.NONE;
    }

    @Override
    public String getDebugText(int x, int z) {
        Clime clime = this.sample(x, z);
        double temp = clime.temp();
        double rain = clime.rain();

        return String.format("Climate Temp: %.3f Rainfall: %.3f", temp, rain);
    }
}
