package mod.bluestaggo.modernerbeta.world.feature.configured;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ModernBetaConfiguredFeatures {
    public static void bootstrap(BootstrapContext<?> context) {
        ModernBetaMiscConfiguredFeatures.bootstrap(context);
        ModernBetaOreConfiguredFeatures.bootstrap(context);
        ModernBetaTreeConfiguredFeatures.bootstrap(context);
        ModernBetaVegetationConfiguredFeatures.bootstrap(context);
    }
    
    public static ResourceKey<ConfiguredFeature<?, ?>> of(String id) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ModernerBeta.createId(id));
    }
}
