package mod.bluestaggo.modernerbeta.level.feature.configured;

import mod.bluestaggo.modernerbeta.util.BlockStates;
import mod.bluestaggo.modernerbeta.level.feature.ModernBetaFeatureTags;
import net.minecraft.data.worldgen.BootstrapContext;
//? if <26.3
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.*;
//? if <26.3
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModernBetaOreConfiguredFeatures {
    //~ if >=26.3 'ConfiguredFeature<?, ?>>' -> 'Feature>' {
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_CLAY = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.ORE_CLAY);
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_EMERALD_Y95 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.ORE_EMERALD_Y95);
    public static final ResourceKey<ConfiguredFeature<?, ?>> CAVE_INFDEV_325 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.CAVE_INFDEV_325);
    //~ }

    @SuppressWarnings("unchecked")
    public static void bootstrap(BootstrapContext<?> context) {
        TagMatchTest ruleStone = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        TagMatchTest ruleDeepslate = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        //~ if >=26.3 'OreConfiguration.TargetBlockState' -> 'BlockReplacement', 'OreConfiguration.target' -> 'BlockReplacement.replace' {
        List<OreConfiguration.TargetBlockState> emeraldTargets = List.of(
            OreConfiguration.target(ruleStone, Blocks.EMERALD_ORE.defaultBlockState()),
            OreConfiguration.target(ruleDeepslate, Blocks.DEEPSLATE_EMERALD_ORE.defaultBlockState())
        );
        List<OreConfiguration.TargetBlockState> caveTargets = List.of(
            OreConfiguration.target(AlwaysTrueTest.INSTANCE, Blocks.AIR.defaultBlockState()),
            OreConfiguration.target(new BlockMatchTest(Blocks.WATER), BlockStates.WATER),
            OreConfiguration.target(new BlockMatchTest(Blocks.LAVA), BlockStates.LAVA)
        );
        //~ }

        //? if >=26.3 {
        /*BootstrapContext<Feature> featureContext = (BootstrapContext<Feature>)context;

        featureContext.register(ORE_CLAY, new mod.bluestaggo.modernerbeta.level.feature.BetaOreClayFeature(new BlockMatchTest(Blocks.SAND), Blocks.CLAY.defaultBlockState(), 33));
        featureContext.register(ORE_EMERALD_Y95, new OreFeature(emeraldTargets, 8, 0.9f));
        featureContext.register(CAVE_INFDEV_325, new mod.bluestaggo.modernerbeta.level.feature.CaveInfdev325Feature(caveTargets, 16, 0.0f));
        *///? } else {
        BootstrapContext<ConfiguredFeature<?, ?>> featureContext = (BootstrapContext<ConfiguredFeature<?, ?>>)context;

        FeatureUtils.register(featureContext, ORE_CLAY, mod.bluestaggo.modernerbeta.level.feature.ModernBetaFeatures.ORE_CLAY, new OreConfiguration(new BlockMatchTest(Blocks.SAND), Blocks.CLAY.defaultBlockState(), 33));
        FeatureUtils.register(featureContext, ORE_EMERALD_Y95, Feature.ORE, new OreConfiguration(emeraldTargets, 8, 0.9f));
        FeatureUtils.register(featureContext, CAVE_INFDEV_325, mod.bluestaggo.modernerbeta.level.feature.ModernBetaFeatures.CAVE_INFDEV_325, new OreConfiguration(caveTargets, 16, 0.0f));
        //? }
    }
}