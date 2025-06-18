package mod.bluestaggo.modernerbeta.client.gui.optioncallbacks;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

@Environment(EnvType.CLIENT)
public record IntegerFieldCallbacks(String prefix, IntFunction<String> serializer, ToIntFunction<String> deserializer) implements SimpleOption.Callbacks<Integer> {
    @Override
    public Function<SimpleOption<Integer>, ClickableWidget> getWidgetCreator(SimpleOption.TooltipFactory<Integer> tooltipFactory, GameOptions gameOptions, int x, int y, int width, Consumer<Integer> changeCallback) {
        return option -> {
            var widget = new TextFieldWidget(
                MinecraftClient.getInstance().advanceValidatingTextRenderer,
                x, y, width, 20, Text.of(option.toString())
            );
            widget.setMaxLength(256);
            widget.setChangedListener(value -> {
                if (value.length() <= this.prefix.length()
                        || value.length() == this.prefix.length() + 1 && value.charAt(this.prefix.length()) == '-') {
                    option.setValue(0);
                } else {
                    try {
                        option.setValue(this.deserializer.applyAsInt(value.substring(this.prefix.length())));
                    } catch (NumberFormatException ignored) {
                    }
                }
            });
            widget.setTextPredicate(string -> {
               if (!string.startsWith(this.prefix)) {
                   return false;
               }

               try {
                   this.deserializer.applyAsInt(string.substring(this.prefix.length()));
                   return true;
               } catch (NumberFormatException exception) {
                   return false;
               }
            });
            widget.setText(this.prefix + this.serializer.apply(option.getValue()));
            return widget;
        };
    }

    @Override
    public Optional<Integer> validate(Integer value) {
        return Optional.of(value);
    }

    @Override
    public Codec<Integer> codec() {
        return Codec.INT;
    }
}
