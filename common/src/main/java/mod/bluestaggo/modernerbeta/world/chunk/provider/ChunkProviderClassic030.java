package mod.bluestaggo.modernerbeta.world.chunk.provider;

import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProviderFinite;
import mod.bluestaggo.modernerbeta.api.world.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.util.BlockStates;
import mod.bluestaggo.modernerbeta.util.noise.PerlinOctaveNoise;
import mod.bluestaggo.modernerbeta.util.noise.PerlinOctaveNoiseCombined;
import mod.bluestaggo.modernerbeta.world.blocksource.BlockSourceRules;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowyBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;

public class ChunkProviderClassic030 extends ChunkProviderFinite {
    private PerlinOctaveNoiseCombined minHeightOctaveNoise;
    private PerlinOctaveNoiseCombined maxHeightOctaveNoise;
    private PerlinOctaveNoise mainHeightOctaveNoise;
    
    private PerlinOctaveNoise dirtOctaveNoise;
    
    private PerlinOctaveNoiseCombined erodeOctaveNoise0;
    private PerlinOctaveNoiseCombined erodeOctaveNoise1;
    
    private PerlinOctaveNoise sandOctaveNoise;
    private PerlinOctaveNoise gravelOctaveNoise;
    
    private final int waterLevel;

    public ChunkProviderClassic030(ModernBetaChunkGenerator chunkGenerator, long seed) {
        super(chunkGenerator, seed);

        this.waterLevel = this.levelHeight / 2;
    }

    @Override
    protected void pregenerateTerrain() {
        this.generateHeightmap();
        this.erodeTerrain();
        this.soilTerrain();
        if (this.chunkSettings.indevUseCaves) this.carveTerrain();
        // Ore population step here, but not included
        this.floodFluid();
        this.floodLava();
        this.growSurface();
    }

