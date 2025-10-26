package mod.bluestaggo.modernerbeta;

import mod.bluestaggo.modernerbeta.tags.ModernBetaBiomeTags;
import net.minecraft.resources.ResourceLocation;

public final class ModernBetaBuiltInTypes {
    public enum SettingsComponentType {
        PRESET("preset"),
        PROVIDER("provider"),
        DEEPSLATE_GENERATION("deepslate_generation"),
        USE_SURFACE_RULES("use_surface_rules"),
        SEA_LEVEL_OFFSET("sea_level_offset"),
        CAVE_GENERATION("cave_generation"),
        NOISE_SETTINGS("noise_settings"),
        NOISE_3D_SETTINGS("noise_3d_settings"),
        NOISE_SCALE("noise_scale"),
        NOISE_SLIDE("noise_slide"),
        NOISE_LANDMASS("noise_landmass"),
        FORCED_BIOME_HEIGHT("forced_biome_height"),
        SURFACE_PROPERTIES("surface_properties"),
        INFDEV_227_STRUCTURES("infdev_227_structures"),
        FINITE_LEVEL_PROPERTIES("finite_level_properties"),
        FINITE_CAVE_GENERATION("finite_cave_generation"),
        FINITE_NOISE("finite_noise"),
        FINITE_BEACHES("finite_beaches"),
        FINITE_POOLS("finite_pools"),
        SPAWN_INDEV_HOUSE("spawn_indev_house"),
        ISLES_PROPERTIES("isles_properties"),
        SINGLE_BIOME("single_biome"),
        CLIMATE_SCALE("climate_scale"),
        CLIMATE_MAPPINGS("climate_mappings"),
        CLIMATE_DISTRIBUTION("climate_distribution"),
        VORONOI_POINTS("voronoi_points"),
        FRACTAL_LAYERS("fractal_layers"),
        USE_32BIT_LAYER_SEED("use_32bit_layer_seed"),
        USE_OCEAN_BIOMES("use_ocean_biomes"),
        TEMPERATURE_HEIGHT_SCALING("temperature_height_scaling"),
        CAVE_BIOME_VORONOI("cave_biome_voronoi"),
        CONFIG_BETA_CLIMATIC_COLORS("config/beta_climatic_colors"),
        CONFIG_PE_CLIMATIC_COLORS("config/pe_climatic_colors"),
        CONFIG_BETA_FRACTAL_CLIMATIC_COLORS("config/beta_fractal_climatic_colors"),
        CONFIG_BIOME_PREVIEW_COLORS("config/biome_preview_colors"),
        CONFIG_MISCELLANEOUS("config/miscellaneous"),
        ;

        public final ResourceLocation id;

        SettingsComponentType(String id) {
            this.id = ModernerBeta.createId(id);
        }
    }

    public enum Chunk {
        SKYLANDS("skylands"),
        INFDEV_415("infdev_415"),
        INFDEV_227("infdev_227"),
        INDEV("indev"),
        CLASSIC_0_30("classic_0_30"),
        PE("pe"),
        NOISE_3D("noise_3d"),
        EARLY_BEDROCK("early_bedrock")
        ;

        public final ResourceLocation id;
        
        Chunk(String id) {
            this.id = ModernerBeta.createId(id);
        }
    }
    
    public enum Biome {
        BETA("beta"),
        SINGLE("single"),
        PE("pe"),
        VORONOI("voronoi"),
        FRACTAL("fractal"),
        BETA_FRACTAL("beta_fractal"),
        ;

        public final ResourceLocation id;
        
        Biome(String id) { this.id = ModernerBeta.createId(id); }
    }
    
    public enum CaveBiome {
        NONE("none"),
        SINGLE("single"),
        VORONOI("voronoi"),
        ;

        public final ResourceLocation id;
        
        CaveBiome(String id) {
            this.id = ModernerBeta.createId(id);
        }
    }
    
