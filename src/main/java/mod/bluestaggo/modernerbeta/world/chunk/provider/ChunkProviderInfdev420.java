package mod.bluestaggo.modernerbeta.world.chunk.provider;

import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProviderNoise;
import mod.bluestaggo.modernerbeta.api.world.chunk.surface.SurfaceBlocks;
import mod.bluestaggo.modernerbeta.api.world.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.api.world.spawn.SpawnLocator;
import mod.bluestaggo.modernerbeta.util.BlockStates;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkHeightmap;
import mod.bluestaggo.modernerbeta.util.noise.PerlinOctaveNoise;
import mod.bluestaggo.modernerbeta.util.noise.SimpleNoisePos;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bluestaggo.modernerbeta.world.spawn.SpawnLocatorBeta;
import net.minecraft.block.BlockState;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.util.Random;

public class ChunkProviderInfdev420 extends ChunkProviderNoise {
    private final PerlinOctaveNoise minLimitOctaveNoise;
    private final PerlinOctaveNoise maxLimitOctaveNoise;
    private final PerlinOctaveNoise mainOctaveNoise;
    private final PerlinOctaveNoise beachOctaveNoise;
    private final PerlinOctaveNoise surfaceOctaveNoise;
    private final PerlinOctaveNoise forestOctaveNoise;
    
    public ChunkProviderInfdev420(ModernBetaChunkGenerator chunkGenerator, long seed) {
        super(chunkGenerator, seed);
        
        this.minLimitOctaveNoise = new PerlinOctaveNoise(this.random, 16, true);
        this.maxLimitOctaveNoise = new PerlinOctaveNoise(this.random, 16, true);
        this.mainOctaveNoise = new PerlinOctaveNoise(this.random, 8, true);
        this.beachOctaveNoise = new PerlinOctaveNoise(this.random, 4, true);
        this.surfaceOctaveNoise = new PerlinOctaveNoise(this.random, 4, true);
        new PerlinOctaveNoise(this.random, 5, true); // Unused in original source
        this.forestOctaveNoise = new PerlinOctaveNoise(this.random, 5, true);
    }
    
    @Override
    public SpawnLocator getSpawnLocator() {
        return new SpawnLocatorBeta(this, this.beachOctaveNoise, new Random(this.seed));
    }

