package mod.bluestaggo.modernerbeta.level.chunk.provider;

import mod.bluestaggo.modernerbeta.api.level.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.Clime;
import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProviderForcedHeight;
import mod.bluestaggo.modernerbeta.api.level.chunk.surface.SurfaceBlocks;
import mod.bluestaggo.modernerbeta.api.level.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.api.level.spawn.SpawnLocator;
import mod.bluestaggo.modernerbeta.level.spawn.SpawnLocatorPE;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.*;
import mod.bluestaggo.modernerbeta.util.BlockStates;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkCache;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkHeightmap;
import mod.bluestaggo.modernerbeta.util.noise.OctaveNoise;
import mod.bluestaggo.modernerbeta.util.noise.PerlinOctaveNoise;
import mod.bluestaggo.modernerbeta.util.noise.SimpleNoisePos;
import mod.bluestaggo.modernerbeta.util.noise.SimplexOctaveNoise;
import mod.bluestaggo.modernerbeta.util.random.BedrockRandomSource;
import mod.bluestaggo.modernerbeta.level.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.biome.provider.BiomeProviderBeta;
import mod.bluestaggo.modernerbeta.level.biome.provider.BiomeProviderPE;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaChunkGenerator;
import mod.bluestaggo.modernerbeta.level.spawn.SpawnLocatorBeta;
import mod.bluestaggo.modernerbeta.level.spawn.SpawnLocatorRelease;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;

public class ChunkProviderNoise3D extends ChunkProviderForcedHeight {
    private final Noise3DSettings noise3DSettings;
    private final NoiseLandmass noiseLandmass;
    private final SurfaceProperties surfaceProperties;
    private final DeepslateGeneration deepslateGeneration;
    private final BlockState deepslateBlock;
    private final boolean forcedBiomeHeightEnabled;

    private final PerlinOctaveNoise minLimitOctaveNoise;
    private final PerlinOctaveNoise maxLimitOctaveNoise;
    private final PerlinOctaveNoise mainOctaveNoise;
    private final PerlinOctaveNoise beachOctaveNoise;
    private final OctaveNoise surfaceOctaveNoise;
    private final PerlinOctaveNoise scaleOctaveNoise;
    private final PerlinOctaveNoise depthOctaveNoise;
    private final PerlinOctaveNoise forestOctaveNoise;

    private final ChunkCache<double[]> surfaceNoiseCache;
    private final ClimateSampler climateSampler;

