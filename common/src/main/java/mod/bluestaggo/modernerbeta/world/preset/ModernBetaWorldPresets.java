package mod.bluestaggo.modernerbeta.world.preset;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.api.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGeneratorSettings;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterLists;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;

import java.util.Map;

public class ModernBetaWorldPresets {
    public static final RegistryKey<WorldPreset> MODERN_BETA = keyOf(ModernerBeta.createId(ModernerBeta.MOD_ID));
            
    public static void bootstrap(Registerable<WorldPreset> presetRegisterable) {
        RegistryEntryLookup<DimensionType> registryDimensionType = presetRegisterable.getRegistryLookup(RegistryKeys.DIMENSION_TYPE);
        RegistryEntryLookup<ChunkGeneratorSettings> registrySettings = presetRegisterable.getRegistryLookup(RegistryKeys.CHUNK_GENERATOR_SETTINGS);
        RegistryEntryLookup<Biome> registryBiome = presetRegisterable.getRegistryLookup(RegistryKeys.BIOME);
        RegistryEntryLookup<MultiNoiseBiomeSourceParameterList> registryParameters = presetRegisterable.getRegistryLookup(RegistryKeys.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST);

        DimensionOptions overworld = createOverworldOptions(registryDimensionType, registrySettings, registryBiome);
        DimensionOptions nether = createNetherOptions(registryDimensionType, registrySettings, registryParameters);
        DimensionOptions end = createEndOptions(registryDimensionType, registrySettings, registryBiome);
        
        presetRegisterable.register(
            MODERN_BETA,
            new WorldPreset(Map.of(DimensionOptions.OVERWORLD, overworld, DimensionOptions.NETHER, nether, DimensionOptions.END, end))
        );
    }
    
    private static DimensionOptions createOverworldOptions(
        RegistryEntryLookup<DimensionType> registryDimensionType,
        RegistryEntryLookup<ChunkGeneratorSettings> registrySettings,
        RegistryEntryLookup<Biome> registryBiome
    ) {
        RegistryEntry.Reference<DimensionType> dimensionType = registryDimensionType.getOrThrow(DimensionTypes.OVERWORLD);
        RegistryEntry.Reference<ChunkGeneratorSettings> settings = registrySettings.getOrThrow(ModernBetaChunkGeneratorSettings.BETA);
        
        ModernBetaSettingsPreset defaultPreset = ModernBetaRegistries.SETTINGS_PRESET.get(ModernBetaBuiltInTypes.Chunk.BETA.id);
        
        return new DimensionOptions(
            dimensionType,
            new ModernBetaChunkGenerator(
                new ModernBetaBiomeSource(
                    registryBiome,
                    defaultPreset.settingsBiome().toCompound(),
                    defaultPreset.settingsCaveBiome().toCompound()
                ),
                settings,
                defaultPreset.settingsChunk().toCompound()
            )
        );
    }
    
    private static DimensionOptions createNetherOptions(
        RegistryEntryLookup<DimensionType> registryDimensionType,
        RegistryEntryLookup<ChunkGeneratorSettings> registrySettings,
        RegistryEntryLookup<MultiNoiseBiomeSourceParameterList> registryParameters
    ) {
        RegistryEntry.Reference<DimensionType> dimensionType = registryDimensionType.getOrThrow(DimensionTypes.THE_NETHER);
        RegistryEntry.Reference<ChunkGeneratorSettings> settings = registrySettings.getOrThrow(ChunkGeneratorSettings.NETHER);
        RegistryEntry.Reference<MultiNoiseBiomeSourceParameterList> parameters = registryParameters.getOrThrow(MultiNoiseBiomeSourceParameterLists.NETHER);
        
        return new DimensionOptions(dimensionType, new NoiseChunkGenerator(MultiNoiseBiomeSource.create(parameters), settings));
    }
    
    private static DimensionOptions createEndOptions(
        RegistryEntryLookup<DimensionType> registryDimensionType,
        RegistryEntryLookup<ChunkGeneratorSettings> registrySettings,
        RegistryEntryLookup<Biome> registryBiome
    ) {
        RegistryEntry.Reference<DimensionType> dimensionType = registryDimensionType.getOrThrow(DimensionTypes.THE_END);
        RegistryEntry.Reference<ChunkGeneratorSettings> settings = registrySettings.getOrThrow(ChunkGeneratorSettings.END);

        return new DimensionOptions(dimensionType, new NoiseChunkGenerator(TheEndBiomeSource.createVanilla(registryBiome), settings));
    }
    
    private static RegistryKey<WorldPreset> keyOf(Identifier id) {
        return RegistryKey.of(RegistryKeys.WORLD_PRESET, id);
    }
}
