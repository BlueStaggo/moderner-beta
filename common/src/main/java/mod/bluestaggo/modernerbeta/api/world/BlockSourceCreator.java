package mod.bluestaggo.modernerbeta.api.world;

import mod.bluestaggo.modernerbeta.api.world.blocksource.BlockSource;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsChunk;
import net.minecraft.util.math.random.RandomSplitter;

@FunctionalInterface
public interface BlockSourceCreator {
    BlockSource apply(ModernBetaSettingsChunk settingsChunk, RandomSplitter randomSplitter);
}
