package mod.bluestaggo.modernerbeta.fabric.data.reduced_height;

import com.mojang.datafixers.util.Either;
import mod.bluestaggo.modernerbeta.fabric.mixin.AccessorDensityFunctionsFabric;
import mod.bluestaggo.modernerbeta.mixin.AccessorDensityFunctions;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.world.carver.configured.ModernBetaConfiguredCarvers;
import mod.bluestaggo.modernerbeta.world.feature.configured.ModernBetaConfiguredFeatures;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGeneratorSettings.*;

public class ModernBetaReducedHeightDataProvider extends FabricDynamicRegistryProvider {
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_DEEPSLATE_OLD = ModernBetaConfiguredFeatures.of("ore_deepslate_old");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_DIAMOND_OLD = ModernBetaConfiguredFeatures.of("ore_diamond_old");

    private static boolean isGeneratingData;

    public ModernBetaReducedHeightDataProvider(FabricDataOutput output, CompletableFuture<Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(Provider registries, Entries entries) {
        isGeneratingData = true;

        //Dimension types
        entries.add(
            BuiltinDimensionTypes.OVERWORLD,
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
                BuiltinDimensionTypes.OVERWORLD_EFFECTS,
                0.0F,
                //? if >=1.21.6
                Optional.empty(),
                new DimensionType.MonsterSettings(false, true, UniformInt.of(0, 7), 0)
            )
        );

        //Configured carvers
        HolderLookup.RegistryLookup<Block> registryBlock = registries.lookupOrThrow(Registries.BLOCK);
        CaveCarverConfiguration configCaveDeep = new CaveCarverConfiguration(
            0.0f,
            ConstantHeight.of(VerticalAnchor.absolute(-2032)),
            ConstantFloat.of(0.0f),
            VerticalAnchor.absolute(-2032),
            CarverDebugSettings.of(false, Blocks.CRIMSON_BUTTON.defaultBlockState()),
            //? if >=1.21 {
            registryBlock.getOrThrow(BlockTags.AIR),
            //?} else {
            /*net.minecraft.core.HolderSet.emptyNamed(registryBlock,
                net.minecraft.tags.TagKey.create(Registries.BLOCK, mod.bluestaggo.modernerbeta.ModernerBeta.createId("air"))),
            *///?}
            ConstantFloat.of(0.0f),
            ConstantFloat.of(0.0f),
            ConstantFloat.of(0.0f)
        );
        entries.add(ModernBetaConfiguredCarvers.BETA_CAVE_DEEP, WorldCarver.CAVE.configured(configCaveDeep));

        //Configured features
        RuleTest overworldStone = new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD);
        RuleTest deepslateReplacers = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        entries.add(OreFeatures.ORE_GRANITE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(overworldStone, Blocks.GRANITE.defaultBlockState(), 33)));
        entries.add(OreFeatures.ORE_DIORITE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(overworldStone, Blocks.DIORITE.defaultBlockState(), 33)));
        entries.add(OreFeatures.ORE_ANDESITE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(overworldStone, Blocks.ANDESITE.defaultBlockState(), 33)));
        entries.add(OreFeatures.ORE_TUFF, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(overworldStone, Blocks.TUFF.defaultBlockState(), 33)));

        entries.add(ORE_DEEPSLATE_OLD, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(overworldStone, Blocks.DEEPSLATE.defaultBlockState(), 64)));
        entries.add(ORE_DIAMOND_OLD, new ConfiguredFeature<>(Feature.ORE,
            new OreConfiguration(
                List.of(
                    OreConfiguration.target(overworldStone, Blocks.DIAMOND_ORE.defaultBlockState()),
                    OreConfiguration.target(deepslateReplacers, Blocks.DEEPSLATE_DIAMOND_ORE.defaultBlockState())
                ), 8
            )
        ));

        //Chunk generator settings
        entries.add(NoiseGeneratorSettings.OVERWORLD, createVanillaSurfaceSettings(registries, false, false));
        entries.add(NoiseGeneratorSettings.LARGE_BIOMES, createVanillaSurfaceSettings(registries, false, true));
        entries.add(NoiseGeneratorSettings.AMPLIFIED, createVanillaSurfaceSettings(registries, true, false));
        entries.add(NoiseGeneratorSettings.CAVES, createVanillaCavesSettings(registries));

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
        HolderGetter<DensityFunction> densityFunctionLookup = registries.lookupOrThrow(Registries.DENSITY_FUNCTION);
        HolderGetter<NormalNoise.NoiseParameters> noiseParametersLookup = registries.lookupOrThrow(Registries.NOISE);

        entries.add(AccessorDensityFunctionsFabric.getSpaghetti2d(), createCavesSpaghetti2dOverworldFunction(densityFunctionLookup, noiseParametersLookup));
        entries.add(AccessorDensityFunctions.getEntrancesKey(), createCavesEntrancesOverworldFunction(densityFunctionLookup, noiseParametersLookup));
        entries.add(AccessorDensityFunctions.getNoodleKey(), createCavesNoodleOverworldFunction(densityFunctionLookup, noiseParametersLookup));

        //Placed features
        HolderGetter<ConfiguredFeature<?, ?>> registryConfiguredFeature = registries.lookupOrThrow(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> noOp = new Holder.Direct<>(new ConfiguredFeature<>(Feature.NO_OP, NoneFeatureConfiguration.NONE));
        Holder<ConfiguredFeature<?, ?>> dirt = registryConfiguredFeature.getOrThrow(OreFeatures.ORE_DIRT);
        Holder<ConfiguredFeature<?, ?>> gravel = registryConfiguredFeature.getOrThrow(OreFeatures.ORE_GRAVEL);
        Holder<ConfiguredFeature<?, ?>> granite = registryConfiguredFeature.getOrThrow(OreFeatures.ORE_GRANITE);
        Holder<ConfiguredFeature<?, ?>> diorite = registryConfiguredFeature.getOrThrow(OreFeatures.ORE_DIORITE);
        Holder<ConfiguredFeature<?, ?>> andesite = registryConfiguredFeature.getOrThrow(OreFeatures.ORE_ANDESITE);
        Holder<ConfiguredFeature<?, ?>> coal = registryConfiguredFeature.getOrThrow(OreFeatures.ORE_COAL);
        Holder<ConfiguredFeature<?, ?>> iron = registryConfiguredFeature.getOrThrow(OreFeatures.ORE_IRON);
        Holder<ConfiguredFeature<?, ?>> gold = registryConfiguredFeature.getOrThrow(OreFeatures.ORE_GOLD);
        Holder<ConfiguredFeature<?, ?>> redstone = registryConfiguredFeature.getOrThrow(OreFeatures.ORE_REDSTONE);
        Holder<ConfiguredFeature<?, ?>> diamond = alwaysSerializableHolder(entries.ref(ORE_DIAMOND_OLD));
        Holder<ConfiguredFeature<?, ?>> lapis = registryConfiguredFeature.getOrThrow(OreFeatures.ORE_LAPIS);
        Holder<ConfiguredFeature<?, ?>> copperSmall = registryConfiguredFeature.getOrThrow(OreFeatures.ORE_COPPPER_SMALL);

        entries.add(OrePlacements.ORE_ANDESITE_UPPER, new PlacedFeature(noOp, List.of(CountPlacement.of(0))));
        entries.add(OrePlacements.ORE_ANDESITE_LOWER, new PlacedFeature(andesite,
                modifiersWithCount(10, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(79)))));
        entries.add(OrePlacements.ORE_COAL_UPPER, new PlacedFeature(noOp, List.of(CountPlacement.of(0))));
        entries.add(OrePlacements.ORE_COAL_LOWER, new PlacedFeature(coal,
                modifiersWithCount(20, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(127)))));
        entries.add(OrePlacements.ORE_COPPER, new PlacedFeature(copperSmall,
                modifiersWithCount(6, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(96)))));
        entries.add(OrePlacements.ORE_COPPER_LARGE, new PlacedFeature(noOp, List.of(CountPlacement.of(0))));
        entries.add(OrePlacements.ORE_DIAMOND, new PlacedFeature(diamond,
                modifiersWithCount(1, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(15)))));
        //? if >=1.20.2
        entries.add(OrePlacements.ORE_DIAMOND_MEDIUM, new PlacedFeature(noOp, List.of(CountPlacement.of(0))));
        entries.add(OrePlacements.ORE_DIAMOND_LARGE, new PlacedFeature(noOp, List.of(CountPlacement.of(0))));
        entries.add(OrePlacements.ORE_DIAMOND_BURIED, new PlacedFeature(noOp, List.of(CountPlacement.of(0))));
        entries.add(OrePlacements.ORE_DIORITE_UPPER, new PlacedFeature(noOp, List.of(CountPlacement.of(0))));
        entries.add(OrePlacements.ORE_DIORITE_LOWER, new PlacedFeature(diorite,
                modifiersWithCount(10, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(79)))));
        entries.add(OrePlacements.ORE_DIRT, new PlacedFeature(dirt,
                modifiersWithCount(10, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(255)))));
        entries.add(OrePlacements.ORE_GOLD_EXTRA, new PlacedFeature(noOp, List.of(CountPlacement.of(0))));
        entries.add(OrePlacements.ORE_GOLD, new PlacedFeature(gold,
                modifiersWithCount(2, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(31)))));
        entries.add(OrePlacements.ORE_GOLD_LOWER, new PlacedFeature(noOp, List.of(CountPlacement.of(0))));
        entries.add(OrePlacements.ORE_GRANITE_UPPER, new PlacedFeature(noOp, List.of(CountPlacement.of(0))));
        entries.add(OrePlacements.ORE_GRANITE_LOWER, new PlacedFeature(granite,
                modifiersWithCount(10, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(79)))));
        entries.add(OrePlacements.ORE_GRAVEL, new PlacedFeature(gravel,
                modifiersWithCount(10, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(255)))));
        entries.add(OrePlacements.ORE_IRON_UPPER, new PlacedFeature(noOp, List.of(CountPlacement.of(0))));
        entries.add(OrePlacements.ORE_IRON_MIDDLE, new PlacedFeature(iron,
                modifiersWithCount(20, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(63)))));
        entries.add(OrePlacements.ORE_IRON_SMALL, new PlacedFeature(noOp, List.of(CountPlacement.of(0))));
        entries.add(OrePlacements.ORE_LAPIS, new PlacedFeature(lapis,
                modifiersWithCount(1, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(31)))));
        entries.add(OrePlacements.ORE_LAPIS_BURIED, new PlacedFeature(noOp, List.of(CountPlacement.of(0))));
        entries.add(OrePlacements.ORE_REDSTONE, new PlacedFeature(redstone,
                modifiersWithCount(8, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(15)))));
        entries.add(OrePlacements.ORE_REDSTONE_LOWER, new PlacedFeature(noOp, List.of(CountPlacement.of(0))));
        entries.add(OrePlacements.ORE_TUFF, new PlacedFeature(noOp, List.of(CountPlacement.of(0))));

        isGeneratingData = false;
    }

    private static NoiseGeneratorSettings createVanillaSurfaceSettings(Provider lookup, boolean amplified, boolean largeBiomes) {
        return new NoiseGeneratorSettings(
            ModernBetaShapeReducedHeightConfigs.VANILLA_SURFACE,
            Blocks.STONE.defaultBlockState(),
            Blocks.WATER.defaultBlockState(),
            AccessorDensityFunctionsFabric.invokeOverworld(lookup.lookupOrThrow(Registries.DENSITY_FUNCTION),
                lookup.lookupOrThrow(Registries.NOISE), largeBiomes, amplified),
            SurfaceRuleData.overworld(),
            (new OverworldBiomeBuilder()).spawnTarget(),
            63,
            false,
            true,
            true,
            false
        );
    }

    private static NoiseGeneratorSettings createVanillaCavesSettings(Provider lookup) {
        return new NoiseGeneratorSettings(
            ModernBetaShapeReducedHeightConfigs.VANILLA_CAVES,
            Blocks.STONE.defaultBlockState(),
            Blocks.WATER.defaultBlockState(),
            AccessorDensityFunctionsFabric.invokeNether(lookup.lookupOrThrow(Registries.DENSITY_FUNCTION),
                lookup.lookupOrThrow(Registries.NOISE)),
            SurfaceRuleData.overworldLike(false, true, true),
            List.of(),
            32,
            false,
            false,
            false,
            true
        );
    }

    private static DensityFunction createCavesEntrancesOverworldFunction(
            HolderGetter<DensityFunction> densityFunctionLookup,
            HolderGetter<NormalNoise.NoiseParameters> noiseParametersLookup
    ) {
        DensityFunction spaghettiRarity = DensityFunctions.cacheOnce(DensityFunctions.noise(
                noiseParametersLookup.getOrThrow(Noises.SPAGHETTI_3D_RARITY), 2.0, 1.0));
        DensityFunction spaghettiThickness = DensityFunctions.mappedNoise(
                noiseParametersLookup.getOrThrow(Noises.SPAGHETTI_3D_THICKNESS), -0.065, -0.088);

        DensityFunction weirdSpaghetti1 = DensityFunctions.weirdScaledSampler(spaghettiRarity,
                noiseParametersLookup.getOrThrow(Noises.SPAGHETTI_3D_1), DensityFunctions.WeirdScaledSampler.RarityValueMapper.TYPE1);
        DensityFunction weirdSpaghetti2 = DensityFunctions.weirdScaledSampler(spaghettiRarity,
                noiseParametersLookup.getOrThrow(Noises.SPAGHETTI_3D_2), DensityFunctions.WeirdScaledSampler.RarityValueMapper.TYPE1);

        DensityFunction mainSpaghetti = DensityFunctions.add(DensityFunctions.max(weirdSpaghetti1, weirdSpaghetti2), spaghettiThickness).clamp(-1.0, 1.0);
        DensityFunction spaghettiRoughness = new DensityFunctions.HolderHolder(
                densityFunctionLookup.getOrThrow(AccessorDensityFunctionsFabric.getSpaghettiRoughnessFunction()));

        DensityFunction entranceNoise = DensityFunctions.noise(noiseParametersLookup.getOrThrow(
                Noises.CAVE_ENTRANCE), 0.75, 0.5);
        DensityFunction mainEntrance = DensityFunctions.add(
                DensityFunctions.add(entranceNoise, DensityFunctions.constant(0.37)), DensityFunctions.yClampedGradient(10, 30, 0.3, 0.0)
        );
        return DensityFunctions.cacheOnce(DensityFunctions.min(mainEntrance, DensityFunctions.add(spaghettiRoughness, mainSpaghetti)));
    }

    private static DensityFunction createCavesNoodleOverworldFunction(
            HolderGetter<DensityFunction> densityFunctionLookup,
            HolderGetter<NormalNoise.NoiseParameters> noiseParametersLookup
    ) {
        DensityFunction y = new DensityFunctions.HolderHolder(
                densityFunctionLookup.getOrThrow(AccessorDensityFunctionsFabric.getY()));

        int absMin = 0;
        int min = absMin + 4;
        int max = 320;
        DensityFunction noodleNoise = verticalRangeChoice(y, DensityFunctions.noise(
                noiseParametersLookup.getOrThrow(Noises.NOODLE), 1.0, 1.0), min, max, -1);
        DensityFunction noodleThickness = verticalRangeChoice(y, DensityFunctions.mappedNoise(
                noiseParametersLookup.getOrThrow(Noises.NOODLE_THICKNESS), 1.0, 1.0, -0.05, -0.1), min, max, 0);

        double scale = 8D / 3D;
        DensityFunction noodleRidgeA = verticalRangeChoice(y, DensityFunctions.noise(
                noiseParametersLookup.getOrThrow(Noises.NOODLE_RIDGE_A), scale, scale), min, max, 0);
        DensityFunction noodleRidgeB = verticalRangeChoice(y, DensityFunctions.noise(
                noiseParametersLookup.getOrThrow(Noises.NOODLE_RIDGE_B), scale, scale), min, max, 0);
        DensityFunction noodleRidges = DensityFunctions.mul(DensityFunctions.constant(1.5),
                DensityFunctions.max(noodleRidgeA.abs(), noodleRidgeB.abs()));

        return DensityFunctions.rangeChoice(noodleNoise, -1000000.0, 0.0,
                DensityFunctions.constant(absMin + 64), DensityFunctions.add(noodleThickness, noodleRidges));
    }

    private static DensityFunction createCavesSpaghetti2dOverworldFunction(
            HolderGetter<DensityFunction> densityFunctionLookup,
            HolderGetter<NormalNoise.NoiseParameters> noiseParametersLookup
    ) {
        DensityFunction spaghettiModulator = DensityFunctions.noise(noiseParametersLookup.getOrThrow(
                Noises.SPAGHETTI_2D_MODULATOR), 2.0, 1.0);
        DensityFunction weirdSpaghetti = DensityFunctions.weirdScaledSampler(spaghettiModulator, noiseParametersLookup.getOrThrow(
                Noises.SPAGHETTI_2D), DensityFunctions.WeirdScaledSampler.RarityValueMapper.TYPE2);
        DensityFunction spaghettiElevation = DensityFunctions.mappedNoise(noiseParametersLookup.getOrThrow(
                Noises.SPAGHETTI_2D_ELEVATION), 0.0, Math.floorDiv(-64, 8), 8.0);
        DensityFunction spaghettiThicknessModulator = new DensityFunctions.HolderHolder(
                densityFunctionLookup.getOrThrow(AccessorDensityFunctionsFabric.getSpaghetti2dThicknessModulator()));

        DensityFunction clampedElevation = DensityFunctions.add(spaghettiElevation, DensityFunctions.yClampedGradient(0, 320, 8.0, -40.0)).abs();
        DensityFunction minSpaghetti = DensityFunctions.add(clampedElevation, spaghettiThicknessModulator).cube();
        double d = 0.083;
        DensityFunction maxSpaghetti = DensityFunctions.add(weirdSpaghetti, DensityFunctions.mul(
                DensityFunctions.constant(d), spaghettiThicknessModulator));
        return DensityFunctions.max(maxSpaghetti, minSpaghetti).clamp(-1.0, 1.0);
    }

    private static DensityFunction verticalRangeChoice(DensityFunction y, DensityFunction whenInRange, int minInclusive, int maxInclusive, int whenOutOfRange) {
        return DensityFunctions.interpolated(DensityFunctions.rangeChoice(y, minInclusive, maxInclusive + 1,
                whenInRange, DensityFunctions.constant(whenOutOfRange)));
    }

    protected static List<PlacementModifier> modifiers(PlacementModifier countModifier, PlacementModifier heightModifier) {
        return List.of(countModifier, InSquarePlacement.spread(), heightModifier, BiomeFilter.biome());
    }

    protected static List<PlacementModifier> modifiersWithCount(int count, PlacementModifier heightModifier) {
        return modifiers(CountPlacement.of(count), heightModifier);
    }

    protected static <T> Holder<T> alwaysSerializableHolder(Holder<T> entry) {
        return new Holder<>() {
            @Override
            public T value() {
                return entry.value();
            }

            @Override
            public boolean isBound() {
                return entry.isBound();
            }

            @Override
            public boolean is(ResourceLocation id) {
                return entry.is(id);
            }

            @Override
            public boolean is(ResourceKey<T> key) {
                return entry.is(key);
            }

            @Override
            public boolean is(Predicate<ResourceKey<T>> predicate) {
                return entry.is(predicate);
            }

            @Override
            public boolean is(TagKey<T> tag) {
                return entry.is(tag);
            }

            //? if >=1.21 {
            @SuppressWarnings("deprecation")
            @Override
            public boolean is(Holder<T> entry) {
                return entry.is(entry);
            }
            //?}

            @Override
            public Stream<TagKey<T>> tags() {
                return entry.tags();
            }

            @Override
            public Either<ResourceKey<T>, T> unwrap() {
                return entry.unwrap();
            }

            @Override
            public Optional<ResourceKey<T>> unwrapKey() {
                return entry.unwrapKey();
            }

            @Override
            public Kind kind() {
                return entry.kind();
            }

            @Override
            public boolean canSerializeIn(HolderOwner<T> owner) {
                return true;
            }
        };
    }

    public static boolean isGeneratingData() {
        return isGeneratingData;
    }

    @Override
    public String getName() {
        return "Reduced Height Data";
    }
}
