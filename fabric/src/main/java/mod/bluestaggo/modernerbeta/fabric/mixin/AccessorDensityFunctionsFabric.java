package mod.bluestaggo.modernerbeta.fabric.mixin;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctions;
import net.minecraft.world.gen.noise.NoiseRouter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DensityFunctions.class)
public interface AccessorDensityFunctionsFabric {
    @Invoker
    static NoiseRouter invokeCreateSurfaceNoiseRouter(RegistryEntryLookup<DensityFunction> densityFunctionLookup, RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> noiseParametersLookup, boolean largeBiomes, boolean amplified) {
        throw new AssertionError();
    }

    @Invoker
    static NoiseRouter invokeCreateNetherNoiseRouter(RegistryEntryLookup<DensityFunction> densityFunctionLookup, RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> noiseParametersLookup) {
        throw new AssertionError();
    }

    @Accessor("Y")
    static RegistryKey<DensityFunction> getY() {
        throw new AssertionError();
    }

    @Accessor("CAVES_SPAGHETTI_ROUGHNESS_FUNCTION_OVERWORLD")
    static RegistryKey<DensityFunction> getCavesSpaghettiRoughnessFunction() {
        throw new AssertionError();
    }

    @Accessor("CAVES_SPAGHETTI_2D_THICKNESS_MODULATOR_OVERWORLD")
    static RegistryKey<DensityFunction> getCavesSpaghetti2dThicknessModulator() {
        throw new AssertionError();
    }

    @Accessor("CAVES_SPAGHETTI_2D_OVERWORLD")
    static RegistryKey<DensityFunction> getCavesSpaghetti2d() {
        throw new AssertionError();
    }
}
