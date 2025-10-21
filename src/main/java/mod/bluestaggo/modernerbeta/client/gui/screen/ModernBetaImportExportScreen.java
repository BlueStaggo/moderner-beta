package mod.bluestaggo.modernerbeta.client.gui.screen;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.slf4j.event.Level;

import java.util.function.Consumer;

public class ModernBetaImportExportScreen extends ModernBetaScreen {
    private final Gson gson = ModernerBeta.getSettingsGson().create();
    private final ModernBetaSettingsPreset settings;
    private final Consumer<ModernBetaSettingsPreset> onDone;

    private EditBox settingsBox;

    public ModernBetaImportExportScreen(Component title, Screen parent, ModernBetaSettingsPreset settings, Consumer<ModernBetaSettingsPreset> onDone) {
        super(title, parent);

        this.settings = settings;
        this.onDone = onDone;
    }

    @Override
    protected void init() {
        super.init();

        GridLayout gridWidgetMain = this.createGridWidget();
        GridLayout gridWidgetQuick = this.createGridWidget();

        GridLayout.RowHelper gridAdderMain = gridWidgetMain.createRowHelper(1);
        GridLayout.RowHelper gridAdderQuick = gridWidgetQuick.createRowHelper(2);

        gridAdderMain.addChild(gridWidgetQuick);

        Component copyString = Component.translatable("Copy");
        int copyStringLen = this.font.width(copyString);

        this.settingsBox = new EditBox(this.font, this.width - copyStringLen - 100, 20, Component.empty());
        this.settingsBox.setMaxLength(2000);
        this.settingsBox.setValue(this.save());
        Button copyToClipboard = Button.builder(copyString, button -> {
            this.minecraft.keyboardHandler.setClipboard(this.settingsBox.getValue());
        }).width(copyStringLen + 40).build();

        gridAdderQuick.addChild(this.settingsBox);
        gridAdderQuick.addChild(copyToClipboard);

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> {
            this.onDone.accept(this.fromString(this.settingsBox.getValue()));
            this.minecraft.setScreen(this.parent);
        }).bounds(this.width / 2 + 4, this.height - 26, BUTTON_LENGTH, BUTTON_HEIGHT).build());


        gridWidgetMain.arrangeElements();
        FrameLayout.alignInRectangle(gridWidgetMain, 0, this.overlayTop + 8, this.width, this.height, 0.5f, 0.0f);
        gridWidgetMain.visitWidgets(this::addRenderableWidget);
    }

    @Override
    public void resize(/*? <1.21.11 {*/ net.minecraft.client.Minecraft minecraft, /*?}*/ int width, int height) {
        String temp = this.settingsBox.getValue();
        this.init(/*? <1.21.11 {*/ minecraft, /*?}*/ width, height);
        this.settingsBox.setValue(temp);
    }

    private String save() {
        return this.gson.toJson(VersionCompat.getOrThrow(
            ModernBetaSettingsPreset.CODEC.encode(this.settings, JsonOps.INSTANCE, new JsonObject())));
    }

    private ModernBetaSettingsPreset fromString(String str) {
        JsonElement json = str != null && !str.isBlank() ? gson.fromJson(str, JsonElement.class) : null;

        try {
            return json != null ?
                VersionCompat.getOrThrow(ModernBetaSettingsPreset.CODEC.decode(JsonOps.INSTANCE, json)).getFirst() : null;
        } catch (Exception e) {
            ModernerBeta.log(Level.ERROR, "Unable to read settings JSON! Reverting to previous settings..");
            ModernerBeta.log(Level.ERROR, String.format("Reason: %s", e.getMessage()));
            return null;
        }
    }
}
