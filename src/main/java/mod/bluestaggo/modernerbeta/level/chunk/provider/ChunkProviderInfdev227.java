package mod.bluestaggo.modernerbeta.level.chunk.provider;

import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProviderNoiseImitable;
import mod.bluestaggo.modernerbeta.api.level.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.Infdev227Structures;
import mod.bluestaggo.modernerbeta.util.BlockStates;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkCache;
import mod.bluestaggo.modernerbeta.util.noise.PerlinOctaveNoise;
import mod.bluestaggo.modernerbeta.util.noise.SimpleNoisePos;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.blocksource.BlockSourceRules;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaChunkGenerator;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class ChunkProviderInfdev227 extends ChunkProvider implements ChunkProviderNoiseImitable {
    private final int worldMinY;
    private final int worldHeight;
    private final int worldTopY;
    private final int seaLevel;
    
    private final int bedrockFloor;
    
    private final BlockState defaultBlock;
    private final BlockState defaultFluid;
    
    private final boolean infdevUsePyramid;
    private final boolean infdevUseWall;

    private final PerlinOctaveNoise octaveNoiseA;
    private final PerlinOctaveNoise octaveNoiseB;
    private final PerlinOctaveNoise octaveNoiseC;
    private final PerlinOctaveNoise octaveNoiseD;
    private final PerlinOctaveNoise octaveNoiseE;
    private final PerlinOctaveNoise octaveNoiseF;
    private final PerlinOctaveNoise forestOctaveNoise;
    
    private final ChunkCache<int[]> chunkCacheHeightmap;

    public ChunkProviderInfdev227(ModernBetaChunkGenerator chunkGenerator, long seed) {
        super(chunkGenerator, seed);
        
        NoiseGeneratorSettings generatorSettings = this.chunkGenerator.generatorSettings().value();
        NoiseSettings shapeConfig = generatorSettings.noiseSettings();
        
        this.worldMinY = shapeConfig.minY();
        this.worldHeight = shapeConfig.height();
        this.worldTopY = this.worldHeight + this.worldMinY;
        this.seaLevel = generatorSettings.seaLevel();
        this.bedrockFloor = 0;

        this.defaultBlock = generatorSettings.defaultBlock();
        this.defaultFluid = generatorSettings.defaultFluid();

        Infdev227Structures structures = this.chunkSettings.getOrDefault(SettingsComponentTypes.INFDEV_227_STRUCTURES);
        this.infdevUsePyramid = structures.brickPyramids();
        this.infdevUseWall = structures.obsidianWalls();
        
        this.octaveNoiseA = new PerlinOctaveNoise(this.random, 16, true); 
        this.octaveNoiseB = new PerlinOctaveNoise(this.random, 16, true);
        this.octaveNoiseC = new PerlinOctaveNoise(this.random, 8, true);
        this.octaveNoiseD = new PerlinOctaveNoise(this.random, 4, true);
        this.octaveNoiseE = new PerlinOctaveNoise(this.random, 4, true);
        this.octaveNoiseF = new PerlinOctaveNoise(this.random, 5, true);
        this.forestOctaveNoise = new PerlinOctaveNoise(this.random, 5, true);
        
        this.chunkCacheHeightmap = new ChunkCache<>("heightmap", this::sampleHeightmapChunk);
    }

    @Override
    public CompletableFuture<ChunkAccess> provideChunk(Blender blender, StructureManager structureAccessor, ChunkAccess chunk, RandomState noiseConfig) {
        this.generateTerrain(chunk, structureAccessor);  
        
        return CompletableFuture.<ChunkAccess>supplyAsync(
            () -> chunk, Util.backgroundExecutor()
        );
    }

    public void provideSurface(WorldGenRegion region, StructureManager structureAccessor, ChunkAccess chunk, ModernBetaBiomeSource biomeSource, RandomState noiseConfig) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        
        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;

        int startX = chunk.getPos().getMinBlockX();
        int startZ = chunk.getPos().getMinBlockZ();

        int bedrockFloor = this.worldMinY + this.bedrockFloor;

        Random bedrockRand = this.createSurfaceRandom(chunkX, chunkZ);
        
        for (int localX = 0; localX < 16; ++localX) {
            for (int localZ = 0; localZ < 16; ++localZ) {
                int x = startX + localX;
                int z = startZ + localZ;
                int surfaceTopY = chunk.getOrCreateHeightmapUnprimed(Types.OCEAN_FLOOR_WG).getFirstAvailable(localX, localZ) - 1;
                
                Holder<Biome> biome = biomeSource.getBiomeForSurfaceGen(region, pos.set(x, surfaceTopY, z));
                
                SurfaceConfig surfaceConfig = this.surfaceBuilder.getSurfaceConfig(biome);
                BlockState topBlock = surfaceConfig.normal().topBlock();
                BlockState fillerBlock = surfaceConfig.normal().fillerBlock();

                int runDepth = 0;

                for (int y = this.worldTopY; y >= this.worldMinY; --y) {
                    BlockState blockState;
                    
                    pos.set(localX, y, localZ);
                    blockState = chunk.getBlockState(pos);
                    
                    // Place bedrock
                    if (y <= bedrockFloor + bedrockRand.nextInt(5)) {
                        VersionCompat.setBlockState(chunk, pos, BlockStates.BEDROCK);
                        continue;
                    }

                    boolean inFluid = blockState.equals(BlockStates.AIR) || blockState.equals(this.defaultFluid);
                    
                    if (inFluid) {
                        runDepth = 0;
                        continue;
                    }
                    
                    if (!blockState.is(this.defaultBlock.getBlock())) {
                        continue;
                    }
                        
                    if (runDepth == 0) blockState = (y >= this.seaLevel) ? topBlock : fillerBlock;
                    if (runDepth == 1) blockState = fillerBlock;
                    
                    runDepth++;

                    VersionCompat.setBlockState(chunk, pos, blockState);
                }
            }
        }
    }

    @Override
    public int getHeight(LevelHeightAccessor level, int x, int z, Types type) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        
        int[] heightmap = this.chunkCacheHeightmap.get(chunkX, chunkZ); 
        int height = heightmap[(z & 0xF) + (x & 0xF) * 16];
        
        if (type == Heightmap.Types.WORLD_SURFACE_WG && height < this.seaLevel)
            height = this.seaLevel;
        
        return height + 1;
    }
    
    protected void generateTerrain(ChunkAccess chunk, StructureManager structureAccessor) {
        Random rand = new Random();
        
        Heightmap heightmapOcean = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
        Heightmap heightmapSurface = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);

        Beardifier structureWeightSampler = Beardifier.forStructuresInChunk(structureAccessor, chunk.getPos());
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        SimpleNoisePos noisePos = new SimpleNoisePos();
        
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        int startX = chunk.getPos().getMinBlockX();
        int startZ = chunk.getPos().getMinBlockZ();
        
        BlockHolder blockHolder = new BlockHolder();
        Block defaultBlock = this.defaultBlock.getBlock();
        Block defaultFluid = this.defaultFluid.getBlock();
        
        BlockSourceRules.Builder builder = new BlockSourceRules.Builder().add(this.getBaseBlockSource(structureWeightSampler, noisePos, blockHolder, defaultBlock, defaultFluid));
        this.blockSources.forEach(builder::add);
        builder.add(this.getActualBlockSource(blockHolder));
        
        BlockSourceRules blockSources = builder.build(this.defaultBlock);

        for (int localX = 0; localX < 16; ++localX) {
            int x = startX + localX;
            int rX = x / 1024;
            
            for (int localZ = 0; localZ < 16; ++localZ) {    
                int z = startZ + localZ;
                int rZ = z / 1024;
                
                int[] heightmap = this.chunkCacheHeightmap.get(chunkX, chunkZ); 
                int height = heightmap[(z & 0xF) + (x & 0xF) * 16];
                
                for (int y = this.worldMinY; y < this.worldTopY; ++y) {
                    Block block = Blocks.AIR;
                    
                    if (this.infdevUseWall && (x == 0 || z == 0) && y <= height + 2) {
                        block = Blocks.OBSIDIAN;
                    }
                    
                    /* Original code for reference, but unused so conventional surface/feature generation can be used.
                    else if (y == heightVal + 1 && heightVal >= this.seaLevel && Math.random() < 0.02) {
                        //blockToSet = Blocks.DANDELION;
                    }
                    else if (y == heightVal && heightVal >= this.seaLevel) {
                        blockToSet = Blocks.GRASS_BLOCK;
                    }
                    else if (y <= heightVal - 2) {
                        blockToSet = defaultBlock;
                    }
                    else if (y <= heightVal) {
                        blockToSet = Blocks.DIRT;
                    }
                    */
                    
                    else if (y <= height) {
                        block = defaultBlock;
                    }
                    
                    else if (y <= this.seaLevel) {
                        block = defaultFluid;
                    }
                    
                    if (this.infdevUsePyramid) {
                        rand.setSeed(rX + rZ * 13871);
                        int bX = (rX << 10) + 128 + rand.nextInt(512);
                        int bZ = (rZ << 10) + 128 + rand.nextInt(512);
                        
                        bX = x - bX;
                        bZ = z - bZ;
                        
                        if (bX < 0) bX = -bX;
                        if (bZ < 0) bZ = -bZ;
                        
                        if (bZ > bX) bX = bZ;
                        if ((bX = 127 - bX) == 255) bX = 1;
                        if (bX < height) bX = height;
                        
                        if (y <= bX && (block == Blocks.AIR || block == defaultFluid))
                            block = Blocks.BRICKS;     
                    }
                    
                    blockHolder.setBlock(block);
                    BlockState blockState = blockSources.apply(x, y, z);

                    VersionCompat.setBlockState(chunk, mutable.set(localX, y, localZ), blockState);
                    
                    heightmapOcean.update(localX, y, localZ, blockState);
                    heightmapSurface.update(localX, y, localZ, blockState);
                }
            }
        }
    }
    
    @Override
    protected PerlinOctaveNoise getForestOctaveNoise() {
        return this.forestOctaveNoise;
    }
    
    private int sampleHeightmap(int x, int z) {
        float noiseA = (float)(
            this.octaveNoiseA.sample(x * 32.0f, 0.0, z * 32.0f) - 
            this.octaveNoiseB.sample(x * 64.0f, 0.0, z * 64.0f)) / 512.0f / 4.0f;
        float noiseB = (float)this.octaveNoiseE.sampleXY(x / 4.0f, z / 4.0f);
        float noiseC = (float)this.octaveNoiseF.sampleXY(x / 8.0f, z / 8.0f) / 8.0f;
        
        noiseB = noiseB > 0.0f ? 
            ((float)(this.octaveNoiseC.sampleXY(x / 3.888889f * 2.0f, z / 3.888889f * 2.0f) * noiseC / 4.0)) :
            ((float)(this.octaveNoiseD.sampleXY(x / 3.888889f, z / 3.888889f) * noiseC));
            
        int heightVal = (int)(noiseA + this.seaLevel + noiseB);

        if ((float)this.octaveNoiseE.sampleXY(x, z) < 0.0f) {
            heightVal = heightVal / 2 << 1;
            if ((float)this.octaveNoiseE.sampleXY(x / 5, z / 5) < 0.0f) {
                ++heightVal;
            }
        }
        
        return heightVal;
    }
    
    private int[] sampleHeightmapChunk(int chunkX, int chunkZ) {
        int[] heightmap = new int[256];
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        
        int ndx = 0;
        for (int x = startX; x < startX + 16; ++x) {
            for (int z = startZ; z < startZ + 16; ++z) {
                heightmap[ndx++] = this.sampleHeightmap(x, z);
            }
        }
        
        return heightmap;
    }
}
