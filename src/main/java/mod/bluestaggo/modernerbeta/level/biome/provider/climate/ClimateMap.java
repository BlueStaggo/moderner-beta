package mod.bluestaggo.modernerbeta.level.biome.provider.climate;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.*;

public class ClimateMap {
    private final Map<String, ClimateMapping> climateMap;
    private final ClimateMapping[] climateTable;
    
    public ClimateMap(Map<String, ClimateMapping> climateMappings) {
        this.climateMap = new LinkedHashMap<>(climateMappings);
        this.climateTable = new ClimateMapping[4096];
        this.generateBiomeLookup();
    }
    
    public Map<String, ClimateMapping> getMap() {
        return new LinkedHashMap<>(this.climateMap);
    }
    
    public Holder<Biome> getBiome(double temp, double rain) {
        int t = (int) (temp * 63D);
        int r = (int) (rain * 63D);

        return this.climateTable[t + r * 64].biome();
    }
    
    public Set<Holder<Biome>> getBiomes() {
        Set<Holder<Biome>> biomes = new HashSet<>();
        
        this.climateMap.values().forEach(mapping ->
                biomes.add(mapping.biome()));
        
        return biomes;
    }
    
    private void generateBiomeLookup() {
        for (int t = 0; t < 64; t++) {
            for (int r = 0; r < 64; r++) {
                this.climateTable[t + r * 64] = this.getBiome((float) t / 63F, (float) r / 63F);
            }
        }
    }
    
    private ClimateMapping getBiome(float temp, float rain) {
        rain *= temp;

        if (temp < 0.1F) {
            return this.climateMap.get("ice_desert");
        }

        if (rain < 0.2F) {
            if (temp < 0.5F) {
                return this.climateMap.get("tundra");
            }
            if (temp < 0.95F) {
                return this.climateMap.get("savanna");
            } else {
                return this.climateMap.get("desert");
            }
        }

        if (rain > 0.5F && temp < 0.7F) {
            return this.climateMap.get("swampland");
        }

        if (temp < 0.5F) {
            return this.climateMap.get("taiga");
        }

        if (temp < 0.97F) {
            if (rain < 0.35F) {
                return this.climateMap.get("shrubland");
            } else {
                return this.climateMap.get("forest");
            }
        }

        if (rain < 0.45F) {
            return this.climateMap.get("plains");
        }

        if (rain < 0.9F) {
            return this.climateMap.get("seasonal_forest");
        } else {
            return this.climateMap.get("rainforest");
        }
    }
}
