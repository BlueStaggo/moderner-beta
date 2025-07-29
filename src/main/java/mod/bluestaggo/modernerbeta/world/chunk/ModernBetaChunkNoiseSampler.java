package mod.bluestaggo.modernerbeta.world.chunk;

import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProviderNoise;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkHeightmap;
import mod.bluestaggo.modernerbeta.util.noise.SimpleDensityFunction;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevelSampler;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.noise.NoiseConfig;

public class ModernBetaChunkNoiseSampler {
    private static final int HEIGHT_OFFSET = -8;
    
    private final ChunkProvider chunkProvider;
    
    public static ChunkNoiseSampler create(
        Chunk chunk,
        NoiseConfig noiseConfig,
        ChunkGeneratorSettings chunkGeneratorSettings,
        AquiferSampler.FluidLevelSampler fluidLevelSampler,
        ChunkProvider chunkProvider
    ) {
        GenerationShapeConfig shapeConfig = chunkGeneratorSettings.generationShapeConfig().trimHeight(chunk);
        ChunkPos chunkPos = chunk.getPos();
        
        int horizontalSize = 16 / shapeConfig.horizontalCellBlockCount();
        
        return new ModernBetaChunkNoiseSampler(chunkProvider).createSampler(
            horizontalSize,
            noiseConfig,
            chunkPos.getStartX(),
            chunkPos.getStartZ(),
            shapeConfig,
            SimpleDensityFunction.INSTANCE,
            chunkGeneratorSettings,
            fluidLevelSampler,
            Blender.getNoBlending()
        );
    }

    private ModernBetaChunkNoiseSampler(ChunkProvider chunkProvider) {
        this.chunkProvider = chunkProvider;
    }

    private SamplerImpl createSampler(
        int horizontalSize,
        NoiseConfig noiseConfig,
        int startX,
        int startZ,
        GenerationShapeConfig shapeConfig,
        DensityFunctionTypes.Beardifying beardifying,
        ChunkGeneratorSettings settings,
        FluidLevelSampler fluidLevelSampler,
        Blender blender
    ) {
        return new SamplerImpl(
            horizontalSize,
            noiseConfig,
            startX,
            startZ,
            shapeConfig,
            beardifying,
            settings,
            fluidLevelSampler,
            blender
        );
    }

    private class SamplerImpl extends ChunkNoiseSampler {
        private SamplerImpl(
            int horizontalSize,
            NoiseConfig noiseConfig,
            int startX,
            int startZ,
            GenerationShapeConfig shapeConfig,
            DensityFunctionTypes.Beardifying beardifying,
            ChunkGeneratorSettings settings,
            FluidLevelSampler fluidLevelSampler,
            Blender blender
        ) {
            super(
                horizontalSize,
                noiseConfig,
                startX,
                startZ,
                shapeConfig,
                beardifying,
                settings,
                fluidLevelSampler,
                blender
            );
        }


        /*
         * Simulates a general y height at x/z block coordinates.
         * Replace vanilla noise implementation with plain height sampling.
         *
         * Used to determine whether an aquifer should use sea level or local water level.
         * Also used in SurfaceBuilder to determine min surface y.
         *
         * Reference: https://twitter.com/henrikkniberg/status/1432615996880310274
         *
         */
        @Override
        public int estimateSurfaceHeight(int x, int z) {
            int height = (chunkProvider instanceof ChunkProviderNoise noiseChunkProvider) ?
                    noiseChunkProvider.getHeight(x, z, ChunkHeightmap.Type.SURFACE_FLOOR) :
                    chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
            return height + HEIGHT_OFFSET;
        }
    }
}
