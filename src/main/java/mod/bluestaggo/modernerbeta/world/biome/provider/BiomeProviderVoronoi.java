package mod.bluestaggo.modernerbeta.world.biome.provider;

import mod.bluestaggo.modernerbeta.api.world.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.api.world.biome.BiomeResolverOcean;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.Clime;
import mod.bluestaggo.modernerbeta.api.debug.DebugTextProvider2D;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.ClimateScale;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkCache;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkClimate;
import mod.bluestaggo.modernerbeta.util.noise.SimplexOctaveNoise;
import mod.bluestaggo.modernerbeta.world.biome.provider.climate.ClimateMapping;
import mod.bluestaggo.modernerbeta.world.biome.provider.climate.ClimateType;
import mod.bluestaggo.modernerbeta.world.biome.voronoi.VoronoiPointBiome;
import mod.bluestaggo.modernerbeta.world.biome.voronoi.VoronoiPointRules;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BiomeProviderVoronoi extends BiomeProvider implements BiomeResolverOcean, DebugTextProvider2D {
    private final VoronoiClimateSampler climateSampler;
    private final VoronoiPointRules<ClimateMapping, Clime> rules;
    
    public BiomeProviderVoronoi(ModernBetaSettings settings, HolderGetter<Biome> biomeRegistry, long seed) {
        super(settings, biomeRegistry, seed);

        List<VoronoiPointBiome> voronoiPoints = this.getSettings().getOrThrow(SettingsComponentTypes.VORONOI_POINTS);
        ClimateScale climateScale = this.getSettings().getOrDefault(SettingsComponentTypes.CLIMATE_SCALE);

        this.climateSampler = new VoronoiClimateSampler(
            seed,
            climateScale.temp(),
            climateScale.rain(),
            climateScale.detail(),
            climateScale.weird()
        );
        this.rules = buildRules(voronoiPoints);
    }

    @Override
    public Holder<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
        ClimateMapping climateMapping = this.getClimateMapping(biomeX, biomeZ);
        
        return this.biomeRegistry.getOrThrow(climateMapping.getBiome(ClimateType.LAND));
    }
 
    @Override
    public Holder<Biome> getOceanBiome(int biomeX, int biomeY, int biomeZ) {
        ClimateMapping climateMapping = this.getClimateMapping(biomeX, biomeZ);
        
        return this.biomeRegistry.getOrThrow(climateMapping.getBiome(ClimateType.OCEAN));
    }
    
    @Override
    public Holder<Biome> getDeepOceanBiome(int biomeX, int biomeY, int biomeZ) {
        ClimateMapping climateMapping = this.getClimateMapping(biomeX, biomeZ);
        
        return this.biomeRegistry.getOrThrow(climateMapping.getBiome(ClimateType.DEEP_OCEAN));
    }

    @Override
    public List<Holder<Biome>> getBiomes() {
        List<ResourceLocation> biomes = new ArrayList<>();

        this.rules.getItems().forEach(key -> {
            biomes.add(key.biome());
            biomes.add(key.oceanBiome());
            biomes.add(key.deepOceanBiome());
        });
        
        return biomes
            .stream()
            .distinct()
            .map(key -> this.biomeRegistry.getOrThrow(ResourceKey.create(Registries.BIOME, key)))
            .collect(Collectors.toList());
    }
    
    private ClimateMapping getClimateMapping(int biomeX, int biomeZ) {
        int x = biomeX << 2;
        int z = biomeZ << 2;
        
        Clime clime = this.climateSampler.sample(x, z);

        return this.rules.calculateClosestTo(clime);
    }
    
    private static VoronoiPointRules<ClimateMapping, Clime> buildRules(List<VoronoiPointBiome> points) {
        VoronoiPointRules.Builder<ClimateMapping, Clime> builder = new VoronoiPointRules.Builder<>();
        
        for (VoronoiPointBiome point : points) {
            ResourceLocation biome = point.biome();
            ResourceLocation oceanBiome = point.oceanBiome();
            ResourceLocation deepOceanBiome = point.deepOceanBiome();
            
            double temp = Mth.clamp(point.temp(), 0.0, 1.0);
            double rain = Mth.clamp(point.rain(), 0.0, 1.0);
            double weird = Mth.clamp(point.weird(), 0.0, 1.0);
            
            ClimateMapping climateMapping = new ClimateMapping(biome, oceanBiome, deepOceanBiome);
            Clime clime = new Clime(temp, rain, weird);
            
            builder.add(climateMapping, clime);
        }
        
        return builder.build();
    }

    @Override
    public String getDebugText(int x, int z) {
        return this.climateSampler.getDebugText(x, z);
    }

    private static class VoronoiClimateSampler {
        private final SimplexOctaveNoise tempOctaveNoise;
        private final SimplexOctaveNoise rainOctaveNoise;
        private final SimplexOctaveNoise detailOctaveNoise;
        private final SimplexOctaveNoise weirdOctaveNoise;
        
        private final ChunkCache<ChunkClimate> chunkCacheClimate;
        
        private final double tempNoiseScale;
        private final double rainNoiseScale;
        private final double detailNoiseScale;
        private final double weirdNoiseScale;
        
        public VoronoiClimateSampler(long seed, double tempNoiseScale, double rainNoiseScale, double detailNoiseScale, double weirdNoiseScale) {
            this.tempOctaveNoise = new SimplexOctaveNoise(new Random(seed * 9871L), 4);
            this.rainOctaveNoise = new SimplexOctaveNoise(new Random(seed * 39811L), 4);
            this.detailOctaveNoise = new SimplexOctaveNoise(new Random(seed * 543321L), 2);
            this.weirdOctaveNoise = new SimplexOctaveNoise(new Random(seed * 134714L), 2);
            
            this.chunkCacheClimate = new ChunkCache<>(
                "climate",
                (chunkX, chunkZ) -> new ChunkClimate(chunkX, chunkZ, this::sampleNoise)
            );
            
            this.tempNoiseScale = tempNoiseScale;
            this.rainNoiseScale = rainNoiseScale;
            this.detailNoiseScale = detailNoiseScale;
            this.weirdNoiseScale = weirdNoiseScale;
        }

        public Clime sample(int x, int z) {
            int chunkX = x >> 4;
            int chunkZ = z >> 4;
            
            return this.chunkCacheClimate.get(chunkX, chunkZ).sampleClime(x, z);
        }
        
        public Clime sampleNoise(int x, int z) {
            double temp = this.tempOctaveNoise.sample(x, z, this.tempNoiseScale, 0.25D);
            double rain = this.rainOctaveNoise.sample(x, z, this.rainNoiseScale, 0.33333333333333331D);
            double detail = this.detailOctaveNoise.sample(x, z, this.detailNoiseScale, 0.58823529411764708D);
            double weird = this.weirdOctaveNoise.sample(x, z, this.weirdNoiseScale, 0.2941176471D);

            detail = detail * 1.1D + 0.5D;
            weird = (weird / 1.525D + 1.0D) / 2.0D;

            temp = (temp * 0.15D + 0.7D) * 0.99D + detail * 0.01D;
            rain = (rain * 0.15D + 0.5D) * 0.998D + detail * 0.002D;

            temp = 1.0D - (1.0D - temp) * (1.0D - temp);
            
            temp = Mth.clamp(temp, 0.0, 1.0);
            rain = Mth.clamp(rain, 0.0, 1.0);
            weird = Mth.clamp(weird, 0.0, 1.0);
            
            return new Clime(temp, rain, weird);
        }

        private String getDebugText(int x, int z) {
            Clime clime = this.sample(x, z);
            double temp = clime.temp();
            double rain = clime.rain();
            double weird = clime.weird();

            return String.format("Climate Temp: %.3f Rainfall: %.3f Weirdness: %.3f", temp, rain, weird);
        }
    }
}
