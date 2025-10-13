package mod.bluestaggo.modernerbeta.client.gui.optioncallbacks;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.OptionInstance;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public record FloatSliderCallbacks(float min, float max) implements OptionInstance.SliderableValueSet<Float> {
    @Override
    public @NotNull Optional<Float> validateValue(Float value) {
        return value >= this.min && value <= this.max ? Optional.of(value) : Optional.empty();
    }

    @Override
    public double toSliderValue(Float value) {
        return Mth.map(value, this.min, this.max, 0.0F, 1.0F);
    }

    public @NotNull Float fromSliderValue(double value) {
        return Mth.map((float)value, 0.0F, 1.0F, this.min, this.max);
    }

    public @NotNull Codec<Float> codec() {
        return Codec.FLOAT;
    }
}