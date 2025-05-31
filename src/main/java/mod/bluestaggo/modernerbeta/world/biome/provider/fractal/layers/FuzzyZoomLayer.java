package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;

public class FuzzyZoomLayer extends BaseZoomLayer {
    public static final MapCodec<FuzzyZoomLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .apply(instance, FuzzyZoomLayer::new)
    );

    public FuzzyZoomLayer(String id, long seed, String parent) {
        super(id, seed, parent);
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.FUZZY_ZOOM;
    }

    @Override
    protected ExtendedBiomeId interpolate(LayerRandom random, ExtendedBiomeId a, ExtendedBiomeId b) {
        return random.nextInt(2) == 0 ? a : b;
    }

    @Override
    protected ExtendedBiomeId interpolate(LayerRandom random, ExtendedBiomeId a, ExtendedBiomeId b, ExtendedBiomeId c, ExtendedBiomeId d) {
        int choice = random.nextInt(4);
        return switch (choice) {
            case 0 -> a;
            case 1 -> b;
            case 2 -> c;
            case 3 -> d;
            default -> throw new IllegalStateException("Unexpected value: " + choice);
        };
    }
}
