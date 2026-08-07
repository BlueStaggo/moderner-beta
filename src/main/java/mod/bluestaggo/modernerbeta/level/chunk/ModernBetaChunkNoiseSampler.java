package mod.bluestaggo.modernerbeta.level.chunk;

import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProviderNoise;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkHeightmap;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.Aquifer.FluidPicker;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

public class ModernBetaChunkNoiseSampler {
    private static final int HEIGHT_OFFSET = -8;
    
    private final ChunkProvider chunkProvider;
    
    public static NoiseChunk create(
        ChunkAccess chunk,
        RandomState noiseConfig,
        DensityFunctions.BeardifierOrMarker beardifier,
        NoiseGeneratorSettings chunkGeneratorSettings,
        Aquifer.FluidPicker fluidLevelSampler,
        ChunkProvider chunkProvider
    ) {
        NoiseSettings shapeConfig = chunkGeneratorSettings.noiseSettings().clampToHeightAccessor(chunk);
        ChunkPos chunkPos = chunk.getPos();
        
        int horizontalSize = 16 / shapeConfig.getCellWidth();
        
        return new ModernBetaChunkNoiseSampler(chunkProvider).createSampler(
            horizontalSize,
            noiseConfig,
            chunkPos.getMinBlockX(),
            chunkPos.getMinBlockZ(),
            shapeConfig,
            beardifier,
            chunkGeneratorSettings,
            fluidLevelSampler,
            Blender.empty()
        );
    }

    private ModernBetaChunkNoiseSampler(ChunkProvider chunkProvider) {
        this.chunkProvider = chunkProvider;
    }

    private SamplerImpl createSampler(
        int horizontalSize,
        RandomState noiseConfig,
        int startX,
        int startZ,
        NoiseSettings shapeConfig,
        DensityFunctions.BeardifierOrMarker beardifying,
        NoiseGeneratorSettings settings,
        FluidPicker fluidLevelSampler,
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

    private class SamplerImpl extends NoiseChunk {
        private SamplerImpl(
            int horizontalSize,
            RandomState noiseConfig,
            int startX,
            int startZ,
            NoiseSettings shapeConfig,
            DensityFunctions.BeardifierOrMarker beardifying,
            NoiseGeneratorSettings settings,
            FluidPicker fluidLevelSampler,
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
        //~ if >=26.3 'preliminarySurfaceLevel' -> 'computePreliminarySurfaceLevel'
        public int preliminarySurfaceLevel(int x, int z) {
            int height = (chunkProvider instanceof ChunkProviderNoise noiseChunkProvider) ?
                    noiseChunkProvider.getHeight(null, x, z, ChunkHeightmap.Type.SURFACE_FLOOR) :
                    chunkProvider.getHeight(null, x, z, Heightmap.Types.OCEAN_FLOOR_WG);
            return height + HEIGHT_OFFSET;
        }
    }
}
