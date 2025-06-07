package mod.bluestaggo.modernerbeta;

import mod.bluestaggo.modernerbeta.tags.ModernBetaBiomeTags;
import net.minecraft.util.Identifier;

public final class ModernBetaBuiltInTypes {
    public enum SettingsComponentType {
        PRESET("preset"),
        PROVIDER("provider"),
        DEEPSLATE_GENERATION("deepslate_generation"),
        USE_SURFACE_RULES("use_surface_rules"),
        SEA_LEVEL_OFFSET("sea_level_offset"),
        CAVE_GENERATION("cave_generation"),
        NOISE_SCALE("noise_scale"),
        NOISE_SLIDE("noise_slide"),
        FORCED_BIOME_HEIGHT("forced_biome_height"),
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
        USE_OCEAN_BIOMES("use_ocean_biomes"),
        CAVE_BIOME_VORONOI("cave_biome_voronoi"),
        ;

        public final Identifier id;

        SettingsComponentType(String id) {
            this.id = ModernerBeta.createId(id);
        }
    }

    public enum Chunk {
        BETA("beta"),
        SKYLANDS("skylands"),
        ALPHA("alpha"),
        INFDEV_611("infdev_611"),
        INFDEV_420("infdev_420"),
        INFDEV_415("infdev_415"),
        INFDEV_227("infdev_227"),
        INDEV("indev"),
        CLASSIC_0_30("classic_0_30"),
        PE("pe"),
        EARLY_RELEASE("early_release"),
        MAJOR_RELEASE("major_release"),
        ;

        public final Identifier id;
        
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

        public final Identifier id;
        
        Biome(String id) { this.id = ModernerBeta.createId(id); }
    }
    
    public enum CaveBiome {
        NONE("none"),
        SINGLE("single"),
        VORONOI("voronoi"),
        ;

        public final Identifier id;
        
        CaveBiome(String id) {
            this.id = ModernerBeta.createId(id);
        }
    }
    
    public enum SurfaceConfig {
        SAND(ModernBetaBiomeTags.SURFACE_CONFIG_SAND.id().getPath()),
        RED_SAND(ModernBetaBiomeTags.SURFACE_CONFIG_RED_SAND.id().getPath()),
        BADLANDS(ModernBetaBiomeTags.SURFACE_CONFIG_BADLANDS.id().getPath()),
        NETHER(ModernBetaBiomeTags.SURFACE_CONFIG_NETHER.id().getPath()),
        WARPED_NYLIUM(ModernBetaBiomeTags.SURFACE_CONFIG_WARPED_NYLIUM.id().getPath()),
        CRIMSON_NYLIUM(ModernBetaBiomeTags.SURFACE_CONFIG_CRIMSON_NYLIUM.id().getPath()),
        BASALT(ModernBetaBiomeTags.SURFACE_CONFIG_BASALT.id().getPath()),
        SOUL_SOIL(ModernBetaBiomeTags.SURFACE_CONFIG_SOUL_SOIL.id().getPath()),
        THEEND(ModernBetaBiomeTags.SURFACE_CONFIG_END.id().getPath()),
        GRASS(ModernBetaBiomeTags.SURFACE_CONFIG_GRASS.id().getPath()),
        MUD(ModernBetaBiomeTags.SURFACE_CONFIG_MUD.id().getPath()),
        MYCELIUM(ModernBetaBiomeTags.SURFACE_CONFIG_MYCELIUM.id().getPath()),
        PODZOL(ModernBetaBiomeTags.SURFACE_CONFIG_PODZOL.id().getPath()),
        STONE(ModernBetaBiomeTags.SURFACE_CONFIG_STONE.id().getPath()),
        SNOW(ModernBetaBiomeTags.SURFACE_CONFIG_SNOW.id().getPath()),
        SNOW_DIRT(ModernBetaBiomeTags.SURFACE_CONFIG_SNOW_DIRT.id().getPath()),
        SNOW_PACKED_ICE(ModernBetaBiomeTags.SURFACE_CONFIG_SNOW_PACKED_ICE.id().getPath()),
        SNOW_STONE(ModernBetaBiomeTags.SURFACE_CONFIG_SNOW_STONE.id().getPath()),
        ;
        
        public final Identifier id;
        
        SurfaceConfig(String id) {
            this.id = ModernerBeta.createId(id);
        }
    }

