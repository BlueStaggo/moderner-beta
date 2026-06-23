package mod.bluestaggo.modernerbeta.level.feature.configured;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
//? if >=26.3 {
/*import net.minecraft.world.level.levelgen.feature.Feature;
*///? } else {
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
//? }

public class ModernBetaConfiguredFeatures {
    public static void bootstrap(BootstrapContext<?> context) {
        ModernBetaMiscConfiguredFeatures.bootstrap(context);
        ModernBetaOreConfiguredFeatures.bootstrap(context);
        ModernBetaTreeConfiguredFeatures.bootstrap(context);
        ModernBetaVegetationConfiguredFeatures.bootstrap(context);
    }

    //~ if >=26.3 'ConfiguredFeature<?, ?>' -> 'Feature'
    public static ResourceKey<ConfiguredFeature<?, ?>> of(String id) {
        //~ if >=26.3 'CONFIGURED_FEATURE' -> 'FEATURE'
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ModernerBeta.createId(id));
    }
}