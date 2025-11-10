package mod.bluestaggo.modernerbeta.level.chunk.provider;

import mod.bluestaggo.modernerbeta.api.level.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.Clime;
import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProviderForcedHeight;
import mod.bluestaggo.modernerbeta.api.level.chunk.surface.SurfaceBlocks;
import mod.bluestaggo.modernerbeta.api.level.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.api.level.spawn.SpawnLocator;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.Noise3DSettings;
import mod.bluestaggo.modernerbeta.settings.component.NoiseLandmass;
import mod.bluestaggo.modernerbeta.settings.component.SurfaceProperties;
import mod.bluestaggo.modernerbeta.util.BlockStates;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkHeightmap;
import mod.bluestaggo.modernerbeta.util.noise.PerlinOctaveNoise;
import mod.bluestaggo.modernerbeta.util.noise.SimpleNoisePos;
import mod.bluestaggo.modernerbeta.util.noise.SimplexOctaveNoise;
import mod.bluestaggo.modernerbeta.util.random.mersenne.MTRandom;
import mod.bluestaggo.modernerbeta.level.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.biome.provider.BiomeProviderBeta;
import mod.bluestaggo.modernerbeta.level.biome.provider.BiomeProviderPE;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaChunkGenerator;
import mod.bluestaggo.modernerbeta.level.spawn.SpawnLocatorBeta;
import mod.bluestaggo.modernerbeta.level.spawn.SpawnLocatorRelease;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
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

public class ChunkProviderNoise3D extends ChunkProviderForcedHeight {
    private final Noise3DSettings noise3DSettings;
    private final NoiseLandmass noiseLandmass;
    private final SurfaceProperties surfaceProperties;
    private final boolean forcedBiomeHeightEnabled;

    private final PerlinOctaveNoise minLimitOctaveNoise;
    private final PerlinOctaveNoise maxLimitOctaveNoise;
    private final PerlinOctaveNoise mainOctaveNoise;
    private final PerlinOctaveNoise beachOctaveNoise;
    private final PerlinOctaveNoise surfacePerlinOctaveNoise;
    private final SimplexOctaveNoise surfaceSimplexOctaveNoise;
    private final PerlinOctaveNoise scaleOctaveNoise;
    private final PerlinOctaveNoise depthOctaveNoise;
    private final PerlinOctaveNoise forestOctaveNoise;

    private final ClimateSampler climateSampler;

    public ChunkProviderNoise3D(ModernBetaChunkGenerator chunkGenerator, long seed) {
        super(chunkGenerator, seed);

        this.noise3DSettings = this.getChunkSettings().getOrDefault(SettingsComponentTypes.NOISE_3D_SETTINGS);
        this.noiseLandmass = this.getChunkSettings().getOrDefault(SettingsComponentTypes.NOISE_LANDMASS);
        this.surfaceProperties = this.getChunkSettings().getOrDefault(SettingsComponentTypes.SURFACE_PROPERTIES);
        this.forcedBiomeHeightEnabled = this.getChunkSettings().getOrDefault(SettingsComponentTypes.FORCED_BIOME_HEIGHT).enabled();

        this.minLimitOctaveNoise = new PerlinOctaveNoise(this.random, 16, noise3DSettings.randomNoiseOffsets());
        this.maxLimitOctaveNoise = new PerlinOctaveNoise(this.random, 16, noise3DSettings.randomNoiseOffsets());
        this.mainOctaveNoise = new PerlinOctaveNoise(this.random, 8, noise3DSettings.randomNoiseOffsets());

        this.beachOctaveNoise = surfaceProperties.surfaceBeaches()
            ? new PerlinOctaveNoise(this.random, 4, noise3DSettings.randomNoiseOffsets())
            : null;

        if (noise3DSettings.simplexSurfaceNoise()) {
            this.surfacePerlinOctaveNoise = null;
            this.surfaceSimplexOctaveNoise = new SimplexOctaveNoise(this.random, 4);
        } else {
            this.surfacePerlinOctaveNoise = new PerlinOctaveNoise(this.random, 4, noise3DSettings.randomNoiseOffsets());
            this.surfaceSimplexOctaveNoise = null;
        }

        if (noiseLandmass.enabled()) {
            this.scaleOctaveNoise = new PerlinOctaveNoise(this.random, 10, noise3DSettings.randomNoiseOffsets());
            this.depthOctaveNoise = new PerlinOctaveNoise(this.random, 16, noise3DSettings.randomNoiseOffsets());
        } else {
            this.scaleOctaveNoise = null;
            this.depthOctaveNoise = null;
            new PerlinOctaveNoise(this.random, noiseScale.forestNoiseOctaves(), noise3DSettings.randomNoiseOffsets());
        }

        this.forestOctaveNoise = new PerlinOctaveNoise(this.random, noiseScale.forestNoiseOctaves(), noise3DSettings.randomNoiseOffsets());

        this.climateSampler = !this.noise3DSettings.climateHeightScaling() ? null
            : (this.chunkGenerator.getBiomeSource() instanceof ModernBetaBiomeSource biomeSource
                    && biomeSource.getBiomeProvider() instanceof ClimateSampler climateSampler
            ) ? climateSampler
            : this.noise3DSettings.pocketEditionRng() ? new BiomeProviderPE(ModernBetaSettings.empty(), null, seed)
            : new BiomeProviderBeta(ModernBetaSettings.empty(), null, seed);
    }
    
