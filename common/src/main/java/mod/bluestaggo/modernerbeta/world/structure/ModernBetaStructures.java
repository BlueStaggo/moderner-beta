package mod.bluestaggo.modernerbeta.world.structure;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.tags.ModernBetaBiomeTags;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.StructureSpawns;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.structure.StrongholdStructure;
import net.minecraft.world.gen.structure.Structure;

import java.util.Map;

public class ModernBetaStructures {
    public static final RegistryKey<Structure> INDEV_STRONGHOLD = RegistryKey.of(RegistryKeys.STRUCTURE, ModernerBeta.createId("indev_stronghold"));
    
    public static void bootstrap(Registerable<Structure> structureRegisterable) {
        RegistryEntryLookup<Biome> registryBiome = structureRegisterable.getRegistryLookup(RegistryKeys.BIOME);
        
        structureRegisterable.register(
            INDEV_STRONGHOLD,
            new StrongholdStructure(createConfig(registryBiome.getOrThrow(ModernBetaBiomeTags.INDEV_STRONGHOLD_HAS_STRUCTURE), StructureTerrainAdaptation.BURY))
        );
    }
    
    private static Structure.Config createConfig(RegistryEntryList<Biome> biomes, StructureTerrainAdaptation terrainAdaptation) {
        return createConfig(biomes, Map.of(), GenerationStep.Feature.SURFACE_STRUCTURES, terrainAdaptation);
    }
    
    private static Structure.Config createConfig(RegistryEntryList<Biome> biomes, Map<SpawnGroup, StructureSpawns> spawns, GenerationStep.Feature featureStep, StructureTerrainAdaptation terrainAdaptation) {
        return new Structure.Config(biomes, spawns, featureStep, terrainAdaptation);
    }
}
