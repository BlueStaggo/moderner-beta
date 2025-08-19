package mod.bluestaggo.modernerbeta.client.gui.optioncallbacks;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.client.gui.widget.AlignedTextWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public record TextLabelCallbacks(Text text, float alignment) implements SimpleOption.Callbacks<Void> {
    public TextLabelCallbacks(Text text) {
        this(text, 0.5F);
    }

    @Override
    public Function<SimpleOption<Void>, ClickableWidget> getWidgetCreator(SimpleOption.TooltipFactory<Void> tooltipFactory, GameOptions gameOptions, int x, int y, int width, Consumer<Void> changeCallback) {
        return option -> {
            AlignedTextWidget textWidget = new AlignedTextWidget(x, y, width, 20, text, MinecraftClient.getInstance().advanceValidatingTextRenderer);
            textWidget.align(alignment);
            return textWidget;
        };
    }

    @Override
    public Optional<Void> validate(Void value) {
        return Optional.empty();
    }

    @Override
    public Codec<Void> codec() {
        return null;
    }
}
