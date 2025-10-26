package mod.bluestaggo.modernerbeta.fabric.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import mod.bluestaggo.modernerbeta.client.gui.screen.config.ModernBetaGraphicalConfigSettingsScreen;
import net.fabricmc.loader.api.FabricLoader;

public class ModernBetaModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ModernBetaGraphicalConfigSettingsScreen(parent, FabricLoader.getInstance().getConfigDir());
    }
}
