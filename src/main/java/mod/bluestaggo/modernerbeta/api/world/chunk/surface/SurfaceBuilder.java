package mod.bluestaggo.modernerbeta.api.world.chunk.surface;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;

import java.util.LinkedHashMap;
import java.util.Map;

public class SurfaceBuilder {    
    private final Map<Holder<Biome>, SurfaceConfig> surfaceConfigs;
    
    public SurfaceBuilder(BiomeSource biomeSource) {
        this.surfaceConfigs = new LinkedHashMap<>();
        
        this.initMap(biomeSource);
    }
    
    public SurfaceConfig getSurfaceConfig(Holder<Biome> biome) {
        return this.surfaceConfigs.computeIfAbsent(biome, (k) -> SurfaceConfig.getSurfaceConfig(biome));
    }
    
    private void initMap(BiomeSource biomeSource) {
        biomeSource.possibleBiomes().stream().forEach(biome -> {
            this.surfaceConfigs.put(biome, SurfaceConfig.getSurfaceConfig(biome));
        });
    }
}
