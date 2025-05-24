package mod.bluestaggo.modernerbeta.api.world.chunk;

import mod.bluestaggo.modernerbeta.world.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.util.math.MathHelper;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class ChunkProviderForcedHeight extends ChunkProviderNoise {
    private static final float[] BIOME_HEIGHT_WEIGHTS = new float[25];

    private final Map<ExtendedBiomeId, HeightConfig> biomeHeightValues;

    static {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                float value = 10.0F / MathHelper.sqrt((float)(x * x + z * z) + 0.2F);
                BIOME_HEIGHT_WEIGHTS[x + 2 + (z + 2) * 5] = value;
            }
        }
    }

    public ChunkProviderForcedHeight(ModernBetaChunkGenerator chunkGenerator, long seed) {
        super(chunkGenerator, seed);
        this.biomeHeightValues = this.chunkSettings.releaseBiomeHeightValues.entrySet().stream()
            .map(entry -> Map.entry(ExtendedBiomeId.of(entry.getKey()), HeightConfig.parse(entry.getValue(), HeightConfig.DEFAULT)))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public ExtendedBiomeId getExtendedBiomeId(int biomeX, int biomeZ) {
        if (this.chunkGenerator.getBiomeSource() instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
            return modernBetaBiomeSource.getBiomeForHeightGen(biomeX, 16, biomeZ);
        } else {
            return ExtendedBiomeId.of(this.getBiome(biomeX, 16, biomeZ, null).getKey().orElseThrow().getValue());
        }
    }

    public HeightConfig getHeightConfigOfBiome(ExtendedBiomeId extendedBiomeId) {
        return this.biomeHeightValues.getOrDefault(extendedBiomeId, HeightConfig.DEFAULT);
    }

    public HeightConfig getRawHeightConfigAt(int x, int z) {
        return this.getHeightConfigOfBiome(this.getExtendedBiomeId(x, z));
    }

    public HeightConfig getHeightConfigAt(int noiseX, int noiseZ) {
        float scale = 0.0F;
        float depth = 0.0F;
        float totalWeight = 0.0F;

        ExtendedBiomeId biome = this.getExtendedBiomeId(noiseX, noiseZ);
        double minSurfaceHeight = this.getHeightConfigOfBiome(biome).depth();

        for (int biomeX = -2; biomeX <= 2; biomeX++) {
            for (int biomeZ = -2; biomeZ <= 2; biomeZ++) {
                biome = this.getExtendedBiomeId(noiseX + biomeX, noiseZ + biomeZ);
                HeightConfig heightConfig = this.getHeightConfigOfBiome(biome);

                float thisScale = this.chunkSettings.releaseBiomeScaleOffset + heightConfig.scale() * this.chunkSettings.releaseBiomeScaleWeight;
                float thisDepth = this.chunkSettings.releaseBiomeDepthOffset + heightConfig.depth() * this.chunkSettings.releaseBiomeDepthWeight;

                float weight = BIOME_HEIGHT_WEIGHTS[biomeX + 2 + (biomeZ + 2) * 5] / Math.max(thisDepth + 2.0F, 0.01F);
                if (heightConfig.depth() > minSurfaceHeight) {
                    weight /= 2.0F;
                }

                scale += thisScale * weight;
                depth += thisDepth * weight;
                totalWeight += weight;
            }
        }

        scale /= totalWeight;
        depth /= totalWeight;
        scale = scale * 0.9F + 0.1F;
        depth = (depth * 4.0F - 1.0F) / 8.0F;

        return new HeightConfig(depth, scale);
    }
}
