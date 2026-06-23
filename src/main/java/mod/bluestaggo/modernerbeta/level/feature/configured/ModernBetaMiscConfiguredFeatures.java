package mod.bluestaggo.modernerbeta.level.feature.configured;

import mod.bluestaggo.modernerbeta.level.feature.ModernBetaFeatureTags;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
//? if >=26.3 {
/*import net.minecraft.world.level.levelgen.feature.Feature;
*///? } else {
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
//? }

public class ModernBetaMiscConfiguredFeatures {
    //~ if >=26.3 'ConfiguredFeature<?, ?>>' -> 'Feature>'
    public static final ResourceKey<ConfiguredFeature<?, ?>> FREEZE_TOP_LAYER = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.SNOW_AND_FREEZE);
    
    @SuppressWarnings("unchecked")
    public static void bootstrap(BootstrapContext<?> context) {

        //? if >=26.3 {
        /*BootstrapContext<Feature> featureContext = (BootstrapContext<Feature>)context;

        featureContext.register(FREEZE_TOP_LAYER, mod.bluestaggo.modernerbeta.level.feature.BetaSnowAndFreezeFeature.INSTANCE);
        *///? } else {
        BootstrapContext<ConfiguredFeature<?, ?>> featureContext = (BootstrapContext<ConfiguredFeature<?, ?>>)context;

        net.minecraft.data.worldgen.features.FeatureUtils.register(featureContext, FREEZE_TOP_LAYER, mod.bluestaggo.modernerbeta.level.feature.ModernBetaFeatures.SNOW_AND_FREEZE);
        //? }
    }
}