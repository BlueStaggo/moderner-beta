package mod.bluestaggo.modernerbeta.fabric.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import mod.bluestaggo.modernerbeta.client.gui.screen.config.graphical.ModernBetaGraphicalConfigSettingsScreen;
import net.fabricmc.loader.api.FabricLoader;

public class ModernBetaModMenu implements ModMenuApi {
    //FIXME: Currently Mod Menu does not have any builds for unobf Minecraft!!
    //? if <26.1 {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ModernBetaGraphicalConfigSettingsScreen(parent, FabricLoader.getInstance().getConfigDir());
    }
    //? }
}
