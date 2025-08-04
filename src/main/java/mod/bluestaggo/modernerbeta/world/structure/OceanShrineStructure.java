package mod.bluestaggo.modernerbeta.world.structure;

import java.util.Optional;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

public class OceanShrineStructure extends Structure {
    public static final com.mojang.serialization.MapCodec<OceanShrineStructure> CODEC = createCodec(OceanShrineStructure::new);
    private static final Identifier SHRINE_BASE = ModernerBeta.createId("ocean_shrine/base");

    public OceanShrineStructure(Structure.Config config) {
        super(config);
    }

    @Override
    public Optional<StructurePosition> getStructurePosition(Context context) {
        int x = context.chunkPos().getStartX();
        int z = context.chunkPos().getStartZ();
        int y = context.chunkGenerator().getHeightInGround(x, z, Type.OCEAN_FLOOR_WG, context.world(), context.noiseConfig());

        BlockPos pos = new BlockPos(x, y, z);
        BlockRotation rot = BlockRotation.random(context.random());

        return Optional.of(new StructurePosition(pos, (collector) ->
            collector.addPiece(new OceanShrineStructurePiece(context.structureTemplateManager(), pos, SHRINE_BASE, rot))));
    }
    @Override
    public StructureType<?> getType() {
        return ModernBetaStructureTypes.OCEAN_SHRINE;
    }
}
