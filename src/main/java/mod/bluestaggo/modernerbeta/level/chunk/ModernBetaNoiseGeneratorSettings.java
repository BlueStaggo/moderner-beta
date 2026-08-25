package mod.bluestaggo.modernerbeta.level.chunk;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.mixin.NoiseRouterDataAccessor;
import mod.bluestaggo.modernerbeta.settings.component.NoiseSettings;
import mod.bluestaggo.modernerbeta.util.BlockStates;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.*;
//? if >=26.3 {
/*import net.minecraft.world.level.levelgen.densityfunction.*;
import net.minecraft.world.level.levelgen.material.rule.SurfaceRules.RuleSource;
*///? }
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;
//? if >=26.3
//import java.util.Optional;

public class ModernBetaNoiseGeneratorSettings {
    public static final ResourceKey<NoiseGeneratorSettings> INFDEV_415;
    public static final ResourceKey<NoiseGeneratorSettings> FINITE_2D;
    public static final ResourceKey<NoiseGeneratorSettings> SKY_128;
    public static final ResourceKey<NoiseGeneratorSettings> OVERWORLD_128;
    public static final ResourceKey<NoiseGeneratorSettings> OVERWORLD_256;

    public static void bootstrap(BootstrapContext<NoiseGeneratorSettings> context) {
        context.register(FINITE_2D, createNoiseGeneratorSettings(context, ModernBetaNoiseSettings.FINITE_2D, 64, false));
        context.register(INFDEV_415, createNoiseGeneratorSettings(context, ModernBetaNoiseSettings.INFDEV_415, 64, true));
        context.register(SKY_128, createNoiseGeneratorSettings(context, ModernBetaNoiseSettings.SKY_128, 0, false));
        context.register(OVERWORLD_128, createNoiseGeneratorSettings(context, ModernBetaNoiseSettings.OVERWORLD_128, 64, true));
        context.register(OVERWORLD_256, createNoiseGeneratorSettings(context, ModernBetaNoiseSettings.OVERWORLD_256, 64, true));
    }
    
