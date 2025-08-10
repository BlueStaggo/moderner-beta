package mod.bluestaggo.modernerbeta.client.debug.hudentry;

import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProviderNoise;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkHeightmap;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
//? if >=1.21.9 {
/*import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.client.gui.hud.debug.DebugHudEntry;
import net.minecraft.client.gui.hud.debug.DebugHudLines;
import net.minecraft.util.Identifier;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
*///?}
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Environment(EnvType.CLIENT)
public class HeightmapDebugHudEntry /*? >=1.21.9 {*/ /*implements DebugHudEntry *//*?}*/ {
    //? if >=1.21.9 {
    /*private static final Identifier SECTION_ID = ModernerBeta.createId("heightmap");

    @Override
    public void render(DebugHudLines lines, @Nullable World world, @Nullable WorldChunk clientChunk, @Nullable WorldChunk chunk) {
        MinecraftClient client = MinecraftClient.getInstance();
        Entity entity = client.getCameraEntity();
        if (entity == null)
            return;

        BlockPos pos = entity.getBlockPos();

        int x = pos.getX();
        int z = pos.getZ();

        lines.addLinesToSection(SECTION_ID, getLines(world, x, z));
    }
    *///?}

    public static Collection<String> getLines(World world, int x, int z) {
        if (!(world instanceof ServerWorld serverWorld))
            return List.of();

        ChunkGenerator chunkGenerator = serverWorld.getChunkManager().getChunkGenerator();

        List<String> lines = new ArrayList<>();
        if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator) {
            ChunkProvider chunkProvider = modernBetaChunkGenerator.getChunkProvider();

            lines.add(
                    String.format(
                            "[Modern Beta] Chunk Provider WS height: %d OF height: %d Sea level: %d",
                            chunkProvider.getHeight(world, x, z, Heightmap.Type.WORLD_SURFACE_WG),
                            chunkProvider.getHeight(world, x, z, Heightmap.Type.OCEAN_FLOOR),
                            chunkProvider.getSeaLevel()
                    )
            );

            if (chunkProvider instanceof ChunkProviderNoise noiseChunkProvider) {
                lines.add(
                        String.format(
                                "[Modern Beta] Noise Chunk Provider WSF height: %d",
                                noiseChunkProvider.getHeight(world, x, z, ChunkHeightmap.Type.SURFACE_FLOOR)
                        )
                );
            }
        }

        return lines;
    }
}
