package mod.bluestaggo.modernerbeta.client.gui.screen.config.json;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import mod.bluestaggo.modernerbeta.client.gui.screen.ModernBetaScreen;
import mod.bluestaggo.modernerbeta.mixin.client.MultiLineEditBoxAccessor;
import mod.bluestaggo.modernerbeta.mixin.client.MultilineTextFieldAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public abstract class ModernBetaJSONEditScreen extends ModernBetaScreen {
    private static final String TEXT_SETTINGS = "createWorld.customize.modern_beta.settings";
    private static final String TEXT_INVALID_JSON = "createWorld.customize.modern_beta.invalid_json";

    protected final Consumer<String> onDone;
    protected final Gson gson;
    protected String settingsString;

    private MultiLineEditBox widgetSettings;
    private StringWidget widgetInvalid;

    public ModernBetaJSONEditScreen(Component title, Screen parent, Consumer<String> onDone) {
        super(title, parent, 40, 33);

        this.onDone = onDone;
        this.gson = makeGson();
    }

    @Override
    protected void init() {
        super.init();

        // Set cursor to beginning of edit box
        MultilineTextField editBox = ((MultiLineEditBoxAccessor) this.widgetSettings).getTextField();
        editBox.seekCursor(Whence.ABSOLUTE, 0);

        this.onChange(this.isValidJson(this.settingsString));
    }

    @Override
    protected void initContent(GridLayout contentLayout) {
        GridLayout.RowHelper row = contentLayout.createRowHelper(1);

        //? if >=1.21.6 {
        this.widgetSettings = MultiLineEditBox.builder().build(
        //?} else {
        /*this.widgetSettings = new MultiLineEditBox(
         *///?}
            this.font,
            //? if <1.21.6
            //0, 0,
            100, 100,
            //? if <1.21.6
            //Component.literal(""),
            Component.translatable(TEXT_SETTINGS)
        );
        this.widgetSettings.setValue(this.settingsString);
        this.widgetSettings.setValueListener(string -> {
            this.settingsString = string;
            this.onChange(this.isValidJson(this.settingsString));
        });

        Component textInvalid = Component.translatable(TEXT_INVALID_JSON).withStyle(ChatFormatting.RED);
        this.widgetInvalid = new StringWidget(textInvalid, this.font);

        row.addChild(this.widgetSettings);
        row.addChild(this.widgetInvalid);
    }

    protected abstract Gson makeGson();

    @Override
    protected void repositionElements() {
        int editBoxWidth = this.width - 16;
        int editBoxHeight = this.layout.getContentHeight() - 16;
        MultilineTextField textField = ((MultiLineEditBoxAccessor) this.widgetSettings).getTextField();

        int textWidth = ((MultilineTextFieldAccessor) textField).getWidth();
        int boxWidth = this.widgetSettings.getWidth();
        int totalPadding = boxWidth - textWidth;

        //? if >=1.20.3 {
        this.widgetSettings.setSize(editBoxWidth, editBoxHeight);
        //? } else {
        /*this.widgetSettings.setWidth(editBoxWidth);
        ((mod.bluestaggo.modernerbeta.mixin.client.AbstractWidgetAccessor) this.widgetSettings).setHeight(editBoxHeight);
        *///? }
        ((MultilineTextFieldAccessor) textField).setWidth(editBoxWidth + totalPadding);
        ((MultilineTextFieldAccessor) textField).invokeReflowDisplayLines();

        super.repositionElements();
    }

    protected void onChange(boolean isValid) {
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
