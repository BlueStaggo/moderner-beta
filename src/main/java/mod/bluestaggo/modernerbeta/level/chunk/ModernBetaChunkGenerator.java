//~registryOr
package mod.bluestaggo.modernerbeta.level.chunk;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.level.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.compat.ModCompat;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaSurfaceSystem;
import mod.bluestaggo.modernerbeta.level.biome.injection.InjectionNeeds;
import mod.bluestaggo.modernerbeta.level.carver.*;
import mod.bluestaggo.modernerbeta.mixin.BiomeManagerAccessor;
import mod.bluestaggo.modernerbeta.mixin.ChunkGeneratorStructureStateAccessor;
import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionRule;
import mod.bluestaggo.modernerbeta.mixin.SequenceRuleSourceAccessor;
import mod.bluestaggo.modernerbeta.registry.DefferedDirectHolder;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.CaveGeneration;
import mod.bluestaggo.modernerbeta.settings.component.DeepslateGeneration;
import mod.bluestaggo.modernerbeta.settings.component.StructureModifiers;
//? if >=26.3
//import mod.bluestaggo.modernerbeta.tags.ModernBetaBlockTags;
import mod.bluestaggo.modernerbeta.util.BlockStates;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.util.random.BedrockRandomSource;
import mod.bluestaggo.modernerbeta.util.random.BedrockWorldgenRandom;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.carver.configured.ModernBetaConfiguredCarvers;
import net.minecraft.util.Util;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
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
//? if >=26.3
//import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.*;
//? if <1.21.2
//import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.carver.*;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
//? if <1.21
//import java.util.concurrent.Executor;

