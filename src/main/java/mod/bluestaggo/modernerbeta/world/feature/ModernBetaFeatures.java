package mod.bluestaggo.modernerbeta.world.feature;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class ModernBetaFeatures {
    private static IRegistryHandler<Feature<?>> registryHandler;
    public static BetaFreezeTopLayerFeature FREEZE_TOP_LAYER;
    public static BetaOreClayFeature ORE_CLAY;
    public static CaveInfdev325Feature CAVE_INFDEV_325;

    private static <F extends Feature<?>> F register(String id, F feature) {
        return registryHandler.register(ModernerBeta.createId(id), feature);
    }
    
    @SuppressWarnings("unchecked")
    public static void register(IRegistryHandler<?> handler) {
        registryHandler = (IRegistryHandler<Feature<?>>) handler;
        FREEZE_TOP_LAYER = register(
                ModernBetaFeatureTags.FREEZE_TOP_LAYER, new BetaFreezeTopLayerFeature(DefaultFeatureConfig.CODEC)
        );

        ORE_CLAY = register(
                ModernBetaFeatureTags.ORE_CLAY, new BetaOreClayFeature(OreFeatureConfig.CODEC)
        );

        CAVE_INFDEV_325 = register(
                ModernBetaFeatureTags.CAVE_INFDEV_325, new CaveInfdev325Feature(OreFeatureConfig.CODEC)
        );
    }
}