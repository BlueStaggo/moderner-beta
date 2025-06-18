package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;

import java.nio.file.Path;
import java.util.List;

public class ModernBetaGraphicalConfigSettingsScreen extends ModernBetaGraphicalComponentedSettingsScreen {
    public ModernBetaGraphicalConfigSettingsScreen(Screen parent, Path configDir) {
        super("createWorld.customize.modern_beta.title.config", parent, null, null, ModernerBeta.config.toCompound(), compound -> onDone(compound, configDir));
    }

    private static void onDone(NbtCompound compound, Path configDir) {
        ModernBetaSettings.CODEC.decode(NbtOps.INSTANCE, compound).result().ifPresent(result -> {
            ModernerBeta.config = result.getFirst();
            ModernerBeta.saveConfig(configDir);
        });
    }

    @Override
    protected void addOptions(OptionListWidget list) {
        this.addOptionsForComponents(list, List.of(
            SettingsComponentTypes.CONFIG_BETA_CLIMATIC_COLORS,
            SettingsComponentTypes.CONFIG_PE_CLIMATIC_COLORS,
            SettingsComponentTypes.CONFIG_BETA_FRACTAL_CLIMATIC_COLORS,
            SettingsComponentTypes.CONFIG_BIOME_PREVIEW_COLORS,
            SettingsComponentTypes.CONFIG_MISCELLANEOUS
        ));
    }
}
