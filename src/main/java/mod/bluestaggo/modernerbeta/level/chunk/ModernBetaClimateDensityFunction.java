package mod.bluestaggo.modernerbeta.level.chunk;

import com.mojang.serialization.MapCodec;
import mod.bluestaggo.modernerbeta.api.level.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.Clime;
//? if <26.3
import net.minecraft.util.KeyDispatchDataCodec;
//? if >=26.3 {
/*import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
*///? }
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.function.Supplier;

public final class ModernBetaClimateDensityFunction implements DensityFunction {
    private final Supplier<BiomeProvider> biomeProvider;
    private final DensityFunction fallback;
    private final Type type;

    public ModernBetaClimateDensityFunction(Supplier<BiomeProvider> biomeProvider, DensityFunction fallback, Type type) {
        this.biomeProvider = biomeProvider;
        this.fallback = fallback;
        this.type = type;
    }

    //? if <26.3 {
    @Override
    public double compute(FunctionContext context) {
        BiomeProvider provider = this.biomeProvider.get();
        if (provider instanceof ClimateSampler sampler) {
            return this.type.sample(sampler.sample(context.blockX(), context.blockZ())) * 2.0 - 1.0;
        }

        return this.fallback.compute(context);
    }

    @Override
    public void fillArray(double[] densities, ContextProvider context) {
        context.fillAllDirectly(densities, this);
    }

    //? if <26.2 {
    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new ModernBetaClimateDensityFunction(
            this.biomeProvider,
            this.fallback.mapAll(visitor),
            this.type
        ));
    }
    //? } else {
    /*@Override
    public DensityFunction mapChildren(Visitor visitor) {
        return new ModernBetaClimateDensityFunction(
            this.biomeProvider,
            visitor.apply(this.fallback),
            this.type
        );
    }
    *///? }

    @Override
    public double minValue() {
        return Math.min(-1.0, this.fallback.minValue());
    }

    @Override
    public double maxValue() {
        return Math.max(1.0, this.fallback.maxValue());
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return KeyDispatchDataCodec.of(MapCodec.unit(this));
    }
    //? } else {
    /*@Override
    public DensitySampler compileSampler(CompileContext context) {
        return new Sampler(this.biomeProvider, this.fallback.compileSampler(context), this.type);
    }

    @Override
    public DensityFunction rewriteChildren(net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule rule) {
        DensityFunction rewrittenFallback = rule.rewrite(this.fallback);
        return rewrittenFallback == this.fallback
            ? this
            : new ModernBetaClimateDensityFunction(this.biomeProvider, rewrittenFallback, this.type);
    }

    @Override
    public Interval range() {
        return Interval.encapsulating(Interval.ofSymmetric(1.0F), this.fallback.range());
    }

    @Override
    public int domainAxes() {
        return AXIS_X | AXIS_Z | this.fallback.domainAxes();
    }

    @Override
    public MapCodec<? extends DensityFunction> codec() {
        return MapCodec.unit(this);
    }

    private record Sampler(
        Supplier<BiomeProvider> biomeProvider,
        DensitySampler fallback,
        Type type
    ) implements DensitySampler {
        @Override
        public void sampleVolume(SamplerContext context, DensityBuffer buffer, DensityVolume volume) {
            DensitySampler.sampleVolumeNaive(context, buffer, volume, this);
        }

        @Override
        public float sampleValue(SamplerContext context, int x, int y, int z) {
            BiomeProvider provider = this.biomeProvider.get();
            if (provider instanceof ClimateSampler sampler) {
                return (float)(this.type.sample(sampler.sample(x, z)) * 2.0 - 1.0);
            }

            return this.fallback.sampleValue(context, x, y, z);
        }
    }
    *///? }

    public enum Type {
        TEMPERATURE,
        HUMIDITY,
        WEIRDNESS;

        private double sample(Clime clime) {
            return switch (this) {
                case TEMPERATURE -> clime.temp();
                case HUMIDITY -> clime.rain();
                case WEIRDNESS -> clime.weird();
            };
        }
    }
}
