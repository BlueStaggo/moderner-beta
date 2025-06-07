package mod.bluestaggo.modernerbeta.world.biome.provider;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.TemperatureHeightScaling;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSamplerSky;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.Clime;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.ClimateDistribution;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.intlayers.IntClimateSampleLayer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.intlayers.IntLayer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.intlayers.IntZoomLayer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;

import java.util.Optional;

public class BiomeProviderBetaFractal extends BiomeProviderFractal implements ClimateSampler, ClimateSamplerSky {
    private final IntLayer temperatureLayer;
    private final IntLayer downfallLayer;
    private final ClimateDistribution distribution;

    public BiomeProviderBetaFractal(ModernBetaSettings settings, RegistryEntryLookup<Biome> biomeRegistry, long seed) {
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
        int temperature = this.temperatureLayer.sample(this.biomeRegistry, x, z);
        int downfall = this.downfallLayer.sample(this.biomeRegistry, x, z);
        return new Clime(
            MathHelper.clamp(temperature, 0, 65536) / 65536.0,
            MathHelper.clamp(downfall, 0, 65536) / 65536.0
        );
    }

    @Override
    public boolean useBiomeColor() {
        return ModernerBeta.CONFIG.useBetaFractalBiomeColor;
    }

    @Override
    public boolean useSkyColor() {
        return ModernerBeta.CONFIG.useBetaFractalSkyColor;
    }

    @Override
    public boolean useWaterColor() {
        return ModernerBeta.CONFIG.useBetaFractalWaterColor;
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
}
