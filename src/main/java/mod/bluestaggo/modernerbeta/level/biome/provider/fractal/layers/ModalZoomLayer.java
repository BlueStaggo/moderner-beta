package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.util.VersionCompat;

public class ModalZoomLayer extends FuzzyZoomLayer {
    public static final com.mojang.serialization.MapCodec<ModalZoomLayer> CODEC = VersionCompat.createMaybeMapCodec(
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
            return this.interpolate(
                random,
                this.parentLayer.sample(halfX, halfZ),
                this.parentLayer.sample(halfX + 1, halfZ),
                this.parentLayer.sample(halfX, halfZ + 1),
                this.parentLayer.sample(halfX + 1, halfZ + 1)
            );
        }
    }

    private ExtendedIdentifier interpolate(LayerRandom random, ExtendedIdentifier a, ExtendedIdentifier b, ExtendedIdentifier c, ExtendedIdentifier d) {
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
}
