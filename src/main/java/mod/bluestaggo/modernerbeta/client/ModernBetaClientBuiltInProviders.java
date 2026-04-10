package mod.bluestaggo.modernerbeta.client;

import mod.bluestaggo.modernerbeta.api.level.biome.climate.TemperatureHeightScaling;
import mod.bluestaggo.modernerbeta.client.gui.screen.config.graphical.*;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.settings.component.CaveGeneration;
import mod.bluestaggo.modernerbeta.level.chunk.provider.indev.IndevTheme;
import mod.bluestaggo.modernerbeta.level.chunk.provider.indev.IndevType;
import mod.bluestaggo.modernerbeta.level.chunk.provider.island.IslandShape;
import mod.bluestaggo.modernerbeta.settings.component.WorldBorderLocation;
import net.minecraft.ChatFormatting;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.nbt.Tag;

import static mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes.SettingsComponentType.*;

@SuppressWarnings("unchecked")
public class ModernBetaClientBuiltInProviders {
    private static void addAll(OptionsList list, OptionInstance<?>... options) {
        list.addSmall(options);
    }

    public static void registerSettingsComponentTypeGuis(IRegistryHandler<?> handler) {
        IRegistryHandler<GraphicalConfigBuilder> registryHandler = (IRegistryHandler<GraphicalConfigBuilder>) handler;

        registryHandler.register(
            DEEPSLATE_GENERATION.id,
            (screen, options) -> {
                int minY = -64;
                int maxY = 320;
                if (screen instanceof ModernBetaGraphicalProviderSettingsScreen providerSettingsScreen) {
                    minY = providerSettingsScreen.worldMinY;
                    maxY = providerSettingsScreen.worldMaxY;
                }

                addAll(
                    options,
                    screen.booleanOption("enabled"),
                    screen.blockOption("block"),
                    screen.intRangeOption("minY", minY, maxY),
                    screen.intRangeOption("maxY", minY, maxY)
                );
            }
        );

        registryHandler.register(
            USE_SURFACE_RULES.id,
            (screen, options) -> {
                options.addBig(screen.booleanOption("self"));
            }
        );

        registryHandler.register(
            SEA_LEVEL.id,
            (screen, options) -> {
                int minY = -64;
                int maxY = 320;
                if (screen instanceof ModernBetaGraphicalProviderSettingsScreen providerSettingsScreen) {
                    minY = providerSettingsScreen.worldMinY;
                    maxY = providerSettingsScreen.worldMaxY;
                }

                options.addBig(screen.intRangeOption("self", minY, maxY));
            }
        );

        registryHandler.register(
            CAVE_GENERATION.id,
            (screen, options) -> {
                addAll(
                    options,
                    screen.booleanOption("useCarvers"),
                    screen.booleanOption("useNoiseCaves"),
                    screen.booleanOption("fixCaveBorders"),
                    screen.booleanOption("forceBetaCaves"),
                    screen.booleanOption("forceBetaCanyons"),
                    screen.selectionOption("seedMethod", CaveGeneration.SeedMethod::values)
                );
            }
        );

        registryHandler.register(
            NOISE_SETTINGS.id,
            (screen, options) -> {
                int minY = -64;
                int maxY = 320;
                if (screen instanceof ModernBetaGraphicalProviderSettingsScreen providerSettingsScreen) {
                    minY = providerSettingsScreen.worldMinY;
                    maxY = providerSettingsScreen.worldMaxY;
                }

                addAll(
                    options,
                    screen.intRangeOption("min_y", minY, maxY, 16),
                    screen.intRangeOption("height", 0, maxY - minY, 16),
                    screen.intRangeOption("size_horizontal", 1, 4),
                    screen.intRangeOption("size_vertical", 1, 4)
                );
            }
        );

        registryHandler.register(
            NOISE_3D_SETTINGS.id,
            (screen, options) -> {
                addAll(
                    options,
                    screen.booleanOption("monoliths"),
                    screen.booleanOption("oldInfdevTerrainNoise"),
                    screen.booleanOption("climateHeightScaling"),
                    screen.booleanOption("arraySurfaceNoise"),
                    screen.booleanOption("simplexSurfaceNoise"),
                    screen.booleanOption("pocketEditionRng")
                );
            }
        );

        registryHandler.register(
            PERLIN_NOISE_SETTINGS.id,
            (screen, options) -> {
                addAll(
                    options,
                    screen.booleanOption("wrapped"),
                    screen.booleanOption("randomNoiseOffsets"),
                    screen.booleanOption("alpha2DSampling"),
                    screen.booleanOption("infdevNoiseScaling"),
                    screen.intRangeOption("failurePoint", 0, Integer.MAX_VALUE)
                );
            }
        );

        registryHandler.register(
            NOISE_SCALE.id,
            (screen, options) -> {
                addAll(
                    options,
                    screen.floatRangeOption("mainNoiseX", 1.0f, 5000.0f),
                    screen.floatRangeOption("mainNoiseY", 1.0f, 5000.0f),
                    screen.floatRangeOption("mainNoiseZ", 1.0f, 5000.0f),
                    screen.floatRangeOption("depthNoiseX", 1.0f, 2000.0f),
                    screen.floatRangeOption("depthNoiseZ", 1.0f, 2000.0f),
                    screen.floatRangeOption("baseSize", 1.0f, 25.0f),
                    screen.floatRangeOption("coordinate", 1.0f, 6000.0f),
                    screen.floatRangeOption("height", 1.0f, 6000.0f),
                    screen.floatRangeOption("stretchY", 0.01f, 50.0f),
                    screen.floatRangeOption("upperLimit", 1.0f, 5000.0f),
                    screen.floatRangeOption("lowerLimit", 1.0f, 5000.0f),
                    screen.floatRangeOption("densityUnderdamp", -10.0f, 10.0f),
                    screen.floatRangeOption("limitBlending", 0.01f, 100.0f),
                    screen.booleanOption("useFixedOffset"),
                    screen.floatRangeOption("fixedOffset", 0.01f, 100.0f),
                    screen.intRangeOption("forestNoiseOctaves", 1, 16)
                );
            }
        );

        registryHandler.register(
            NOISE_SLIDE.id,
            (screen, options) -> {
                addAll(
                    options,
                    screen.intRangeOption("topTarget", -50, 0),
                    screen.intRangeOption("bottomTarget", 0, 50),
                    screen.intRangeOption("topSize", 0, 50),
                    screen.intRangeOption("bottomSize", 0, 50),
                    screen.intRangeOption("topOffset", -10, 10),
                    screen.intRangeOption("bottomOffset", -10, 10)
                );
            }
        );

        registryHandler.register(
            NOISE_LANDMASS.id,
            (screen, options) -> {
                addAll(
                    options,
                    screen.booleanOption("depth.enabled"),
                    screen.booleanOption("scale.enabled"),
                    screen.booleanOption("depth.sample"),
                    screen.booleanOption("scale.sample"),
                    screen.floatRangeOption("scale.variation", 0.0f, 2.0f),
                    screen.floatRangeOption("depth.influence", 0.0f, 2.0f),
                    screen.floatRangeOption("scale.influence", 0.0f, 2.0f),
                    screen.floatRangeOption("depth.negativeInfluence", 0.0f, 2.0f),
                    screen.floatRangeOption("depth.stretch", 0.0f, 5.0f),
                    screen.floatRangeOption("scale.offset", -5.0f, 5.0f),
                    screen.floatRangeOption("depth.offset", -5.0f, 5.0f),
                    screen.floatRangeOption("depth.positiveDampening", 1.0f, 10.0f),
                    screen.floatRangeOption("depth.negativeDampening", 1.0f, 10.0f),
                    screen.floatRangeOption("depth.minValue", -1.0f, 0.0f),
                    screen.floatRangeOption("depth.maxValue", 0.0f, 1.0f),
                    screen.booleanOption("depth.negativeFlattening"),
                    screen.booleanOption("alphaSampling")
                );
            }
        );

        registryHandler.register(
            FORCED_BIOME_HEIGHT.id,
            (screen, options) -> {
                options.addBig(screen.mapEditButton(
                    screen.getText("heightOverrides"),
                    "heightOverrides",
                    ExtendedBiomeIdToHeightConfigMapScreen::new
                ));
                addAll(
                    options,
                    screen.booleanOption("enabled"),
                    screen.booleanOption("modifyOnlyPositiveDepth"),
                    screen.floatRangeOption("depthWeight", 1.0f, 20.0f),
                    screen.floatRangeOption("depthOffset", 0.0f, 20.0f),
                    screen.floatRangeOption("scaleWeight", 1.0f, 20.0f),
                    screen.floatRangeOption("scaleOffset", 0.0f, 20.0f)
                );
            }
        );

        registryHandler.register(
            SURFACE_PROPERTIES.id,
            (screen, options) -> {
                addAll(
                    options,
                    screen.booleanOption("generateLiquids"),
                    screen.booleanOption("generateBedrock"),
                    screen.booleanOption("bedrockHoles"),
                    screen.booleanOption("uniformBedrock"),
                    screen.booleanOption("flipNoiseCoordinates"),
                    screen.booleanOption("enableBeaches"),
                    screen.floatRangeOption("sandBeachScale", 0.0f, 1.0f),
                    screen.floatRangeOption("gravelBeachScale", 0.0f, 1.0f),
                    screen.floatRangeOption("surfaceNoiseScale", 0.0f, 1.0f),
                    screen.booleanOption("generateBeaches"),
                    screen.booleanOption("generateSandstone"),
                    screen.booleanOption("erosion"),
                    screen.booleanOption("gravelOceanBed")
                );
            }
        );

        registryHandler.register(
            INFDEV_227_STRUCTURES.id,
            (screen, options) -> {
                addAll(
                    options,
                    screen.booleanOption("brickPyramids"),
                    screen.booleanOption("obsidianWalls")
                );
            }
        );

        registryHandler.register(
            FINITE_LEVEL_PROPERTIES.id,
            (screen, options) -> {
                int minY = -64;
                int maxY = 320;
                if (screen instanceof ModernBetaGraphicalProviderSettingsScreen providerSettingsScreen) {
                    minY = providerSettingsScreen.worldMinY;
                    maxY = providerSettingsScreen.worldMaxY;
                }

                addAll(
                    options,
                    screen.selectionOption("type", IndevType::values),
                    screen.selectionOption("theme", IndevTheme::values),
                    screen.intRangeOption("width", 64, 1024, 64),
                    screen.intRangeOption("length", 64, 1024, 64),
                    screen.intRangeOption("height", 64, maxY - minY, 64)
                );
            }
        );

        registryHandler.register(
            FINITE_CAVE_GENERATION.id,
            (screen, options) -> {
                addAll(
                    options,
                    screen.booleanOption("useCaves"),
                    screen.booleanOption("use14aCaves"),
                    screen.intRangeOption("rarity", 1024, 40960, 1024),
                    screen.floatRangeOption("radius", 0.01f, 5.0f),
                    screen.floatRangeOption("length", 0.0f, 500.0f)
                );
            }
        );

        registryHandler.register(
            FINITE_NOISE.id,
            (screen, options) -> {
                addAll(
                    options,
                    screen.floatRangeOption("heightNoiseScale", 0.01f, 10.0f),
                    screen.floatRangeOption("selectorScale", 0.01f, 10.0f),
                    screen.floatRangeOption("minHeightDamp", 0.01f, 25.0f),
                    screen.floatRangeOption("minHeightBoost", -50.0f, 50.0f),
                    screen.floatRangeOption("maxHeightDamp", 0.01f, 25.0f),
                    screen.floatRangeOption("maxHeightBoost", -50.0f, 50.0f),
                    screen.intRangeOption("selectorOctaves", 1, 16),
                    screen.floatRangeOption("heightUnderDamp", 0.01f, 5.0f)
                );
            }
        );

        registryHandler.register(
            FINITE_BEACHES.id,
            (screen, options) -> {
                addAll(
                    options,
                    screen.floatRangeOption("sandThreshold", -32.0f, 32.0f),
                    screen.booleanOption("sandUnderAir"),
                    screen.booleanOption("sandUnderFluid"),
                    screen.floatRangeOption("gravelThreshold", -32.0f, 32.0f),
                    screen.booleanOption("gravelUnderAir"),
                    screen.booleanOption("gravelUnderFluid"),
                    screen.booleanOption("prioritizeGravelBeaches")
                );
            }
        );

        registryHandler.register(
            FINITE_POOLS.id,
            (screen, options) -> {
                addAll(
                    options,
                    screen.intRangeOption("waterRarity", 1000, 50000, 1000),
                    screen.intRangeOption("lavaRarity", 1000, 50000, 1000),
                    screen.booleanOption("uniformLavaHeights")
                );
            }
        );

        registryHandler.register(
            SPAWN_INDEV_HOUSE.id,
            (screen, options) -> {
                options.addBig(screen.booleanOption("self"));
            }
        );

        registryHandler.register(
            ISLES_PROPERTIES.id,
            (screen, options) -> {
                addAll(
                    options,
                    screen.booleanOption("useIslands"),
                    screen.booleanOption("useOuterIslands"),
                    screen.floatRangeOption("oceanSlideTarget", -1000.0f, 0.0f),
                    screen.selectionOption("centerIslandShape", IslandShape::values),
                    screen.intRangeOption("centerIslandRadius", 1, 100),
                    screen.intRangeOption("centerIslandFalloffDistance", 1, 100),
                    screen.intRangeOption("centerOceanRadius", 1, 100),
                    screen.intRangeOption("centerOceanFalloffDistance", 1, 100),
                    screen.floatRangeOption("outerIslandNoiseScale", 0.01f, 5000.0f),
                    screen.floatRangeOption("outerIslandNoiseOffset", -1.0f, 1.0f)
                );
            }
        );

        registryHandler.register(
            WORLD_BORDER.id,
            (screen, options) -> {
                int minY = -64;
                int maxY = 320;
                if (screen instanceof ModernBetaGraphicalProviderSettingsScreen providerSettingsScreen) {
                    minY = providerSettingsScreen.worldMinY;
                    maxY = providerSettingsScreen.worldMaxY;
                }

                addAll(
                    options,
                    screen.booleanOption("enabled"),
                    screen.intRangeOption("width", 16, 1024, 16),
                    screen.selectionOption("centerType", WorldBorderLocation.CenterType::values),
                    screen.selectionOption("falloffType", WorldBorderLocation.FalloffType::values),
                    screen.intRangeOption("groundLevel", minY, maxY)
                );
            }
        );

        registryHandler.register(
            SINGLE_BIOME.id,
            (screen, options) -> {
                options.addBig(screen.biomeOption("self", false));
            }
        );

        registryHandler.register(
            CLIMATE_SCALE.id,
            (screen, options) -> {
                options.addBig(screen.floatRangeOption("temp", 0.001f, 1.0f));
                options.addBig(screen.floatRangeOption("rain", 0.001f, 1.0f));
                options.addBig(screen.floatRangeOption("detail", 0.001f, 1.0f));
                options.addBig(screen.floatRangeOption("weird", 0.001f, 1.0f));
            }
        );

        registryHandler.register(
            CLIMATE_MAPPINGS.id,
            (screen, options) -> {
                String[] climateMappingTargets = {
                    "desert", "forest", "ice_desert", "plains", "rainforest", "savanna", "shrubland", "seasonal_forest",
                    "swampland", "taiga", "tundra"
                };

                for (String target : climateMappingTargets) {
                    options.addBig(screen.headerOption(screen.getText(target).withStyle(ChatFormatting.BOLD)));
                    addAll(
                        options,
                        screen.headerOption(screen.getText("biome")),
                        screen.biomeOption(target + ".biome", false),
                        screen.headerOption(screen.getText("oceanBiome")),
                        screen.biomeOption(target + ".oceanBiome", false),
                        screen.headerOption(screen.getText("deepOceanBiome")),
                        screen.biomeOption(target + ".deepOceanBiome", false)
                    );
                }
            }
        );

        registryHandler.register(
            CLIMATE_DISTRIBUTION.id,
            (screen, options) -> {
                addAll(
                    options,
                    screen.booleanOption("fuzzyGrass"),
                    screen.booleanOption("smoothBorders")
                );
            }
        );

        registryHandler.register(
            TEMPERATURE_HEIGHT_SCALING.id,
            (screen, options) -> {
                options.addBig(
                    screen.selectionOption("self", TemperatureHeightScaling::values)
                );
            }
        );

        registryHandler.register(
            VORONOI_POINTS.id,
            (screen, options) -> {
                options.addBig(screen.listEditButton(
                    screen.getText("self"),
                    "self",
                    Tag.TAG_COMPOUND,
                    VoronoiPointBiomeListScreen::new
                ));
            }
        );

        registryHandler.register(
            FRACTAL_LAYERS.id,
            (screen, options) -> {
                options.addBig(screen.headerOption(
                    screen.getText("self").withStyle(ChatFormatting.RED, ChatFormatting.BOLD)));
            }
        );

        registryHandler.register(
            CAVE_BIOME_VORONOI.id,
            (screen, options) -> {
                int minY = -64;
                int maxY = 320;
                if (screen instanceof ModernBetaGraphicalProviderSettingsScreen providerSettingsScreen) {
                    minY = providerSettingsScreen.worldMinY;
                    maxY = providerSettingsScreen.worldMaxY;
                }

                addAll(
                    options,
                    screen.floatRangeOption("horizontalScale", 0.001F, 100.0F),
                    screen.floatRangeOption("verticalScale", 0.001F, 100.0F),
                    screen.intRangeOption("depthMinY", minY, maxY),
                    screen.intRangeOption("depthMaxY", minY, maxY)
                );
                options.addBig(screen.listEditButton(
                    screen.getText("points"),
                    "points",
                    Tag.TAG_COMPOUND,
                    VoronoiPointCaveBiomeListScreen::new
                ));
            }
        );

        registryHandler.register(CONFIG_BETA_CLIMATIC_COLORS.id, ModernBetaClientBuiltInProviders::addClimaticColorOptions);
        registryHandler.register(CONFIG_PE_CLIMATIC_COLORS.id, ModernBetaClientBuiltInProviders::addClimaticColorOptions);
        registryHandler.register(CONFIG_BETA_FRACTAL_CLIMATIC_COLORS.id, ModernBetaClientBuiltInProviders::addClimaticColorOptions);

        registryHandler.register(
            CONFIG_BIOME_PREVIEW_COLORS.id,
            (screen, options) -> {
                options.addBig(screen.mapEditButton(
                    screen.getText("self"),
                    "self",
                    ExtendedBiomeIdToColorMapScreen::new
                ));
            }
        );

        registryHandler.register(
            CONFIG_MISCELLANEOUS.id,
            (screen, options) -> {
                options.addBig(screen.booleanOption("oldFogColorWeighting"));
                addAll(
                    options,
                    screen.headerOption(screen.getText("defaultSettingsPreset")),
                    screen.extendedBiomeIdOption("defaultSettingsPreset")
                );
            }
        );
    }

    private static void addClimaticColorOptions(ModernBetaGraphicalCompoundSettingsScreen screen, OptionsList options) {
        addAll(
            options,
            screen.booleanOption("sky"),
            screen.booleanOption("vegetation"),
            screen.booleanOption("water")
        );
    }
}
