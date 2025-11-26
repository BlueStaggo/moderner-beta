package mod.bluestaggo.modernerbeta;

import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.tags.ModernBetaBiomeTags;
import net.minecraft.resources.ResourceKey;
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
        INFDEV_227("infdev_227"),
        FINITE_2D("finite_2d"),
        NOISE_3D("noise_3d"),
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

    public enum LayerOutput {
        BIOME("biome"),
        ;

        public final ResourceLocation id;

        LayerOutput(String id) {
            this.id = ModernerBeta.createId(id);
        }
    }
}
