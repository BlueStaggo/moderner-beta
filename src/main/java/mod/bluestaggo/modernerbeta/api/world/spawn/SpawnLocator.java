package mod.bluestaggo.modernerbeta.api.world.spawn;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelHeightAccessor;

import java.util.Optional;

public interface SpawnLocator {
    Optional<BlockPos> locateSpawn(LevelHeightAccessor level);
    
    SpawnLocator DEFAULT = level -> Optional.empty();
}
