package mod.bluestaggo.modernerbeta.client.gui.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractStringWidget;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;

@SuppressWarnings("unused")
public class AlignedStringWidget extends AbstractStringWidget {
    private float horizontalAlignment = 0.5F;

    public AlignedStringWidget(Component message, Font textRenderer) {
        this(0, 0, textRenderer.width(message.getVisualOrderText()), 9, message, textRenderer);
    }

    public AlignedStringWidget(int width, int height, Component message, Font textRenderer) {
        this(0, 0, width, height, message, textRenderer);
    }

    public AlignedStringWidget(int x, int y, int width, int height, Component message, Font textRenderer) {
        super(x, y, width, height, message, textRenderer);
        this.active = false;
    }

    //? <1.21.11 {
    @Override
    public AlignedStringWidget setColor(int textColor) {
        super.setColor(textColor);
        return this;
    }
    //? }

    public AlignedStringWidget align(float horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
        return this;
    }

    public AlignedStringWidget alignLeft() {
        return this.align(0.0F);
    }

    public AlignedStringWidget alignCenter() {
        return this.align(0.5F);
    }

    public AlignedStringWidget alignRight() {
        return this.align(1.0F);
    }

    @Override
    //? if >=1.21.11 {
    /*public void visitLines(net.minecraft.client.gui.ActiveTextCollector collector) {
    *///? } else {
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float deltaTicks) {
    //? }
        Component message = this.getMessage();
        Font textRenderer = this.getFont();

        int width = this.getWidth();
        int textWidth = textRenderer.width(message);

        int x = this.getX() + Math.round(this.horizontalAlignment * (width - textWidth));
        int y = this.getY() + (this.getHeight() - 9) / 2;
        FormattedCharSequence orderedText = textWidth > width ? this.trim(message, width) : message.getVisualOrderText();
        //? if >=1.21.11 {
        /*collector.accept(x, y, orderedText);
        *///? } else {
        graphics.drawString(textRenderer, orderedText, x, y, this.getColor());
        //? }
    }

    private FormattedCharSequence trim(Component text, int width) {
        Font textRenderer = this.getFont();
        FormattedText str = textRenderer.substrByWidth(text, width - textRenderer.width(CommonComponents.ELLIPSIS));
        return Language.getInstance().getVisualOrder(FormattedText.composite(str, CommonComponents.ELLIPSIS));
    }
}