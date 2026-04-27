//~dotLocation
package mod.bluestaggo.modernerbeta.level.biome.voronoi;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

public record VoronoiPointBiome(Holder<Biome> biome, double temp, double rain, double weird) {
    public static final Codec<VoronoiPointBiome> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Biome.CODEC.fieldOf("biome").forGetter(VoronoiPointBiome::biome),
            Codec.DOUBLE.fieldOf("temp").forGetter(VoronoiPointBiome::temp),
            Codec.DOUBLE.fieldOf("rain").forGetter(VoronoiPointBiome::rain),
            Codec.DOUBLE.fieldOf("weird").forGetter(VoronoiPointBiome::weird)
        ).apply(instance, VoronoiPointBiome::new)
    );

    public VoronoiPointBiome(Holder<Biome> biome, double temp, double rain) {
        this(biome, temp, rain, 0.5);
    }
}
