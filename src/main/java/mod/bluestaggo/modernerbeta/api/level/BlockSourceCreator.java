package mod.bluestaggo.modernerbeta.api.level;

import mod.bluestaggo.modernerbeta.api.level.blocksource.BlockSource;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

@FunctionalInterface
public interface BlockSourceCreator {
    BlockSource apply(ModernBetaSettings chunkSettings, PositionalRandomFactory randomSplitter);
}
