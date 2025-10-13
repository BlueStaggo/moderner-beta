package mod.bluestaggo.modernerbeta.world.feature;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.mixin.AccessorTrunkPlacerType;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.world.feature.trunk.BetaLargeOakTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

public class ModernBetaTrunkPlacers {
    private static IRegistryHandler<TrunkPlacerType<?>> registryHandler;
    public static TrunkPlacerType<BetaLargeOakTrunkPlacer> BETA_LARGE_OAK_TRUNK_PLACER;

    private static <P extends TrunkPlacer> TrunkPlacerType<P> register(
            String id,
            com.mojang.serialization./*Map*/Codec<P> codec
    ) {
        return registryHandler.register(ModernerBeta.createId(id), AccessorTrunkPlacerType.create(codec));
    }

    @SuppressWarnings("unchecked")
    public static void register(IRegistryHandler<?> handler) {
        registryHandler = (IRegistryHandler<TrunkPlacerType<?>>) handler;
        BETA_LARGE_OAK_TRUNK_PLACER = register(
                ModernBetaFeatureTags.BETA_LARGE_OAK_TRUNK_PLACER, BetaLargeOakTrunkPlacer.CODEC
        );
    }
}
