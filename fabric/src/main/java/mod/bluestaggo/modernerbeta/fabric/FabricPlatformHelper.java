package mod.bluestaggo.modernerbeta.fabric;

import mod.bluestaggo.modernerbeta.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatformHelper implements IPlatformHelper {
    private final FabricLoader loader = FabricLoader.getInstance();

    @Override
    public boolean isModPresent(String mod) {
        return loader.isModLoaded(mod);
    }

    @Override
    public boolean isDevEnvironment() {
        return loader.isDevelopmentEnvironment();
    }
}
