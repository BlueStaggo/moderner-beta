package mod.bluestaggo.modernerbeta.mixin;

import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NoiseRouterData.class)
public interface AccessorDensityFunctions {
    @Accessor("ENTRANCES")
    static ResourceKey<DensityFunction> getEntrancesKey() {
        throw new AssertionError();
    }

    @Accessor("NOODLE")
    static ResourceKey<DensityFunction> getNoodleKey() {
        throw new AssertionError();
    }

    @Invoker
    static DensityFunction invokeUnderground(
        HolderGetter<DensityFunction> densityFunctionLookup,
        HolderGetter<NormalNoise.NoiseParameters> noiseParametersLookup,
        DensityFunction slopedCheese
    ) {
        throw new AssertionError();
    }

    @Invoker
    static DensityFunction invokePostProcess(
        DensityFunction density
    ) {
        throw new AssertionError();
    }

    @Invoker
    static DensityFunction invokeSlideOverworld(
        boolean amplified,
        DensityFunction density
    ) {
        throw new AssertionError();
    }
}
