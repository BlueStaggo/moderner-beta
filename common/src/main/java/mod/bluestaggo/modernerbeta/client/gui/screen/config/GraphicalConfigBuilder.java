package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import net.minecraft.client.gui.widget.OptionListWidget;

@FunctionalInterface
public interface GraphicalConfigBuilder {
    void apply(ModernBetaGraphicalProviderSettingsScreen screen, OptionListWidget options);
}
