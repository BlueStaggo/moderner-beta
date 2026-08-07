//~dotLocation
package mod.bluestaggo.modernerbeta;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import mod.bluestaggo.modernerbeta.api.level.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.level.biome.injection.injector.BiomeInjectorType;
import mod.bluestaggo.modernerbeta.level.biome.injection.predicates.InjectionPredicateType;
import mod.bluestaggo.modernerbeta.network.INetworkHelper;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.services.ModernBetaServices;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSavedPresetPack;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresets;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.MiscConfig;
import mod.bluestaggo.modernerbeta.util.AtomicFile;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ConfiguredLayers;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.LayerType;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.predicates.BiomePredicateType;
import mod.bluestaggo.modernerbeta.level.carver.ModernBetaCarvers;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaChunkGenerator;
import mod.bluestaggo.modernerbeta.level.feature.ModernBetaFeatures;
import mod.bluestaggo.modernerbeta.level.feature.ModernBetaFoliagePlacers;
import mod.bluestaggo.modernerbeta.level.feature.ModernBetaTrunkPlacers;
import mod.bluestaggo.modernerbeta.level.feature.placement.ModernBetaPlacementTypes;
import mod.bluestaggo.modernerbeta.level.structure.ModernBetaStructurePieceTypes;
import mod.bluestaggo.modernerbeta.level.structure.ModernBetaStructureTypes;
import mod.bluestaggo.modernerbeta.util.LoggingUtil;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import org.slf4j.event.Level;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public class ModernerBeta {
    public static final String MOD_ID = "moderner_beta";
    public static final String MOD_NAME = "Moderner Beta";

    public static final boolean DEV_ENV = ModernBetaServices.PLATFORM.isDevEnvironment();
    public static boolean GENERATING_DATA;

    public static final List<String> BUILT_IN_PACKS = List.of(
        "reduced_height",
        "deepslate_blobs"
    );

    public static final List<Pair<Registry<?>, Consumer<IRegistryHandler<?>>>> REGISTRY_HANDLERS = List.of(
        new Pair<>(BuiltInRegistries.FOLIAGE_PLACER_TYPE, ModernBetaFoliagePlacers::register),
        new Pair<>(BuiltInRegistries.TRUNK_PLACER_TYPE, ModernBetaTrunkPlacers::register),
        new Pair<>(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, ModernBetaPlacementTypes::register),
        new Pair<>(BuiltInRegistries.STRUCTURE_TYPE, ModernBetaStructureTypes::register),
        new Pair<>(BuiltInRegistries.STRUCTURE_PIECE, ModernBetaStructurePieceTypes::register),
        new Pair<>(BuiltInRegistries.FEATURE, ModernBetaFeatures::register),
        new Pair<>(BuiltInRegistries.CARVER, ModernBetaCarvers::register),
        new Pair<>(BuiltInRegistries.BIOME_SOURCE, ModernBetaBiomeSource::register),
        new Pair<>(BuiltInRegistries.CHUNK_GENERATOR, ModernBetaChunkGenerator::register)
    );

    public static List<Pair<Registry<?>, Consumer<IRegistryHandler<?>>>> CUSTOM_REGISTRY_HANDLERS;
    public static List<Pair<ResourceKey<?>, Codec<?>>> CUSTOM_DYNAMIC_REGISTRIES;
    public static INetworkHelper networkHelper;
    public static ModernBetaSettings config;
    private static Path configDir;

    public static void init() {
        LoggingUtil.log(Level.INFO, "Initializing Moderner Beta...");
    }

    public static void setupCustomRegistryHandlers() {
        CUSTOM_REGISTRY_HANDLERS = List.of(
            new Pair<>(ModernBetaRegistries.SETTINGS_COMPONENT_TYPE, SettingsComponentTypes::init),
            new Pair<>(ModernBetaRegistries.CHUNK, ModernBetaBuiltInProviders::registerChunkProviders),
            new Pair<>(ModernBetaRegistries.BIOME, ModernBetaBuiltInProviders::registerBiomeProviders),
            new Pair<>(ModernBetaRegistries.CAVE_BIOME, ModernBetaBuiltInProviders::registerCaveBiomeProviders),
            new Pair<>(ModernBetaRegistries.HEIGHT_CONFIG, ModernBetaBuiltInProviders::registerHeightConfigs),
            new Pair<>(ModernBetaRegistries.BLOCKSOURCE, ModernBetaBuiltInProviders::registerBlockSources),
            new Pair<>(ModernBetaRegistries.FRACTAL_LAYER, LayerType::init),
            new Pair<>(ModernBetaRegistries.BIOME_PREDICATE, BiomePredicateType::init),
            new Pair<>(ModernBetaRegistries.BIOME_INJECTOR, BiomeInjectorType::init),
            new Pair<>(ModernBetaRegistries.INJECTION_PREDICATE, InjectionPredicateType::init)
        );
    }

    public static void setupCustomDynamicRegistries() {
        CUSTOM_DYNAMIC_REGISTRIES = List.of(
                new Pair<>(ModernBetaResourceKeys.SETTINGS_PRESET, ModernBetaSettingsPreset.CODEC),
                new Pair<>(ModernBetaResourceKeys.SETTINGS_PRESET_CATEGORY, ModernBetaSettingsPresetCategory.CODEC),
                new Pair<>(ModernBetaResourceKeys.SURFACE_CONFIG, SurfaceConfig.CODEC)
        );
    }

    public static Identifier createId(String name) {
        return Identifier./*? >=1.21 {*/fromNamespaceAndPath/*?} else {*//*tryBuild*//*?}*/(MOD_ID, name);
    }

    public static GsonBuilder getSettingsGson() {
        GsonBuilder gson = new GsonBuilder();
        CodecUtil.registerTypeAdapter(gson, ConfiguredLayers.class, ConfiguredLayers.CODEC);
        CodecUtil.registerTypeAdapter(gson, Identifier.class, Identifier.CODEC);
        return gson;
    }

    public static void loadConfig(Path configDir) {
        ModernerBeta.configDir = configDir;
        Path configFile = configDir.resolve(MOD_ID + ".json");
        try (BufferedReader reader = Files.newBufferedReader(configFile)) {
            config = ModernBetaSettings.CODEC.decode(
                JsonOps.INSTANCE,
                getSettingsGson().create().fromJson(reader, JsonElement.class)
            )
                .result()
                .orElseGet(() -> com.mojang.datafixers.util.Pair.of(ModernBetaSettings.empty(), null))
                .getFirst();
        } catch (NoSuchFileException exception) {
            config = ModernBetaSettings.builder()
                .addDefault(
                    SettingsComponentTypes.CONFIG_BETA_CLIMATIC_COLORS,
                    SettingsComponentTypes.CONFIG_PE_CLIMATIC_COLORS,
                    SettingsComponentTypes.CONFIG_BETA_FRACTAL_CLIMATIC_COLORS,
                    SettingsComponentTypes.CONFIG_BIOME_PREVIEW_COLORS,
                    SettingsComponentTypes.CONFIG_MISCELLANEOUS
                )
                .build();
            saveConfig(configDir);
        } catch (IOException exception) {
            exception.printStackTrace();
            config = ModernBetaSettings.empty();
        }

        if (ModernBetaSavedPresetPack.exists(configDir)) {
            ModernBetaSavedPresetPack.refreshMetadata(configDir);
        }
    }

    public static boolean saveConfig(Path configDir) {
        Path configFile = configDir.resolve(MOD_ID + ".json");

        DataResult<JsonElement> encodedConfig = ModernBetaSettings.CODEC.encode(config, JsonOps.INSTANCE, new JsonObject());
        if (encodedConfig.result().isEmpty()) {
            LoggingUtil.log(Level.WARN, "Failed to serialize config to JSON: " + encodedConfig);
            return false;
        }

        try {
            byte[] bytes = getSettingsGson()
                .setPrettyPrinting()
                .create()
                .toJson(encodedConfig.result().get())
                .getBytes(StandardCharsets.UTF_8);
            AtomicFile.write(configFile, bytes);
            return true;
        } catch (IOException exception) {
            LoggingUtil.log(Level.ERROR, "Failed to save config: " + exception.getMessage());
            return false;
        }
    }

    public static boolean setDefaultSettingsPreset(Identifier presetId) {
        MiscConfig misc = config.getOrDefault(SettingsComponentTypes.CONFIG_MISCELLANEOUS);
        ModernBetaSettings previousConfig = config;
        config = previousConfig.extend()
            .add(SettingsComponentTypes.CONFIG_MISCELLANEOUS, new MiscConfig(
                misc.oldFogColorWeighting(),
                presetId
            ))
            .build();
        if (configDir != null && saveConfig(configDir)) {
            return true;
        }

        config = previousConfig;
        return false;
    }

    public static Identifier getDefaultPresetId() {
        return config == null ?
            ModernBetaSettingsPresets.BETA_1_7_3.identifier() :
            config.getOrDefault(SettingsComponentTypes.CONFIG_MISCELLANEOUS).defaultSettingsPreset();
    }

    public static Path getConfigDir() {
        return configDir;
    }
}
