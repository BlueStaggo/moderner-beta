package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeIds;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeResolver;
import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.world.level.biome.Biome;

import java.util.LinkedHashMap;
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
    private static final Set<ExtendedIdentifier> BASE_OCEANS = Set.of(ExtendedBiomeIds.OCEAN, ExtendedBiomeIds.DEEP_OCEAN);
    private static final Map<ExtendedIdentifier, ExtendedIdentifier> DEEP_MAP = Map.of(
        ExtendedBiomeIds.WARM_OCEAN, ExtendedBiomeIds.WARM_OCEAN.withExt("deep"),
        ExtendedBiomeIds.LUKEWARM_OCEAN, ExtendedBiomeIds.DEEP_LUKEWARM_OCEAN,
        ExtendedBiomeIds.OCEAN, ExtendedBiomeIds.DEEP_OCEAN,
        ExtendedBiomeIds.COLD_OCEAN, ExtendedBiomeIds.DEEP_COLD_OCEAN,
        ExtendedBiomeIds.FROZEN_OCEAN, ExtendedBiomeIds.DEEP_FROZEN_OCEAN
    );

    private final String oceanClimate;
    private final boolean applyCoasts;
    private transient Layer oceanClimateLayer;
    private transient ExtendedHolder<Biome> lukewarmOcean;
    private transient ExtendedHolder<Biome> coldOcean;
    private transient Map<ExtendedIdentifier, ExtendedHolder<Biome>> deepOceans;

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
    protected ExtendedHolder<Biome> generate(int x, int z) {
        ExtendedHolder<Biome> base = this.parentLayer.sample(x, z);
        if (!matches(BASE_OCEANS, base)) {
            return base;
        }

        ExtendedHolder<Biome> ocean = this.oceanClimateLayer.sample(x, z);

        if (this.applyCoasts) {
            boolean isWarm = ocean.is(ExtendedBiomeIds.WARM_OCEAN);
            if (isWarm || ocean.is(ExtendedBiomeIds.FROZEN_OCEAN)) {
                for (int ox = -8; ox <= 8; ox += 4) {
                    for (int oz = -8; oz <= 8; oz += 4) {
                        ExtendedHolder<Biome> nearBiome = this.parentLayer.sample(x + ox, z + oz);
                        if (!matches(BASE_OCEANS, nearBiome)) {
                            return isWarm ? this.lukewarmOcean : this.coldOcean;
                        }
                    }
                }
            }
        }

        if (base.is(ExtendedBiomeIds.DEEP_OCEAN)) {
            ExtendedHolder<Biome> deepOcean = getMatching(this.deepOceans, ocean);
            if (deepOcean != null) {
                ocean = deepOcean;
            }
        }
        return ocean;
    }

    @Override
    protected void bindOwnBiomes(ExtendedBiomeResolver biomeResolver) {
        this.lukewarmOcean = biomeResolver.resolve(ExtendedBiomeIds.LUKEWARM_OCEAN);
        this.coldOcean = biomeResolver.resolve(ExtendedBiomeIds.COLD_OCEAN);
        this.deepOceans = new LinkedHashMap<>();
        for (Map.Entry<ExtendedIdentifier, ExtendedIdentifier> entry : DEEP_MAP.entrySet()) {
            this.deepOceans.put(entry.getKey(), biomeResolver.resolve(entry.getValue()));
        }
    }
}
