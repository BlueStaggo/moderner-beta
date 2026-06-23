package mod.bluestaggo.modernerbeta.level.feature;

//? if >=26.3
//import com.mojang.serialization.MapCodec;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import net.minecraft.world.level.levelgen.feature.Feature;
//? if <26.3 {
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
//? }

public class ModernBetaFeatures {
    //~ if >=26.3 'Feature<?>' -> 'MapCodec<? extends Feature>'
    private static IRegistryHandler<Feature<?>> registryHandler;
    //? if <26.3 {
    public static BetaSnowAndFreezeFeature SNOW_AND_FREEZE;
    public static BetaOreClayFeature ORE_CLAY;
    public static CaveInfdev325Feature CAVE_INFDEV_325;
    //? }

    //~ if >=26.3 '<F extends Feature<?>> F' -> '<F> MapCodec<? extends Feature>', 'F' -> 'MapCodec<? extends Feature>'
    private static <F extends Feature<?>> F register(String id, F feature) {
        return registryHandler.register(ModernerBeta.createId(id), feature);
    }
    
    @SuppressWarnings("unchecked")
    public static void register(IRegistryHandler<?> handler) {
        //? if >=26.3 {
        /*registryHandler = (IRegistryHandler<MapCodec<? extends Feature>>) handler ;

        register(ModernBetaFeatureTags.SNOW_AND_FREEZE, BetaSnowAndFreezeFeature.CODEC);
        register(ModernBetaFeatureTags.ORE_CLAY, BetaOreClayFeature.CODEC);
        register(ModernBetaFeatureTags.CAVE_INFDEV_325, CaveInfdev325Feature.CODEC);
        *///? } else {
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
        //? }
    }
}