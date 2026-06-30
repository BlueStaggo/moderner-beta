package mod.bluestaggo.modernerbeta.level.carver;

//? if >=26.3
//import com.mojang.serialization.MapCodec;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;

public class ModernBetaCarvers {
    //~ if >=26.3 'WorldCarver<?>>' -> 'MapCodec<? extends WorldCarver>>' {
    private static IRegistryHandler<WorldCarver<?>> registryHandler;
    //? if <26.3
    public static WorldCarver<BetaCaveCarverConfiguration> BETA_CAVE;

    //~ if >=26.3 'WorldCarver<?>' -> 'MapCodec<? extends WorldCarver>'
    private static WorldCarver<?> register(String id, WorldCarver<?> carver) {
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
