package mod.bluestaggo.modernerbeta;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import mod.bluestaggo.modernerbeta.api.level.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.network.INetworkHelper;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
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
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
    public static boolean GENERATING_DATA;

    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final List<String> BUILT_IN_PACKS = List.of(
        "reduced_height",
        "deepslate_blobs"
    );

    public static final List<Tuple<Registry<?>, Consumer<IRegistryHandler<?>>>> REGISTRY_HANDLERS = List.of(
        new Tuple<>(BuiltInRegistries.FOLIAGE_PLACER_TYPE, ModernBetaFoliagePlacers::register),
        new Tuple<>(BuiltInRegistries.TRUNK_PLACER_TYPE, ModernBetaTrunkPlacers::register),
        new Tuple<>(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, ModernBetaPlacementTypes::register),
        new Tuple<>(BuiltInRegistries.STRUCTURE_TYPE, ModernBetaStructureTypes::register),
        new Tuple<>(BuiltInRegistries.STRUCTURE_PIECE, ModernBetaStructurePieceTypes::register),
        new Tuple<>(BuiltInRegistries.FEATURE, ModernBetaFeatures::register),
        new Tuple<>(BuiltInRegistries.CARVER, ModernBetaCarvers::register),
        new Tuple<>(BuiltInRegistries.BIOME_SOURCE, ModernBetaBiomeSource::register),
        new Tuple<>(BuiltInRegistries.CHUNK_GENERATOR, ModernBetaChunkGenerator::register)
    );

    public static List<Tuple<Registry<?>, Consumer<IRegistryHandler<?>>>> CUSTOM_REGISTRY_HANDLERS;
    public static List<CustomDynamicRegistry<?>> CUSTOM_DYNAMIC_REGISTRIES;
    public static INetworkHelper networkHelper;
    public static ModernBetaSettings config;

    public static void init() {
        ModernerBeta.log(Level.INFO, "Initializing Moderner Beta...");
    }

    public static void setupCustomRegistryHandlers() {
        CUSTOM_REGISTRY_HANDLERS = List.of(
            new Tuple<>(ModernBetaRegistries.SETTINGS_COMPONENT_TYPE, SettingsComponentTypes::init),
            new Tuple<>(ModernBetaRegistries.CHUNK, ModernBetaBuiltInProviders::registerChunkProviders),
            new Tuple<>(ModernBetaRegistries.BIOME, ModernBetaBuiltInProviders::registerBiomeProviders),
            new Tuple<>(ModernBetaRegistries.CAVE_BIOME, ModernBetaBuiltInProviders::registerCaveBiomeProviders),
            new Tuple<>(ModernBetaRegistries.HEIGHT_CONFIG, ModernBetaBuiltInProviders::registerHeightConfigs),
            new Tuple<>(ModernBetaRegistries.BLOCKSOURCE, ModernBetaBuiltInProviders::registerBlockSources),
            new Tuple<>(ModernBetaRegistries.FRACTAL_LAYER, LayerType::init),
            new Tuple<>(ModernBetaRegistries.BIOME_PREDICATE, BiomePredicateType::init)
        );
    }

    public record CustomDynamicRegistry<T>(
        ResourceKey<? extends Registry<T>> key,
        ResourceKey<? extends Registry<?>> insertAfter,
        Codec<T> codec
    ) {
        public CustomDynamicRegistry(ResourceKey<? extends Registry<T>> key, Codec<T> codec) {
            this(key, null, codec);
        }
    }

    public static void setupCustomDynamicRegistries() {
        CUSTOM_DYNAMIC_REGISTRIES = List.of(
            new CustomDynamicRegistry<>(ModernBetaResourceKeys.SETTINGS_PRESET, Registries.NOISE_SETTINGS, ModernBetaSettingsPreset.CODEC),
            new CustomDynamicRegistry<>(ModernBetaResourceKeys.SETTINGS_PRESET_CATEGORY, ModernBetaSettingsPresetCategory.CODEC),
            new CustomDynamicRegistry<>(ModernBetaResourceKeys.SURFACE_CONFIG, SurfaceConfig.CODEC)
        );
    }

    public static ResourceLocation createId(String name) {
        return ResourceLocation./*? >=1.21 {*/fromNamespaceAndPath/*?} else {*//*tryBuild*//*?}*/(MOD_ID, name);
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
        CodecUtil.registerTypeAdapter(gson, ResourceLocation.class, ResourceLocation.CODEC);
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
