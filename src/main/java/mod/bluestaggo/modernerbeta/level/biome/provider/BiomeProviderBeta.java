package mod.bluestaggo.modernerbeta.level.biome.provider;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.level.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.api.level.biome.BiomeResolverBlock;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.ClimateSamplerSky;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.Clime;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.ClimateDistribution;
import mod.bluestaggo.modernerbeta.settings.component.ClimateScale;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkCache;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkClimate;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkClimateSky;
import mod.bluestaggo.modernerbeta.util.noise.SimplexOctaveNoise;
import mod.bluestaggo.modernerbeta.level.biome.provider.climate.ClimateMap;
import mod.bluestaggo.modernerbeta.level.biome.provider.climate.ClimateMapping;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;

import java.util.Map;
import java.util.Random;
import java.util.Set;

public class BiomeProviderBeta extends BiomeProvider implements ClimateSampler, ClimateSamplerSky, BiomeResolverBlock {
    private final ClimateMap climateMap;
    private final BetaClimateSampler climateSampler;
    private final BetaClimateSamplerSky climateSamplerSky;
    private final ClimateDistribution distribution;

    public BiomeProviderBeta(ModernBetaSettings settings, HolderGetter<Biome> biomeRegistry, long seed) {
        super(settings, biomeRegistry, seed);

        ClimateScale climateScale = this.settings.getOrDefault(SettingsComponentTypes.CLIMATE_SCALE);
        Map<String, ClimateMapping> climateMappings = this.settings.getOrDefault(SettingsComponentTypes.CLIMATE_MAPPINGS);

        this.climateMap = new ClimateMap(climateMappings);
        this.climateSampler = new BetaClimateSampler(
            this.seed,
            climateScale.temp(),
            climateScale.rain(),
            climateScale.detail()
        );
        this.climateSamplerSky = new BetaClimateSamplerSky(
            this.seed,
            climateScale.temp()
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
        
        return this.climateMap.getBiome(temp, rain);
    }
    
    @Override
    public Holder<Biome> getBiomeBlock(int x, int y, int z) {
        Clime clime = this.climateSampler.sample(x, z);
        double temp = clime.temp();
        double rain = clime.rain();
        
        return this.climateMap.getBiome(temp, rain);
    }

    @Override
    public Set<Holder<Biome>> getBiomes() {
        return this.climateMap.getBiomes();
    }

    @Override
    public double sampleSky(int x, int z) {
        return this.climateSamplerSky.sample(x, z);
    }

    @Override
    public Clime sample(int x, int z) {
        return this.climateSampler.sample(x, z);
    }
    
    @Override
    public boolean useBiomeColor() {
        return ModernerBeta.config.getOrDefault(SettingsComponentTypes.CONFIG_BETA_CLIMATIC_COLORS).vegetation();
    }
    
    @Override
    public boolean useSkyColor() {
        return ModernerBeta.config.getOrDefault(SettingsComponentTypes.CONFIG_BETA_CLIMATIC_COLORS).sky();
    }
    
    @Override
    public boolean useWaterColor() {
        return ModernerBeta.config.getOrDefault(SettingsComponentTypes.CONFIG_BETA_CLIMATIC_COLORS).water();
    }

    @Override
    public ClimateDistribution getDistribution() {
        return this.distribution;
    }

    @Override
    public String getDebugText(int x, int z) {
        return climateSampler.getDebugText(x, z);
    }

    private static class BetaClimateSampler {
        private final SimplexOctaveNoise tempOctaveNoise;
        private final SimplexOctaveNoise rainOctaveNoise;
        private final SimplexOctaveNoise detailOctaveNoise;
        
        private final ChunkCache<ChunkClimate> chunkCacheClimate;
        
        private final double tempNoiseScale;
        private final double rainNoiseScale;
        private final double detailNoiseScale;
        
        public BetaClimateSampler(long seed, double tempNoiseScale, double rainNoiseScale, double detailNoiseScale) {
            this.tempOctaveNoise = new SimplexOctaveNoise(new Random(seed * 9871L), 4);
            this.rainOctaveNoise = new SimplexOctaveNoise(new Random(seed * 39811L), 4);
            this.detailOctaveNoise = new SimplexOctaveNoise(new Random(seed * 543321L), 2);
            
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
        
        public Clime sampleNoise(int x, int z) {
            double temp = this.tempOctaveNoise.sampleXZ(x, z, this.tempNoiseScale, this.tempNoiseScale, 1.0 / 4.0);
            double rain = this.rainOctaveNoise.sampleXZ(x, z, this.rainNoiseScale, this.rainNoiseScale, 1.0 / 3.0);
            double detail = this.detailOctaveNoise.sampleXZ(x, z, this.detailNoiseScale, this.detailNoiseScale, 1.0 / 1.7);

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

    private static class BetaClimateSamplerSky {
        private final SimplexOctaveNoise tempOctaveNoise;
        
        private final ChunkCache<ChunkClimateSky> chunkCacheClimateSky;
        
        private final double tempNoiseScale;
        
        public BetaClimateSamplerSky(long seed, double tempNoiseScale) {
            this.tempOctaveNoise = new SimplexOctaveNoise(new Random(seed * 9871L), 4);
            
            this.chunkCacheClimateSky = new ChunkCache<>(
                "sky",
                (chunkX, chunkZ) -> new ChunkClimateSky(chunkX, chunkZ, this::sampleNoise)
            );
            
            this.tempNoiseScale = tempNoiseScale;
        }
        
        public double sample(int x, int z) {
            int chunkX = x >> 4;
            int chunkZ = z >> 4;
            
            return this.chunkCacheClimateSky.get(chunkX, chunkZ).sampleTemp(x, z);
        }
        
        private double sampleNoise(int x, int z) {
            return this.tempOctaveNoise.sampleXZ(x, z, this.tempNoiseScale, this.tempNoiseScale, 0.5D);
        }
    }
}
