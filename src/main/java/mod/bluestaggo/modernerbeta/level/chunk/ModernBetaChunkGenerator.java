//~registryOr
package mod.bluestaggo.modernerbeta.level.chunk;

import com.google.common.base.Suppliers;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.level.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.compat.ModCompat;
import mod.bluestaggo.modernerbeta.level.biome.injector.BiomeInjectionRule;
import mod.bluestaggo.modernerbeta.mixin.ChunkGeneratorStructureStateAccessor;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.CaveGeneration;
import mod.bluestaggo.modernerbeta.settings.component.StructureModifiers;
import mod.bluestaggo.modernerbeta.util.BlockStates;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.util.random.BedrockRandomSource;
import mod.bluestaggo.modernerbeta.util.random.BedrockWorldgenRandom;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.biome.injector.BiomeInjector;
import mod.bluestaggo.modernerbeta.level.carver.BetaCaveCarverConfiguration;
import mod.bluestaggo.modernerbeta.level.carver.configured.ModernBetaConfiguredCarvers;
import net.minecraft.Util;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate.Sampler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.*;
//? if <1.21.2
//import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;
//? if <1.21
//import java.util.concurrent.Executor;

public class ModernBetaChunkGenerator extends NoiseBasedChunkGenerator {
    public static final com.mojang.serialization.MapCodec<ModernBetaChunkGenerator> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
            RegistryOps.retrieveGetter(ModernBetaResourceKeys.SETTINGS_PRESET),
            CodecUtil.retrieveLookup(ModernBetaResourceKeys.SURFACE_CONFIG),
            ModernBetaSettings.WORLD_CODEC.fieldOf("provider_settings").forGetter(generator -> generator.chunkSettings)
        ).apply(instance, instance.stable(ModernBetaChunkGenerator::new))
    );

    private final HolderGetter<ModernBetaSettingsPreset> presetRegistry;
    private final HolderGetter<SurfaceConfig> surfaceConfigRegistry;
    private final ModernBetaSettings chunkSettings;
    private final Supplier<BiomeInjector> biomeInjector;

    private boolean useSurfaceRules;
    private CaveGeneration caveSettings = CaveGeneration.DEFAULT;

    private ChunkProvider chunkProvider;

    public ModernBetaChunkGenerator(
        BiomeSource biomeSource,
        HolderGetter<ModernBetaSettingsPreset> presetRegistry,
        HolderGetter<SurfaceConfig> surfaceConfigRegistry,
        ModernBetaSettings chunkProviderSettings
    ) {
        super(biomeSource, generatorSettings(presetRegistry, fixupPreset(chunkProviderSettings)));

        this.presetRegistry = presetRegistry;
        this.surfaceConfigRegistry = surfaceConfigRegistry;
        this.chunkSettings = fixupPreset(chunkProviderSettings);
        this.biomeInjector = Suppliers.memoize(() ->
            this.biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource
                ? new BiomeInjector(this, modernBetaBiomeSource) : null);

        if (this.biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
            modernBetaBiomeSource.setChunkGenerator(this);
        }
    }

    private static ModernBetaSettings fixupPreset(ModernBetaSettings chunkProviderSettings) {
        if (!ModernerBeta.GENERATING_DATA && ModernBetaSettings.DEFAULT_PRESET_ID.equals(
            chunkProviderSettings.getOrDefault(SettingsComponentTypes.PRESET))) {
            chunkProviderSettings = chunkProviderSettings
                .extend()
                .remove(SettingsComponentTypes.PRESET)
                .add(SettingsComponentTypes.PRESET, ModernerBeta.config
                    .getOrDefault(SettingsComponentTypes.CONFIG_MISCELLANEOUS).defaultSettingsPreset())
                .build();
        }

        return chunkProviderSettings;
    }


    private static Holder<NoiseGeneratorSettings> generatorSettings(
        HolderGetter<ModernBetaSettingsPreset> presetRegistry,
        ModernBetaSettings chunkSettings
    ) {
        ModernBetaSettings mappedChunkSettings = chunkSettings.mapPreset(presetRegistry, ModernBetaSettingsPreset::chunkSettings);
        Holder<NoiseGeneratorSettings> generatorSettings = mappedChunkSettings.getOrDefault(SettingsComponentTypes.NOISE_GENERATOR_SETTINGS);

        NoiseSettings noiseSettings = mappedChunkSettings.get(SettingsComponentTypes.NOISE_SETTINGS);
        Integer seaLevel = mappedChunkSettings.get(SettingsComponentTypes.SEA_LEVEL);
        if (noiseSettings == null & seaLevel == null)
            return generatorSettings;


        NoiseGeneratorSettings unboxed = generatorSettings.value();
        //noinspection deprecation
        unboxed = new NoiseGeneratorSettings(
            noiseSettings != null ? noiseSettings : unboxed.noiseSettings(),
            unboxed.defaultBlock(),
            unboxed.defaultFluid(),
            unboxed.noiseRouter(),
            unboxed.surfaceRule(),
            unboxed.spawnTarget(),
            seaLevel != null ? seaLevel : unboxed.seaLevel(),
            unboxed.disableMobGeneration(),
            unboxed.aquifersEnabled(),
            unboxed.oreVeinsEnabled(),
            unboxed.useLegacyRandomSource()
        );
        generatorSettings = Holder.direct(unboxed);

        return generatorSettings;
    }

    public void initProvider(long seed) {
        ModernBetaSettings chunkSettings = this.chunkSettings.mapPreset(this.presetRegistry, ModernBetaSettingsPreset::chunkSettings);

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
    public @NotNull ChunkGeneratorStructureState createState(HolderLookup<StructureSet> structureSetLookup, RandomState randomState, long seed) {
        ModernBetaSettings mappedChunkSettings = chunkSettings.mapPreset(presetRegistry, ModernBetaSettingsPreset::chunkSettings);
        StructureModifiers modifiers = mappedChunkSettings.getOrDefault(SettingsComponentTypes.STRUCTURE_MODIFERS);

        List<Holder<StructureSet>> list = structureSetLookup.listElements()
            .filter(reference -> {
                ResourceKey<StructureSet> key = reference.key();
                if (modifiers.removed().contains(key) || modifiers.overrides().containsKey(key))
                    return false;

                return ChunkGeneratorStructureStateAccessor.invokeHasBiomesForStructureSet(reference.value(), biomeSource);
            })
            .collect(Collectors.toList());

        for (Map.Entry<ResourceKey<StructureSet>, StructureSet> override : modifiers.overrides().entrySet()) {
            list.add(Holder.direct(override.getValue()));
        }

        return ChunkGeneratorStructureStateAccessor.invokeInit(randomState, biomeSource, seed, seed, list);
    }

    @Override
    public @NotNull CompletableFuture<ChunkAccess> createBiomes(
        //? if <1.21
        //Executor executor,
        RandomState noiseConfig, Blender blender, StructureManager structureAccessor, ChunkAccess chunk
    ) {
        return CompletableFuture.supplyAsync(Util.name(() -> {
            NoiseChunk noiseSampler = chunk.getOrCreateNoiseChunk(c -> this.createNoiseChunk(c, structureAccessor, blender, noiseConfig));
            chunk.fillBiomesFromNoise(this.biomeSource, noiseSampler.cachedClimateSampler(noiseConfig.router(), this.generatorSettings().value().spawnTarget()));
            
            return chunk;
        }, () -> "init_biomes"), Util.backgroundExecutor());
    }
    
    @Override
    public @NotNull CompletableFuture<ChunkAccess> fillFromNoise(
        //? if <1.21
        //Executor executor,
        Blender blender, RandomState noiseConfig, StructureManager structureAccessor, ChunkAccess chunk
    ) {
        ChunkPos pos = chunk.getPos();
        if (ModCompat.skipGeneratingChunk(pos.x, pos.z))
            return CompletableFuture.completedFuture(chunk);

        return this.chunkProvider.provideChunk(Blender.empty(), structureAccessor, chunk, noiseConfig);
    }

    @Override
    public void buildSurface(WorldGenRegion chunkRegion, StructureManager structureAccessor, RandomState noiseConfig, ChunkAccess chunk) {
        ChunkPos pos = chunk.getPos();

        if (ModCompat.skipGeneratingChunk(pos.x, pos.z))
            return;

        this.injectBiomes(chunk, noiseConfig.sampler(), BiomeInjectionRule.Step.PRE);

        if (!this.chunkProvider.skipChunk(pos.x, pos.z, ModernBetaGenerationStep.SURFACE)) {
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

        this.injectBiomes(chunk, noiseConfig.sampler(), BiomeInjectionRule.Step.POST);
    }

    @Override
    public void buildSurface(
        ChunkAccess chunk,
        WorldGenerationContext context,
        RandomState random,
        StructureManager structureManager,
        BiomeManager biomeManager,
        Registry<Biome> biomes,
        Blender blender
    ) {
        NoiseChunk noiseChunk = chunk.getOrCreateNoiseChunk(chunkAccess -> this.createNoiseChunk(chunkAccess, structureManager, blender, random));
        NoiseGeneratorSettings noiseGeneratorSettings = this.generatorSettings().value();
        random.surfaceSystem()
            .buildSurface(random, biomeManager, biomes, noiseGeneratorSettings.useLegacyRandomSource(), context, chunk, noiseChunk, noiseGeneratorSettings.surfaceRule());
    }

    public void buildDefaultSurface(WorldGenRegion chunkRegion, StructureManager structureAccessor, RandomState noiseConfig, ChunkAccess chunk) {
        super.buildSurface(chunkRegion, structureAccessor, noiseConfig, chunk);
    }

    @Override
    public void applyCarvers(WorldGenRegion chunkRegion, long seed, RandomState noiseConfig, BiomeManager biomeAccess, StructureManager structureAccessor, ChunkAccess chunk
                      //? if <1.21.2
                      //, GenerationStep.Carving carverStep
    ) {
        ChunkPos pos = chunk.getPos();

        if (ModCompat.skipGeneratingChunk(pos.x, pos.z) ||
            this.chunkProvider.skipChunk(pos.x, pos.z, ModernBetaGenerationStep.CARVERS))
            return;

        BiomeManager biomeAccessWithSource = biomeAccess.withDifferentSource((biomeX, biomeY, biomeZ) -> this.biomeSource.getNoiseBiome(biomeX, biomeY, biomeZ, noiseConfig.sampler()));
        ChunkPos chunkPos = chunk.getPos();

        int mainChunkX = chunkPos.x;
        int mainChunkZ = chunkPos.z;
        
        Aquifer aquiferSampler = this.chunkProvider.getAquiferSampler(chunk, noiseConfig);
        
        // Chunk Noise Sampler used to sample surface level
        NoiseChunk chunkNoiseSampler = chunk.getOrCreateNoiseChunk(c -> this.createNoiseChunk(c, structureAccessor, Blender.of(chunkRegion), noiseConfig));

        Registry<ConfiguredWorldCarver<?>> configuredCarverRegistry = chunkRegion.registryAccess().lookupOrThrow(Registries.CONFIGURED_CARVER);
        CarvingContext carverContext = new CarvingContext(this, chunkRegion.registryAccess(), chunk.getHeightAccessorForGeneration(), chunkNoiseSampler, noiseConfig, this.generatorSettings().value().surfaceRule());
        CarvingMask carvingMask = ((ProtoChunk)chunk).getOrCreateCarvingMask(
            //? if <1.21.2
            //carverStep
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
                    //carverStep
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

                    ++salt;
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
        
        if (ModCompat.skipGeneratingChunk(pos.x, pos.z) ||
            this.chunkProvider.skipChunk(pos.x, pos.z, ModernBetaGenerationStep.ENTITY_SPAWN))
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
    public @NotNull NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState noiseConfig) {
        int minY = this.chunkProvider.getWorldMinY();
        if (ModCompat.skipGeneratingChunk(x, z))
            return new NoiseColumn(minY, new BlockState[0]);

        int worldHeight = this.chunkProvider.getWorldHeight();
        int height = this.chunkProvider.getHeight(level, x, z, Heightmap.Types.OCEAN_FLOOR_WG);

        BlockState[] column = new BlockState[worldHeight];
        
        for (int y = worldHeight - 1; y >= 0; --y) {
            int worldY = y + minY;
            
            if (worldY > height) {
                if (worldY > this.getSeaLevel())
                    column[y] = BlockStates.AIR;
                else
                    column[y] = this.generatorSettings().value().defaultFluid();
            } else {
                column[y] = this.generatorSettings().value().defaultBlock();
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
            return this.generatorSettings().value().noiseSettings().height();
       
        return this.chunkProvider.getWorldHeight();
    }
    
    @Override
    public int getMinY() {
        if (this.chunkProvider == null)
            return this.generatorSettings().value().noiseSettings().minY();
        
        return this.chunkProvider.getWorldMinY();
    }

    @Override
    public int getSeaLevel() {
        return this.chunkProvider.getSeaLevel();
    }

    @Override
    protected @NotNull NoiseChunk createNoiseChunk(ChunkAccess chunk, StructureManager manager, Blender blender, RandomState noiseConfig) {
        return ModernBetaChunkNoiseSampler.create(
            chunk,
            noiseConfig,
            this.generatorSettings().value(),
            this.chunkProvider.getFluidLevelSampler(),
            this.chunkProvider
        );
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
    
    public ModernBetaSettings getChunkSettings() {
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
    
    private void injectBiomes(ChunkAccess chunk, Sampler noiseSampler, BiomeInjectionRule.Step step) {
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
