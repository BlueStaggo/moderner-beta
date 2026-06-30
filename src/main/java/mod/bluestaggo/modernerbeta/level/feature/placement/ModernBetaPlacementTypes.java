package mod.bluestaggo.modernerbeta.level.feature.placement;

//? if >=26.3
//import com.mojang.serialization.MapCodec;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
//? if <26.3
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class ModernBetaPlacementTypes {
    //~ if >=26.3 'PlacementModifierType<?>>' -> 'MapCodec<? extends PlacementModifier>>' {
    private static IRegistryHandler<PlacementModifierType<?>> registryHandler;
    //? if <26.3 {
    public static PlacementModifierType<NoiseBasedCountPlacementModifierBeta> BETA_NOISE_BASED_COUNT;
    public static PlacementModifierType<NoiseBasedCountPlacementModifierAlpha> ALPHA_NOISE_BASED_COUNT;
    public static PlacementModifierType<NoiseBasedCountPlacementModifierInfdev325> INFDEV_325_NOISE_BASED_COUNT;
    public static PlacementModifierType<Infdev325CavePlacementModifier> INFDEV_325_CAVES;
    public static PlacementModifierType<NoiseBasedCountPlacementModifierInfdev415> INFDEV_415_NOISE_BASED_COUNT;
    public static PlacementModifierType<NoiseBasedCountPlacementModifierInfdev420> INFDEV_420_NOISE_BASED_COUNT;
    public static PlacementModifierType<NoiseBasedCountPlacementModifierInfdev611> INFDEV_611_NOISE_BASED_COUNT;
    
    public static PlacementModifierType<HeightmapSpreadDoublePlacementModifier> HEIGHTMAP_SPREAD_DOUBLE;
    //? }

    //~ if >=26.3 '> PlacementModifierType<P>' -> '> MapCodec<P>'
    private static <P extends PlacementModifier> PlacementModifierType<P> register(String id, com.mojang.serialization.MapCodec<P> codec) {
        return registryHandler.register(ModernerBeta.createId(id), /*? if <26.3 {*/ () -> /*? }*/ codec);
    }
    
    @SuppressWarnings("unchecked")
    public static void register(IRegistryHandler<?> handler) {
        registryHandler = (IRegistryHandler<PlacementModifierType<?>>) handler;

        /*? if <26.3 {*/ BETA_NOISE_BASED_COUNT = /*? }*/ register("beta_noise_based_count", NoiseBasedCountPlacementModifierBeta.MODIFIER_CODEC);
        /*? if <26.3 {*/ ALPHA_NOISE_BASED_COUNT = /*? }*/ register("alpha_noise_based_count", NoiseBasedCountPlacementModifierAlpha.MODIFIER_CODEC);
        /*? if <26.3 {*/ INFDEV_325_NOISE_BASED_COUNT = /*? }*/ register("infdev_325_noise_based_count", NoiseBasedCountPlacementModifierInfdev325.MODIFIER_CODEC);
        /*? if <26.3 {*/ INFDEV_325_CAVES = /*? }*/ register("infdev_325_caves", Infdev325CavePlacementModifier.MODIFIER_CODEC);
        /*? if <26.3 {*/ INFDEV_415_NOISE_BASED_COUNT = /*? }*/ register("infdev_415_noise_based_count", NoiseBasedCountPlacementModifierInfdev415.MODIFIER_CODEC);
        /*? if <26.3 {*/ INFDEV_420_NOISE_BASED_COUNT = /*? }*/ register("infdev_420_noise_based_count", NoiseBasedCountPlacementModifierInfdev420.MODIFIER_CODEC);
        /*? if <26.3 {*/ INFDEV_611_NOISE_BASED_COUNT = /*? }*/ register("infdev_611_noise_based_count", NoiseBasedCountPlacementModifierInfdev611.MODIFIER_CODEC);

        /*? if <26.3 {*/ HEIGHTMAP_SPREAD_DOUBLE = /*? }*/ register("heightmap_spread_double", HeightmapSpreadDoublePlacementModifier.MODIFIER_CODEC);
    }
    //~ }
}