    public enum SurfaceConfig {
        SAND(ModernBetaBiomeTags.SURFACE_CONFIG_SAND.location().getPath()),
        RED_SAND(ModernBetaBiomeTags.SURFACE_CONFIG_RED_SAND.location().getPath()),
        BADLANDS(ModernBetaBiomeTags.SURFACE_CONFIG_BADLANDS.location().getPath()),
        NETHER(ModernBetaBiomeTags.SURFACE_CONFIG_NETHER.location().getPath()),
        WARPED_NYLIUM(ModernBetaBiomeTags.SURFACE_CONFIG_WARPED_NYLIUM.location().getPath()),
        CRIMSON_NYLIUM(ModernBetaBiomeTags.SURFACE_CONFIG_CRIMSON_NYLIUM.location().getPath()),
        BASALT(ModernBetaBiomeTags.SURFACE_CONFIG_BASALT.location().getPath()),
        SOUL_SOIL(ModernBetaBiomeTags.SURFACE_CONFIG_SOUL_SOIL.location().getPath()),
        THEEND(ModernBetaBiomeTags.SURFACE_CONFIG_END.location().getPath()),
        GRASS(ModernBetaBiomeTags.SURFACE_CONFIG_GRASS.location().getPath()),
        MUD(ModernBetaBiomeTags.SURFACE_CONFIG_MUD.location().getPath()),
        MYCELIUM(ModernBetaBiomeTags.SURFACE_CONFIG_MYCELIUM.location().getPath()),
        PODZOL(ModernBetaBiomeTags.SURFACE_CONFIG_PODZOL.location().getPath()),
        STONE(ModernBetaBiomeTags.SURFACE_CONFIG_STONE.location().getPath()),
        SNOW(ModernBetaBiomeTags.SURFACE_CONFIG_SNOW.location().getPath()),
        SNOW_DIRT(ModernBetaBiomeTags.SURFACE_CONFIG_SNOW_DIRT.location().getPath()),
        SNOW_PACKED_ICE(ModernBetaBiomeTags.SURFACE_CONFIG_SNOW_PACKED_ICE.location().getPath()),
        SNOW_STONE(ModernBetaBiomeTags.SURFACE_CONFIG_SNOW_STONE.location().getPath()),
        ;
        
        public final ResourceLocation id;
        
        SurfaceConfig(String id) {
            this.id = ModernerBeta.createId(id);
        }
    }

