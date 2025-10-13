package mod.bluestaggo.modernerbeta.world.chunk.provider;

import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProviderForcedHeight;
import mod.bluestaggo.modernerbeta.api.world.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.api.world.spawn.SpawnLocator;
import mod.bluestaggo.modernerbeta.util.BlockStates;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkHeightmap;
import mod.bluestaggo.modernerbeta.util.noise.PerlinOctaveNoise;
import mod.bluestaggo.modernerbeta.util.noise.SimpleNoisePos;
import mod.bluestaggo.modernerbeta.util.noise.SimplexOctaveNoise;
import mod.bluestaggo.modernerbeta.world.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bluestaggo.modernerbeta.world.spawn.SpawnLocatorRelease;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;

import java.util.Random;

public class ChunkProviderMajorRelease extends ChunkProviderForcedHeight {
    private final PerlinOctaveNoise minLimitOctaveNoise;
    private final PerlinOctaveNoise maxLimitOctaveNoise;
    private final PerlinOctaveNoise mainOctaveNoise;
    private final SimplexOctaveNoise surfaceOctaveNoise;
    private final PerlinOctaveNoise depthOctaveNoise;
    private final PerlinOctaveNoise forestOctaveNoise;

    public ChunkProviderMajorRelease(ModernBetaChunkGenerator chunkGenerator, long seed) {
        super(chunkGenerator, seed);

        Random random = this.getRandom(seed);
        this.minLimitOctaveNoise = new PerlinOctaveNoise(random, 16, true);
        this.maxLimitOctaveNoise = new PerlinOctaveNoise(random, 16, true);
        this.mainOctaveNoise = new PerlinOctaveNoise(random, 8, true);
        this.surfaceOctaveNoise = new SimplexOctaveNoise(random, 4);
        new PerlinOctaveNoise(random, 10, true);
        this.depthOctaveNoise = new PerlinOctaveNoise(random, 16, true);
        this.forestOctaveNoise = new PerlinOctaveNoise(random, 8, true);
    }
    
    @Override
    public SpawnLocator getSpawnLocator() {
        return new SpawnLocatorRelease(this, new Random(this.seed));
    }

