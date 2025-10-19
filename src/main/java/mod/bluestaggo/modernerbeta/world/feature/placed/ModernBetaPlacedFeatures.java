package mod.bluestaggo.modernerbeta.world.feature.placed;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModernBetaPlacedFeatures {
    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        ModernBetaMiscPlacedFeatures.bootstrap(context);
        ModernBetaOrePlacedFeatures.bootstrap(context);
        ModernBetaTreePlacedFeatures.bootstrap(context);
        ModernBetaVegetationPlacedFeatures.bootstrap(context);
    }
    
    public static ResourceKey<PlacedFeature> of(String id) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ModernerBeta.createId(id));
    }
}
