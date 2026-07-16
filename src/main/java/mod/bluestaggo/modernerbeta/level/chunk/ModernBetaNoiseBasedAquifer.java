//? if >=26.3 {
/*package mod.bluestaggo.modernerbeta.level.chunk;

import net.minecraft.core.QuartPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

public class ModernBetaNoiseBasedAquifer extends Aquifer.NoiseBasedAquifer {
    private ModernBetaNoiseBasedAquifer(
        NoiseChunk noiseChunk,
        ChunkPos pos,
        Aquifer.Config config,
        PositionalRandomFactory positionalRandomFactory,
        int minBlockY,
        int yBlockSize,
        Aquifer.FluidPicker globalFluidPicker
    ) {
        super(noiseChunk, pos, config, positionalRandomFactory, minBlockY, yBlockSize, globalFluidPicker);
    }

    public static Aquifer create(
        NoiseChunk noiseChunk,
        ChunkPos pos,
        Aquifer.Config config,
        PositionalRandomFactory positionalRandomFactory,
        int minBlockY,
        int yBlockSize,
        FluidPicker fluidRule
    ) {
        return new NoiseBasedAquifer(noiseChunk, pos, config, positionalRandomFactory, minBlockY, yBlockSize, fluidRule);
    }

    @Override
    public int surfaceLevel(int blockX, int blockZ) {
        int quantizedX = QuartPos.toBlock(QuartPos.fromBlock(blockX));
        int quantizedZ = QuartPos.toBlock(QuartPos.fromBlock(blockZ));

        //undoes a change done in 26.3, where the aquifer has its own preliminary surface level density function
        return this.surfaceLevelCache.computeIfAbsent(ChunkPos.pack(quantizedX, quantizedZ),
            k -> Mth.floor(this.noiseChunk.computePreliminarySurfaceLevel(quantizedX, quantizedZ)));
    }
}
*///? }