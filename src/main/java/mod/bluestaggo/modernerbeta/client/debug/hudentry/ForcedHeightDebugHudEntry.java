package mod.bluestaggo.modernerbeta.client.debug.hudentry;

import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProviderForcedHeight;
import mod.bluestaggo.modernerbeta.world.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
//? if >=1.21.9 {
/*import net.minecraft.client.gui.hud.debug.DebugHudEntry;
import net.minecraft.client.gui.hud.debug.DebugHudLines;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
*///?}
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.chunk.ChunkGenerator;

@Environment(EnvType.CLIENT)
public class ForcedHeightDebugHudEntry /*? >=1.21.9 {*/ /*implements DebugHudEntry *//*?}*/ {
    //? if >=1.21.9 {
    /*@Override
    public void render(DebugHudLines lines, @Nullable World world, @Nullable WorldChunk clientChunk, @Nullable WorldChunk chunk) {
        MinecraftClient client = MinecraftClient.getInstance();
        Entity entity = client.getCameraEntity();
        if (entity == null)
            return;

        BlockPos pos = entity.getBlockPos();

        int x = pos.getX();
        int z = pos.getZ();

        String line = getLine(world, x, z);
        if (!line.isEmpty())
            lines.addLine(line);
    }
    *///?}

    public static String getLine(World world, int x, int z) {
        if (!(world instanceof ServerWorld serverWorld))
            return "";

        ChunkGenerator chunkGenerator = serverWorld.getChunkManager().getChunkGenerator();

        if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator) {
            ChunkProvider chunkProvider = modernBetaChunkGenerator.getChunkProvider();

            if (chunkProvider instanceof ChunkProviderForcedHeight forcedHeightChunkProvider) {
                HeightConfig heightConfig = forcedHeightChunkProvider.getRawHeightConfigAt(x >> 2, z >> 2);
                return String.format(
                        String.format(
                                "[Modern Beta] Forced Height Chunk Provider height: %s",
                                heightConfig.toString()
                        )
                );
            }
        }

        return "";
    }
}
