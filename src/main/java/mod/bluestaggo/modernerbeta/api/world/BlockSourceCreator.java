package mod.bluestaggo.modernerbeta.api.world;

import mod.bluestaggo.modernerbeta.api.world.blocksource.BlockSource;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import net.minecraft.util.math.random.RandomSplitter;

@FunctionalInterface
public interface BlockSourceCreator {
    BlockSource apply(ModernBetaSettings chunkSettings, RandomSplitter randomSplitter);
}
