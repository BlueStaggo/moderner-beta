package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomes;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.LayerType;

import java.util.Map;
import java.util.Set;

import static mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId.*;

public class AddBetaLandLayer extends SingleParentLayer {
    public static final MapCodec<AddBetaLandLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(instance.group(
                ExtendedBiomeId.CODEC.fieldOf("ocean").orElse(OCEAN).forGetter(layer -> layer.ocean),
                ExtendedBiomeId.CODEC.fieldOf("land").orElse(PLAINS).forGetter(layer -> layer.land),
                Codec.unboundedMap(ExtendedBiomeId.CODEC, ExtendedBiomeId.CODEC).fieldOf("biomeSpecificOceans").forGetter(layer -> layer.biomeSpecificOceans)
            ))
            .apply(instance, AddBetaLandLayer::new)
    );

    private final ExtendedBiomeId ocean;
    private final ExtendedBiomeId land;
    private final Map<ExtendedBiomeId, ExtendedBiomeId> biomeSpecificOceans;

    public AddBetaLandLayer(String id, long seed, String parent, ExtendedBiomeId ocean, ExtendedBiomeId land, Map<ExtendedBiomeId, ExtendedBiomeId> biomeSpecificOceans) {
        super(id, seed, parent);
        this.ocean = ocean;
        this.land = land;
        this.biomeSpecificOceans = biomeSpecificOceans;
    }

    public static AddBetaLandLayer forIslandScale(String id, long seed, String parent) {
        return new AddBetaLandLayer(id, seed, parent, OCEAN, PLAINS, Map.of(
            SNOWY_PLAINS, FROZEN_OCEAN
        ));
    }

    public static AddBetaLandLayer forLateBeta(String id, long seed, String parent) {
        return new AddBetaLandLayer(id, seed, parent, OCEAN, ExtendedBiomeId.of(ModernBetaBiomes.LATE_BETA_PLAINS), Map.of(
            ExtendedBiomeId.of(ModernBetaBiomes.LATE_BETA_ICE_PLAINS), FROZEN_OCEAN,
            ExtendedBiomeId.of(ModernBetaBiomes.EARLY_RELEASE_ICE_PLAINS), FROZEN_OCEAN
        ));
    }

    @Override
    protected LayerType<?> getType() {
        return LayerType.ADD_BETA_LAND;
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        ExtendedBiomeId base = this.parentLayer.sample(x, z);
        ExtendedBiomeId[] neighbors = this.parentLayer.sampleDiagonalNeighbors(x, z);

        boolean addLand;
        if (base.equals(this.ocean) && !allNeighborsEqual(neighbors, this.ocean)) {
            addLand = this.getRandom(x, z).nextInt(3) / 2 > 0;
        } else if (!base.equals(this.ocean) && neighborsContain(neighbors, this.ocean)) {
            addLand = 1 - this.getRandom(x, z).nextInt(5) / 4 > 0;
        } else {
            return base;
        }

        if (addLand) {
            return this.biomeSpecificOceans.containsKey(base) ? base : this.land;
        } else {
            return this.biomeSpecificOceans.getOrDefault(base, this.ocean);
        }
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedBiomeId> biomes) {
        biomes.add(this.ocean);
        biomes.add(this.land);
        for (Map.Entry<ExtendedBiomeId, ExtendedBiomeId> entry : this.biomeSpecificOceans.entrySet()) {
            if (biomes.contains(entry.getKey())) {
                biomes.add(entry.getValue());
            } else if (biomes.contains(entry.getValue())) {
                biomes.add(entry.getKey());
            }
        }
    }
}
