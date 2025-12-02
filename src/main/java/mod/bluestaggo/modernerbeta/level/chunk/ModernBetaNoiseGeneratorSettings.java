package mod.bluestaggo.modernerbeta.level.chunk;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.mixin.NoiseRouterDataAccessor;
import mod.bluestaggo.modernerbeta.util.BlockStates;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.synth.NormalNoise.NoiseParameters;

import java.util.List;

public class ModernBetaNoiseGeneratorSettings {
    private static boolean useModernBetaSurfaceRules;

    public static final ResourceKey<NoiseGeneratorSettings> INFDEV_227;
    public static final ResourceKey<NoiseGeneratorSettings> FINITE_2D;
    public static final ResourceKey<NoiseGeneratorSettings> NOISE_3D;

    public static void bootstrap(BootstrapContext<NoiseGeneratorSettings> context) {
        context.register(INFDEV_227, createNoiseGeneratorSettings(context, ModernBetaNoiseSettings.OVERWORLD_128, 64, true));
        context.register(FINITE_2D, createNoiseGeneratorSettings(context, ModernBetaNoiseSettings.FINITE_2D, 64, false));
        context.register(NOISE_3D, createNoiseGeneratorSettings(context, ModernBetaNoiseSettings.OVERWORLD_FULL, 64, true));
    }
    
    private static NoiseRouter createDensityFunctions(
        HolderGetter<DensityFunction> densityFunctionLookup,
        HolderGetter<NormalNoise.NoiseParameters> noiseParametersLookup
    ) {
        Reference<NoiseParameters> aquiferBarrier = noiseParametersLookup.getOrThrow(Noises.AQUIFER_BARRIER);
        Reference<NoiseParameters> aquiferFloodedness = noiseParametersLookup.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS);
        Reference<NoiseParameters> aquiferSpread = noiseParametersLookup.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_SPREAD);
        Reference<NoiseParameters> aquiferLava = noiseParametersLookup.getOrThrow(Noises.AQUIFER_LAVA);
        Reference<NoiseParameters> caveEntranceNoise = noiseParametersLookup.getOrThrow(Noises.CAVE_ENTRANCE);
        
        DensityFunction functionAquiferBarrier = DensityFunctions.noise(aquiferBarrier, 0.5);
        DensityFunction functionAquiferFloodedness = DensityFunctions.noise(aquiferFloodedness, 0.67);
        DensityFunction functionAquiferSpread = DensityFunctions.noise(aquiferSpread, 0.7142857142857143);
        DensityFunction functionAquiferLava = DensityFunctions.noise(aquiferLava);
        DensityFunction functionCaveEntranceNoise = DensityFunctions.noise(caveEntranceNoise);

        DensityFunction functionSlopedCheeseEstimate = DensityFunctions.add(
            DensityFunctions.yClampedGradient(0, 64, 3.0, 1.0),
            DensityFunctions.mul(
                DensityFunctions.constant(-0.5),
                functionCaveEntranceNoise
            )
        );
        DensityFunction functionCaveEntrances = DensityFunctions.min(
            functionSlopedCheeseEstimate,
            DensityFunctions.mul(DensityFunctions.constant(5.0),
                new DensityFunctions.HolderHolder(densityFunctionLookup.getOrThrow(NoiseRouterDataAccessor.getEntrancesKey())))
        );
        DensityFunction functionCaves = DensityFunctions.rangeChoice(
            functionSlopedCheeseEstimate, -1000000.0, 1.5625, functionCaveEntrances,
            NoiseRouterDataAccessor.invokeUnderground(densityFunctionLookup, noiseParametersLookup, functionSlopedCheeseEstimate)
        );
        DensityFunction functionCavesWithNoodles = DensityFunctions.min(
            NoiseRouterDataAccessor.invokePostProcess(NoiseRouterDataAccessor.invokeSlideOverworld(false, functionCaves)),
            new DensityFunctions.HolderHolder(densityFunctionLookup.getOrThrow(NoiseRouterDataAccessor.getNoodleKey()))
        );
        
        return new NoiseRouter(
            functionAquiferBarrier,      // Barrier noise
            functionAquiferFloodedness,  // Fluid level floodedness noise
            functionAquiferSpread,       // Fluid level spread noise
            functionAquiferLava,         // Lava noise
            DensityFunctions.zero(),     // Temperature
            DensityFunctions.zero(),     // Vegetation
            DensityFunctions.zero(),     // Continents
            DensityFunctions.zero(),     // Erosion
            DensityFunctions.zero(),     // Depth
            DensityFunctions.zero(),     // Ridges
            DensityFunctions.zero(),     // Initial density
            functionCavesWithNoodles,    // Final density (used for noise caves post-processor)
            DensityFunctions.zero(),     // Vein Toggle
            DensityFunctions.zero(),     // Vein Ridged
            DensityFunctions.zero()      // Vein Gap
        );
    }

    private static NoiseGeneratorSettings createNoiseGeneratorSettings(
        BootstrapContext<NoiseGeneratorSettings> context,
        NoiseSettings shapeConfig,
        int seaLevel,
        boolean useAquifers
    ) {
        HolderGetter<DensityFunction> densityFunctionLookup = context.lookup(Registries.DENSITY_FUNCTION);
        HolderGetter<NormalNoise.NoiseParameters> noiseParametersLookup = context.lookup(Registries.NOISE);

        return createNoiseGeneratorSettings(densityFunctionLookup, noiseParametersLookup, shapeConfig, seaLevel, useAquifers);
    }

    public static NoiseGeneratorSettings createNoiseGeneratorSettings(
        HolderLookup.Provider lookup,
        NoiseSettings shapeConfig,
        int seaLevel,
        boolean useAquifers
    ) {
        HolderGetter<DensityFunction> densityFunctionLookup = lookup.lookupOrThrow(Registries.DENSITY_FUNCTION);
        HolderGetter<NormalNoise.NoiseParameters> noiseParametersLookup = lookup.lookupOrThrow(Registries.NOISE);

        return createNoiseGeneratorSettings(densityFunctionLookup, noiseParametersLookup, shapeConfig, seaLevel, useAquifers);
    }
    
    private static NoiseGeneratorSettings createNoiseGeneratorSettings(
        HolderGetter<DensityFunction> densityFunctionLookup,
        HolderGetter<NormalNoise.NoiseParameters> noiseParametersLookup,
        NoiseSettings shapeConfig,
        int seaLevel,
        boolean useAquifers
    ) {
        useModernBetaSurfaceRules = true;
        SurfaceRules.RuleSource materialRule = SurfaceRuleData.overworld();
        useModernBetaSurfaceRules = false;

        return new NoiseGeneratorSettings(
            shapeConfig,
            BlockStates.STONE,
            BlockStates.WATER,
            createDensityFunctions(densityFunctionLookup, noiseParametersLookup),
            materialRule,
            List.of(),
            seaLevel,
            false,
            useAquifers,
            false,
            true
        );
    }

    public static boolean useModernBetaSurfaceRules() {
        return useModernBetaSurfaceRules;
    }

    static {
        INFDEV_227 = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBetaBuiltInTypes.Chunk.INFDEV_227.id);
        FINITE_2D = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBetaBuiltInTypes.Chunk.FINITE_2D.id);
        NOISE_3D = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBetaBuiltInTypes.Chunk.NOISE_3D.id);
    }
}
