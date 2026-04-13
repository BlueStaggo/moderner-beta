//~dotLocation
package mod.bluestaggo.modernerbeta.level.biome.voronoi;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomes;
import net.minecraft.resources.ResourceLocation;

public record VoronoiPointBiome(ResourceLocation biome, double temp, double rain, double weird) {
    public static final Codec<VoronoiPointBiome> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("biome").forGetter(VoronoiPointBiome::biome),
            Codec.DOUBLE.fieldOf("temp").forGetter(VoronoiPointBiome::temp),
            Codec.DOUBLE.fieldOf("rain").forGetter(VoronoiPointBiome::rain),
            Codec.DOUBLE.fieldOf("weird").forGetter(VoronoiPointBiome::weird)
        ).apply(instance, VoronoiPointBiome::new)
    );

    public static final VoronoiPointBiome DEFAULT = new VoronoiPointBiome(ModernBetaBiomes.BETA_FOREST.location(), 0.5, 0.5);
    
    public VoronoiPointBiome(ResourceLocation biome, double temp, double rain) {
        this(biome, temp, rain, 0.5);
    }
}
