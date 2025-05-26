package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomes;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.LayerType;

import java.util.*;
import java.util.function.Function;

public class MixRiverLayer extends SingleParentLayer {
    public static final MapCodec<MixRiverLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(instance.group(
                Codec.STRING.fieldOf("riverSource").forGetter(layer -> layer.riverSource),
                CodecUtil.set(ExtendedBiomeId.CODEC).fieldOf("ignoredBiomes").forGetter(layer -> layer.ignoredBiomes),
                Codec.unboundedMap(ExtendedBiomeId.CODEC, ExtendedBiomeId.CODEC).fieldOf("biomeSpecificRivers").forGetter(layer -> layer.biomeSpecificRivers)
            ))
            .apply(instance, MixRiverLayer::new)
    );

    private final String riverSource;
    private final Set<ExtendedBiomeId> ignoredBiomes;
    private final Map<ExtendedBiomeId, ExtendedBiomeId> biomeSpecificRivers;

    private transient Layer riverSourceLayer;

    public static MixRiverLayer forEarlyRelease(String id, long seed, String parent, String riverSource) {
        return new MixRiverLayer(id, seed, parent, riverSource, Set.of(ExtendedBiomeId.OCEAN), Map.of(
            ExtendedBiomeId.of(ModernBetaBiomes.LATE_BETA_ICE_PLAINS), ExtendedBiomeId.FROZEN_RIVER,
            ExtendedBiomeId.of(ModernBetaBiomes.EARLY_RELEASE_ICE_PLAINS), ExtendedBiomeId.FROZEN_RIVER,
            ExtendedBiomeId.MUSHROOM_ISLAND, ExtendedBiomeId.MUSHROOM_SHORE,
            ExtendedBiomeId.MUSHROOM_SHORE, ExtendedBiomeId.MUSHROOM_SHORE
        ));
    }

    public MixRiverLayer(String id, long seed, String parent, String riverSource, Set<ExtendedBiomeId> ignoredBiomes, Map<ExtendedBiomeId, ExtendedBiomeId> biomeSpecificRivers) {
        super(id, seed, parent);
        this.riverSource = riverSource;
        this.ignoredBiomes = ignoredBiomes;
        this.biomeSpecificRivers = biomeSpecificRivers;
    }

    @Override
    public void configure(Function<String, Layer> layerMap) {
        super.configure(layerMap);
        this.riverSourceLayer = layerMap.apply(this.riverSource);
    }

    @Override
    protected LayerType<?> getType() {
        return LayerType.MIX_RIVER;
    }

    @Override
    protected List<Layer> getParents() {
        return List.of(this.parentLayer, this.riverSourceLayer);
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        ExtendedBiomeId base = this.parentLayer.sample(x, z);
        if (this.ignoredBiomes.contains(base)) {
            return base;
        }

        ExtendedBiomeId river = this.riverSourceLayer.sample(x, z);
        if (!river.equals(ExtendedBiomeId.RIVER)) {
            return base;
        }

        return this.biomeSpecificRivers.getOrDefault(base, ExtendedBiomeId.RIVER);
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedBiomeId> biomes) {
        biomes.add(ExtendedBiomeId.RIVER);
        for (Map.Entry<ExtendedBiomeId, ExtendedBiomeId> entry : this.biomeSpecificRivers.entrySet()) {
            if (biomes.contains(entry.getKey())) {
                biomes.add(entry.getValue());
            }
        }
    }
}
