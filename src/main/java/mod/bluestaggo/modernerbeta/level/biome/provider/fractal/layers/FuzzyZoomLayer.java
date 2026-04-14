package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.util.VersionCompat;

public class FuzzyZoomLayer extends SingleParentLayer {
    public static final com.mojang.serialization.MapCodec<FuzzyZoomLayer> CODEC = VersionCompat.createMaybeMapCodec(
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
    protected ExtendedIdentifier generate(int x, int z) {
        int xHalf = x & 1;
        int zHalf = z & 1;

        int halfX = x >> 1;
        int halfZ = z >> 1;

        ExtendedIdentifier biome00 = this.parentLayer.sample(halfX, halfZ);
        if (xHalf == 0 && zHalf == 0) {
            return biome00;
        }

        LayerRandom random = this.getRandom(halfX << 1, halfZ << 1);
        if (xHalf == 0) {
            return random.nextInt(2) == 1 ? this.parentLayer.sample(halfX, halfZ + 1) : biome00;
        } else if (zHalf == 0) {
            random.consumeCount(1);
            return random.nextInt(2) == 1 ? this.parentLayer.sample(halfX + 1, halfZ) : biome00;
        } else {
            random.consumeCount(2);
            int choice = random.nextInt(4);
            return switch (choice) {
                case 0 -> biome00;
                case 1 -> this.parentLayer.sample(halfX + 1, halfZ);
                case 2 -> this.parentLayer.sample(halfX, halfZ + 1);
                case 3 -> this.parentLayer.sample(halfX + 1, halfZ + 1);
                default -> throw new IllegalStateException("Unexpected value: " + choice);
            };
        }
    }
}
