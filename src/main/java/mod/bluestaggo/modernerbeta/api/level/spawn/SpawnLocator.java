//~minBuild
package mod.bluestaggo.modernerbeta.api.level.spawn;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Optional;

public interface SpawnLocator {
    Optional<BlockPos> locateSpawn(LevelHeightAccessor level);

    static Optional<BlockPos> getFinalSpawn(LevelHeightAccessor level, int x, int y, int z) {
        if (!(level instanceof LevelReader levelReader)) {
            return Optional.of(new BlockPos(x, y, z));
        }

        int minY = level.getMinY();
        levelReader.getChunk(x >> 4, z >> 4);
        BlockPos.MutableBlockPos spawnPos = new BlockPos.MutableBlockPos(x, y, z);

        if (y > minY &&
            blocksMotion(levelReader.getBlockState(spawnPos.below())) &&
            !blocksMotion(levelReader.getBlockState(spawnPos)) &&
            !blocksMotion(levelReader.getBlockState(spawnPos.above())) &&
            levelReader.getFluidState(spawnPos).isEmpty() &&
            levelReader.getFluidState(spawnPos.above()).isEmpty()
        ) {
            return Optional.of(spawnPos.immutable());
        }

        for (BlockPos.MutableBlockPos offset : BlockPos.spiralAround(BlockPos.ZERO, 16, Direction.EAST, Direction.SOUTH)) {
            int spawnX = x + offset.getX();
            int spawnZ = z + offset.getZ();
            levelReader.getChunk(spawnX >> 4, spawnZ >> 4);
            int spawnY = levelReader.getHeight(Heightmap.Types.OCEAN_FLOOR, spawnX, spawnZ);
            int surfaceY = levelReader.getHeight(Heightmap.Types.MOTION_BLOCKING, spawnX, spawnZ);
            spawnPos.set(spawnX, spawnY, spawnZ);

            if (spawnY > minY &&
                spawnY == surfaceY &&
                levelReader.getFluidState(spawnPos).isEmpty()
            ) {
                return Optional.of(spawnPos.immutable());
            }
        }

        return Optional.empty();
    }

    private static boolean blocksMotion(BlockState state) {
        //? if >=26.3 {
        /*return state.is(net.minecraft.tags.BlockTags.BLOCKS_MOTION_IN_HEIGHTMAP);
        *///? } else {
        return state.blocksMotion();
        //? }
    }
    
    SpawnLocator DEFAULT = level -> Optional.empty();
}
