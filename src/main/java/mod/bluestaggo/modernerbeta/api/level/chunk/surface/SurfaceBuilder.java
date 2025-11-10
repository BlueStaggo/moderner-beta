package mod.bluestaggo.modernerbeta.api.level.chunk.surface;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;

import java.util.LinkedHashMap;
import java.util.Map;

public class SurfaceBuilder {    
    private final Map<Holder<Biome>, SurfaceConfig> surfaceConfigs;
    private final HolderLookup<SurfaceConfig> surfaceConfigRegistry;
    
    public SurfaceBuilder(BiomeSource biomeSource, HolderLookup<SurfaceConfig> surfaceConfigRegistry) {
        this.surfaceConfigs = new LinkedHashMap<>();
        this.surfaceConfigRegistry = surfaceConfigRegistry;
        
        this.initMap(biomeSource, surfaceConfigRegistry);
    }
    
    public SurfaceConfig getSurfaceConfig(Holder<Biome> biome) {
        return this.surfaceConfigs.computeIfAbsent(biome, (k) -> SurfaceConfig.getSurfaceConfig(biome, this.surfaceConfigRegistry));
    }
    
    private void initMap(BiomeSource biomeSource, HolderLookup<SurfaceConfig> surfaceConfigRegistry) {
        biomeSource.possibleBiomes().stream().forEach(biome -> {
            this.surfaceConfigs.put(biome, SurfaceConfig.getSurfaceConfig(biome, surfaceConfigRegistry));
        });
    }
}
