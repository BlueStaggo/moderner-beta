package mod.bluestaggo.modernerbeta.client.debug.entries;

import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProviderNoise;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkHeightmap;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
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
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DebugEntryHeightmap /*? >=1.21.9 {*/ /*implements DebugScreenEntry *//*?}*/ {
    //? if >=1.21.9 {
    /*private static final ResourceLocation SECTION_ID = ModernerBeta.createId("heightmap");

    @Override
    public void display(DebugScreenDisplayer lines, @Nullable Level level, @Nullable LevelChunk clientChunk, @Nullable LevelChunk chunk) {
        Minecraft client = Minecraft.getInstance();
        Entity entity = client.getCameraEntity();
        if (entity == null)
            return;

        BlockPos pos = entity.blockPosition();

        int x = pos.getX();
        int z = pos.getZ();

        lines.addToGroup(SECTION_ID, getLines(level, x, z));
    }
    *///?}

    public static Collection<String> getLines(Level level, int x, int z) {
        if (!(level instanceof ServerLevel serverLevel))
            return List.of();

        ChunkGenerator chunkGenerator = serverLevel.getChunkSource().getGenerator();

        List<String> lines = new ArrayList<>();
        if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator) {
            ChunkProvider chunkProvider = modernBetaChunkGenerator.getChunkProvider();

            lines.add(
                    String.format(
                            "[Modern Beta] Chunk Provider WS height: %d OF height: %d Sea level: %d",
                            chunkProvider.getHeight(level, x, z, Heightmap.Types.WORLD_SURFACE_WG),
                            chunkProvider.getHeight(level, x, z, Heightmap.Types.OCEAN_FLOOR),
                            chunkProvider.getSeaLevel()
                    )
            );

            if (chunkProvider instanceof ChunkProviderNoise noiseChunkProvider) {
                lines.add(
                        String.format(
                                "[Modern Beta] Noise Chunk Provider WSF height: %d",
                                noiseChunkProvider.getHeight(level, x, z, ChunkHeightmap.Type.SURFACE_FLOOR)
                        )
                );
            }
        }

        return lines;
    }
}
