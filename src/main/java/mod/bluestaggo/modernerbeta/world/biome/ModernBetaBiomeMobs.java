package mod.bluestaggo.modernerbeta.world.biome;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;

public class ModernBetaBiomeMobs {
    public static void addCommonMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings);
    }
    
    public static void addSquid(SpawnSettings.Builder spawnSettings) {
        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.WATER_CREATURE, EntityType.SQUID, 10, 1, 4);
    }
    
    public static void addTurtles(SpawnSettings.Builder spawnSettings) {
        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.CREATURE, EntityType.TURTLE, 5, 2, 5);
    }
    
    public static void addWolves(SpawnSettings.Builder spawnSettings) {
        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.CREATURE, EntityType.WOLF, 5, 4, 4);
    }
    
    public static void addColdOceanMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addOceanMobs(spawnSettings, 3, 4, 15);

        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.WATER_AMBIENT, EntityType.SALMON, 15, 1, 5);
    }
    
    public static void addFrozenOceanMobs(SpawnSettings.Builder spawnSettings) {
        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.WATER_CREATURE, EntityType.SQUID, 1, 1, 4);
        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.WATER_AMBIENT, EntityType.SALMON, 15, 1, 5);
        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.CREATURE, EntityType.POLAR_BEAR, 1, 1, 2);

        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings);
        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.MONSTER, EntityType.DROWNED, 5, 1, 1);
    }
    
    public static void addOceanMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addOceanMobs(spawnSettings, 10, 4, 10);

        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.WATER_CREATURE, EntityType.DOLPHIN, 1, 1, 2);
    }

    public static void addWarmOceanMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addWarmOceanMobs(spawnSettings, 10, 4);

        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.WATER_AMBIENT, EntityType.PUFFERFISH, 15, 1, 3);
    }
    
    public static void addLukewarmOceanMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addOceanMobs(spawnSettings, 10, 2, 15);

        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.WATER_AMBIENT, EntityType.PUFFERFISH, 5, 1, 3);
        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.WATER_AMBIENT, EntityType.TROPICAL_FISH, 25, 8, 8);
        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.WATER_CREATURE, EntityType.DOLPHIN, 2, 1, 2);
    }
    
    public static void addDesertMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addDesertMobs(spawnSettings);
    }
    
    public static void addPlainsMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addPlainsMobs(spawnSettings);
    }
    
    public static void addRainforestMobs(SpawnSettings.Builder spawnSettings) {
        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.MONSTER, EntityType.OCELOT, 2, 1, 3);
        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.CREATURE, EntityType.PANDA, 2, 1, 2);
        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.CREATURE, EntityType.PARROT, 40, 1, 2);
    }
    
    public static void addSwamplandMobs(SpawnSettings.Builder spawnSettings) {
        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.MONSTER, EntityType.SLIME, 1, 1, 1);
        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.CREATURE, EntityType.FROG, 10, 2, 5);
    }
    
    public static void addTaigaMobs(SpawnSettings.Builder spawnSettings) {
        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.CREATURE, EntityType.WOLF, 5, 4, 4);
        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.CREATURE, EntityType.RABBIT, 4, 2, 3);
        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.CREATURE, EntityType.LLAMA, 4, 4, 6);
        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.CREATURE, EntityType.FOX, 8, 2, 4);
    }
    
    public static void addTundraMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addSnowyMobs(spawnSettings);
        
        // TODO: Move maybe later
        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.CREATURE, EntityType.GOAT, 5, 4, 6);
    }
    
    public static void addSkyMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addMonsters(spawnSettings, 95, 5, 20, false);
        VersionCompat.addSpawnEntry(spawnSettings, SpawnGroup.CREATURE, EntityType.CHICKEN, 10, 4, 4);
    }
}
