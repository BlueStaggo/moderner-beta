package mod.bluestaggo.modernerbeta.fabric.mixin;

import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NoiseRouterData.class)
public interface NoiseRouterDataAccessor {
    @Invoker
    static NoiseRouter invokeOverworld(HolderGetter<DensityFunction> densityFunctionLookup, HolderGetter<NormalNoise.NoiseParameters> noiseParametersLookup, boolean largeBiomes, boolean amplified) {
        throw new AssertionError();
    }

    @Invoker
    static NoiseRouter invokeNether(HolderGetter<DensityFunction> densityFunctionLookup, HolderGetter<NormalNoise.NoiseParameters> noiseParametersLookup) {
        throw new AssertionError();
    }

    @Accessor("Y")
    static ResourceKey<DensityFunction> getY() {
        throw new AssertionError();
    }

    @Accessor("SPAGHETTI_ROUGHNESS_FUNCTION")
    static ResourceKey<DensityFunction> getSpaghettiRoughnessFunction() {
        throw new AssertionError();
    }

    @Accessor("SPAGHETTI_2D_THICKNESS_MODULATOR")
    static ResourceKey<DensityFunction> getSpaghetti2dThicknessModulator() {
        throw new AssertionError();
    }

    @Accessor("SPAGHETTI_2D")
    static ResourceKey<DensityFunction> getSpaghetti2d() {
        throw new AssertionError();
    }
}
