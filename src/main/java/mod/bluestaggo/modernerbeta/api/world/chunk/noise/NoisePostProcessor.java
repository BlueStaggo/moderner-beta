package mod.bluestaggo.modernerbeta.api.world.chunk.noise;

import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.util.noise.SimpleNoisePos;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.noise.NoiseConfig;

public interface NoisePostProcessor {
    NoisePostProcessor NOISE_CAVES = (noise, noiseX, noiseY, noiseZ, noiseConfig, generatorSettings, chunkSettings) -> {
        int hBlock = generatorSettings.generationShapeConfig().horizontalCellBlockCount();
        int vBlock = generatorSettings.generationShapeConfig().verticalCellBlockCount();
        double cave = noiseConfig.getNoiseRouter().finalDensity().sample(new SimpleNoisePos(noiseX * hBlock, noiseY * vBlock, noiseZ * hBlock)) * 2048.0;
        return Math.min(cave, noise);
    };

    double sample(double noise, int noiseX, int noiseY, int noiseZ, NoiseConfig noiseConfig, ChunkGeneratorSettings generatorSettings, ModernBetaSettings chunkSettings);
}
