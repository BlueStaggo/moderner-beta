package mod.bluestaggo.modernerbeta.client.debug.entries;

import mod.bluestaggo.modernerbeta.world.biome.injector.BiomeInjector;
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
public class DebugEntryInjectedBiome /*? >=1.21.9 {*/ /*implements DebugScreenEntry *//*?}*/ {
    //? if >=1.21.9 {
    /*@Override
    public void display(DebugScreenDisplayer lines, @Nullable Level level, @Nullable LevelChunk clientChunk, @Nullable LevelChunk chunk) {
        Minecraft client = Minecraft.getInstance();
        Entity entity = client.getCameraEntity();
        if (entity == null)
            return;

        BlockPos pos = entity.blockPosition();

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        String line = getLine(level, x, y, z);
        if (!line.isEmpty())
            lines.addLine(line);
    }
    *///?}

    public static String getLine(Level level, int x, int y, int z) {
        if (!(level instanceof ServerLevel serverLevel))
            return "";

        ChunkGenerator chunkGenerator = serverLevel.getChunkSource().getGenerator();

        if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator &&
                modernBetaChunkGenerator.getBiomeInjector() != null) {
            String biome = modernBetaChunkGenerator.getBiomeInjector().getBiomeNameAtBlock(level, x, y, z, null, BiomeInjector.BiomeInjectionStep.ALL);
            return String.format("[Modern Beta] Injected biome: %s", biome);
        }

        return "";
    }
}
