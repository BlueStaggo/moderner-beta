//? if >=26.3 {
/*package mod.bluestaggo.modernerbeta.level.chunk;

import com.mojang.serialization.MapCodec;
import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProviderNoise;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkHeightmap;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

import java.util.Objects;
import java.util.function.Supplier;

public final class ModernBetaChunkSurfaceLevel implements DensityFunction {
    private static final int HEIGHT_OFFSET = -8;

    private final Supplier<ChunkProvider> chunkProvider;

    public ModernBetaChunkSurfaceLevel(Supplier<ChunkProvider> chunkProvider) {
        this.chunkProvider = chunkProvider;
    }

    @Override
    public DensitySampler compileSampler(CompileContext context) {
        return new Sampler(this.chunkProvider);
    }

    @Override
    public DensityFunction rewriteChildren(net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule rule) {
        return this;
    }

    @Override
    public Interval range() {
        return Interval.INFINITE;
    }

    @Override
    public int domainAxes() {
        return AXIS_X | AXIS_Z;
    }

    @Override
    public MapCodec<? extends DensityFunction> codec() {
        return MapCodec.unit(this);
    }

    private record Sampler(Supplier<ChunkProvider> chunkProvider) implements DensitySampler {
        @Override
        public void sampleVolume(SamplerContext context, DensityBuffer buffer, DensityVolume volume) {
            DensitySampler.sampleVolumeNaive(context, buffer, volume, this);
        }

        @Override
        public float sampleValue(SamplerContext context, int x, int y, int z) {
            ChunkProvider provider = Objects.requireNonNull(this.chunkProvider.get());
            int height = provider instanceof ChunkProviderNoise noiseProvider
                ? noiseProvider.getHeight(null, x, z, ChunkHeightmap.Type.SURFACE_FLOOR)
                : provider.getHeight(null, x, z, Heightmap.Types.OCEAN_FLOOR_WG);
            return height + HEIGHT_OFFSET;
        }
    }
}
*///? }
