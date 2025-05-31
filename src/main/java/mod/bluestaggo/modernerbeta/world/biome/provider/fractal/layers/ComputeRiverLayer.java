package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import net.minecraft.world.biome.BiomeKeys;

import static mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId.*;

public class ComputeRiverLayer extends SingleParentLayer {
    public static final MapCodec<ComputeRiverLayer> CODEC = RecordCodecBuilder.mapCodec(
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
    protected ExtendedBiomeId generate(int x, int z) {
        ExtendedBiomeId base = this.parentLayer.sample(x, z);
        ExtendedBiomeId[] neighbors = this.parentLayer.sampleNeighbors(x, z);

        return (!this.convertOceans
            ? neighborsRiverBorder(neighbors, base)
            : ((base.isOf(BiomeKeys.OCEAN) || neighborsContain(neighbors, OCEAN))
                || !allNeighborsEqual(neighbors, base)))
            ? RIVER : NULL;
    }

    private static boolean neighborsRiverBorder(ExtendedBiomeId[] neighbors, ExtendedBiomeId match) {
        byte matchType = getRiverType(match);
        for (ExtendedBiomeId neighbor : neighbors) {
            if (getRiverType(neighbor) != matchType) {
                return true;
            }
        }
        return false;
    }

    private static byte getRiverType(ExtendedBiomeId biome) {
        return RIVER_REGION_A.equals(biome) ? (byte)1
            : RIVER_REGION_B.equals(biome) ? (byte)2
            : biome.isOf(ExtendedBiomeId.RANDOM.baseId()) ? (byte)(1 + (biome.ext().charAt(biome.ext().length() - 1) & 1))
            : (byte)0;
    }
}
