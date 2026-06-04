package mod.bluestaggo.modernerbeta.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Supplier;

@Mixin(NoiseBasedChunkGenerator.class)
public interface NoiseBasedChunkGeneratorAccessor {
    @Mutable
    @Accessor("settings")
    void setSettings(Holder<NoiseGeneratorSettings> settings);

    @Mutable
    @Accessor("globalFluidPicker")
    void setGlobalFluidPicker(Supplier<Aquifer.FluidPicker> supplier);

    @Invoker("createFluidPicker")
    static Aquifer.FluidPicker invokeCreateFluidPicker(NoiseGeneratorSettings settings) {
        throw new AssertionError();
    }
}
