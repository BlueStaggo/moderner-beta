package mod.bluestaggo.modernerbeta.level.feature.configured;

import mod.bluestaggo.modernerbeta.level.feature.ModernBetaFeatureTags;
import mod.bluestaggo.modernerbeta.level.feature.ModernBetaFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ModernBetaMiscConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> FREEZE_TOP_LAYER = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.SNOW_AND_FREEZE);
    
    @SuppressWarnings("unchecked")
    public static void bootstrap(BootstrapContext<?> context) {
        BootstrapContext<ConfiguredFeature<?, ?>> featureContext = (BootstrapContext<ConfiguredFeature<?, ?>>)context;
        
        FeatureUtils.register(featureContext, FREEZE_TOP_LAYER, ModernBetaFeatures.SNOW_AND_FREEZE);
    }
}
