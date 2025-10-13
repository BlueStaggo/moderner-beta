package mod.bluestaggo.modernerbeta.world.feature.configured;

import mod.bluestaggo.modernerbeta.util.BlockStates;
import mod.bluestaggo.modernerbeta.world.feature.ModernBetaFeatureTags;
import mod.bluestaggo.modernerbeta.world.feature.ModernBetaFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModernBetaOreConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_CLAY = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.ORE_CLAY);
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_EMERALD_Y95 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.ORE_EMERALD_Y95);
    public static final ResourceKey<ConfiguredFeature<?, ?>> CAVE_INFDEV_325 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.CAVE_INFDEV_325);

    @SuppressWarnings("unchecked")
    public static void bootstrap(BootstrapContext<?> registerable) {
        BootstrapContext<ConfiguredFeature<?, ?>> featureRegisterable = (BootstrapContext<ConfiguredFeature<?, ?>>)registerable;
        
        TagMatchTest ruleStone = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        TagMatchTest ruleDeepslate = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> emeraldTargets = List.of(
            OreConfiguration.target(ruleStone, Blocks.EMERALD_ORE.defaultBlockState()),
            OreConfiguration.target(ruleDeepslate, Blocks.DEEPSLATE_EMERALD_ORE.defaultBlockState())
        );
        List<OreConfiguration.TargetBlockState> caveTargets = List.of(
            OreConfiguration.target(AlwaysTrueTest.INSTANCE, Blocks.AIR.defaultBlockState()),
            OreConfiguration.target(new BlockMatchTest(Blocks.WATER), BlockStates.WATER),
            OreConfiguration.target(new BlockMatchTest(Blocks.LAVA), BlockStates.LAVA)
        );
        
        FeatureUtils.register(featureRegisterable, ORE_CLAY, ModernBetaFeatures.ORE_CLAY, new OreConfiguration(new BlockMatchTest(Blocks.SAND), Blocks.CLAY.defaultBlockState(), 33));
        FeatureUtils.register(featureRegisterable, ORE_EMERALD_Y95, Feature.ORE, new OreConfiguration(emeraldTargets, 8, 0.9f));
        FeatureUtils.register(featureRegisterable, CAVE_INFDEV_325, ModernBetaFeatures.CAVE_INFDEV_325, new OreConfiguration(caveTargets, 16, 0.0f));
    }
}