    @Override
    protected void generateBorder(Chunk chunk) {
        BlockPos.Mutable pos = new BlockPos.Mutable();
        
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 0; y < this.levelHeight; ++y) {
                    pos.set(x, y, z);
                    
                    if (y < this.waterLevel - 2) {
                        chunk.setBlockState(pos, BlockStates.BEDROCK, false);
                    } else if (y < this.waterLevel) {
                        chunk.setBlockState(pos, this.defaultFluid, false);
                    }
                }
            }
        }
    }

    @Override
    protected BlockState postProcessTerrainState(Block block, BlockSourceRules blockSources, TerrainState terrainState, BlockPos pos, int topY) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        
        BlockState blockState = block.getDefaultState();
        BlockState modifiedBlockState = blockSources.apply(x, y, z);
        
        boolean inFluid = modifiedBlockState.isAir() || modifiedBlockState.isOf(this.getLevelFluidBlock());
        int runDepth = terrainState.getRunDepth();
        
        // Check to see if structure weight sampler modifies terrain.
        if (!blockState.equals(modifiedBlockState)) {
            terrainState.terrainModified();
        }
        
        // Replace default block set by structure sampling with topsoil blocks.
        if (terrainState.isTerrainModified() && !inFluid) {
            if (runDepth == 0) {
                modifiedBlockState = y >= this.waterLevel - 1 ? BlockStates.GRASS_BLOCK : BlockStates.DIRT;
            }
            
            if (runDepth == 1) {
                modifiedBlockState = BlockStates.DIRT;
            }
            
            terrainState.incrementRunDepth();
        }
        
        return modifiedBlockState;
    }

    @Override
    protected void generateBedrock(Chunk chunk, Block block, BlockPos pos) {
        int y = pos.getY();
        
        // Set bedrock at y0 to simulate bottom of world.
        if (y == 0)
            chunk.setBlockState(pos, BlockStates.BEDROCK, false);
    }

    @Override
    protected BlockState postProcessSurfaceState(BlockState blockState, SurfaceConfig surfaceConfig, BlockPos pos, boolean isCold) {
        BlockState topBlock = surfaceConfig.normal().topBlock();
        BlockState fillerBlock = surfaceConfig.normal().fillerBlock();
        
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        
        if (blockState.isOf(BlockStates.GRASS_BLOCK.getBlock())) {
            blockState = topBlock;
        } else if (blockState.isOf(BlockStates.DIRT.getBlock())) {
            blockState = fillerBlock;
        }
        
        // Set snow/ice
        if (!this.inWorldBounds(x, z)) {
            if (y == this.waterLevel) {
                if (isCold && blockState.equals(topBlock)) {
                    blockState = topBlock.with(SnowyBlock.SNOWY, true);
                }
                
            } else if (y == this.waterLevel - 1) {
                if (isCold && blockState.equals(BlockStates.WATER)) {
                    blockState = BlockStates.ICE;
                }
            }
        }
        
        return blockState;
    }

    private void generateHeightmap() {
        this.setPhase("Raising");
        
        this.minHeightOctaveNoise = new PerlinOctaveNoiseCombined(new PerlinOctaveNoise(random, 8, false), new PerlinOctaveNoise(random, 8, false));
        this.maxHeightOctaveNoise = new PerlinOctaveNoiseCombined(new PerlinOctaveNoise(random, 8, false), new PerlinOctaveNoise(random, 8, false));
        this.mainHeightOctaveNoise = new PerlinOctaveNoise(random, 6, false);
        
        for (int x = 0; x < this.levelWidth; ++x) {
            for (int z = 0; z < this.levelLength; ++z) {
                double heightLow = minHeightOctaveNoise.sample(x * this.chunkSettings.indevNoiseScale, z * this.chunkSettings.indevNoiseScale)
                        / this.chunkSettings.indevMinHeightDamp + this.chunkSettings.indevMinHeightBoost;
                double heightHigh = maxHeightOctaveNoise.sample(x * this.chunkSettings.indevNoiseScale, z * this.chunkSettings.indevNoiseScale)
                        / this.chunkSettings.indevMaxHeightDamp + this.chunkSettings.indevMaxHeightBoost;
                
                double heightSelector = mainHeightOctaveNoise.sampleXY(x * this.chunkSettings.indevSelectorScale, z * this.chunkSettings.indevSelectorScale) / 8.0;
                
                if (heightSelector > 0.0) {
                    heightHigh = heightLow;
                }
                
                double heightResult = Math.max(heightLow, heightHigh) / 2.0;
                
                if (heightResult < 0.0) {
                    heightResult /= this.chunkSettings.indevHeightUnderDamp;
                }
                
                this.heightmap[x + z * this.levelWidth] = (int)heightResult;
            }
        }
    }
    
    private void erodeTerrain() {
        this.setPhase("Eroding");
        
        this.erodeOctaveNoise0 = new PerlinOctaveNoiseCombined(new PerlinOctaveNoise(random, 8, false), new PerlinOctaveNoise(random, 8, false));
        this.erodeOctaveNoise1 = new PerlinOctaveNoiseCombined(new PerlinOctaveNoise(random, 8, false), new PerlinOctaveNoise(random, 8, false));
        
        for (int x = 0; x < this.levelWidth; ++x) {
            for (int z = 0; z < this.levelLength; ++z) {
                double erodeSelector = erodeOctaveNoise0.sample(x << 1, z << 1) / 8.0;
                int erodeNoise = erodeOctaveNoise1.sample(x << 1, z << 1) > 0.0 ? 1 : 0;
            
                if (erodeSelector > 2.0) {
                    int heightResult = this.heightmap[x + z * this.levelWidth];
                    heightResult = ((heightResult - erodeNoise) / 2 << 1) + erodeNoise;
                    
                    this.heightmap[x + z * this.levelWidth] = heightResult;
                }
            }
        }
    }
    
    private void soilTerrain() {
        this.setPhase("Soiling");
        
        this.dirtOctaveNoise = new PerlinOctaveNoise(random, 8, false);
        
        for (int x = 0; x < this.levelWidth; ++x) {
            for (int z = 0; z < this.levelLength; ++z) {
                int dirtThickness = (int)(dirtOctaveNoise.sampleXY(x, z) / 24.0) - 4;
                int dirtThreshold = this.heightmap[x + z * this.levelWidth] + this.waterLevel;
                
                int stoneThreshold = dirtThickness + dirtThreshold;
                this.heightmap[x + z * this.levelWidth] = Math.max(dirtThreshold, stoneThreshold);
                
                if (this.heightmap[x + z * this.levelWidth] > this.levelHeight - 2) {
                    this.heightmap[x + z * this.levelWidth] = this.levelHeight - 2;
                }
             
                if (this.heightmap[x + z * this.levelWidth] < 1) {
                    this.heightmap[x + z * this.levelWidth] = 1;
                }
                
                for (int y = 0; y < this.levelHeight; ++y) {
                    Block block = Blocks.AIR;
                    
                    if (y <= dirtThreshold)
                        block = Blocks.DIRT;
                    
                    if (y <= stoneThreshold)
                        block = Blocks.STONE;
                    
                    // Move lava up a block to leave room for bedrock
                    if (y == 1) 
                        block = Blocks.LAVA;
                    
                    this.setLevelBlock(x, y, z, block);
                }
            }
        }
    }
    
    private void carveTerrain() {
        this.setPhase("Carving");
        
        int caveCount = this.levelWidth * this.levelLength * this.levelHeight / this.chunkSettings.indevCaveRarity;
        
        for (int i = 0; i < caveCount; ++i) {
            float caveX = random.nextFloat() * this.levelWidth;
            float caveY = random.nextFloat() * this.levelHeight;
            float caveZ = random.nextFloat() * this.levelLength;

            int caveLen = (int)((random.nextFloat() + random.nextFloat()) * 200F);
            
            float theta = random.nextFloat() * 3.1415927f * 2.0f;
            float deltaTheta = 0.0f;
            float phi = random.nextFloat() * 3.1415927f * 2.0f;
            float deltaPhi = 0.0f;
            
            float caveRadius = random.nextFloat() * random.nextFloat() * this.caveRadius;
            
            for (int len = 0; len < caveLen; ++len) {
                caveX += MathHelper.sin(theta) * MathHelper.cos(phi);
                caveZ += MathHelper.cos(theta) * MathHelper.cos(phi);
                caveY += MathHelper.sin(phi);
                
                // TODO: Double-check
                theta = theta + deltaTheta * 0.2f;
                deltaTheta = (deltaTheta * 0.9f) + (random.nextFloat() - random.nextFloat());
                phi = phi * 0.5f + deltaPhi * 0.25f;
                deltaPhi = (deltaPhi * 0.75f) + (random.nextFloat() - random.nextFloat());
                
                if (random.nextFloat() >= 0.25f) {
                    float centerX = caveX + (random.nextFloat() * 4.0f - 2.0f) * 0.2f;
                    float centerY = caveY + (random.nextFloat() * 4.0f - 2.0f) * 0.2f;
                    float centerZ = caveZ + (random.nextFloat() * 4.0f - 2.0f) * 0.2f;
                    
                    float radius = (this.levelHeight - centerY) / this.levelHeight;
                    radius = 1.2f + (radius * 3.5f + 1.0f) * caveRadius;
                    radius = radius * MathHelper.sin(len * 3.1415927f / caveLen);
                    
                    fillOblateSpheroid(centerX, centerY, centerZ, radius, Blocks.AIR);
                }
            }
        }
    }
    
    // Using Classic generation algorithm
    private void floodFluid() {
        this.setPhase("Watering");
        
        Block fluid = this.defaultFluid.getBlock();

        for (int x = 0; x < this.levelWidth; ++x) {
            flood(x, this.waterLevel - 1, 0, fluid);
            flood(x, this.waterLevel - 1, this.levelLength - 1, fluid);
        }
        
        for (int z = 0; z < this.levelLength; ++z) {
            flood(this.levelWidth - 1, this.waterLevel - 1, z, fluid);
            flood(0, this.waterLevel - 1, z, fluid);
        }
        
        int waterSourceCount = this.levelWidth * this.levelLength / this.chunkSettings.indevWaterRarity;
        
        for (int i = 0; i < waterSourceCount; ++i) {
            int randX = random.nextInt(this.levelWidth);
            int randZ = random.nextInt(this.levelLength);
            int randY = (this.waterLevel - 1) - random.nextInt(2);
            
            this.flood(randX, randY, randZ, fluid);
        }
    }
    
    // Using Classic generation algorithm
    private void floodLava() {
        this.setPhase("Melting");

        int lavaSourceCount = this.levelWidth * this.levelLength / this.chunkSettings.indevLavaRarity;
         
        for (int i = 0; i < lavaSourceCount; ++i) {
            int randX = random.nextInt(this.levelWidth);
            int randZ = random.nextInt(this.levelLength);
            int randY = (int)((float)(this.waterLevel - 3) * random.nextFloat() * random.nextFloat());
            
            this.flood(randX, randY, randZ, Blocks.LAVA);
        }
    }
    
    private void growSurface() {
        this.setPhase("Growing");
        
        this.sandOctaveNoise = new PerlinOctaveNoise(random, 8, false);
        this.gravelOctaveNoise = new PerlinOctaveNoise(random, 8, false);
        
        for (int x = 0; x < this.levelWidth; ++x) {
            for (int z = 0; z < this.levelLength; ++z) {
                boolean genSand = sandOctaveNoise.sampleXY(x, z) > this.chunkSettings.indevSandBeachThreshold;
                boolean genGravel = gravelOctaveNoise.sampleXY(x, z) > this.chunkSettings.indevGravelBeachThreshold;
                
                int heightResult = heightmap[x + z * this.levelWidth];
                Block blockUp = this.getLevelBlock(x, heightResult + 1, z);

                genSand &= heightResult <= this.waterLevel - 1
                        && (this.chunkSettings.indevSandBeachUnderAir && blockUp == Blocks.AIR
                                || this.chunkSettings.indevSandBeachUnderFluid && blockUp == this.defaultFluid.getBlock());
                genGravel &= heightResult <= this.waterLevel - 1
                        && (this.chunkSettings.indevGravelBeachUnderAir && blockUp == Blocks.AIR
                                || this.chunkSettings.indevGravelBeachUnderFluid && blockUp == this.defaultFluid.getBlock());

                Block surfaceBlock = genSand ? Blocks.SAND
                        : genGravel ? Blocks.GRAVEL
                        : blockUp == this.defaultFluid.getBlock() ? Blocks.DIRT
                        : Blocks.GRASS_BLOCK;
                this.setLevelBlock(x, heightResult, z, surfaceBlock);
            }
        }
    }
}
