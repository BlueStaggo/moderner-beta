package mod.bluestaggo.modernerbeta.level.biome.provider;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.level.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.api.level.biome.BiomeResolverBlock;
import mod.bluestaggo.modernerbeta.api.level.biome.BiomeResolverOcean;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.ClimateSamplerSky;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.Clime;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.ClimateDistribution;
import mod.bluestaggo.modernerbeta.settings.component.ClimateScale;
import mod.bluestaggo.modernerbeta.settings.component.PerlinNoiseSettings;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkCache;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkClimate;
import mod.bluestaggo.modernerbeta.util.random.mersenne.MTRandom;
import mod.bluestaggo.modernerbeta.util.noise.PerlinOctaveNoise;
import mod.bluestaggo.modernerbeta.level.biome.provider.climate.ClimateMap;
import mod.bluestaggo.modernerbeta.level.biome.provider.climate.ClimateMapping;
import mod.bluestaggo.modernerbeta.level.biome.provider.climate.ClimateType;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BiomeProviderPE extends BiomeProvider implements ClimateSampler, ClimateSamplerSky, BiomeResolverBlock, BiomeResolverOcean {
    private final ClimateMap climateMap;
    private final PEClimateSampler climateSampler;
    private final ClimateDistribution distribution;

    public BiomeProviderPE(ModernBetaSettings settings, HolderGetter<Biome> biomeRegistry, long seed) {
        super(settings, biomeRegistry, seed);

        ClimateScale climateScale = this.settings.getOrDefault(SettingsComponentTypes.CLIMATE_SCALE);
        Map<String, ClimateMapping> climateMappings = this.settings.getOrDefault(SettingsComponentTypes.CLIMATE_MAPPINGS);

        this.climateMap = new ClimateMap(climateMappings);
        this.climateSampler = new PEClimateSampler(
            this.seed,
            climateScale.temp(),
            climateScale.rain(),
            climateScale.detail()
        );

        this.distribution = settings.getOrDefault(SettingsComponentTypes.CLIMATE_DISTRIBUTION);
    }
    
    @Override
    public Holder<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
        int x = biomeX << 2;
        int z = biomeZ << 2;
        
        Clime clime = this.climateSampler.sample(x, z);
        double temp = clime.temp();
        double rain = clime.rain();
        
        return this.biomeRegistry.getOrThrow(this.climateMap.getBiome(temp, rain, ClimateType.LAND));
    }
 
    @Override
    public Holder<Biome> getOceanBiome(int biomeX, int biomeY, int biomeZ) {
        int x = biomeX << 2;
        int z = biomeZ << 2;
        
        Clime clime = this.climateSampler.sample(x, z);
        double temp = clime.temp();
        double rain = clime.rain();
        
        return this.biomeRegistry.getOrThrow(this.climateMap.getBiome(temp, rain, ClimateType.OCEAN));
    }
    
    @Override
    public Holder<Biome> getDeepOceanBiome(int biomeX, int biomeY, int biomeZ) {
        int x = biomeX << 2;
        int z = biomeZ << 2;
        
        Clime clime = this.climateSampler.sample(x, z);
        double temp = clime.temp();
        double rain = clime.rain();
        
        return this.biomeRegistry.getOrThrow(this.climateMap.getBiome(temp, rain, ClimateType.DEEP_OCEAN));
    }
    
    @Override
    public Holder<Biome> getBiomeBlock(int x, int y, int z) {
        Clime clime = this.climateSampler.sample(x, z);
        double temp = clime.temp();
        double rain = clime.rain();
        
        return this.biomeRegistry.getOrThrow(this.climateMap.getBiome(temp, rain, ClimateType.LAND));
    }

    @Override
    public List<Holder<Biome>> getBiomes() {
        return this.climateMap
            .getBiomeKeys()
            .stream()
            .map(i -> this.biomeRegistry.getOrThrow(i))
            .collect(Collectors.toList());
    }

    @Override
    public double sampleSky(int x, int z) {
        return this.climateSampler.sampleSky(x, z);
    }

    @Override
    public Clime sample(int x, int z) {
        return this.climateSampler.sample(x, z);
    }
    
    @Override
    public boolean useBiomeColor() {
        return ModernerBeta.config.getOrDefault(SettingsComponentTypes.CONFIG_PE_CLIMATIC_COLORS).vegetation();
    }
    
    @Override
    public boolean useSkyColor() {
        return ModernerBeta.config.getOrDefault(SettingsComponentTypes.CONFIG_PE_CLIMATIC_COLORS).sky();
    }
    
    @Override
    public boolean useWaterColor() {
        return ModernerBeta.config.getOrDefault(SettingsComponentTypes.CONFIG_PE_CLIMATIC_COLORS).water();
    }

    @Override
    public ClimateDistribution getDistribution() {
        return this.distribution;
    }

    @Override
    public String getDebugText(int x, int z) {
        return this.climateSampler.getDebugText(x, z);
    }

    private static class PEClimateSampler {
        private final PerlinOctaveNoise tempOctaveNoise;
        private final PerlinOctaveNoise rainOctaveNoise;
        private final PerlinOctaveNoise detailOctaveNoise;
        
        private final ChunkCache<ChunkClimate> chunkCacheClimate;
        
        private final double tempNoiseScale;
        private final double rainNoiseScale;
        private final double detailNoiseScale;
        
        public PEClimateSampler(long seed, double tempNoiseScale, double rainNoiseScale, double detailNoiseScale) {
            this.tempOctaveNoise = new PerlinOctaveNoise(new MTRandom(seed * 9871L), 4, PerlinNoiseSettings.DEFAULT);
            this.rainOctaveNoise = new PerlinOctaveNoise(new MTRandom(seed * 39811L), 4, PerlinNoiseSettings.DEFAULT);
            this.detailOctaveNoise = new PerlinOctaveNoise(new MTRandom(seed * 543321L), 2, PerlinNoiseSettings.DEFAULT);
            
            this.chunkCacheClimate = new ChunkCache<>(
                "climate",
                (chunkX, chunkZ) -> new ChunkClimate(chunkX, chunkZ, this::sampleNoise)
            );
            
            this.tempNoiseScale = tempNoiseScale;
            this.rainNoiseScale = rainNoiseScale;
            this.detailNoiseScale = detailNoiseScale;
        }
        
        public Clime sample(int x, int z) {
            int chunkX = x >> 4;
            int chunkZ = z >> 4;
            
            return this.chunkCacheClimate.get(chunkX, chunkZ).sampleClime(x, z);
        }
        
        public double sampleSky(int x, int z) {
            int chunkX = x >> 4;
            int chunkZ = z >> 4;
            
            return this.chunkCacheClimate.get(chunkX, chunkZ).sampleClime(x, z).temp();
        }
        
        private Clime sampleNoise(int x, int z) {
            double temp = this.tempOctaveNoise.sampleXZ(x, z, this.tempNoiseScale, this.tempNoiseScale);
            double rain = this.rainOctaveNoise.sampleXZ(x, z, this.rainNoiseScale, this.rainNoiseScale);
            double detail = this.detailOctaveNoise.sampleXZ(x, z, this.detailNoiseScale, this.detailNoiseScale);

            detail = detail * 1.1D + 0.5D;

            temp = (temp * 0.15D + 0.7D) * 0.99D + detail * 0.01D;
            rain = (rain * 0.15D + 0.5D) * 0.998D + detail * 0.002D;

            temp = 1.0D - (1.0D - temp) * (1.0D - temp);
            
            return new Clime(Mth.clamp(temp, 0.0, 1.0), Mth.clamp(rain, 0.0, 1.0));
        }

        private String getDebugText(int x, int z) {
            Clime clime = this.sample(x, z);
            double temp = clime.temp();
            double rain = clime.rain();

            return String.format("Climate Temp: %.3f Rainfall: %.3f", temp, rain);
        }
    }
}
