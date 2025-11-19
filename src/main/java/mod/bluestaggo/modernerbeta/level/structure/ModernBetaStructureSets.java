package mod.bluestaggo.modernerbeta.level.structure;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

public class ModernBetaStructureSets {
    public static final ResourceKey<StructureSet> INDEV_STRONGHOLDS = keyOf("indev_strongholds");
    public static final ResourceKey<StructureSet> OCEAN_SHRINE = keyOf("ocean_shrine");

    public static void bootstrap(BootstrapContext<StructureSet> context) {
        HolderGetter<Structure> registryStructure = context.lookup(Registries.STRUCTURE);
        HolderGetter<Biome> registryBiome = context.lookup(Registries.BIOME);
        
        context.register(
            INDEV_STRONGHOLDS,
            new StructureSet(
                registryStructure.getOrThrow(ModernBetaStructures.INDEV_STRONGHOLD),
                new ConcentricRingsStructurePlacement(0, 0, 1, registryBiome.getOrThrow(BiomeTags.STRONGHOLD_BIASED_TO))
            )
        );
        context.register(
            OCEAN_SHRINE,
            new StructureSet(
                registryStructure.getOrThrow(ModernBetaStructures.OCEAN_SHRINE),
                new RandomSpreadStructurePlacement(64, 16, RandomSpreadType.TRIANGULAR, 357)
            )
        );
    }
    
    private static ResourceKey<StructureSet> keyOf(String id) {
        return ResourceKey.create(Registries.STRUCTURE_SET, ModernerBeta.createId(id));
    }
}
