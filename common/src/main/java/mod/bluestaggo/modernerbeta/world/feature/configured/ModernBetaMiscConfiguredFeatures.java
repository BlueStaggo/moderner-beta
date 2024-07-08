package mod.bluestaggo.modernerbeta.world.feature.configured;

import mod.bluestaggo.modernerbeta.world.feature.ModernBetaFeatureTags;
import mod.bluestaggo.modernerbeta.world.feature.ModernBetaFeatures;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;

public class ModernBetaMiscConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> FREEZE_TOP_LAYER = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.FREEZE_TOP_LAYER);
    
    @SuppressWarnings("unchecked")
    public static void bootstrap(Registerable<?> registerable) {
        Registerable<ConfiguredFeature<?, ?>> featureRegisterable = (Registerable<ConfiguredFeature<?, ?>>)registerable;
        
        ConfiguredFeatures.register(featureRegisterable, FREEZE_TOP_LAYER, ModernBetaFeatures.FREEZE_TOP_LAYER.get());
    }
}
