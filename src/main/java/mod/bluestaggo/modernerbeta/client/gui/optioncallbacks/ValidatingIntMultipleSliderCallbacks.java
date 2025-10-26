package mod.bluestaggo.modernerbeta.client.gui.optioncallbacks;

import com.mojang.serialization.Codec;
import net.minecraft.client.OptionInstance;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record ValidatingIntMultipleSliderCallbacks(int minInclusive, int maxInclusive, int multiple) implements OptionInstance.IntRangeBase {
    @Override
    public @NotNull Optional<Integer> validateValue(Integer integer) {
        return integer.compareTo(this.minInclusive()) >= 0 && integer.compareTo(this.maxInclusive()) <= 0 ? Optional.of(integer) : Optional.empty();
    }

    public @NotNull Codec<Integer> codec() {
        return Codec.intRange(this.minInclusive, this.maxInclusive + 1);
    }

    @Override
    public @NotNull Integer fromSliderValue(double d) {
        return (int) Math.round(Mth.map(d, 0.0, 1.0, (double)this.minInclusive(), (double)this.maxInclusive()) / multiple) * multiple;
    }
}