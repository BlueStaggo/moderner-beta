package mod.bluestaggo.modernerbeta.client.gui.screen;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.JsonOps;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.mixin.client.MultiLineEditBoxAccessor;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.components.MultilineTextField;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Whence;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class ModernBetaSettingsScreen extends ModernBetaScreen {
    private static final String TEXT_NAVIGATION = "createWorld.customize.modern_beta.navigation";
    private static final String TEXT_SETTINGS = "createWorld.customize.modern_beta.settings";
    private static final String TEXT_SETTINGS_SAVE = "createWorld.customize.modern_beta.settings.save";
    private static final String TEXT_INVALID_JSON = "createWorld.customize.modern_beta.invalid_json";
    
    private final Consumer<String> onDone;
    private final Gson gson;
    private String settingsString;
    
    private Button widgetDone;
    private MultiLineEditBox widgetSettings;
    private StringWidget widgetInvalid;
    
    public ModernBetaSettingsScreen(String title, Screen parent, ModernBetaSettings settings, Consumer<String> onDone) {
        super(Component.translatable(title), parent);

        this.onDone = onDone;
        this.gson = ModernerBeta.getSettingsGson().setPrettyPrinting().create();
        this.settingsString = this.gson.toJson(VersionCompat.getOrThrow(
            ModernBetaSettings.CODEC.encode(settings, JsonOps.INSTANCE, new JsonObject())));
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.widgetDone = Button.builder(Component.translatable(TEXT_SETTINGS_SAVE), button -> {
            this.onDone.accept(this.settingsString);
            this.minecraft.setScreen(this.parent);
        }).bounds(this.width / 2 - 154, this.height - 26, BUTTON_LENGTH, BUTTON_HEIGHT).build();
        
        this.addRenderableWidget(this.widgetDone);
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> 
            this.minecraft.setScreen(this.parent)
        ).bounds(this.width / 2 + 4, this.height - 26, BUTTON_LENGTH, BUTTON_HEIGHT).build());
        
        int editBoxWidth = this.width - 16;
        int editBoxHeight = this.height - 96;

        //? if >=1.21.6 {
        this.widgetSettings = MultiLineEditBox.builder().build(
        //?} else {
        /*this.widgetSettings = new MultiLineEditBox(
        *///?}
                this.font,
                //? if <1.21.6
                /*0, 0,*/
                editBoxWidth, editBoxHeight,
                //? if <1.21.6
                /*Component.literal(""),*/
                Component.translatable(TEXT_SETTINGS)
        );
        this.widgetSettings.setValue(this.settingsString);
        this.widgetSettings.setValueListener(string -> {
            this.settingsString = string;
            this.onChange();
        });
        
        Component textInvalid = Component.translatable(TEXT_INVALID_JSON).withStyle(ChatFormatting.RED);
        this.widgetInvalid = new StringWidget(textInvalid, this.font);
        
        Component textNavigation = Component.translatable(TEXT_NAVIGATION);
        StringWidget widgetNavigation = new StringWidget(textNavigation, this.font);
        
        GridLayout gridWidget = this.createGridWidget();
        
        GridLayout.RowHelper gridWidgetAdder = gridWidget.createRowHelper(1);
        gridWidgetAdder.addChild(widgetNavigation);
        gridWidgetAdder.addChild(this.widgetSettings);
        gridWidgetAdder.addChild(this.widgetInvalid);
        
        gridWidget.arrangeElements();
        FrameLayout.alignInRectangle(gridWidget, 0, this.overlayTop + 8, this.width, this.height, 0.5f, 0.0f);
        gridWidget.visitWidgets(this::addRenderableWidget);
        
        // Set cursor to beginning of edit box
        MultilineTextField editBox = ((MultiLineEditBoxAccessor) this.widgetSettings).getTextField();
        editBox.seekCursor(Whence.ABSOLUTE, 0);
        
        this.onChange();
    }
    
    private void onChange() {
        boolean isValid = this.isValidJson(this.settingsString);
        
        this.widgetDone.active = isValid;
        this.widgetInvalid.visible = !isValid;
    }
    
    private boolean isValidJson(String json) {
        try {
            JsonParser.parseString(json);
        } catch (JsonSyntaxException e) {
            return false;
        }
        
        return true;
    }
}

