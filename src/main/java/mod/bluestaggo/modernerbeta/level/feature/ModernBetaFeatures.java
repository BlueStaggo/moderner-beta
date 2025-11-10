package mod.bluestaggo.modernerbeta.level.feature;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

public class ModernBetaFeatures {
    private static IRegistryHandler<Feature<?>> registryHandler;
    public static BetaSnowAndFreezeFeature SNOW_AND_FREEZE;
    public static BetaOreClayFeature ORE_CLAY;
    public static CaveInfdev325Feature CAVE_INFDEV_325;

    private static <F extends Feature<?>> F register(String id, F feature) {
        return registryHandler.register(ModernerBeta.createId(id), feature);
    }
    
    @SuppressWarnings("unchecked")
    public static void register(IRegistryHandler<?> handler) {
        registryHandler = (IRegistryHandler<Feature<?>>) handler;
        SNOW_AND_FREEZE = register(
                ModernBetaFeatureTags.SNOW_AND_FREEZE, new BetaSnowAndFreezeFeature(NoneFeatureConfiguration.CODEC)
        );

        ORE_CLAY = register(
                ModernBetaFeatureTags.ORE_CLAY, new BetaOreClayFeature(OreConfiguration.CODEC)
        );

        CAVE_INFDEV_325 = register(
                ModernBetaFeatureTags.CAVE_INFDEV_325, new CaveInfdev325Feature(OreConfiguration.CODEC)
        );
    }
}