    public ChunkProviderNoise3D(ModernBetaChunkGenerator chunkGenerator, long seed) {
        super(chunkGenerator, seed);

        PerlinNoiseSettings perlinSettings = this.getChunkSettings().getOrDefault(SettingsComponentTypes.PERLIN_NOISE_SETTINGS);

        this.noise3DSettings = this.getChunkSettings().getOrDefault(SettingsComponentTypes.NOISE_3D_SETTINGS);
        this.noiseLandmass = this.getChunkSettings().getOrDefault(SettingsComponentTypes.NOISE_LANDMASS);
        this.surfaceProperties = this.getChunkSettings().getOrDefault(SettingsComponentTypes.SURFACE_PROPERTIES);
        this.deepslateGeneration = this.getChunkSettings().getOrDefault(SettingsComponentTypes.DEEPSLATE_GENERATION);
        this.deepslateBlock = BuiltInRegistries.BLOCK.getOrThrow(ResourceKey.create(Registries.BLOCK, this.deepslateGeneration.block()))
                //? if >=1.21.2
                .value()
                .defaultBlockState();
        this.forcedBiomeHeightEnabled = this.getChunkSettings().getOrDefault(SettingsComponentTypes.FORCED_BIOME_HEIGHT).enabled();

        this.minLimitOctaveNoise = new PerlinOctaveNoise(this.random, 16, perlinSettings);
        this.maxLimitOctaveNoise = new PerlinOctaveNoise(this.random, 16, perlinSettings);
        this.mainOctaveNoise = new PerlinOctaveNoise(this.random, 8, perlinSettings);

        this.beachOctaveNoise = surfaceProperties.enableBeaches()
            ? new PerlinOctaveNoise(this.random, 4, perlinSettings)
            : null;

        this.surfaceOctaveNoise = noise3DSettings.simplexSurfaceNoise()
            ? new SimplexOctaveNoise(this.random, 4)
            : new PerlinOctaveNoise(this.random, 4, perlinSettings);

        if (noiseLandmass.scale().enabled()) {
            this.scaleOctaveNoise = new PerlinOctaveNoise(this.random, 10, perlinSettings);
        } else {
            this.scaleOctaveNoise = null;
        }

        if (noiseLandmass.depth().enabled()) {
            this.depthOctaveNoise = new PerlinOctaveNoise(this.random, 16, perlinSettings);
        } else {
            this.depthOctaveNoise = null;
        }

        if (!noiseLandmass.scale().enabled() && !noiseLandmass.depth().enabled()) {
            new PerlinOctaveNoise(this.random, noiseScale.forestNoiseOctaves(), perlinSettings);
        }

        this.forestOctaveNoise = new PerlinOctaveNoise(this.random, noiseScale.forestNoiseOctaves(), perlinSettings);

        this.surfaceNoiseCache = new ChunkCache<>("surface_noise", (chunkX, chunkZ) -> {
            float surfaceScale = surfaceProperties.surfaceNoiseScale();

            return !noise3DSettings.simplexSurfaceNoise() && noise3DSettings.arraySurfaceNoise()
                ? surfaceOctaveNoise.sampleArray(
                    chunkX * 16, chunkZ * 16, 0.0D,
                    16, 16, 1,
                    surfaceScale, surfaceScale, surfaceScale
                )
                : null;
        });

        this.climateSampler = !this.noise3DSettings.climateHeightScaling() ? null :
            this.chunkGenerator.getBiomeSource() instanceof ModernBetaBiomeSource biomeSource
            ? biomeSource.getBiomeProvider() instanceof ClimateSampler climateSampler
                ? climateSampler
                : this.noise3DSettings.pocketEditionRng()
                    ? new BiomeProviderPE(biomeSource.getBiomeProvider().getSettings(), null, seed)
                    : new BiomeProviderBeta(biomeSource.getBiomeProvider().getSettings(), null, seed)
            : null;
    }
    
    @Override
    public SpawnLocator getSpawnLocator() {
        //TODO: maybe rewrite this to be more neat?
        if (this.beachOctaveNoise != null) {
            if (this.surfaceProperties.generateBeaches()) {
                if (this.noise3DSettings.pocketEditionRng()) {
                    return new SpawnLocatorPE(this, this.beachOctaveNoise, this.createRandom(this.seed));
                }

                return new SpawnLocatorBeta(this, this.beachOctaveNoise, this.createRandom(this.seed));
            } else {
                return SpawnLocator.DEFAULT;
            }
        }
        return new SpawnLocatorRelease(this, this.createRandom(this.seed));
    }

