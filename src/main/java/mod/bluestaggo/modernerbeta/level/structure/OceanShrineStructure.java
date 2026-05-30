package mod.bluestaggo.modernerbeta.level.structure;

import java.util.Optional;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class OceanShrineStructure extends Structure {
    public static final com.mojang.serialization.MapCodec<OceanShrineStructure> CODEC = simpleCodec(OceanShrineStructure::new);
    private static final Identifier SHRINE_BASE = ModernerBeta.createId("ocean_shrine/base");

    public OceanShrineStructure(Structure.StructureSettings config) {
        super(config);
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        int x = context.chunkPos().getMinBlockX();
        int z = context.chunkPos().getMinBlockZ();
        int y = context.chunkGenerator().getFirstOccupiedHeight(x, z, Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());

        BlockPos pos = new BlockPos(x, y, z);
        Rotation rot = Rotation.getRandom(context.random());

        return Optional.of(new GenerationStub(pos, (collector) ->
            collector.addPiece(new OceanShrineStructurePiece(context.structureTemplateManager(), pos, SHRINE_BASE, rot))));
    }
    @Override
    public StructureType<?> type() {
        return ModernBetaStructureTypes.OCEAN_SHRINE;
    }
}
