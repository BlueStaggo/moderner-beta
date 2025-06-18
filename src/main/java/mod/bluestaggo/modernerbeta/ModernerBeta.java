package mod.bluestaggo.modernerbeta;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import mod.bluestaggo.modernerbeta.network.INetworkHelper;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistryKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ConfiguredLayers;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerType;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates.BiomePredicateType;
import mod.bluestaggo.modernerbeta.world.carver.ModernBetaCarvers;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bluestaggo.modernerbeta.world.feature.ModernBetaFeatures;
import mod.bluestaggo.modernerbeta.world.feature.ModernBetaFoliagePlacers;
import mod.bluestaggo.modernerbeta.world.feature.ModernBetaTrunkPlacers;
import mod.bluestaggo.modernerbeta.world.feature.placement.ModernBetaPlacementTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public class ModernerBeta {
    public static final String MOD_ID = "moderner_beta";
    public static final String MOD_NAME = "Moderner Beta";

    public static boolean DEV_ENV;

    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final List<String> BUILT_IN_PACKS = List.of(
        "reduced_height",
        "deepslate_blobs"
    );

    public static final List<Pair<Registry<?>, Consumer<IRegistryHandler<?>>>> REGISTRY_HANDLERS = List.of(
        new Pair<>(Registries.FOLIAGE_PLACER_TYPE, ModernBetaFoliagePlacers::register),
        new Pair<>(Registries.TRUNK_PLACER_TYPE, ModernBetaTrunkPlacers::register),
        new Pair<>(Registries.PLACEMENT_MODIFIER_TYPE, ModernBetaPlacementTypes::register),
        new Pair<>(Registries.FEATURE, ModernBetaFeatures::register),
        new Pair<>(Registries.CARVER, ModernBetaCarvers::register),
        new Pair<>(Registries.BIOME_SOURCE, ModernBetaBiomeSource::register),
        new Pair<>(Registries.CHUNK_GENERATOR, ModernBetaChunkGenerator::register)
    );

    public static List<Pair<Registry<?>, Consumer<IRegistryHandler<?>>>> CUSTOM_REGISTRY_HANDLERS;
    public static List<Pair<RegistryKey<?>, Codec<?>>> CUSTOM_DYNAMIC_REGISTRIES;
    public static INetworkHelper networkHelper;
    public static ModernBetaSettings config;

    public static void init() {
        ModernerBeta.log(Level.INFO, "Initializing Moderner Beta...");
    }

    public static void setupCustomRegistryHandlers() {
        CUSTOM_REGISTRY_HANDLERS = List.of(
            new Pair<>(ModernBetaRegistries.SETTINGS_COMPONENT_TYPE, SettingsComponentTypes::init),
            new Pair<>(ModernBetaRegistries.CHUNK, ModernBetaBuiltInProviders::registerChunkProviders),
            new Pair<>(ModernBetaRegistries.BIOME, ModernBetaBuiltInProviders::registerBiomeProviders),
            new Pair<>(ModernBetaRegistries.CAVE_BIOME, ModernBetaBuiltInProviders::registerCaveBiomeProviders),
            new Pair<>(ModernBetaRegistries.SURFACE_CONFIG, ModernBetaBuiltInProviders::registerSurfaceConfigs),
            new Pair<>(ModernBetaRegistries.HEIGHT_CONFIG, ModernBetaBuiltInProviders::registerHeightConfigs),
            new Pair<>(ModernBetaRegistries.BLOCKSOURCE, ModernBetaBuiltInProviders::registerBlockSources),
            new Pair<>(ModernBetaRegistries.FRACTAL_LAYER, LayerType::init),
            new Pair<>(ModernBetaRegistries.BIOME_PREDICATE, BiomePredicateType::init)
        );
    }

    public static void setupCustomDynamicRegistries() {
        CUSTOM_DYNAMIC_REGISTRIES = List.of(
            new Pair<>(ModernBetaRegistryKeys.SETTINGS_PRESET, ModernBetaSettingsPreset.CODEC),
            new Pair<>(ModernBetaRegistryKeys.SETTINGS_PRESET_CATEGORY, ModernBetaSettingsPresetCategory.CODEC)
        );
    }

    public static Identifier createId(String name) {
        return Identifier.of(MOD_ID, name);
    }

    public static void log(Level level, String message) {
        LOGGER.atLevel(level).log("[" + MOD_NAME + "] {}", message);
    }

    public static void log(String message) {
        log(Level.INFO, message);
    }

    public static GsonBuilder getSettingsGson() {
        GsonBuilder gson = new GsonBuilder();
        CodecUtil.registerTypeAdapter(gson, ConfiguredLayers.class, ConfiguredLayers.CODEC);
        CodecUtil.registerTypeAdapter(gson, Identifier.class, Identifier.CODEC);
        return gson;
    }

    public static void loadConfig(Path configDir) {
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
    }

    public static void saveConfig(Path configDir) {
        Path configFile = configDir.resolve(MOD_ID + ".json");

        DataResult<JsonElement> encodedConfig = ModernBetaSettings.CODEC.encode(config, JsonOps.INSTANCE, new JsonObject());
        if (encodedConfig.result().isEmpty()) {
            log(Level.WARN, "Failed to serialize config to JSON: " + encodedConfig);
            return;
        }

        try {
            Files.createDirectories(configDir);
            try (BufferedWriter writer = Files.newBufferedWriter(configFile)) {
                getSettingsGson().setPrettyPrinting().create().toJson(encodedConfig.result().get(), writer);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
