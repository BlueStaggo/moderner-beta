package mod.bluestaggo.modernerbeta.world.feature.configured;

import mod.bluestaggo.modernerbeta.world.feature.ModernBetaFeatureTags;
import mod.bluestaggo.modernerbeta.world.feature.ModernBetaFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ModernBetaMiscConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> FREEZE_TOP_LAYER = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.FREEZE_TOP_LAYER);
    
    @SuppressWarnings("unchecked")
    public static void bootstrap(BootstrapContext<?> registerable) {
        BootstrapContext<ConfiguredFeature<?, ?>> featureRegisterable = (BootstrapContext<ConfiguredFeature<?, ?>>)registerable;
        
        FeatureUtils.register(featureRegisterable, FREEZE_TOP_LAYER, ModernBetaFeatures.FREEZE_TOP_LAYER);
    }
}
