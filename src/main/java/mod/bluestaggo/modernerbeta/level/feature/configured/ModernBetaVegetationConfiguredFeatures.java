package mod.bluestaggo.modernerbeta.level.feature.configured;

import mod.bluestaggo.modernerbeta.util.BlockStates;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.feature.ModernBetaFeatureTags;
import mod.bluestaggo.modernerbeta.level.feature.placed.ModernBetaTreePlacedFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
//? <26.1
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public class ModernBetaVegetationConfiguredFeatures {
    private static final ResourceKey<PlacedFeature> OAK_BEES_0002 =
            //? if >=1.21.5 {
            TreePlacements.OAK_BEES_0002_LEAF_LITTER;
            //? } else {
            /*TreePlacements.OAK_BEES_0002;
            *///? }

    public static final ResourceKey<ConfiguredFeature<?, ?>> MUSHROOM_HELL = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.MUSHROOM_HELL);
    //? if >=26.1 {
    /*public static final ResourceKey<ConfiguredFeature<?, ?>> DANDELION = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.DANDELION);
    public static final ResourceKey<ConfiguredFeature<?, ?>> POPPY = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.POPPY);
    public static final ResourceKey<ConfiguredFeature<?, ?>> GRASS = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.GRASS);
    public static final ResourceKey<ConfiguredFeature<?, ?>> GRASS_LUSH = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.GRASS_LUSH);
    *///? } else {
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_BROWN_MUSHROOM = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.PATCH_BROWN_MUSHROOM);
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_RED_MUSHROOM = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.PATCH_RED_MUSHROOM);
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_DANDELION = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.PATCH_DANDELION);
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_POPPY = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.PATCH_POPPY);
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_DANDELION_INFDEV_227 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.PATCH_DANDELION_INFDEV_227);
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_GRASS = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.PATCH_GRASS);
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_GRASS_LUSH = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.PATCH_GRASS_LUSH);
    //? }

    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_ALPHA = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_ALPHA);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INFDEV_611 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_611);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INFDEV_420 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_420);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INFDEV_415 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_415);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INFDEV_325 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_325);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INFDEV_227 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_227);

    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_ALPHA_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_ALPHA_BEES);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INFDEV_611_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_611_BEES);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INFDEV_420_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_420_BEES);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INFDEV_415_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_415_BEES);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INFDEV_325_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_325_BEES);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INFDEV_227_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_227_BEES);

    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BETA_FOREST = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_BETA_FOREST);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BETA_RAINFOREST = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_BETA_RAINFOREST);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BETA_SEASONAL_FOREST = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_BETA_SEASONAL_FOREST);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BETA_SPARSE = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_BETA_SPARSE);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BETA_TAIGA = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_BETA_TAIGA);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BETA_OAK_FOREST = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_BETA_OAK_FOREST);

    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BETA_FOREST_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_BETA_FOREST_BEES);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BETA_RAINFOREST_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_BETA_RAINFOREST_BEES);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BETA_SEASONAL_FOREST_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_BETA_SEASONAL_FOREST_BEES);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BETA_SPARSE_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_BETA_SPARSE_BEES);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BETA_OAK_FOREST_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_BETA_OAK_FOREST_BEES);

    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PE_FOREST = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_PE_FOREST);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PE_RAINFOREST = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_PE_RAINFOREST);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PE_SEASONAL_FOREST = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_PE_SEASONAL_FOREST);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PE_SPARSE = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_PE_SPARSE);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PE_TAIGA = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_PE_TAIGA);
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PE_FOREST_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_PE_FOREST_BEES);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PE_RAINFOREST_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_PE_RAINFOREST_BEES);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PE_SEASONAL_FOREST_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_PE_SEASONAL_FOREST_BEES);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PE_SPARSE_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_PE_SPARSE_BEES);
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INDEV = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INDEV);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INDEV_WOODS = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INDEV_WOODS);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_CLASSIC_14A_08 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_CLASSIC_14A_08);

    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INDEV_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INDEV_BEES);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INDEV_WOODS_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INDEV_WOODS_BEES);
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_CLASSIC_14A_08_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_CLASSIC_14A_08_BEES);

    @SuppressWarnings("unchecked")
    public static void bootstrap(BootstrapContext<?> context) {
        BootstrapContext<ConfiguredFeature<?, ?>> featureContext = (BootstrapContext<ConfiguredFeature<?, ?>>)context;
        HolderGetter<PlacedFeature> registryPlaced = featureContext.lookup(Registries.PLACED_FEATURE);

        //? if >=26.1 {
        /*FeatureUtils.register(featureContext, MUSHROOM_HELL, Feature.SIMPLE_BLOCK, ModernBetaRandomPatchConfigs.MUSHROOM_HELL_FEATURE_CONFIG);
        FeatureUtils.register(featureContext, DANDELION, Feature.SIMPLE_BLOCK, ModernBetaRandomPatchConfigs.DANDELION_FEATURE_CONFIG);
        FeatureUtils.register(featureContext, POPPY, Feature.SIMPLE_BLOCK, ModernBetaRandomPatchConfigs.POPPY_FEATURE_CONFIG);
        FeatureUtils.register(featureContext, GRASS, Feature.SIMPLE_BLOCK, ModernBetaRandomPatchConfigs.GRASS_FEATURE_CONFIG);
        FeatureUtils.register(featureContext, GRASS_LUSH, Feature.SIMPLE_BLOCK, ModernBetaRandomPatchConfigs.LUSH_GRASS_FEATURE_CONFIG);
        *///? } else {
        FeatureUtils.register(featureContext, MUSHROOM_HELL, Feature.FLOWER, ModernBetaRandomPatchConfigs.MUSHROOM_HELL);
        FeatureUtils.register(featureContext, PATCH_BROWN_MUSHROOM, Feature.RANDOM_PATCH, ModernBetaRandomPatchConfigs.BROWN_MUSHROOM);
        FeatureUtils.register(featureContext, PATCH_RED_MUSHROOM, Feature.RANDOM_PATCH, ModernBetaRandomPatchConfigs.RED_MUSHROOM);
        FeatureUtils.register(featureContext, PATCH_DANDELION, Feature.FLOWER, ModernBetaRandomPatchConfigs.DANDELION_CONFIG);
        FeatureUtils.register(featureContext, PATCH_POPPY, Feature.FLOWER, ModernBetaRandomPatchConfigs.POPPY_CONFIG);
        FeatureUtils.register(featureContext, PATCH_DANDELION_INFDEV_227, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.DANDELION)));
        FeatureUtils.register(featureContext, PATCH_GRASS, Feature.RANDOM_PATCH, ModernBetaRandomPatchConfigs.GRASS_CONFIG);
        FeatureUtils.register(featureContext, PATCH_GRASS_LUSH, Feature.RANDOM_PATCH, ModernBetaRandomPatchConfigs.LUSH_GRASS_CONFIG);
        //? }

        FeatureUtils.register(featureContext, TREES_ALPHA, Feature.RANDOM_SELECTOR, createDefaultRandomTreeConfig(registryPlaced, false));
        FeatureUtils.register(featureContext, TREES_INFDEV_611, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, false));
        FeatureUtils.register(featureContext, TREES_INFDEV_420, Feature.RANDOM_SELECTOR, createInfdevRandomTreeConfig(registryPlaced, false));
        FeatureUtils.register(featureContext, TREES_INFDEV_415, Feature.RANDOM_SELECTOR, createInfdevRandomTreeConfig(registryPlaced, false));
        FeatureUtils.register(featureContext, TREES_INFDEV_325, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, false));
        FeatureUtils.register(featureContext, TREES_INFDEV_227, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, false));
        
        FeatureUtils.register(featureContext, TREES_ALPHA_BEES, Feature.RANDOM_SELECTOR, createDefaultRandomTreeConfig(registryPlaced, true));
        FeatureUtils.register(featureContext, TREES_INFDEV_611_BEES, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, true));
        FeatureUtils.register(featureContext, TREES_INFDEV_420_BEES, Feature.RANDOM_SELECTOR, createInfdevRandomTreeConfig(registryPlaced, true));
        FeatureUtils.register(featureContext, TREES_INFDEV_415_BEES, Feature.RANDOM_SELECTOR, createInfdevRandomTreeConfig(registryPlaced, true));
        FeatureUtils.register(featureContext, TREES_INFDEV_325_BEES, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, true));
        FeatureUtils.register(featureContext, TREES_INFDEV_227_BEES, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, true));

        FeatureUtils.register(featureContext, TREES_BETA_FOREST, Feature.RANDOM_SELECTOR, createForestRandomTreeConfig(registryPlaced, false));
        FeatureUtils.register(featureContext, TREES_BETA_RAINFOREST, Feature.RANDOM_SELECTOR, createRainforestRandomTreeConfig(registryPlaced, false));
        FeatureUtils.register(featureContext, TREES_BETA_SEASONAL_FOREST, Feature.RANDOM_SELECTOR, createSeasonalForestRandomTreeConfig(registryPlaced, false));
        FeatureUtils.register(featureContext, TREES_BETA_SPARSE, Feature.RANDOM_SELECTOR, createDefaultRandomTreeConfig(registryPlaced, false));
        FeatureUtils.register(featureContext, TREES_BETA_TAIGA, Feature.RANDOM_SELECTOR, createTaigaRandomTreeConfig(registryPlaced));
        FeatureUtils.register(featureContext, TREES_BETA_OAK_FOREST, Feature.RANDOM_SELECTOR, createOakForestRandomTreeConfig(registryPlaced, false));

        FeatureUtils.register(featureContext, TREES_BETA_FOREST_BEES, Feature.RANDOM_SELECTOR, createForestRandomTreeConfig(registryPlaced, true));
        FeatureUtils.register(featureContext, TREES_BETA_RAINFOREST_BEES, Feature.RANDOM_SELECTOR, createRainforestRandomTreeConfig(registryPlaced, true));
        FeatureUtils.register(featureContext, TREES_BETA_SEASONAL_FOREST_BEES, Feature.RANDOM_SELECTOR, createSeasonalForestRandomTreeConfig(registryPlaced, true));
        FeatureUtils.register(featureContext, TREES_BETA_SPARSE_BEES, Feature.RANDOM_SELECTOR, createDefaultRandomTreeConfig(registryPlaced, true));
        FeatureUtils.register(featureContext, TREES_BETA_OAK_FOREST_BEES, Feature.RANDOM_SELECTOR, createOakForestRandomTreeConfig(registryPlaced, true));

        FeatureUtils.register(featureContext, TREES_PE_FOREST, Feature.RANDOM_SELECTOR, createPEForestRandomTreeConfig(registryPlaced, false));
        FeatureUtils.register(featureContext, TREES_PE_RAINFOREST, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, false));
        FeatureUtils.register(featureContext, TREES_PE_SEASONAL_FOREST, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, false));
        FeatureUtils.register(featureContext, TREES_PE_SPARSE, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, false));
        FeatureUtils.register(featureContext, TREES_PE_TAIGA, Feature.RANDOM_SELECTOR, createTaigaRandomTreeConfig(registryPlaced));
        
        FeatureUtils.register(featureContext, TREES_PE_FOREST_BEES, Feature.RANDOM_SELECTOR, createPEForestRandomTreeConfig(registryPlaced, true));
        FeatureUtils.register(featureContext, TREES_PE_RAINFOREST_BEES, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, true));
        FeatureUtils.register(featureContext, TREES_PE_SEASONAL_FOREST_BEES, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, true));
        FeatureUtils.register(featureContext, TREES_PE_SPARSE_BEES, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, true));
        
        FeatureUtils.register(featureContext, TREES_INDEV, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, false));
        FeatureUtils.register(featureContext, TREES_INDEV_WOODS, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, false));
        FeatureUtils.register(featureContext, TREES_CLASSIC_14A_08, Feature.RANDOM_SELECTOR, createOak14a08TreeConfig(registryPlaced, false));

        FeatureUtils.register(featureContext, TREES_INDEV_BEES, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, true));
        FeatureUtils.register(featureContext, TREES_INDEV_WOODS_BEES, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, true));
        FeatureUtils.register(featureContext, TREES_CLASSIC_14A_08_BEES, Feature.RANDOM_SELECTOR, createOak14a08TreeConfig(registryPlaced, false));
    }

    private static RandomFeatureConfiguration createOakTreeConfig(HolderGetter<PlacedFeature> registryPlaced, boolean bees) {
        Holder.Reference<PlacedFeature> oak = registryPlaced.getOrThrow(TreePlacements.OAK_CHECKED);
        Holder.Reference<PlacedFeature> oakBees = registryPlaced.getOrThrow(OAK_BEES_0002);
        
        return new RandomFeatureConfiguration(
            List.of(
                //? if >=1.21.5
                withChance(registryPlaced.getOrThrow(TreePlacements.FALLEN_OAK_TREE), 1.0f / 80.0f)
            ), bees ? oakBees : oak
        );
    }
    
    private static RandomFeatureConfiguration createDefaultRandomTreeConfig(HolderGetter<PlacedFeature> registryPlaced, boolean bees) {
        Holder.Reference<PlacedFeature> fancyOak = registryPlaced.getOrThrow(ModernBetaTreePlacedFeatures.FANCY_OAK);
        Holder.Reference<PlacedFeature> oak = registryPlaced.getOrThrow(TreePlacements.OAK_CHECKED);
        Holder.Reference<PlacedFeature> oakBees = registryPlaced.getOrThrow(OAK_BEES_0002);
        
        return new RandomFeatureConfiguration(
            List.of(
                withChance(fancyOak, 0.1f)
                //? if >=1.21.5
                , withChance(registryPlaced.getOrThrow(TreePlacements.FALLEN_OAK_TREE), 1.0f / 80.0f)
            ), bees ? oakBees : oak
        );
    }
    
    private static RandomFeatureConfiguration createInfdevRandomTreeConfig(HolderGetter<PlacedFeature> registryPlaced, boolean bees) {
        Holder.Reference<PlacedFeature> fancyOak = registryPlaced.getOrThrow(ModernBetaTreePlacedFeatures.FANCY_OAK);
        Holder.Reference<PlacedFeature> oak = registryPlaced.getOrThrow(TreePlacements.OAK_CHECKED);
        Holder.Reference<PlacedFeature> oakBees = registryPlaced.getOrThrow(OAK_BEES_0002);
        
        return new RandomFeatureConfiguration(
            List.of(
                withChance(bees ? oakBees : oak, 0.1f)
                //? if >=1.21.5
                , withChance(registryPlaced.getOrThrow(TreePlacements.FALLEN_OAK_TREE), 1.0f / 80.0f)
            ), fancyOak
        );
    }

    private static RandomFeatureConfiguration createOakForestRandomTreeConfig(HolderGetter<PlacedFeature> registryPlaced, boolean bees) {
        Holder.Reference<PlacedFeature> fancyOak = registryPlaced.getOrThrow(ModernBetaTreePlacedFeatures.FANCY_OAK);
        Holder.Reference<PlacedFeature> oak = registryPlaced.getOrThrow(TreePlacements.OAK_CHECKED);
        Holder.Reference<PlacedFeature> oakBees = registryPlaced.getOrThrow(OAK_BEES_0002);

        return new RandomFeatureConfiguration(
            List.of(
                withChance(fancyOak, 1.0f / 3.0f)
                //? if >=1.21.5
                , withChance(registryPlaced.getOrThrow(TreePlacements.FALLEN_OAK_TREE), 1.0f / 80.0f)
            ), bees ? oakBees : oak
        );
    }
    
    private static RandomFeatureConfiguration createForestRandomTreeConfig(HolderGetter<PlacedFeature> registryPlaced, boolean bees) {
        Holder.Reference<PlacedFeature> fancyOak = registryPlaced.getOrThrow(ModernBetaTreePlacedFeatures.FANCY_OAK);
        Holder.Reference<PlacedFeature> oak = registryPlaced.getOrThrow(TreePlacements.OAK_CHECKED);
        Holder.Reference<PlacedFeature> oakBees = registryPlaced.getOrThrow(OAK_BEES_0002);

        Holder.Reference<PlacedFeature> birch = registryPlaced.getOrThrow(TreePlacements.BIRCH_CHECKED);
        Holder.Reference<PlacedFeature> birchBees = registryPlaced.getOrThrow(TreePlacements.BIRCH_BEES_0002_PLACED);
        
        return new RandomFeatureConfiguration(
            List.of(
                //? if >=1.21.5
                withChance(registryPlaced.getOrThrow(TreePlacements.FALLEN_BIRCH_TREE), 1.0f / 400.0f),
                withChance(bees ? birchBees : birch, 0.2f),
                withChance(fancyOak, 1.0f / 3.0f)
                //? if >=1.21.5
                , withChance(registryPlaced.getOrThrow(TreePlacements.FALLEN_OAK_TREE), 1.0f / 80.0f)
            ), bees ? oakBees : oak
        );
    }
    
    private static RandomFeatureConfiguration createRainforestRandomTreeConfig(HolderGetter<PlacedFeature> registryPlaced, boolean bees) {
        Holder.Reference<PlacedFeature> fancyOak = registryPlaced.getOrThrow(ModernBetaTreePlacedFeatures.FANCY_OAK);
        Holder.Reference<PlacedFeature> oak = registryPlaced.getOrThrow(TreePlacements.OAK_CHECKED);
        Holder.Reference<PlacedFeature> oakBees = registryPlaced.getOrThrow(OAK_BEES_0002);
        
        return new RandomFeatureConfiguration(
            List.of(
                withChance(fancyOak, 1.0f / 3.0f)
                //? if >=1.21.5
                , withChance(registryPlaced.getOrThrow(TreePlacements.FALLEN_OAK_TREE), 1.0f / 80.0f)
            ), bees ? oakBees : oak
        );
    }
    
    private static RandomFeatureConfiguration createSeasonalForestRandomTreeConfig(HolderGetter<PlacedFeature> registryPlaced, boolean bees) {
        Holder.Reference<PlacedFeature> fancyOak = registryPlaced.getOrThrow(ModernBetaTreePlacedFeatures.FANCY_OAK);
        Holder.Reference<PlacedFeature> oak = registryPlaced.getOrThrow(TreePlacements.OAK_CHECKED);
        Holder.Reference<PlacedFeature> oakBees = registryPlaced.getOrThrow(OAK_BEES_0002);
        
        return new RandomFeatureConfiguration(
            List.of(
                withChance(fancyOak, 0.1f)
                //? if >=1.21.5
                , withChance(registryPlaced.getOrThrow(TreePlacements.FALLEN_OAK_TREE), 1.0f / 80.0f)
            ), bees ? oakBees : oak
        );
    }

    private static RandomFeatureConfiguration createTaigaRandomTreeConfig(HolderGetter<PlacedFeature> registryPlaced) {
        Holder.Reference<PlacedFeature> pine = registryPlaced.getOrThrow(TreePlacements.PINE_CHECKED);
        Holder.Reference<PlacedFeature> spruce = registryPlaced.getOrThrow(TreePlacements.SPRUCE_CHECKED);
        
        return new RandomFeatureConfiguration(
            List.of(
                withChance(pine, 1.0f / 3.0f)
                //? if >=1.21.5
                , withChance(registryPlaced.getOrThrow(TreePlacements.FALLEN_SPRUCE_TREE), 1.0f / 80.0f)
            ), spruce
        );
    }
    
    private static RandomFeatureConfiguration createPEForestRandomTreeConfig(HolderGetter<PlacedFeature> registryPlaced, boolean bees) {
        Holder.Reference<PlacedFeature> oak = registryPlaced.getOrThrow(TreePlacements.OAK_CHECKED);
        Holder.Reference<PlacedFeature> oakBees = registryPlaced.getOrThrow(OAK_BEES_0002);

        Holder.Reference<PlacedFeature> birch = registryPlaced.getOrThrow(TreePlacements.BIRCH_CHECKED);
        Holder.Reference<PlacedFeature> birchBees = registryPlaced.getOrThrow(TreePlacements.BIRCH_BEES_0002_PLACED);
        
        return new RandomFeatureConfiguration(
            List.of(
                //? if >=1.21.5
                withChance(registryPlaced.getOrThrow(TreePlacements.FALLEN_BIRCH_TREE), 1.0f / 400.0f),
                withChance(bees ? birchBees : birch, 0.2f)
                //? if >=1.21.5
                , withChance(registryPlaced.getOrThrow(TreePlacements.FALLEN_OAK_TREE), 1.0f / 80.0f)
            ), bees ? oak : oakBees
        );
    }

    private static RandomFeatureConfiguration createOak14a08TreeConfig(HolderGetter<PlacedFeature> registryPlaced, boolean bees) {
        Holder.Reference<PlacedFeature> oak = registryPlaced.getOrThrow(ModernBetaTreePlacedFeatures.OAK_14A_08);
        Holder.Reference<PlacedFeature> oakBees = registryPlaced.getOrThrow(ModernBetaTreePlacedFeatures.OAK_14A_08_BEES_0002);

        return new RandomFeatureConfiguration(List.of(), bees ? oakBees : oak);
    }
    
    private static WeightedPlacedFeature withChance(Holder<PlacedFeature> feature, float chance) {
        return new WeightedPlacedFeature(feature, chance);
    }
    
    private static final class ModernBetaRandomPatchConfigs {
        private static final int XZ_SPREAD = 7;
        private static final int Y_SPREAD = 3;
        private static final int TRIES = 64;
        private static final int GRASS_TRIES = 64;
        
        public static final SimpleBlockConfiguration DANDELION_FEATURE_CONFIG;
        public static final SimpleBlockConfiguration POPPY_FEATURE_CONFIG;
        
        public static final SimpleBlockConfiguration GRASS_FEATURE_CONFIG;
        public static final SimpleBlockConfiguration LUSH_GRASS_FEATURE_CONFIG;

        //? if <26.1 {
        public static final SimpleBlockConfiguration BROWN_MUSHROOM_FEATURE_CONFIG;
        public static final SimpleBlockConfiguration RED_MUSHROOM_FEATURE_CONFIG;
        //? }
        public static final SimpleBlockConfiguration MUSHROOM_HELL_FEATURE_CONFIG;

        //? if <26.1 {
        public static final RandomPatchConfiguration GRASS_CONFIG;
        public static final RandomPatchConfiguration LUSH_GRASS_CONFIG;
        
        public static final RandomPatchConfiguration DANDELION_CONFIG;
        public static final RandomPatchConfiguration POPPY_CONFIG;

        public static final RandomPatchConfiguration BROWN_MUSHROOM;
        public static final RandomPatchConfiguration RED_MUSHROOM;
        public static final RandomPatchConfiguration MUSHROOM_HELL;

        public static RandomPatchConfiguration createRandomPatchFeatureConfig(int tries, SimpleBlockConfiguration featureConfig) {
            return new RandomPatchConfiguration(tries, XZ_SPREAD, Y_SPREAD, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, featureConfig));
        }

        public static RandomPatchConfiguration createRandomPatchFeatureConfig(int tries, SimpleBlockConfiguration featureConfig, BlockPredicate filter) {
            return new RandomPatchConfiguration(tries, XZ_SPREAD, Y_SPREAD, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, featureConfig, filter));
        }
        //? }
        
        private static WeightedList.Builder<BlockState> pool() {
            return WeightedList.builder();
        }
        
        static {
            DANDELION_FEATURE_CONFIG = new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.DANDELION));
            POPPY_FEATURE_CONFIG = new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.POPPY));
            
            GRASS_FEATURE_CONFIG = new SimpleBlockConfiguration(BlockStateProvider.simple(VersionCompat.SHORT_GRASS));
            LUSH_GRASS_FEATURE_CONFIG = new SimpleBlockConfiguration(new WeightedStateProvider(pool().add(BlockStates.SHORT_GRASS, 1).add(BlockStates.FERN, 4)));

            //? if <26.1 {
            BROWN_MUSHROOM_FEATURE_CONFIG = new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.BROWN_MUSHROOM));
            RED_MUSHROOM_FEATURE_CONFIG = new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.RED_MUSHROOM));
            //? }
            MUSHROOM_HELL_FEATURE_CONFIG = new SimpleBlockConfiguration(new WeightedStateProvider(pool().add(Blocks.BROWN_MUSHROOM.defaultBlockState(), 2).add(Blocks.RED_MUSHROOM.defaultBlockState(), 1)));

            //? if <26.1 {
            // # of tries in Beta equivalent is 128, but here it seems to generate too much grass,
            // so keep # of tries at 64 for now.
            GRASS_CONFIG = createRandomPatchFeatureConfig(GRASS_TRIES, GRASS_FEATURE_CONFIG);
            LUSH_GRASS_CONFIG = createRandomPatchFeatureConfig(GRASS_TRIES, LUSH_GRASS_FEATURE_CONFIG);
        
            DANDELION_CONFIG = createRandomPatchFeatureConfig(TRIES, DANDELION_FEATURE_CONFIG);
            POPPY_CONFIG = createRandomPatchFeatureConfig(TRIES, POPPY_FEATURE_CONFIG);
            
            BROWN_MUSHROOM = createRandomPatchFeatureConfig(TRIES, BROWN_MUSHROOM_FEATURE_CONFIG, BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.CAVE_AIR));
            RED_MUSHROOM = createRandomPatchFeatureConfig(TRIES, RED_MUSHROOM_FEATURE_CONFIG, BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.CAVE_AIR));
            MUSHROOM_HELL = createRandomPatchFeatureConfig(TRIES, MUSHROOM_HELL_FEATURE_CONFIG);
            //? }
        }
    }
}
