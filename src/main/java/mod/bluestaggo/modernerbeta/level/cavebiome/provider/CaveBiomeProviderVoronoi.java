package mod.bluestaggo.modernerbeta.level.cavebiome.provider;

import mod.bluestaggo.modernerbeta.api.level.cavebiome.CaveBiomeProvider;
import mod.bluestaggo.modernerbeta.api.level.cavebiome.climate.CaveClimateSampler;
import mod.bluestaggo.modernerbeta.api.level.cavebiome.climate.CaveClime;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.CaveBiomeVoronoi;
import mod.bluestaggo.modernerbeta.settings.component.PerlinNoiseSettings;
import mod.bluestaggo.modernerbeta.util.noise.PerlinOctaveNoise;
import mod.bluestaggo.modernerbeta.level.biome.voronoi.VoronoiPointCaveBiome;
import mod.bluestaggo.modernerbeta.level.biome.voronoi.VoronoiPointRules;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class CaveBiomeProviderVoronoi extends CaveBiomeProvider implements CaveClimateSampler {
    private final VoronoiCaveClimateSampler climateSampler;
    private final VoronoiPointRules<Holder<Biome>, CaveClime> rules;

    public CaveBiomeProviderVoronoi(ModernBetaSettings settings, HolderGetter<Biome> biomeRegistry, long seed) {
        super(settings, biomeRegistry, seed);

        CaveBiomeVoronoi voronoi = this.settings.getOrDefault(SettingsComponentTypes.CAVE_BIOME_VORONOI);
        this.climateSampler = new VoronoiCaveClimateSampler(
            seed,
            voronoi.verticalScale(),
            voronoi.horizontalScale(),
            voronoi.depthMinY(),
            voronoi.depthMaxY()
        );
        this.rules = buildRules(voronoi.points());
    }

    @Override
    public Holder<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
        CaveClime clime = this.sample(biomeX, biomeY, biomeZ);
        return this.rules.calculateClosestTo(clime);
    }
    
    @Override
    public Set<Holder<Biome>> getBiomes() {
        return new HashSet<>(this.rules.getItems());
    }

    @Override
    public CaveClime sample(int x, int y, int z) {
        return this.climateSampler.sample(x, y, z);
    }
    
    private static VoronoiPointRules<Holder<Biome>, CaveClime> buildRules(List<VoronoiPointCaveBiome> points) {
        VoronoiPointRules.Builder<Holder<Biome>, CaveClime> builder = new VoronoiPointRules.Builder<>();
        
        for (VoronoiPointCaveBiome point : points) {
            Holder<Biome> biomeKey = point.biome().isEmpty() ? null : point.biome().get();
            
            double temp = Mth.clamp(point.temp(), 0.0, 1.0);
            double rain = Mth.clamp(point.rain(), 0.0, 1.0);
            double depth = Mth.clamp(point.depth(), 0.0, 1.0);
            
            builder.add(biomeKey, new CaveClime(temp, rain, depth));
        }
        
        return builder.build();
    }

    @Override
    public String getDebugText(int x, int y, int z) {
        return this.climateSampler.getDebugText(x >> 2, y >> 2, z >> 2);
    }

    private static class VoronoiCaveClimateSampler {
        private final PerlinOctaveNoise tempOctaveNoise;
        private final PerlinOctaveNoise rainOctaveNoise;
        private final PerlinOctaveNoise detailOctaveNoise;
        
        private final float verticalScale;
        private final float horizontalScale;
        
        private final int depthMinY;
        private final int depthMaxY;
        
        public VoronoiCaveClimateSampler(long seed, float verticalScale, float horizontalScale, int depthMinY, int depthMaxY) {
            this.tempOctaveNoise = new PerlinOctaveNoise(new Random(seed * 9871L), 2, PerlinNoiseSettings.DEFAULT);
            this.rainOctaveNoise = new PerlinOctaveNoise(new Random(seed * 39811L), 2, PerlinNoiseSettings.DEFAULT);
            this.detailOctaveNoise = new PerlinOctaveNoise(new Random(seed * 543321L), 1, PerlinNoiseSettings.DEFAULT);
            
            this.verticalScale = verticalScale;
            this.horizontalScale = horizontalScale;
            
            this.depthMinY = depthMinY >> 2;
            this.depthMaxY = depthMaxY >> 2;
        }
        
        public CaveClime sample(int x, int y, int z) {
            // 1 Octave range: -0.6240559817912857/0.6169702737066762
            // 2 Octave range: -1.4281536012354779/1.4303502066204832
            // 4 Octave range: -7.6556244276339145/7.410194314594666
            
            double tempNoise = this.tempOctaveNoise.sample(
                x / (double)this.horizontalScale,
                y / (double)this.verticalScale,
                z / (double)this.horizontalScale
            );
            
            double rainNoise = this.rainOctaveNoise.sample(
                x / (double)this.horizontalScale, 
                y / (double)this.verticalScale, 
                z / (double)this.horizontalScale
            );
            
            double detailNoise = this.detailOctaveNoise.sample(
                x / (double)this.horizontalScale, 
                y / (double)this.verticalScale, 
                z / (double)this.horizontalScale
            );
            
            tempNoise /= 1.4D;
            rainNoise /= 1.4D;
            detailNoise /= 0.55D;
            
            tempNoise = tempNoise * 0.99D + detailNoise * 0.01D;
            rainNoise = rainNoise * 0.98D + detailNoise * 0.02D;
            
            tempNoise = (tempNoise + 1.0) / 2D;
            rainNoise = (rainNoise + 1.0) / 2D;
            
            int depthHeight = this.depthMaxY - this.depthMinY;
            double depth = Mth.clamp(y, this.depthMinY, this.depthMaxY);
            
            depth -= this.depthMinY;
            depth /= depthHeight;
            
            return new CaveClime(
                Mth.clamp(tempNoise, 0.0, 1.0),
                Mth.clamp(rainNoise, 0.0, 1.0),
                Mth.clamp(depth, 0.0, 1.0)
            );
        }

        private String getDebugText(int x, int y, int z) {
            CaveClime clime = this.sample(x, y, z);
            double temp = clime.temp();
            double rain = clime.rain();

            return String.format("Cave Climate Temp: %.3f Rainfall: %.3f", temp, rain);
        }
    }
}
