package mod.bluestaggo.modernerbeta.client.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public abstract class ModernBetaScreen extends Screen {
    public static final int BUTTON_HEIGHT = 20;
    public static final int BUTTON_LENGTH = 150;
    public static final int BUTTON_LENGTH_PRESET = 200;
    public static final int BUTTON_HEIGHT_PRESET = 20;
    
    protected final Screen parent;
    protected int overlayLeft;
    protected int overlayRight;
    protected int overlayTop;
    protected int overlayBottom;
    
    public ModernBetaScreen(Component title, Screen parent) {
        super(title);
        
        this.parent = parent;
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        //? if <1.20.5
        /*this.renderBackground(graphics);*/
        super.render(graphics, mouseX, mouseY, delta);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 16, 0xFFFFFFFF);
    }
    
    @Override
    protected void init() {
        this.overlayLeft = 0;
        this.overlayRight = this.width;
        this.overlayTop = 32;
        this.overlayBottom = this.height - 32;
    }
    
    protected GridLayout createGridWidget() {
        GridLayout gridWidget = new GridLayout();
        gridWidget.defaultCellSetting().paddingHorizontal(5).paddingBottom(4).alignHorizontallyCenter().alignVerticallyTop();
        
        return gridWidget;
    }
    
    protected void addGridTextButtonPair(GridLayout.RowHelper adder, String text, Button buttonWidget) {
        adder.addChild(new StringWidget(Component.translatable(text), this.font));
        adder.addChild(buttonWidget);
    }

    protected void addGridTextButtonTriplet(GridLayout.RowHelper adder, String text, Button buttonWidget, Button buttonWidget2) {
        adder.addChild(new StringWidget(Component.translatable(text), this.font));
        adder.addChild(buttonWidget);
        adder.addChild(buttonWidget2);
    }
}
