package mod.bluestaggo.modernerbeta.world.structure;

import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.structure.processor.BlockRotStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class OceanShrineStructurePiece extends SimpleStructurePiece {
    public OceanShrineStructurePiece(StructureTemplateManager manager, BlockPos pos, Identifier template, BlockRotation rot) {
        super(ModernBetaStructurePieceTypes.OCEAN_SHRINE, 0, manager, template, template.toString(), getPlacementData(rot), pos);
    }

    public OceanShrineStructurePiece(StructureTemplateManager manager, NbtCompound tag) {
        super(ModernBetaStructurePieceTypes.OCEAN_SHRINE, tag, manager, identifier ->
            getPlacementData(BlockRotation.valueOf(tag.getString("Rot")
                //? if >=1.21.5
                .orElseThrow()
            ))
        );
    }

    private static StructurePlacementData getPlacementData(BlockRotation rotation) {
        return new StructurePlacementData().setRotation(rotation).setMirror(BlockMirror.NONE).addProcessor(BlockIgnoreStructureProcessor.IGNORE_AIR_AND_STRUCTURE_BLOCKS);
    }

    @Override
    protected void writeNbt(StructureContext context, NbtCompound nbtCompound) {
        super.writeNbt(context, nbtCompound);
        nbtCompound.putString("Rot", this.placementData.getRotation().name());
    }

    @Override
    protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox) {
        if (metadata.equals("chest")) {
            world.setBlockState(pos, Blocks.CHEST.getDefaultState().with(ChestBlock.WATERLOGGED, world.getFluidState(pos).isIn(FluidTags.WATER)), 2);

            if (world.getBlockEntity(pos) instanceof ChestBlockEntity chestBlockEntity) {
                chestBlockEntity.setLootTable(LootTables.BURIED_TREASURE_CHEST, random.nextLong());
            }
        }
    }

    @Override
    public void generate(
        StructureWorldAccess world,
        StructureAccessor accessor,
        ChunkGenerator chunkGenerator,
        Random random,
        BlockBox blockBox,
        ChunkPos chunkPos,
        BlockPos blockPos
    ) {
        this.placementData.clearProcessors()
                .addProcessor(new BlockRotStructureProcessor(1.0f))
                .addProcessor(BlockIgnoreStructureProcessor.IGNORE_AIR_AND_STRUCTURE_BLOCKS);

        super.generate(world, accessor, chunkGenerator, random, blockBox, chunkPos, blockPos);
    }
}
