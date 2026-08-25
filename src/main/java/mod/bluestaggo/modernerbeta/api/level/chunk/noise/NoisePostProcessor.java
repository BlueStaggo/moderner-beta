package mod.bluestaggo.modernerbeta.api.level.chunk.noise;

import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
//? if <26.3
import mod.bluestaggo.modernerbeta.util.noise.SimpleNoisePos;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.NoiseSettings;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.RandomState;

public interface NoisePostProcessor {
    NoisePostProcessor NOISE_CAVES = (noise, noiseX, noiseY, noiseZ, noiseConfig, generatorSettings, chunkSettings) -> {
        NoiseSettings settings = chunkSettings.get(SettingsComponentTypes.NOISE_SETTINGS);
        settings = settings != null ? settings : NoiseSettings.fromVanilla(generatorSettings.noiseSettings());

        int hBlock = settings.getCellWidth();
        int vBlock = settings.getCellHeight();

        int x = noiseX * hBlock;
        int y = noiseY * vBlock;
        int z = noiseZ * hBlock;
        //? if >=26.3 {
        /*net.minecraft.world.level.levelgen.densityfunction.SamplerContext context =
                net.minecraft.world.level.levelgen.densityfunction.SamplerContext.builder().enableCaches().build();
        *///? }

        //~ if >=26.3 'noiseConfig.router().finalDensity().compute(new SimpleNoisePos(x, y, z)' -> 'noiseConfig.getSampler(generatorSettings.noiseRouter().finalDensity()).sampleValue(context, x, y, z'
        double cave = noiseConfig.router().finalDensity().compute(new SimpleNoisePos(x, y, z)) * 2048.0;

        return Math.min(cave, noise);
    };

    double sample(double noise, int noiseX, int noiseY, int noiseZ, RandomState noiseConfig, NoiseGeneratorSettings generatorSettings, ModernBetaSettings chunkSettings);
}
