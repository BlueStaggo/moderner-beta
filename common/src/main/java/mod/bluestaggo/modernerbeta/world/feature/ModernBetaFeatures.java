package mod.bluestaggo.modernerbeta.world.feature;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class ModernBetaFeatures {
    public static final BetaFreezeTopLayerFeature FREEZE_TOP_LAYER = register(
        ModernBetaFeatureTags.FREEZE_TOP_LAYER, new BetaFreezeTopLayerFeature(DefaultFeatureConfig.CODEC)
    );

    public static final BetaFancyOakFeature OLD_FANCY_OAK = register(
        ModernBetaFeatureTags.FANCY_OAK, new BetaFancyOakFeature(DefaultFeatureConfig.CODEC)
    );

    public static final BetaOreClayFeature ORE_CLAY = register(
        ModernBetaFeatureTags.ORE_CLAY, new BetaOreClayFeature(OreFeatureConfig.CODEC)
    );

    public static final CaveInfdev325Feature CAVE_INFDEV_325 = register(
        ModernBetaFeatureTags.CAVE_INFDEV_325, new CaveInfdev325Feature(OreFeatureConfig.CODEC)
    );
    
    private static <F extends Feature<?>> F register(String id, F feature) {
        return Registry.register(Registries.FEATURE, ModernerBeta.createId(id), feature);
    }
    
    public static void register() {}
}