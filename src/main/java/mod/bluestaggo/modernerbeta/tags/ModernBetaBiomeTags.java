package mod.bluestaggo.modernerbeta.tags;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ModernBetaBiomeTags {
    public static final TagKey<Biome> IS_MODERN_BETA = keyOf("is_modern_beta");
    public static final TagKey<Biome> IS_EARLY_RELEASE = keyOf("is_early_release");
    public static final TagKey<Biome> IS_LATE_BETA = keyOf("is_late_beta");
    public static final TagKey<Biome> IS_BETA = keyOf("is_beta");
    public static final TagKey<Biome> IS_PE = keyOf("is_pe");
    public static final TagKey<Biome> IS_ALPHA = keyOf("is_alpha");
    public static final TagKey<Biome> IS_INFDEV = keyOf("is_infdev");
    public static final TagKey<Biome> IS_INDEV = keyOf("is_indev");

    public static final TagKey<Biome> IS_FOREST = keyOf("is_forest");
    public static final TagKey<Biome> IS_SEASONAL_FOREST = keyOf("is_seasonal_forest");
    public static final TagKey<Biome> IS_RAINFOREST = keyOf("is_rainforest");
    public static final TagKey<Biome> IS_DESERT = keyOf("is_desert");
    public static final TagKey<Biome> IS_PLAINS = keyOf("is_plains");
    public static final TagKey<Biome> IS_SHRUBLAND = keyOf("is_shrubland");
    public static final TagKey<Biome> IS_SAVANNA = keyOf("is_savanna");
    public static final TagKey<Biome> IS_SWAMP = keyOf("is_swamp");
    public static final TagKey<Biome> IS_TAIGA = keyOf("is_taiga");
    public static final TagKey<Biome> IS_TUNDRA = keyOf("is_tundra");
    public static final TagKey<Biome> IS_OCEAN = keyOf("is_ocean");
    public static final TagKey<Biome> IS_EXTREME_HILLS = keyOf("is_extreme_hills");

    public static final TagKey<Biome> IS_RELEASE_SPAWN = keyOf("is_early_release_spawn");
    public static final TagKey<Biome> HAS_EARLY_RELEASE_SWAMP_COLORS = keyOf("has_early_release_swamp_colors");

    public static final TagKey<Biome> INDEV_STRONGHOLD_HAS_STRUCTURE = keyOf("has_structure/indev_stronghold");
    public static final TagKey<Biome> OCEAN_SHRINE_HAS_STRUCTURE = keyOf("has_structure/ocean_shrine");

    public static final TagKey<Biome> SURFACE_CONFIG_SAND = keyOf("surface_config/sand");

    public static final TagKey<Biome> HEIGHT_CONFIG_DEFAULT = keyOf("height_config/default");
    public static final TagKey<Biome> HEIGHT_CONFIG_OCEAN = keyOf("height_config/ocean");
    public static final TagKey<Biome> HEIGHT_CONFIG_DESERT = keyOf("height_config/desert");
    public static final TagKey<Biome> HEIGHT_CONFIG_EXTREME_HILLS = keyOf("height_config/extreme_hills");
    public static final TagKey<Biome> HEIGHT_CONFIG_BETA_HILLS = keyOf("height_config/beta_hills");
    public static final TagKey<Biome> HEIGHT_CONFIG_TAIGA = keyOf("height_config/taiga");
    public static final TagKey<Biome> HEIGHT_CONFIG_SWAMPLAND = keyOf("height_config/swampland");
    public static final TagKey<Biome> HEIGHT_CONFIG_RIVER = keyOf("height_config/river");
    public static final TagKey<Biome> HEIGHT_CONFIG_MOUNTAINS = keyOf("height_config/mountains");
    public static final TagKey<Biome> HEIGHT_CONFIG_MUSHROOM_ISLAND = keyOf("height_config/mushroom_island");
    public static final TagKey<Biome> HEIGHT_CONFIG_MUSHROOM_ISLAND_SHORE = keyOf("height_config/mushroom_island_shore");
    public static final TagKey<Biome> HEIGHT_CONFIG_BEACH = keyOf("height_config/beach");
    public static final TagKey<Biome> HEIGHT_CONFIG_HILLS = keyOf("height_config/hills");
    public static final TagKey<Biome> HEIGHT_CONFIG_SHORT_HILLS = keyOf("height_config/short_hills");
    public static final TagKey<Biome> HEIGHT_CONFIG_EXTREME_HILLS_EDGE = keyOf("height_config/extreme_hills_edge");
    public static final TagKey<Biome> HEIGHT_CONFIG_JUNGLE = keyOf("height_config/jungle");
    public static final TagKey<Biome> HEIGHT_CONFIG_JUNGLE_HILLS = keyOf("height_config/jungle_hills");
    public static final TagKey<Biome> HEIGHT_CONFIG_PLATEAU = keyOf("height_config/plateau");
    public static final TagKey<Biome> HEIGHT_CONFIG_SWAMPLAND_HILLS = keyOf("height_config/swampland_hills");
    public static final TagKey<Biome> HEIGHT_CONFIG_PLATEAU_HILL = keyOf("height_config/plateau_hill");
    public static final TagKey<Biome> HEIGHT_CONFIG_DEEP_OCEAN = keyOf("height_config/deep_ocean");

    private static TagKey<Biome> keyOf(String id) {
        return TagKey.create(Registries.BIOME, ModernerBeta.createId(id));
    }
}
