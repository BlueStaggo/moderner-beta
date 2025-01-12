package mod.bluestaggo.modernerbeta.world.biome;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.SpawnSettings.SpawnEntry;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;

public class ModernBetaBiomeMobs {
    public static void addCommonMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings);
    }
    
    public static void addSquid(SpawnSettings.Builder spawnSettings) {
        spawnSettings.spawn(SpawnGroup.WATER_CREATURE, 10, new SpawnEntry(EntityType.SQUID, 1, 4));
    }
    
    public static void addTurtles(SpawnSettings.Builder spawnSettings) {
        spawnSettings.spawn(SpawnGroup.CREATURE, 5, new SpawnEntry(EntityType.TURTLE, 2, 5));
    }
    
    public static void addWolves(SpawnSettings.Builder spawnSettings) {
        spawnSettings.spawn(SpawnGroup.CREATURE, 5, new SpawnEntry(EntityType.WOLF, 4, 4));
    }
    
    public static void addColdOceanMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addOceanMobs(spawnSettings, 3, 4, 15);
        
        spawnSettings.spawn(SpawnGroup.WATER_AMBIENT, 15, new SpawnEntry(EntityType.SALMON, 1, 5));
    }
    
    public static void addFrozenOceanMobs(SpawnSettings.Builder spawnSettings) {
        spawnSettings.spawn(SpawnGroup.WATER_CREATURE, 1, new SpawnEntry(EntityType.SQUID, 1, 4));
        spawnSettings.spawn(SpawnGroup.WATER_AMBIENT, 15, new SpawnEntry(EntityType.SALMON, 1, 5));
        spawnSettings.spawn(SpawnGroup.CREATURE, 1, new SpawnSettings.SpawnEntry(EntityType.POLAR_BEAR, 1, 2));

        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings);
        spawnSettings.spawn(SpawnGroup.MONSTER, 5, new SpawnSettings.SpawnEntry(EntityType.DROWNED, 1, 1));
    }
    
    public static void addOceanMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addOceanMobs(spawnSettings, 10, 4, 10);
        
        spawnSettings.spawn(SpawnGroup.WATER_CREATURE, 1, new SpawnEntry(EntityType.DOLPHIN, 1, 2));
    }

    public static void addWarmOceanMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addWarmOceanMobs(spawnSettings, 10, 4);
        
        spawnSettings.spawn(SpawnGroup.WATER_AMBIENT, 15, new SpawnEntry(EntityType.PUFFERFISH, 1, 3));
    }
    
    public static void addLukewarmOceanMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addOceanMobs(spawnSettings, 10, 2, 15);
        
        spawnSettings.spawn(SpawnGroup.WATER_AMBIENT, 5, new SpawnEntry(EntityType.PUFFERFISH, 1, 3));
        spawnSettings.spawn(SpawnGroup.WATER_AMBIENT, 25, new SpawnEntry(EntityType.TROPICAL_FISH, 8, 8));
        spawnSettings.spawn(SpawnGroup.WATER_CREATURE, 2, new SpawnEntry(EntityType.DOLPHIN, 1, 2));
    }
    
    public static void addDesertMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addDesertMobs(spawnSettings);
    }
    
    public static void addPlainsMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addPlainsMobs(spawnSettings);
    }
    
    public static void addRainforestMobs(SpawnSettings.Builder spawnSettings) {
        spawnSettings.spawn(SpawnGroup.MONSTER, 2, new SpawnEntry(EntityType.OCELOT, 1, 3));
        spawnSettings.spawn(SpawnGroup.CREATURE, 2, new SpawnEntry(EntityType.PANDA, 1, 2));
        spawnSettings.spawn(SpawnGroup.CREATURE, 40, new SpawnEntry(EntityType.PARROT, 1, 2));
    }
    
    public static void addSwamplandMobs(SpawnSettings.Builder spawnSettings) {
        spawnSettings.spawn(SpawnGroup.MONSTER, 1, new SpawnSettings.SpawnEntry(EntityType.SLIME, 1, 1));
        spawnSettings.spawn(SpawnGroup.CREATURE, 10, new SpawnEntry(EntityType.FROG, 2, 5));
    }
    
    public static void addTaigaMobs(SpawnSettings.Builder spawnSettings) {
        spawnSettings.spawn(SpawnGroup.CREATURE, 5, new SpawnEntry(EntityType.WOLF, 4, 4));
        spawnSettings.spawn(SpawnGroup.CREATURE, 4, new SpawnEntry(EntityType.RABBIT, 2, 3));
        spawnSettings.spawn(SpawnGroup.CREATURE, 4, new SpawnEntry(EntityType.LLAMA, 4, 6));
        spawnSettings.spawn(SpawnGroup.CREATURE, 8, new SpawnEntry(EntityType.FOX, 2, 4));
    }
    
    public static void addTundraMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addSnowyMobs(spawnSettings);
        
        // TODO: Move maybe later
        spawnSettings.spawn(SpawnGroup.CREATURE, 5, new SpawnEntry(EntityType.GOAT, 4, 6));
    }
    
    public static void addSkyMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addMonsters(spawnSettings, 95, 5, 20, false);
        spawnSettings.spawn(SpawnGroup.CREATURE, 10, new SpawnSettings.SpawnEntry(EntityType.CHICKEN, 4, 4));
    }
}
