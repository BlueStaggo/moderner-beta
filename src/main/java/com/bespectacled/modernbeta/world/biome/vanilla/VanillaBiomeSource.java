package com.bespectacled.modernbeta.world.biome.vanilla;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.mixin.MixinVanillaBiomeParametersAccessor;
import com.bespectacled.modernbeta.world.gen.OldGeneratorConfig;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.ParameterRange;
import net.minecraft.world.gen.NoiseColumnSampler;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.random.ChunkRandom;

public class VanillaBiomeSource {
    private static final GenerationShapeConfig SHAPE_CONFIG = OldGeneratorConfig.BETA_SHAPE_CONFIG;
    
    private static final boolean GEN_NOISE_CAVES = false;
    private static final ChunkRandom.RandomProvider RANDOM_TYPE = ChunkRandom.RandomProvider.XOROSHIRO;
    
    private final MultiNoiseUtil.Entries<Supplier<Biome>> biomeEntries;
    private final MultiNoiseBiomeSource biomeSource;
    private final NoiseColumnSampler columnSampler;
    private final long seed;
    
    private VanillaBiomeSource(ImmutableList<Pair<MultiNoiseUtil.NoiseHypercube, Supplier<Biome>>> biomeList, long seed) {
        this.biomeEntries = new MultiNoiseUtil.Entries<Supplier<Biome>>(biomeList);
        this.biomeSource = new MultiNoiseBiomeSource(this.biomeEntries, Optional.empty());
        this.columnSampler = new NoiseColumnSampler(
            SHAPE_CONFIG,
            GEN_NOISE_CAVES,
            seed,
            BuiltinRegistries.NOISE_PARAMETERS,
            RANDOM_TYPE
        );
        this.seed = seed;
    }
    
    public Biome getBiome(int biomeX, int biomeY, int biomeZ) {
        return this.biomeSource.getBiome(biomeX, biomeY, biomeZ, this.columnSampler);
    }
    
    public MultiNoiseUtil.Entries<Supplier<Biome>> getBiomeEntries() {
        return this.biomeEntries;
    }
    
    public List<Biome> getBiomes() {
        return this.biomeEntries.getEntries().stream().map(p -> p.getSecond().get()).toList();
    }
    
    public long getSeed() {
        return this.seed;
    }
    
    public static class Builder {
        private final ImmutableList.Builder<Pair<MultiNoiseUtil.NoiseHypercube, Supplier<Biome>>> builder;
        private final ExtendedVanillaBiomeParameters biomeParameters;
        private final Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters;
        private final long seed;
        
        public Builder(Registry<Biome> biomeRegistry, long seed) {
            this.builder = ImmutableList.builder(); 
            this.biomeParameters = new ExtendedVanillaBiomeParameters();
            this.parameters = pair -> this.builder.add(pair.mapSecond(key -> () -> biomeRegistry.getOrThrow(key)));
            this.seed = seed;
        }
        
        public Builder writeMixedBiomes(ParameterRange range) {
            MixinVanillaBiomeParametersAccessor invoker = this.getInvoker();
            invoker.invokeWriteMixedBiomes(this.parameters, range);
            
            return this;
        }
        
        public Builder writePlainsBiomes(ParameterRange range) {
            MixinVanillaBiomeParametersAccessor invoker = this.getInvoker();
            invoker.invokeWritePlainsBiomes(this.parameters, range);
            
            return this;
        }
        
        public Builder writeMountainousBiomes(ParameterRange range) {
            MixinVanillaBiomeParametersAccessor invoker = this.getInvoker();
            invoker.invokeWriteMountainousBiomes(this.parameters, range);
            
            return this;
        }
        
        public Builder writeOceanBiomes() {
            MixinVanillaBiomeParametersAccessor invoker = this.getInvoker();
            invoker.invokeWriteOceanBiomes(this.parameters);
            
            return this;
        }
        
        public Builder writeDeepOceanBiomes() {
            this.biomeParameters.writeDeepOceanBiomes(this.parameters);
            
            return this;
        }
        
        public VanillaBiomeSource build() {
            return new VanillaBiomeSource(this.builder.build(), this.seed);
        }
        
        private MixinVanillaBiomeParametersAccessor getInvoker() {
            return ((MixinVanillaBiomeParametersAccessor)(Object)this.biomeParameters);
        }
    }
}
