package mod.bluestaggo.modernerbeta.client.debug.hudentry;

import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProviderForcedHeight;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
//? if >=1.21.9 {
/*import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;
*///?}
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;

@Environment(EnvType.CLIENT)
public class ExtendedBiomeDebugHudEntry /*? >=1.21.9 {*/ /*implements DebugScreenEntry *//*?}*/ {
    //? if >=1.21.9 {
    /*@Override
    public void display(DebugScreenDisplayer lines, @Nullable Level world, @Nullable LevelChunk clientChunk, @Nullable LevelChunk chunk) {
        Minecraft client = Minecraft.getInstance();
        Entity entity = client.getCameraEntity();
        if (entity == null)
            return;

        BlockPos pos = entity.blockPosition();

        int x = pos.getX();
        int z = pos.getZ();

        String line = getLine(world, x, z);
        if (!line.isEmpty())
            lines.addLine(line);
    }
    *///?}

    public static String getLine(Level world, int x, int z) {
        if (!(world instanceof ServerLevel serverWorld))
            return "";

        ChunkGenerator chunkGenerator = serverWorld.getChunkSource().getGenerator();

        if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator) {
            ChunkProvider chunkProvider = modernBetaChunkGenerator.getChunkProvider();
            if (chunkProvider instanceof ChunkProviderForcedHeight chunkProviderForcedHeight) {
                ExtendedBiomeId extendedBiomeId = chunkProviderForcedHeight.getExtendedBiomeId(x >> 2, z >> 2);
                return String.format(
                        "[Modern Beta] Extended biome: %s",
                        extendedBiomeId
                );
            }
        }

        return "";
    }
}
