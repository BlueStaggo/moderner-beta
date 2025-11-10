package mod.bluestaggo.modernerbeta.level.feature.configured;

import mod.bluestaggo.modernerbeta.level.feature.ModernBetaFeatureTags;
import mod.bluestaggo.modernerbeta.level.feature.foliage.BetaLargeOakFoliagePlacer;
import mod.bluestaggo.modernerbeta.level.feature.foliage.Oak14a08FoliagePlacer;
import mod.bluestaggo.modernerbeta.level.feature.trunk.BetaLargeOakTrunkPlacer;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

import java.util.List;
import java.util.OptionalInt;

public class ModernBetaTreeConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> FANCY_OAK = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.FANCY_OAK);
    public static final ResourceKey<ConfiguredFeature<?, ?>> OAK_14A_08 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.OAK_14A_08);
    public static final ResourceKey<ConfiguredFeature<?, ?>> OAK_14A_08_BEES_0002 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.OAK_14A_08_BEES_0002);

    @SuppressWarnings("unchecked")
    public static void bootstrap(BootstrapContext<?> context) {
        BootstrapContext<ConfiguredFeature<?, ?>> featureContext = (BootstrapContext<ConfiguredFeature<?, ?>>)context;

        FeatureUtils.register(featureContext, FANCY_OAK, Feature.TREE, getOldFancyTreeConfig());
        FeatureUtils.register(featureContext, OAK_14A_08, Feature.TREE, getOak14a08Config(false));
        FeatureUtils.register(featureContext, OAK_14A_08_BEES_0002, Feature.TREE, getOak14a08Config(true));
    }

    private static TreeConfiguration getOak14a08Config(boolean bees) {
        TreeConfiguration.TreeConfigurationBuilder builder = new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(Blocks.OAK_LOG),
                new StraightTrunkPlacer(4, 1, 0),
                BlockStateProvider.simple(Blocks.OAK_LEAVES),
                new Oak14a08FoliagePlacer(ConstantInt.of(1), ConstantInt.of(0), 2),
                new TwoLayersFeatureSize(1, 0, 1)
        );

        if (bees) {
            builder.decorators(List.of(new BeehiveDecorator(0.002F)));
        }

        return builder.build();
    }

    private static TreeConfiguration getOldFancyTreeConfig() {
        TreeConfiguration.TreeConfigurationBuilder builder = new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(Blocks.OAK_LOG),
                new BetaLargeOakTrunkPlacer(5, 11, 0, false),
                BlockStateProvider.simple(Blocks.OAK_LEAVES),
                new BetaLargeOakFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4),
                new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(8))
        );

        return builder.build();
    }
}   
