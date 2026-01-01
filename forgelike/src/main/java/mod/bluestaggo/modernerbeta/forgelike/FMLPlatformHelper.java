package mod.bluestaggo.modernerbeta.forgelike;

import mod.bluestaggo.modernerbeta.IPlatformHelper;
//? if neoforge {
import net.neoforged.fml.loading.FMLLoader;
//?} else {
/*import net.minecraftforge.fml.loading.FMLLoader;
*///?}

public class FMLPlatformHelper implements IPlatformHelper {
    public boolean isModPresent(String mod) {
        return FMLLoader/*? >=1.21.9 {*//*.getCurrent()*//*?}*/.getLoadingModList().getModFileById(mod) != null;
    }

    @Override
    public boolean isDevEnvironment() {
        return !FMLLoader/*? >=1.21.9 {*//*.getCurrent()*//*?}*/.isProduction();
    }
}
