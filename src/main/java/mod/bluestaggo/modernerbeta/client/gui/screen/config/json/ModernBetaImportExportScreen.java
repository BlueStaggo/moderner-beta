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
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;

import java.util.function.Consumer;

public class ModernBetaImportExportScreen extends ModernBetaJSONEditScreen {
    private static final String TEXT_SETTINGS_SAVE = "createWorld.customize.modern_beta.settings.save";
    private static final String COPY_PASTE_HINT = "createWorld.customize.modern_beta.settings.import_export.hint";
    public static final int BUTTON_LENGTH = 100;

    private Button widgetDone;

    public ModernBetaImportExportScreen(String title, Screen parent, ModernBetaSettingsPreset settings, HolderLookup.Provider registries, Consumer<String> onDone) {
        super(Component.translatable(title), parent, onDone);

        this.settingsString = this.gson.toJson(VersionCompat.getOrThrow(
            ModernBetaSettingsPreset.CODEC.encode(settings, RegistryOps.create(JsonOps.INSTANCE, registries), new JsonObject())));
    }

    @Override
    protected void initHeader(GridLayout headerLayout) {
        super.initHeader(headerLayout);

        Component textPasting = Component.translatable(COPY_PASTE_HINT);
        StringWidget widgetPasting = new StringWidget(textPasting, this.font);

        headerLayout.addChild(widgetPasting, 1, 0);
    }

    @Override
    protected void initFooter(GridLayout footerLayout) {
        GridLayout.RowHelper row = footerLayout.createRowHelper(3);

        this.widgetDone = Button.builder(Component.translatable(TEXT_SETTINGS_SAVE), button -> {
            this.onDone.accept(this.settingsString);
            this.minecraft.setScreen(this.parent);
        }).size(BUTTON_LENGTH, ModernBetaScreen.BUTTON_HEIGHT).build();

        row.addChild(Button.builder(Component.translatable("chat.copy"), button ->
            this.minecraft.keyboardHandler.setClipboard(this.settingsString)
        ).size(BUTTON_LENGTH, ModernBetaScreen.BUTTON_HEIGHT).build());
        row.addChild(this.widgetDone);
        row.addChild(Button.builder(CommonComponents.GUI_CANCEL, button ->
            this.minecraft.setScreen(this.parent)
        ).size(BUTTON_LENGTH, ModernBetaScreen.BUTTON_HEIGHT).build());
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
