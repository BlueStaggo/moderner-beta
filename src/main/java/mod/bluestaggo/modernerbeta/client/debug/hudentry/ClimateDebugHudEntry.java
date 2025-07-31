package mod.bluestaggo.modernerbeta.client.debug.hudentry;

import mod.bluestaggo.modernerbeta.api.debug.DebugTextProvider2D;
import mod.bluestaggo.modernerbeta.api.debug.DebugTextProvider3D;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
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
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ClimateDebugHudEntry /*? >=1.21.9 {*/ /*implements DebugHudEntry *//*?}*/ {
    //? if >=1.21.9 {
    /*private static final Identifier SECTION_ID = ModernerBeta.createId("climate");

    @Override
    public void render(DebugHudLines lines, @Nullable World world, @Nullable WorldChunk clientChunk, @Nullable WorldChunk chunk) {
        MinecraftClient client = MinecraftClient.getInstance();
        Entity entity = client.getCameraEntity();
        if (entity == null)
            return;

        BlockPos pos = entity.getBlockPos();

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        lines.addLinesToSection(SECTION_ID, getLines(world, x, y, z));
    }
    *///?}

    public static Collection<String> getLines(World world, int x, int y, int z) {
        if (!(world instanceof ServerWorld serverWorld))
            return List.of();

        ChunkGenerator chunkGenerator = serverWorld.getChunkManager().getChunkGenerator();
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
