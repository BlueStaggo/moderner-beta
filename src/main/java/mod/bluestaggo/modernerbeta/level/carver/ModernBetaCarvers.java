package mod.bluestaggo.modernerbeta.level.carver;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import net.minecraft.world.level.levelgen.carver.WorldCarver;

public class ModernBetaCarvers {
    private static IRegistryHandler<WorldCarver<?>> registryHandler;
    public static WorldCarver<BetaCaveCarverConfiguration> BETA_CAVE;
    
    private static WorldCarver<BetaCaveCarverConfiguration> register(String id, WorldCarver<BetaCaveCarverConfiguration> carver) {
        return registryHandler.register(ModernerBeta.createId(id), carver);
    }
    
    @SuppressWarnings("unchecked")
    public static void register(IRegistryHandler<?> handler) {
        registryHandler = (IRegistryHandler<WorldCarver<?>>) handler;

        BETA_CAVE = register(
                "beta_cave",
                new BetaCaveWorldCarver(BetaCaveCarverConfiguration.CODEC)
        );
    }
}
