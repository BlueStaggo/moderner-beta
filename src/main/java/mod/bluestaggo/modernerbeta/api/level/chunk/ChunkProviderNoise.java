package mod.bluestaggo.modernerbeta.api.level.chunk;

import com.google.common.collect.Sets;
import mod.bluestaggo.modernerbeta.api.level.blocksource.BlockSource;
import mod.bluestaggo.modernerbeta.api.level.chunk.noise.NoisePostProcessor;
import mod.bluestaggo.modernerbeta.api.level.chunk.noise.NoiseProvider;
import mod.bluestaggo.modernerbeta.api.level.chunk.noise.NoiseProviderBase;
import mod.bluestaggo.modernerbeta.api.level.chunk.noise.NoiseSampler;
import mod.bluestaggo.modernerbeta.level.blocksource.BlockSourceRules;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaChunkGenerator;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaChunkNoiseSampler;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaGenerationStep;
import mod.bluestaggo.modernerbeta.level.chunk.provider.island.IslandShape;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.*;
import mod.bluestaggo.modernerbeta.util.BlockStates;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.util.chunk.AuxChunkCache;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkCache;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkHeightmap;
import mod.bluestaggo.modernerbeta.util.noise.SimpleNoisePos;
import mod.bluestaggo.modernerbeta.util.noise.SimplexNoise;
import net.minecraft.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public abstract class ChunkProviderNoise extends ChunkProvider {
    protected final int worldMinY;
    protected final int worldHeight;
    protected final int worldTopY;
    
    protected final int bedrockFloor;
    protected final int bedrockCeiling;
    
    protected final BlockState defaultBlock;
    protected final BlockState defaultFluid;

    protected final int noiseResolutionVertical;   // Number of blocks in a vertical subchunk
    protected final int noiseResolutionHorizontal; // Number of blocks in a horizontal subchunk 
    
    protected final int noiseSizeX; // Number of horizontal subchunks along x
    protected final int noiseSizeZ; // Number of horizontal subchunks along z
    protected final int noiseSizeY; // Number of vertical subchunks
    protected final int noiseMinY;  // Subchunk index of bottom of the world
    protected final int noiseTopY;  // Number of positive (y >= 0) vertical subchunks

    private final ChunkCache<NoiseProviderBase> chunkCacheNoise;
    private final AuxChunkCache<LevelHeightAccessor, ChunkHeightmap> chunkCacheHeightmap;
    
    protected final List<NoisePostProcessor> noisePostProcessors = new ArrayList<>();
    private final SimplexNoise islandNoise;

    private final IslesProperties islesProperties;
    protected final NoiseScale noiseScale;
    private final NoiseSlide noiseSlide;
    protected final WorldBorderLocation worldBorderLocation;

    private final AtomicReference<RandomState> noiseConfig = new AtomicReference<>();

    public ChunkProviderNoise(ModernBetaChunkGenerator chunkGenerator, long seed) {
        super(chunkGenerator, seed);
        
        NoiseGeneratorSettings generatorSettings = chunkGenerator.generatorSettings().value();
        NoiseSettings noiseSettings = this.getNoiseSettings();

        this.islesProperties = this.getChunkSettings().getOrDefault(SettingsComponentTypes.ISLES_PROPERTIES);
        this.noiseScale = this.getChunkSettings().getOrDefault(SettingsComponentTypes.NOISE_SCALE);
        this.noiseSlide = this.getChunkSettings().getOrElse(SettingsComponentTypes.NOISE_SLIDE, NoiseSlide.DISABLED);
        this.worldBorderLocation = this.getChunkSettings().getOrDefault(SettingsComponentTypes.WORLD_BORDER);

        this.worldMinY = noiseSettings.minY();
        this.worldHeight = noiseSettings.height();
        this.worldTopY = this.worldHeight + this.worldMinY;
        
        this.bedrockFloor = this.worldMinY;
        this.bedrockCeiling = this.worldTopY;

        this.defaultBlock = generatorSettings.defaultBlock();
        this.defaultFluid = generatorSettings.defaultFluid();
        
        this.noiseResolutionVertical = noiseSettings.noiseSizeVertical() * 4;
        this.noiseResolutionHorizontal = noiseSettings.noiseSizeHorizontal() * 4;
        
        this.noiseSizeX = 16 / this.noiseResolutionHorizontal;
        this.noiseSizeZ = 16 / this.noiseResolutionHorizontal;
        this.noiseSizeY = Mth.floorDiv(this.worldHeight, this.noiseResolutionVertical);
        this.noiseMinY = Mth.floorDiv(this.worldMinY, this.noiseResolutionVertical);
        this.noiseTopY = Mth.floorDiv(this.worldMinY + this.worldHeight, this.noiseResolutionVertical);

        this.chunkCacheNoise = new ChunkCache<>(
            "base_noise",
            (chunkX, chunkZ) -> {
                NoiseProviderBase noiseProviderBase = new NoiseProviderBase(
                    this.noiseSizeX,
                    this.noiseSizeY,
                    this.noiseSizeZ,
                    this::sampleNoiseColumn,
                    this.isDensityModified() ? this::modifyEdgeDensity : null
                );

                noiseProviderBase.sampleInitialNoise(chunkX * this.noiseSizeX, chunkZ * this.noiseSizeZ);
                return noiseProviderBase;
            }
        );
        this.chunkCacheHeightmap = new AuxChunkCache<>("heightmap", this::sampleHeightmap);

        this.islandNoise = new SimplexNoise(this.createRandom(this.seed));

        CaveGeneration caveSettings = this.getChunkSettings().getOrDefault(SettingsComponentTypes.CAVE_GENERATION);
        if (caveSettings.useNoiseCaves()) {
            this.noisePostProcessors.add(NoisePostProcessor.NOISE_CAVES);
        }
    }

    /**
     * Generates base terrain for given chunk and returns it.
     * @param blender
     * @param structureAccessor
     * @param chunk
     * @param noiseConfig
     * @return A completed chunk.
     */
    @Override
    public CompletableFuture<ChunkAccess> provideChunk(Blender blender, StructureManager structureAccessor, ChunkAccess chunk, RandomState noiseConfig) {
        this.setNoiseConfig(noiseConfig);

        NoiseSettings noiseSettings = this.getNoiseSettings().clampToHeightAccessor(chunk.getHeightAccessorForGeneration());
        int minY = noiseSettings.minY();
        int minimumCellY = Mth.floorDiv(minY, noiseSettings.getCellHeight());
        int cellHeight = Mth.floorDiv(noiseSettings.height(), noiseSettings.getCellHeight());

        return cellHeight <= 0 ? CompletableFuture.completedFuture(chunk) : CompletableFuture.supplyAsync(() -> {
            int sectionTopY = chunk.getSectionIndex(cellHeight * noiseSettings.getCellHeight() - 1 + minY);
            int sectionMinY = chunk.getSectionIndex(minY);

            HashSet<LevelChunkSection> sections = Sets.newHashSet();
            for (int sectionNdx = sectionTopY; sectionNdx >= sectionMinY; --sectionNdx) {
                LevelChunkSection section = chunk.getSection(sectionNdx);

                section.acquire();
                sections.add(section);
            }

            try {
                this.generateTerrain(chunk, structureAccessor, noiseConfig, minimumCellY, cellHeight);
            } finally {
                for (LevelChunkSection section : sections) {
                    section.release();
                }
            }

            return chunk;
        }, Util.backgroundExecutor());
    }

    @Override
    public boolean skipChunk(int chunkX, int chunkZ, ModernBetaGenerationStep step) {
        return super.skipChunk(chunkX, chunkX, step)
            || step == ModernBetaGenerationStep.CARVERS && !this.worldBorderLocation.containsChunk(chunkX, chunkZ);
    }

    /**
     * Sample height at given x/z coordinate. Initially generates heightmap for entire chunk,
     * if chunk containing x/z coordinates has never been sampled.
     *
     * @param level a world context to clamp heights to.
     * @param x     x-coordinate in block coordinates.
     * @param z     z-coordinate in block coordinates.
     * @param type  Vanilla heightmap type.
     * @return The y-coordinate of top block at x/z.
     */
    @Override
    public int getHeight(LevelHeightAccessor level, int x, int z, Heightmap.Types type) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        
        return this.chunkCacheHeightmap.get(level, chunkX, chunkZ).getHeight(x, z, type);
    }

    /**
     * Sample height at given x/z coordinate. Initially generates heightmap for entire chunk,
     * if chunk containing x/z coordinates has never been sampled.
     *
     * @param level a level context to clamp heights to.
     * @param x     x-coordinate in block coordinates.
     * @param z     z-coordinate in block coordinates.
     * @param type  HeightmapChunk heightmap type.
     * @return The y-coordinate of top block at x/z.
     */
    public int getHeight(LevelHeightAccessor level, int x, int z, ChunkHeightmap.Type type) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        
        return this.chunkCacheHeightmap.get(level, chunkX, chunkZ).getHeight(x, z, type);
    }
    
    /**
     * Create a new aquifer sampler.
     * 
     * @param chunk
     * @param noiseConfig
     * 
     * @return A new aquifer sampler.
     */
    @Override
    public Aquifer getAquiferSampler(ChunkAccess chunk, RandomState noiseConfig) {
        SurfaceProperties surfaceProperties = this.getChunkSettings().getOrDefault(SettingsComponentTypes.SURFACE_PROPERTIES);

        PositionalRandomFactory randomDeriver = this.randomSource.newInstance(this.seed).forkPositional();
        NoiseChunk noiseSampler = ModernBetaChunkNoiseSampler.create(
            chunk,
            noiseConfig,
            this.generatorSettings.value(),
            this.getFluidLevelSampler(),
            this
        );
        
        AquiferSamplerProvider aquiferSamplerProvider = new AquiferSamplerProvider(
            this.generatorSettings.value().noiseRouter(),
            randomDeriver,
            noiseSampler,
            this.defaultFluid,
            this.getSeaLevel(),
            this.worldMinY + 10,
            this.worldMinY,
            this.worldHeight,
            this.noiseResolutionVertical,
            this.generatorSettings.value().aquifersEnabled() && surfaceProperties.generateLiquids()
        );
        
        return aquiferSamplerProvider.provideAquiferSampler(chunk);
    }

    public void setNoiseConfig(RandomState noiseConfig) {
        this.noiseConfig.set(noiseConfig);
    }
    
    /**
     * Generates noise for a column at startNoiseX + localNoiseX / startNoiseZ + localNoiseZ.
     *
     * @param primaryBuffer   Primary heightmap buffer, with noise caves.
     * @param heightmapBuffer Heightmap buffer, identical to primaryBuffer sans noise caves.
     * @param startNoiseX     x-coordinate start of chunk in noise coordinates.
     * @param startNoiseZ     z-coordinate start of chunk in noise coordinates.
     * @param localNoiseX     Current subchunk index along x-axis.
     * @param localNoiseZ     Current subchunk index along z-axis.
     */
    protected abstract void sampleNoiseColumn(
        double[] primaryBuffer,
        double[] heightmapBuffer,
        int startNoiseX,
        int startNoiseZ,
        int localNoiseX,
        int localNoiseZ
    );

    /**
     * Check if default noise post processor (i.e. NONE) is being used.
     * 
     * @return Whether default noise post processor is being used.
     */
    protected boolean hasNoisePostProcessor() {
        return !this.noisePostProcessors.isEmpty();
    }
    
    /**
     * Samples density for noise post processor.
     * 
     * @param noise Base density.
     * @param noiseX x-coordinate in absolute noise coordinates.
     * @param noiseY y-coordinate in absolute noise coordinates.
     * @param noiseZ z-coordinate in absolute noise coordinates.
     *
     * @return Modified noise density.
     */
    protected double sampleNoisePostProcessor(double noise, int noiseX, int noiseY, int noiseZ) {
        RandomState noiseConfig = this.noiseConfig.get();
        if (!this.hasNoisePostProcessor() || noiseConfig == null) {
            return noise;
        }

        for (NoisePostProcessor noisePostProcessor : this.noisePostProcessors) {
            noise = noisePostProcessor.sample(noise, noiseX, noiseY, noiseZ, noiseConfig, this.generatorSettings.value(), this.chunkSettings);
        }
        return noise;
    }

    /**
     * Calculate a noise offset for generating islands.
     * 
     * @param noiseX
     * @param noiseZ
     * 
     * @return A noise addition.
     */
    protected double getIslandOffset(int noiseX, int noiseZ) {
        if (!this.islesProperties.useIslands()) {
            return 0.0;
        }
        
        Function<Integer, Integer> toNoiseCoord = chunkCoord -> chunkCoord * this.noiseSizeX;
        IslandShape islandShape = this.islesProperties.centerIslandShape();
        
        double distance = islandShape.getDistance(noiseX, noiseZ);
        double oceanSlideTarget = this.islesProperties.oceanSlideTarget();

        int centerIslandRadius = toNoiseCoord.apply(this.islesProperties.centerIslandRadius());
        int centerIslandFalloffDistance = toNoiseCoord.apply(this.islesProperties.centerIslandFalloffDistance());

        int centerOceanRadius = toNoiseCoord.apply(this.islesProperties.centerOceanRadius());
        int centerOceanFalloffDistance = toNoiseCoord.apply(this.islesProperties.centerOceanFalloffDistance());
        
        double outerIslandNoiseScale = this.islesProperties.outerIslandNoiseScale();
        double outerIslandNoiseOffset = this.islesProperties.outerIslandNoiseOffset();
        
        double islandDelta = (distance - centerIslandRadius) / centerIslandFalloffDistance;
        double islandOffset = VersionCompat.clampedLerp(0.0, oceanSlideTarget, islandDelta);
            
        if (this.islesProperties.useOuterIslands() && distance > centerOceanRadius) {
            double islandAddition = this.islandNoise.sample(
                noiseX / outerIslandNoiseScale,
                noiseZ / outerIslandNoiseScale,
                1.0,
                1.0
            ) + outerIslandNoiseOffset;
            
            // 0.885539 = Simplex upper range, but scale a little higher to ensure island centers have untouched terrain.
            islandAddition /= 0.8F;
            islandAddition = Mth.clamp(islandAddition, 0.0F, 1.0F);
            
            // Interpolate noise addition so there isn't a sharp cutoff at start of ocean ring edge.
            double oceanDelta = (distance - centerOceanRadius) / centerOceanFalloffDistance;
            islandAddition = VersionCompat.clampedLerp(0.0F, islandAddition, oceanDelta);
            
            islandOffset += islandAddition * -oceanSlideTarget;
            islandOffset = Mth.clamp(islandOffset, oceanSlideTarget, 0.0F);
        }
        
        return islandOffset;
    }

    /**
     * Interpolate density to set terrain curve at top and bottom of world.
     * 
     * @param density Base density.
     * @param noiseY y-coordinate in noise coordinates from [0, noiseSizeY]
     * 
     * @return Modified noise density.
     */
    protected double applySlides(double density, int noiseY) {
        if (this.noiseSlide.topSize() > 0) {
            double delta = ((double)(this.noiseSizeY - noiseY) - this.noiseSlide.topOffset()) / this.noiseSlide.topSize();
            density = VersionCompat.clampedLerp(this.noiseSlide.topTarget(), density, delta);
        }
        
        if (this.noiseSlide.bottomSize() > 0) {
            double delta = ((double)noiseY - this.noiseSlide.bottomOffset()) / this.noiseSlide.bottomSize();
            density = VersionCompat.clampedLerp(this.noiseSlide.bottomTarget(), density, delta);
        }
        
        return density;
    }
    
    /**
     * Schedules fluid tick for aquifer sampler, so water flows when generated.
     * 
     * @param chunk 
     * @param aquiferSampler
     * @param pos BlockPos in block coordinates.
     * @param blockState Blockstate at pos.
     */
    protected void scheduleFluidTick(ChunkAccess chunk, Aquifer aquiferSampler, BlockPos pos, BlockState blockState) {
        if (aquiferSampler.shouldScheduleFluidUpdate() && !blockState.getFluidState().isEmpty()) {
            chunk.markPosForPostprocessing(pos);
        }
    }

    /**
     * Gets heightmap for given set of chunk coordinates.
     *
     * @param level
     * @param chunkX
     * @param chunkZ
     * @return Heightmap for chunk.
     */
    protected ChunkHeightmap getChunkHeightmap(LevelHeightAccessor level, int chunkX, int chunkZ) {
        return this.chunkCacheHeightmap.get(level, chunkX, chunkZ);
    }

    /**
     * Check if a block state is valid for extra surface features to be placed over.
     * The condition is that the block is opaque and it is not the base block (typically stone).
     *
     * @param blockState The block state to check.
     *
     * @return If the block state is valid for placing extra surface features over.
     */
    protected boolean isBlockSuitableForSurface(BlockState blockState) {
        return blockState.canOcclude() && !blockState.is(this.defaultBlock.getBlock());
    }

    /**
     * Generates the base terrain for a given chunk.
     * 
     * @param chunk
     * @param structureAccessor Collects structures within the chunk, so that terrain can be modified to accommodate them.
     * @param noiseConfig NoiseConfig
     */
    private void generateTerrain(ChunkAccess chunk, StructureManager structureAccessor, RandomState noiseConfig, int minimumCellY, int cellHeight) {
        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x();
        int chunkZ = chunkPos.z();
        int startX = chunkPos.getMinBlockX();
        int startZ = chunkPos.getMinBlockZ();
        
        Heightmap heightmapOcean = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
        Heightmap heightmapSurface = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
        
        Beardifier structureWeightSampler = Beardifier.forStructuresInChunk(structureAccessor, chunkPos);
        Aquifer aquiferSampler = this.getAquiferSampler(chunk, noiseConfig);
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        
        // Create and populate noise provider
        NoiseProvider noiseProvider = this.chunkCacheNoise.get(chunkX, chunkZ);
        NoiseSampler noiseSampler = noiseProvider.getSampler();
        BlockSource baseBlockSource = this.getBaseBlockSource(
            noiseSampler,
            structureWeightSampler,
            aquiferSampler
        );

        // Create and populate block sources
        BlockSourceRules.Builder builder = new BlockSourceRules.Builder().add(baseBlockSource);
        this.blockSources.forEach(builder::add);

        BlockSourceRules blockSources = builder.build(this.defaultBlock);

        for (int subChunkX = 0; subChunkX < this.noiseSizeX; ++subChunkX) {
            for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; ++subChunkZ) {
                int sections = chunk.getSectionsCount() - 1;
                LevelChunkSection section = chunk.getSection(sections);
                
                for (int subChunkY = cellHeight - 1; subChunkY >= 0; --subChunkY) {
                    noiseSampler.sampleNoiseCorners(subChunkX, subChunkY, subChunkZ);

                    for (int subY = this.noiseResolutionVertical - 1; subY >= 0; --subY) {
                        int y = subY + (subChunkY + minimumCellY) * this.noiseResolutionVertical;
                        int localY = y & 0xF;

                        if (y < worldMinY)
                            continue;
                        
                        int sectionNdx = chunk.getSectionIndex(y);
                        if (sections != sectionNdx) {
                            sections = sectionNdx;
                            section = chunk.getSection(sectionNdx);
                        }
                        
                        double deltaY = subY / (double)this.noiseResolutionVertical;
                        noiseSampler.sampleNoiseY(deltaY);
                        
                        for (int subX = 0; subX < this.noiseResolutionHorizontal; ++subX) {
                            int localX = subX + subChunkX * this.noiseResolutionHorizontal;
                            int x = startX + localX;
                            
                            double deltaX = subX / (double)this.noiseResolutionHorizontal;
                            noiseSampler.sampleNoiseX(deltaX);
                            
                            for (int subZ = 0; subZ < this.noiseResolutionHorizontal; ++subZ) {
                                int localZ = subZ + subChunkZ * this.noiseResolutionHorizontal;
                                int z = startZ + localZ;
                                
                                double deltaZ = subZ / (double)this.noiseResolutionHorizontal;
                                noiseSampler.sampleNoiseZ(deltaZ);
                                
                                BlockState blockState = blockSources.apply(x, y, z);
                                if (blockState.equals(BlockStates.AIR)) continue;
                                
                                section.setBlockState(localX, localY, localZ, blockState, false);

                                heightmapOcean.update(localX, y, localZ, blockState);
                                heightmapSurface.update(localX, y, localZ, blockState);
                                
                                this.scheduleFluidTick(chunk, aquiferSampler, mutable.set(x, y, z), blockState);
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Generates a heightmap for the chunk containing the given x/z coordinates
     * and returns to {@link ChunkProvider#getHeight(LevelHeightAccessor, int, int, Heightmap.Types)}
     * to cache and return the height.
     *
     * @param level  a level context to clamp heights to.
     * @param chunkX x-coordinate in chunk coordinates to sample all y-values for.
     * @param chunkZ z-coordinate in chunk coordinates to sample all y-values for.
     * 
     * @return A HeightmapChunk, containing an array of ints containing the heights for the entire chunk.
     */
    private ChunkHeightmap sampleHeightmap(LevelHeightAccessor level, int chunkX, int chunkZ) {
        NoiseSettings noiseSettings = this.getNoiseSettings();

        if (level != null)
            noiseSettings = noiseSettings.clampToHeightAccessor(level);

        short minHeight = 32;
        short worldMinY = (short) noiseSettings.minY();
        short worldTopY = (short) (noiseSettings.height() + worldMinY);

        int minimumCellY = Mth.floorDiv(worldMinY, noiseSettings.getCellHeight());
        int cellHeight = Mth.floorDiv(noiseSettings.height(), noiseSettings.getCellHeight());
        int seaLevel = this.getSeaLevel();

        NoiseProviderBase noiseProvider = this.chunkCacheNoise.get(chunkX, chunkZ);
        NoiseSampler noiseSampler = noiseProvider.getSamplerForHeightmap();

        short[] heightmapSurface = new short[256];
        short[] heightmapOcean = new short[256];
        short[] heightmapSurfaceFloor = new short[256];
        
        Arrays.fill(heightmapSurface, minHeight);
        Arrays.fill(heightmapOcean, minHeight);
        Arrays.fill(heightmapSurfaceFloor, worldMinY);
        
        for (int subChunkX = 0; subChunkX < this.noiseSizeX; ++subChunkX) {
            for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; ++subChunkZ) {
                for (int subChunkY = 0; subChunkY < cellHeight; ++subChunkY) {
                    noiseSampler.sampleNoiseCorners(subChunkX, subChunkY, subChunkZ);
                    
                    for (int subY = 0; subY < this.noiseResolutionVertical; ++subY) {
                        int y = subY + (subChunkY + minimumCellY) * this.noiseResolutionVertical;
                        
                        double deltaY = subY / (double)this.noiseResolutionVertical;
                        noiseSampler.sampleNoiseY(deltaY);
                        
                        for (int subX = 0; subX < this.noiseResolutionHorizontal; ++subX) {
                            int x = subX + subChunkX * this.noiseResolutionHorizontal;
                            
                            double deltaX = subX / (double)this.noiseResolutionHorizontal;
                            noiseSampler.sampleNoiseX(deltaX);
                            
                            for (int subZ = 0; subZ < this.noiseResolutionHorizontal; ++subZ) {
                                int z = subZ + subChunkZ * this.noiseResolutionHorizontal;
                                
                                double deltaZ = subZ / (double)this.noiseResolutionHorizontal;
                                noiseSampler.sampleNoiseZ(deltaZ);
                                
                                double density = noiseSampler.sample();
                                boolean isSolid = density > 0.0;
                                
                                short height = (short)(y + 1);
                                int ndx = z + x * 16;
                                
                                // Capture topmost solid/fluid block height.
                                if (y < seaLevel || isSolid) {
                                    heightmapOcean[ndx] = height;
                                }
                                
                                // Capture topmost solid block height.
                                if (isSolid) {
                                    heightmapSurface[ndx] = height;
                                }
                                
                                // Capture lowest solid block height.
                                // First, set max world height as flag when hitting first solid layer
                                // then set the actual height value when hitting first non-solid layer.
                                // This handles situations where the bottom of the world may not be solid,
                                // i.e. Skylands-style world types.
                                if (isSolid && heightmapSurfaceFloor[ndx] == worldMinY) {
                                    heightmapSurfaceFloor[ndx] = worldTopY;
                                }
                                
                                if (!isSolid && heightmapSurfaceFloor[ndx] == worldTopY) {
                                    heightmapSurfaceFloor[ndx] = (short)(height - 1);
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Construct new heightmap cache from generated heightmap array
        return new ChunkHeightmap(heightmapSurface, heightmapOcean, heightmapSurfaceFloor);
    }
    
    /**
     * Creates block source to sample BlockState at block coordinates given base noise provider.
     * 
     * @param noiseSampler Primary noise sampler to sample density noise.
     * @param weightSampler Sampler used to add/subtract density if a structure start is at coordinate.
     * @param aquiferSampler Sampler used to adjust local water levels for noise caves.
     *
     * @return BlockSource to sample blockstate at x/y/z block coordinates.
     */
    private BlockSource getBaseBlockSource(
        NoiseSampler noiseSampler,
        Beardifier weightSampler,
        Aquifer aquiferSampler
    ) {
        SimpleNoisePos noisePos = new SimpleNoisePos();
        return (x, y, z) -> {
            if (!worldBorderLocation.containsPoint(x, z)) {
                return switch (worldBorderLocation.falloffType()) {
                    case OCEAN, SMOOTH_OCEAN ->
                        y < worldBorderLocation.groundLevel() ? this.defaultBlock
                            : y < getSeaLevel() ? this.defaultFluid
                            : BlockStates.AIR;
                    default -> BlockStates.AIR;
                };
            }

            double density = noiseSampler.sample();
            double clampedDensity = Mth.clamp(density / 200.0, -1.0, 1.0);
            
            clampedDensity = clampedDensity / 2.0 - clampedDensity * clampedDensity * clampedDensity / 24.0;
            clampedDensity += weightSampler.compute(noisePos.set(x, y, z));

            return aquiferSampler.computeSubstance(noisePos, clampedDensity);
        };
    }

    protected boolean isDensityModified() {
        return this.worldBorderLocation.affectsDensity();
    }

    protected double modifyEdgeDensity(double density, double x, double y, double z) {
        int worldX = (int)(x * this.noiseResolutionHorizontal);
        int worldZ = (int)(z * this.noiseResolutionHorizontal);
        density = this.worldBorderLocation.modifyDensity(density, worldX, worldZ);
        return density;
    }

    private NoiseSettings getNoiseSettings() {
        return this.generatorSettings.value().noiseSettings();
    }
}

