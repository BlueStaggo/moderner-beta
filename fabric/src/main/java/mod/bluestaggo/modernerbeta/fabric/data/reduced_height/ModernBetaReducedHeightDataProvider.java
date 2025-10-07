package mod.bluestaggo.modernerbeta.fabric.data.reduced_height;

import mod.bluestaggo.modernerbeta.fabric.mixin.AccessorDensityFunctionsFabric;
import mod.bluestaggo.modernerbeta.mixin.AccessorDensityFunctions;
import mod.bluestaggo.modernerbeta.world.carver.configured.ModernBetaConfiguredCarvers;
import mod.bluestaggo.modernerbeta.world.feature.configured.ModernBetaConfiguredFeatures;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.*;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.biome.source.util.VanillaBiomeParameters;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverDebugConfig;
import net.minecraft.world.gen.carver.CaveCarverConfig;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.heightprovider.ConstantHeightProvider;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.placementmodifier.*;
import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;

import static mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGeneratorSettings.*;

public class ModernBetaReducedHeightDataProvider extends FabricDynamicRegistryProvider {
    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_DEEPSLATE_OLD = ModernBetaConfiguredFeatures.of("ore_deepslate_old");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_DIAMOND_OLD = ModernBetaConfiguredFeatures.of("ore_diamond_old");

    private static boolean isGeneratingData;

    public ModernBetaReducedHeightDataProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(WrapperLookup registries, Entries entries) {
        isGeneratingData = true;

        //Dimension types
        entries.add(
            DimensionTypes.OVERWORLD,
            new DimensionType(
                OptionalLong.empty(),
                true,
                false,
                false,
                true,
                1.0,
                true,
                false,
                0,
                320,
                320,
                BlockTags.INFINIBURN_OVERWORLD,
                DimensionTypes.OVERWORLD_ID,
                0.0F,
                Optional.empty(),
                new DimensionType.MonsterSettings(false, true, UniformIntProvider.create(0, 7), 0)
            )
        );

        //Configured carvers
        RegistryEntryLookup<Block> registryBlock = registries.getOrThrow(RegistryKeys.BLOCK);
        CaveCarverConfig configCaveDeep = new CaveCarverConfig(
            0.0f,
            ConstantHeightProvider.create(YOffset.fixed(-2032)),
            ConstantFloatProvider.create(0.0f),
            YOffset.fixed(-2032),
            CarverDebugConfig.create(false, Blocks.CRIMSON_BUTTON.getDefaultState()),
            registryBlock.getOrThrow(BlockTags.AIR),
            ConstantFloatProvider.create(0.0f),
            ConstantFloatProvider.create(0.0f),
            ConstantFloatProvider.create(0.0f)
        );
        entries.add(ModernBetaConfiguredCarvers.BETA_CAVE_DEEP, Carver.CAVE.configure(configCaveDeep));

        //Configured features
        RuleTest overworldStone = new TagMatchRuleTest(BlockTags.BASE_STONE_OVERWORLD);
        RuleTest deepslateReplacers = new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        entries.add(OreConfiguredFeatures.ORE_GRANITE, new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(overworldStone, Blocks.GRANITE.getDefaultState(), 33)));
        entries.add(OreConfiguredFeatures.ORE_DIORITE, new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(overworldStone, Blocks.DIORITE.getDefaultState(), 33)));
        entries.add(OreConfiguredFeatures.ORE_ANDESITE, new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(overworldStone, Blocks.ANDESITE.getDefaultState(), 33)));
        entries.add(OreConfiguredFeatures.ORE_TUFF, new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(overworldStone, Blocks.TUFF.getDefaultState(), 33)));

        entries.add(ORE_DEEPSLATE_OLD, new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(overworldStone, Blocks.DEEPSLATE.getDefaultState(), 64)));
        entries.add(ORE_DIAMOND_OLD, new ConfiguredFeature<>(Feature.ORE,
            new OreFeatureConfig(
                List.of(
                        OreFeatureConfig.createTarget(overworldStone, Blocks.DIAMOND_ORE.getDefaultState()),
                        OreFeatureConfig.createTarget(deepslateReplacers, Blocks.DEEPSLATE_DIAMOND_ORE.getDefaultState())
                ), 8
            )
        ));

        //Chunk generator settings
        entries.add(ChunkGeneratorSettings.OVERWORLD, createVanillaSurfaceSettings(registries, false, false));
        entries.add(ChunkGeneratorSettings.LARGE_BIOMES, createVanillaSurfaceSettings(registries, false, true));
        entries.add(ChunkGeneratorSettings.AMPLIFIED, createVanillaSurfaceSettings(registries, true, false));
        entries.add(ChunkGeneratorSettings.CAVES, createVanillaCavesSettings(registries));

        entries.add(BETA, createGeneratorSettings(registries, ModernBetaShapeReducedHeightConfigs.BETA, 64, true));
        entries.add(ALPHA, createGeneratorSettings(registries, ModernBetaShapeReducedHeightConfigs.ALPHA, 64, true));
        entries.add(SKYLANDS, createGeneratorSettings(registries, ModernBetaShapeReducedHeightConfigs.SKYLANDS, 0, false));
        entries.add(INFDEV_611, createGeneratorSettings(registries, ModernBetaShapeReducedHeightConfigs.INFDEV_611, 64, true));
        entries.add(INFDEV_420, createGeneratorSettings(registries, ModernBetaShapeReducedHeightConfigs.INFDEV_420, 64, true));
        entries.add(INFDEV_415, createGeneratorSettings(registries, ModernBetaShapeReducedHeightConfigs.INFDEV_415, 64, true));
        entries.add(INFDEV_227, createGeneratorSettings(registries, ModernBetaShapeReducedHeightConfigs.INFDEV_227, 64, true));
        entries.add(INDEV, createGeneratorSettings(registries, ModernBetaShapeReducedHeightConfigs.INDEV, 64, false));
        entries.add(CLASSIC_0_30, createGeneratorSettings(registries, ModernBetaShapeReducedHeightConfigs.CLASSIC_0_30, 64, false));
        entries.add(PE, createGeneratorSettings(registries, ModernBetaShapeReducedHeightConfigs.PE, 64, true));
        entries.add(EARLY_RELEASE, createGeneratorSettings(registries, ModernBetaShapeReducedHeightConfigs.EARLY_RELEASE, 63, true));
        entries.add(MAJOR_RELEASE, createGeneratorSettings(registries, ModernBetaShapeReducedHeightConfigs.MAJOR_RELEASE, 63, true));
        entries.add(EARLY_BEDROCK, createGeneratorSettings(registries, ModernBetaShapeReducedHeightConfigs.EARLY_BEDROCK, 63, true));

        //Density functions
        RegistryEntryLookup<DensityFunction> densityFunctionLookup = registries.getOrThrow(RegistryKeys.DENSITY_FUNCTION);
        RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> noiseParametersLookup = registries.getOrThrow(RegistryKeys.NOISE_PARAMETERS);

        entries.add(AccessorDensityFunctionsFabric.getCavesSpaghetti2d(), createCavesSpaghetti2dOverworldFunction(densityFunctionLookup, noiseParametersLookup));
        entries.add(AccessorDensityFunctions.getCavesEntrancesOverworldKey(), createCavesEntrancesOverworldFunction(densityFunctionLookup, noiseParametersLookup));
        entries.add(AccessorDensityFunctions.getCavesNoodleOverworldKey(), createCavesNoodleOverworldFunction(densityFunctionLookup, noiseParametersLookup));

        //Placed features
        RegistryEntryLookup<ConfiguredFeature<?, ?>> registryConfiguredFeature = registries.getOrThrow(RegistryKeys.CONFIGURED_FEATURE);
        RegistryEntry<ConfiguredFeature<?, ?>> noOp = new RegistryEntry.Direct<>(new ConfiguredFeature<>(Feature.NO_OP, DefaultFeatureConfig.DEFAULT));
        RegistryEntry<ConfiguredFeature<?, ?>> dirt = registryConfiguredFeature.getOrThrow(OreConfiguredFeatures.ORE_DIRT);
        RegistryEntry<ConfiguredFeature<?, ?>> gravel = registryConfiguredFeature.getOrThrow(OreConfiguredFeatures.ORE_GRAVEL);
        RegistryEntry<ConfiguredFeature<?, ?>> granite = registryConfiguredFeature.getOrThrow(OreConfiguredFeatures.ORE_GRANITE);
        RegistryEntry<ConfiguredFeature<?, ?>> diorite = registryConfiguredFeature.getOrThrow(OreConfiguredFeatures.ORE_DIORITE);
        RegistryEntry<ConfiguredFeature<?, ?>> andesite = registryConfiguredFeature.getOrThrow(OreConfiguredFeatures.ORE_ANDESITE);
        RegistryEntry<ConfiguredFeature<?, ?>> coal = registryConfiguredFeature.getOrThrow(OreConfiguredFeatures.ORE_COAL);
        RegistryEntry<ConfiguredFeature<?, ?>> iron = registryConfiguredFeature.getOrThrow(OreConfiguredFeatures.ORE_IRON);
        RegistryEntry<ConfiguredFeature<?, ?>> gold = registryConfiguredFeature.getOrThrow(OreConfiguredFeatures.ORE_GOLD);
        RegistryEntry<ConfiguredFeature<?, ?>> redstone = registryConfiguredFeature.getOrThrow(OreConfiguredFeatures.ORE_REDSTONE);
        RegistryEntry<ConfiguredFeature<?, ?>> diamond = entries.ref(ORE_DIAMOND_OLD);
        RegistryEntry<ConfiguredFeature<?, ?>> lapis = registryConfiguredFeature.getOrThrow(OreConfiguredFeatures.ORE_LAPIS);
        RegistryEntry<ConfiguredFeature<?, ?>> copperSmall = registryConfiguredFeature.getOrThrow(OreConfiguredFeatures.ORE_COPPER_SMALL);

        entries.add(OrePlacedFeatures.ORE_ANDESITE_UPPER, new PlacedFeature(noOp, List.of(CountPlacementModifier.of(0))));
        entries.add(OrePlacedFeatures.ORE_ANDESITE_LOWER, new PlacedFeature(andesite,
                modifiersWithCount(10, HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.aboveBottom(79)))));
        entries.add(OrePlacedFeatures.ORE_COAL_UPPER, new PlacedFeature(noOp, List.of(CountPlacementModifier.of(0))));
        entries.add(OrePlacedFeatures.ORE_COAL_LOWER, new PlacedFeature(coal,
                modifiersWithCount(20, HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.aboveBottom(127)))));
        entries.add(OrePlacedFeatures.ORE_COPPER, new PlacedFeature(copperSmall,
                modifiersWithCount(6, HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.aboveBottom(96)))));
        entries.add(OrePlacedFeatures.ORE_COPPER_LARGE, new PlacedFeature(noOp, List.of(CountPlacementModifier.of(0))));
        entries.add(OrePlacedFeatures.ORE_DIAMOND, new PlacedFeature(diamond,
                modifiersWithCount(1, HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.aboveBottom(15)))));
        entries.add(OrePlacedFeatures.ORE_DIAMOND_MEDIUM, new PlacedFeature(noOp, List.of(CountPlacementModifier.of(0))));
        entries.add(OrePlacedFeatures.ORE_DIAMOND_LARGE, new PlacedFeature(noOp, List.of(CountPlacementModifier.of(0))));
        entries.add(OrePlacedFeatures.ORE_DIAMOND_BURIED, new PlacedFeature(noOp, List.of(CountPlacementModifier.of(0))));
        entries.add(OrePlacedFeatures.ORE_DIORITE_UPPER, new PlacedFeature(noOp, List.of(CountPlacementModifier.of(0))));
        entries.add(OrePlacedFeatures.ORE_DIORITE_LOWER, new PlacedFeature(diorite,
                modifiersWithCount(10, HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.aboveBottom(79)))));
        entries.add(OrePlacedFeatures.ORE_DIRT, new PlacedFeature(dirt,
                modifiersWithCount(10, HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.aboveBottom(255)))));
        entries.add(OrePlacedFeatures.ORE_GOLD_EXTRA, new PlacedFeature(noOp, List.of(CountPlacementModifier.of(0))));
        entries.add(OrePlacedFeatures.ORE_GOLD, new PlacedFeature(gold,
                modifiersWithCount(2, HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.aboveBottom(31)))));
        entries.add(OrePlacedFeatures.ORE_GOLD_LOWER, new PlacedFeature(noOp, List.of(CountPlacementModifier.of(0))));
        entries.add(OrePlacedFeatures.ORE_GRANITE_UPPER, new PlacedFeature(noOp, List.of(CountPlacementModifier.of(0))));
        entries.add(OrePlacedFeatures.ORE_GRANITE_LOWER, new PlacedFeature(granite,
                modifiersWithCount(10, HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.aboveBottom(79)))));
        entries.add(OrePlacedFeatures.ORE_GRAVEL, new PlacedFeature(gravel,
                modifiersWithCount(10, HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.aboveBottom(255)))));
        entries.add(OrePlacedFeatures.ORE_IRON_UPPER, new PlacedFeature(noOp, List.of(CountPlacementModifier.of(0))));
        entries.add(OrePlacedFeatures.ORE_IRON_MIDDLE, new PlacedFeature(iron,
                modifiersWithCount(20, HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.aboveBottom(63)))));
        entries.add(OrePlacedFeatures.ORE_IRON_SMALL, new PlacedFeature(noOp, List.of(CountPlacementModifier.of(0))));
        entries.add(OrePlacedFeatures.ORE_LAPIS, new PlacedFeature(lapis,
                modifiersWithCount(1, HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.aboveBottom(31)))));
        entries.add(OrePlacedFeatures.ORE_LAPIS_BURIED, new PlacedFeature(noOp, List.of(CountPlacementModifier.of(0))));
        entries.add(OrePlacedFeatures.ORE_REDSTONE, new PlacedFeature(redstone,
                modifiersWithCount(8, HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.aboveBottom(15)))));
        entries.add(OrePlacedFeatures.ORE_REDSTONE_LOWER, new PlacedFeature(noOp, List.of(CountPlacementModifier.of(0))));
        entries.add(OrePlacedFeatures.ORE_TUFF, new PlacedFeature(noOp, List.of(CountPlacementModifier.of(0))));

        isGeneratingData = false;
    }

    private static ChunkGeneratorSettings createVanillaSurfaceSettings(WrapperLookup lookup, boolean amplified, boolean largeBiomes) {
        return new ChunkGeneratorSettings(
            ModernBetaShapeReducedHeightConfigs.VANILLA_SURFACE,
            Blocks.STONE.getDefaultState(),
            Blocks.WATER.getDefaultState(),
            AccessorDensityFunctionsFabric.invokeCreateSurfaceNoiseRouter(lookup.getOrThrow(RegistryKeys.DENSITY_FUNCTION),
                lookup.getOrThrow(RegistryKeys.NOISE_PARAMETERS), largeBiomes, amplified),
            VanillaSurfaceRules.createOverworldSurfaceRule(),
            (new VanillaBiomeParameters()).getSpawnSuitabilityNoises(),
            63,
            false,
            true,
            true,
            false
        );
    }

    private static ChunkGeneratorSettings createVanillaCavesSettings(WrapperLookup lookup) {
        return new ChunkGeneratorSettings(
            ModernBetaShapeReducedHeightConfigs.VANILLA_CAVES,
            Blocks.STONE.getDefaultState(),
            Blocks.WATER.getDefaultState(),
            AccessorDensityFunctionsFabric.invokeCreateNetherNoiseRouter(lookup.getOrThrow(RegistryKeys.DENSITY_FUNCTION),
                lookup.getOrThrow(RegistryKeys.NOISE_PARAMETERS)),
            VanillaSurfaceRules.createDefaultRule(false, true, true),
            List.of(),
            32,
            false,
            false,
            false,
            true
        );
    }

    private static DensityFunction createCavesEntrancesOverworldFunction(
            RegistryEntryLookup<DensityFunction> densityFunctionLookup,
            RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> noiseParametersLookup
    ) {
        DensityFunction spaghettiRarity = DensityFunctionTypes.cacheOnce(DensityFunctionTypes.noise(
                noiseParametersLookup.getOrThrow(NoiseParametersKeys.SPAGHETTI_3D_RARITY), 2.0, 1.0));
        DensityFunction spaghettiThickness = DensityFunctionTypes.noiseInRange(
                noiseParametersLookup.getOrThrow(NoiseParametersKeys.SPAGHETTI_3D_THICKNESS), -0.065, -0.088);

        DensityFunction weirdSpaghetti1 = DensityFunctionTypes.weirdScaledSampler(spaghettiRarity,
                noiseParametersLookup.getOrThrow(NoiseParametersKeys.SPAGHETTI_3D_1), DensityFunctionTypes.WeirdScaledSampler.RarityValueMapper.TYPE1);
        DensityFunction weirdSpaghetti2 = DensityFunctionTypes.weirdScaledSampler(spaghettiRarity,
                noiseParametersLookup.getOrThrow(NoiseParametersKeys.SPAGHETTI_3D_2), DensityFunctionTypes.WeirdScaledSampler.RarityValueMapper.TYPE1);

        DensityFunction mainSpaghetti = DensityFunctionTypes.add(DensityFunctionTypes.max(weirdSpaghetti1, weirdSpaghetti2), spaghettiThickness).clamp(-1.0, 1.0);
        DensityFunction spaghettiRoughness = new DensityFunctionTypes.RegistryEntryHolder(
                densityFunctionLookup.getOrThrow(AccessorDensityFunctionsFabric.getCavesSpaghettiRoughnessFunction()));

        DensityFunction entranceNoise = DensityFunctionTypes.noise(noiseParametersLookup.getOrThrow(
                NoiseParametersKeys.CAVE_ENTRANCE), 0.75, 0.5);
        DensityFunction mainEntrance = DensityFunctionTypes.add(
                DensityFunctionTypes.add(entranceNoise, DensityFunctionTypes.constant(0.37)), DensityFunctionTypes.yClampedGradient(10, 30, 0.3, 0.0)
        );
        return DensityFunctionTypes.cacheOnce(DensityFunctionTypes.min(mainEntrance, DensityFunctionTypes.add(spaghettiRoughness, mainSpaghetti)));
    }

    private static DensityFunction createCavesNoodleOverworldFunction(
            RegistryEntryLookup<DensityFunction> densityFunctionLookup,
            RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> noiseParametersLookup
    ) {
        DensityFunction y = new DensityFunctionTypes.RegistryEntryHolder(
                densityFunctionLookup.getOrThrow(AccessorDensityFunctionsFabric.getY()));

        int absMin = 0;
        int min = absMin + 4;
        int max = 320;
        DensityFunction noodleNoise = verticalRangeChoice(y, DensityFunctionTypes.noise(
                noiseParametersLookup.getOrThrow(NoiseParametersKeys.NOODLE), 1.0, 1.0), min, max, -1);
        DensityFunction noodleThickness = verticalRangeChoice(y, DensityFunctionTypes.noiseInRange(
                noiseParametersLookup.getOrThrow(NoiseParametersKeys.NOODLE_THICKNESS), 1.0, 1.0, -0.05, -0.1), min, max, 0);

        double scale = 8D / 3D;
        DensityFunction noodleRidgeA = verticalRangeChoice(y, DensityFunctionTypes.noise(
                noiseParametersLookup.getOrThrow(NoiseParametersKeys.NOODLE_RIDGE_A), scale, scale), min, max, 0);
        DensityFunction noodleRidgeB = verticalRangeChoice(y, DensityFunctionTypes.noise(
                noiseParametersLookup.getOrThrow(NoiseParametersKeys.NOODLE_RIDGE_B), scale, scale), min, max, 0);
        DensityFunction noodleRidges = DensityFunctionTypes.mul(DensityFunctionTypes.constant(1.5),
                DensityFunctionTypes.max(noodleRidgeA.abs(), noodleRidgeB.abs()));

        return DensityFunctionTypes.rangeChoice(noodleNoise, -1000000.0, 0.0,
                DensityFunctionTypes.constant(absMin + 64), DensityFunctionTypes.add(noodleThickness, noodleRidges));
    }

    private static DensityFunction createCavesSpaghetti2dOverworldFunction(
            RegistryEntryLookup<DensityFunction> densityFunctionLookup,
            RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> noiseParametersLookup
    ) {
        DensityFunction spaghettiModulator = DensityFunctionTypes.noise(noiseParametersLookup.getOrThrow(
                NoiseParametersKeys.SPAGHETTI_2D_MODULATOR), 2.0, 1.0);
        DensityFunction weirdSpaghetti = DensityFunctionTypes.weirdScaledSampler(spaghettiModulator, noiseParametersLookup.getOrThrow(
                NoiseParametersKeys.SPAGHETTI_2D), DensityFunctionTypes.WeirdScaledSampler.RarityValueMapper.TYPE2);
        DensityFunction spaghettiElevation = DensityFunctionTypes.noiseInRange(noiseParametersLookup.getOrThrow(
                NoiseParametersKeys.SPAGHETTI_2D_ELEVATION), 0.0, Math.floorDiv(-64, 8), 8.0);
        DensityFunction spaghettiThicknessModulator = new DensityFunctionTypes.RegistryEntryHolder(
                densityFunctionLookup.getOrThrow(AccessorDensityFunctionsFabric.getCavesSpaghetti2dThicknessModulator()));

        DensityFunction clampedElevation = DensityFunctionTypes.add(spaghettiElevation, DensityFunctionTypes.yClampedGradient(0, 320, 8.0, -40.0)).abs();
        DensityFunction minSpaghetti = DensityFunctionTypes.add(clampedElevation, spaghettiThicknessModulator).cube();
        double d = 0.083;
        DensityFunction maxSpaghetti = DensityFunctionTypes.add(weirdSpaghetti, DensityFunctionTypes.mul(
                DensityFunctionTypes.constant(d), spaghettiThicknessModulator));
        return DensityFunctionTypes.max(maxSpaghetti, minSpaghetti).clamp(-1.0, 1.0);
    }

    private static DensityFunction verticalRangeChoice(DensityFunction y, DensityFunction whenInRange, int minInclusive, int maxInclusive, int whenOutOfRange) {
        return DensityFunctionTypes.interpolated(DensityFunctionTypes.rangeChoice(y, minInclusive, maxInclusive + 1,
                whenInRange, DensityFunctionTypes.constant(whenOutOfRange)));
    }

    protected static List<PlacementModifier> modifiers(PlacementModifier countModifier, PlacementModifier heightModifier) {
        return List.of(countModifier, SquarePlacementModifier.of(), heightModifier, BiomePlacementModifier.of());
    }

    protected static List<PlacementModifier> modifiersWithCount(int count, PlacementModifier heightModifier) {
        return modifiers(CountPlacementModifier.of(count), heightModifier);
    }

    public static boolean isGeneratingData() {
        return isGeneratingData;
    }

    @Override
    public String getName() {
        return "Reduced Height Data";
    }
}
