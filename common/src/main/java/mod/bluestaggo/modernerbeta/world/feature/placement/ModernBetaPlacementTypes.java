package mod.bluestaggo.modernerbeta.world.feature.placement;

import com.mojang.serialization.MapCodec;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;

public class ModernBetaPlacementTypes {
    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPE = DeferredRegister.create(ModernerBeta.MOD_ID, RegistryKeys.PLACEMENT_MODIFIER_TYPE);
    public static final RegistrySupplier<PlacementModifierType<NoiseBasedCountPlacementModifierBeta>> BETA_NOISE_BASED_COUNT;
    public static final RegistrySupplier<PlacementModifierType<NoiseBasedCountPlacementModifierAlpha>> ALPHA_NOISE_BASED_COUNT;
    public static final RegistrySupplier<PlacementModifierType<NoiseBasedCountPlacementModifierInfdev325>> INFDEV_325_NOISE_BASED_COUNT;
    public static final RegistrySupplier<PlacementModifierType<Infdev325CavePlacementModifier>> INFDEV_325_CAVES;
    public static final RegistrySupplier<PlacementModifierType<NoiseBasedCountPlacementModifierInfdev415>> INFDEV_415_NOISE_BASED_COUNT;
    public static final RegistrySupplier<PlacementModifierType<NoiseBasedCountPlacementModifierInfdev420>> INFDEV_420_NOISE_BASED_COUNT;
    public static final RegistrySupplier<PlacementModifierType<NoiseBasedCountPlacementModifierInfdev611>> INFDEV_611_NOISE_BASED_COUNT;
    
    public static final RegistrySupplier<PlacementModifierType<HeightmapSpreadDoublePlacementModifier>> HEIGHTMAP_SPREAD_DOUBLE;
    
    private static <P extends PlacementModifier> RegistrySupplier<PlacementModifierType<P>> register(String id, MapCodec<P> codec) {
        return PLACEMENT_MODIFIER_TYPE.register(ModernerBeta.createId(id), () -> () -> codec);
    }
    
    public static void register() {
        PLACEMENT_MODIFIER_TYPE.register();
    }
    
    static {
        BETA_NOISE_BASED_COUNT = register("beta_noise_based_count", NoiseBasedCountPlacementModifierBeta.MODIFIER_CODEC);
        ALPHA_NOISE_BASED_COUNT = register("alpha_noise_based_count", NoiseBasedCountPlacementModifierAlpha.MODIFIER_CODEC);
        INFDEV_325_NOISE_BASED_COUNT = register("infdev_325_noise_based_count", NoiseBasedCountPlacementModifierInfdev325.MODIFIER_CODEC);
        INFDEV_325_CAVES = register("infdev_325_caves", Infdev325CavePlacementModifier.MODIFIER_CODEC);
        INFDEV_415_NOISE_BASED_COUNT = register("infdev_415_noise_based_count", NoiseBasedCountPlacementModifierInfdev415.MODIFIER_CODEC);
        INFDEV_420_NOISE_BASED_COUNT = register("infdev_420_noise_based_count", NoiseBasedCountPlacementModifierInfdev420.MODIFIER_CODEC);
        INFDEV_611_NOISE_BASED_COUNT = register("infdev_611_noise_based_count", NoiseBasedCountPlacementModifierInfdev611.MODIFIER_CODEC);
    
        HEIGHTMAP_SPREAD_DOUBLE = register("heightmap_spread_double", HeightmapSpreadDoublePlacementModifier.MODIFIER_CODEC);
    }
}