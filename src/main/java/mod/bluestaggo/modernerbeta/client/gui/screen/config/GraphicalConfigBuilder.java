package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.OptionsList;

@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface GraphicalConfigBuilder {
    void apply(ModernBetaGraphicalCompoundSettingsScreen screen, OptionsList options);
}
