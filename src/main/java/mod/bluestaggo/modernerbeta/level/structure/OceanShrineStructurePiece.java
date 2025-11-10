package mod.bluestaggo.modernerbeta.level.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class OceanShrineStructurePiece extends TemplateStructurePiece {
    public OceanShrineStructurePiece(StructureTemplateManager manager, BlockPos pos, ResourceLocation template, Rotation rot) {
        super(ModernBetaStructurePieceTypes.OCEAN_SHRINE, 0, manager, template, template.toString(), getPlacementData(rot), pos);
    }

    public OceanShrineStructurePiece(StructureTemplateManager manager, CompoundTag tag) {
        super(ModernBetaStructurePieceTypes.OCEAN_SHRINE, tag, manager, identifier ->
            getPlacementData(Rotation.valueOf(tag.getString("Rot")
                //? if >=1.21.5
                .orElseThrow()
            ))
        );
    }

    private static StructurePlaceSettings getPlacementData(Rotation rotation) {
        return new StructurePlaceSettings().setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag nbtCompound) {
        super.addAdditionalSaveData(context, nbtCompound);
        nbtCompound.putString("Rot", this.placeSettings.getRotation().name());
    }

    @Override
    protected void handleDataMarker(String metadata, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox boundingBox) {
        if (metadata.equals("chest")) {
            level.setBlock(pos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.WATERLOGGED, level.getFluidState(pos).is(FluidTags.WATER)), 2);

            if (level.getBlockEntity(pos) instanceof ChestBlockEntity chestBlockEntity) {
                chestBlockEntity.setLootTable(BuiltInLootTables.BURIED_TREASURE, random.nextLong());
            }
        }
    }

    @Override
    public void postProcess(
        WorldGenLevel level,
        StructureManager manager,
        ChunkGenerator chunkGenerator,
        RandomSource random,
        BoundingBox boundingBox,
        ChunkPos chunkPos,
        BlockPos blockPos
    ) {
        this.placeSettings.clearProcessors()
                .addProcessor(new BlockRotProcessor(1.0f))
                .addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);

        super.postProcess(level, manager, chunkGenerator, random, boundingBox, chunkPos, blockPos);
    }
}
