package mod.bluestaggo.modernerbeta.api.level.blocksource;

import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface BlockSource {
    BlockSource DEFAULT = (x, y, z) -> null;
    
    BlockState apply(int x, int y, int z);

    default int priority() {
        return 0;
    }
}
