package mod.bluestaggo.modernerbeta.level.structure;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.tags.ModernBetaBiomeTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.structures.StrongholdStructure;

import java.util.List;
import java.util.Map;

public class ModernBetaStructures {
    public static final ResourceKey<Structure> OCEAN_SHRINE = of("ocean_shrine");

    public static void bootstrap(BootstrapContext<Structure> context) {
        HolderGetter<Biome> registryBiome = context.lookup(Registries.BIOME);

        context.register(
            OCEAN_SHRINE,
            new OceanShrineStructure(
                createConfig(
                    registryBiome.getOrThrow(ModernBetaBiomeTags.OCEAN_SHRINE_HAS_STRUCTURE),
                    Map.of(
                        MobCategory.MONSTER, new StructureSpawnOverride(
                            StructureSpawnOverride.BoundingBoxType.STRUCTURE,
                            WeightedList.of(new MobSpawnSettings.SpawnerData(EntityType.GUARDIAN, /*? if <1.21.5 {*//*1, *//*?}*/ 1, 2))
                        ),
                        MobCategory.UNDERGROUND_WATER_CREATURE, new StructureSpawnOverride(
                            StructureSpawnOverride.BoundingBoxType.STRUCTURE,
                            MobSpawnSettings.EMPTY_MOB_LIST
                        ),
                        MobCategory.AXOLOTLS, new StructureSpawnOverride(
                            StructureSpawnOverride.BoundingBoxType.STRUCTURE,
                            MobSpawnSettings.EMPTY_MOB_LIST
                        )
                    ),
                    GenerationStep.Decoration.SURFACE_STRUCTURES,
                    TerrainAdjustment.BEARD_THIN
                )
            )
        );
    }
    
    private static Structure.StructureSettings createConfig(HolderSet<Biome> biomes, TerrainAdjustment terrainAdaptation) {
        return createConfig(biomes, Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, terrainAdaptation);
    }
    
    private static Structure.StructureSettings createConfig(HolderSet<Biome> biomes, Map<MobCategory, StructureSpawnOverride> spawns, GenerationStep.Decoration featureStep, TerrainAdjustment terrainAdaptation) {
        return new Structure.StructureSettings(biomes, spawns, featureStep, terrainAdaptation);
    }

    private static ResourceKey<Structure> of(String id) {
        return ResourceKey.create(Registries.STRUCTURE, ModernerBeta.createId(id));
    }
}
