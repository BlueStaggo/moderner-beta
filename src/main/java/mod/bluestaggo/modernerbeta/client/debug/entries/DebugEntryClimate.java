package mod.bluestaggo.modernerbeta.client.debug.entries;

import mod.bluestaggo.modernerbeta.api.debug.DebugTextProvider2D;
import mod.bluestaggo.modernerbeta.api.debug.DebugTextProvider3D;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
//? if >=1.21.9 {
/*import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;
*///?}
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DebugEntryClimate /*? >=1.21.9 {*/ /*implements DebugScreenEntry *//*?}*/ {
    //? if >=1.21.9 {
    /*private static final ResourceLocation SECTION_ID = ModernerBeta.createId("climate");

    @Override
    public void display(DebugScreenDisplayer lines, @Nullable Level level, @Nullable LevelChunk clientChunk, @Nullable LevelChunk chunk) {
        Minecraft client = Minecraft.getInstance();
        Entity entity = client.getCameraEntity();
        if (entity == null)
            return;

        BlockPos pos = entity.blockPosition();

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        lines.addToGroup(SECTION_ID, getLines(level, x, y, z));
    }
    *///?}

    public static Collection<String> getLines(Level level, int x, int y, int z) {
        if (!(level instanceof ServerLevel serverLevel))
            return List.of();

        ChunkGenerator chunkGenerator = serverLevel.getChunkSource().getGenerator();
        BiomeSource biomeSource = chunkGenerator.getBiomeSource();

        List<String> lines = new ArrayList<>();
        if (biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
            if (modernBetaBiomeSource.getBiomeProvider() instanceof DebugTextProvider2D provider2D) {
                lines.add("[Modern Beta] " + provider2D.getDebugText(x, z));
            }

            if (modernBetaBiomeSource.getCaveBiomeProvider() instanceof DebugTextProvider3D provider3D) {
                lines.add("[Modern Beta] " + provider3D.getDebugText(x, y, z));
            }
        }

        return lines;
    }
}
