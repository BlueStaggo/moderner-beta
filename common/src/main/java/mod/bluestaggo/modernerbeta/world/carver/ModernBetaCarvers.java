package mod.bluestaggo.modernerbeta.world.carver;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import net.minecraft.world.gen.carver.Carver;

public class ModernBetaCarvers {
    private static IRegistryHandler<Carver<?>> registryHandler;
    public static Carver<BetaCaveCarverConfig> BETA_CAVE;
    
    private static Carver<BetaCaveCarverConfig> register(String id, Carver<BetaCaveCarverConfig> carver) {
        return registryHandler.register(ModernerBeta.createId(id), carver);
    }
    
    @SuppressWarnings("unchecked")
    public static void register(IRegistryHandler<?> handler) {
        registryHandler = (IRegistryHandler<Carver<?>>) handler;

        BETA_CAVE = register(
                "beta_cave",
                new BetaCaveCarver(BetaCaveCarverConfig.CAVE_CODEC)
        );
    }
}
