package mod.bluestaggo.modernerbeta.world.feature.placed;

import com.google.common.collect.ImmutableList;
import mod.bluestaggo.modernerbeta.world.feature.ModernBetaFeatureTags;
import mod.bluestaggo.modernerbeta.world.feature.configured.ModernBetaVegetationConfiguredFeatures;
import mod.bluestaggo.modernerbeta.world.feature.placement.*;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;

import java.util.List;
import java.util.stream.IntStream;

public class ModernBetaVegetationPlacedFeatures {
    public static final PlacementModifier SURFACE_WATER_DEPTH = SurfaceWaterDepthFilter.forMaxDepth(0);
    public static final PlacementModifier HEIGHTMAP_SPREAD_DOUBLE = HeightmapSpreadDoublePlacementModifier.of(Heightmap.Types.MOTION_BLOCKING);
    public static final PlacementModifier HEIGHT_RANGE_128 = HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(127));
    public static final PlacementModifier WORLD_SURFACE_WG_HEIGHTMAP = PlacementUtils.HEIGHTMAP_WORLD_SURFACE;
    public static final PlacementModifier MOTION_BLOCKING_HEIGHTMAP = PlacementUtils.HEIGHTMAP;

    private static final PlacementModifier INFDEV_325_TREE_PREDICATES = BlockPredicateFilter.forPredicate(
            BlockPredicate.not(
                    BlockPredicate.anyOf(
                            IntStream.range(0, 25 * 6)
                                    .mapToObj(i -> new Vec3i((i % 5) - 2, i / 25, (i / 5 % 5) - 2))
                                    .filter(v -> v.getX() % 2 != 0 || v.getZ() % 2 != 0)
                                    .map(v -> BlockPredicate.matchesBlocks(v, Blocks.OAK_LEAVES, Blocks.OAK_LOG)).toList())));

    private static ImmutableList.Builder<PlacementModifier> withBaseTreeModifiers(PlacementModifier modifier) {
        return ImmutableList.<PlacementModifier>builder()
            .add(modifier)
            .add(InSquarePlacement.spread())
            .add(SURFACE_WATER_DEPTH)
            .add(PlacementUtils.HEIGHTMAP_OCEAN_FLOOR)
            .add(BiomeFilter.biome());
    }
    
    private static ImmutableList.Builder<PlacementModifier> withBaseModifiers(PlacementModifier modifier) {
        return ImmutableList.<PlacementModifier>builder()
            .add(modifier)
            .add(InSquarePlacement.spread())
            .add(BiomeFilter.biome());
    }

    public static List<PlacementModifier> withTreeModifier(PlacementModifier modifier) {
        return withBaseTreeModifiers(modifier).build();
    }
    
    public static List<PlacementModifier> withCountModifier(int count) {
        return withBaseModifiers(CountPlacement.of(count)).build();
    }
    
    public static List<PlacementModifier> withCountExtraAndTreeModifier(int count, float extraChance, int extraCount) {
        return withTreeModifier(PlacementUtils.countExtra(count, extraChance, extraCount));
    }
    
    public static PlacementModifier withCountExtraModifier(int count, float extraChance, int extraCount) {
        return PlacementUtils.countExtra(count, extraChance, extraCount);
    }
    
    public static List<PlacementModifier> withNoiseBasedCountModifier(String id, NoiseBasedCountPlacementModifier modifier) {
        return withBaseTreeModifiers(modifier).build();
    }
    
    public static final ResourceKey<PlacedFeature> PATCH_CACTUS_ALPHA = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.PATCH_CACTUS_ALPHA);
    public static final ResourceKey<PlacedFeature> PATCH_CACTUS_PE = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.PATCH_CACTUS_PE);
    public static final ResourceKey<PlacedFeature> MUSHROOM_HELL = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.MUSHROOM_HELL);
    
    public static final ResourceKey<PlacedFeature> PATCH_DANDELION_2 = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.PATCH_DANDELION_2);
    public static final ResourceKey<PlacedFeature> PATCH_DANDELION_3 = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.PATCH_DANDELION_3);
    public static final ResourceKey<PlacedFeature> PATCH_DANDELION_4 = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.PATCH_DANDELION_4);
    public static final ResourceKey<PlacedFeature> PATCH_DANDELION = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.PATCH_DANDELION);
    public static final ResourceKey<PlacedFeature> PATCH_POPPY = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.PATCH_POPPY);
    public static final ResourceKey<PlacedFeature> PATCH_FLOWER_PARADISE = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.PATCH_FLOWER_INDEV_PARADISE);
    public static final ResourceKey<PlacedFeature> PATCH_DANDELION_INFDEV_227 = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.PATCH_DANDELION_INFDEV_227);

    public static final ResourceKey<PlacedFeature> PATCH_GRASS_PLAINS_10 = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.PATCH_GRASS_PLAINS_10);
    public static final ResourceKey<PlacedFeature> PATCH_GRASS_TAIGA_1 = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.PATCH_GRASS_TAIGA_1);
    public static final ResourceKey<PlacedFeature> PATCH_GRASS_RAINFOREST_10 = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.PATCH_GRASS_RAINFOREST_10);
    public static final ResourceKey<PlacedFeature> PATCH_GRASS_ALPHA_2 = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.PATCH_GRASS_ALPHA_2);
    
    public static final ResourceKey<PlacedFeature> TREES_ALPHA = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_ALPHA);
    public static final ResourceKey<PlacedFeature> TREES_INFDEV_611 = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_611);
    public static final ResourceKey<PlacedFeature> TREES_INFDEV_420 = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_420);
    public static final ResourceKey<PlacedFeature> TREES_INFDEV_415 = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_415);
    public static final ResourceKey<PlacedFeature> TREES_INFDEV_325 = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_325);
    public static final ResourceKey<PlacedFeature> TREES_INFDEV_227 = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_227);

    public static final ResourceKey<PlacedFeature> TREES_ALPHA_BEES = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_ALPHA_BEES);
    public static final ResourceKey<PlacedFeature> TREES_INFDEV_611_BEES = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_611_BEES);
    public static final ResourceKey<PlacedFeature> TREES_INFDEV_420_BEES = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_420_BEES);
    public static final ResourceKey<PlacedFeature> TREES_INFDEV_415_BEES = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_415_BEES);
    public static final ResourceKey<PlacedFeature> TREES_INFDEV_325_BEES = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_325_BEES);
    public static final ResourceKey<PlacedFeature> TREES_INFDEV_227_BEES = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_227_BEES);
    
    public static final ResourceKey<PlacedFeature> TREES_BETA_FOREST = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_BETA_FOREST);
    public static final ResourceKey<PlacedFeature> TREES_BETA_RAINFOREST = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_BETA_RAINFOREST);
    public static final ResourceKey<PlacedFeature> TREES_BETA_SEASONAL_FOREST = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_BETA_SEASONAL_FOREST);
    public static final ResourceKey<PlacedFeature> TREES_BETA_SPARSE = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_BETA_SPARSE);
    public static final ResourceKey<PlacedFeature> TREES_BETA_TAIGA = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_BETA_TAIGA);
    public static final ResourceKey<PlacedFeature> TREES_BETA_OAK_FOREST = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_BETA_OAK_FOREST);

    public static final ResourceKey<PlacedFeature> TREES_BETA_FOREST_BEES = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_BETA_FOREST_BEES);
    public static final ResourceKey<PlacedFeature> TREES_BETA_RAINFOREST_BEES = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_BETA_RAINFOREST_BEES);
    public static final ResourceKey<PlacedFeature> TREES_BETA_SEASONAL_FOREST_BEES = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_BETA_SEASONAL_FOREST_BEES);
    public static final ResourceKey<PlacedFeature> TREES_BETA_SPARSE_BEES = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_BETA_SPARSE_BEES);
    public static final ResourceKey<PlacedFeature> TREES_BETA_OAK_FOREST_BEES = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_BETA_OAK_FOREST_BEES);

    public static final ResourceKey<PlacedFeature> TREES_PE_FOREST = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_PE_FOREST);
    public static final ResourceKey<PlacedFeature> TREES_PE_RAINFOREST = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_PE_RAINFOREST);
    public static final ResourceKey<PlacedFeature> TREES_PE_SEASONAL_FOREST = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_PE_SEASONAL_FOREST);
    public static final ResourceKey<PlacedFeature> TREES_PE_SPARSE = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_PE_SPARSE);
    public static final ResourceKey<PlacedFeature> TREES_PE_TAIGA = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_PE_TAIGA);

    public static final ResourceKey<PlacedFeature> TREES_PE_FOREST_BEES = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_PE_FOREST_BEES);
    public static final ResourceKey<PlacedFeature> TREES_PE_RAINFOREST_BEES = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_PE_RAINFOREST_BEES);
    public static final ResourceKey<PlacedFeature> TREES_PE_SEASONAL_FOREST_BEES = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_PE_SEASONAL_FOREST_BEES);
    public static final ResourceKey<PlacedFeature> TREES_PE_SPARSE_BEES = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_PE_SPARSE_BEES);

    public static final ResourceKey<PlacedFeature> TREES_INDEV = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_INDEV);
    public static final ResourceKey<PlacedFeature> TREES_INDEV_WOODS = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_INDEV_WOODS);
    public static final ResourceKey<PlacedFeature> TREES_CLASSIC_14A_08 = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_CLASSIC_14A_08);

    public static final ResourceKey<PlacedFeature> TREES_INDEV_BEES = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_INDEV_BEES);
    public static final ResourceKey<PlacedFeature> TREES_INDEV_WOODS_BEES = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_INDEV_WOODS_BEES);
    public static final ResourceKey<PlacedFeature> TREES_CLASSIC_14A_08_BEES = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.TREES_CLASSIC_14A_08_BEES);

    public static void bootstrap(BootstrapContext<PlacedFeature> featureRegisterable) {
        HolderGetter<ConfiguredFeature<?, ?>> registryConfigured = featureRegisterable.lookup(Registries.CONFIGURED_FEATURE);

        Holder.Reference<ConfiguredFeature<?, ?>> patchCactus = registryConfigured.getOrThrow(VegetationFeatures.PATCH_CACTUS);
        Holder.Reference<ConfiguredFeature<?, ?>> mushroomHell = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.MUSHROOM_HELL);
        
        Holder.Reference<ConfiguredFeature<?, ?>> patchDandelion = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.PATCH_DANDELION);
        Holder.Reference<ConfiguredFeature<?, ?>> patchPoppy = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.PATCH_POPPY);
        Holder.Reference<ConfiguredFeature<?, ?>> flowerDefault = registryConfigured.getOrThrow(VegetationFeatures.FLOWER_DEFAULT);
        Holder.Reference<ConfiguredFeature<?, ?>> patchDandelionInfdev227 = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.PATCH_DANDELION_INFDEV_227);
        
        Holder.Reference<ConfiguredFeature<?, ?>> patchGrass = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.PATCH_GRASS);
        Holder.Reference<ConfiguredFeature<?, ?>> patchGrassLush = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.PATCH_GRASS_LUSH);

        Holder.Reference<ConfiguredFeature<?, ?>> treesAlpha = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_ALPHA);
        Holder.Reference<ConfiguredFeature<?, ?>> treesInfdev611 = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_611);
        Holder.Reference<ConfiguredFeature<?, ?>> treesInfdev420 = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_420);
        Holder.Reference<ConfiguredFeature<?, ?>> treesInfdev415 = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_415);
        Holder.Reference<ConfiguredFeature<?, ?>> treesInfdev325 = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_325);
        Holder.Reference<ConfiguredFeature<?, ?>> treesInfdev227 = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_227);

        Holder.Reference<ConfiguredFeature<?, ?>> treesAlphaBees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_ALPHA_BEES);
        Holder.Reference<ConfiguredFeature<?, ?>> treesInfdev611Bees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_611_BEES);
        Holder.Reference<ConfiguredFeature<?, ?>> treesInfdev420Bees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_420_BEES);
        Holder.Reference<ConfiguredFeature<?, ?>> treesInfdev415Bees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_415_BEES);
        Holder.Reference<ConfiguredFeature<?, ?>> treesInfdev325Bees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_325_BEES);
        Holder.Reference<ConfiguredFeature<?, ?>> treesInfdev227Bees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_227_BEES);

        Holder.Reference<ConfiguredFeature<?, ?>> treesBetaForest = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_BETA_FOREST);
        Holder.Reference<ConfiguredFeature<?, ?>> treesBetaRainforest = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_BETA_RAINFOREST);
        Holder.Reference<ConfiguredFeature<?, ?>> treesBetaSeasonalForest = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_BETA_SEASONAL_FOREST);
        Holder.Reference<ConfiguredFeature<?, ?>> treesBetaSparse = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_BETA_SPARSE);
        Holder.Reference<ConfiguredFeature<?, ?>> treesBetaTaiga = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_BETA_TAIGA);
        Holder.Reference<ConfiguredFeature<?, ?>> treesBetaOakForest = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_BETA_OAK_FOREST);

        Holder.Reference<ConfiguredFeature<?, ?>> treesBetaForestBees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_BETA_FOREST_BEES);
        Holder.Reference<ConfiguredFeature<?, ?>> treesBetaRainforestBees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_BETA_RAINFOREST_BEES);
        Holder.Reference<ConfiguredFeature<?, ?>> treesBetaSeasonalForestBees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_BETA_SEASONAL_FOREST_BEES);
        Holder.Reference<ConfiguredFeature<?, ?>> treesBetaSparseBees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_BETA_SPARSE_BEES);
        Holder.Reference<ConfiguredFeature<?, ?>> treesBetaOakForestBees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_BETA_OAK_FOREST_BEES);

        Holder.Reference<ConfiguredFeature<?, ?>> treesPEForest = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_PE_FOREST);
        Holder.Reference<ConfiguredFeature<?, ?>> treesPERainforest = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_PE_RAINFOREST);
        Holder.Reference<ConfiguredFeature<?, ?>> treesPESeasonalForest = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_PE_SEASONAL_FOREST);
        Holder.Reference<ConfiguredFeature<?, ?>> treesPESparse = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_PE_SPARSE);
        Holder.Reference<ConfiguredFeature<?, ?>> treesPETaiga = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_PE_TAIGA);

        Holder.Reference<ConfiguredFeature<?, ?>> treesPEForestBees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_PE_FOREST_BEES);
        Holder.Reference<ConfiguredFeature<?, ?>> treesPERainforestBees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_PE_RAINFOREST_BEES);
        Holder.Reference<ConfiguredFeature<?, ?>> treesPESeasonalForestBees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_PE_SEASONAL_FOREST_BEES);
        Holder.Reference<ConfiguredFeature<?, ?>> treesPESparseBees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_PE_SPARSE_BEES);

        Holder.Reference<ConfiguredFeature<?, ?>> treesIndev = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INDEV);
        Holder.Reference<ConfiguredFeature<?, ?>> treesIndevWoods = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INDEV_WOODS);
        Holder.Reference<ConfiguredFeature<?, ?>> treesClassic14a08 = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_CLASSIC_14A_08);

        Holder.Reference<ConfiguredFeature<?, ?>> treesIndevBees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INDEV_BEES);
        Holder.Reference<ConfiguredFeature<?, ?>> treesIndevWoodsBees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INDEV_WOODS_BEES);
        Holder.Reference<ConfiguredFeature<?, ?>> treesClassic14a08Bees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_CLASSIC_14A_08_BEES);

        PlacementUtils.register(featureRegisterable, PATCH_CACTUS_ALPHA, patchCactus, CountPlacement.of(2), InSquarePlacement.spread(), HEIGHTMAP_SPREAD_DOUBLE, BiomeFilter.biome());
        PlacementUtils.register(featureRegisterable, PATCH_CACTUS_PE, patchCactus, CountPlacement.of(5), InSquarePlacement.spread(), HEIGHTMAP_SPREAD_DOUBLE, BiomeFilter.biome());
        PlacementUtils.register(featureRegisterable, MUSHROOM_HELL, mushroomHell, CountPlacement.of(1), InSquarePlacement.spread(), MOTION_BLOCKING_HEIGHTMAP, BiomeFilter.biome());
        
        PlacementUtils.register(featureRegisterable, PATCH_DANDELION_2, patchDandelion, CountPlacement.of(2), InSquarePlacement.spread(), HEIGHT_RANGE_128, BiomeFilter.biome());
        PlacementUtils.register(featureRegisterable, PATCH_DANDELION_3, patchDandelion, CountPlacement.of(3), InSquarePlacement.spread(), HEIGHT_RANGE_128, BiomeFilter.biome());
        PlacementUtils.register(featureRegisterable, PATCH_DANDELION_4, patchDandelion, CountPlacement.of(4), InSquarePlacement.spread(), HEIGHT_RANGE_128, BiomeFilter.biome());
        PlacementUtils.register(featureRegisterable, PATCH_DANDELION, patchDandelion, withCountExtraModifier(0, 0.5f, 1), InSquarePlacement.spread(), HEIGHT_RANGE_128, BiomeFilter.biome());
        PlacementUtils.register(featureRegisterable, PATCH_POPPY, patchPoppy, withCountExtraModifier(0, 0.5f, 1), InSquarePlacement.spread(), HEIGHT_RANGE_128, BiomeFilter.biome());
        PlacementUtils.register(featureRegisterable, PATCH_FLOWER_PARADISE, flowerDefault, CountPlacement.of(20), InSquarePlacement.spread(), HEIGHT_RANGE_128, BiomeFilter.biome());
        PlacementUtils.register(featureRegisterable, PATCH_DANDELION_INFDEV_227, patchDandelionInfdev227, CountPlacement.of(UniformInt.of(0, 10)), InSquarePlacement.spread(), SURFACE_WATER_DEPTH, PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome());
    
        PlacementUtils.register(featureRegisterable, PATCH_GRASS_PLAINS_10, patchGrass, CountPlacement.of(10), InSquarePlacement.spread(), WORLD_SURFACE_WG_HEIGHTMAP, BiomeFilter.biome());
        PlacementUtils.register(featureRegisterable, PATCH_GRASS_TAIGA_1, patchGrass, CountPlacement.of(1), InSquarePlacement.spread(), WORLD_SURFACE_WG_HEIGHTMAP, BiomeFilter.biome());
        PlacementUtils.register(featureRegisterable, PATCH_GRASS_RAINFOREST_10, patchGrassLush, CountPlacement.of(10), InSquarePlacement.spread(), WORLD_SURFACE_WG_HEIGHTMAP, BiomeFilter.biome());
        PlacementUtils.register(featureRegisterable, PATCH_GRASS_ALPHA_2, patchGrass, withCountExtraModifier(0, 0.05f, 1), InSquarePlacement.spread(), WORLD_SURFACE_WG_HEIGHTMAP, BiomeFilter.biome());
        
        PlacementUtils.register(featureRegisterable, TREES_ALPHA, treesAlpha, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_ALPHA, NoiseBasedCountPlacementModifierAlpha.of(0, 0.1f, 1)));
        PlacementUtils.register(featureRegisterable, TREES_INFDEV_611, treesInfdev611, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_INFDEV_611, NoiseBasedCountPlacementModifierInfdev611.of(0, 0.1f, 1)));
        PlacementUtils.register(featureRegisterable, TREES_INFDEV_420, treesInfdev420, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_INFDEV_420, NoiseBasedCountPlacementModifierInfdev420.of(0, 0.01f, 1)));
        PlacementUtils.register(featureRegisterable, TREES_INFDEV_415, treesInfdev415, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_INFDEV_415, NoiseBasedCountPlacementModifierInfdev415.of(0, 0, 0)));
        PlacementUtils.register(featureRegisterable, TREES_INFDEV_325, treesInfdev325, withBaseTreeModifiers(NoiseBasedCountPlacementModifierInfdev325.of(0, 0, 0)).add(INFDEV_325_TREE_PREDICATES).build());
        PlacementUtils.register(featureRegisterable, TREES_INFDEV_227, treesInfdev227, withCountExtraAndTreeModifier(0, 0.1f, 1));

        PlacementUtils.register(featureRegisterable, TREES_ALPHA_BEES, treesAlphaBees, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_ALPHA_BEES, NoiseBasedCountPlacementModifierAlpha.of(0, 0.1f, 1)));
        PlacementUtils.register(featureRegisterable, TREES_INFDEV_611_BEES, treesInfdev611Bees, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_INFDEV_611_BEES, NoiseBasedCountPlacementModifierInfdev611.of(0, 0.1f, 1)));
        PlacementUtils.register(featureRegisterable, TREES_INFDEV_420_BEES, treesInfdev420Bees, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_INFDEV_420_BEES, NoiseBasedCountPlacementModifierInfdev420.of(0, 0.01f, 1)));
        PlacementUtils.register(featureRegisterable, TREES_INFDEV_415_BEES, treesInfdev415Bees, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_INFDEV_415_BEES, NoiseBasedCountPlacementModifierInfdev415.of(0, 0, 0)));
        PlacementUtils.register(featureRegisterable, TREES_INFDEV_325_BEES, treesInfdev325Bees, withBaseTreeModifiers(NoiseBasedCountPlacementModifierInfdev325.of(0, 0, 0)).add(INFDEV_325_TREE_PREDICATES).build());
        PlacementUtils.register(featureRegisterable, TREES_INFDEV_227_BEES, treesInfdev227Bees, withCountExtraAndTreeModifier(0, 0.1f, 1));
             
        PlacementUtils.register(featureRegisterable, TREES_BETA_FOREST, treesBetaForest, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_BETA_FOREST, NoiseBasedCountPlacementModifierBeta.of(5, 0.1f, 1)));
        PlacementUtils.register(featureRegisterable, TREES_BETA_RAINFOREST, treesBetaRainforest, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_BETA_RAINFOREST, NoiseBasedCountPlacementModifierBeta.of(5, 0.1f, 1)));
        PlacementUtils.register(featureRegisterable, TREES_BETA_SEASONAL_FOREST, treesBetaSeasonalForest, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_BETA_SEASONAL_FOREST, NoiseBasedCountPlacementModifierBeta.of(2, 0.1f, 1)));
        PlacementUtils.register(featureRegisterable, TREES_BETA_SPARSE, treesBetaSparse, withCountExtraAndTreeModifier(0, 0.1f, 1));
        PlacementUtils.register(featureRegisterable, TREES_BETA_TAIGA, treesBetaTaiga, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_BETA_TAIGA, NoiseBasedCountPlacementModifierBeta.of(5, 0.1f, 1)));
        PlacementUtils.register(featureRegisterable, TREES_BETA_OAK_FOREST, treesBetaOakForest, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_BETA_OAK_FOREST, NoiseBasedCountPlacementModifierBeta.of(5, 0.1f, 1)));

        PlacementUtils.register(featureRegisterable, TREES_BETA_FOREST_BEES, treesBetaForestBees, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_BETA_FOREST_BEES, NoiseBasedCountPlacementModifierBeta.of(5, 0.1f, 1)));
        PlacementUtils.register(featureRegisterable, TREES_BETA_RAINFOREST_BEES, treesBetaRainforestBees, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_BETA_RAINFOREST_BEES, NoiseBasedCountPlacementModifierBeta.of(5, 0.1f, 1)));
        PlacementUtils.register(featureRegisterable, TREES_BETA_SEASONAL_FOREST_BEES, treesBetaSeasonalForestBees, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_BETA_SEASONAL_FOREST_BEES, NoiseBasedCountPlacementModifierBeta.of(2, 0.1f, 1)));
        PlacementUtils.register(featureRegisterable, TREES_BETA_SPARSE_BEES, treesBetaSparseBees, withCountExtraAndTreeModifier(0, 0.1f, 1));
        PlacementUtils.register(featureRegisterable, TREES_BETA_OAK_FOREST_BEES, treesBetaOakForestBees, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_BETA_OAK_FOREST_BEES, NoiseBasedCountPlacementModifierBeta.of(5, 0.1f, 1)));

        PlacementUtils.register(featureRegisterable, TREES_PE_FOREST, treesPEForest, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_PE_FOREST_BEES, NoiseBasedCountPlacementModifierBeta.of(2, 0.1f, 1)));
        PlacementUtils.register(featureRegisterable, TREES_PE_RAINFOREST, treesPERainforest, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_PE_RAINFOREST_BEES, NoiseBasedCountPlacementModifierBeta.of(2, 0.1f, 1)));
        PlacementUtils.register(featureRegisterable, TREES_PE_SEASONAL_FOREST, treesPESeasonalForest, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_PE_SEASONAL_FOREST_BEES, NoiseBasedCountPlacementModifierBeta.of(1, 0.1f, 1)));
        PlacementUtils.register(featureRegisterable, TREES_PE_SPARSE, treesPESparse, withCountExtraAndTreeModifier(0, 0.1f, 1));
        PlacementUtils.register(featureRegisterable, TREES_PE_TAIGA, treesPETaiga, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_PE_TAIGA, NoiseBasedCountPlacementModifierBeta.of(1, 0.1f, 1)));

        PlacementUtils.register(featureRegisterable, TREES_PE_FOREST_BEES, treesPEForestBees, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_PE_FOREST_BEES, NoiseBasedCountPlacementModifierBeta.of(2, 0.1f, 1)));
        PlacementUtils.register(featureRegisterable, TREES_PE_RAINFOREST_BEES, treesPERainforestBees, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_PE_RAINFOREST_BEES, NoiseBasedCountPlacementModifierBeta.of(2, 0.1f, 1)));
        PlacementUtils.register(featureRegisterable, TREES_PE_SEASONAL_FOREST_BEES, treesPESeasonalForestBees, withNoiseBasedCountModifier(ModernBetaFeatureTags.TREES_PE_SEASONAL_FOREST_BEES, NoiseBasedCountPlacementModifierBeta.of(1, 0.1f, 1)));
        PlacementUtils.register(featureRegisterable, TREES_PE_SPARSE_BEES, treesPESparseBees, withCountExtraAndTreeModifier(0, 0.1f, 1));
        
        PlacementUtils.register(featureRegisterable, TREES_INDEV, treesIndev, RarityFilter.onAverageOnceEvery(3), withCountExtraModifier(5, 0.1f, 1), InSquarePlacement.spread(), SURFACE_WATER_DEPTH, PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome());
        PlacementUtils.register(featureRegisterable, TREES_INDEV_WOODS, treesIndevWoods, withCountExtraModifier(30, 0.1f, 1), InSquarePlacement.spread(), SURFACE_WATER_DEPTH, PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome());
        PlacementUtils.register(featureRegisterable, TREES_CLASSIC_14A_08, treesClassic14a08, RarityFilter.onAverageOnceEvery(5), withCountExtraModifier(20, 0.1f, 1), InSquarePlacement.spread(), SURFACE_WATER_DEPTH, PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome());

        PlacementUtils.register(featureRegisterable, TREES_INDEV_BEES, treesIndevBees, RarityFilter.onAverageOnceEvery(3), withCountExtraModifier(5, 0.1f, 1), InSquarePlacement.spread(), SURFACE_WATER_DEPTH, PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome());
        PlacementUtils.register(featureRegisterable, TREES_INDEV_WOODS_BEES, treesIndevWoodsBees, withCountExtraModifier(30, 0.1f, 1), InSquarePlacement.spread(), SURFACE_WATER_DEPTH, PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome());
        PlacementUtils.register(featureRegisterable, TREES_CLASSIC_14A_08_BEES, treesClassic14a08Bees, RarityFilter.onAverageOnceEvery(5), withCountExtraModifier(20, 0.1f, 1), InSquarePlacement.spread(), SURFACE_WATER_DEPTH, PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome());
    }
}
