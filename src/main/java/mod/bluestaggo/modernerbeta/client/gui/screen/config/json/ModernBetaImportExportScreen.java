package mod.bluestaggo.modernerbeta.client.gui.screen.config.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.gui.screen.ModernBetaScreen;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class ModernBetaImportExportScreen extends ModernBetaJSONEditScreen {
    private static final String TEXT_SETTINGS_SAVE = "createWorld.customize.modern_beta.settings.save";
    public static final int BUTTON_LENGTH = 100;

    private Button widgetDone;

    public ModernBetaImportExportScreen(String title, Screen parent, ModernBetaSettingsPreset settings, Consumer<String> onDone) {
        super(Component.translatable(title), parent, onDone);

        this.settingsString = this.gson.toJson(VersionCompat.getOrThrow(
            ModernBetaSettingsPreset.CODEC.encode(settings, JsonOps.INSTANCE, new JsonObject())));
    }

    @Override
    protected void makeHeader(GridLayout.RowHelper content) {
        Component textPasting = Component.translatable("Copy and paste JSON code into this text box");
        StringWidget widgetPasting = new StringWidget(textPasting, this.font);

        content.addChild(widgetPasting);
    }

    @Override
    protected void makeFooter(GridLayout grid) {
        GridLayout.RowHelper row = grid.createRowHelper(3);

        this.widgetDone = Button.builder(Component.translatable(TEXT_SETTINGS_SAVE), button -> {
            this.onDone.accept(this.settingsString);
            this.minecraft.setScreen(this.parent);
        }).bounds(0, 0, BUTTON_LENGTH, ModernBetaScreen.BUTTON_HEIGHT).build();

        row.addChild(Button.builder(Component.translatable("Copy to clipboard"), button ->
            this.minecraft.keyboardHandler.setClipboard(this.settingsString)
        ).bounds(0, 0, BUTTON_LENGTH, ModernBetaScreen.BUTTON_HEIGHT).build());
        row.addChild(this.widgetDone);
        row.addChild(Button.builder(CommonComponents.GUI_CANCEL, button ->
            this.minecraft.setScreen(this.parent)
        ).bounds(0, 0, BUTTON_LENGTH, ModernBetaScreen.BUTTON_HEIGHT).build());
    }

    @Override
    protected Gson makeGson() {
        return ModernerBeta.getSettingsGson().create();
    }

    @Override
    protected void onChange(boolean isValid) {
        super.onChange(isValid);
        this.widgetDone.active = isValid;
    }
}