    public enum HeightConfig {
        HEIGHT_CONFIG_DEFAULT(ModernBetaBiomeTags.HEIGHT_CONFIG_DEFAULT.location().getPath()),
        HEIGHT_CONFIG_OCEAN(ModernBetaBiomeTags.HEIGHT_CONFIG_OCEAN.location().getPath()),
        HEIGHT_CONFIG_DESERT(ModernBetaBiomeTags.HEIGHT_CONFIG_DESERT.location().getPath()),
        HEIGHT_CONFIG_EXTREME_HILLS(ModernBetaBiomeTags.HEIGHT_CONFIG_EXTREME_HILLS.location().getPath()),
        HEIGHT_CONFIG_BETA_HILLS(ModernBetaBiomeTags.HEIGHT_CONFIG_BETA_HILLS.location().getPath()),
        HEIGHT_CONFIG_TAIGA(ModernBetaBiomeTags.HEIGHT_CONFIG_TAIGA.location().getPath()),
        HEIGHT_CONFIG_SWAMPLAND(ModernBetaBiomeTags.HEIGHT_CONFIG_SWAMPLAND.location().getPath()),
        HEIGHT_CONFIG_RIVER(ModernBetaBiomeTags.HEIGHT_CONFIG_RIVER.location().getPath()),
        HEIGHT_CONFIG_MOUNTAINS(ModernBetaBiomeTags.HEIGHT_CONFIG_MOUNTAINS.location().getPath()),
        HEIGHT_CONFIG_MUSHROOM_ISLAND(ModernBetaBiomeTags.HEIGHT_CONFIG_MUSHROOM_ISLAND.location().getPath()),
        HEIGHT_CONFIG_MUSHROOM_ISLAND_SHORE(ModernBetaBiomeTags.HEIGHT_CONFIG_MUSHROOM_ISLAND_SHORE.location().getPath()),
        HEIGHT_CONFIG_BEACH(ModernBetaBiomeTags.HEIGHT_CONFIG_BEACH.location().getPath()),
        HEIGHT_CONFIG_HILLS(ModernBetaBiomeTags.HEIGHT_CONFIG_HILLS.location().getPath()),
        HEIGHT_CONFIG_SHORT_HILLS(ModernBetaBiomeTags.HEIGHT_CONFIG_SHORT_HILLS.location().getPath()),
        HEIGHT_CONFIG_EXTREME_HILLS_EDGE(ModernBetaBiomeTags.HEIGHT_CONFIG_EXTREME_HILLS_EDGE.location().getPath()),
        HEIGHT_CONFIG_JUNGLE(ModernBetaBiomeTags.HEIGHT_CONFIG_JUNGLE.location().getPath()),
        HEIGHT_CONFIG_JUNGLE_HILLS(ModernBetaBiomeTags.HEIGHT_CONFIG_JUNGLE_HILLS.location().getPath()),
        HEIGHT_CONFIG_PLATEAU(ModernBetaBiomeTags.HEIGHT_CONFIG_PLATEAU.location().getPath()),
        HEIGHT_CONFIG_SWAMPLAND_HILLS(ModernBetaBiomeTags.HEIGHT_CONFIG_SWAMPLAND_HILLS.location().getPath()),
        HEIGHT_CONFIG_PLATEAU_HILL(ModernBetaBiomeTags.HEIGHT_CONFIG_PLATEAU_HILL.location().getPath()),
        HEIGHT_CONFIG_DEEP_OCEAN(ModernBetaBiomeTags.HEIGHT_CONFIG_DEEP_OCEAN.location().getPath()),
        ;

        public final ResourceLocation id;

        HeightConfig(String id) {
            this.id = ModernerBeta.createId(id);
        }
    }
    
    public enum BlockSource {
        DEEPSLATE("deepslate"),
        ;
        
        public final ResourceLocation id;
        
        BlockSource(String id) {
            this.id = ModernerBeta.createId(id);
        }
    }
    
