package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.OptionListWidget;

@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface GraphicalConfigBuilder {
    void apply(ModernBetaGraphicalCompoundSettingsScreen screen, OptionListWidget options);
}
