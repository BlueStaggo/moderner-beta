package mod.bluestaggo.modernerbeta.api.world.chunk.noise;

import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.util.noise.SimpleNoisePos;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.RandomState;

public interface NoisePostProcessor {
    NoisePostProcessor NOISE_CAVES = (noise, noiseX, noiseY, noiseZ, noiseConfig, generatorSettings, chunkSettings) -> {
        int hBlock = generatorSettings.noiseSettings().getCellWidth();
        int vBlock = generatorSettings.noiseSettings().getCellHeight();
        double cave = noiseConfig.router().finalDensity().compute(new SimpleNoisePos(noiseX * hBlock, noiseY * vBlock, noiseZ * hBlock)) * 2048.0;
        return Math.min(cave, noise);
    };

    double sample(double noise, int noiseX, int noiseY, int noiseZ, RandomState noiseConfig, NoiseGeneratorSettings generatorSettings, ModernBetaSettings chunkSettings);
}
