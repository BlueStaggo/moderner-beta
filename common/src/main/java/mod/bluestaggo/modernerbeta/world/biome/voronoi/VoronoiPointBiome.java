package mod.bluestaggo.modernerbeta.world.biome.voronoi;

import mod.bluestaggo.modernerbeta.util.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import java.util.List;

public record VoronoiPointBiome(String biome, String oceanBiome, String deepOceanBiome, double temp, double rain, double weird) {
    public static final VoronoiPointBiome DEFAULT = new VoronoiPointBiome("moderner_beta:beta_forest", "moderner_beta:beta_ocean", 0.5, 0.5);
    
    public VoronoiPointBiome(String biome, String oceanBiome, double temp, double rain) {
        this(biome, oceanBiome, oceanBiome, temp, rain, 0.5);
    }

    public static List<VoronoiPointBiome> listFromReader(NbtReader reader, List<VoronoiPointBiome> alternate) {
        if (reader.contains(NbtTags.VORONOI_POINTS)) {
            return reader.readListOrThrow(NbtTags.VORONOI_POINTS)
                .stream()
                .map(e -> {
                    NbtCompound point = NbtUtil.toCompoundOrThrow(e);
                    NbtReader pointReader = new NbtReader(point);
                    
                    String biome = pointReader.readStringOrThrow(NbtTags.BIOME);
                    String oceanBiome = pointReader.readStringOrThrow(NbtTags.OCEAN_BIOME);
                    String deepOceanBiome = pointReader.readStringOrThrow(NbtTags.DEEP_OCEAN_BIOME);
                    
                    double temp = pointReader.readDoubleOrThrow(NbtTags.TEMP);
                    double rain = pointReader.readDoubleOrThrow(NbtTags.RAIN);
                    double weird = pointReader.readDouble(NbtTags.WEIRD, 0.5);
                    
                    return new VoronoiPointBiome(
                        biome,
                        oceanBiome,
                        deepOceanBiome,
                        temp,
                        rain,
                        weird
                    );
                })
                .toList();
        }
        
        return List.copyOf(alternate);
    }
    
    public static NbtList listToNbt(List<VoronoiPointBiome> points) {
        NbtListBuilder builder = new NbtListBuilder();
        points.forEach(p -> builder.add(p.toCompound()));
        
        return builder.build();
    }
    
    public NbtCompound toCompound() {
        return new NbtCompoundBuilder()
            .putString(NbtTags.BIOME, this.biome)
            .putString(NbtTags.OCEAN_BIOME, this.oceanBiome)
            .putString(NbtTags.DEEP_OCEAN_BIOME, this.deepOceanBiome)
            .putDouble(NbtTags.TEMP, this.temp)
            .putDouble(NbtTags.RAIN, this.rain)
            .putDouble(NbtTags.WEIRD, this.weird)
            .build();
    }
}
