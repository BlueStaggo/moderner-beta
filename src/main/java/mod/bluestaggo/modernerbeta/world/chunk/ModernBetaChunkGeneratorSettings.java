package mod.bluestaggo.modernerbeta.world.chunk;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.mixin.AccessorDensityFunctions;
import mod.bluestaggo.modernerbeta.util.BlockStates;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
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

public class ModernBetaChunkGeneratorSettings {
    private static boolean useModernBetaSurfaceRules;

    public static final ResourceKey<NoiseGeneratorSettings> BETA;
    public static final ResourceKey<NoiseGeneratorSettings> ALPHA;
    public static final ResourceKey<NoiseGeneratorSettings> SKYLANDS;
    public static final ResourceKey<NoiseGeneratorSettings> INFDEV_611;
    public static final ResourceKey<NoiseGeneratorSettings> INFDEV_420;
    public static final ResourceKey<NoiseGeneratorSettings> INFDEV_415;
    public static final ResourceKey<NoiseGeneratorSettings> INFDEV_227;
    public static final ResourceKey<NoiseGeneratorSettings> INDEV;
    public static final ResourceKey<NoiseGeneratorSettings> CLASSIC_0_30;
    public static final ResourceKey<NoiseGeneratorSettings> PE;
    public static final ResourceKey<NoiseGeneratorSettings> EARLY_RELEASE;
    public static final ResourceKey<NoiseGeneratorSettings> MAJOR_RELEASE;
    public static final ResourceKey<NoiseGeneratorSettings> EARLY_BEDROCK;

    public static void bootstrap(BootstrapContext<NoiseGeneratorSettings> context) {
        context.register(BETA, createGeneratorSettings(context, ModernBetaShapeConfigs.BETA, 64, true));
        context.register(ALPHA, createGeneratorSettings(context, ModernBetaShapeConfigs.ALPHA, 64, true));
        context.register(SKYLANDS, createGeneratorSettings(context, ModernBetaShapeConfigs.SKYLANDS, 0, false));
        context.register(INFDEV_611, createGeneratorSettings(context, ModernBetaShapeConfigs.INFDEV_611, 64, true));
        context.register(INFDEV_420, createGeneratorSettings(context, ModernBetaShapeConfigs.INFDEV_420, 64, true));
        context.register(INFDEV_415, createGeneratorSettings(context, ModernBetaShapeConfigs.INFDEV_415, 64, true));
        context.register(INFDEV_227, createGeneratorSettings(context, ModernBetaShapeConfigs.INFDEV_227, 64, true));
        context.register(INDEV, createGeneratorSettings(context, ModernBetaShapeConfigs.INDEV, 64, false));
        context.register(CLASSIC_0_30, createGeneratorSettings(context, ModernBetaShapeConfigs.CLASSIC_0_30, 64, false));
        context.register(PE, createGeneratorSettings(context, ModernBetaShapeConfigs.PE, 64, true));
        context.register(EARLY_RELEASE, createGeneratorSettings(context, ModernBetaShapeConfigs.EARLY_RELEASE, 63, true));
        context.register(MAJOR_RELEASE, createGeneratorSettings(context, ModernBetaShapeConfigs.MAJOR_RELEASE, 63, true));
        context.register(EARLY_BEDROCK, createGeneratorSettings(context, ModernBetaShapeConfigs.EARLY_BEDROCK, 63, true));
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
                new DensityFunctions.HolderHolder(densityFunctionLookup.getOrThrow(AccessorDensityFunctions.getEntrancesKey())))
        );
        DensityFunction functionCaves = DensityFunctions.rangeChoice(
            functionSlopedCheeseEstimate, -1000000.0, 1.5625, functionCaveEntrances,
            AccessorDensityFunctions.invokeUnderground(densityFunctionLookup, noiseParametersLookup, functionSlopedCheeseEstimate)
        );
        DensityFunction functionCavesWithNoodles = DensityFunctions.min(
            AccessorDensityFunctions.invokePostProcess(AccessorDensityFunctions.invokeSlideOverworld(false, functionCaves)),
            new DensityFunctions.HolderHolder(densityFunctionLookup.getOrThrow(AccessorDensityFunctions.getNoodleKey()))
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

    private static NoiseGeneratorSettings createGeneratorSettings(
        BootstrapContext<NoiseGeneratorSettings> context,
        NoiseSettings shapeConfig,
        int seaLevel,
        boolean useAquifers
    ) {
        HolderGetter<DensityFunction> densityFunctionLookup = context.lookup(Registries.DENSITY_FUNCTION);
        HolderGetter<NormalNoise.NoiseParameters> noiseParametersLookup = context.lookup(Registries.NOISE);

        return createGeneratorSettings(densityFunctionLookup, noiseParametersLookup, shapeConfig, seaLevel, useAquifers);
    }

    public static NoiseGeneratorSettings createGeneratorSettings(
        HolderLookup.Provider lookup,
        NoiseSettings shapeConfig,
        int seaLevel,
        boolean useAquifers
    ) {
        HolderGetter<DensityFunction> densityFunctionLookup = lookup.lookupOrThrow(Registries.DENSITY_FUNCTION);
        HolderGetter<NormalNoise.NoiseParameters> noiseParametersLookup = lookup.lookupOrThrow(Registries.NOISE);

        return createGeneratorSettings(densityFunctionLookup, noiseParametersLookup, shapeConfig, seaLevel, useAquifers);
    }
    
    private static NoiseGeneratorSettings createGeneratorSettings(
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
        BETA = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBetaBuiltInTypes.Chunk.BETA.id);
        ALPHA = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBetaBuiltInTypes.Chunk.ALPHA.id);
        SKYLANDS = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBetaBuiltInTypes.Chunk.SKYLANDS.id);
        INFDEV_611 = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBetaBuiltInTypes.Chunk.INFDEV_611.id);
        INFDEV_420 = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBetaBuiltInTypes.Chunk.INFDEV_420.id);
        INFDEV_415 = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBetaBuiltInTypes.Chunk.INFDEV_415.id);
        INFDEV_227 = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBetaBuiltInTypes.Chunk.INFDEV_227.id);
        INDEV = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBetaBuiltInTypes.Chunk.INDEV.id);
        CLASSIC_0_30 = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBetaBuiltInTypes.Chunk.CLASSIC_0_30.id);
        PE = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBetaBuiltInTypes.Chunk.PE.id);
        EARLY_RELEASE = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id);
        MAJOR_RELEASE = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBetaBuiltInTypes.Chunk.MAJOR_RELEASE.id);
        EARLY_BEDROCK = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBetaBuiltInTypes.Chunk.EARLY_BEDROCK.id);
    }
}
