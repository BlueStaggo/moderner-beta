package mod.bluestaggo.modernerbeta.fabric.data;

import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomes;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
//? if >=1.20.5 {
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
//?} else {
/*import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
 *///?}
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.concurrent.CompletableFuture;

import static mod.bluestaggo.modernerbeta.tags.ModernBetaBiomeTags.*;

@SuppressWarnings("unchecked")
public class ModernBetaTagProviderBiome extends FabricTagProvider<Biome> {
    public ModernBetaTagProviderBiome(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BIOME, registriesFuture);
    }

    @Override
    protected void addTags(Provider provider) {
        this.configureModernBeta(provider);
        this.configureVanilla(provider);
        this.configureConventional(provider);
        this.configureModded(provider);
    }
    
    private void configureModernBeta(Provider provider) {
        /* Modern Beta Biome Tags */
        
        this.builder(IS_MODERN_BETA).add(
            ModernBetaBiomes.BETA_FOREST,
            ModernBetaBiomes.BETA_SHRUBLAND,
            ModernBetaBiomes.BETA_DESERT,
            ModernBetaBiomes.BETA_SAVANNA,
            ModernBetaBiomes.BETA_PLAINS,
            ModernBetaBiomes.BETA_SEASONAL_FOREST,
            ModernBetaBiomes.BETA_RAINFOREST,
            ModernBetaBiomes.BETA_SWAMPLAND,
            ModernBetaBiomes.BETA_TAIGA,
            ModernBetaBiomes.BETA_TUNDRA,
            ModernBetaBiomes.BETA_ICE_DESERT,
            ModernBetaBiomes.BETA_OCEAN,
            ModernBetaBiomes.BETA_LUKEWARM_OCEAN,
            ModernBetaBiomes.BETA_WARM_OCEAN,
            ModernBetaBiomes.BETA_COLD_OCEAN,
            ModernBetaBiomes.BETA_FROZEN_OCEAN,
            ModernBetaBiomes.BETA_SKY,
                
            ModernBetaBiomes.PE_FOREST,
            ModernBetaBiomes.PE_SHRUBLAND,
            ModernBetaBiomes.PE_DESERT,
            ModernBetaBiomes.PE_SAVANNA,
            ModernBetaBiomes.PE_PLAINS,
            ModernBetaBiomes.PE_SEASONAL_FOREST,
            ModernBetaBiomes.PE_RAINFOREST,
            ModernBetaBiomes.PE_SWAMPLAND,
            ModernBetaBiomes.PE_TAIGA,
            ModernBetaBiomes.PE_TUNDRA,
            ModernBetaBiomes.PE_ICE_DESERT,
            ModernBetaBiomes.PE_OCEAN,
            ModernBetaBiomes.PE_LUKEWARM_OCEAN,
            ModernBetaBiomes.PE_WARM_OCEAN,
            ModernBetaBiomes.PE_COLD_OCEAN,
            ModernBetaBiomes.PE_FROZEN_OCEAN,
            
            ModernBetaBiomes.ALPHA,
            ModernBetaBiomes.ALPHA_WINTER,
            
            ModernBetaBiomes.INFDEV_611,
            ModernBetaBiomes.INFDEV_420,
            ModernBetaBiomes.INFDEV_415,
            ModernBetaBiomes.INFDEV_325,
            ModernBetaBiomes.INFDEV_227,

            ModernBetaBiomes.INDEV_NORMAL,
            ModernBetaBiomes.INDEV_HELL,
            ModernBetaBiomes.INDEV_PARADISE,
            ModernBetaBiomes.INDEV_WOODS,

            ModernBetaBiomes.LATE_BETA_EXTREME_HILLS,
            ModernBetaBiomes.LATE_BETA_SWAMPLAND,
            ModernBetaBiomes.LATE_BETA_PLAINS,
            ModernBetaBiomes.LATE_BETA_TAIGA,

            ModernBetaBiomes.EARLY_RELEASE_ICE_PLAINS,
            ModernBetaBiomes.EARLY_RELEASE_SWAMPLAND,
            ModernBetaBiomes.EARLY_RELEASE_EXTREME_HILLS,
            ModernBetaBiomes.EARLY_RELEASE_TAIGA
        );

        this.builder(IS_EARLY_RELEASE).add(
            ModernBetaBiomes.EARLY_RELEASE_ICE_PLAINS,
            ModernBetaBiomes.EARLY_RELEASE_SWAMPLAND,
            ModernBetaBiomes.EARLY_RELEASE_EXTREME_HILLS,
            ModernBetaBiomes.EARLY_RELEASE_TAIGA
        );

        this.builder(IS_LATE_BETA).add(
            ModernBetaBiomes.LATE_BETA_EXTREME_HILLS,
            ModernBetaBiomes.LATE_BETA_SWAMPLAND,
            ModernBetaBiomes.LATE_BETA_PLAINS,
            ModernBetaBiomes.LATE_BETA_TAIGA
        );

        this.builder(IS_BETA).add(
            ModernBetaBiomes.BETA_FOREST,
            ModernBetaBiomes.BETA_SHRUBLAND,
            ModernBetaBiomes.BETA_DESERT,
            ModernBetaBiomes.BETA_SAVANNA,
            ModernBetaBiomes.BETA_PLAINS,
            ModernBetaBiomes.BETA_SEASONAL_FOREST,
            ModernBetaBiomes.BETA_RAINFOREST,
            ModernBetaBiomes.BETA_SWAMPLAND,
            ModernBetaBiomes.BETA_TAIGA,
            ModernBetaBiomes.BETA_TUNDRA,
            ModernBetaBiomes.BETA_ICE_DESERT,
            ModernBetaBiomes.BETA_OCEAN,
            ModernBetaBiomes.BETA_LUKEWARM_OCEAN,
            ModernBetaBiomes.BETA_WARM_OCEAN,
            ModernBetaBiomes.BETA_COLD_OCEAN,
            ModernBetaBiomes.BETA_FROZEN_OCEAN,
            ModernBetaBiomes.BETA_SKY
        );
        
        this.builder(IS_PE).add(
            ModernBetaBiomes.PE_FOREST,
            ModernBetaBiomes.PE_SHRUBLAND,
            ModernBetaBiomes.PE_DESERT,
            ModernBetaBiomes.PE_SAVANNA,
            ModernBetaBiomes.PE_PLAINS,
            ModernBetaBiomes.PE_SEASONAL_FOREST,
            ModernBetaBiomes.PE_RAINFOREST,
            ModernBetaBiomes.PE_SWAMPLAND,
            ModernBetaBiomes.PE_TAIGA,
            ModernBetaBiomes.PE_TUNDRA,
            ModernBetaBiomes.PE_ICE_DESERT,
            ModernBetaBiomes.PE_OCEAN,
            ModernBetaBiomes.PE_LUKEWARM_OCEAN,
            ModernBetaBiomes.PE_WARM_OCEAN,
            ModernBetaBiomes.PE_COLD_OCEAN,
            ModernBetaBiomes.PE_FROZEN_OCEAN
        );

        this.builder(IS_ALPHA).add(
            ModernBetaBiomes.ALPHA,
            ModernBetaBiomes.ALPHA_WINTER
        );

        this.builder(IS_INFDEV).add(
            ModernBetaBiomes.INFDEV_611,
            ModernBetaBiomes.INFDEV_420,
            ModernBetaBiomes.INFDEV_415,
            ModernBetaBiomes.INFDEV_325,
            ModernBetaBiomes.INFDEV_227
        );

        this.builder(IS_INDEV).add(
            ModernBetaBiomes.INDEV_NORMAL,
            ModernBetaBiomes.INDEV_HELL,
            ModernBetaBiomes.INDEV_PARADISE,
            ModernBetaBiomes.INDEV_WOODS
        );
        
        this.builder(IS_FOREST).add(
            ModernBetaBiomes.BETA_FOREST,
            ModernBetaBiomes.PE_FOREST
        );
        
        this.builder(IS_SEASONAL_FOREST).add(
            ModernBetaBiomes.BETA_SEASONAL_FOREST,
            ModernBetaBiomes.PE_SEASONAL_FOREST
        );
        
        this.builder(IS_RAINFOREST).add(
            ModernBetaBiomes.BETA_RAINFOREST,
            ModernBetaBiomes.PE_RAINFOREST
        );

        this.builder(IS_DESERT).add(
            ModernBetaBiomes.BETA_DESERT,
            ModernBetaBiomes.PE_DESERT
        );
        
        this.builder(IS_PLAINS).add(
            ModernBetaBiomes.BETA_PLAINS,
            ModernBetaBiomes.PE_PLAINS,
            ModernBetaBiomes.LATE_BETA_PLAINS
        );
        
        this.builder(IS_SHRUBLAND).add(
            ModernBetaBiomes.BETA_SHRUBLAND,
            ModernBetaBiomes.PE_SHRUBLAND
        );
        
        this.builder(IS_SAVANNA).add(
            ModernBetaBiomes.BETA_SAVANNA,
            ModernBetaBiomes.PE_SAVANNA
        );
        
        this.builder(IS_SWAMP).add(
            ModernBetaBiomes.BETA_SWAMPLAND,
            ModernBetaBiomes.PE_SWAMPLAND,
            ModernBetaBiomes.LATE_BETA_SWAMPLAND,
            ModernBetaBiomes.EARLY_RELEASE_SWAMPLAND
        );
        
        this.builder(IS_TAIGA).add(
            ModernBetaBiomes.BETA_TAIGA,
            ModernBetaBiomes.PE_TAIGA,
            ModernBetaBiomes.LATE_BETA_TAIGA,
            ModernBetaBiomes.EARLY_RELEASE_TAIGA
        );
        
        this.builder(IS_TUNDRA).add(
            ModernBetaBiomes.BETA_TUNDRA,
            ModernBetaBiomes.PE_TUNDRA,
            ModernBetaBiomes.BETA_ICE_DESERT,
            ModernBetaBiomes.PE_ICE_DESERT,
            ModernBetaBiomes.LATE_BETA_ICE_PLAINS,
            ModernBetaBiomes.EARLY_RELEASE_ICE_PLAINS
        );
        
        this.builder(IS_OCEAN).add(
            ModernBetaBiomes.BETA_OCEAN,
            ModernBetaBiomes.BETA_LUKEWARM_OCEAN,
            ModernBetaBiomes.BETA_WARM_OCEAN,
            ModernBetaBiomes.BETA_COLD_OCEAN,
            ModernBetaBiomes.BETA_FROZEN_OCEAN,

            ModernBetaBiomes.PE_OCEAN,
            ModernBetaBiomes.PE_LUKEWARM_OCEAN,
            ModernBetaBiomes.PE_WARM_OCEAN,
            ModernBetaBiomes.PE_COLD_OCEAN,
            ModernBetaBiomes.PE_FROZEN_OCEAN
        );

        this.builder(IS_EXTREME_HILLS).add(
            ModernBetaBiomes.LATE_BETA_EXTREME_HILLS,
            ModernBetaBiomes.EARLY_RELEASE_EXTREME_HILLS
        );

        this.builder(IS_RELEASE_SPAWN).add(
            ModernBetaBiomes.BETA_FOREST,
            ModernBetaBiomes.BETA_PLAINS,
            ModernBetaBiomes.BETA_TAIGA,
            ModernBetaBiomes.BETA_RAINFOREST,
            ModernBetaBiomes.LATE_BETA_SWAMPLAND,
            ModernBetaBiomes.LATE_BETA_PLAINS,
            ModernBetaBiomes.LATE_BETA_TAIGA,
            ModernBetaBiomes.EARLY_RELEASE_TAIGA,
            Biomes.FOREST,
            Biomes.PLAINS,
            Biomes.TAIGA,
            Biomes.JUNGLE
        );

        this.builder(HAS_EARLY_RELEASE_SWAMP_COLORS).add(
            ModernBetaBiomes.EARLY_RELEASE_SWAMPLAND,
            Biomes.SWAMP,
            Biomes.MANGROVE_SWAMP
        );

        /* Modern Beta Biome Structure Tags */
        
        this.builder(INDEV_STRONGHOLD_HAS_STRUCTURE)
            .addTag(IS_INDEV);

        this.builder(OCEAN_SHRINE_HAS_STRUCTURE)
            .addTag(IS_OCEAN);
        
        /* Modern Beta Surface Config Tags */
        
        this.builder(SURFACE_CONFIG_SAND)
            .add(
                ModernBetaBiomes.BETA_DESERT,
                ModernBetaBiomes.PE_DESERT,
                Biomes.DESERT,
                Biomes.BEACH,
                Biomes.SNOWY_BEACH
            );
        
        this.builder(SURFACE_CONFIG_RED_SAND);
        
        this.builder(SURFACE_CONFIG_BADLANDS)
            .add(
                Biomes.BADLANDS,
                Biomes.ERODED_BADLANDS,
                Biomes.WOODED_BADLANDS
            );
    
        this.builder(SURFACE_CONFIG_NETHER)
            .add(Biomes.NETHER_WASTES);
        
        this.builder(SURFACE_CONFIG_WARPED_NYLIUM)
            .add(Biomes.WARPED_FOREST);
        
        this.builder(SURFACE_CONFIG_CRIMSON_NYLIUM)
            .add(Biomes.CRIMSON_FOREST);
        
        this.builder(SURFACE_CONFIG_BASALT)
            .add(Biomes.BASALT_DELTAS);
        
        this.builder(SURFACE_CONFIG_SOUL_SOIL)
            .add(Biomes.SOUL_SAND_VALLEY);
        
        this.builder(SURFACE_CONFIG_END)
            .add(
                Biomes.THE_END,
                Biomes.END_BARRENS,
                Biomes.END_HIGHLANDS,
                Biomes.END_MIDLANDS,
                Biomes.SMALL_END_ISLANDS
            );
        
        this.builder(SURFACE_CONFIG_GRASS)
            .add(Biomes.SWAMP);
        
        this.builder(SURFACE_CONFIG_MUD)
            .add(Biomes.MANGROVE_SWAMP);
        
        this.builder(SURFACE_CONFIG_MYCELIUM)
            .add(Biomes.MUSHROOM_FIELDS);
        
        this.builder(SURFACE_CONFIG_PODZOL)
            .add(
                Biomes.OLD_GROWTH_PINE_TAIGA,
                Biomes.OLD_GROWTH_SPRUCE_TAIGA
            );
        
        this.builder(SURFACE_CONFIG_STONE)
            .add(
                Biomes.STONY_PEAKS,
                Biomes.STONY_SHORE
            );
        
        this.builder(SURFACE_CONFIG_SNOW)
            .add(Biomes.SNOWY_SLOPES);
        
        this.builder(SURFACE_CONFIG_SNOW_DIRT)
            .add(
                Biomes.GROVE,
                Biomes.ICE_SPIKES
            );
        
        this.builder(SURFACE_CONFIG_SNOW_PACKED_ICE)
            .add(Biomes.FROZEN_PEAKS);

        this.builder(SURFACE_CONFIG_SNOW_STONE)
            .add(Biomes.JAGGED_PEAKS);

        /* Modern Beta Biome Height Tags */

        this.builder(HEIGHT_CONFIG_DEFAULT)
            .addTag(IS_TUNDRA)
            .addTag(HEIGHT_CONFIG_SHORT_HILLS)
            .add(
                Biomes.BADLANDS,
                Biomes.ERODED_BADLANDS,
                Biomes.WOODED_BADLANDS,
                Biomes.SAVANNA
            );

        this.builder(HEIGHT_CONFIG_OCEAN)
            .addTag(BiomeTags.IS_OCEAN)
            .add(
                Biomes.FROZEN_OCEAN,
                Biomes.OCEAN,
                Biomes.COLD_OCEAN,
                Biomes.LUKEWARM_OCEAN,
                Biomes.WARM_OCEAN
            );

        this.builder(HEIGHT_CONFIG_DESERT)
            .addTag(IS_DESERT)
            .add(
                Biomes.DESERT,
                Biomes.BADLANDS,
                Biomes.ERODED_BADLANDS,
                Biomes.WOODED_BADLANDS
            );

        this.builder(HEIGHT_CONFIG_BETA_HILLS)
            .addTag(IS_RAINFOREST)
            .add(
                ModernBetaBiomes.LATE_BETA_EXTREME_HILLS,
                Biomes.WINDSWEPT_SAVANNA
            );

        this.builder(HEIGHT_CONFIG_EXTREME_HILLS)
            .add(
                ModernBetaBiomes.EARLY_RELEASE_EXTREME_HILLS,
                Biomes.WINDSWEPT_HILLS,
                Biomes.WINDSWEPT_FOREST,
                Biomes.WINDSWEPT_GRAVELLY_HILLS
            );

        this.builder(HEIGHT_CONFIG_TAIGA)
            .addTag(BiomeTags.IS_TAIGA);

        this.builder(HEIGHT_CONFIG_SWAMPLAND)
            .addTag(IS_SWAMP)
            .add(
                Biomes.SWAMP,
                Biomes.MANGROVE_SWAMP
            );

        this.builder(HEIGHT_CONFIG_RIVER)
            .forceAddTag(BiomeTags.IS_RIVER)
            .add(
                Biomes.RIVER,
                Biomes.FROZEN_RIVER
            );

        this.builder(HEIGHT_CONFIG_MOUNTAINS)
            .addTag(IS_TUNDRA)
            .add(
                Biomes.SNOWY_PLAINS,
                Biomes.GROVE,
                Biomes.FROZEN_PEAKS,
                Biomes.JAGGED_PEAKS,
                Biomes.STONY_PEAKS,
                Biomes.SNOWY_SLOPES
            );

        this.builder(HEIGHT_CONFIG_MUSHROOM_ISLAND)
            .add(Biomes.MUSHROOM_FIELDS);

        this.builder(HEIGHT_CONFIG_MUSHROOM_ISLAND_SHORE)
            .add(Biomes.MUSHROOM_FIELDS);

        this.builder(HEIGHT_CONFIG_BEACH)
            .add(
                Biomes.BEACH,
                Biomes.SNOWY_BEACH
            );

        this.builder(HEIGHT_CONFIG_HILLS)
            .addTag(HEIGHT_CONFIG_DESERT)
            .addTag(HEIGHT_CONFIG_TAIGA);

        this.builder(HEIGHT_CONFIG_SHORT_HILLS)
            .addTag(IS_FOREST)
            .addTag(IS_SEASONAL_FOREST)
            .addTag(IS_SHRUBLAND)
            .addTag(IS_PLAINS)
            .add(
                Biomes.PLAINS,
                Biomes.FOREST,
                Biomes.FLOWER_FOREST,
                Biomes.BIRCH_FOREST,
                Biomes.DARK_FOREST,
                //? if >=1.21.4
                Biomes.PALE_GARDEN,
                Biomes.OLD_GROWTH_BIRCH_FOREST,
                Biomes.SNOWY_BEACH
            );

        this.builder(HEIGHT_CONFIG_EXTREME_HILLS_EDGE)
            .addTag(HEIGHT_CONFIG_EXTREME_HILLS);

        this.builder(HEIGHT_CONFIG_JUNGLE)
            .add(
                Biomes.JUNGLE,
                Biomes.BAMBOO_JUNGLE,
                Biomes.SPARSE_JUNGLE
            );

        this.builder(HEIGHT_CONFIG_JUNGLE_HILLS)
            .addTag(HEIGHT_CONFIG_JUNGLE)
            .add(
                Biomes.BADLANDS,
                Biomes.ERODED_BADLANDS,
                Biomes.WOODED_BADLANDS,
                Biomes.SAVANNA
            );

        this.builder(HEIGHT_CONFIG_PLATEAU)
            .add(
                Biomes.CHERRY_GROVE,
                Biomes.MEADOW
            );

        this.builder(HEIGHT_CONFIG_SWAMPLAND_HILLS)
            .addTag(HEIGHT_CONFIG_SWAMPLAND);

        this.builder(HEIGHT_CONFIG_PLATEAU_HILL)
            .add(
                Biomes.BADLANDS,
                Biomes.ERODED_BADLANDS,
                Biomes.WOODED_BADLANDS
            );

        this.builder(HEIGHT_CONFIG_DEEP_OCEAN)
            .add(
                Biomes.DEEP_FROZEN_OCEAN,
                Biomes.DEEP_OCEAN,
                Biomes.DEEP_COLD_OCEAN,
                Biomes.DEEP_LUKEWARM_OCEAN
            );
    }
    
    private void configureVanilla(Provider provider) {
        /* Vanilla Biome Tags */
        
        this.builder(BiomeTags.IS_OVERWORLD)
            .addTag(IS_MODERN_BETA);
        
        this.builder(BiomeTags.IS_DEEP_OCEAN)
            .addTag(IS_OCEAN);
        
        this.builder(BiomeTags.IS_FOREST)
            .addTag(IS_FOREST)
            .addTag(IS_SEASONAL_FOREST);
        
        this.builder(BiomeTags.IS_JUNGLE)
            .addTag(IS_RAINFOREST);
        
        this.builder(BiomeTags.IS_OCEAN)
            .addTag(IS_OCEAN);
        
        this.builder(BiomeTags.IS_TAIGA)
            .addTag(IS_TAIGA);
        
        /* Vanilla Biome Structure Tags */
        
        this.builder(BiomeTags.HAS_BURIED_TREASURE)
            .addTag(BiomeTags.IS_OCEAN);
        
        this.builder(BiomeTags.HAS_DESERT_PYRAMID)
            .addTag(IS_DESERT);
        
        this.builder(BiomeTags.HAS_IGLOO)
            .addTag(IS_TUNDRA);
        
        this.builder(BiomeTags.HAS_JUNGLE_TEMPLE)
            .addTag(IS_RAINFOREST);
        
        this.builder(BiomeTags.HAS_MINESHAFT)
            .addTag(IS_BETA)
            .addTag(IS_PE)
            .addTag(IS_ALPHA)
            .addTag(IS_INFDEV)
            .addTag(IS_INDEV);

        this.builder(BiomeTags.HAS_OCEAN_RUIN_COLD).add(
            ModernBetaBiomes.BETA_OCEAN,
            ModernBetaBiomes.BETA_COLD_OCEAN,
            ModernBetaBiomes.BETA_FROZEN_OCEAN,

            ModernBetaBiomes.PE_OCEAN,
            ModernBetaBiomes.PE_COLD_OCEAN,
            ModernBetaBiomes.PE_FROZEN_OCEAN
        );
        
        this.builder(BiomeTags.HAS_OCEAN_RUIN_WARM).add(
            ModernBetaBiomes.BETA_LUKEWARM_OCEAN,
            ModernBetaBiomes.BETA_WARM_OCEAN,

            ModernBetaBiomes.PE_LUKEWARM_OCEAN,
            ModernBetaBiomes.PE_WARM_OCEAN
        );
        
        this.builder(BiomeTags.HAS_PILLAGER_OUTPOST)
            .addTag(IS_DESERT)
            .addTag(IS_PLAINS)
            .addTag(IS_SAVANNA)
            .addTag(IS_SWAMP)
            .addTag(IS_TUNDRA)
            .add(ModernBetaBiomes.BETA_SKY);
        
        this.builder(BiomeTags.HAS_RUINED_PORTAL_DESERT)
            .addTag(IS_DESERT);
        
        this.builder(BiomeTags.HAS_RUINED_PORTAL_STANDARD)
            .addTag(IS_PLAINS)
            .addTag(IS_SAVANNA)
            .addTag(IS_TUNDRA)
            .add(ModernBetaBiomes.BETA_SKY);
        
        this.builder(BiomeTags.HAS_RUINED_PORTAL_SWAMP)
            .addTag(IS_SWAMP);
        
        this.builder(BiomeTags.HAS_STRONGHOLD)
            .addTag(IS_BETA)
            .addTag(IS_PE)
            .addTag(IS_ALPHA)
            .addTag(IS_INFDEV)
            .addTag(IS_LATE_BETA)
            .addTag(IS_EARLY_RELEASE)
            .add(
                ModernBetaBiomes.LATE_BETA_EXTREME_HILLS,
                ModernBetaBiomes.LATE_BETA_SWAMPLAND,
                ModernBetaBiomes.LATE_BETA_TAIGA,
                ModernBetaBiomes.LATE_BETA_ICE_PLAINS,

                ModernBetaBiomes.EARLY_RELEASE_EXTREME_HILLS,
                ModernBetaBiomes.EARLY_RELEASE_SWAMPLAND,
                ModernBetaBiomes.EARLY_RELEASE_TAIGA,
                ModernBetaBiomes.EARLY_RELEASE_ICE_PLAINS
            );

        this.builder(BiomeTags.HAS_SWAMP_HUT)
            .addTag(IS_SWAMP);

        //? if >=1.21 {
        this.builder(BiomeTags.HAS_TRIAL_CHAMBERS)
            .addTag(IS_BETA)
            .addTag(IS_PE)
            .addTag(IS_ALPHA)
            .addTag(IS_INFDEV)
            .addTag(IS_LATE_BETA)
            .addTag(IS_EARLY_RELEASE);
        //?}
        
        this.builder(BiomeTags.HAS_VILLAGE_DESERT)
            .addTag(IS_DESERT);
        
        this.builder(BiomeTags.HAS_VILLAGE_PLAINS)
            .addTag(IS_PLAINS)
            .addTag(IS_SHRUBLAND)
            .addTag(IS_SAVANNA)
            .addTag(IS_ALPHA)
            .addTag(IS_INFDEV)
            .addTag(IS_INDEV);
        
        this.builder(BiomeTags.HAS_VILLAGE_SNOWY)
            .addTag(IS_TUNDRA);
        
        this.builder(BiomeTags.HAS_VILLAGE_TAIGA)
            .addTag(IS_TAIGA);
        
        this.builder(BiomeTags.HAS_WOODLAND_MANSION)
            .addTag(IS_SEASONAL_FOREST);
        
        /* Misc. Tags */
        
        this.builder(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL).add(
            ModernBetaBiomes.BETA_WARM_OCEAN,
            ModernBetaBiomes.PE_WARM_OCEAN
        );
        
        this.builder(BiomeTags.POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS).add(
            ModernBetaBiomes.BETA_FROZEN_OCEAN,
            ModernBetaBiomes.PE_FROZEN_OCEAN
        );
        
        this.builder(BiomeTags.ALLOWS_SURFACE_SLIME_SPAWNS)
            .addTag(IS_SWAMP);
        
        this.builder(BiomeTags.HAS_TRAIL_RUINS)
            .add(
                ModernBetaBiomes.BETA_TAIGA,
                ModernBetaBiomes.BETA_RAINFOREST,
                
                ModernBetaBiomes.PE_TAIGA,
                ModernBetaBiomes.PE_RAINFOREST
            );

        this.builder(BiomeTags.SPAWNS_SNOW_FOXES)
            .add(
                ModernBetaBiomes.BETA_TAIGA,
                ModernBetaBiomes.PE_TAIGA,
                ModernBetaBiomes.EARLY_RELEASE_TAIGA
            );

        //? if >=1.21.5 {
        this.builder(BiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS)
            .addTag(IS_TAIGA)
            .add(
                ModernBetaBiomes.LATE_BETA_EXTREME_HILLS,
                ModernBetaBiomes.EARLY_RELEASE_EXTREME_HILLS
            );
        
        this.builder(BiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS)
            .addTag(IS_SAVANNA)
            .addTag(IS_RAINFOREST);
        //?}
    }
    
    /*
     * For determining climate tags, see:
     * https://www.minecraftforum.net/forums/archive/alpha/alpha-survival-single-player/820956-biome-geography-algorithm-analysis-updated-11-4
     * 
     */
    private void configureConventional(Provider provider) {
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_AQUATIC
            //?} else {
            /*ConventionalBiomeTags.AQUATIC
            *///?}
        )
            .addTag(IS_OCEAN);
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_AQUATIC_ICY
            //?} else {
            /*ConventionalBiomeTags.AQUATIC_ICY
            *///?}
        )
            .add(
                ModernBetaBiomes.BETA_FROZEN_OCEAN,
                ModernBetaBiomes.PE_FROZEN_OCEAN
            );
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_COLD
            //?} else {
            /*ConventionalBiomeTags.CLIMATE_COLD
            *///?}
        )
            .addTag(IS_TAIGA)
            .addTag(IS_TUNDRA);
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_DRY
            //?} else {
            /*ConventionalBiomeTags.CLIMATE_DRY
            *///?}
        )
            .addTag(IS_DESERT)
            .addTag(IS_PLAINS)
            .addTag(IS_SAVANNA)
            .addTag(IS_SHRUBLAND)
            .addTag(IS_TUNDRA);
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_HOT
            //?} else {
            /*ConventionalBiomeTags.CLIMATE_HOT
            *///?}
        )
            .addTag(IS_DESERT)
            .addTag(IS_PLAINS)
            .addTag(IS_SEASONAL_FOREST)
            .addTag(IS_RAINFOREST);
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_TEMPERATE
            //?} else {
            /*ConventionalBiomeTags.CLIMATE_TEMPERATE
            *///?}
        )
            .addTag(IS_SAVANNA)
            .addTag(IS_SHRUBLAND)
            .addTag(IS_FOREST)
            .addTag(IS_SWAMP);
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_WET
            //?} else {
            /*ConventionalBiomeTags.CLIMATE_WET
            *///?}
        )
            .addTag(IS_OCEAN)
            .addTag(IS_SWAMP)
            .addTag(IS_RAINFOREST);
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_DESERT
            //?} else {
            /*ConventionalBiomeTags.DESERT
            *///?}
        )
            .addTag(IS_DESERT);
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_FOREST
            //?} else {
            /*ConventionalBiomeTags.FOREST
            *///?}
        )
            .addTag(IS_FOREST)
            .addTag(IS_SEASONAL_FOREST);
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_OVERWORLD
            //?} else {
            /*ConventionalBiomeTags.IN_OVERWORLD
            *///?}
        )
            .addTag(IS_MODERN_BETA);
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_JUNGLE
            //?} else {
            /*ConventionalBiomeTags.JUNGLE
            *///?}
        )
            .addTag(IS_RAINFOREST);
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_DEEP_OCEAN
            //?} else {
            /*ConventionalBiomeTags.DEEP_OCEAN
            *///?}
        )
            .addTag(IS_OCEAN);
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_PLAINS
            //?} else {
            /*ConventionalBiomeTags.PLAINS
            *///?}
        )
            .addTag(IS_PLAINS)
            .addTag(IS_SHRUBLAND);
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_SAVANNA
            //?} else {
            /*ConventionalBiomeTags.SAVANNA
            *///?}
        )
            .addTag(IS_SAVANNA);
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_SHALLOW_OCEAN
            //?} else {
            /*ConventionalBiomeTags.SHALLOW_OCEAN
            *///?}
        )
            .addTag(IS_OCEAN);
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_SNOWY
            //?} else {
            /*ConventionalBiomeTags.SNOWY
            *///?}
        )
            .addTag(IS_TAIGA)
            .addTag(IS_TUNDRA);
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_SNOWY_PLAINS
            //?} else {
            /*ConventionalBiomeTags.SNOWY_PLAINS
            *///?}
        )
            .addTag(IS_TUNDRA);
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_SWAMP
            //?} else {
            /*ConventionalBiomeTags.SWAMP
            *///?}
        )
            .addTag(IS_SWAMP);
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_TAIGA
            //?} else {
            /*ConventionalBiomeTags.TAIGA
            *///?}
        )
            .addTag(IS_TAIGA);
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_CONIFEROUS_TREE
            //?} else {
            /*ConventionalBiomeTags.TREE_CONIFEROUS
            *///?}
        )
            .addTag(IS_TAIGA);
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_DECIDUOUS_TREE
            //?} else {
            /*ConventionalBiomeTags.TREE_DECIDUOUS
            *///?}
        )
            .addTag(IS_FOREST)
            .addTag(IS_SEASONAL_FOREST);
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_JUNGLE_TREE
            //?} else {
            /*ConventionalBiomeTags.TREE_JUNGLE
            *///?}
        )
            .addTag(IS_RAINFOREST);
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_SAVANNA_TREE
            //?} else {
            /*ConventionalBiomeTags.TREE_SAVANNA
            *///?}
        )
            .addTag(IS_SAVANNA);
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_VEGETATION_DENSE
            //?} else {
            /*ConventionalBiomeTags.VEGETATION_DENSE
            *///?}
        )
            .addTag(IS_RAINFOREST)
            .addTag(IS_PLAINS);
        
        this.builder(
            //? if >=1.20.5 {
            ConventionalBiomeTags.IS_VEGETATION_SPARSE
            //?} else {
            /*ConventionalBiomeTags.VEGETATION_SPARSE
            *///?}
        )
            .addTag(IS_DESERT)
            .addTag(IS_SAVANNA)
            .addTag(IS_SHRUBLAND)
            .addTag(IS_TUNDRA)
            .addTag(IS_EXTREME_HILLS);
    }

    private void configureModded(Provider provider) {
        //TODO: maybe clean these up?
        this.builder(TagKey.create(Registries.BIOME, VersionCompat.id("sereneseasons", "blacklisted_biomes")))
            .addTag(IS_INDEV)
            .addTag(IS_INFDEV)
            .addTag(IS_ALPHA);

        this.builder(TagKey.create(Registries.BIOME, VersionCompat.id("sereneseasons", "lesser_color_change_biomes")))
            .add(ModernBetaBiomes.EARLY_RELEASE_SWAMPLAND);

        this.builder(TagKey.create(Registries.BIOME, VersionCompat.id("sereneseasons", "tropical_biomes")))
            .addTag(IS_RAINFOREST)
            .addTag(IS_DESERT)
            .addTag(IS_SAVANNA)
            .addTag(IS_SWAMP);
    }
}