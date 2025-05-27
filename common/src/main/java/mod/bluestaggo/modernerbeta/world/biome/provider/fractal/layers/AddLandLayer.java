package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomes;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;

import java.util.Map;
import java.util.Set;

import static mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId.*;

public class AddLandLayer extends SingleParentLayer {
    public static final MapCodec<AddLandLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(instance.group(
                Codec.BOOL.fieldOf("betaShape").orElse(false).forGetter(layer -> layer.betaShape),
                ExtendedBiomeId.CODEC.fieldOf("ocean").orElse(OCEAN).forGetter(layer -> layer.ocean),
                ExtendedBiomeId.CODEC.fieldOf("land").orElse(PLAINS).forGetter(layer -> layer.land),
                Codec.unboundedMap(ExtendedBiomeId.CODEC, ExtendedBiomeId.CODEC).fieldOf("biomeSpecificOceans").forGetter(layer -> layer.biomeSpecificOceans)
            ))
            .apply(instance, AddLandLayer::new)
    );

    private final boolean betaShape;
    private final ExtendedBiomeId ocean;
    private final ExtendedBiomeId land;
    private final Map<ExtendedBiomeId, ExtendedBiomeId> biomeSpecificOceans;

    public AddLandLayer(String id, long seed, String parent, boolean betaShape, ExtendedBiomeId ocean, ExtendedBiomeId land, Map<ExtendedBiomeId, ExtendedBiomeId> biomeSpecificOceans) {
        super(id, seed, parent);
        this.betaShape = betaShape;
        this.ocean = ocean;
        this.land = land;
        this.biomeSpecificOceans = biomeSpecificOceans;
    }

    public static AddLandLayer forIslandScaleBeta(String id, long seed, String parent) {
        return new AddLandLayer(id, seed, parent, true, OCEAN, PLAINS, Map.of());
    }

    public static AddLandLayer forIslandScale(String id, long seed, String parent) {
        return new AddLandLayer(id, seed, parent, false, OCEAN, PLAINS, Map.of(
            SNOWY_PLAINS, FROZEN_OCEAN
        ));
    }

    public static AddLandLayer forBeta(String id, long seed, String parent) {
        return new AddLandLayer(id, seed, parent, true, OCEAN, ExtendedBiomeId.of(ModernBetaBiomes.LATE_BETA_PLAINS), Map.of());
    }

    public static AddLandLayer forEarlyRelease(String id, long seed, String parent, ExtendedBiomeId icePlains) {
        return new AddLandLayer(id, seed, parent, false, OCEAN, ExtendedBiomeId.of(ModernBetaBiomes.LATE_BETA_PLAINS), Map.of(icePlains, FROZEN_OCEAN));
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.ADD_LAND;
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        ExtendedBiomeId base = this.parentLayer.sample(x, z);
        ExtendedBiomeId[] neighbors = this.parentLayer.sampleDiagonalNeighbors(x, z);

        if (base.equals(this.ocean) && !allNeighborsEqual(neighbors, this.ocean)) {
            int landSampleChance = 1;
            ExtendedBiomeId sampledLand = this.land;
            LayerRandom random = this.getRandom(x, z);

            boolean addLand = false;
            if (this.betaShape) {
                addLand = random.nextInt(3) == 2;
            }

            for (ExtendedBiomeId neighbor : neighbors) {
                if (!neighbor.equals(this.ocean) && random.nextInt(landSampleChance++) == 0) {
                    sampledLand = neighbor;
                }
            }

            if (!this.betaShape) {
                addLand = random.nextInt(3) == 0;
            }
            return addLand ? sampledLand : this.biomeSpecificOceans.getOrDefault(sampledLand, this.ocean);
        } else if (!base.equals(this.ocean) && neighborsContain(neighbors, this.ocean)) {
            LayerRandom random = this.getRandom(x, z);
            if (random.nextInt(5) == (this.betaShape ? 4 : 0)) {
                return this.biomeSpecificOceans.getOrDefault(base, this.ocean);
            }
        }
        return base;
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedBiomeId> biomes) {
        biomes.add(this.ocean);
        biomes.add(this.land);
        for (Map.Entry<ExtendedBiomeId, ExtendedBiomeId> entry : this.biomeSpecificOceans.entrySet()) {
            biomes.add(entry.getKey());
            biomes.add(entry.getValue());
        }
    }
}
