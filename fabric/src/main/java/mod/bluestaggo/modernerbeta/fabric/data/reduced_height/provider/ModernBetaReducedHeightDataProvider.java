package mod.bluestaggo.modernerbeta.fabric.data.reduced_height.provider;

import com.mojang.datafixers.util.Either;
import mod.bluestaggo.modernerbeta.fabric.data.reduced_height.ModernBetaReducedHeightNoiseSettings;
import mod.bluestaggo.modernerbeta.level.carver.configured.ModernBetaConfiguredCarvers;
import mod.bluestaggo.modernerbeta.level.feature.configured.ModernBetaConfiguredFeatures;
import mod.bluestaggo.modernerbeta.mixin.NoiseRouterDataAccessor;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.*;
import net.minecraft.core.HolderLookup.Provider;
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
//? if >=1.21.11
//import net.minecraft.world.attribute.*;
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
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static mod.bluestaggo.modernerbeta.level.chunk.ModernBetaNoiseGeneratorSettings.*;

public class ModernBetaReducedHeightDataProvider extends FabricDynamicRegistryProvider {
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_DEEPSLATE_OLD = ModernBetaConfiguredFeatures.of("ore_deepslate_old");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_DIAMOND_OLD = ModernBetaConfiguredFeatures.of("ore_diamond_old");

    private static boolean isGeneratingData;

