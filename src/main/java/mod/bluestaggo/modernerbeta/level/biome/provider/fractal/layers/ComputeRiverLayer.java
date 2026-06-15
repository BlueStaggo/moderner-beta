package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.world.level.biome.Biomes;

import static mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeIds.*;

public class ComputeRiverLayer extends SingleParentLayer {
    public static final com.mojang.serialization.MapCodec<ComputeRiverLayer> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(Codec.BOOL.fieldOf("convertOceans").orElse(false).forGetter(layer -> layer.convertOceans))
            .apply(instance, ComputeRiverLayer::new)
    );

    private final boolean convertOceans;

    public ComputeRiverLayer(String id, long seed, String parent, boolean convertOceans) {
        super(id, seed, parent);
        this.convertOceans = convertOceans;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.COMPUTE_RIVER;
    }

    @Override
    protected ExtendedIdentifier generate(int x, int z) {
        ExtendedIdentifier base = this.parentLayer.sample(x, z);
        ExtendedIdentifier[] neighbors = this.parentLayer.sampleNeighbors(x, z);

        return (!this.convertOceans
            ? neighborsRiverBorder(neighbors, base)
            : ((base.isOf(Biomes.OCEAN) || neighborsContain(neighbors, OCEAN))
                || !allNeighborsEqual(neighbors, base)))
            ? RIVER : NULL;
    }

    private static boolean neighborsRiverBorder(ExtendedIdentifier[] neighbors, ExtendedIdentifier match) {
        byte matchType = getRiverType(match);
        for (ExtendedIdentifier neighbor : neighbors) {
            if (getRiverType(neighbor) != matchType) {
                return true;
            }
        }
        return false;
    }

    private static byte getRiverType(ExtendedIdentifier biome) {
        return RIVER_REGION_A.equals(biome) ? (byte)1
            : RIVER_REGION_B.equals(biome) ? (byte)2
            : biome.isOf(RANDOM.baseId()) ? (byte)(1 + (biome.ext().charAt(biome.ext().length() - 1) & 1))
            : (byte)0;
    }
}