    @Override
    public void provideSurface(WorldGenRegion region, StructureManager structureAccessor, ChunkAccess chunk, ModernBetaBiomeSource biomeSource, RandomState noiseConfig) {
        double scale = 0.03125;

        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;

        int startX = chunk.getPos().getMinBlockX();
        int startZ = chunk.getPos().getMinBlockZ();

        Random rand = this.createSurfaceRandom(chunkX, chunkZ);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        Aquifer aquiferSampler = this.getAquiferSampler(chunk, noiseConfig);
        ChunkHeightmap heightmapChunk = this.getChunkHeightmap(region, chunkX, chunkZ);
        SimpleNoisePos noisePos = new SimpleNoisePos();

        for (int localZ = 0; localZ < 16; localZ++) {
            for (int localX = 0; localX < 16; localX++) {
                int x = startX + localX;
                int z = startZ + localZ;
                int surfaceTopY = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG).getFirstAvailable(localX, localZ) - 1;
                int surfaceMinY = (this.hasNoisePostProcessor()) ?
                    heightmapChunk.getHeight(x, z, ChunkHeightmap.Type.SURFACE_FLOOR) - 8 :
                    this.worldMinY;

                int surfaceDepth = (int) (surfaceOctaveNoise.sample(x, z, scale * 2.0D, 1.0D) / 3D + 3D + rand.nextDouble() * 0.25D);

                int runDepth = -1;

                Holder<Biome> biome = biomeSource.getBiomeForSurfaceGen(region, pos.set(x, surfaceTopY, z));

                SurfaceConfig surfaceConfig = this.surfaceBuilder.getSurfaceConfig(biome);
                BlockState topBlock = surfaceConfig.normal().topBlock();
                BlockState fillerBlock = surfaceConfig.normal().fillerBlock();

                // Generate from top to bottom of world
                for (int y = this.worldTopY - 1; y >= this.worldMinY; y--) {
                    BlockState blockState;

                    pos.set(localX, y, localZ);
                    blockState = chunk.getBlockState(pos);

                    // Place bedrock
                    if (y <= this.bedrockFloor + rand.nextInt(5)) {
                        VersionCompat.setBlockState(chunk, pos, BlockStates.BEDROCK);
                        continue;
                    }

                    // Skip if at surface min y
                    if (y < surfaceMinY) {
                        continue;
                    }

                    if (blockState.isAir()) { // Skip if air block
                        runDepth = -1;
                        continue;
                    }

                    if (!blockState.is(this.defaultBlock.getBlock())) { // Skip if not stone
                        continue;
                    }

                    // At the first default block
                    if (runDepth == -1) {
                        if (surfaceDepth <= 0) { // Generate stone basin if noise permits
                            topBlock = BlockStates.AIR;
                            fillerBlock = this.defaultBlock;
                        } else if (y >= this.seaLevel - 4 && y <= this.seaLevel + 1) { // Generate beaches at this y range
                            topBlock = surfaceConfig.normal().topBlock();
                            fillerBlock = surfaceConfig.normal().fillerBlock();
                        }

                        runDepth = surfaceDepth;

                        if (y < this.seaLevel && topBlock.isAir()) { // Generate water bodies
                            BlockState fluidBlock = aquiferSampler.computeSubstance(noisePos.set(x, y, z), 0.0);

                            boolean isAir = fluidBlock == null;
                            topBlock = isAir ? BlockStates.AIR : fluidBlock;

                            this.scheduleFluidTick(chunk, aquiferSampler, pos, topBlock);
                        }

                        if (y >= this.seaLevel - 1 || (y < this.seaLevel - 1 && chunk.getBlockState(pos.above()).isAir())) {
                            blockState = topBlock;
                        } else if (y < this.seaLevel - 7 - surfaceDepth) {
                            topBlock = BlockStates.AIR;
                            fillerBlock = BlockStates.STONE;
                            blockState = BlockStates.GRAVEL;
                        } else {
                            blockState = fillerBlock;
                        }

                        VersionCompat.setBlockState(chunk, pos, blockState);

                        continue;
                    }

                    if (runDepth <= 0) {
                        continue;
                    }

                    runDepth--;
                    VersionCompat.setBlockState(chunk, pos, fillerBlock);

                    // Generates layer of sandstone starting at lowest block of sand, of height 1 to 4.
                    if (runDepth == 0 && fillerBlock.is(Blocks.SAND)) {
                        runDepth = rand.nextInt(4);
                        fillerBlock = BlockStates.SANDSTONE;
                    }

                    if (runDepth == 0 && fillerBlock.is(Blocks.RED_SAND)) {
                        runDepth = rand.nextInt(4);
                        fillerBlock = BlockStates.RED_SANDSTONE;
                    }
                }
            }
        }
    }

    @Override
    protected void sampleNoiseColumn(double[] primaryBuffer, double[] heightmapBuffer, int startNoiseX, int startNoiseZ, int localNoiseX, int localNoiseZ) {
        int noiseX = startNoiseX + localNoiseX;
        int noiseZ = startNoiseZ + localNoiseZ;
        
        double islandOffset = this.getIslandOffset(noiseX, noiseZ);

        double depthNoiseScaleX = this.noiseScale.depthNoiseX();
        double depthNoiseScaleZ = this.noiseScale.depthNoiseZ();

        double coordinateScale = this.noiseScale.coordinate();
        double heightScale = this.noiseScale.height();

        double mainNoiseScaleX = this.noiseScale.mainNoiseX();
        double mainNoiseScaleY = this.noiseScale.mainNoiseY();
        double mainNoiseScaleZ = this.noiseScale.mainNoiseZ();

        double lowerLimitScale = this.noiseScale.lowerLimit();
        double upperLimitScale = this.noiseScale.upperLimit();

        double baseSize = this.noiseScale.baseSize();
        double heightStretch = this.noiseScale.stretchY();

        boolean wrapped = !this.noiseScale.farlands();

        double depth = this.depthOctaveNoise.sampleXZ(noiseX, noiseZ, depthNoiseScaleX, depthNoiseScaleZ, wrapped);

        HeightConfig heightConfig = this.getHeightConfigAt(noiseX, noiseZ);

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

        depth = heightConfig.depth() + depth * 0.2D;
        depth *= baseSize / 8.0D;
        depth = baseSize + depth * 4.0D;
        
        for (int y = 0; y < primaryBuffer.length; ++y) {
            int noiseY = y + this.noiseMinY;
            
            double density;
            double heightmapDensity;
            
            double densityOffset = this.getOffset(noiseY, heightStretch, depth, heightConfig.scale());
                       
            double mainNoise = (this.mainOctaveNoise.sample(
                noiseX, noiseY, noiseZ,
                coordinateScale / mainNoiseScaleX, 
                heightScale / mainNoiseScaleY, 
                coordinateScale / mainNoiseScaleZ,
                wrapped
            ) / 10D + 1.0D) / 2D;
            
            if (mainNoise < 0.0D) {
                density = this.minLimitOctaveNoise.sample(
                    noiseX, noiseY, noiseZ,
                    coordinateScale, 
                    heightScale, 
                    coordinateScale,
                    wrapped
                ) / lowerLimitScale;
                
            } else if (mainNoise > 1.0D) {
                density = this.maxLimitOctaveNoise.sample(
                    noiseX, noiseY, noiseZ,
                    coordinateScale, 
                    heightScale, 
                    coordinateScale,
                    wrapped
                ) / upperLimitScale;
                
            } else {
                double minLimitNoise = this.minLimitOctaveNoise.sample(
                    noiseX, noiseY, noiseZ,
                    coordinateScale, 
                    heightScale, 
                    coordinateScale,
                    wrapped
                ) / lowerLimitScale;
                
                double maxLimitNoise = this.maxLimitOctaveNoise.sample(
                    noiseX, noiseY, noiseZ,
                    coordinateScale, 
                    heightScale, 
                    coordinateScale,
                    wrapped
                ) / upperLimitScale;
                
                density = minLimitNoise + (maxLimitNoise - minLimitNoise) * mainNoise;
            }
            
            density -= densityOffset;
            density += islandOffset;
            
            // Sample without post-processing
            heightmapDensity = density;
            
            // Sample with post-processing
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

    protected Random getRandom(long seed) {
        return this.random;
    }
    
    private double getOffset(int noiseY, double heightStretch, double depth, double scale) {
        double offset = (((double)noiseY - depth) * heightStretch) / scale;
        
        if (offset < 0D)
            offset *= 4D;
        
        return offset;
    }
}
