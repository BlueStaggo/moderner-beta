package mod.bluestaggo.modernerbeta.api.world.chunk.noise;

import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsChunk;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public interface NoisePostProcessor {
    public static final NoisePostProcessor DEFAULT = (noise, noiseX, noiseY, noiseZ, generatorSettings, chunkSettings) -> noise;
    
    double sample(double noise, int noiseX, int noiseY, int noiseZ, ChunkGeneratorSettings generatorSettings, ModernBetaSettingsChunk chunkSettings);
}
