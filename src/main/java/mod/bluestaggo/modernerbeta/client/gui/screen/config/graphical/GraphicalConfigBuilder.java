package mod.bluestaggo.modernerbeta.client.gui.screen.config.graphical;

import net.minecraft.client.gui.components.OptionsList;

@FunctionalInterface
public interface GraphicalConfigBuilder {
    void apply(ModernBetaGraphicalCompoundSettingsScreen screen, OptionsList options);
}
