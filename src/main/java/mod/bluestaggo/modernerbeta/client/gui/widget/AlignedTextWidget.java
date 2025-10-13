package mod.bluestaggo.modernerbeta.client.gui.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractStringWidget;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class AlignedTextWidget extends AbstractStringWidget {
    private float horizontalAlignment = 0.5F;

    public AlignedTextWidget(Component message, Font textRenderer) {
        this(0, 0, textRenderer.width(message.getVisualOrderText()), 9, message, textRenderer);
    }

    public AlignedTextWidget(int width, int height, Component message, Font textRenderer) {
        this(0, 0, width, height, message, textRenderer);
    }

    public AlignedTextWidget(int x, int y, int width, int height, Component message, Font textRenderer) {
        super(x, y, width, height, message, textRenderer);
        this.active = false;
    }

    public AlignedTextWidget setColor(int textColor) {
        super.setColor(textColor);
        return this;
    }

    public AlignedTextWidget align(float horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
        return this;
    }

    public AlignedTextWidget alignLeft() {
        return this.align(0.0F);
    }

    public AlignedTextWidget alignCenter() {
        return this.align(0.5F);
    }

    public AlignedTextWidget alignRight() {
        return this.align(1.0F);
    }

    @Override
    public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float deltaTicks) {
        Component message = this.getMessage();
        Font textRenderer = this.getFont();

        int width = this.getWidth();
        int textWidth = textRenderer.width(message);

        int x = this.getX() + Math.round(this.horizontalAlignment * (width - textWidth));
        int y = this.getY() + (this.getHeight() - 9) / 2;
        FormattedCharSequence orderedText = textWidth > width ? this.trim(message, width) : message.getVisualOrderText();
        context.drawString(textRenderer, orderedText, x, y, this.getColor());
    }

    private FormattedCharSequence trim(Component text, int width) {
        Font textRenderer = this.getFont();
        FormattedText str = textRenderer.substrByWidth(text, width - textRenderer.width(CommonComponents.ELLIPSIS));
        return Language.getInstance().getVisualOrder(FormattedText.composite(str, CommonComponents.ELLIPSIS));
    }
}