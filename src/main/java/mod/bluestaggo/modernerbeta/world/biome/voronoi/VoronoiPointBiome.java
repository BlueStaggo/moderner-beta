//~dotLocation
package mod.bluestaggo.modernerbeta.world.biome.voronoi;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomes;
import net.minecraft.resources.ResourceLocation;

public record VoronoiPointBiome(ResourceLocation biome, ResourceLocation oceanBiome, ResourceLocation deepOceanBiome, double temp, double rain, double weird) {
    public static final Codec<VoronoiPointBiome> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("biome").forGetter(VoronoiPointBiome::biome),
            ResourceLocation.CODEC.fieldOf("oceanBiome").forGetter(VoronoiPointBiome::oceanBiome),
            ResourceLocation.CODEC.fieldOf("deepOceanBiome").forGetter(VoronoiPointBiome::deepOceanBiome),
            Codec.DOUBLE.fieldOf("temp").forGetter(VoronoiPointBiome::temp),
            Codec.DOUBLE.fieldOf("rain").forGetter(VoronoiPointBiome::rain),
            Codec.DOUBLE.fieldOf("weird").forGetter(VoronoiPointBiome::weird)
        ).apply(instance, VoronoiPointBiome::new)
    );

    public static final VoronoiPointBiome DEFAULT = new VoronoiPointBiome(ModernBetaBiomes.BETA_FOREST.location(), ModernBetaBiomes.BETA_OCEAN.location(), 0.5, 0.5);
    
    public VoronoiPointBiome(ResourceLocation biome, ResourceLocation oceanBiome, double temp, double rain) {
        this(biome, oceanBiome, oceanBiome, temp, rain, 0.5);
    }
}
