package mod.bluestaggo.modernerbeta.client.gui.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AbstractTextWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Language;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class AlignedTextWidget extends AbstractTextWidget {
    private float horizontalAlignment = 0.5F;

    public AlignedTextWidget(Text message, TextRenderer textRenderer) {
        this(0, 0, textRenderer.getWidth(message.asOrderedText()), 9, message, textRenderer);
    }

    public AlignedTextWidget(int width, int height, Text message, TextRenderer textRenderer) {
        this(0, 0, width, height, message, textRenderer);
    }

    public AlignedTextWidget(int x, int y, int width, int height, Text message, TextRenderer textRenderer) {
        super(x, y, width, height, message, textRenderer);
        this.active = false;
    }

    public AlignedTextWidget setTextColor(int textColor) {
        super.setTextColor(textColor);
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
    public void
    //? if >=1.20.3 {
    renderWidget
    //?} else {
    /*renderButton
     *///?}
        (DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        Text message = this.getMessage();
        TextRenderer textRenderer = this.getTextRenderer();

        int width = this.getWidth();
        int textWidth = textRenderer.getWidth(message);

        int x = this.getX() + Math.round(this.horizontalAlignment * (width - textWidth));
        int y = this.getY() + (this.getHeight() - 9) / 2;
        OrderedText orderedText = textWidth > width ? this.trim(message, width) : message.asOrderedText();
        context.drawTextWithShadow(textRenderer, orderedText, x, y, this.getTextColor());
    }

    private OrderedText trim(Text text, int width) {
        TextRenderer textRenderer = this.getTextRenderer();
        StringVisitable str = textRenderer.trimToWidth(text, width - textRenderer.getWidth(ScreenTexts.ELLIPSIS));
        return Language.getInstance().reorder(StringVisitable.concat(str, ScreenTexts.ELLIPSIS));
    }
}