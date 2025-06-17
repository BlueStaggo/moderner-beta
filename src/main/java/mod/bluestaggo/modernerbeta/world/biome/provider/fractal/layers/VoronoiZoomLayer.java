package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import net.minecraft.util.math.MathHelper;

public class VoronoiZoomLayer extends FuzzyZoomLayer {
    public static final com.mojang.serialization.MapCodec<VoronoiZoomLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(Codec.INT.fieldOf("scale").orElse(4).forGetter(layer -> layer.scale))
            .apply(instance, VoronoiZoomLayer::new)
    );

    private final int scale;

    public VoronoiZoomLayer(String id, long seed, String parent, int scale) {
        super(id, seed, parent);
        this.scale = scale;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.VORONOI_ZOOM;
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        int voronoiZoom = this.scale;
        double voronoiFactor = voronoiZoom * 0.9D;

        int scaledX = Math.floorDiv(x, voronoiZoom);
        int scaledZ = Math.floorDiv(z, voronoiZoom);

        int subX = Math.floorMod(x, voronoiZoom);
        int subZ = Math.floorMod(z, voronoiZoom);

        LayerRandom voronoiRandom = this.getRandom(scaledX, scaledZ);
        double n00x = (voronoiRandom.nextInt(1024) / 1024.0D - 0.5D) * voronoiFactor;
        double n00z = (voronoiRandom.nextInt(1024) / 1024.0D - 0.5D) * voronoiFactor;
        voronoiRandom.init(scaledX + 1, scaledZ);
        double n10x = (voronoiRandom.nextInt(1024) / 1024.0D - 0.5D) * voronoiFactor + voronoiZoom;
        double n10z = (voronoiRandom.nextInt(1024) / 1024.0D - 0.5D) * voronoiFactor;
        voronoiRandom.init(scaledX, scaledZ + 1);
        double n01x = (voronoiRandom.nextInt(1024) / 1024.0D - 0.5D) * voronoiFactor;
        double n01z = (voronoiRandom.nextInt(1024) / 1024.0D - 0.5D) * voronoiFactor + voronoiZoom;
        voronoiRandom.init(scaledX + 1, scaledZ + 1);
        double n11x = (voronoiRandom.nextInt(1024) / 1024.0D - 0.5D) * voronoiFactor + voronoiZoom;
        double n11z = (voronoiRandom.nextInt(1024) / 1024.0D - 0.5D) * voronoiFactor + voronoiZoom;

        double dist00 = MathHelper.square(subX - n00x) + MathHelper.square(subZ - n00z);
        double dist10 = MathHelper.square(subX - n10x) + MathHelper.square(subZ - n10z);
        double dist01 = MathHelper.square(subX - n01x) + MathHelper.square(subZ - n01z);
        double dist11 = MathHelper.square(subX - n11x) + MathHelper.square(subZ - n11z);

        if (dist00 < dist10 && dist00 < dist01 && dist00 < dist11) {
            return this.parentLayer.sample(scaledX, scaledZ);
        } else if (dist10 < dist00 && dist10 < dist01 && dist10 < dist11) {
            return this.parentLayer.sample(scaledX + 1, scaledZ);
        } else if (dist01 < dist00 && dist01 < dist10 && dist01 < dist11) {
            return this.parentLayer.sample(scaledX, scaledZ + 1);
        } else {
            return this.parentLayer.sample(scaledX + 1, scaledZ + 1);
        }
    }
}
