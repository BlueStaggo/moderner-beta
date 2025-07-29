package mod.bluestaggo.modernerbeta.client.debug.text;

import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.Clime;
import mod.bluestaggo.modernerbeta.api.world.cavebiome.climate.CaveClimateSampler;
import mod.bluestaggo.modernerbeta.api.world.cavebiome.climate.CaveClime;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
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
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClimateDebugText /*? >=1.21.9 {*/ /*implements class_11632 *//*?}*/ {
    //? if >=1.21.9 {
    /*private static final Identifier TEXT_ID = ModernerBeta.createId("climate");

    @Override
    public void method_72751(class_11630 arg, @Nullable World world, @Nullable WorldChunk worldChunk, @Nullable WorldChunk worldChunk2) {
        MinecraftClient client = MinecraftClient.getInstance();
        Entity entity = client.getCameraEntity();
        if (entity == null)
            return;

        BlockPos pos = entity.getBlockPos();

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        arg.method_72744(TEXT_ID, getLines(world, x, y, z));
    }
    *///?}

    public static Collection<String> getLines(World world, int x, int y, int z) {
        if (!(world instanceof ServerWorld serverWorld))
            return List.of();

        ChunkGenerator chunkGenerator = serverWorld.getChunkManager().getChunkGenerator();
        BiomeSource biomeSource = chunkGenerator.getBiomeSource();

        List<String> lines = new ArrayList<>();
        if (biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
            if (modernBetaBiomeSource.getBiomeProvider() instanceof ClimateSampler climateSampler) {
                Clime clime = climateSampler.sample(x, z);
                double temp = clime.temp();
                double rain = clime.rain();

                lines.add(
                        String.format(
                                "[Modern Beta] Climate Temp: %.3f Rainfall: %.3f",
                                temp,
                                rain
                        )
                );
            }

            if (modernBetaBiomeSource.getCaveBiomeProvider() instanceof CaveClimateSampler climateSampler) {
                CaveClime clime = climateSampler.sample(x >> 2, y >> 2, z >> 2);
                double temp = clime.temp();
                double rain = clime.rain();

                lines.add(
                        String.format(
                                "[Modern Beta] Cave Climate Temp: %.3f Rainfall: %.3f",
                                temp,
                                rain
                        )
                );
            }
        }

        return lines;
    }
}
