package mod.bluestaggo.modernerbeta.client.gui.optioncallbacks;

import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public record IntegerFieldCallbacks(String prefix, IntFunction<String> serializer, ToIntFunction<String> deserializer) implements OptionInstance.ValueSet<Integer> {
    @Override
    public @NotNull Function<OptionInstance<Integer>, AbstractWidget> createButton(OptionInstance.TooltipSupplier<Integer> tooltipFactory, Options gameOptions, int x, int y, int width, Consumer<Integer> changeCallback) {
        return option -> {
            var widget = new EditBox(
                Minecraft.getInstance().fontFilterFishy,
                x, y, width, 20, Component.nullToEmpty(option.toString())
            );
            widget.setMaxLength(256);
            widget.setResponder(value -> {
                if (value.length() <= this.prefix.length()
                        || value.length() == this.prefix.length() + 1 && value.charAt(this.prefix.length()) == '-') {
                    option.set(0);
                } else {
                    try {
                        option.set(this.deserializer.applyAsInt(value.substring(this.prefix.length())));
                    } catch (NumberFormatException ignored) {
                    }
                }
            });
            widget.setFilter(string -> {
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
            widget.setValue(this.prefix + this.serializer.apply(option.get()));
            return widget;
        };
    }

    @Override
    public @NotNull Optional<Integer> validateValue(Integer value) {
        return Optional.of(value);
    }

    @Override
    public @NotNull Codec<Integer> codec() {
        return Codec.INT;
    }
}