    private static NoiseRouter createDensityFunctions(
        NoiseSettings noiseSettings,
        HolderGetter<DensityFunction> densityFunctionLookup,
        HolderGetter<NormalNoise.NoiseParameters> noiseParametersLookup
    ) {
        //? if <26.3 {
        Reference<NormalNoise.NoiseParameters> aquiferBarrier = noiseParametersLookup.getOrThrow(Noises.AQUIFER_BARRIER);
        Reference<NormalNoise.NoiseParameters> aquiferFloodedness = noiseParametersLookup.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS);
        Reference<NormalNoise.NoiseParameters> aquiferSpread = noiseParametersLookup.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_SPREAD);
        Reference<NormalNoise.NoiseParameters> aquiferLava = noiseParametersLookup.getOrThrow(Noises.AQUIFER_LAVA);
        //? }
        Reference<NormalNoise.NoiseParameters> caveEntranceNoise = noiseParametersLookup.getOrThrow(Noises.CAVE_ENTRANCE);

        //? if <26.3 {
        DensityFunction functionAquiferBarrier = DensityFunctions.noise(aquiferBarrier, 0.5);
        DensityFunction functionAquiferFloodedness = DensityFunctions.noise(aquiferFloodedness, 0.67);
        DensityFunction functionAquiferSpread = DensityFunctions.noise(aquiferSpread, 0.7142857142857143);
        DensityFunction functionAquiferLava = DensityFunctions.noise(aquiferLava);
        //? }
        DensityFunction functionCaveEntranceNoise = DensityFunctions.noise(caveEntranceNoise);

        DensityFunction functionSlopedCheeseEstimate = DensityFunctions.add(
            DensityFunctions.yClampedGradient(0, 64, 3.0F, 1.0F),
            DensityFunctions.mul(
                DensityFunctions.constant(-0.5F),
                functionCaveEntranceNoise
            )
        );
        DensityFunction functionCaveEntrances = DensityFunctions.min(
            functionSlopedCheeseEstimate,
            DensityFunctions.mul(DensityFunctions.constant(5.0F),
                new DensityFunctions.HolderHolder(densityFunctionLookup.getOrThrow(NoiseRouterDataAccessor.getEntrancesKey())))
        );
        DensityFunction functionCaves = DensityFunctions.rangeChoice(
            functionSlopedCheeseEstimate, -1000000.0F, 1.5625F, functionCaveEntrances,
            NoiseRouterDataAccessor.invokeUnderground(densityFunctionLookup, noiseParametersLookup, functionSlopedCheeseEstimate)
        );
        DensityFunction functionCavesWithNoodles = DensityFunctions.min(
            NoiseRouterDataAccessor.invokePostProcess(
                NoiseRouterDataAccessor.invokeSlideOverworld(false, functionCaves)
                //? if >=26.3 {
                /*, noiseSettings.noiseSizeHorizontal(),
                noiseSettings.noiseSizeVertical()
                *///? }
            ),
            new DensityFunctions.HolderHolder(densityFunctionLookup.getOrThrow(NoiseRouterDataAccessor.getNoodleKey()))
        );
        
        return new NoiseRouter(
            //? if <26.3 {
            functionAquiferBarrier,      // Barrier noise
            functionAquiferFloodedness,  // Fluid level floodedness noise
            functionAquiferSpread,       // Fluid level spread noise
            functionAquiferLava,         // Lava noise
            //? }
            DensityFunctions.zero(),     // Temperature
            DensityFunctions.zero(),     // Vegetation
            DensityFunctions.zero(),     // Continents
            DensityFunctions.zero(),     // Erosion
            DensityFunctions.zero(),     // Depth
            DensityFunctions.zero(),     // Ridges
            DensityFunctions.zero(),     // Initial density
            functionCavesWithNoodles    // Final density (used for noise caves post-processor)
            //? if <26.3 {
            , DensityFunctions.zero(),     // Vein Toggle
            DensityFunctions.zero(),     // Vein Ridged
            DensityFunctions.zero()      // Vein Gap
            //? }
        );
    }

    //? if >=26.3 {
    /*private static Aquifer.Config createAquiferConfig(HolderGetter<NormalNoise.NoiseParameters> noiseParametersLookup) {
        Reference<NormalNoise.NoiseParameters> aquiferBarrier = noiseParametersLookup.getOrThrow(Noises.AQUIFER_BARRIER);
        Reference<NormalNoise.NoiseParameters> aquiferFloodedness = noiseParametersLookup.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS);
        Reference<NormalNoise.NoiseParameters> aquiferSpread = noiseParametersLookup.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_SPREAD);
        Reference<NormalNoise.NoiseParameters> aquiferLava = noiseParametersLookup.getOrThrow(Noises.AQUIFER_LAVA);

        DensityFunction functionAquiferBarrier = DensityFunctions.noise(aquiferBarrier, 0.5);
        DensityFunction functionAquiferFloodedness = DensityFunctions.noise(aquiferFloodedness, 0.67);
        DensityFunction functionAquiferSpread = DensityFunctions.noise(aquiferSpread, 0.7142857142857143);
        DensityFunction functionAquiferLava = DensityFunctions.noise(aquiferLava);

        return new Aquifer.Config(
            functionAquiferBarrier,
            functionAquiferFloodedness,
            functionAquiferSpread,
            functionAquiferLava,
            DensityFunctions.zero(),
            DensityFunctions.zero()
        );
    }
    *///? }

    private static NoiseGeneratorSettings createNoiseGeneratorSettings(
        BootstrapContext<NoiseGeneratorSettings> context,
        NoiseSettings shapeConfig,
        int seaLevel,
        boolean useAquifers
    ) {
        HolderGetter<DensityFunction> densityFunctionLookup = context.lookup(Registries.DENSITY_FUNCTION);
        HolderGetter<NormalNoise.NoiseParameters> noiseParametersLookup = context.lookup(Registries.NOISE);
        //? if >=26.3 {
        /*HolderGetter<SurfaceRules.RuleSource> materialRuleLookup = context.lookup(Registries.MATERIAL_RULE);
        *///? } else {
        HolderGetter<net.minecraft.world.level.biome.Biome> biomeLookup = context.lookup(Registries.BIOME);
         //? }

        return createNoiseGeneratorSettings(
            densityFunctionLookup,
            noiseParametersLookup,
            //? if >=26.3 {
            /*materialRuleLookup,
            *///? } else {
            biomeLookup,
            //? }
            shapeConfig,
            seaLevel,
            useAquifers
        );
    }

    public static NoiseGeneratorSettings createNoiseGeneratorSettings(
        HolderLookup.Provider lookup,
        NoiseSettings shapeConfig,
        int seaLevel,
        boolean useAquifers
    ) {
        HolderGetter<DensityFunction> densityFunctionLookup = lookup.lookupOrThrow(Registries.DENSITY_FUNCTION);
        HolderGetter<NormalNoise.NoiseParameters> noiseParametersLookup = lookup.lookupOrThrow(Registries.NOISE);
        //? if >=26.3 {
        /*HolderGetter<SurfaceRules.RuleSource> materialRuleLookup = lookup.lookupOrThrow(Registries.MATERIAL_RULE);
        *///? } else {
        HolderGetter<net.minecraft.world.level.biome.Biome> biomeLookup = lookup.lookupOrThrow(Registries.BIOME);
        //? }

        return createNoiseGeneratorSettings(
            densityFunctionLookup,
            noiseParametersLookup,
            //? if >=26.3 {
            /*materialRuleLookup,
            *///? } else {
            biomeLookup,
            //? }
            shapeConfig,
            seaLevel,
            useAquifers
        );
    }
    
    private static NoiseGeneratorSettings createNoiseGeneratorSettings(
        HolderGetter<DensityFunction> densityFunctionLookup,
        HolderGetter<NormalNoise.NoiseParameters> noiseParametersLookup,
        //? if >=26.3 {
        /*HolderGetter<SurfaceRules.RuleSource> materialRuleLookup,
        *///? } else {
        HolderGetter<net.minecraft.world.level.biome.Biome> biomeLookup,
        //? }
        NoiseSettings shapeConfig,
        int seaLevel,
        boolean useAquifers
    ) {
        //? if <26.3
        SurfaceRules.RuleSource materialRule = ModernBetaSurfaceRuleData.overworldLike(biomeLookup, true, false, false, false);

        return new NoiseGeneratorSettings(
            shapeConfig.toVanilla(),
            BlockStates.STONE,
            BlockStates.WATER,
            createDensityFunctions(shapeConfig, densityFunctionLookup, noiseParametersLookup),
            /*? >=26.3 {*/ /*materialRuleLookup.getOrThrow(net.minecraft.data.worldgen.material.OverworldSurfaceRules.OVERWORLD) *//*? } else {*/ materialRule /*? }*/,
            List.of(),
            seaLevel,
            false,
            //? if >=26.3 {
            /*useAquifers ? Optional.of(createAquiferConfig(noiseParametersLookup)) : Optional.empty(),
            *///? } else {
            useAquifers,
            false,
            //? }
            true
            //? if >=26.3
            //, NoiseGeneratorSettings.DebugFunctions.EMPTY
        );
    }

    static {
        INFDEV_415 = ResourceKey.create(Registries.NOISE_SETTINGS, ModernerBeta.createId("infdev_415"));
        FINITE_2D = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBetaBuiltInTypes.Chunk.FINITE_2D.id);
        SKY_128 = ResourceKey.create(Registries.NOISE_SETTINGS, ModernerBeta.createId("sky_128"));
        OVERWORLD_128 = ResourceKey.create(Registries.NOISE_SETTINGS, ModernerBeta.createId("overworld_128"));
        OVERWORLD_256 = ResourceKey.create(Registries.NOISE_SETTINGS, ModernerBeta.createId("overworld_256"));
    }
}
