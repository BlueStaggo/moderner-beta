package mod.bluestaggo.modernerbeta.world.biome.voronoi;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record VoronoiPointCaveBiome(String biome, double temp, double rain, double depth) {
    public static final Codec<VoronoiPointCaveBiome> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.STRING.fieldOf("biome").forGetter(VoronoiPointCaveBiome::biome),
            Codec.DOUBLE.fieldOf("temp").forGetter(VoronoiPointCaveBiome::temp),
            Codec.DOUBLE.fieldOf("rain").forGetter(VoronoiPointCaveBiome::rain),
            Codec.DOUBLE.fieldOf("depth").forGetter(VoronoiPointCaveBiome::depth)
        ).apply(instance, VoronoiPointCaveBiome::new)
    );

    public static final VoronoiPointCaveBiome DEFAULT = new VoronoiPointCaveBiome("minecraft:lush_caves", 0.5, 0.5, 0.0);

    public static final List<VoronoiPointCaveBiome> DEFAULT_POINTS = List.of(
        new VoronoiPointCaveBiome("", 0.0, 0.5, 0.75),
        new VoronoiPointCaveBiome("minecraft:lush_caves", 0.1, 0.5, 0.75),
        new VoronoiPointCaveBiome("", 0.5, 0.5, 0.75),
        new VoronoiPointCaveBiome("minecraft:dripstone_caves", 0.9, 0.5, 0.75),
        new VoronoiPointCaveBiome("", 1.0, 0.5, 0.75),

        new VoronoiPointCaveBiome("", 0.0, 0.5, 0.25),
        new VoronoiPointCaveBiome("minecraft:lush_caves", 0.2, 0.5, 0.25),
        new VoronoiPointCaveBiome("", 0.4, 0.5, 0.25),
        new VoronoiPointCaveBiome("minecraft:deep_dark", 0.5, 0.5, 0.25),
        new VoronoiPointCaveBiome("", 0.6, 0.5, 0.25),
        new VoronoiPointCaveBiome("minecraft:dripstone_caves", 0.8, 0.5, 0.25),
        new VoronoiPointCaveBiome("", 1.0, 0.5, 0.25)
    );
}
