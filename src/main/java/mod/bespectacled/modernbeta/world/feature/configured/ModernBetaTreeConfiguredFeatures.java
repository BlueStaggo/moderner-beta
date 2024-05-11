package mod.bespectacled.modernbeta.world.feature.configured;

import mod.bespectacled.modernbeta.world.feature.ModernBetaFeatureTags;
import mod.bespectacled.modernbeta.world.feature.ModernBetaFeatures;
import mod.bespectacled.modernbeta.world.feature.foliage.Oak14a08FoliagePlacer;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.BeehiveTreeDecorator;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.List;

public class ModernBetaTreeConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> FANCY_OAK = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.FANCY_OAK);
    public static final RegistryKey<ConfiguredFeature<?, ?>> OAK_14A_08 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.OAK_14A_08);
    public static final RegistryKey<ConfiguredFeature<?, ?>> OAK_14A_08_BEES_0002 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.OAK_14A_08_BEES_0002);

    @SuppressWarnings("unchecked")
    public static void bootstrap(Registerable<?> registerable) {
        Registerable<ConfiguredFeature<?, ?>> featureRegisterable = (Registerable<ConfiguredFeature<?, ?>>)registerable;
        
        ConfiguredFeatures.register(featureRegisterable, FANCY_OAK, ModernBetaFeatures.OLD_FANCY_OAK, FeatureConfig.DEFAULT);
        ConfiguredFeatures.register(featureRegisterable, OAK_14A_08, Feature.TREE, getOak14a08Config(false));
        ConfiguredFeatures.register(featureRegisterable, OAK_14A_08_BEES_0002, Feature.TREE, getOak14a08Config(true));
    }

    private static TreeFeatureConfig getOak14a08Config(boolean bees) {
        TreeFeatureConfig.Builder builder = new TreeFeatureConfig.Builder(
                        BlockStateProvider.of(Blocks.OAK_LOG),
                        new StraightTrunkPlacer(4, 1, 0),
                        BlockStateProvider.of(Blocks.OAK_LEAVES),
                        new Oak14a08FoliagePlacer(ConstantIntProvider.create(1), ConstantIntProvider.create(0), 2),
                        new TwoLayersFeatureSize(1, 0, 1)
        );

        if (bees) {
            builder.decorators(List.of(new BeehiveTreeDecorator(0.002F)));
        }

        return builder.build();
    }
}   
