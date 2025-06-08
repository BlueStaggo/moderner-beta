package mod.bluestaggo.modernerbeta.mixin;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DensityFunctions.class)
public interface AccessorDensityFunctions {
    @Accessor("CAVES_ENTRANCES_OVERWORLD")
    static RegistryKey<DensityFunction> getCavesEntrancesOverworldKey() {
        throw new AssertionError();
    }

    @Accessor("CAVES_NOODLE_OVERWORLD")
    static RegistryKey<DensityFunction> getCavesNoodleOverworldKey() {
        throw new AssertionError();
    }

    @Invoker
    static DensityFunction invokeCreateCavesFunction(
        RegistryEntryLookup<DensityFunction> densityFunctionLookup,
        RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> noiseParametersLookup,
        DensityFunction slopedCheese
    ) {
        throw new AssertionError();
    }

    @Invoker
    static DensityFunction invokeApplyBlendDensity(
        DensityFunction density
    ) {
        throw new AssertionError();
    }

    @Invoker
    static DensityFunction invokeApplySurfaceSlides(
        boolean amplified,
        DensityFunction density
    ) {
        throw new AssertionError();
    }
}
