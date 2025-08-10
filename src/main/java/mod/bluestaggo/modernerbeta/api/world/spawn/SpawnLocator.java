package mod.bluestaggo.modernerbeta.api.world.spawn;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.HeightLimitView;

import java.util.Optional;

public interface SpawnLocator {
    Optional<BlockPos> locateSpawn(HeightLimitView world);
    
    SpawnLocator DEFAULT = world -> Optional.empty();
}
