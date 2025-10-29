//~registryOr
package mod.bluestaggo.modernerbeta.world.chunk;

import com.google.common.base.Suppliers;
import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.world.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.CaveGeneration;
import mod.bluestaggo.modernerbeta.util.BlockStates;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.util.random.BedrockRandomSource;
import mod.bluestaggo.modernerbeta.util.random.BedrockWorldgenRandom;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.biome.injector.BiomeInjector;
import mod.bluestaggo.modernerbeta.world.biome.injector.BiomeInjector.BiomeInjectionStep;
import mod.bluestaggo.modernerbeta.world.carver.BetaCaveCarverConfiguration;
import mod.bluestaggo.modernerbeta.world.carver.configured.ModernBetaConfiguredCarvers;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate.Sampler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.Aquifer;
//? if <1.21.2
/*import net.minecraft.world.level.levelgen.GenerationStep;*/
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
//? if <1.21
/*import java.util.concurrent.Executor;*/

public class ModernBetaChunkGenerator extends NoiseBasedChunkGenerator {
    public static final com.mojang.serialization.MapCodec<ModernBetaChunkGenerator> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
            RegistryOps.retrieveGetter(ModernBetaResourceKeys.SETTINGS_PRESET),
            RegistryOps.retrieveGetter(ModernBetaResourceKeys.SURFACE_CONFIG),
            NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(generator -> generator.settings),
            CompoundTag.CODEC.fieldOf("provider_settings").forGetter(generator -> generator.chunkSettings)
        ).apply(instance, instance.stable(ModernBetaChunkGenerator::new))
    );

    private final HolderGetter<ModernBetaSettingsPreset> presetRegistry;
    private final HolderGetter<SurfaceConfig> surfaceConfigRegistry;
    private final Holder<NoiseGeneratorSettings> settings;
    private final CompoundTag chunkSettings;
    private final Supplier<BiomeInjector> biomeInjector;

    private boolean useSurfaceRules;
    private CaveGeneration caveSettings = CaveGeneration.DEFAULT;

    private ChunkProvider chunkProvider;

    public ModernBetaChunkGenerator(
        BiomeSource biomeSource,
        HolderGetter<ModernBetaSettingsPreset> presetRegistry,
        HolderGetter<SurfaceConfig> surfaceConfigRegistry,
        Holder<NoiseGeneratorSettings> settings,
        CompoundTag chunkProviderSettings
    ) {
        super(biomeSource, settings);

        String presetKey = ModernBetaBuiltInTypes.SettingsComponentType.PRESET.id.toString();

        if (ModernBetaSettings.DEFAULT_PRESET_ID.toString().equals(
            chunkProviderSettings.getString(presetKey)/*? >=1.21.5 {*/.orElse(null)/*?}*/)) {
            chunkProviderSettings.putString(presetKey, ModernerBeta.config.getOrDefault(SettingsComponentTypes.CONFIG_MISCELLANEOUS)
                .defaultSettingsPreset().toString());
        }

        this.settings = settings;
        this.presetRegistry = presetRegistry;
        this.surfaceConfigRegistry = surfaceConfigRegistry;
        this.chunkSettings = chunkProviderSettings;
        this.biomeInjector = Suppliers.memoize(() ->
            this.biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource
                ? new BiomeInjector(this, modernBetaBiomeSource) : null);
        
        if (this.biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
            modernBetaBiomeSource.setChunkGenerator(this);
        }
    }

    public void initProvider(long seed) {
        ModernBetaSettings chunkSettings = ModernBetaSettings.fromCompound(this.chunkSettings)
            .mapPreset(this.presetRegistry, ModernBetaSettingsPreset::chunkSettings);

        this.chunkProvider = ModernBetaRegistries.CHUNK
            //? if >=1.21.2 {
            .getValue
            //? } else {
            /*.get
            *///? }
                (chunkSettings.getProvider())
            .apply(this, seed);
        
        this.chunkProvider.initForestOctaveNoise();

        this.useSurfaceRules = chunkSettings.getOrDefault(SettingsComponentTypes.USE_SURFACE_RULES);
        this.caveSettings = chunkSettings.getOrDefault(SettingsComponentTypes.CAVE_GENERATION);
    }

    @Override
    public CompletableFuture<ChunkAccess> createBiomes(
        //? if <1.21
        /*Executor executor,*/
        RandomState noiseConfig, Blender blender, StructureManager structureAccessor, ChunkAccess chunk
    ) {
        return CompletableFuture.supplyAsync(Util.name(() -> {
            NoiseChunk noiseSampler = chunk.getOrCreateNoiseChunk(c -> this.createNoiseChunk(c, structureAccessor, blender, noiseConfig));
            chunk.fillBiomesFromNoise(this.biomeSource, noiseSampler.cachedClimateSampler(noiseConfig.router(), this.settings.value().spawnTarget()));
            
            return chunk;
        }, () -> "init_biomes"), Util.backgroundExecutor());
    }
    
    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(
        //? if <1.21
        /*Executor executor,*/
        Blender blender, RandomState noiseConfig, StructureManager structureAccessor, ChunkAccess chunk
    ) {
        return this.chunkProvider.provideChunk(Blender.empty(), structureAccessor, chunk, noiseConfig);
    }

    @Override
    public void buildSurface(WorldGenRegion chunkRegion, StructureManager structureAccessor, RandomState noiseConfig, ChunkAccess chunk) {
        this.injectBiomes(chunk, noiseConfig.sampler(), BiomeInjectionStep.PRE);

        if (!this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ModernBetaGenerationStep.SURFACE)) {
            if (this.biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
                if (this.useSurfaceRules) {
                    this.buildDefaultSurface(chunkRegion, structureAccessor, noiseConfig, chunk);
                    this.chunkProvider.provideSurfaceExtra(chunkRegion, structureAccessor, chunk, modernBetaBiomeSource, noiseConfig);
                } else {
                    this.chunkProvider.provideSurface(chunkRegion, structureAccessor, chunk, modernBetaBiomeSource, noiseConfig);
                }
            } else {
                super.buildSurface(chunkRegion, structureAccessor, noiseConfig, chunk);
            }
        }

        this.injectBiomes(chunk, noiseConfig.sampler(), BiomeInjectionStep.POST);
    }

    public void buildDefaultSurface(WorldGenRegion chunkRegion, StructureManager structureAccessor, RandomState noiseConfig, ChunkAccess chunk) {
        super.buildSurface(chunkRegion, structureAccessor, noiseConfig, chunk);
    }

    @Override
    public void applyCarvers(WorldGenRegion chunkRegion, long seed, RandomState noiseConfig, BiomeManager biomeAccess, StructureManager structureAccessor, ChunkAccess chunk
                      //? if <1.21.2
                      /*, GenerationStep.Carving carverStep*/
    ) {
        if (this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ModernBetaGenerationStep.CARVERS)) return;

        BiomeManager biomeAccessWithSource = biomeAccess.withDifferentSource((biomeX, biomeY, biomeZ) -> this.biomeSource.getNoiseBiome(biomeX, biomeY, biomeZ, noiseConfig.sampler()));
        ChunkPos chunkPos = chunk.getPos();

        int mainChunkX = chunkPos.x;
        int mainChunkZ = chunkPos.z;
        
        Aquifer aquiferSampler = this.chunkProvider.getAquiferSampler(chunk, noiseConfig);
        
        // Chunk Noise Sampler used to sample surface level
        NoiseChunk chunkNoiseSampler = chunk.getOrCreateNoiseChunk(c -> this.createNoiseChunk(c, structureAccessor, Blender.of(chunkRegion), noiseConfig));

        Registry<ConfiguredWorldCarver<?>> configuredCarverRegistry = chunkRegion.registryAccess().lookupOrThrow(Registries.CONFIGURED_CARVER);
        CarvingContext carverContext = new CarvingContext(this, chunkRegion.registryAccess(), chunk.getHeightAccessorForGeneration(), chunkNoiseSampler, noiseConfig, this.settings.value().surfaceRule());
        CarvingMask carvingMask = ((ProtoChunk)chunk).getOrCreateCarvingMask(
            //? if <1.21.2
            /*carverStep*/
        );

        CaveGeneration.SeedMethod seedMethod = this.caveSettings.seedMethod();

        RandomSource random = switch (seedMethod) {
            case MODERN -> new WorldgenRandom(new LegacyRandomSource(RandomSupport.generateUniqueSeed()));
            case BEDROCK -> new BedrockWorldgenRandom(new BedrockRandomSource((int) RandomSupport.generateUniqueSeed()));
            default -> new SingleThreadedRandomSource(seed);
        };

        long saltX = switch (seedMethod) {
            case BETA -> (random.nextLong() / 2L) * 2L + 1L;
            case EARLY_RELEASE -> random.nextLong();
            case MODERN, BEDROCK -> 0;
        };
        long saltZ = switch (seedMethod) {
            case BETA -> (random.nextLong() / 2L) * 2L + 1L;
            case EARLY_RELEASE -> random.nextLong();
            case MODERN, BEDROCK -> 0;
        };

        for (int chunkX = mainChunkX - 8; chunkX <= mainChunkX + 8; ++chunkX) {
            for (int chunkZ = mainChunkZ - 8; chunkZ <= mainChunkZ + 8; ++chunkZ) {
                ChunkPos carverPos = new ChunkPos(chunkX, chunkZ);
                ChunkAccess carverChunk = chunkRegion.getChunk(carverPos.x, carverPos.z);
                
                @SuppressWarnings("deprecation")
                BiomeGenerationSettings genSettings = carverChunk.carverBiome(() -> this.getBiomeGenerationSettings(
                    this.biomeSource.getNoiseBiome(QuartPos.fromBlock(carverPos.getMinBlockX()), 0, QuartPos.fromBlock(carverPos.getMinBlockZ()), noiseConfig.sampler()))
                );
                Iterable<Holder<ConfiguredWorldCarver<?>>> carverList = genSettings.getCarvers(
                    //? if <1.21.2
                    /*carverStep*/
                );

                int salt = 0;
                for(Holder<ConfiguredWorldCarver<?>> carverEntry : carverList) {
                    ConfiguredWorldCarver<?> configuredCarver = carverEntry.value();
                    if (random instanceof WorldgenRandom chunkRandom) {
                        chunkRandom.setLargeFeatureSeed(seed + salt, chunkX, chunkZ);
                    } else {
                        random.setSeed((long) chunkX * saltX + (long) chunkZ * saltZ ^ seed);
                    }

                    if (this.caveSettings.forceBetaCaves() || this.caveSettings.forceBetaCanyons()) {
                        ResourceKey<ConfiguredWorldCarver<?>> carverKey = carverEntry.unwrapKey().orElse(null);
                        if (carverKey != null) {
                            ConfiguredWorldCarver<?> replacementCarver = null;
                            if (this.caveSettings.forceBetaCaves()) {
                                if (carverKey.equals(Carvers.CAVE)) {
                                    replacementCarver = configuredCarverRegistry
                                        //? if >=1.21.2 {
                                        .getValue
                                        //? } else {
                                        /*.get
                                        *///? }
                                            (ModernBetaConfiguredCarvers.BETA_CAVE);
                                } else if (carverKey.equals(Carvers.CAVE_EXTRA_UNDERGROUND)) {
                                    replacementCarver = configuredCarverRegistry
                                        //? if >=1.21.2 {
                                        .getValue
                                        //? } else {
                                        /*.get
                                        *///? }
                                            (ModernBetaConfiguredCarvers.BETA_CAVE_DEEP);
                                }
                            }
                            if (this.caveSettings.forceBetaCanyons() && carverKey.equals(Carvers.CANYON)) {
                                replacementCarver = configuredCarverRegistry
                                    //? if >=1.21.2 {
                                    .getValue
                                    //? } else {
                                    /*.get
                                    *///? }
                                        (ModernBetaConfiguredCarvers.BETA_CANYON);
                            }

                            if (replacementCarver != null) {
                                configuredCarver = replacementCarver;
                            }
                        }
                    }

                    if (configuredCarver.isStartChunk(random)) {
                        if (configuredCarver.config() instanceof BetaCaveCarverConfiguration betaCaveCarverConfig) {
                            betaCaveCarverConfig.useFixedCaves = Optional.of(this.caveSettings.fixCaveBorders());
                            betaCaveCarverConfig.useSurfaceRules = Optional.of(this.useSurfaceRules);
                        }

                        configuredCarver.carve(carverContext, chunk, biomeAccessWithSource::getBiome, random, aquiferSampler, carverPos, carvingMask);
                    }

                    if (seedMethod != CaveGeneration.SeedMethod.BEDROCK) {
                        ++salt;
                    }
                }
            }
        }
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureAccessor) {
        ChunkPos pos = chunk.getPos();
        
        if (this.chunkProvider.skipChunk(pos.x, pos.z, ModernBetaGenerationStep.FEATURES)) 
            return;

        super.applyBiomeDecoration(level, chunk, structureAccessor);
    }
    
    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
        ChunkPos pos = region.getCenter();
        
        if (this.chunkProvider.skipChunk(pos.x, pos.z, ModernBetaGenerationStep.ENTITY_SPAWN))
            return;
        
        super.spawnOriginalMobs(region);
    }
    
    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor level, RandomState noiseConfig) {
        return this.chunkProvider.getHeight(level, x, z, type);
    }
    
    public int getHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor level) {
        return this.chunkProvider.getHeight(level, x, z, type);
    }
  
    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState noiseConfig) {
        int height = this.chunkProvider.getHeight(level, x, z, Heightmap.Types.OCEAN_FLOOR_WG);
        int worldHeight = this.chunkProvider.getWorldHeight();
        int minY = this.chunkProvider.getWorldMinY();
        
        BlockState[] column = new BlockState[worldHeight];
        
        for (int y = worldHeight - 1; y >= 0; --y) {
            int worldY = y + minY;
            
            if (worldY > height) {
                if (worldY > this.getSeaLevel())
                    column[y] = BlockStates.AIR;
                else
                    column[y] = this.settings.value().defaultFluid();
            } else {
                column[y] = this.settings.value().defaultBlock();
            }
        }
        
        return new NoiseColumn(minY, column);
    }

    @Override
    public int getGenDepth() {
        // TODO: Causes issue with YOffset.BelowTop decorator (i.e. ORE_COAL_UPPER), find some workaround.
        // Affects both getWorldHeight() and getMinimumY().
        // See: MC-236933 and MC-236723
        if (this.chunkProvider == null)
            return this.getGeneratorSettings().value().noiseSettings().height();
       
        return this.chunkProvider.getWorldHeight();
    }
    
    @Override
    public int getMinY() {
        if (this.chunkProvider == null)
            return this.getGeneratorSettings().value().noiseSettings().minY();
        
        return this.chunkProvider.getWorldMinY();
    }

    @Override
    public int getSeaLevel() {
        return this.chunkProvider.getSeaLevel();
    }

    @Override
    protected NoiseChunk createNoiseChunk(ChunkAccess chunk, StructureManager manager, Blender blender, RandomState noiseConfig) {
        return ModernBetaChunkNoiseSampler.create(
            chunk,
            noiseConfig,
            this.settings.value(),
            this.chunkProvider.getFluidLevelSampler(),
            this.chunkProvider
        );
    }

    public Holder<NoiseGeneratorSettings> getGeneratorSettings() {
        return this.settings;
    }

    public HolderGetter<ModernBetaSettingsPreset> getPresetRegistry() {
        return this.presetRegistry;
    }

    public HolderGetter<SurfaceConfig> getSurfaceConfigRegistry() {
        return this.surfaceConfigRegistry;
    }

    public ChunkProvider getChunkProvider() {
        return this.chunkProvider;
    }
    
    public CompoundTag getChunkSettings() {
        return this.chunkSettings;
    }
    
    public BiomeInjector getBiomeInjector() {
        return this.biomeInjector.get();
    }

    public boolean allowSurfaceRules() {
        return useSurfaceRules;
    }

    @Override
    protected com.mojang.serialization.MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }
    
    private void injectBiomes(ChunkAccess chunk, Sampler noiseSampler, BiomeInjectionStep step) {
        BiomeInjector biomeInjector = this.biomeInjector.get();
        if (biomeInjector != null) {
            biomeInjector.injectBiomes(chunk, noiseSampler, step);
        }
    }

    @SuppressWarnings("unchecked")
    public static void register(IRegistryHandler<?> handler) {
        var registryHandler = (IRegistryHandler<com.mojang.serialization.MapCodec<?>>) handler;
        registryHandler.register(ModernerBeta.createId(ModernerBeta.MOD_ID), CODEC);
    }
}
