package mod.bluestaggo.modernerbeta;

import com.google.gson.GsonBuilder;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import mod.bluestaggo.modernerbeta.config.ModernBetaConfig;
import mod.bluestaggo.modernerbeta.network.INetworkHelper;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ConfiguredLayers;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerType;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates.BiomePredicateType;
import mod.bluestaggo.modernerbeta.world.carver.ModernBetaCarvers;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bluestaggo.modernerbeta.world.feature.ModernBetaFeatures;
import mod.bluestaggo.modernerbeta.world.feature.ModernBetaFoliagePlacers;
import mod.bluestaggo.modernerbeta.world.feature.placement.ModernBetaPlacementTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.util.Map;
import java.util.function.Consumer;

public class ModernerBeta {
    public static final String MOD_ID = "moderner_beta";
    public static final String MOD_NAME = "Moderner Beta";

    public static boolean DEV_ENV;

    public static final ModernBetaConfig CONFIG = AutoConfig.register(ModernBetaConfig.class, GsonConfigSerializer::new).getConfig();
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Map<Registry<?>, Consumer<IRegistryHandler<?>>> REGISTRY_HANDLERS = Map.of(
        Registries.FOLIAGE_PLACER_TYPE, ModernBetaFoliagePlacers::register,
        Registries.PLACEMENT_MODIFIER_TYPE, ModernBetaPlacementTypes::register,
        Registries.FEATURE, ModernBetaFeatures::register,
        Registries.CARVER, ModernBetaCarvers::register,
        Registries.BIOME_SOURCE, ModernBetaBiomeSource::register,
        Registries.CHUNK_GENERATOR, ModernBetaChunkGenerator::register
    );

    public static Map<Registry<?>, Consumer<IRegistryHandler<?>>> CUSTOM_REGISTRY_HANDLERS;
    public static INetworkHelper networkHelper;

    public static void init() {
        ModernerBeta.log(Level.INFO, "Initializing Moderner Beta...");
    }

    public static void setupCustomRegistryHandlers() {
        CUSTOM_REGISTRY_HANDLERS = Map.ofEntries(
            Map.entry(ModernBetaRegistries.CHUNK, ModernBetaBuiltInProviders::registerChunkProviders),
            Map.entry(ModernBetaRegistries.BIOME, ModernBetaBuiltInProviders::registerBiomeProviders),
            Map.entry(ModernBetaRegistries.CAVE_BIOME, ModernBetaBuiltInProviders::registerCaveBiomeProviders),
            Map.entry(ModernBetaRegistries.SURFACE_CONFIG, ModernBetaBuiltInProviders::registerSurfaceConfigs),
            Map.entry(ModernBetaRegistries.HEIGHT_CONFIG, ModernBetaBuiltInProviders::registerHeightConfigs),
            Map.entry(ModernBetaRegistries.NOISE_POST_PROCESSOR, ModernBetaBuiltInProviders::registerNoisePostProcessors),
            Map.entry(ModernBetaRegistries.BLOCKSOURCE, ModernBetaBuiltInProviders::registerBlockSources),
            Map.entry(ModernBetaRegistries.SETTINGS_PRESET, ModernBetaBuiltInProviders::registerSettingsPresets),
            Map.entry(ModernBetaRegistries.SETTINGS_PRESET_CATEGORY, ModernBetaBuiltInProviders::registerSettingsPresetCategories),
            Map.entry(ModernBetaRegistries.FRACTAL_LAYER, LayerType::init),
            Map.entry(ModernBetaRegistries.BIOME_PREDICATE, BiomePredicateType::init)
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
}
