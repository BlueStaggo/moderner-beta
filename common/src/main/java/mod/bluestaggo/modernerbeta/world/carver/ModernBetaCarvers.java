package mod.bluestaggo.modernerbeta.world.carver;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.carver.Carver;

public class ModernBetaCarvers {
    public static final DeferredRegister<Carver<?>> CARVER = DeferredRegister.create(ModernerBeta.MOD_ID, RegistryKeys.CARVER);
    public static final RegistrySupplier<Carver<BetaCaveCarverConfig>> BETA_CAVE = register(
        "beta_cave", 
        new BetaCaveCarver(BetaCaveCarverConfig.CAVE_CODEC)
    );
    
    private static RegistrySupplier<Carver<BetaCaveCarverConfig>> register(String id, Carver<BetaCaveCarverConfig> carver) {
        return CARVER.register(ModernerBeta.createId(id), () -> carver);
    }
    
    public static void register() {
        CARVER.register();
    }
}
