package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeIds;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomes;
import net.minecraft.world.level.biome.Biomes;

import java.util.*;
import java.util.function.Function;

public class MixRiverLayer extends SingleParentLayer {
    public static final com.mojang.serialization.MapCodec<MixRiverLayer> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(instance.group(
                Codec.STRING.fieldOf("riverSource").forGetter(layer -> layer.riverSource),
                CodecUtil.set(ExtendedIdentifier.CODEC).fieldOf("ignoredBiomes").forGetter(layer -> layer.ignoredBiomes),
                Codec.unboundedMap(ExtendedIdentifier.CODEC, ExtendedIdentifier.CODEC).fieldOf("biomeSpecificRivers").forGetter(layer -> layer.biomeSpecificRivers)
            ))
            .apply(instance, MixRiverLayer::new)
    );

    private final String riverSource;
    private final Set<ExtendedIdentifier> ignoredBiomes;
    private final Map<ExtendedIdentifier, ExtendedIdentifier> biomeSpecificRivers;

    private transient Layer riverSourceLayer;

    public static MixRiverLayer forEarlyRelease(String id, long seed, String parent, String riverSource) {
        return new MixRiverLayer(id, seed, parent, riverSource, Set.of(ExtendedBiomeIds.OCEAN), Map.of(
            ExtendedIdentifier.of(ModernBetaBiomes.LATE_BETA_ICE_PLAINS), ExtendedBiomeIds.FROZEN_RIVER,
            ExtendedIdentifier.of(ModernBetaBiomes.EARLY_RELEASE_ICE_PLAINS), ExtendedBiomeIds.FROZEN_RIVER,
            ExtendedBiomeIds.MUSHROOM_ISLAND, ExtendedBiomeIds.MUSHROOM_SHORE,
            ExtendedBiomeIds.MUSHROOM_SHORE, ExtendedBiomeIds.MUSHROOM_SHORE
        ));
    }

    public static MixRiverLayer forMajorRelease(String id, long seed, String parent, String riverSource) {
        return new MixRiverLayer(id, seed, parent, riverSource, Set.of(ExtendedBiomeIds.OCEAN, ExtendedBiomeIds.DEEP_OCEAN), Map.of(
            ExtendedIdentifier.of(Biomes.SNOWY_PLAINS), ExtendedBiomeIds.FROZEN_RIVER,
            ExtendedBiomeIds.MUSHROOM_ISLAND, ExtendedBiomeIds.MUSHROOM_SHORE,
            ExtendedBiomeIds.MUSHROOM_SHORE, ExtendedBiomeIds.MUSHROOM_SHORE
        ));
    }

    public MixRiverLayer(String id, long seed, String parent, String riverSource, Set<ExtendedIdentifier> ignoredBiomes, Map<ExtendedIdentifier, ExtendedIdentifier> biomeSpecificRivers) {
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
    public LayerType<?> getType() {
        return LayerType.MIX_RIVER;
    }

    @Override
    protected List<Layer> getParents() {
        return List.of(this.parentLayer, this.riverSourceLayer);
    }

    @Override
    protected ExtendedIdentifier generate(int x, int z) {
        ExtendedIdentifier base = this.parentLayer.sample(x, z);
        if (this.ignoredBiomes.contains(base)) {
            return base;
        }

        ExtendedIdentifier river = this.riverSourceLayer.sample(x, z);
        if (!river.equals(ExtendedBiomeIds.RIVER)) {
            return base;
        }

        return this.biomeSpecificRivers.getOrDefault(base, ExtendedBiomeIds.RIVER);
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedIdentifier> biomes) {
        biomes.add(ExtendedBiomeIds.RIVER);
        for (Map.Entry<ExtendedIdentifier, ExtendedIdentifier> entry : this.biomeSpecificRivers.entrySet()) {
            if (biomes.contains(entry.getKey())) {
                biomes.add(entry.getValue());
            }
        }
    }
}