    public enum HeightConfig {
        HEIGHT_CONFIG_DEFAULT(ModernBetaBiomeTags.HEIGHT_CONFIG_DEFAULT.id().getPath()),
        HEIGHT_CONFIG_OCEAN(ModernBetaBiomeTags.HEIGHT_CONFIG_OCEAN.id().getPath()),
        HEIGHT_CONFIG_DESERT(ModernBetaBiomeTags.HEIGHT_CONFIG_DESERT.id().getPath()),
        HEIGHT_CONFIG_EXTREME_HILLS(ModernBetaBiomeTags.HEIGHT_CONFIG_EXTREME_HILLS.id().getPath()),
        HEIGHT_CONFIG_BETA_HILLS(ModernBetaBiomeTags.HEIGHT_CONFIG_BETA_HILLS.id().getPath()),
        HEIGHT_CONFIG_TAIGA(ModernBetaBiomeTags.HEIGHT_CONFIG_TAIGA.id().getPath()),
        HEIGHT_CONFIG_SWAMPLAND(ModernBetaBiomeTags.HEIGHT_CONFIG_SWAMPLAND.id().getPath()),
        HEIGHT_CONFIG_RIVER(ModernBetaBiomeTags.HEIGHT_CONFIG_RIVER.id().getPath()),
        HEIGHT_CONFIG_MOUNTAINS(ModernBetaBiomeTags.HEIGHT_CONFIG_MOUNTAINS.id().getPath()),
        HEIGHT_CONFIG_MUSHROOM_ISLAND(ModernBetaBiomeTags.HEIGHT_CONFIG_MUSHROOM_ISLAND.id().getPath()),
        HEIGHT_CONFIG_MUSHROOM_ISLAND_SHORE(ModernBetaBiomeTags.HEIGHT_CONFIG_MUSHROOM_ISLAND_SHORE.id().getPath()),
        HEIGHT_CONFIG_BEACH(ModernBetaBiomeTags.HEIGHT_CONFIG_BEACH.id().getPath()),
        HEIGHT_CONFIG_HILLS(ModernBetaBiomeTags.HEIGHT_CONFIG_HILLS.id().getPath()),
        HEIGHT_CONFIG_SHORT_HILLS(ModernBetaBiomeTags.HEIGHT_CONFIG_SHORT_HILLS.id().getPath()),
        HEIGHT_CONFIG_EXTREME_HILLS_EDGE(ModernBetaBiomeTags.HEIGHT_CONFIG_EXTREME_HILLS_EDGE.id().getPath()),
        HEIGHT_CONFIG_JUNGLE(ModernBetaBiomeTags.HEIGHT_CONFIG_JUNGLE.id().getPath()),
        HEIGHT_CONFIG_JUNGLE_HILLS(ModernBetaBiomeTags.HEIGHT_CONFIG_JUNGLE_HILLS.id().getPath()),
        HEIGHT_CONFIG_PLATEAU(ModernBetaBiomeTags.HEIGHT_CONFIG_PLATEAU.id().getPath()),
        HEIGHT_CONFIG_SWAMPLAND_HILLS(ModernBetaBiomeTags.HEIGHT_CONFIG_SWAMPLAND_HILLS.id().getPath()),
        HEIGHT_CONFIG_PLATEAU_HILL(ModernBetaBiomeTags.HEIGHT_CONFIG_PLATEAU_HILL.id().getPath()),
        HEIGHT_CONFIG_DEEP_OCEAN(ModernBetaBiomeTags.HEIGHT_CONFIG_DEEP_OCEAN.id().getPath()),
        ;

        public final Identifier id;

        HeightConfig(String id) {
            this.id = ModernerBeta.createId(id);
        }
    }
    
    public enum NoisePostProcessor {
        NONE("none"),
        ;
        
        public final Identifier id;
        
        NoisePostProcessor(String id) {
            this.id = ModernerBeta.createId(id);
        }
    }
    
    public enum BlockSource {
        DEEPSLATE("deepslate"),
        ;
        
        public final Identifier id;
        
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
        ;
        
        public final Identifier id;
        
        Preset(String id) {
            this.id = ModernerBeta.createId(id);
        }
    }

    public enum LayerOutput {
        BIOME("biome"),
        ;

        public final Identifier id;

        LayerOutput(String id) {
            this.id = ModernerBeta.createId(id);
        }
    }
}
