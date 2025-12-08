package mod.bluestaggo.modernerbeta.api.level.chunk;

import mod.bluestaggo.modernerbeta.api.level.blocksource.BlockSource;
import mod.bluestaggo.modernerbeta.api.level.chunk.surface.SurfaceBuilder;
import mod.bluestaggo.modernerbeta.api.level.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.api.level.spawn.SpawnLocator;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaChunkGenerator;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaGenerationStep;
import mod.bluestaggo.modernerbeta.level.feature.placement.Infdev325CavePlacementModifier;
import mod.bluestaggo.modernerbeta.level.feature.placement.NoiseBasedCountPlacementModifier;
import mod.bluestaggo.modernerbeta.mixin.ChunkGeneratorAccessor;
import mod.bluestaggo.modernerbeta.mixin.PlacedFeatureAccessor;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.PerlinNoiseSettings;
import mod.bluestaggo.modernerbeta.util.BlockStates;
import mod.bluestaggo.modernerbeta.util.noise.PerlinOctaveNoise;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.FeatureSorter.StepFeatureData;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.Aquifer.FluidPicker;
import net.minecraft.world.level.levelgen.Aquifer.FluidStatus;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public abstract class ChunkProvider {    
    private final FluidPicker defaultFluidLevelSampler;

    protected final ModernBetaChunkGenerator chunkGenerator;
    protected final long seed;

    protected final Holder<NoiseGeneratorSettings> generatorSettings;
    protected final ModernBetaSettings chunkSettings;
    protected final boolean skipCarvers;
    protected final Random random;

    protected final WorldgenRandom.Algorithm randomSource;
    protected final PositionalRandomFactory randomFactory;
    
    protected final List<BlockSource> blockSources;
    protected final SurfaceBuilder surfaceBuilder;
    
    /**
     * Construct a Modern Beta chunk provider with seed and settings.
     * 
     * @param chunkGenerator Parent ModernBetaChunkGenerator object used to initialize fields.
     */
    public ChunkProvider(ModernBetaChunkGenerator chunkGenerator, long seed) {
        this.chunkGenerator = chunkGenerator;
        this.seed = seed;
        
        this.generatorSettings = chunkGenerator.generatorSettings();
        this.chunkSettings = chunkGenerator.getChunkSettings()
            .mapPreset(chunkGenerator.getPresetRegistry(), ModernBetaSettingsPreset::chunkSettings);
        this.random = this.createRandom(this.seed);

        this.defaultFluidLevelSampler = (x, y, z) -> new FluidStatus(this.getSeaLevel(), BlockStates.AIR);
        this.randomSource = chunkGenerator.generatorSettings().value().getRandomSource();
        this.randomFactory = this.randomSource.newInstance(this.seed).forkPositional();
        
        this.blockSources = ModernBetaRegistries.BLOCKSOURCE
            .listElements()
            .map(func -> func.value().apply(this.chunkSettings, this.randomFactory))
            .toList();

        HolderGetter<SurfaceConfig> surfaceConfigGetter = chunkGenerator.getSurfaceConfigRegistry();
        this.surfaceBuilder = new SurfaceBuilder(this.chunkGenerator.getBiomeSource(),
            surfaceConfigGetter instanceof HolderLookup<SurfaceConfig> lookup ? lookup : null);
        this.skipCarvers = !this.chunkSettings.getOrDefault(SettingsComponentTypes.CAVE_GENERATION).useCarvers();
    }
    
    /**
     * Generates base terrain for given chunk and returns it.
     * 
     * @param blender TODO
     * @param structureAccessor
     * @param chunk
     * @param noiseConfig TODO
     * @return A completed chunk.
     */
    public abstract CompletableFuture<ChunkAccess> provideChunk(Blender blender, StructureManager structureAccessor, ChunkAccess chunk, RandomState noiseConfig);
    
    /**
     * Generates biome-specific surface for given chunk.
     * 
     * @param region
     * @param structureAccessor TODO
     * @param chunk
     * @param biomeSource
     * @param noiseConfig TODO
     */
    public abstract void provideSurface(WorldGenRegion region, StructureManager structureAccessor, ChunkAccess chunk, ModernBetaBiomeSource biomeSource, RandomState noiseConfig);

    /**
     * Generates biome-specific surface for given chunk on top of surface rules.
     *
     * @param region
     * @param structureAccessor TODO
     * @param chunk
     * @param biomeSource
     * @param noiseConfig TODO
     */
    public void provideSurfaceExtra(WorldGenRegion region, StructureManager structureAccessor, ChunkAccess chunk, ModernBetaBiomeSource biomeSource, RandomState noiseConfig) {
    }

    /**
     * Sample height at given x/z coordinate. Initially generates heightmap for entire chunk,
     * if chunk containing x/z coordinates has never been sampled.
     *
     * @param level
     * @param x         x-coordinate in block coordinates.
     * @param z         z-coordinate in block coordinates.
     * @param heightmap Vanilla heightmap type.
     * @return The y-coordinate of top block at x/z.
     */
    public abstract int getHeight(LevelHeightAccessor level, int x, int z, Heightmap.Types heightmap);
    
    /**
     * Determines whether to skip the chunk for some chunk generation step, depending on the x/z chunk coordinates.
     * 
     * @param chunkX x-coordinate in chunk coordinates.
     * @param chunkZ z-coordinate in chunk coordinates.
     * @param step Chunk generation step used for skip context.
     * 
     * @return Whether to skip the chunk.
     */
    public boolean skipChunk(int chunkX, int chunkZ, ModernBetaGenerationStep step) {
        if (step == ModernBetaGenerationStep.CARVERS) {
            return this.skipCarvers;
        }
        
        return false;
    }
    
    /**
     * Get total world height in blocks, including minimum Y.
     * (i.e. Returns 320 if bottomY is -64 and topY is 256.)
     * 
     * @return Total world height in blocks.
     */
    public int getWorldHeight() {
        return this.generatorSettings.value().noiseSettings().height();
    }
    
    /**
     * @return Minimum Y coordinate in block coordinates.
     */
    public int getWorldMinY() {
        return this.generatorSettings.value().noiseSettings().minY();
    }
    
    /**
     * @return World sea level in block coordinates.
     */
    public int getSeaLevel() {
        return this.generatorSettings.value().seaLevel();
    }

    /**
     * Get aquifer sampler, for carving for now.
     * 
     * @return An aquifer sampler.
     */
    public Aquifer getAquiferSampler(ChunkAccess chunk, RandomState noiseConfig) {
        return Aquifer.createDisabled(this.defaultFluidLevelSampler);
    }
    
    /**
     * Get empty fluid level sampler.
     * 
     * @return Empty FluidLevelSampler.
     */
    public FluidPicker getFluidLevelSampler() {
        return this.defaultFluidLevelSampler;
    }
    
    /**
     * @return Parent ModernBetaChunkGenerator.
     */
    public ModernBetaChunkGenerator getChunkGenerator() {
        return this.chunkGenerator;
    }
    
    /**
     * @return Chunk provider's spawn locator.
     */
    public SpawnLocator getSpawnLocator() {
        return SpawnLocator.DEFAULT;
    }

    /**
     * Sets forest density using PerlinOctaveNoise sampler created with world seed.
     * Checks every placed feature in the biome source feature list,
     * and if it uses ModernBetaNoiseBasedCountPlacementModifier, replaces the noise sampler.
     */
    public void initForestOctaveNoise() {
        List<StepFeatureData> generationSteps = ((ChunkGeneratorAccessor)this.chunkGenerator).getFeaturesPerStep().get();
        
        for (StepFeatureData step : generationSteps) {
            List<PlacedFeature> featureList = step.features();
            
            for (PlacedFeature placedFeature : featureList) {
                PlacedFeatureAccessor accessor = (PlacedFeatureAccessor)(Object)placedFeature;
                List<PlacementModifier> modifiers = accessor.getPlacement();
                
                for (PlacementModifier modifier : modifiers) {
                    if (modifier instanceof NoiseBasedCountPlacementModifier noiseModifier) {
                        noiseModifier.setOctaves(this.getForestOctaveNoise());
                    } else if (modifier instanceof Infdev325CavePlacementModifier noiseModifier) {
                        noiseModifier.setOctaves(this.getForestOctaveNoise());
                    }
                }
            }
        }
    }
    
    /**
     * Samples biome at given biome coordinates.
     * 
     * @param biomeX x-coordinate in biome coordinates.
     * @param biomeY y-coordinate in biome coordinates.
     * @param biomeZ z-coordinate in biome coordinates.
     * @param noiseSampler
     * 
     * @return A biome.
     */
    public Holder<Biome> getBiome(int biomeX, int biomeY, int biomeZ, Climate.Sampler noiseSampler) {
        return this.chunkGenerator.getBiomeSource().getNoiseBiome(biomeX, biomeY, biomeZ, noiseSampler);
    }
    
    /**
     * Creates a ModernBetaChunkNoiseSampler
     *
     */
    public NoiseChunk createChunkNoiseSampler(ChunkAccess chunk, StructureManager manager, Blender blender, RandomState noiseConfig) {
        return NoiseChunk.forChunk(
            chunk,
            noiseConfig,
            Beardifier.forStructuresInChunk(manager, chunk.getPos()),
            this.generatorSettings.value(),
            this.getFluidLevelSampler(),
            blender
        );
    }
    
    public ModernBetaSettings getChunkSettings() {
        return this.chunkSettings;
    }

    protected Random createRandom(long seed) {
        return new Random(seed);
    }

    /**
     * Get a new Random object initialized with chunk coordinates for seed, for surface generation.
     * 
     * @param chunkX x-coordinate in chunk coordinates.
     * @param chunkZ z-coordinate in chunk coordinates.
     * 
     * @return New Random object initialized with chunk coordinates for seed.
     */
    protected Random createSurfaceRandom(int chunkX, int chunkZ) {
        long seed = (long)chunkX * 0x4f9939f508L + (long)chunkZ * 0x1ef1565bd5L;
        return this.createRandom(seed);
    }
    
    /**
     * Get Perlin octave noise sampler for tree placement.
     * 
     * @return Perlin octave noise sampler.
     */
    protected PerlinOctaveNoise getForestOctaveNoise() {
        return new PerlinOctaveNoise(new Random(this.seed), 8, PerlinNoiseSettings.DEFAULT);
    }
}