public class ModernBetaChunkGenerator extends NoiseBasedChunkGenerator {
    public static final com.mojang.serialization.MapCodec<ModernBetaChunkGenerator> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
            RegistryOps.retrieveGetter(ModernBetaResourceKeys.SETTINGS_PRESET),
            RegistryOps.retrieveGetter(ModernBetaResourceKeys.SURFACE_CONFIG), //TODO: remove this
            ModernBetaSettings.WORLD_CODEC.fieldOf("provider_settings").forGetter(generator -> generator.chunkSettings)
        ).apply(instance, instance.stable(ModernBetaChunkGenerator::new))
    );

    private final HolderGetter<ModernBetaSettingsPreset> presetRegistry;
    private final HolderGetter<SurfaceConfig> surfaceConfigRegistry;
    private final ModernBetaSettings chunkSettings;

    private boolean useSurfaceRules;
    private CaveGeneration caveSettings = CaveGeneration.DEFAULT;

    private ChunkProvider chunkProvider;

    public ModernBetaChunkGenerator(
        BiomeSource biomeSource,
        HolderGetter<ModernBetaSettingsPreset> presetRegistry,
        HolderGetter<SurfaceConfig> surfaceConfigRegistry,
        ModernBetaSettings chunkProviderSettings
    ) {
        super(biomeSource, createGeneratorSettings(presetRegistry, chunkProviderSettings));

        this.presetRegistry = presetRegistry;
        this.surfaceConfigRegistry = surfaceConfigRegistry;
        this.chunkSettings = chunkProviderSettings.resolveDefaultPreset();

        if (this.biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
            modernBetaBiomeSource.setChunkGenerator(this);
        }
    }

    private static Holder<NoiseGeneratorSettings> createGeneratorSettings(
        HolderGetter<ModernBetaSettingsPreset> presetRegistry,
        ModernBetaSettings chunkProviderSettings
    ) {
        ModernBetaSettings fixedSettings = chunkProviderSettings.resolveDefaultPreset();
        return DefferedDirectHolder.of(() -> noiseGeneratorSettings(fixedSettings, presetRegistry));
    }

    private static NoiseGeneratorSettings noiseGeneratorSettings(
        ModernBetaSettings settings,
        HolderGetter<ModernBetaSettingsPreset> presetRegistry
    ) {
        ModernBetaSettings chunkSettings = settings.mapPreset(presetRegistry, ModernBetaSettingsPreset::chunkSettings);
        Holder<NoiseGeneratorSettings> generatorSettings = chunkSettings.getOrDefault(SettingsComponentTypes.NOISE_GENERATOR_SETTINGS);

        NoiseSettings noiseSettings = chunkSettings.get(SettingsComponentTypes.NOISE_SETTINGS);
        Integer seaLevel = chunkSettings.get(SettingsComponentTypes.SEA_LEVEL);
        DeepslateGeneration deepslateGeneration = chunkSettings.getOrDefault(SettingsComponentTypes.DEEPSLATE_GENERATION);
        boolean deepslateEnabled = deepslateGeneration.enabled();
        int deepslateMinY = deepslateGeneration.minY();
        int deepslateMaxY = deepslateGeneration.maxY();
        BlockState deepslateBlock = BuiltInRegistries.BLOCK.getOrThrow(ResourceKey.create(Registries.BLOCK, deepslateGeneration.block()))
                //? if >=1.21.2
                .value()
                .defaultBlockState();

        NoiseGeneratorSettings unboxed = generatorSettings.value();
        if (noiseSettings == null && seaLevel == null && !deepslateEnabled)
            return unboxed;

        //~ if >=26.3 '.surfaceRule()' -> '.materialRule().value()'
        SurfaceRules.RuleSource surfaceRules = unboxed.surfaceRule();
        if (deepslateEnabled) {
            SurfaceRules.RuleSource deepslateRule = SurfaceRules.ifTrue(
                SurfaceRules.verticalGradient(
                    "deepslate",
                    VerticalAnchor.absolute(deepslateMinY),
                    VerticalAnchor.absolute(deepslateMaxY)
                ),
                SurfaceRules.state(deepslateBlock)
            );

            if (surfaceRules instanceof SequenceRuleSourceAccessor sequenceRule) {
                List<SurfaceRules.RuleSource> ruleSequence = new ArrayList<>(sequenceRule.sequence());
                ruleSequence.add(deepslateRule);

                surfaceRules = SurfaceRules.sequence(ruleSequence.toArray(new SurfaceRules.RuleSource[0]));
            } else {
                surfaceRules = SurfaceRules.sequence(surfaceRules, deepslateRule);
            }
        }

        //? if >=26.3
        //Holder<SurfaceRules.RuleSource> boxedSurfaceRules = Holder.direct(surfaceRules);

        //noinspection deprecation
        unboxed = new NoiseGeneratorSettings(
            noiseSettings != null ? noiseSettings : unboxed.noiseSettings(),
            unboxed.defaultBlock(),
            unboxed.defaultFluid(),
            unboxed.noiseRouter(),
            /*? >=26.3 {*/ /*boxedSurfaceRules *//*? } else {*/ surfaceRules /*? }*/,
            unboxed.spawnTarget(),
            seaLevel != null ? seaLevel : unboxed.seaLevel(),
            unboxed.disableMobGeneration(),
            //~ if >=26.3 'Enabled()' -> '()' {
            unboxed.aquifersEnabled(),
            unboxed.oreVeinsEnabled(),
            //~ }
            unboxed.useLegacyRandomSource()
        );

        return unboxed;
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

        return ChunkGeneratorStructureStateAccessor.invokeInit(
            randomState,
            biomeSource,
            seed,
            //? if >=26.3
            //this.getOrigin(randomState),
            seed,
            list
        );
    }

    @Override
    public @NotNull CompletableFuture<ChunkAccess> createBiomes(
        //? if <1.21
        //Executor executor,
        RandomState noiseConfig, Blender blender, StructureManager structureAccessor, ChunkAccess chunk
    ) {
        return CompletableFuture.supplyAsync(Util.name(() -> {
            NoiseChunk noiseSampler = chunk.getOrCreateNoiseChunk(c -> this.createNoiseChunk(c, structureAccessor, blender, noiseConfig));
            chunk.fillBiomesFromNoise(this.biomeSource, noiseSampler.cachedClimateSampler(noiseConfig.router() /*? <26.3 {*/, this.generatorSettings().value().spawnTarget() /*? }*/));
            
            return chunk;
        }, () -> "init_biomes"), Util.backgroundExecutor());
    }
    
    @Override
    public @NotNull CompletableFuture<ChunkAccess> fillFromNoise(
        //? if <1.21
        //Executor executor,
        Blender blender, RandomState noiseConfig, StructureManager structureAccessor, ChunkAccess chunk
    ) {
        ChunkPos chunkPos = chunk.getPos();
        if (ModCompat.skipGeneratingChunk(chunkPos.x(), chunkPos.z()))
            return CompletableFuture.completedFuture(chunk);

        return this.chunkProvider.provideChunk(Blender.empty(), structureAccessor, chunk, noiseConfig);
    }

    @Override
    public void buildSurface(WorldGenRegion chunkRegion, StructureManager structureAccessor, RandomState noiseConfig, ChunkAccess chunk) {
        ChunkPos chunkPos = chunk.getPos();

        if (ModCompat.skipGeneratingChunk(chunkPos.x(), chunkPos.z()))
            return;

        this.injectBiomes(chunk, BiomeInjectionRule.Step.PRE);

        if (!this.chunkProvider.skipChunk(chunkPos.x(), chunkPos.z(), ModernBetaGenerationStep.SURFACE)) {
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

        this.injectBiomes(chunk, BiomeInjectionRule.Step.POST);
    }

    @Override
    public void buildSurface(
        ChunkAccess chunk,
        WorldGenerationContext context,
        RandomState random,
        StructureManager structureManager,
        BiomeManager biomeManager,
        //? if <26.2
        Registry<Biome> biomes,
        Blender blender
        //? if >=26.2
        //, Set<Holder<Biome>> possibleBiomes
    ) {
        NoiseChunk noiseChunk = chunk.getOrCreateNoiseChunk(chunkAccess -> this.createNoiseChunk(chunkAccess, structureManager, blender, random));
        NoiseGeneratorSettings noiseGeneratorSettings = this.generatorSettings().value();
        if (random.surfaceSystem() instanceof ModernBetaSurfaceSystem modernBetaSurfaceSystem) {
            modernBetaSurfaceSystem.modernerBeta$setupChunkContext(this.chunkProvider);

            if (this.biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
                modernBetaSurfaceSystem.modernerBeta$setupBiomeContext(modernBetaBiomeSource);
            }

            modernBetaSurfaceSystem.modernerBeta$beforeSurfaceBuild(chunk);
        }

        //? if <26.3
        ModCompat.useModernBetaSurfaceRules = true;
        random.surfaceSystem()
            .buildSurface(
                random,
                biomeManager,
                //? if <26.2
                biomes,
                //? if <26.3
                noiseGeneratorSettings.useLegacyRandomSource(),
                context,
                chunk,
                noiseChunk,
                //~ if >=26.3 '.surfaceRule()' -> '.materialRule().value()'
                noiseGeneratorSettings.surfaceRule()
                //? if >=26.2
                //, possibleBiomes
            );
        //? if <26.3
        ModCompat.useModernBetaSurfaceRules = false;
    }

    public void buildDefaultSurface(WorldGenRegion chunkRegion, StructureManager structureAccessor, RandomState noiseConfig, ChunkAccess chunk) {
        super.buildSurface(chunkRegion, structureAccessor, noiseConfig, chunk);
    }

    @Override
    public void applyCarvers(WorldGenRegion chunkRegion, long seed, RandomState noiseConfig, BiomeManager biomeAccess, StructureManager structureAccessor, ChunkAccess chunk
                      //? if >=26.3
                      //, CarvingMask.Filter filter
                      //? if <1.21.2
                      //, GenerationStep.Carving carverStep
    ) {
        ChunkPos chunkPos = chunk.getPos();

        if (ModCompat.skipGeneratingChunk(chunkPos.x(), chunkPos.z()) ||
            this.chunkProvider.skipChunk(chunkPos.x(), chunkPos.z(), ModernBetaGenerationStep.CARVERS))
            return;

        BiomeManager biomeAccessWithSource = biomeAccess.withDifferentSource((biomeX, biomeY, biomeZ) -> this.biomeSource.getNoiseBiome(biomeX, biomeY, biomeZ, noiseConfig.sampler()));

        int mainChunkX = chunkPos.x();
        int mainChunkZ = chunkPos.z();
        
        Aquifer aquiferSampler = this.chunkProvider.getAquiferSampler(chunk, noiseConfig);
        
        // Chunk Noise Sampler used to sample surface level
        NoiseChunk chunkNoiseSampler = chunk.getOrCreateNoiseChunk(c -> this.createNoiseChunk(c, structureAccessor, Blender.of(chunkRegion), noiseConfig));

        //~ if >=26.3 'CONFIGURED_CARVER' -> 'CARVER'
        Registry<ConfiguredWorldCarver<?>> configuredCarverRegistry = chunkRegion.registryAccess().lookupOrThrow(Registries.CONFIGURED_CARVER);
        //? if >=26.3 {
        /*WorldGenerationContext carverContext = new WorldGenerationContext(this, chunk.getHeightAccessorForGeneration());
        int protectedBlocksOnTop = chunk.isUpgrading() ? 0 : 7;
        int maxY = carverContext.getMinGenY() + carverContext.getGenDepth() - 1 - protectedBlocksOnTop;
        CarvingMask carvingMask = new ReferenceCarvingMask(chunk, carverContext.getMinGenY() + 1, maxY);
        *///? } else {
        ModCompat.useModernBetaSurfaceRules = true;
        CarvingContext carverContext = new CarvingContext(
            this,
            chunkRegion.registryAccess(),
            chunk.getHeightAccessorForGeneration(),
            chunkNoiseSampler,
            noiseConfig,
            this.generatorSettings().value().surfaceRule()
        );
        ModCompat.useModernBetaSurfaceRules = false;
        CarvingMask carvingMask = ((ProtoChunk)chunk).getOrCreateCarvingMask(
            //? if <1.21.2
            //carverStep
        );
        //? }

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

        Function<BlockPos, Holder<Biome>> blockToBiomeFunc = pos -> {
            //todo: maybe improve this
            if (this.biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
                return modernBetaBiomeSource.getBiomeInjectionHandler().getBiomeAtBlock(
                    chunk,
                    modernBetaBiomeSource.getBiomeProvider(),
                    ((BiomeManagerAccessor) biomeAccessWithSource).getBiomeZoomSeed(),
                    pos.getX(), pos.getY(), pos.getZ(),
                    BiomeInjectionRule.Step.POST,
                    InjectionNeeds.all(),
                    true
                );
            }

            return biomeAccessWithSource.getBiome(pos);
        };

        for (int chunkX = mainChunkX - 8; chunkX <= mainChunkX + 8; ++chunkX) {
            for (int chunkZ = mainChunkZ - 8; chunkZ <= mainChunkZ + 8; ++chunkZ) {
                ChunkPos carverPos = new ChunkPos(chunkX, chunkZ);
                ChunkAccess carverChunk = chunkRegion.getChunk(chunkX, chunkZ);
                
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
                    //~ if >=26.3 'ConfiguredWorldCarver<?>' -> 'WorldCarver'
                    ConfiguredWorldCarver<?> configuredCarver = carverEntry.value();
                    if (random instanceof WorldgenRandom chunkRandom) {
                        chunkRandom.setLargeFeatureSeed(seed + salt, chunkX, chunkZ);
                    } else {
                        random.setSeed((long) chunkX * saltX + (long) chunkZ * saltZ ^ seed);
                    }

                    if (this.caveSettings.forceBetaCaves() || this.caveSettings.forceBetaCanyons()) {
                        ResourceKey<ConfiguredWorldCarver<?>> carverKey = carverEntry.unwrapKey().orElse(null);
                        if (carverKey != null) {
                            //~ if >=26.3 'ConfiguredWorldCarver<?>' -> 'WorldCarver'
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
                        //? if >=26.3 {
                        /*if (configuredCarver instanceof BetaCaveWorldCarver betaCaveWorldCarver) {
                            //records be damned
                            configuredCarver = new BetaCaveWorldCarver(
                                betaCaveWorldCarver.probability(),
                                betaCaveWorldCarver.y(),
                                betaCaveWorldCarver.roomVerticalRadiusMultiplier(),
                                betaCaveWorldCarver.horizontalRadiusMultiplier(),
                                betaCaveWorldCarver.verticalRadiusMultiplier(),
                                betaCaveWorldCarver.floorLevel(),
                                Optional.of(this.caveSettings.fixCaveBorders()),
                                betaCaveWorldCarver.useAquifers()
                            );
                        }
                        *///? } else {
                        if (configuredCarver.config() instanceof BetaCaveCarverConfiguration betaCaveCarverConfig) {
                            betaCaveCarverConfig.useFixedCaves = Optional.of(this.caveSettings.fixCaveBorders());
                            betaCaveCarverConfig.useSurfaceRules = Optional.of(this.useSurfaceRules);
                        }
                        //? }

                        configuredCarver.carve(
                            carverContext,
                            //? if <26.3 {
                            chunk,
                            blockToBiomeFunc,
                            //? }
                            random,
                            //? if <26.3
                            aquiferSampler,
                            //? if >=26.3
                            //chunk.getPos(),
                            carverPos,
                            carvingMask
                        );
                    }

                    ++salt;
                }
            }
        }

        //? if >=26.3 {
        /*if (!carvingMask.isEmpty()) {
            this.applyCarvingMask(chunk, carvingMask, noiseConfig, carverContext, chunkNoiseSampler, blockToBiomeFunc, filter);
        }
        *///? }
    }

    //? if >=26.3 {
    /*@SuppressWarnings("deprecation")
    private void applyCarvingMask(
        ChunkAccess chunk,
        CarvingMask mask,
        RandomState randomState,
        WorldGenerationContext context,
        NoiseChunk noiseChunk,
        Function<BlockPos, Holder<Biome>> biomeGetter,
        CarvingMask.Filter filter
    ) {
        ChunkPos chunkPos = chunk.getPos();
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos helperPos = new BlockPos.MutableBlockPos();
        Aquifer aquifer = noiseChunk.aquifer();
        SurfaceRules.RuleSource materialRule = this.generatorSettings().value().materialRule().value();
        mask.visit((x, z, bottomY, topY) -> {
            boolean hasGrass = false;
            int worldX = chunkPos.getBlockX(x);
            int worldZ = chunkPos.getBlockZ(z);

            for (int worldY = topY; worldY >= bottomY; worldY--) {
                if (filter == null || filter.test(x, worldY, z)) {
                    blockPos.set(worldX, worldY, worldZ);
                    BlockState blockState = chunk.getBlockState(blockPos);
                    if (blockState.is(Blocks.GRASS_BLOCK) || blockState.is(Blocks.MYCELIUM)) {
                        hasGrass = true;
                    }

                    //TODO: #minecraft:overworld_carver_replaceables was removed, but this change does not translate well
                    //      in Moderner Beta. Investigate a proper solution.
                    if (blockState.is(ModernBetaBlockTags.OVERWORLD_CARVER_REPLACEABLES)) {
                        BlockState state = this.getCarveState(blockPos, aquifer);
                        if (state == null) {
                            return;
                        }
                        VersionCompat.setBlockState(chunk, blockPos, state);
                        if (aquifer.shouldScheduleFluidUpdate() && !state.getFluidState().isEmpty()) {
                            chunk.markPosForPostprocessing(blockPos);
                        }

                        if (hasGrass) {
                            helperPos.setWithOffset(blockPos, Direction.DOWN);
                            if (chunk.getBlockState(helperPos).is(Blocks.DIRT)) {
                                if (useSurfaceRules) {
                                    randomState.surfaceSystem()
                                        .topMaterial(materialRule, randomState, context, biomeGetter, chunk, noiseChunk, helperPos, !state.getFluidState().isEmpty())
                                        .ifPresent(topMaterial -> {
                                            VersionCompat.setBlockState(chunk, helperPos, topMaterial);
                                            if (!topMaterial.getFluidState().isEmpty()) {
                                                chunk.markPosForPostprocessing(helperPos);
                                            }
                                        });
                                } else {
                                    VersionCompat.setBlockState(chunk, helperPos, BlockStates.GRASS_BLOCK);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public BlockState getCarveState(BlockPos pos, Aquifer aquiferSampler) {
        //TODO: implement something for this
        /^if (pos.getY() <= this.lavaLevel.resolveY(context)) {
            return BlockStates.LAVA;
        }^/

        //TODO: implement something for this
        boolean useAquifers = true;//this.useAquifers.orElse(false);

        if (!useAquifers) {
            return BlockStates.AIR;
        }

        // TODO: Produces too many flooded caves, re-visit this later.

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        return aquiferSampler.computeSubstance(new DensityFunction.SinglePointContext(x, y, z), 0.0);
    }
    *///? }

    @Override
    public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureAccessor) {
        ChunkPos chunkPos = chunk.getPos();
        
        if (this.chunkProvider.skipChunk(chunkPos.x(), chunkPos.z(), ModernBetaGenerationStep.FEATURES))
            return;

        super.applyBiomeDecoration(level, chunk, structureAccessor);
    }
    
    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
        ChunkPos chunkPos = region.getCenter();
        
        if (ModCompat.skipGeneratingChunk(chunkPos.x(), chunkPos.z()) ||
            this.chunkProvider.skipChunk(chunkPos.x(), chunkPos.z(), ModernBetaGenerationStep.ENTITY_SPAWN))
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
            Beardifier.forStructuresInChunk(manager, chunk.getPos()),
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

    public boolean allowSurfaceRules() {
        return useSurfaceRules;
    }

    @Override
    protected com.mojang.serialization.MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }
    
    private void injectBiomes(ChunkAccess chunk, BiomeInjectionRule.Step step) {
        if (this.biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
            modernBetaBiomeSource.getBiomeInjectionHandler().injectIntoChunk(chunk, step, InjectionNeeds.all());
        }
    }

    @SuppressWarnings("unchecked")
    public static void register(IRegistryHandler<?> handler) {
        var registryHandler = (IRegistryHandler<com.mojang.serialization.MapCodec<?>>) handler;
        registryHandler.register(ModernerBeta.createId(ModernerBeta.MOD_ID), CODEC);
    }
}
