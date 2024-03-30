package mod.bespectacled.modernbeta.world.chunk.provider;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.api.world.chunk.ChunkProviderNoise;
import mod.bespectacled.modernbeta.api.world.spawn.SpawnLocator;
import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.world.biome.HeightConfig;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.biome.provider.fractal.BiomeInfo;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bespectacled.modernbeta.world.spawn.SpawnLocatorRelease;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.slf4j.event.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChunkProviderMajorRelease extends ChunkProviderNoise {
    private static final float[] BIOME_HEIGHT_WEIGHTS = new float[25];

    private final PerlinOctaveNoise minLimitOctaveNoise;
    private final PerlinOctaveNoise maxLimitOctaveNoise;
    private final PerlinOctaveNoise mainOctaveNoise;
    private final PerlinOctaveNoise depthOctaveNoise;
    private final PerlinOctaveNoise forestOctaveNoise;
    private final Map<BiomeInfo, HeightConfig> heightOverrideCache;

    static {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                float value = 10.0F / MathHelper.sqrt((float)(x * x + z * z) + 0.2F);
                BIOME_HEIGHT_WEIGHTS[x + 2 + (z + 2) * 5] = value;
            }
        }
    }

    public ChunkProviderMajorRelease(ModernBetaChunkGenerator chunkGenerator, long seed) {
        super(chunkGenerator, seed);

        this.minLimitOctaveNoise = new PerlinOctaveNoise(this.random, 16, true);
        this.maxLimitOctaveNoise = new PerlinOctaveNoise(this.random, 16, true);
        this.mainOctaveNoise = new PerlinOctaveNoise(this.random, 8, true);
        new PerlinOctaveNoise(this.random, 4, true);
        new PerlinOctaveNoise(this.random, 10, true);
        this.depthOctaveNoise = new PerlinOctaveNoise(this.random, 16, true);
        this.forestOctaveNoise = new PerlinOctaveNoise(this.random, 8, true);

        this.heightOverrideCache = new HashMap<>();
    }
    
    @Override
    public SpawnLocator getSpawnLocator() {
        return new SpawnLocatorRelease(this, new Random(this.seed));
    }

    @Override
    public void provideSurface(ChunkRegion region, StructureAccessor structureAccessor, Chunk chunk, ModernBetaBiomeSource biomeSource, NoiseConfig noiseConfig) {
        this.chunkGenerator.buildDefaultSurface(region, structureAccessor, noiseConfig, chunk);
    }

    @Override
    protected void sampleNoiseColumn(double[] primaryBuffer, double[] heightmapBuffer, int startNoiseX, int startNoiseZ, int localNoiseX, int localNoiseZ) {
        int noiseX = startNoiseX + localNoiseX;
        int noiseZ = startNoiseZ + localNoiseZ;
        
        double islandOffset = this.getIslandOffset(noiseX, noiseZ);
        
        double depthNoiseScaleX = this.chunkSettings.noiseDepthNoiseScaleX;
        double depthNoiseScaleZ = this.chunkSettings.noiseDepthNoiseScaleZ;
        
        double coordinateScale = this.chunkSettings.noiseCoordinateScale;
        double heightScale = this.chunkSettings.noiseHeightScale;
        
        double mainNoiseScaleX = this.chunkSettings.noiseMainNoiseScaleX;
        double mainNoiseScaleY = this.chunkSettings.noiseMainNoiseScaleY;
        double mainNoiseScaleZ = this.chunkSettings.noiseMainNoiseScaleZ;

        double lowerLimitScale = this.chunkSettings.noiseLowerLimitScale;
        double upperLimitScale = this.chunkSettings.noiseUpperLimitScale;
        
        double baseSize = this.chunkSettings.noiseBaseSize;
        double heightStretch = this.chunkSettings.noiseStretchY;
        
        double depth = this.depthOctaveNoise.sampleXZWrapped(noiseX, noiseZ, depthNoiseScaleX, depthNoiseScaleZ);

        float biomeHeightStretch = 0.0F;
        float biomeSurfaceHeight = 0.0F;
        float totalBiomeHeightWeight = 0.0F;

        BiomeInfo biome = this.getBiomeInfo(noiseX, noiseZ);
        double minSurfaceHeight = this.getHeightConfig(biome).scale();

        for (int biomeX = -2; biomeX <= 2; biomeX++) {
            for (int biomeZ = -2; biomeZ <= 2; biomeZ++) {
                biome = this.getBiomeInfo(noiseX + biomeX, noiseZ + biomeZ);
                HeightConfig heightConfig = this.getHeightConfig(biome);

                float weight = BIOME_HEIGHT_WEIGHTS[biomeX + 2 + (biomeZ + 2) * 5] / (heightConfig.scale() + 2.0F);
                if (heightConfig.scale() > minSurfaceHeight) {
                    weight /= 2.0F;
                }

                biomeHeightStretch += heightConfig.depth() * weight;
                biomeSurfaceHeight += heightConfig.scale() * weight;
                totalBiomeHeightWeight += weight;
            }
        }

        biomeHeightStretch /= totalBiomeHeightWeight;
        biomeSurfaceHeight /= totalBiomeHeightWeight;
        biomeHeightStretch = biomeHeightStretch * 0.9F + 0.1F;
        biomeSurfaceHeight = (biomeSurfaceHeight * 4.0F - 1.0F) / 8.0F;

        depth /= 8000D;

        if (depth < 0.0D) {
            depth = -depth * 0.3D;
        }

        depth = depth * 3D - 2D;

        if (depth < 0.0D) {
            depth /= 2D;

            if (depth < -1D) {
                depth = -1D;
            }

            depth /= 1.4D;
            depth /= 2D;
        } else {
            if (depth > 1.0D) {
                depth = 1.0D;
            }
            depth /= 8D;
        }

        depth = biomeSurfaceHeight + depth * 0.2D;
        depth *= baseSize / 8.0D;
        depth = baseSize + depth * 4.0D;
        
        for (int y = 0; y < primaryBuffer.length; ++y) {
            int noiseY = y + this.noiseMinY;
            
            double density;
            double heightmapDensity;
            
            double densityOffset = this.getOffset(noiseY, heightStretch, depth, biomeHeightStretch);
                       
            double mainNoise = (this.mainOctaveNoise.sampleWrapped(
                noiseX, noiseY, noiseZ,
                coordinateScale / mainNoiseScaleX, 
                heightScale / mainNoiseScaleY, 
                coordinateScale / mainNoiseScaleZ
            ) / 10D + 1.0D) / 2D;
            
            if (mainNoise < 0.0D) {
                density = this.minLimitOctaveNoise.sampleWrapped(
                    noiseX, noiseY, noiseZ,
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / lowerLimitScale;
                
            } else if (mainNoise > 1.0D) {
                density = this.maxLimitOctaveNoise.sampleWrapped(
                    noiseX, noiseY, noiseZ,
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / upperLimitScale;
                
            } else {
                double minLimitNoise = this.minLimitOctaveNoise.sampleWrapped(
                    noiseX, noiseY, noiseZ,
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / lowerLimitScale;
                
                double maxLimitNoise = this.maxLimitOctaveNoise.sampleWrapped(
                    noiseX, noiseY, noiseZ,
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / upperLimitScale;
                
                density = minLimitNoise + (maxLimitNoise - minLimitNoise) * mainNoise;
            }
            
            density -= densityOffset;
            density += islandOffset;
            
            // Sample without noise caves
            heightmapDensity = density;
            
            // Sample for noise caves
            density = this.sampleNoisePostProcessor(density, noiseX, noiseY, noiseZ);
            
            // Apply slides
            density = this.applySlides(density, y);
            heightmapDensity = this.applySlides(heightmapDensity, y);
            
            primaryBuffer[y] = density;
            heightmapBuffer[y] = heightmapDensity;
        }
    }
    
    @Override
    protected PerlinOctaveNoise getForestOctaveNoise() {
        return this.forestOctaveNoise;
    }
    
    private double getOffset(int noiseY, double heightStretch, double depth, double scale) {
        double offset = (((double)noiseY - depth) * heightStretch) / scale;
        
        if (offset < 0D)
            offset *= 4D;
        
        return offset;
    }

    private BiomeInfo getBiomeInfo(int biomeX, int biomeZ) {
        if (chunkGenerator.getBiomeSource() instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
            return modernBetaBiomeSource.getBiomeForHeightGen(biomeX, 16, biomeZ);
        } else {
            return BiomeInfo.of(this.getBiome(biomeX, 16, biomeZ, null));
        }
    }

    private HeightConfig getHeightConfig(BiomeInfo biomeInfo) {
        HeightConfig config = HeightConfig.getHeightConfig(biomeInfo);
        String id = biomeInfo.getId();
        if (this.chunkSettings.releaseHeightOverrides.containsKey(id)) {
            HeightConfig fallbackConfig = config;
            config = this.heightOverrideCache.computeIfAbsent(biomeInfo, k -> {
                String heightConfigString = this.chunkSettings.releaseHeightOverrides.get(id);
                String[] heightConfigPair = heightConfigString.split(";");
                try {
                    float scale = Float.parseFloat(heightConfigPair[0]);
                    float depth = Float.parseFloat(heightConfigPair[1]);
                    return new HeightConfig(scale, depth);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
                    ModernBeta.log(Level.WARN, String.format("Invalid height config \"%s\"", heightConfigString));
                    return fallbackConfig;
                }
            });
        }

        return config;
    }
}
