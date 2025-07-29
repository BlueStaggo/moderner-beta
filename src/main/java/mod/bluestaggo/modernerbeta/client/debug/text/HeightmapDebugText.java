package mod.bluestaggo.modernerbeta.client.debug.text;

import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProviderNoise;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkHeightmap;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
//? if >=1.21.9 {
/*import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.class_11630;
import net.minecraft.class_11632;
import net.minecraft.util.Identifier;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
*///?}
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HeightmapDebugText /*? >=1.21.9 {*/ /*implements class_11632 *//*?}*/ {
    //? if >=1.21.9 {
    /*private static final Identifier TEXT_ID = ModernerBeta.createId("heightmap");

    @Override
    public void method_72751(class_11630 arg, @Nullable World world, @Nullable WorldChunk worldChunk, @Nullable WorldChunk worldChunk2) {
        MinecraftClient client = MinecraftClient.getInstance();
        Entity entity = client.getCameraEntity();
        if (entity == null)
            return;

        BlockPos pos = entity.getBlockPos();

        int x = pos.getX();
        int z = pos.getZ();

        arg.method_72744(TEXT_ID, getLines(world, x, z));
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
                            chunkProvider.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG),
                            chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR),
                            chunkProvider.getSeaLevel()
                    )
            );

            if (chunkProvider instanceof ChunkProviderNoise noiseChunkProvider) {
                lines.add(
                        String.format(
                                "[Modern Beta] Noise Chunk Provider WSF height: %d",
                                noiseChunkProvider.getHeight(x, z, ChunkHeightmap.Type.SURFACE_FLOOR)
                        )
                );
            }
        }

        return lines;
    }
}
