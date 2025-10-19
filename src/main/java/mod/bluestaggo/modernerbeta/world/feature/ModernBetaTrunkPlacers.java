package mod.bluestaggo.modernerbeta.world.feature;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.mixin.TrunkPlacerTypeAccessor;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.world.feature.trunk.BetaLargeOakTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

public class ModernBetaTrunkPlacers {
    private static IRegistryHandler<TrunkPlacerType<?>> registryHandler;
    public static TrunkPlacerType<BetaLargeOakTrunkPlacer> BETA_LARGE_OAK;

    private static <P extends TrunkPlacer> TrunkPlacerType<P> register(
            String id,
            com.mojang.serialization.MapCodec<P> codec
    ) {
        return registryHandler.register(ModernerBeta.createId(id), TrunkPlacerTypeAccessor.create(codec));
    }

    @SuppressWarnings("unchecked")
    public static void register(IRegistryHandler<?> handler) {
        registryHandler = (IRegistryHandler<TrunkPlacerType<?>>) handler;
        BETA_LARGE_OAK = register(
                ModernBetaFeatureTags.BETA_LARGE_OAK_TRUNK_PLACER, BetaLargeOakTrunkPlacer.CODEC
        );
    }
}