    @Override
    public void provideSurface(WorldGenRegion region, StructureManager structureAccessor, ChunkAccess chunk, ModernBetaBiomeSource biomeSource, RandomState noiseConfig) {
        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x();
        int chunkZ = chunkPos.z();

        int startX = chunk.getPos().getMinBlockX();
        int startZ = chunk.getPos().getMinBlockZ();

        int seaLevel = this.getSeaLevel();

        RandomSource rand = this.createSurfaceRandom(chunkX, chunkZ);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        Aquifer aquiferSampler = this.getAquiferSampler(chunk, noiseConfig);
        SimpleNoisePos noisePos = new SimpleNoisePos();

        boolean generateBeaches = surfaceProperties.generateBeaches();

        float sandScale = surfaceProperties.sandBeachScale();
        float gravelScale = surfaceProperties.gravelBeachScale();

        boolean initBeachArrays = surfaceProperties.enableBeaches() && generateBeaches && noise3DSettings.arraySurfaceNoise();
        double[] sandNoise = initBeachArrays ? beachOctaveNoise.sampleArray(
            chunkX * 16, chunkZ * 16, 0.0D,
            16, 16, 1,
            sandScale, sandScale, 1.0D
        ) : null;

        double[] gravelNoise = initBeachArrays ? beachOctaveNoise.sampleArray(
            chunkX * 16, 109.0134D, chunkZ * 16,
            16, 1, 16,
            gravelScale, 1.0D, gravelScale
        ) : null;

        for (int localZ = 0; localZ < 16; localZ++) {
            for (int localX = 0; localX < 16; localX++) {
                int x = startX + localX;
                int z = startZ + localZ;
                if (!this.worldBorderLocation.containsPoint(x, z)) {
                    continue;
                }

                int surfaceTopY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, localX, localZ) + 1;

                int noiseCoord = this.surfaceProperties.flipNoiseCoordinates()
                    ? localX + localZ * 16
                    : localZ + localX * 16;

                double sandValue = generateBeaches && sandNoise != null ? sandNoise[noiseCoord]
                    : beachOctaveNoise != null ? beachOctaveNoise.sample(
                        x * sandScale,
                        z * sandScale,
                        0.0D
                    )
                    : Double.NEGATIVE_INFINITY;

                double gravelValue = generateBeaches && gravelNoise != null ? gravelNoise[noiseCoord]
                    : beachOctaveNoise != null ? beachOctaveNoise.sample(
                        z * gravelScale,
                        109.0134,
                        x * gravelScale
                    )
                    : Double.NEGATIVE_INFINITY;

                boolean genSandBeach = generateBeaches && sandValue + rand.nextDouble() * 0.2D > 0.0D;
                boolean genGravelBeach = generateBeaches && gravelValue + rand.nextDouble() * 0.2D > 3.0D;

                int runDepth = -1;

                Holder<Biome> biome = biomeSource.getBiomeForSurfaceGen(region, pos.set(x, surfaceTopY, z));

                SurfaceConfig surfaceConfig = this.surfaceBuilder.getSurfaceConfig(biome);
                BlockState topBlock = surfaceConfig.normal().topBlock();
                BlockState fillerBlock = surfaceConfig.normal().fillerBlock();

                // Generate from top to bottom of world
                for (int y = surfaceTopY; y >= this.worldMinY; y--) {
                    pos.set(localX, y, localZ);
                    BlockState blockAt = chunk.getBlockState(pos);
                    BlockState blockToSet = null;

                    // Place bedrock
                    if (this.surfaceProperties.generateBedrock()) {
                        int bedrockOffset = this.surfaceProperties.uniformBedrock() ? 0 :
                                (this.surfaceProperties.bedrockHoles()
                                    ? rand.nextInt(6) - 1
                                    : rand.nextInt(5));
                        if (y <= this.bedrockFloor + bedrockOffset) {
                            blockToSet = BlockStates.BEDROCK;
                        }
                    }

                    if (blockAt.isAir()) { // Skip if air block
                        runDepth = -1;
                        continue;
                    }

                    if (!blockAt.is(this.defaultBlock.getBlock())) { // Skip if not stone
                        continue;
                    }

                    int surfaceDepth = this.getSurfaceDepth(rand, x, z);

                    // At the first default block
                    if (runDepth == -1) {
                        if (surfaceDepth <= 0) { // Generate stone basin if noise permits
                            topBlock = BlockStates.AIR;
                            fillerBlock = this.defaultBlock;
                        } else if (y >= seaLevel - 4 && y <= seaLevel + 1) { // Generate beaches at this y range
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

                        if (this.surfaceProperties.generateLiquids() && y < seaLevel && topBlock.isAir()) { // Generate water bodies
                            BlockState fluidBlock = aquiferSampler.computeSubstance(noisePos.set(x, y, z), 0.0);

                            boolean isAir = fluidBlock == null;
                            topBlock = isAir ? BlockStates.AIR : fluidBlock;
                        }

                        if (y >= seaLevel - 1 || (y < seaLevel - 1 && chunk.getBlockState(pos.above()).isAir())) {
                            blockToSet = topBlock;
                        } else if (surfaceProperties.gravelOceanBed() && y < seaLevel - 7 - surfaceDepth) {
                            topBlock = BlockStates.AIR;
                            fillerBlock = BlockStates.STONE;
                            blockToSet = BlockStates.GRAVEL;
                        } else {
                            blockToSet = fillerBlock;
                        }
                    } else if (runDepth > 0) {
                        runDepth--;
                        blockToSet = fillerBlock;

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

                    if (blockToSet == null && this.deepslateGeneration.enabled()) {
                        if (y <= this.deepslateGeneration.minY()) {
                            blockToSet = this.deepslateBlock;
                        } else {
                            int minY = this.deepslateGeneration.minY();
                            int maxY = this.deepslateGeneration.maxY();

                            double yThreshold = Mth.lerp(Mth.inverseLerp(y, minY, maxY), 1.0, 0.0);
                            RandomSource random = this.randomFactory.at(x, y, z);

                            blockToSet = (double) random.nextFloat() < yThreshold ? this.deepslateBlock : null;
                        }
                    }

                    if (blockToSet != null) {
                        if (!blockToSet.getFluidState().isEmpty()) {
                            this.scheduleFluidTick(chunk, aquiferSampler, pos, blockToSet);
                        }

                        VersionCompat.setBlockState(chunk, pos, blockToSet);
                    }
                }
            }
        }
    }

    @Override
    public void provideSurfaceExtra(WorldGenRegion region, StructureManager structureAccessor, ChunkAccess chunk, ModernBetaBiomeSource biomeSource, RandomState noiseConfig) {
        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x();
        int chunkZ = chunkPos.z();

        int startX = chunk.getPos().getMinBlockX();
        int startZ = chunk.getPos().getMinBlockZ();

        int seaLevel = this.getSeaLevel();

        RandomSource rand = this.createSurfaceRandom(chunkX, chunkZ);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        ChunkHeightmap heightmapChunk = this.hasNoisePostProcessor() ? this.getChunkHeightmap(region, chunkX, chunkZ) : null;

        boolean generateBeaches = surfaceProperties.generateBeaches();

        float sandScale = surfaceProperties.sandBeachScale();
        float gravelScale = surfaceProperties.gravelBeachScale();

        boolean initBeachArrays = surfaceProperties.enableBeaches() && generateBeaches && noise3DSettings.arraySurfaceNoise();
        double[] sandNoise = initBeachArrays ? beachOctaveNoise.sampleArray(
            chunkX * 16, chunkZ * 16, 0.0D,
            16, 16, 1,
            sandScale, sandScale, 1.0D
        ) : null;

        double[] gravelNoise = initBeachArrays ? beachOctaveNoise.sampleArray(
            chunkX * 16, 109.0134D, chunkZ * 16,
            16, 1, 16,
            gravelScale, 1.0D, gravelScale
        ) : null;

        for (int localZ = 0; localZ < 16; localZ++) {
            for (int localX = 0; localX < 16; localX++) {
                int x = startX + localX;
                int z = startZ + localZ;
                if (!this.worldBorderLocation.containsPoint(x, z)) {
                    continue;
                }

                pos.set(localX, 0, localZ);
                int surfaceTopY = heightmapChunk != null ?
                    heightmapChunk.getHeight(x, z, ChunkHeightmap.Type.SURFACE_FLOOR) :
                    chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG).getFirstAvailable(localX, localZ);
                surfaceTopY--;

                int noiseCoord = this.surfaceProperties.flipNoiseCoordinates()
                    ? localX + localZ * 16
                    : localZ + localX * 16;

                double sandValue = generateBeaches && sandNoise != null ? sandNoise[noiseCoord]
                    : beachOctaveNoise != null ? beachOctaveNoise.sample(
                    x * sandScale,
                    z * sandScale,
                    0.0D
                )
                    : Double.NEGATIVE_INFINITY;

                double gravelValue = generateBeaches && gravelNoise != null ? gravelNoise[noiseCoord]
                    : beachOctaveNoise != null ? beachOctaveNoise.sample(
                    z * gravelScale,
                    109.0134,
                    x * gravelScale
                )
                    : Double.NEGATIVE_INFINITY;

                boolean genSandBeach = generateBeaches && sandValue + rand.nextDouble() * 0.2D > 0.0D;
                boolean genGravelBeach = generateBeaches && gravelValue + rand.nextDouble() * 0.2D > 3.0D;

                double surfaceDepth = this.getSurfaceDepth(rand, x, z);

                Holder<Biome> biome = biomeSource.getBiomeForSurfaceGen(region, pos.set(x, surfaceTopY, z));
                SurfaceConfig surfaceConfig = this.surfaceBuilder.getSurfaceConfig(biome);

                int y = surfaceTopY;
                pos.setY(y);

                if (this.isBlockSuitableForSurface(chunk.getBlockState(pos))) {
                    if (surfaceDepth <= 0) {
                        VersionCompat.setBlockState(chunk, pos, y < seaLevel ? this.defaultBlock : BlockStates.AIR);
                        pos.setY(--y);

                        while (this.isBlockSuitableForSurface(chunk.getBlockState(pos))) {
                            VersionCompat.setBlockState(chunk, pos, this.defaultBlock);
                            pos.setY(--y);
                        }
                    } else if (surfaceTopY >= seaLevel - 4 && surfaceTopY < seaLevel + 1) {
                        SurfaceBlocks beach = genSandBeach ? surfaceConfig.beachSand() : genGravelBeach ? surfaceConfig.beachGravel() : null;
                        if (beach != null) {
                            if (beach.topBlock().isAir() && y < seaLevel) {
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

                if (this.surfaceProperties.generateBedrock()) {
                    if (this.surfaceProperties.uniformBedrock()) {
                        VersionCompat.setBlockState(chunk, pos.atY(this.bedrockFloor), BlockStates.BEDROCK);
                        continue;
                    }

                    for (y = this.bedrockFloor; y < this.bedrockFloor + 5; y++) {
                        int bedrockOffset = this.surfaceProperties.bedrockHoles()
                            ? rand.nextInt(6) - 1
                            : rand.nextInt(5);

                        if (y <= this.bedrockFloor + bedrockOffset) {
                            pos.setY(y);
                            VersionCompat.setBlockState(chunk, pos, BlockStates.BEDROCK);
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets the surface height for the given coordinate
     *
     * @param rand The {@link RandomSource} instance for the height.
     * @param x    The X coordinate to get the height for.
     * @param z    The Z coordinate to get the height for
     * @return The height for the given coordinates.
     */
    @Override
    public int getSurfaceDepth(RandomSource rand, int x, int z) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;

        int localX = x & 15;
        int localZ = z & 15;

        int noiseCoord = this.surfaceProperties.flipNoiseCoordinates()
            ? localX + localZ * 16
            : localZ + localX * 16;

        float surfaceScale = surfaceProperties.surfaceNoiseScale();

        double[] surfaceNoise = this.surfaceNoiseCache.get(chunkX, chunkZ);
        double surfaceSample = surfaceNoise != null
            ? surfaceNoise[noiseCoord]
            : surfaceOctaveNoise.sampleXZ(
            x, z, surfaceScale, surfaceScale, noise3DSettings.simplexSurfaceNoise() ? 1.0D : 0.5D);
        int surfaceDepth = (int) (surfaceSample / 3D + 3D + rand.nextDouble() * 0.25D);

        if (!this.surfaceProperties.erosion() && surfaceDepth < 1) {
            surfaceDepth = 1;
        }

        return surfaceDepth;
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

        boolean oldInfdev = this.noise3DSettings.oldInfdevTerrainNoise();

        double scale = this.scaleOctaveNoise != null && this.noiseLandmass.scale().sample()
            ? (
            (this.noiseLandmass.alphaSampling()
                ? this.scaleOctaveNoise.sample(noiseX, 0, noiseZ, this.noiseLandmass.scale().variation(), 0.0D, this.noiseLandmass.scale().variation())
                : this.scaleOctaveNoise.sampleXZ(noiseX, noiseZ, this.noiseLandmass.scale().variation(), this.noiseLandmass.scale().variation()))
                + 256D) / 512D
            : 1.0D;
        double depth = this.depthOctaveNoise != null && this.noiseLandmass.depth().sample()
            ? this.noiseLandmass.alphaSampling()
            ? this.depthOctaveNoise.sample(noiseX, 0, noiseZ, depthNoiseScaleX, 0.0D, depthNoiseScaleZ)
            : this.depthOctaveNoise.sampleXZ(noiseX, noiseZ, depthNoiseScaleX, depthNoiseScaleZ)
            : 0.0D;

        if (this.noiseLandmass.depth().sample()) {
            depth /= 8000D;

            if (depth < 0.0D) {
                depth = -depth * this.noiseLandmass.depth().negativeInfluence();
            }

            depth = depth * this.noiseLandmass.depth().stretch() + this.noiseLandmass.depth().offset();

            if (depth < 0.0D) {
                depth = Math.max(
                    depth / this.noiseLandmass.depth().negativeDampening(),
                    this.noiseLandmass.depth().minValue()
                );
                if (this.noiseLandmass.depth().negativeFlattening()) {
                    scale = 0.0D;
                }
            } else {
                depth = Math.min(
                    depth / this.noiseLandmass.depth().positiveDampening(),
                    this.noiseLandmass.depth().maxValue()
                );
            }
        }

        double modScale = 0.0D;
        double modDepth = 0.0D;

        if (this.forcedBiomeHeightEnabled) {
            HeightConfig heightConfig = this.getHeightConfigAt(noiseX, noiseZ);
            modScale = heightConfig.scale();
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

        scale = modScale + scale * this.noiseLandmass.scale().influence();
        if (scale < 0.0D && !this.noise3DSettings.monoliths()) {
            scale = 0.0D;
        }

        if (!this.forcedBiomeHeightEnabled && this.noiseLandmass.scale().sample()) {
            scale = Math.min(scale, 1.0D);
        }

        scale += this.noiseLandmass.scale().offset();
        depth = modDepth + depth * this.noiseLandmass.depth().influence();
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
                coordinateScale / mainNoiseScaleZ
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
                    coordinateScale
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
                    coordinateScale
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
                    coordinateScale
                ) / lowerLimitScale;

                double maxLimitNoise = this.maxLimitOctaveNoise.sample(
                    noiseX, noiseY, noiseZ,
                    coordinateScale,
                    heightScale,
                    coordinateScale
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
    protected OctaveNoise getForestOctaveNoise() {
        return this.forestOctaveNoise;
    }

    @Override
    protected RandomSource createRandom(long seed) {
        if (this.getChunkSettings().getOrDefault(SettingsComponentTypes.NOISE_3D_SETTINGS).pocketEditionRng()) {
            return new BedrockRandomSource(seed);
        }
        return super.createRandom(seed);
    }

    @Override
    public RandomSource createSurfaceRandom(int chunkX, int chunkZ) {
        if (this.noise3DSettings.pocketEditionRng()) {
            long seed = (long)chunkX * 0x14609048 + (long)chunkZ * 0x7ebe2d5;
            return new BedrockRandomSource(seed);
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
        if (this.noiseScale.useFixedOffset())
            return this.noiseScale.fixedOffset();

        double offset = this.noise3DSettings.oldInfdevTerrainNoise()
                ? noiseY * this.noiseResolutionVertical - this.getSeaLevel()
                : (((double)noiseY - depth) * heightStretch) / scale;

        if (offset < 0D)
            offset *= this.noiseScale.densityUnderdamp();

        return offset;
    }
}
