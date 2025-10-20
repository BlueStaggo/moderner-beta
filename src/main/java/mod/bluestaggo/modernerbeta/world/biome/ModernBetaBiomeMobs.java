package mod.bluestaggo.modernerbeta.world.biome;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class ModernBetaBiomeMobs {
    public static void addCommonMobs(MobSpawnSettings.Builder spawnSettings) {
        BiomeDefaultFeatures.farmAnimals(spawnSettings);
        BiomeDefaultFeatures.commonSpawns(spawnSettings);
    }
    
    public static void addSquid(MobSpawnSettings.Builder spawnSettings) {
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.WATER_CREATURE, EntityType.SQUID, 10, 1, 4);
    }
    
    public static void addTurtles(MobSpawnSettings.Builder spawnSettings) {
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.CREATURE, EntityType.TURTLE, 5, 2, 5);
    }
    
    public static void addWolves(MobSpawnSettings.Builder spawnSettings) {
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.CREATURE, EntityType.WOLF, 5, 4, 4);
    }
    
    public static void addColdOceanMobs(MobSpawnSettings.Builder spawnSettings) {
        BiomeDefaultFeatures.oceanSpawns(spawnSettings, 3, 4, 15);

        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.WATER_AMBIENT, EntityType.SALMON, 15, 1, 5);
    }
    
    public static void addFrozenOceanMobs(MobSpawnSettings.Builder spawnSettings) {
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.WATER_CREATURE, EntityType.SQUID, 1, 1, 4);
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.WATER_AMBIENT, EntityType.SALMON, 15, 1, 5);
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.CREATURE, EntityType.POLAR_BEAR, 1, 1, 2);

        BiomeDefaultFeatures.commonSpawns(spawnSettings);
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.MONSTER, EntityType.DROWNED, 5, 1, 1);
    }
    
    public static void addOceanMobs(MobSpawnSettings.Builder spawnSettings) {
        BiomeDefaultFeatures.oceanSpawns(spawnSettings, 10, 4, 10);

        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.WATER_CREATURE, EntityType.DOLPHIN, 1, 1, 2);
    }

    public static void addWarmOceanMobs(MobSpawnSettings.Builder spawnSettings) {
        BiomeDefaultFeatures.warmOceanSpawns(spawnSettings, 10, 4);

        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.WATER_AMBIENT, EntityType.PUFFERFISH, 15, 1, 3);
    }
    
    public static void addLukewarmOceanMobs(MobSpawnSettings.Builder spawnSettings) {
        BiomeDefaultFeatures.oceanSpawns(spawnSettings, 10, 2, 15);

        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.WATER_AMBIENT, EntityType.PUFFERFISH, 5, 1, 3);
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.WATER_AMBIENT, EntityType.TROPICAL_FISH, 25, 8, 8);
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.WATER_CREATURE, EntityType.DOLPHIN, 2, 1, 2);
    }
    
    public static void addDesertMobs(MobSpawnSettings.Builder spawnSettings) {
        BiomeDefaultFeatures.desertSpawns(spawnSettings);
    }
    
    public static void addPlainsMobs(MobSpawnSettings.Builder spawnSettings) {
        BiomeDefaultFeatures.plainsSpawns(spawnSettings);
    }
    
    public static void addRainforestMobs(MobSpawnSettings.Builder spawnSettings) {
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.MONSTER, EntityType.OCELOT, 2, 1, 3);
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.CREATURE, EntityType.PANDA, 2, 1, 2);
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.CREATURE, EntityType.PARROT, 40, 1, 2);
    }
    
    public static void addSwamplandMobs(MobSpawnSettings.Builder spawnSettings) {
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.MONSTER, EntityType.SLIME, 1, 1, 1);
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.CREATURE, EntityType.FROG, 10, 2, 5);
    }
    
    public static void addTaigaMobs(MobSpawnSettings.Builder spawnSettings) {
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.CREATURE, EntityType.WOLF, 5, 4, 4);
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.CREATURE, EntityType.RABBIT, 4, 2, 3);
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.CREATURE, EntityType.LLAMA, 4, 4, 6);
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.CREATURE, EntityType.FOX, 8, 2, 4);
    }
    
    public static void addTundraMobs(MobSpawnSettings.Builder spawnSettings) {
        BiomeDefaultFeatures.snowySpawns(spawnSettings /*? >=1.21.11 {*//*, true *//*?}*/);
        
        // TODO: Move maybe later
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.CREATURE, EntityType.GOAT, 5, 4, 6);
    }
    
    public static void addSkyMobs(MobSpawnSettings.Builder spawnSettings) {
        BiomeDefaultFeatures.monsters(spawnSettings, 95, 5, /*? >=1.21.11 {*/ /*0, *//*?}*/ 20, false);
        VersionCompat.addSpawnEntry(spawnSettings, MobCategory.CREATURE, EntityType.CHICKEN, 10, 4, 4);
    }
}