    @Override
    public void provideSurface(ChunkRegion region, StructureAccessor structureAccessor, Chunk chunk, ModernBetaBiomeSource biomeSource, NoiseConfig noiseConfig) {
        double scale = 0.03125D;

        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;
        
        int startX = chunk.getPos().getStartX();
        int startZ = chunk.getPos().getStartZ();
        
        Random rand = this.createSurfaceRandom(chunkX, chunkZ);
        Random bedrockRand = this.createSurfaceRandom(chunkX, chunkZ);
        BlockPos.Mutable pos = new BlockPos.Mutable();

        AquiferSampler aquiferSampler = this.getAquiferSampler(chunk, noiseConfig);
        ChunkHeightmap heightmapChunk = this.getChunkHeightmap(chunkX, chunkZ);
        SimpleNoisePos noisePos = new SimpleNoisePos();

        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {
                int x = startX + localX;
                int z = startZ + localZ;
                int surfaceTopY = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG).get(localX, localZ) - 1;
                int surfaceMinY = (this.hasNoisePostProcessor()) ? 
                    heightmapChunk.getHeight(x, z, ChunkHeightmap.Type.SURFACE_FLOOR) - 8 : 
                    this.worldMinY;
                
                boolean genSandBeach = this.beachOctaveNoise.sample(
                    x * scale,
                    z * scale,
                    0.0
                ) + rand.nextDouble() * 0.2 > 0.0;
                
                boolean genGravelBeach = this.beachOctaveNoise.sample(
                    z * scale,
                    109.0134,
                    x * scale
                ) + rand.nextDouble() * 0.2 > 3.0;
                
                int surfaceDepth = (int)(this.surfaceOctaveNoise.sampleXY(
                    x * scale * 2.0,
                    z * scale * 2.0
                ) / 3.0 + 3.0 + rand.nextDouble() * 0.25);

                int runDepth = -1;
                
                RegistryEntry<Biome> biome = biomeSource.getBiomeForSurfaceGen(region, pos.set(x, surfaceTopY, z));
                
                SurfaceConfig surfaceConfig = this.surfaceBuilder.getSurfaceConfig(biome);
                BlockState topBlock = surfaceConfig.normal().topBlock();
                BlockState fillerBlock = surfaceConfig.normal().fillerBlock();
                
                for (int y = this.worldTopY - 1; y >= this.worldMinY; --y) {
                    BlockState blockState;
                    
                    pos.set(localX, y, localZ);
                    blockState = chunk.getBlockState(pos);

                    // Place bedrock
                    if (y <= this.bedrockFloor + bedrockRand.nextInt(5)) {
                        VersionCompat.setBlockState(chunk, pos, BlockStates.BEDROCK);
                        continue;
                    }
                    
                    // Skip if at surface min y
                    if (y < surfaceMinY) {
                        continue;
                    }
                    
                    if (blockState.isAir()) {
                        runDepth = -1;
                        
                    } else if (blockState.isOf(this.defaultBlock.getBlock())) {
                        if (runDepth == -1) {
                            if (surfaceDepth <= 0) {
                                topBlock = BlockStates.AIR;
                                fillerBlock = this.defaultBlock;
                                
                            } else if (y >= this.seaLevel - 4 && y <= this.seaLevel + 1) {
                                topBlock = surfaceConfig.normal().topBlock();
                                fillerBlock = surfaceConfig.normal().fillerBlock();
                                
                                if (genGravelBeach) {
                                    topBlock = surfaceConfig.beachGravel().topBlock();
                                    fillerBlock = surfaceConfig.beachGravel().fillerBlock();
                                }
                                
                                if (genSandBeach) {
                                    topBlock = surfaceConfig.beachSand().topBlock();
                                    fillerBlock = surfaceConfig.beachSand().fillerBlock();
                                }
                            }
                            
                            runDepth = surfaceDepth;
                            
                            if (y < this.seaLevel && topBlock.isAir()) { // Generate water bodies
                                BlockState fluidBlock = aquiferSampler.apply(noisePos.set(x, y, z), 0.0);
                                
                                boolean isAir = fluidBlock == null;
                                topBlock = isAir ? BlockStates.AIR : fluidBlock;
                                
                                this.scheduleFluidTick(chunk, aquiferSampler, pos, topBlock);
                            }
                            
                            blockState = (y >= this.seaLevel - 1 || (y < this.seaLevel - 1 && chunk.getBlockState(pos.up()).isAir())) ? 
                                topBlock : 
                                fillerBlock;
                            
                            VersionCompat.setBlockState(chunk, pos, blockState);
                            
                        } else if (runDepth > 0) {
                            --runDepth;
                            VersionCompat.setBlockState(chunk, pos, fillerBlock);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void provideSurfaceExtra(ChunkRegion region, StructureAccessor structureAccessor, Chunk chunk, ModernBetaBiomeSource biomeSource, NoiseConfig noiseConfig) {
        double scale = 0.03125;

        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;

        int startX = chunk.getPos().getStartX();
        int startZ = chunk.getPos().getStartZ();

        Random rand = this.createSurfaceRandom(chunkX, chunkZ);
        BlockPos.Mutable pos = new BlockPos.Mutable();

        ChunkHeightmap heightmapChunk = this.hasNoisePostProcessor() ? this.getChunkHeightmap(chunkX, chunkZ) : null;

        for (int localZ = 0; localZ < 16; localZ++) {
            for (int localX = 0; localX < 16; localX++) {
                pos.set(localX, 0, localZ);

                int x = startX + localX;
                int z = startZ + localZ;
                int surfaceTopY = heightmapChunk != null ?
                    heightmapChunk.getHeight(x, z, ChunkHeightmap.Type.SURFACE_FLOOR) :
                    chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG).get(localX, localZ);
                surfaceTopY--;

                boolean genSandBeach = this.beachOctaveNoise.sample(
                    x * scale,
                    z * scale,
                    0.0
                ) + rand.nextDouble() * 0.2 > 0.0;

                boolean genGravelBeach = this.beachOctaveNoise.sample(
                    z * scale,
                    109.0134,
                    x * scale
                ) + rand.nextDouble() * 0.2 > 3.0;

                int surfaceDepth = (int)(this.surfaceOctaveNoise.sampleXY(
                    x * scale * 2.0,
                    z * scale * 2.0
                ) / 3.0 + 3.0 + rand.nextDouble() * 0.25);

                RegistryEntry<Biome> biome = biomeSource.getBiomeForSurfaceGen(region, pos.set(x, surfaceTopY, z));

                SurfaceConfig surfaceConfig = this.surfaceBuilder.getSurfaceConfig(biome);

                int y = surfaceTopY;
                pos.setY(y);

                if (!this.isBlockSuitableForSurface(chunk.getBlockState(pos))) {
                    continue;
                }

                if (surfaceDepth <= 0) {
                    VersionCompat.setBlockState(chunk, pos, y < this.seaLevel ? this.defaultFluid : BlockStates.AIR);
                    pos.setY(--y);

                    while (this.isBlockSuitableForSurface(chunk.getBlockState(pos))) {
                        VersionCompat.setBlockState(chunk, pos, this.defaultBlock);
                        pos.setY(--y);
                    }
                } else if (surfaceTopY >= this.seaLevel - 4 && surfaceTopY < this.seaLevel + 1) {
                    SurfaceBlocks beach = genSandBeach ? surfaceConfig.beachSand() : genGravelBeach ? surfaceConfig.beachGravel() : null;
                    if (beach != null) {
                        if (beach.topBlock().isAir() && y < this.seaLevel) {
                            VersionCompat.setBlockState(chunk, pos, this.defaultFluid);
                        } else {
                            VersionCompat.setBlockState(chunk, pos, beach.topBlock());
                        }
                        pos.setY(--y);

                        while (this.isBlockSuitableForSurface(chunk.getBlockState(pos))) {
                            VersionCompat.setBlockState(chunk, pos, beach.fillerBlock());
                            pos.setY(--y);
                        }
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

        for (int y = 0; y < primaryBuffer.length; ++y) {
            int noiseY = y + this.noiseMinY;
            
            double density;
            double heightmapDensity;
            
            double densityOffset = this.getOffset(noiseY, baseSize, heightStretch);
            
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
    
    private double getOffset(int noiseY, double baseSize, double heightStretch) {
        double offset = ((double) noiseY - baseSize) * heightStretch;
        
        if (offset < 0.0)
            offset *= 2.0;
        
        return offset;
    }

}
