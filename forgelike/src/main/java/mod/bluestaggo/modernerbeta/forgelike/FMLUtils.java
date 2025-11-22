package mod.bluestaggo.modernerbeta.forgelike;

//? if neoforge {
import net.neoforged.fml.loading.FMLLoader;
//?} else {
/*import net.minecraftforge.fml.loading.FMLLoader;
*///?}

public class FMLUtils {
    public static boolean isModPresent(String mod) {
        return FMLLoader/*? >=1.21.9 {*//*.getCurrent()*//*?}*/.getLoadingModList().getModFileById(mod) != null;
    }
}
