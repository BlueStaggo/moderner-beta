package mod.bluestaggo.modernerbeta.world.structure;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.structure.StructureSet;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.gen.chunk.placement.SpreadType;
import net.minecraft.world.gen.structure.Structure;

public class ModernBetaStructureSets {
    public static final RegistryKey<StructureSet> INDEV_STRONGHOLDS = keyOf("indev_strongholds");
    public static final RegistryKey<StructureSet> OCEAN_SHRINE = keyOf("ocean_shrine");

    public static void bootstrap(Registerable<StructureSet> structureSetRegisterable) {
        RegistryEntryLookup<Structure> registryStructure = structureSetRegisterable.getRegistryLookup(RegistryKeys.STRUCTURE);
        RegistryEntryLookup<Biome> registryBiome = structureSetRegisterable.getRegistryLookup(RegistryKeys.BIOME);
        
        structureSetRegisterable.register(
            INDEV_STRONGHOLDS,
            new StructureSet(
                registryStructure.getOrThrow(ModernBetaStructures.INDEV_STRONGHOLD),
                new ConcentricRingsStructurePlacement(0, 0, 1, registryBiome.getOrThrow(BiomeTags.STRONGHOLD_BIASED_TO))
            )
        );
        structureSetRegisterable.register(
            OCEAN_SHRINE,
            new StructureSet(
                registryStructure.getOrThrow(ModernBetaStructures.OCEAN_SHRINE),
                new RandomSpreadStructurePlacement(64, 16, SpreadType.TRIANGULAR, 357)
            )
        );
    }
    
    private static RegistryKey<StructureSet> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.STRUCTURE_SET, ModernerBeta.createId(id));
    }
}
