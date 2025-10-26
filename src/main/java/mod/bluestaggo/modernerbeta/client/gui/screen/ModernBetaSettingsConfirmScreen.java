package mod.bluestaggo.modernerbeta.client.gui.screen;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ModernBetaSettingsConfirmScreen extends ModernBetaScreen {
    private final Runnable runnable;
    private final Component messageText;
    private final Component confirmText;
    
    public ModernBetaSettingsConfirmScreen(Screen parent, Runnable runnable, Component messageText, Component confirmText) {
        super(Component.empty(), parent);
        
        this.runnable = runnable;
        this.messageText = messageText;
        this.confirmText = confirmText;
    }

    @Override
    protected void init() {
        super.init();
        
        Button buttonDone = Button.builder(this.confirmText, button -> {
            this.runnable.run();
            this.minecraft.setScreen(this.parent);
        }).bounds(0, 0, BUTTON_LENGTH, BUTTON_HEIGHT).build();
        
        Button buttonCancel = Button.builder(CommonComponents.GUI_CANCEL, button ->
            this.minecraft.setScreen(this.parent)
        ).bounds(this.width / 2 + 4, this.height - 27, BUTTON_LENGTH, BUTTON_HEIGHT).build();
        
        GridLayout gridWidgetMain = this.createGridWidget();
        GridLayout gridWidgetButtons = this.createGridWidget();
        gridWidgetButtons.defaultCellSetting().alignVerticallyMiddle().alignHorizontallyCenter();
        
        GridLayout.RowHelper gridAdderMain = gridWidgetMain.createRowHelper(1);
        GridLayout.RowHelper gridAdderButtons = gridWidgetButtons.createRowHelper(2);
        
        gridAdderMain.addChild(new StringWidget(this.messageText, this.font));
        gridAdderMain.addChild(new StringWidget(Component.empty(), this.font));
        gridAdderMain.addChild(gridWidgetButtons);
        
        gridAdderButtons.addChild(buttonDone);
        gridAdderButtons.addChild(buttonCancel);
        
        gridWidgetMain.arrangeElements();
        FrameLayout.alignInRectangle(gridWidgetMain, 0, this.height / 2 - gridWidgetMain.getHeight(), this.width, this.height, 0.5f, 0.0f);
        gridWidgetMain.visitWidgets(this::addRenderableWidget);
    }
}
