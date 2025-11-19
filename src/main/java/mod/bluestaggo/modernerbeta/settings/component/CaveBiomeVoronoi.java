package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.level.biome.voronoi.VoronoiPointCaveBiome;

import java.util.List;

public record CaveBiomeVoronoi(
    float horizontalScale,
    float verticalScale,
    int depthMinY,
    int depthMaxY,
    List<VoronoiPointCaveBiome> points
) {
    public static Codec<CaveBiomeVoronoi> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.FLOAT.fieldOf("horizontalScale").orElse(32.0f).forGetter(CaveBiomeVoronoi::horizontalScale),
            Codec.FLOAT.fieldOf("verticalScale").orElse(16.0f).forGetter(CaveBiomeVoronoi::verticalScale),
            Codec.INT.fieldOf("depthMinY").orElse(-64).forGetter(CaveBiomeVoronoi::depthMinY),
            Codec.INT.fieldOf("depthMaxY").orElse(64).forGetter(CaveBiomeVoronoi::depthMaxY),
            VoronoiPointCaveBiome.CODEC.listOf().fieldOf("points").orElse(VoronoiPointCaveBiome.DEFAULT_POINTS).forGetter(CaveBiomeVoronoi::points)
        ).apply(instance, CaveBiomeVoronoi::new)
    );
    public static final CaveBiomeVoronoi DEFAULT = CodecUtil.getDefaultByMap(CODEC);
}
