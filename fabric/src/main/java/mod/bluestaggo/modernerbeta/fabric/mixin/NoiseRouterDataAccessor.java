package mod.bluestaggo.modernerbeta.fabric.mixin;

import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.*;
//? if >=26.3
//import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NoiseRouterData.class)
public interface NoiseRouterDataAccessor {
    @Invoker
    static NoiseRouter invokeOverworld(
        HolderGetter<DensityFunction> densityFunctionLookup,
        //? if <26.3
        HolderGetter<NormalNoise.NoiseParameters> noiseParametersLookup,
        //? if >=26.3 {
        /*OverworldFunctionSet<ResourceKey<DensityFunction>> functionNames
        *///? } else {
        boolean largeBiomes,
        boolean amplified
        //? }
    ) {
        throw new AssertionError();
    }

    //? if >=26.3 {
    /*@Invoker
    static Aquifer.Config invokeOverworldAquifers(
        HolderGetter<DensityFunction> functions,
        HolderGetter<NormalNoise.NoiseParameters> noises,
        OverworldFunctionSet<ResourceKey<DensityFunction>> names
    ) {
        throw new AssertionError();
    }
    *///? }

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
