package mod.bluestaggo.modernerbeta.client.gui.optioncallbacks;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public record TextFieldCallbacks(Predicate<String> validationFunction, Predicate<String> inputValidationFunction) implements OptionInstance.ValueSet<String> {
    public static final TextFieldCallbacks NO_VALIDATION = new TextFieldCallbacks(Objects::nonNull, Objects::nonNull);

    public TextFieldCallbacks(Predicate<String> validationFunction) {
        this(validationFunction, Objects::nonNull);
    }

    @Override
    public @NotNull Function<OptionInstance<String>, AbstractWidget> createButton(OptionInstance.TooltipSupplier<String> tooltipFactory, Options gameOptions, int x, int y, int width, Consumer<String> changeCallback) {
        return option -> {
            var widget = new EditBox(
                Minecraft.getInstance().fontFilterFishy,
                x, y, width, 20, Component.nullToEmpty(option.toString())
            );
            widget.setMaxLength(256);
            widget.setResponder(option::set);
            widget.setFilter(inputValidationFunction);
            widget.setValue(option.get());
            return widget;
        };
    }

    @Override
    public @NotNull Optional<String> validateValue(String value) {
        return validationFunction.test(value) ? Optional.of(value) : Optional.empty();
    }

    @Override
    public @NotNull Codec<String> codec() {
        return Codec.STRING;
    }
}
