package mod.bluestaggo.modernerbeta.level.biome.provider.climate;

public enum ClimateType {
    LAND("biome"),
    OCEAN("oceanBiome"),
    DEEP_OCEAN("deepOceanBiome")
    ;
    
    public final String tag;
    
    private ClimateType(String tag) {
        this.tag = tag;
    }
}