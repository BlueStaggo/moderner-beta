package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeIds;
import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.world.level.biome.Biome;

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
        if (BASE_OCEANS.stream().noneMatch(base::is)) { //TODO: is stream a good idea?
            return base;
        }

        ExtendedHolder<Biome> ocean = this.oceanClimateLayer.sample(x, z);

        if (this.applyCoasts) {
            boolean isWarm = ocean.is(ExtendedBiomeIds.WARM_OCEAN);
            if (isWarm || ocean.is(ExtendedBiomeIds.FROZEN_OCEAN)) {
                for (int ox = -8; ox <= 8; ox += 4) {
                    for (int oz = -8; oz <= 8; oz += 4) {
                        ExtendedHolder<Biome> nearBiome = this.parentLayer.sample(x + ox, z + oz);
                        if (BASE_OCEANS.stream().noneMatch(nearBiome::is)) {
                            return isWarm ? ExtendedBiomeIds.LUKEWARM_OCEAN : ExtendedBiomeIds.COLD_OCEAN;
                        }
                    }
                }
            }
        }

        if (base.is(ExtendedBiomeIds.DEEP_OCEAN)) {
            ExtendedIdentifier deep = DEEP_MAP.get(ocean.unwrapExtendedKey().orElseThrow());

            if (deep != null) {
                ocean = deep;
            }
        }
        return ocean;
    }
}
