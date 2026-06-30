//? if >=26.3 {
/*package mod.bluestaggo.modernerbeta.level.carver;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;

public class ReferenceCarvingMask extends CarvingMask {
    private final ChunkAccess chunk;

    public ReferenceCarvingMask(ChunkAccess chunk, int minY, int maxY) {
        super(minY, maxY);
        this.chunk = chunk;
    }

    public Block getBlockAt(BlockPos pos) {
        return this.chunk.getBlockState(pos).getBlock();
    }

    public Block getBlockAt(int x, int y, int z) {
        return this.getBlockAt(new BlockPos(x, y, z));
    }
}
*///? }