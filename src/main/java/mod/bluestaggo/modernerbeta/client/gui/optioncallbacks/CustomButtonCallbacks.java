package mod.bluestaggo.modernerbeta.client.gui.optioncallbacks;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public record CustomButtonCallbacks(Component text, Runnable onPress) implements OptionInstance.ValueSet<Void> {
    @Override
    public @NotNull Function<OptionInstance<Void>, AbstractWidget> createButton(OptionInstance.TooltipSupplier<Void> tooltipFactory, Options gameOptions, int x, int y, int width, Consumer<Void> changeCallback) {
        return option ->
            Button.builder(text, onPress -> this.onPress.run())
            .bounds(x, y, width, 20).build();
    }

    @Override
    public @NotNull Optional<Void> validateValue(Void value) {
        return Optional.empty();
    }

    @Override
    public Codec<Void> codec() {
        return null;
    }
}
