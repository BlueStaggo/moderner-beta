package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;

public class ModalZoomLayer extends FuzzyZoomLayer {
    public static final com.mojang.serialization.MapCodec<ModalZoomLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .apply(instance, ModalZoomLayer::new)
    );

    public ModalZoomLayer(String id, long seed, String parent) {
        super(id, seed, parent);
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.MODAL_ZOOM;
    }

    @Override
    protected ExtendedBiomeId interpolate(LayerRandom random, ExtendedBiomeId a, ExtendedBiomeId b, ExtendedBiomeId c, ExtendedBiomeId d) {
        boolean ab = a.equals(b);
        boolean ac = a.equals(c);
        boolean ad = a.equals(d);
        boolean bc = b.equals(c);
        boolean bd = b.equals(d);
        boolean cd = c.equals(d);
        if (bc && cd) {
            return b;
        } else if (ab && ac || ab && ad || ac && ad || ab && !cd || ac && !bd || ad && !bc) {
            return a;
        } else if (bc && !ad || bd && !ac) {
            return b;
        } else if (cd && !ab) {
            return c;
        } else {
            return super.interpolate(random, a, b, c, d);
        }
    }
}
