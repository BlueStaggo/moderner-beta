package mod.bluestaggo.modernerbeta.util.noise;

import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

import java.util.Arrays;

public enum SimpleDensityFunction implements DensityFunctions.BeardifierOrMarker {
    INSTANCE;

    @Override
    public double compute(DensityFunction.FunctionContext pos) {
        return 0.0;
    }

    @Override
    public void fillArray(double[] densities, DensityFunction.ContextProvider applier) {
        Arrays.fill(densities, 0.0);
    }

    //? if <26.3 {
    @Override
    public double maxValue() {
        return 0;
    }

    @Override
    public double minValue() {
        return 0;
    }
    //? }
}