    public ModernBetaReducedHeightDataProvider(FabricDataOutput output, CompletableFuture<Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(Provider provider, Entries entries) {
        isGeneratingData = true;
        //? >=26.2
        //HolderGetter<Block> blocks = provider.lookupOrThrow(Registries.BLOCK);

        //Dimension types
        entries.add(
            BuiltinDimensionTypes.OVERWORLD,
            new DimensionType(
                //? if <1.21.11 {
                java.util.OptionalLong.empty(),
                true,
                false,
                false,
                true,
                //? } else {
                /*false,
                true,
                false,
                //? if >=26.1
                //false,
                *///? }
                1.0,
                //? if <1.21.11 {
                true,
                false,
                //? }
                0,
                320,
                320,
                //? if >=26.2 {
                /*blocks.getOrThrow(BlockTags.INFINIBURN_OVERWORLD),
                *///? } else {
                BlockTags.INFINIBURN_OVERWORLD,
                //? }
                //? if <1.21.11
                BuiltinDimensionTypes.OVERWORLD_EFFECTS,
                0.0F,
                //? if >=1.21.6 && <1.21.11
                Optional.empty(),
                new DimensionType.MonsterSettings(/*? if <1.21.11 {*/ false, true, /*?}*/ UniformInt.of(0, 7), 0)
                //? if >=1.21.11 {
                /*, DimensionType.Skybox.OVERWORLD,
                DimensionType.CardinalLightType.DEFAULT,
                EnvironmentAttributeMap.builder()
                    .set(EnvironmentAttributes.FOG_COLOR, 0xFFC0D8FF)
                    .set(EnvironmentAttributes.SKY_COLOR,
                            net.minecraft.data.worldgen.biome.OverworldBiomes.calculateSkyColor(0.8F))
                    .set(EnvironmentAttributes.CLOUD_COLOR, net.minecraft.util.ARGB.white(0.8F))
                    .set(EnvironmentAttributes.CLOUD_HEIGHT, 192.33F)
                    .set(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic.OVERWORLD)
                    .set(EnvironmentAttributes.BED_RULE, BedRule.CAN_SLEEP_WHEN_DARK)
                    .set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, false)
                    .set(EnvironmentAttributes.NETHER_PORTAL_SPAWNS_PIGLINS, true)
                    .set(EnvironmentAttributes.AMBIENT_SOUNDS, AmbientSounds.LEGACY_CAVE_SETTINGS)
                    .build(),
                provider.lookupOrThrow(Registries.TIMELINE).getOrThrow(net.minecraft.tags.TimelineTags.IN_OVERWORLD)
                *///? }
                //? if >=26.1 {
                /*, Optional.of(provider.lookupOrThrow(Registries.WORLD_CLOCK).getOrThrow(net.minecraft.world.clock.WorldClocks.OVERWORLD))
                *///? }
            )
        );

        //Configured carvers
        @SuppressWarnings("deprecation")
        CaveCarverConfiguration configCaveDeep = new CaveCarverConfiguration(
            0.0f,
            ConstantHeight.of(VerticalAnchor.absolute(-2032)),
            ConstantFloat.of(0.0f),
            VerticalAnchor.absolute(-2032),
            CarverDebugSettings.of(false, Blocks.CRIMSON_BUTTON.defaultBlockState()),
            HolderSet.direct(Block::builtInRegistryHolder, Blocks.AIR),
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
        entries.add(NoiseGeneratorSettings.OVERWORLD, createVanillaSurfaceSettings(provider, false, false));
        entries.add(NoiseGeneratorSettings.LARGE_BIOMES, createVanillaSurfaceSettings(provider, false, true));
        entries.add(NoiseGeneratorSettings.AMPLIFIED, createVanillaSurfaceSettings(provider, true, false));
        entries.add(NoiseGeneratorSettings.CAVES, createVanillaCavesSettings(provider));

        entries.add(INFDEV_415, createNoiseGeneratorSettings(provider, ModernBetaReducedHeightNoiseSettings.INFDEV_415, 64, true));
        entries.add(OVERWORLD_128, createNoiseGeneratorSettings(provider, ModernBetaReducedHeightNoiseSettings.OVERWORLD_128, 64, true));
        entries.add(OVERWORLD_256, createNoiseGeneratorSettings(provider, ModernBetaReducedHeightNoiseSettings.OVERWORLD_256, 64, true));

        //Density functions
        HolderGetter<DensityFunction> densityFunctionLookup = provider.lookupOrThrow(Registries.DENSITY_FUNCTION);
        HolderGetter<NormalNoise.NoiseParameters> noiseParametersLookup = provider.lookupOrThrow(Registries.NOISE);

        entries.add(mod.bluestaggo.modernerbeta.fabric.mixin.NoiseRouterDataAccessor.getSpaghetti2d(), createCavesSpaghetti2dOverworldFunction(densityFunctionLookup, noiseParametersLookup));
        entries.add(NoiseRouterDataAccessor.getEntrancesKey(), createCavesEntrancesOverworldFunction(densityFunctionLookup, noiseParametersLookup));
        entries.add(NoiseRouterDataAccessor.getNoodleKey(), createCavesNoodleOverworldFunction(densityFunctionLookup, noiseParametersLookup));

        //Placed features
        HolderGetter<ConfiguredFeature<?, ?>> registryConfiguredFeature = provider.lookupOrThrow(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> noOp = Holder.direct(new ConfiguredFeature<>(Feature.NO_OP, NoneFeatureConfiguration.NONE));
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

    private static NoiseGeneratorSettings createVanillaSurfaceSettings(Provider provider, boolean amplified, boolean largeBiomes) {
        return new NoiseGeneratorSettings(
            ModernBetaReducedHeightNoiseSettings.VANILLA_SURFACE,
            Blocks.STONE.defaultBlockState(),
            Blocks.WATER.defaultBlockState(),
            mod.bluestaggo.modernerbeta.fabric.mixin.NoiseRouterDataAccessor.invokeOverworld(provider.lookupOrThrow(Registries.DENSITY_FUNCTION),
                provider.lookupOrThrow(Registries.NOISE), largeBiomes, amplified),
            SurfaceRuleData.overworld(),
            (new OverworldBiomeBuilder()).spawnTarget(),
            63,
            false,
            true,
            true,
            false
        );
    }

    private static NoiseGeneratorSettings createVanillaCavesSettings(Provider provider) {
        return new NoiseGeneratorSettings(
            ModernBetaReducedHeightNoiseSettings.VANILLA_CAVES,
            Blocks.STONE.defaultBlockState(),
            Blocks.WATER.defaultBlockState(),
            mod.bluestaggo.modernerbeta.fabric.mixin.NoiseRouterDataAccessor.invokeNether(provider.lookupOrThrow(Registries.DENSITY_FUNCTION),
                provider.lookupOrThrow(Registries.NOISE)),
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
                densityFunctionLookup.getOrThrow(mod.bluestaggo.modernerbeta.fabric.mixin.NoiseRouterDataAccessor.getSpaghettiRoughnessFunction()));

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
                densityFunctionLookup.getOrThrow(mod.bluestaggo.modernerbeta.fabric.mixin.NoiseRouterDataAccessor.getY()));

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
                densityFunctionLookup.getOrThrow(mod.bluestaggo.modernerbeta.fabric.mixin.NoiseRouterDataAccessor.getSpaghetti2dThicknessModulator()));

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

    protected static <T> Holder<T> alwaysSerializableHolder(Holder<T> holder) {
        return new Holder<>() {
            @Override
            public @NotNull T value() {
                return holder.value();
            }

            @Override
            public boolean isBound() {
                return holder.isBound();
            }

            //? if >=26.1 {
            /*@Override
            public boolean areComponentsBound() {
                return holder.areComponentsBound();
            }
            *///? }

            @Override
            public boolean is(ResourceLocation id) {
                return holder.is(id);
            }

            @Override
            public boolean is(ResourceKey<T> key) {
                return holder.is(key);
            }

            @Override
            public boolean is(Predicate<ResourceKey<T>> predicate) {
                return holder.is(predicate);
            }

            @Override
            public boolean is(TagKey<T> tag) {
                return holder.is(tag);
            }

            //? if >=1.21 {
            @SuppressWarnings("deprecation")
            @Override
            public boolean is(Holder<T> entry) {
                return entry.is(entry);
            }
            //?}

            @Override
            public @NotNull Stream<TagKey<T>> tags() {
                return holder.tags();
            }

            //? if >=26.1 {
            /*@Override
            public net.minecraft.core.component.DataComponentMap components() {
                return holder.components();
            }
            *///? }

            @Override
            public @NotNull Either<ResourceKey<T>, T> unwrap() {
                return holder.unwrap();
            }

            @Override
            public @NotNull Optional<ResourceKey<T>> unwrapKey() {
                return holder.unwrapKey();
            }

            @Override
            public @NotNull Kind kind() {
                return holder.kind();
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
    public @NotNull String getName() {
        return "Reduced Height Data";
    }
}
