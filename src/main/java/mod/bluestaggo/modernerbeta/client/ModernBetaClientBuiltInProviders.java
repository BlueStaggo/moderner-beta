package mod.bluestaggo.modernerbeta.client;

import mod.bluestaggo.modernerbeta.client.gui.screen.config.*;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.world.chunk.provider.indev.IndevTheme;
import mod.bluestaggo.modernerbeta.world.chunk.provider.indev.IndevType;
import mod.bluestaggo.modernerbeta.world.chunk.provider.island.IslandShape;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Formatting;

import static mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes.SettingsComponentType.*;

@Environment(EnvType.CLIENT)
@SuppressWarnings("unchecked")
public class ModernBetaClientBuiltInProviders {
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

                options.addAll(
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
                options.addSingleOptionEntry(screen.booleanOption("self"));
            }
        );

        registryHandler.register(
            SEA_LEVEL_OFFSET.id,
            (screen, options) -> {
                int minY = -64;
                int maxY = 320;
                if (screen instanceof ModernBetaGraphicalProviderSettingsScreen providerSettingsScreen) {
                    minY = providerSettingsScreen.worldMinY;
                    maxY = providerSettingsScreen.worldMaxY;
                }

                options.addSingleOptionEntry(screen.intRangeOption("self", minY - 64, maxY - 64));
            }
        );

        registryHandler.register(
            CAVE_GENERATION.id,
            (screen, options) -> {
                options.addAll(
                    screen.booleanOption("useCaves"),
                    screen.booleanOption("useFixedCaves"),
                    screen.booleanOption("forceBetaCaves"),
                    screen.booleanOption("forceBetaRavines")
                );
            }
        );

        registryHandler.register(
            NOISE_SCALE.id,
            (screen, options) -> {
                options.addAll(
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
                    screen.floatRangeOption("lowerLimit", 1.0f, 5000.0f)
                );
            }
        );

        registryHandler.register(
            NOISE_SLIDE.id,
            (screen, options) -> {
                options.addAll(
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
            FORCED_BIOME_HEIGHT.id,
            (screen, options) -> {
                options.addSingleOptionEntry(screen.mapEditButton(
                    screen.getText("heightOverrides"),
                    "heightOverrides",
                    ExtendedBiomeIdToHeightConfigMapScreen::new
                ));
                options.addAll(
                    screen.floatRangeOption("depthWeight", 1.0f, 20.0f),
                    screen.floatRangeOption("depthOffset", 0.0f, 20.0f),
                    screen.floatRangeOption("scaleWeight", 1.0f, 20.0f),
                    screen.floatRangeOption("scaleOffset", 0.0f, 20.0f)
                );
            }
        );

        registryHandler.register(
            INFDEV_227_STRUCTURES.id,
            (screen, options) -> {
                options.addAll(
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

                options.addAll(
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
                options.addAll(
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
                options.addAll(
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
                options.addAll(
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
                options.addAll(
                    screen.intRangeOption("waterRarity", 1000, 50000, 1000),
                    screen.intRangeOption("lavaRarity", 1000, 50000, 1000),
                    screen.booleanOption("uniformLavaHeights")
                );
            }
        );

        registryHandler.register(
            SPAWN_INDEV_HOUSE.id,
            (screen, options) -> {
                options.addSingleOptionEntry(screen.booleanOption("self"));
            }
        );

        registryHandler.register(
            ISLES_PROPERTIES.id,
            (screen, options) -> {
                options.addAll(
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
            SINGLE_BIOME.id,
            (screen, options) -> {
                options.addSingleOptionEntry(screen.biomeOption("self", false));
            }
        );

        registryHandler.register(
            CLIMATE_SCALE.id,
            (screen, options) -> {
                options.addSingleOptionEntry(screen.floatRangeOption("temp", 0.001f, 1.0f));
                options.addSingleOptionEntry(screen.floatRangeOption("rain", 0.001f, 1.0f));
                options.addSingleOptionEntry(screen.floatRangeOption("detail", 0.001f, 1.0f));
                options.addSingleOptionEntry(screen.floatRangeOption("weird", 0.001f, 1.0f));
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
                    options.addSingleOptionEntry(screen.headerOption(screen.getText(target).formatted(Formatting.BOLD)));
                    options.addAll(
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
            VORONOI_POINTS.id,
            (screen, options) -> {
                options.addSingleOptionEntry(screen.listEditButton(
                    screen.getText("self"),
                    "self",
                    NbtElement.COMPOUND_TYPE,
                    VoronoiPointBiomeListScreen::new
                ));
            }
        );

        registryHandler.register(
            FRACTAL_LAYERS.id,
            (screen, options) -> {
                options.addSingleOptionEntry(screen.headerOption(
                    screen.getText("self").formatted(Formatting.RED, Formatting.BOLD)));
            }
        );

        registryHandler.register(
            USE_OCEAN_BIOMES.id,
            (screen, options) -> {
                options.addSingleOptionEntry(screen.booleanOption("self"));
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

                options.addAll(
                    screen.floatRangeOption("horizontalScale", 0.001F, 100.0F),
                    screen.floatRangeOption("verticalScale", 0.001F, 100.0F),
                    screen.intRangeOption("depthMinY", minY, maxY),
                    screen.intRangeOption("depthMaxY", minY, maxY)
                );
                options.addSingleOptionEntry(screen.listEditButton(
                    screen.getText("points"),
                    "points",
                    NbtElement.COMPOUND_TYPE,
                    VoronoiPointCaveBiomeListScreen::new
                ));
            }
        );
    }
}
