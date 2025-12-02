package mod.bluestaggo.modernerbeta.client.gui.screen.config.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
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

public class ModernBetaSettingsScreen extends ModernBetaJSONEditScreen {
    private static final String TEXT_NAVIGATION = "createWorld.customize.modern_beta.navigation";
    private static final String TEXT_SETTINGS_SAVE = "createWorld.customize.modern_beta.settings.save";

    private Button widgetDone;

    public ModernBetaSettingsScreen(String title, Screen parent, ModernBetaSettings settings, HolderLookup.Provider registries, Consumer<String> onDone) {
        super(Component.translatable(title), parent, onDone);

        this.settingsString = this.gson.toJson(VersionCompat.getOrThrow(
            ModernBetaSettings.CODEC.encode(settings, RegistryOps.create(JsonOps.INSTANCE, registries), new JsonObject())));
    }

    @Override
    protected void initHeader(GridLayout headerLayout) {
        super.initHeader(headerLayout);

        Component textNavigation = Component.translatable(TEXT_NAVIGATION);
        StringWidget widgetNavigation = new StringWidget(textNavigation, this.font);

        headerLayout.addChild(widgetNavigation, 1, 0);
    }

    @Override
    protected void initFooter(GridLayout footerLayout) {
        GridLayout.RowHelper row = footerLayout.createRowHelper(2);

        this.widgetDone = Button.builder(Component.translatable(TEXT_SETTINGS_SAVE), button -> {
            this.onDone.accept(this.settingsString);
            this.minecraft.setScreen(this.parent);
        }).size(BUTTON_LENGTH, BUTTON_HEIGHT).build();

        row.addChild(this.widgetDone);
        row.addChild(Button.builder(CommonComponents.GUI_CANCEL, button ->
            this.minecraft.setScreen(this.parent)
        ).size(BUTTON_LENGTH, BUTTON_HEIGHT).build());
    }

    @Override
    protected Gson makeGson() {
        return ModernerBeta.getSettingsGson().setPrettyPrinting().create();
    }

    @Override
    protected void onChange(boolean isValid) {
        super.onChange(isValid);
        this.widgetDone.active = isValid;
    }
}

