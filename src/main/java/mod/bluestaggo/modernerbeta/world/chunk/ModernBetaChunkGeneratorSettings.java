package mod.bluestaggo.modernerbeta.world.chunk;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.mixin.AccessorDensityFunctions;
import mod.bluestaggo.modernerbeta.util.BlockStates;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry.Reference;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler.NoiseParameters;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.noise.NoiseRouter;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules;

import java.util.List;

public class ModernBetaChunkGeneratorSettings {
    private static boolean useModernBetaSurfaceRules;

    public static final RegistryKey<ChunkGeneratorSettings> BETA;
    public static final RegistryKey<ChunkGeneratorSettings> ALPHA;
    public static final RegistryKey<ChunkGeneratorSettings> SKYLANDS;
    public static final RegistryKey<ChunkGeneratorSettings> INFDEV_611;
    public static final RegistryKey<ChunkGeneratorSettings> INFDEV_420;
    public static final RegistryKey<ChunkGeneratorSettings> INFDEV_415;
    public static final RegistryKey<ChunkGeneratorSettings> INFDEV_227;
    public static final RegistryKey<ChunkGeneratorSettings> INDEV;
    public static final RegistryKey<ChunkGeneratorSettings> CLASSIC_0_30;
    public static final RegistryKey<ChunkGeneratorSettings> PE;
    public static final RegistryKey<ChunkGeneratorSettings> EARLY_RELEASE;
    public static final RegistryKey<ChunkGeneratorSettings> MAJOR_RELEASE;
    public static final RegistryKey<ChunkGeneratorSettings> EARLY_BEDROCK;