    @Override
    public SpawnLocator getSpawnLocator() {
        if (this.beachOctaveNoise != null) {
            return new SpawnLocatorBeta(this, this.beachOctaveNoise, this.createRandom(this.seed));
        }
        return new SpawnLocatorRelease(this, this.createRandom(this.seed));
    }

    @Override
    public void provideSurface(WorldGenRegion region, StructureManager structureAccessor, ChunkAccess chunk, ModernBetaBiomeSource biomeSource, RandomState noiseConfig) {
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

        boolean wrapped = this.noise3DSettings.wrapped();
        boolean alphaSampling = this.noise3DSettings.alphaNoiseSampling();

        float sandScale = surfaceProperties.sandBeachScale();
        float gravelScale = surfaceProperties.gravelBeachScale();
        float surfaceScale = surfaceProperties.surfaceNoiseScale();

        double[] sandNoise = surfaceProperties.surfaceBeaches() && noise3DSettings.arraySurfaceNoise() ? beachOctaveNoise.sampleArray(
            chunkX * 16, chunkZ * 16, 0.0D,
            16, 16, 1,
            sandScale, sandScale, 1.0D,
            wrapped, alphaSampling
        ) : null;

        double[] gravelNoise = surfaceProperties.surfaceBeaches() && noise3DSettings.arraySurfaceNoise() ? beachOctaveNoise.sampleArray(
            chunkX * 16, 109.0134D, chunkZ * 16,
            16, 1, 16,
            gravelScale, 1.0D, gravelScale,
            wrapped, alphaSampling
        ) : null;

        double[] surfaceNoise = surfacePerlinOctaveNoise != null && noise3DSettings.arraySurfaceNoise() ? surfacePerlinOctaveNoise.sampleArray(
            chunkX * 16, chunkZ * 16, 0.0D,
            16, 16, 1,
            surfaceScale, surfaceScale, surfaceScale,
            wrapped, alphaSampling
        ) : null;

        for (int localZ = 0; localZ < 16; localZ++) {
            for (int localX = 0; localX < 16; localX++) {
                int x = startX + localX;
                int z = startZ + localZ;
                int surfaceTopY = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG).getFirstAvailable(localX, localZ) - 1;
                int surfaceMinY = heightmapChunk != null ?
                    heightmapChunk.getHeight(x, z, ChunkHeightmap.Type.SURFACE_FLOOR) - 8 :
                    this.worldMinY;

                int noiseCoord = this.surfaceProperties.flipNoiseCoordinates()
                    ? localX + localZ * 16
                    : localZ + localX * 16;

                double sandValue = sandNoise != null ? sandNoise[noiseCoord]
                    : beachOctaveNoise != null ? beachOctaveNoise.sample(
                        x * sandScale,
                        z * sandScale,
                        0.0D
                    )
                    : Double.NEGATIVE_INFINITY;

                double gravelValue = gravelNoise != null ? gravelNoise[noiseCoord]
                    : beachOctaveNoise != null ? beachOctaveNoise.sample(
                        z * gravelScale,
                        109.0134,
                        x * gravelScale
                    )
                    : Double.NEGATIVE_INFINITY;

                boolean genSandBeach = sandValue + rand.nextDouble() * 0.2D > 0.0D;
                boolean genGravelBeach = gravelValue + rand.nextDouble() * 0.2D > 3.0D;

                double surfaceSample = surfaceNoise != null
                    ? surfaceNoise[noiseCoord]
                    : surfaceSimplexOctaveNoise != null ? surfaceSimplexOctaveNoise.sample(x, z, surfaceScale, 1.0D)
                    : surfacePerlinOctaveNoise != null ? surfacePerlinOctaveNoise.sample(x, z, surfaceScale)
                    : 0.0D;
                int surfaceDepth = (int) (surfaceSample / 3D + 3D + rand.nextDouble() * 0.25D);

                if (!this.surfaceProperties.erosion() && surfaceDepth < 1) {
                    surfaceDepth = 1;
                }

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
                    int bedrockOffset = this.surfaceProperties.bedrockHoles()
                        ? rand.nextInt(6) - 1
                        : rand.nextInt(5);
                    if (y <= this.bedrockFloor + bedrockOffset) {
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
                            BlockState fluidBlock = aquiferSampler.computeSubstance(noisePos.set(x, y, z), 0.0);

                            boolean isAir = fluidBlock == null;
                            topBlock = isAir ? BlockStates.AIR : fluidBlock;

                            this.scheduleFluidTick(chunk, aquiferSampler, pos, topBlock);
                        }

                        if (y >= this.seaLevel - 1 || (y < this.seaLevel - 1 && chunk.getBlockState(pos.above()).isAir())) {
                            blockState = topBlock;
                        } else if (surfaceProperties.gravelOceanBed() && y < this.seaLevel - 7 - surfaceDepth) {
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
    public void provideSurfaceExtra(WorldGenRegion region, StructureManager structureAccessor, ChunkAccess chunk, ModernBetaBiomeSource biomeSource, RandomState noiseConfig) {
        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;

        int startX = chunk.getPos().getMinBlockX();
        int startZ = chunk.getPos().getMinBlockZ();

        Random rand = this.createSurfaceRandom(chunkX, chunkZ);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        ChunkHeightmap heightmapChunk = this.hasNoisePostProcessor() ? this.getChunkHeightmap(region, chunkX, chunkZ) : null;

        boolean wrapped = this.noise3DSettings.wrapped();
        boolean alphaSampling = this.noise3DSettings.alphaNoiseSampling();

        float sandScale = surfaceProperties.sandBeachScale();
        float gravelScale = surfaceProperties.gravelBeachScale();
        float surfaceScale = surfaceProperties.surfaceNoiseScale();

        double[] sandNoise = surfaceProperties.surfaceBeaches() && noise3DSettings.arraySurfaceNoise() ? beachOctaveNoise.sampleArray(
            chunkX * 16, chunkZ * 16, 0.0D,
            16, 16, 1,
            sandScale, sandScale, 1.0D,
            wrapped, alphaSampling
        ) : null;

        double[] gravelNoise = surfaceProperties.surfaceBeaches() && noise3DSettings.arraySurfaceNoise() ? beachOctaveNoise.sampleArray(
            chunkX * 16, 109.0134D, chunkZ * 16,
            16, 1, 16,
            gravelScale, 1.0D, gravelScale,
            wrapped, alphaSampling
        ) : null;

        double[] surfaceNoise = surfacePerlinOctaveNoise != null && noise3DSettings.arraySurfaceNoise() ? surfacePerlinOctaveNoise.sampleArray(
            chunkX * 16, chunkZ * 16, 0.0D,
            16, 16, 1,
            surfaceScale, surfaceScale, surfaceScale,
            wrapped, alphaSampling
        ) : null;

        for (int localZ = 0; localZ < 16; localZ++) {
            for (int localX = 0; localX < 16; localX++) {
                pos.set(localX, 0, localZ);

                int x = startX + localX;
                int z = startZ + localZ;
                int surfaceTopY = heightmapChunk != null ?
                    heightmapChunk.getHeight(x, z, ChunkHeightmap.Type.SURFACE_FLOOR) :
                    chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG).getFirstAvailable(localX, localZ);
                surfaceTopY--;

                int noiseCoord = this.surfaceProperties.flipNoiseCoordinates()
                    ? localX + localZ * 16
                    : localZ + localX * 16;

                double sandValue = sandNoise != null ? sandNoise[noiseCoord]
                    : beachOctaveNoise != null ? beachOctaveNoise.sample(
                    x * sandScale,
                    z * sandScale,
                    0.0D
                )
                    : Double.NEGATIVE_INFINITY;

                double gravelValue = gravelNoise != null ? gravelNoise[noiseCoord]
                    : beachOctaveNoise != null ? beachOctaveNoise.sample(
                    z * gravelScale,
                    109.0134,
                    x * gravelScale
                )
                    : Double.NEGATIVE_INFINITY;

                boolean genSandBeach = sandValue + rand.nextDouble() * 0.2D > 0.0D;
                boolean genGravelBeach = gravelValue + rand.nextDouble() * 0.2D > 3.0D;

                double surfaceSample = !surfaceProperties.erosion() ? 1.0D
                    : surfaceNoise != null ? surfaceNoise[noiseCoord]
                    : surfaceSimplexOctaveNoise != null ? surfaceSimplexOctaveNoise.sample(x, z, surfaceScale, 1.0D)
                    : surfacePerlinOctaveNoise != null ? surfacePerlinOctaveNoise.sample(x, z, surfaceScale)
                    : 0.0D;
                int surfaceDepth = (int) (surfaceSample / 3D + 3D + rand.nextDouble() * 0.25D);

                Holder<Biome> biome = biomeSource.getBiomeForSurfaceGen(region, pos.set(x, surfaceTopY, z));
                SurfaceConfig surfaceConfig = this.surfaceBuilder.getSurfaceConfig(biome);

                int y = surfaceTopY;
                pos.setY(y);

                if (!this.isBlockSuitableForSurface(chunk.getBlockState(pos))) {
                    continue;
                }

                if (surfaceDepth <= 0) {
                    VersionCompat.setBlockState(chunk, pos, y < this.seaLevel ? this.defaultBlock : BlockStates.AIR);
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

        boolean wrapped = !this.noise3DSettings.farlands();
        boolean oldInfdev = this.noise3DSettings.oldInfdevTerrainNoise();
        boolean landmass = this.noiseLandmass.enabled();

        double scale = this.scaleOctaveNoise != null
            ? (
                (this.noise3DSettings.alphaNoiseSampling()
                    ? this.scaleOctaveNoise.sample(noiseX, 0, noiseZ, this.noiseLandmass.variationScale(), 0.0D, this.noiseLandmass.variationScale(), wrapped)
                    : this.scaleOctaveNoise.sampleXZ(noiseX, noiseZ, this.noiseLandmass.variationScale(), this.noiseLandmass.variationScale(), wrapped))
                + 256D) / 512D
            : 1.0D;
        double depth = this.depthOctaveNoise != null
            ? this.noise3DSettings.alphaNoiseSampling()
                ? this.depthOctaveNoise.sample(noiseX, 0, noiseZ, depthNoiseScaleX, 0.0D, depthNoiseScaleZ, wrapped)
                : this.depthOctaveNoise.sampleXZ(noiseX, noiseZ, depthNoiseScaleX, depthNoiseScaleZ, wrapped)
            : 0.0D;

        if (landmass) {
            depth /= 8000D;

            if (depth < 0.0D) {
                depth = -depth * this.noiseLandmass.negativeDepthInfluence();
            }

            depth = depth * this.noiseLandmass.depthStretch() + this.noiseLandmass.depthOffset();

            if (depth < 0.0D) {
                depth = Math.max(
                    depth / this.noiseLandmass.negativeDepthDampening(),
                    this.noiseLandmass.minDepth()
                );
                if (this.noiseLandmass.negativeDepthFlattening()) {
                    scale = 0.0D;
                }
            } else {
                depth = Math.min(
                    depth / this.noiseLandmass.positiveDepthDampening(),
                    this.noiseLandmass.maxDepth()
                );
            }
        }

        double modDepth = 0.0D;

        if (this.forcedBiomeHeightEnabled) {
            HeightConfig heightConfig = this.getHeightConfigAt(noiseX, noiseZ);
            scale = heightConfig.scale();
            modDepth = heightConfig.depth();
        }

        if (this.noise3DSettings.climateHeightScaling()) {
            int horizNoiseResolution = 16 / (this.noiseSizeX + 1);
            int climeX = (startNoiseX / this.noiseSizeX * 16) + localNoiseX * horizNoiseResolution + horizNoiseResolution / 2;
            int climeZ = (startNoiseZ / this.noiseSizeZ * 16) + localNoiseZ * horizNoiseResolution + horizNoiseResolution / 2;

            Clime clime = this.climateSampler.sample(climeX, climeZ);

            double temp = clime.temp();
            double rain = clime.rain() * temp;

            rain = 1.0D - rain;
            rain *= rain;
            rain *= rain;
            rain = 1.0D - rain;

            scale *= rain;
        }

        if (scale < 0.0D && !this.noise3DSettings.monoliths()) {
            scale = 0.0D;
        }

        if (!this.forcedBiomeHeightEnabled && landmass) {
            scale = Math.min(scale, 1.0D) + 0.5D;
        }

        depth = modDepth + depth * this.noiseLandmass.depthInfluence();
        depth *= baseSize / 8.0D;
        depth = baseSize + depth * 4.0D;

        for (int y = 0; y < primaryBuffer.length; ++y) {
            int noiseY = y + this.noiseMinY;

            double density;
            double heightmapDensity;

            double densityOffset = this.getOffset(noiseY, heightStretch, depth, scale);

            double mainNoise = this.mainOctaveNoise.sample(
                noiseX, noiseY, noiseZ,
                coordinateScale / mainNoiseScaleX,
                heightScale / mainNoiseScaleY,
                coordinateScale / mainNoiseScaleZ,
                wrapped,
                oldInfdev
            ) / this.noiseScale.limitBlending();
            if (!oldInfdev) {
                mainNoise += 1.0D;
            }
            mainNoise /= 2D;

            if (mainNoise < (oldInfdev ? -1.0D : 0.0D)) {
                density = this.minLimitOctaveNoise.sample(
                    noiseX, noiseY, noiseZ,
                    coordinateScale,
                    heightScale,
                    coordinateScale,
                    wrapped,
                    oldInfdev
                ) / lowerLimitScale;

                density -= densityOffset;
                density += islandOffset;

                if (oldInfdev) {
                    density = Mth.clamp(density, -10.0D, 10.0D);
                }
            } else if (mainNoise > 1.0D) {
                density = this.maxLimitOctaveNoise.sample(
                    noiseX, noiseY, noiseZ,
                    coordinateScale,
                    heightScale,
                    coordinateScale,
                    wrapped,
                    oldInfdev
                ) / upperLimitScale;

                density -= densityOffset;
                density += islandOffset;

                if (oldInfdev) {
                    density = Mth.clamp(density, -10.0D, 10.0D);
                }
            } else {
                double minLimitNoise = this.minLimitOctaveNoise.sample(
                    noiseX, noiseY, noiseZ,
                    coordinateScale,
                    heightScale,
                    coordinateScale,
                    wrapped,
                    oldInfdev
                ) / lowerLimitScale;

                double maxLimitNoise = this.maxLimitOctaveNoise.sample(
                    noiseX, noiseY, noiseZ,
                    coordinateScale,
                    heightScale,
                    coordinateScale,
                    wrapped,
                    oldInfdev
                ) / upperLimitScale;

                minLimitNoise -= densityOffset;
                maxLimitNoise -= densityOffset;

                minLimitNoise += islandOffset;
                maxLimitNoise += islandOffset;

                double delta = mainNoise;

                if (oldInfdev) {
                    minLimitNoise = Mth.clamp(minLimitNoise, -10.0D, 10.0D);
                    maxLimitNoise = Mth.clamp(maxLimitNoise, -10.0D, 10.0D);
                    delta = (delta + 1.0D) / 2.0D;
                }

                density = minLimitNoise + (maxLimitNoise - minLimitNoise) * delta;
            }

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

    @Override
    protected Random createRandom(long seed) {
        if (this.getChunkSettings().getOrDefault(SettingsComponentTypes.NOISE_3D_SETTINGS).pocketEditionRng()) {
            return new MTRandom(seed);
        }
        return super.createRandom(seed);
    }

    @Override
    protected Random createSurfaceRandom(int chunkX, int chunkZ) {
        if (this.noise3DSettings.pocketEditionRng()) {
            long seed = (long)chunkX * 0x14609048 + (long)chunkZ * 0x7ebe2d5;
            return new MTRandom(seed);
        }
        return super.createSurfaceRandom(chunkX, chunkZ);
    }

    @Override
    protected int getHeightSampleRadius() {
        return this.getChunkSettings().getOrDefault(SettingsComponentTypes.NOISE_3D_SETTINGS).pocketEditionRng() ? 1 : 2;
    }

    @Override
    protected float calculateBiomeHeightWeight(int x, int z) {
        if (this.getChunkSettings().getOrDefault(SettingsComponentTypes.NOISE_3D_SETTINGS).pocketEditionRng()) {
            x = -1; // Weird PE quirk. No idea why Mojang did this.
        }
        return super.calculateBiomeHeightWeight(x, z);
    }

    private double getOffset(int noiseY, double heightStretch, double depth, double scale) {
        double offset = (((double)noiseY - depth) * heightStretch) / scale;

        if (offset < 0D)
            offset *= this.noiseScale.densityUnderdamp();

        return offset;
    }
}
