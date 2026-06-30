package mod.bluestaggo.modernerbeta.level.carver;

//? if >=26.3
//import com.mojang.serialization.MapCodec;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import net.minecraft.world.level.levelgen.carver.*;

public class ModernBetaCarvers {
    //~ if >=26.3 'WorldCarver<?>>' -> 'MapCodec<? extends WorldCarver>>' {
    private static IRegistryHandler<WorldCarver<?>> registryHandler;
    //? if <26.3
    public static WorldCarver<BetaCaveCarverConfiguration> BETA_CAVE;

    //~ if >=26.3 '<C extends CarverConfiguration>' -> '<C extends WorldCarver>', 'WorldCarver<C>' -> 'MapCodec<C>'
    private static <C extends CarverConfiguration> WorldCarver<C> register(String id, WorldCarver<C> carver) {
        return registryHandler.register(ModernerBeta.createId(id), carver);
    }
    
    @SuppressWarnings("unchecked")
    public static void register(IRegistryHandler<?> handler) {
        registryHandler = (IRegistryHandler<WorldCarver<?>>) handler;

        /*? if <26.3 {*/ BETA_CAVE = /*? }*/ register(
            "beta_cave",
            //? if >=26.3 {
            /*BetaCaveWorldCarver.MAP_CODEC
            *///? } else {
            new BetaCaveWorldCarver(BetaCaveCarverConfiguration.CODEC)
            //? }
        );
    }
    //~ }
}