    public enum Preset {
        BETA_1_7_3("beta"),
        BETA_1_1_02("beta_1_1_02"),
        SKYLANDS("skylands"),
        ALPHA_1_1_2_01("alpha"),
        INFDEV_611("infdev_611"),
        INFDEV_420("infdev_420"),
        INFDEV_415("infdev_415"),
        INFDEV_325("infdev_325"),
        INFDEV_227("infdev_227"),
        INDEV("indev"),
        CLASSIC_0_30("classic_0_30"),
        CLASSIC_0_0_14A_08("classic_0_0_14a_08"),
        PE("pe"),
        BETA_1_8_1("beta_1_8_1"),
        BETA_1_9_PRE_3("beta_1_9_pre_3"),
        RELEASE_1_0_0("release_1_0_0"),
        RELEASE_1_1("release_1_1"),
        RELEASE_1_2_5("release_1_2_5"),
        RELEASE_1_6_4("release_1_6_4"),
        RELEASE_1_12_2("release_1_12_2"),
        RELEASE_1_17_1("release_1_17_1"),
        BEDROCK_1_2("bedrock_1_2"),
        BEDROCK_1_17("bedrock_1_17"),
        BETA_SKYLANDS("beta_skylands"),
        BETA_ISLES("beta_isles"),
        BETA_WATER_WORLD("beta_water_world"),
        BETA_ISLE_LAND("beta_isle_land"),
        BETA_CAVE_DELIGHT("beta_cave_delight"),
        BETA_MOUNTAIN_MADNESS("beta_mountain_madness"),
        BETA_DROUGHT("beta_drought"),
        BETA_CAVE_CHAOS("beta_cave_chaos"),
        BETA_LARGE_BIOMES("beta_large_biomes"),
        BETA_XBOX_LEGACY("beta_xbox_legacy"),
        BETA_SURVIVAL_ISLAND("beta_survival_island"),
        BETA_VANILLA("beta_vanilla"),
        LEGACY_CONSOLE_CLASSIC("legacy_console_classic"),
        LEGACY_CONSOLE_SMALL("legacy_console_small"),
        LEGACY_CONSOLE_MEDIUM("legacy_console_medium"),
        LEGACY_CONSOLE_LARGE("legacy_console_large"),
        RELEASE_HYBRID("release_hybrid"),
        SNOW_AINT_SNOWIER("snow_aint_snowier"),
        ALPHA_WINTER("alpha_winter"),
        INDEV_PARADISE("indev_paradise"),
        INDEV_WOODS("indev_woods"),
        INDEV_HELL("indev_hell"),
        WATER_WORLD("water_world"),
        ISLE_LAND("isle_land"),
        CAVE_DELIGHT("cave_delight"),
        MOUNTAIN_MADNESS("mountain_madness"),
        DROUGHT("drought"),
        CAVE_CHAOS("cave_chaos"),
        BETA_1_8_1_LARGE_BIOMES("beta_1_8_1_large_biomes"),
        BETA_1_9_PRE_3_LARGE_BIOMES("beta_1_9_pre_3_large_biomes"),
        RELEASE_1_0_0_LARGE_BIOMES("release_1_0_0_large_biomes"),
        RELEASE_1_1_LARGE_BIOMES("release_1_1_large_biomes"),
        RELEASE_1_2_5_LARGE_BIOMES("release_1_2_5_large_biomes"),
        RELEASE_1_6_4_LARGE_BIOMES("release_1_6_4_large_biomes"),
        RELEASE_1_12_2_LARGE_BIOMES("release_1_12_2_large_biomes"),
        RELEASE_1_17_1_LARGE_BIOMES("release_1_17_1_large_biomes"),
        RELEASE_HYBRID_LARGE_BIOMES("release_hybrid_large_biomes"),
        SNOW_AINT_SNOWIER_LARGE_BIOMES("snow_aint_snowier_large_biomes"),
        BETA_1_8_1_AMPLIFIED("beta_1_8_1_amplified"),
        BETA_1_9_PRE_3_AMPLIFIED("beta_1_9_pre_3_amplified"),
        RELEASE_1_0_0_AMPLIFIED("release_1_0_0_amplified"),
        RELEASE_1_1_AMPLIFIED("release_1_1_amplified"),
        RELEASE_1_2_5_AMPLIFIED("release_1_2_5_amplified"),
        RELEASE_1_6_4_AMPLIFIED("release_1_6_4_amplified"),
        RELEASE_1_12_2_AMPLIFIED("release_1_12_2_amplified"),
        RELEASE_1_17_1_AMPLIFIED("release_1_17_1_amplified"),
        RELEASE_HYBRID_AMPLIFIED("release_hybrid_amplified"),
        SNOW_AINT_SNOWIER_AMPLIFIED("snow_aint_snowier_amplified"),
        ;
        
        public final ResourceLocation id;
        
        Preset(String id) {
            this.id = ModernerBeta.createId(id);
        }
    }

    public enum PresetCategory {
        BETA("beta"),
        ALPHA_INDEV("alpha_infdev"),
        FINITE("finite"),
        EARLY_RELEASE("early_release"),
        EARLY_RELEASE_LARGE_BIOMES("early_release_large_biomes"),
        EARLY_RELEASE_AMPLIFIED("early_release_amplified"),
        MAJOR_RELEASE("major_release"),
        BETA_CUSTOM("beta_custom"),
        RELEASE_CUSTOM("release_custom");

        public final ResourceLocation id;

        PresetCategory(String id) {
            this.id = ModernerBeta.createId(id);
        }
    }

    public enum LayerOutput {
        BIOME("biome"),
        ;

        public final ResourceLocation id;

        LayerOutput(String id) {
            this.id = ModernerBeta.createId(id);
        }
    }
}
