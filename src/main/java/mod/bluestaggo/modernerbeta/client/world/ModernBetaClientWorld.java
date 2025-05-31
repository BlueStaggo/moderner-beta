package mod.bluestaggo.modernerbeta.client.world;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface ModernBetaClientWorld {
    boolean isModernBetaWorld();
    void setModernBetaWorld(boolean toggle);
}