    public static void bootstrap(Registerable<ChunkGeneratorSettings> settingsRegisterable) {
        settingsRegisterable.register(BETA, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.BETA, 64, true));
        settingsRegisterable.register(ALPHA, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.ALPHA, 64, true));
        settingsRegisterable.register(SKYLANDS, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.SKYLANDS, 0, false));
        settingsRegisterable.register(INFDEV_611, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.INFDEV_611, 64, true));
        settingsRegisterable.register(INFDEV_420, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.INFDEV_420, 64, true));
        settingsRegisterable.register(INFDEV_415, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.INFDEV_415, 64, true));
        settingsRegisterable.register(INFDEV_227, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.INFDEV_227, 64, true));
        settingsRegisterable.register(INDEV, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.INDEV, 64, false));
        settingsRegisterable.register(CLASSIC_0_30, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.CLASSIC_0_30, 64, false));
        settingsRegisterable.register(PE, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.PE, 64, true));
        settingsRegisterable.register(EARLY_RELEASE, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.EARLY_RELEASE, 63, true));
        settingsRegisterable.register(MAJOR_RELEASE, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.MAJOR_RELEASE, 63, true));
        settingsRegisterable.register(EARLY_BEDROCK, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.EARLY_BEDROCK, 63, true));
    }
    
    private static NoiseRouter createDensityFunctions(
        RegistryEntryLookup<DensityFunction> densityFunctionLookup,
        RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> noiseParametersLookup
    ) {
        Reference<NoiseParameters> aquiferBarrier = noiseParametersLookup.getOrThrow(NoiseParametersKeys.AQUIFER_BARRIER);
        Reference<NoiseParameters> aquiferFloodedness = noiseParametersLookup.getOrThrow(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_FLOODEDNESS);
        Reference<NoiseParameters> aquiferSpread = noiseParametersLookup.getOrThrow(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_SPREAD);
        Reference<NoiseParameters> aquiferLava = noiseParametersLookup.getOrThrow(NoiseParametersKeys.AQUIFER_LAVA);
        Reference<NoiseParameters> caveEntranceNoise = noiseParametersLookup.getOrThrow(NoiseParametersKeys.CAVE_ENTRANCE);
        
        DensityFunction functionAquiferBarrier = DensityFunctionTypes.noise(aquiferBarrier, 0.5);
        DensityFunction functionAquiferFloodedness = DensityFunctionTypes.noise(aquiferFloodedness, 0.67);
        DensityFunction functionAquiferSpread = DensityFunctionTypes.noise(aquiferSpread, 0.7142857142857143);
        DensityFunction functionAquiferLava = DensityFunctionTypes.noise(aquiferLava);
        DensityFunction functionCaveEntranceNoise = DensityFunctionTypes.noise(caveEntranceNoise);

        DensityFunction functionSlopedCheeseEstimate = DensityFunctionTypes.add(
            DensityFunctionTypes.yClampedGradient(0, 64, 3.0, 1.0),
            DensityFunctionTypes.mul(
                DensityFunctionTypes.constant(-0.5),
                functionCaveEntranceNoise
            )
        );
        DensityFunction functionCaveEntrances = DensityFunctionTypes.min(
            functionSlopedCheeseEstimate,
            DensityFunctionTypes.mul(DensityFunctionTypes.constant(5.0),
                new DensityFunctionTypes.RegistryEntryHolder(densityFunctionLookup.getOrThrow(AccessorDensityFunctions.getCavesEntrancesOverworldKey())))
        );
        DensityFunction functionCaves = DensityFunctionTypes.rangeChoice(
            functionSlopedCheeseEstimate, -1000000.0, 1.5625, functionCaveEntrances,
            AccessorDensityFunctions.invokeCreateCavesFunction(densityFunctionLookup, noiseParametersLookup, functionSlopedCheeseEstimate)
        );
        DensityFunction functionCavesWithNoodles = DensityFunctionTypes.min(
            AccessorDensityFunctions.invokeApplyBlendDensity(AccessorDensityFunctions.invokeApplySurfaceSlides(false, functionCaves)),
            new DensityFunctionTypes.RegistryEntryHolder(densityFunctionLookup.getOrThrow(AccessorDensityFunctions.getCavesNoodleOverworldKey()))
        );
        
        return new NoiseRouter(
            functionAquiferBarrier,      // Barrier noise
            functionAquiferFloodedness,  // Fluid level floodedness noise
            functionAquiferSpread,       // Fluid level spread noise
            functionAquiferLava,         // Lava noise
            DensityFunctionTypes.zero(), // Temperature
            DensityFunctionTypes.zero(), // Vegetation
            DensityFunctionTypes.zero(), // Continents
            DensityFunctionTypes.zero(), // Erosion
            DensityFunctionTypes.zero(), // Depth
            DensityFunctionTypes.zero(), // Ridges
            DensityFunctionTypes.zero(), // Initial density
            functionCavesWithNoodles,    // Final density (used for noise caves post-processor)
            DensityFunctionTypes.zero(), // Vein Toggle
            DensityFunctionTypes.zero(), // Vein Ridged
            DensityFunctionTypes.zero()  // Vein Gap
        );
    }

    private static ChunkGeneratorSettings createGeneratorSettings(
        Registerable<ChunkGeneratorSettings> settingsRegisterable,
        GenerationShapeConfig shapeConfig,
        int seaLevel,
        boolean useAquifers
    ) {
        RegistryEntryLookup<DensityFunction> densityFunctionLookup = settingsRegisterable.getRegistryLookup(RegistryKeys.DENSITY_FUNCTION);
        RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> noiseParametersLookup = settingsRegisterable.getRegistryLookup(RegistryKeys.NOISE_PARAMETERS);

        return createGeneratorSettings(densityFunctionLookup, noiseParametersLookup, shapeConfig, seaLevel, useAquifers);
    }

    public static ChunkGeneratorSettings createGeneratorSettings(
        RegistryWrapper.WrapperLookup lookup,
        GenerationShapeConfig shapeConfig,
        int seaLevel,
        boolean useAquifers
    ) {
        RegistryEntryLookup<DensityFunction> densityFunctionLookup = VersionCompat.getRegistryWrapper(lookup, RegistryKeys.DENSITY_FUNCTION);
        RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> noiseParametersLookup = VersionCompat.getRegistryWrapper(lookup, RegistryKeys.NOISE_PARAMETERS);

        return createGeneratorSettings(densityFunctionLookup, noiseParametersLookup, shapeConfig, seaLevel, useAquifers);
    }
    
    private static ChunkGeneratorSettings createGeneratorSettings(
        RegistryEntryLookup<DensityFunction> densityFunctionLookup,
        RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> noiseParametersLookup,
        GenerationShapeConfig shapeConfig,
        int seaLevel,
        boolean useAquifers
    ) {
        useModernBetaSurfaceRules = true;
        MaterialRules.MaterialRule materialRule = VanillaSurfaceRules.createOverworldSurfaceRule();
        useModernBetaSurfaceRules = false;

        return new ChunkGeneratorSettings(
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
        BETA = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBetaBuiltInTypes.Chunk.BETA.id);
        ALPHA = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBetaBuiltInTypes.Chunk.ALPHA.id);
        SKYLANDS = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBetaBuiltInTypes.Chunk.SKYLANDS.id);
        INFDEV_611 = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBetaBuiltInTypes.Chunk.INFDEV_611.id);
        INFDEV_420 = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBetaBuiltInTypes.Chunk.INFDEV_420.id);
        INFDEV_415 = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBetaBuiltInTypes.Chunk.INFDEV_415.id);
        INFDEV_227 = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBetaBuiltInTypes.Chunk.INFDEV_227.id);
        INDEV = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBetaBuiltInTypes.Chunk.INDEV.id);
        CLASSIC_0_30 = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBetaBuiltInTypes.Chunk.CLASSIC_0_30.id);
        PE = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBetaBuiltInTypes.Chunk.PE.id);
        EARLY_RELEASE = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id);
        MAJOR_RELEASE = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBetaBuiltInTypes.Chunk.MAJOR_RELEASE.id);
        EARLY_BEDROCK = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBetaBuiltInTypes.Chunk.EARLY_BEDROCK.id);
    }
}
