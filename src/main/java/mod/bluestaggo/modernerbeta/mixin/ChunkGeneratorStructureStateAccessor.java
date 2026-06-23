package mod.bluestaggo.modernerbeta.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(ChunkGeneratorStructureState.class)
public interface ChunkGeneratorStructureStateAccessor {
    @Invoker("<init>")
    static ChunkGeneratorStructureState invokeInit(
        RandomState randomState,
        BiomeSource biomeSource,
        long levelSeed,
        //? if >=26.3
        //net.minecraft.world.level.ChunkPos origin,
        long concentricRingsSeed,
        List<Holder<StructureSet>> possibleStructureSets
    ) {
        throw new AssertionError();
    }

    @Invoker("hasBiomesForStructureSet")
    static boolean invokeHasBiomesForStructureSet(StructureSet structureSet, BiomeSource biomeSource) {
        throw new AssertionError();
    }
}
