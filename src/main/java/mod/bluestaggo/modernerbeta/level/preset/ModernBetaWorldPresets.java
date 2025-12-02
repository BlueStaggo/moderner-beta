package mod.bluestaggo.modernerbeta.level.preset;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.level.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaChunkGenerator;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaNoiseGeneratorSettings;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterLists;
import net.minecraft.world.level.biome.TheEndBiomeSource;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.presets.WorldPreset;

import java.util.Map;

public class ModernBetaWorldPresets {
    public static final ResourceKey<WorldPreset> MODERN_BETA = keyOf(ModernerBeta.createId(ModernerBeta.MOD_ID));
            
    public static void bootstrap(BootstrapContext<WorldPreset> context) {
        HolderGetter<DimensionType> registryDimensionType = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> registrySettings = context.lookup(Registries.NOISE_SETTINGS);
        HolderGetter<Biome> registryBiome = context.lookup(Registries.BIOME);
        HolderGetter<ModernBetaSettingsPreset> registryPreset = context.lookup(ModernBetaResourceKeys.SETTINGS_PRESET);
        HolderGetter<SurfaceConfig> registrySurfaceConfig = context.lookup(ModernBetaResourceKeys.SURFACE_CONFIG);
        HolderGetter<MultiNoiseBiomeSourceParameterList> registryParameters = context.lookup(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST);

        LevelStem overworld = createOverworldOptions(registryDimensionType, registrySettings, registryBiome, registryPreset, registrySurfaceConfig);
        LevelStem nether = createNetherOptions(registryDimensionType, registrySettings, registryParameters);
        LevelStem end = createEndOptions(registryDimensionType, registrySettings, registryBiome);
        
        context.register(
            MODERN_BETA,
            new WorldPreset(Map.of(LevelStem.OVERWORLD, overworld, LevelStem.NETHER, nether, LevelStem.END, end))
        );
    }
    
    private static LevelStem createOverworldOptions(
        HolderGetter<DimensionType> registryDimensionType,
        HolderGetter<NoiseGeneratorSettings> registrySettings,
        HolderGetter<Biome> registryBiome,
        HolderGetter<ModernBetaSettingsPreset> registryPreset,
        HolderGetter<SurfaceConfig> registrySurfaceConfig
    ) {
        Holder.Reference<DimensionType> dimensionType = registryDimensionType.getOrThrow(BuiltinDimensionTypes.OVERWORLD);
        Holder.Reference<NoiseGeneratorSettings> settings = registrySettings.getOrThrow(ModernBetaNoiseGeneratorSettings.OVERWORLD_256);
        
        ModernBetaSettingsPreset defaultPreset = ModernBetaSettingsPreset.referenced(ModernBetaSettings.DEFAULT_PRESET_ID);

        return new LevelStem(
            dimensionType,
            new ModernBetaChunkGenerator(
                new ModernBetaBiomeSource(
                    registryBiome,
                    registryPreset,
                    defaultPreset.biomeSettings().toCompound(),
                    defaultPreset.caveBiomeSettings().toCompound()
                ),
                registrySettings,
                registryPreset,
                registrySurfaceConfig,
                defaultPreset.chunkSettings().toCompound()
            )
        );
    }
    
    private static LevelStem createNetherOptions(
        HolderGetter<DimensionType> registryDimensionType,
        HolderGetter<NoiseGeneratorSettings> registrySettings,
        HolderGetter<MultiNoiseBiomeSourceParameterList> registryParameters
    ) {
        Holder.Reference<DimensionType> dimensionType = registryDimensionType.getOrThrow(BuiltinDimensionTypes.NETHER);
        Holder.Reference<NoiseGeneratorSettings> settings = registrySettings.getOrThrow(NoiseGeneratorSettings.NETHER);
        Holder.Reference<MultiNoiseBiomeSourceParameterList> parameters = registryParameters.getOrThrow(MultiNoiseBiomeSourceParameterLists.NETHER);
        
        return new LevelStem(dimensionType, new NoiseBasedChunkGenerator(MultiNoiseBiomeSource.createFromPreset(parameters), settings));
    }
    
    private static LevelStem createEndOptions(
        HolderGetter<DimensionType> registryDimensionType,
        HolderGetter<NoiseGeneratorSettings> registrySettings,
        HolderGetter<Biome> registryBiome
    ) {
        Holder.Reference<DimensionType> dimensionType = registryDimensionType.getOrThrow(BuiltinDimensionTypes.END);
        Holder.Reference<NoiseGeneratorSettings> settings = registrySettings.getOrThrow(NoiseGeneratorSettings.END);

        return new LevelStem(dimensionType, new NoiseBasedChunkGenerator(TheEndBiomeSource.create(registryBiome), settings));
    }
    
    private static ResourceKey<WorldPreset> keyOf(ResourceLocation id) {
        return ResourceKey.create(Registries.WORLD_PRESET, id);
    }
}
