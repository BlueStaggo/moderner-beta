package mod.bluestaggo.modernerbeta.client.gui.screen.config.json;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import mod.bluestaggo.modernerbeta.client.gui.screen.ModernBetaScreen;
import mod.bluestaggo.modernerbeta.mixin.client.MultiLineEditBoxAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public abstract class ModernBetaJSONEditScreen extends ModernBetaScreen {
    private static final String TEXT_NAVIGATION = "createWorld.customize.modern_beta.navigation";
    private static final String TEXT_SETTINGS = "createWorld.customize.modern_beta.settings";
    private static final String TEXT_INVALID_JSON = "createWorld.customize.modern_beta.invalid_json";

    protected final Consumer<String> onDone;
    protected final Gson gson;
    protected String settingsString;

    private MultiLineEditBox widgetSettings;
    private StringWidget widgetInvalid;

    public ModernBetaJSONEditScreen(Component title, Screen parent, Consumer<String> onDone) {
        super(title, parent);

        this.onDone = onDone;
        this.gson = makeGson();
    }

    @Override
    protected void init() {
        super.init();

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
            this.onChange(this.isValidJson(this.settingsString));
        });

        Component textInvalid = Component.translatable(TEXT_INVALID_JSON).withStyle(ChatFormatting.RED);
        this.widgetInvalid = new StringWidget(textInvalid, this.font);


        GridLayout gridWidgetMain = this.createGridWidget();
        GridLayout gridWidgetFooter = this.createGridWidget();

        GridLayout.RowHelper mainContent = gridWidgetMain.createRowHelper(1);

        this.makeHeader(mainContent);
        mainContent.addChild(this.widgetSettings);
        mainContent.addChild(this.widgetInvalid);

        this.makeFooter(gridWidgetFooter);
        mainContent.addChild(gridWidgetFooter);

        gridWidgetMain.arrangeElements();
        FrameLayout.alignInRectangle(gridWidgetMain, 0, this.overlayTop + 8, this.width, this.height, 0.5f, 0.0f);
        gridWidgetMain.visitWidgets(this::addRenderableWidget);

        // Set cursor to beginning of edit box
        MultilineTextField editBox = ((MultiLineEditBoxAccessor) this.widgetSettings).getTextField();
        editBox.seekCursor(Whence.ABSOLUTE, 0);

        this.onChange(this.isValidJson(this.settingsString));
    }

    protected void makeHeader(GridLayout.RowHelper content) {
        Component textNavigation = Component.translatable(TEXT_NAVIGATION);
        StringWidget widgetNavigation = new StringWidget(textNavigation, this.font);

        content.addChild(widgetNavigation);
    }

    protected abstract void makeFooter(GridLayout grid);

    protected abstract Gson makeGson();

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
