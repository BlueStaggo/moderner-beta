package mod.bluestaggo.modernerbeta.client.gui.optioncallbacks;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.client.gui.widget.AlignedStringWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public record TextLabelCallbacks(Component text, float alignment) implements OptionInstance.ValueSet<Void> {
    public TextLabelCallbacks(Component text) {
        this(text, 0.5F);
    }

    @Override
    public @NotNull Function<OptionInstance<Void>, AbstractWidget> createButton(OptionInstance.TooltipSupplier<Void> tooltipFactory, Options gameOptions, int x, int y, int width, Consumer<Void> changeCallback) {
        return option -> {
            AlignedStringWidget textWidget = new AlignedStringWidget(x, y, width, 20, text, Minecraft.getInstance().fontFilterFishy);
            textWidget.align(alignment);
            return textWidget;
        };
    }

    @Override
    public @NotNull Optional<Void> validateValue(Void value) {
        return Optional.empty();
    }

    @Override
    public @NotNull Codec<Void> codec() {
        return null;
    }
}
