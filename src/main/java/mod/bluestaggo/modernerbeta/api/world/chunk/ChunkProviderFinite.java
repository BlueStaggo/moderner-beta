package mod.bluestaggo.modernerbeta.api.world.chunk;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.api.world.blocksource.BlockSource;
import mod.bluestaggo.modernerbeta.api.world.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.api.world.spawn.SpawnLocator;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.FiniteLevelProperties;
import mod.bluestaggo.modernerbeta.util.BlockStates;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.util.noise.SimpleNoisePos;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.blocksource.BlockSourceRules;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaGenerationStep;
import mod.bluestaggo.modernerbeta.world.spawn.SpawnLocatorIndev;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.Aquifer.FluidPicker;
import net.minecraft.world.level.levelgen.Aquifer.FluidStatus;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.phys.Vec3;
import org.slf4j.event.Level;

import java.util.ArrayDeque;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public abstract class ChunkProviderFinite extends ChunkProvider implements ChunkProviderNoiseImitable {
    private static String levelPhase;
    
    protected final int worldMinY;
    protected final int worldHeight;
    protected final int worldTopY;
    protected final int seaLevel;
    
    protected final int bedrockFloor;
    protected final int bedrockCeiling;
    
    protected final BlockState defaultBlock;
    protected final BlockState defaultFluid;

    protected final FiniteLevelProperties levelProperties;
    protected final int levelWidth;
    protected final int levelLength;
    protected final int levelHeight;
    protected final float caveRadius;
    
    protected final int[] heightmap;
    
    @Deprecated
    private final Block[][][] blockArr;
    
    private boolean pregenerated;

    public ChunkProviderFinite(ModernBetaChunkGenerator chunkGenerator, long seed) {
        super(chunkGenerator, seed);

        NoiseGeneratorSettings generatorSettings = chunkGenerator.getGeneratorSettings().value();
        NoiseSettings shapeConfig = generatorSettings.noiseSettings();

        this.levelProperties = this.getChunkSettings().getOrDefault(SettingsComponentTypes.FINITE_LEVEL_PROPERTIES);
        
        this.worldMinY = shapeConfig.minY();
        this.worldHeight = shapeConfig.height();
        this.worldTopY = this.worldHeight + this.worldMinY;
        this.seaLevel = generatorSettings.seaLevel() + this.getChunkSettings().getOrDefault(SettingsComponentTypes.SEA_LEVEL_OFFSET);
        this.bedrockFloor = 0;
        this.bedrockCeiling = Integer.MIN_VALUE;

        this.defaultBlock = generatorSettings.defaultBlock();
        this.defaultFluid = generatorSettings.defaultFluid();

        this.levelWidth = this.levelProperties.width();
        this.levelLength = this.levelProperties.length();
        this.levelHeight = Mth.clamp(this.levelProperties.height(), 0, this.worldTopY);
        this.caveRadius = this.getChunkSettings().getOrDefault(SettingsComponentTypes.FINITE_CAVE_GENERATION).radius();
        
        this.heightmap = new int[this.levelWidth * this.levelLength];
        this.blockArr = new Block[this.levelWidth][this.levelHeight][this.levelLength];
        this.fillBlockArr(Blocks.AIR);
        
        this.pregenerated = false;
    }
    
    @Override
    public SpawnLocator getSpawnLocator() {
        return new SpawnLocatorIndev(this);
    }

    @Override
    public CompletableFuture<ChunkAccess> provideChunk(Blender blender, StructureManager structureAccessor, ChunkAccess chunk, RandomState noiseConfig) {
        ChunkPos pos = chunk.getPos();

        if (this.inWorldBounds(pos.getMinBlockX(), pos.getMinBlockZ())) {
            this.pregenerateTerrainOrWait();
            this.generateTerrain(chunk, structureAccessor);
        } else {
            this.generateBorder(chunk);
        }

        return CompletableFuture.supplyAsync(
            () -> chunk, Util.backgroundExecutor()
        );
    }
    
    @Override
    public void provideSurface(WorldGenRegion region, StructureManager structureAccessor, ChunkAccess chunk, ModernBetaBiomeSource biomeSource, RandomState noiseConfig) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        
        int startX = chunk.getPos().getMinBlockX();
        int startZ = chunk.getPos().getMinBlockZ();
        
        int worldTopY = this.worldHeight + this.worldMinY;
        
        for (int localX = 0; localX < 16; ++localX) {
            for (int localZ = 0; localZ < 16; ++localZ) {
                int x = startX + localX;
                int z = startZ + localZ;
                Holder<Biome> biome = biomeSource.getBiomeForSurfaceGen(region, pos.set(x, 0, z));
                SurfaceConfig surfaceConfig = this.surfaceBuilder.getSurfaceConfig(biome);
                
                boolean isCold;
                if (biomeSource.getBiomeProvider() instanceof ClimateSampler climateSampler &&
                    climateSampler.useBiomeFeature()) {
                    isCold = climateSampler.sample(x, z).temp() < 0.5D;
                } else {
                    //? if >=1.21.2 {
                    isCold = biome.value().coldEnoughToSnow(pos, seaLevel);
                    //?} else {
                    /*isCold = biome.value().isCold(pos.down(seaLevel - 63));
                    *///?}
                }
                
                for (int y = worldTopY - 1; y >= this.worldMinY; --y) {
                    pos.set(x, y, z);
                    
                    BlockState blockState = this.postProcessSurfaceState(chunk.getBlockState(pos), surfaceConfig, pos, isCold);
                    
                    VersionCompat.setBlockState(chunk, pos, blockState);

                    // Set snow on top of snowy blocks
                    if (blockState.hasProperty(BlockStateProperties.SNOWY) && blockState.getValue(BlockStateProperties.SNOWY))
                        VersionCompat.setBlockState(chunk, pos.above(), BlockStates.SNOW);
                        
                }
            }
        }
    }

    @Override
    public int getHeight(LevelHeightAccessor world, int x, int z, Types type) {
        int seaLevel = this.getSeaLevel();
        
        x += this.levelWidth / 2;
        z += this.levelLength / 2;
        
        if (x < 0 || x >= this.levelWidth || z < 0 || z >= this.levelLength) 
            return seaLevel;
        
        this.pregenerateTerrainOrWait();
        int height = this.getLevelHighestBlock(x, z, type);
         
        return height;
    }

    /**
     * @return World sea level in block coordinates.
     */
    @Override
    public int getSeaLevel() {
        return this.seaLevel;
    }
    
    @Override
    public boolean skipChunk(int chunkX, int chunkZ, ModernBetaGenerationStep step) {
        boolean outOfBounds = !this.inWorldBounds(chunkX << 4, chunkZ << 4);
        
        if (step == ModernBetaGenerationStep.FEATURES) {
            return outOfBounds;
        } else if (step == ModernBetaGenerationStep.STRUCTURE_STARTS) {
            return outOfBounds;
        }  else if (step == ModernBetaGenerationStep.CARVERS) {
            return outOfBounds || this.skipCarvers;
        } else if (step == ModernBetaGenerationStep.SURFACE) { 
            return false;
        } else if (step == ModernBetaGenerationStep.ENTITY_SPAWN) {
            return outOfBounds;
        }
        
        return false;
    }
    
    @Override
    public Aquifer getAquiferSampler(ChunkAccess chunk, RandomState noiseConfig) {
        FluidPicker fluidLevelSampler = (x, y, z) -> new FluidStatus(
            this.getSeaLevel(), this.getLevelFluidBlock().defaultBlockState()
        );
        
        return Aquifer.createDisabled(fluidLevelSampler);
    }
    
    public int getLevelWidth() {
        return this.levelWidth;
    }
    
    public int getLevelLength() {
        return this.levelLength;
    }
    
    public int getLevelHeight() {
        return this.levelHeight;
    }
    
    public float getCaveRadius() {
        return this.caveRadius;
    }
    
    public Block getLevelBlock(int x, int y, int z) {
        x = Mth.clamp(x, 0, this.levelWidth - 1);
        y = Mth.clamp(y, 0, this.levelHeight - 1);
        z = Mth.clamp(z, 0, this.levelLength - 1);
        
        return this.blockArr[x][y][z];
    }
    
    public void setLevelBlock(int x, int y, int z, Block block) {
        x = Mth.clamp(x, 0, this.levelWidth - 1);
        y = Mth.clamp(y, 0, this.levelHeight - 1);
        z = Mth.clamp(z, 0, this.levelLength - 1);
        
        this.blockArr[x][y][z] = block;
    }
    
    public int getLevelHighestBlock(int x, int z, Heightmap.Types type) {
        x = Mth.clamp(x, 0, this.levelWidth - 1);
        z = Mth.clamp(z, 0, this.levelLength - 1);
        
        Predicate<Block> checkBlock = switch(type) {
            case OCEAN_FLOOR_WG -> block -> block == Blocks.AIR || block == this.getLevelFluidBlock();
            case WORLD_SURFACE_WG -> block -> block == Blocks.AIR;
            default -> block -> block == Blocks.AIR;
        };
        
        int y;
        
        for (y = this.levelHeight; checkBlock.test(this.getLevelBlock(x, y - 1, z)) && y > 0; --y);
        
        return y;
    }
    
    public Block getLevelFluidBlock() {
        return this.defaultFluid.getBlock();
    }
    
    protected abstract void pregenerateTerrain();
    
    protected abstract void generateBorder(ChunkAccess chunk);
    
    protected abstract BlockState postProcessTerrainState(
        Block block, 
        BlockSourceRules blockSources,
        TerrainState terrainState,
        BlockPos pos,
        int topY
    );
    
    protected abstract void generateBedrock(ChunkAccess chunk, Block block, BlockPos pos);

    protected abstract BlockState postProcessSurfaceState(BlockState blockState, SurfaceConfig config, BlockPos pos, boolean isCold);
    
    protected void generateTerrain(ChunkAccess chunk, StructureManager structureAccessor) {
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        int offsetX = (chunkX + this.levelWidth / 16 / 2) * 16;
        int offsetZ = (chunkZ + this.levelLength / 16 / 2) * 16;
        
        Heightmap heightmapOcean = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
        Heightmap heightmapSurface = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
        
        Beardifier structureWeightSampler = Beardifier.forStructuresInChunk(structureAccessor, chunk.getPos());
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        SimpleNoisePos noisePos = new SimpleNoisePos();
        
        BlockHolder blockHolder = new BlockHolder();
        BlockSource baseBlockSource = this.getBaseBlockSource(structureWeightSampler, noisePos, blockHolder, this.defaultBlock.getBlock(), this.getLevelFluidBlock());
        BlockSourceRules blockSources = new BlockSourceRules.Builder()
            .add(baseBlockSource)
            .add(this.getActualBlockSource(blockHolder))
            .build(this.defaultBlock);
        
        for (int localX = 0; localX < 16; ++localX) {
            for (int localZ = 0; localZ < 16; ++localZ) {
                
                int x = localX + (chunkX << 4);
                int z = localZ + (chunkZ << 4);
                int topY = this.getHeight(null, x, z, Types.OCEAN_FLOOR_WG);
                
                TerrainState terrainState = new TerrainState();
                
                for (int y = this.levelHeight - 1; y >= 0; --y) {
                    pos.set(x, y, z);
                    
                    Block block = this.getLevelBlock(offsetX + localX, y, offsetZ + localZ);
                    blockHolder.setBlock(block);
                    
                    BlockState blockState = this.postProcessTerrainState(block, blockSources, terrainState, pos, topY);
                    
                    VersionCompat.setBlockState(chunk, pos.set(localX, y, localZ), blockState);
                     
                    this.generateBedrock(chunk, block, pos);
                    
                    heightmapOcean.update(localX, y, localZ, block.defaultBlockState());
                    heightmapSurface.update(localX, y, localZ, block.defaultBlockState());
                }
            }
        }
    }

    protected boolean inWorldBounds(int x, int z) {
        int halfWidth = this.levelWidth / 2;
        int halfLength = this.levelLength / 2;

        return x >= -halfWidth && x < halfWidth && z >= -halfLength && z < halfLength;
    }

    protected boolean inLevelBounds(int x, int y, int z) {
        return x >= 0 && x < this.levelWidth && y >= 0 && y < this.levelHeight && z >= 0 && z < this.levelLength;
    }

    protected void setPhase(String phase) {
        levelPhase = phase + "..";
        
        ModernerBeta.log(Level.INFO, levelPhase);
    }
    
    protected void fillOblateSpheroid(float centerX, float centerY, float centerZ, float radius, Block fillBlock) {
        for (int x = (int)(centerX - radius); x < (int)(centerX + radius); ++x) {
            for (int y = (int)(centerY - radius); y < (int)(centerY + radius); ++y) {
                for (int z = (int)(centerZ - radius); z < (int)(centerZ + radius); ++z) {
                
                    float dx = x - centerX;
                    float dy = y - centerY;
                    float dz = z - centerZ;
                    
                    if ((dx * dx + dy * dy * 2.0f + dz * dz) < radius * radius && inLevelBounds(x, y, z)) {
                        Block block = this.getLevelBlock(x, y, z);
                        
                        if (block == this.defaultBlock.getBlock()) {
                            this.setLevelBlock(x, y, z, fillBlock);
                        }
                    }
                }
            }
        }
    }
    
    protected void flood(int x, int y, int z, Block fillBlock) {
        ArrayDeque<Vec3> positions = new ArrayDeque<>();
        
        positions.add(new Vec3(x, y, z));
        
        while (!positions.isEmpty()) {
            Vec3 curPos = positions.poll();
            x = (int)curPos.x;
            y = (int)curPos.y;
            z = (int)curPos.z;
            
            Block block = this.getLevelBlock(x, y, z);
    
            if (block == Blocks.AIR) {
                this.setLevelBlock(x, y, z, fillBlock);
                
                if (y - 1 >= 0)               this.tryFlood(x, y - 1, z, positions);
                if (x - 1 >= 0)               this.tryFlood(x - 1, y, z, positions);
                if (x + 1 < this.levelWidth)  this.tryFlood(x + 1, y, z, positions);
                if (z - 1 >= 0)               this.tryFlood(x, y, z - 1, positions);
                if (z + 1 < this.levelLength) this.tryFlood(x, y, z + 1, positions);
            }
        }
    }
    
    private void tryFlood(int x, int y, int z, ArrayDeque<Vec3> positions) {
        Block block = this.getLevelBlock(x, y, z);
        
        if (block == Blocks.AIR) {
            positions.add(new Vec3(x, y, z));
        }
    }

    private synchronized void pregenerateTerrainOrWait() {
        if (!this.pregenerated) {
            this.pregenerateTerrain();
            this.pregenerated = true;
        }
    }
    
    private void fillBlockArr(Block block) {
        for (int x = 0; x < this.levelWidth; ++x) {
            for (int z = 0; z < this.levelLength; ++z) {
                for (int y = 0; y < this.levelHeight; ++y) {
                    this.setLevelBlock(x, y, z, block);
                }
            }
        }
    }
    
    public static void resetPhase() {
        levelPhase = "";
    }
    
    public static String getPhase() {
        return levelPhase;
    }
    
    protected static class TerrainState {
        private int runDepth;
        private boolean terrainModified;
        
        public TerrainState() {
            this.runDepth = 0;
            this.terrainModified = false;
        }
        
        public int getRunDepth() {
            return this.runDepth;
        }
        
        public void incrementRunDepth() {
            this.runDepth++;
        }
        
        public boolean isTerrainModified() {
            return this.terrainModified;
        }
        
        public void terrainModified() {
            this.terrainModified = true;
        }
    }
}
