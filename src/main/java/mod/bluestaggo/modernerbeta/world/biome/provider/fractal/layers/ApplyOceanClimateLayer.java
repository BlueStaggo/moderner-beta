package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class ApplyOceanClimateLayer extends SingleParentLayer {
    public static final com.mojang.serialization.MapCodec<ApplyOceanClimateLayer> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(instance.group(
                Codec.STRING.fieldOf("oceanClimate").forGetter(layer -> layer.oceanClimate),
                Codec.BOOL.fieldOf("applyCoasts").orElse(true).forGetter(layer -> layer.applyCoasts)
            ))
            .apply(instance, ApplyOceanClimateLayer::new)
    );
    private static final Set<ExtendedBiomeId> BASE_OCEANS = Set.of(ExtendedBiomeId.OCEAN, ExtendedBiomeId.DEEP_OCEAN);
    private static final Map<ExtendedBiomeId, ExtendedBiomeId> DEEP_MAP = Map.of(
        ExtendedBiomeId.WARM_OCEAN, ExtendedBiomeId.WARM_OCEAN.withExt("deep"),
        ExtendedBiomeId.LUKEWARM_OCEAN, ExtendedBiomeId.DEEP_LUKEWARM_OCEAN,
        ExtendedBiomeId.OCEAN, ExtendedBiomeId.DEEP_OCEAN,
        ExtendedBiomeId.COLD_OCEAN, ExtendedBiomeId.DEEP_COLD_OCEAN,
        ExtendedBiomeId.FROZEN_OCEAN, ExtendedBiomeId.DEEP_FROZEN_OCEAN
    );

    private final String oceanClimate;
    private final boolean applyCoasts;
    private transient Layer oceanClimateLayer;

    public ApplyOceanClimateLayer(String id, long seed, String parent, String oceanClimate, boolean applyCoasts) {
        super(id, seed, parent);
        this.oceanClimate = oceanClimate;
        this.applyCoasts = applyCoasts;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.APPLY_OCEAN_CLIMATE;
    }

    @Override
    public void configure(Function<String, Layer> layerMap) {
        super.configure(layerMap);
        this.oceanClimateLayer = layerMap.apply(this.oceanClimate);
    }

    @Override
    protected List<Layer> getParents() {
        return List.of(this.parentLayer, this.oceanClimateLayer);
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        ExtendedBiomeId base = this.parentLayer.sample(x, z);
        if (!BASE_OCEANS.contains(base)) {
            return base;
        }

        ExtendedBiomeId ocean = this.oceanClimateLayer.sample(x, z);

        if (this.applyCoasts) {
            boolean isWarm = ExtendedBiomeId.WARM_OCEAN.equals(ocean);
            if (isWarm || ExtendedBiomeId.FROZEN_OCEAN.equals(ocean)) {
                for (int ox = -8; ox <= 8; ox += 4) {
                    for (int oz = -8; oz <= 8; oz += 4) {
                        ExtendedBiomeId nearBiome = this.parentLayer.sample(x + ox, z + oz);
                        if (!BASE_OCEANS.contains(nearBiome)) {
                            return isWarm ? ExtendedBiomeId.LUKEWARM_OCEAN : ExtendedBiomeId.COLD_OCEAN;
                        }
                    }
                }
            }
        }

        if (ExtendedBiomeId.DEEP_OCEAN.equals(base)) {
            ocean = DEEP_MAP.getOrDefault(ocean, ocean);
        }
        return ocean;
    }
}
