package mod.bluestaggo.modernerbeta.world.structure;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.tags.ModernBetaBiomeTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.collection.Pool;
import net.minecraft.world.StructureSpawns;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.structure.StrongholdStructure;
import net.minecraft.world.gen.structure.Structure;

import java.util.Map;

public class ModernBetaStructures {
    public static final RegistryKey<Structure> INDEV_STRONGHOLD = of("indev_stronghold");
    public static final RegistryKey<Structure> OCEAN_SHRINE = of("ocean_shrine");

    public static void bootstrap(Registerable<Structure> structureRegisterable) {
        RegistryEntryLookup<Biome> registryBiome = structureRegisterable.getRegistryLookup(RegistryKeys.BIOME);
        
        structureRegisterable.register(
            INDEV_STRONGHOLD,
            new StrongholdStructure(createConfig(registryBiome.getOrThrow(ModernBetaBiomeTags.INDEV_STRONGHOLD_HAS_STRUCTURE), StructureTerrainAdaptation.BURY))
        );
        structureRegisterable.register(
            OCEAN_SHRINE,
            new OceanShrineStructure(
                createConfig(
                    registryBiome.getOrThrow(ModernBetaBiomeTags.OCEAN_SHRINE_HAS_STRUCTURE),
                    Map.of(
                        SpawnGroup.MONSTER, new StructureSpawns(
                            StructureSpawns.BoundingBox.STRUCTURE,
                            Pool.of(new SpawnSettings.SpawnEntry(EntityType.GUARDIAN, /*? if <1.21.5 {*//*1, *//*?}*/ 1, 2))
                        ),
                        SpawnGroup.UNDERGROUND_WATER_CREATURE, new StructureSpawns(
                            StructureSpawns.BoundingBox.STRUCTURE,
                            SpawnSettings.EMPTY_ENTRY_POOL
                        ),
                        SpawnGroup.AXOLOTLS, new StructureSpawns(
                            StructureSpawns.BoundingBox.STRUCTURE,
                            SpawnSettings.EMPTY_ENTRY_POOL
                        )
                    ),
                    GenerationStep.Feature.SURFACE_STRUCTURES,
                    StructureTerrainAdaptation.BEARD_THIN
                )
            )
        );
    }
    
    private static Structure.Config createConfig(RegistryEntryList<Biome> biomes, StructureTerrainAdaptation terrainAdaptation) {
        return createConfig(biomes, Map.of(), GenerationStep.Feature.SURFACE_STRUCTURES, terrainAdaptation);
    }
    
    private static Structure.Config createConfig(RegistryEntryList<Biome> biomes, Map<SpawnGroup, StructureSpawns> spawns, GenerationStep.Feature featureStep, StructureTerrainAdaptation terrainAdaptation) {
        return new Structure.Config(biomes, spawns, featureStep, terrainAdaptation);
    }

    private static RegistryKey<Structure> of(String id) {
        return RegistryKey.of(RegistryKeys.STRUCTURE, ModernerBeta.createId(id));
    }
}
