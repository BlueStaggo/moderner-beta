package mod.bluestaggo.modernerbeta.forgelike;

//? if neoforge {
import net.neoforged.fml.loading.FMLLoader;
//?} else {
/*import net.minecraftforge.fml.loading.FMLLoader;
*///?}

public class ModernerBetaForgeLike {
    public static boolean isModPresent(String mod) {
        return FMLLoader.getLoadingModList().getModFileById(mod) != null;
    }
}
