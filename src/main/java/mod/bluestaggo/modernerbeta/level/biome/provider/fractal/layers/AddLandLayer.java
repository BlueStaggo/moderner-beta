package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeResolver;
import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomes;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Biome;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeIds.*;

public class AddLandLayer extends SingleParentLayer {
    public static final com.mojang.serialization.MapCodec<AddLandLayer> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            Codec.STRING.fieldOf("id").forGetter(layer -> layer.id),
            Codec.LONG.fieldOf("seed").orElse(0L).forGetter(layer -> layer.seed),
            Codec.STRING.fieldOf("parent").forGetter(layer -> layer.parent),
            Codec.BOOL.fieldOf("betaShape").orElse(false).forGetter(layer -> layer.betaShape),
            ExtendedIdentifier.CODEC.fieldOf("ocean").orElse(OCEAN).forGetter(layer -> layer.ocean),
            ExtendedIdentifier.CODEC.fieldOf("land").orElse(PLAINS).forGetter(layer -> layer.land),
            Codec.unboundedMap(ExtendedIdentifier.CODEC, ExtendedIdentifier.CODEC).fieldOf("biomeSpecificOceans").orElse(Map.of()).forGetter(layer -> layer.biomeSpecificOceans),
            Codec.INT.fieldOf("landChance").orElse(3).forGetter(layer -> layer.landChance),
            Codec.INT.fieldOf("oceanChance").orElse(5).forGetter(layer -> layer.oceanChance)
        ).apply(instance, AddLandLayer::new)
    );

    private final boolean betaShape;
    private final ExtendedIdentifier ocean;
    private final ExtendedIdentifier land;
    private final Map<ExtendedIdentifier, ExtendedIdentifier> biomeSpecificOceans;
    private final int landChance;
    private final int oceanChance;
    private transient ExtendedHolder<Biome> resolvedOcean;
    private transient ExtendedHolder<Biome> resolvedLand;
    private transient Map<ExtendedIdentifier, ExtendedHolder<Biome>> resolvedBiomeSpecificOceans;
    private transient Set<ExtendedHolder<Biome>> resolvedPossibleBiomes;

    public AddLandLayer(String id, long seed, String parent, boolean betaShape, ExtendedIdentifier ocean, ExtendedIdentifier land, Map<ExtendedIdentifier, ExtendedIdentifier> biomeSpecificOceans) {
        this(id, seed, parent, betaShape, ocean, land, biomeSpecificOceans, 3, 5);
    }

    public AddLandLayer(String id, long seed, String parent, boolean betaShape, ExtendedIdentifier ocean, ExtendedIdentifier land, Map<ExtendedIdentifier, ExtendedIdentifier> biomeSpecificOceans, int landChance, int oceanChance) {
        super(id, seed, parent);
        this.betaShape = betaShape;
        this.ocean = ocean;
        this.land = land;
        this.biomeSpecificOceans = biomeSpecificOceans;
        this.landChance = landChance;
        this.oceanChance = oceanChance;
    }

    public static AddLandLayer forIslandScaleBeta(String id, long seed, String parent) {
        return new AddLandLayer(id, seed, parent, true, OCEAN, PLAINS, Map.of());
    }

    public static AddLandLayer forIslandScale(String id, long seed, String parent) {
        return new AddLandLayer(id, seed, parent, false, OCEAN, PLAINS, Map.of(
            SNOWY_PLAINS, FROZEN_OCEAN
        ));
    }

    public static AddLandLayer forIslandScaleMajor(String id, long seed, String parent) {
        return new AddLandLayer(id, seed, parent, false, OCEAN, PLAINS, Map.of(
            CLIMATE_SNOWY, CLIMATE_SNOWY
        ));
    }

    public static AddLandLayer forBeta(String id, long seed, String parent) {
        return new AddLandLayer(id, seed, parent, true, OCEAN, ExtendedIdentifier.of(ModernBetaBiomes.LATE_BETA_PLAINS), Map.of());
    }

    public static AddLandLayer forEarlyRelease(String id, long seed, String parent, ExtendedIdentifier icePlains) {
        return new AddLandLayer(id, seed, parent, false, OCEAN, ExtendedIdentifier.of(ModernBetaBiomes.LATE_BETA_PLAINS), Map.of(icePlains, FROZEN_OCEAN));
    }

    public static AddLandLayer forMajorRelease(String id, long seed, String parent) {
        return new AddLandLayer(id, seed, parent, false, OCEAN, PLAINS, Map.of(ExtendedIdentifier.of(Biomes.FOREST), ExtendedIdentifier.of(Biomes.FOREST)));
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.ADD_LAND;
    }

    @Override
    protected ExtendedHolder<Biome> generate(int x, int z) {
        ExtendedHolder<Biome> base = this.parentLayer.sample(x, z);
        ExtendedHolder<Biome>[] neighbors = this.parentLayer.sampleDiagonalNeighbors(x, z);

        if (base.is(this.ocean) && !allNeighborsEqual(neighbors, this.ocean)) {
            int landSampleChance = 1;
            ExtendedHolder<Biome> sampledLand = this.resolvedLand;
            LayerRandom random = this.getRandom(x, z);

            boolean addLand;
            if (this.betaShape) {
                addLand = random.nextInt(this.landChance) == this.landChance - 1;
            } else {
                for (ExtendedHolder<Biome> neighbor : neighbors) {
                    if (!neighbor.is(this.ocean) && random.nextInt(landSampleChance++) == 0) {
                        sampledLand = neighbor;
                    }
                }
                addLand = random.nextInt(this.landChance) == 0;
            }

            ExtendedHolder<Biome> sampledOcean = getMatching(this.resolvedBiomeSpecificOceans, sampledLand);
            return addLand ? sampledLand : sampledOcean == null ? this.resolvedOcean : sampledOcean;
        } else if (this.betaShape
            ? base.is(this.land) && !allNeighborsEqual(neighbors, this.land)
            : !base.is(this.ocean) && neighborsContain(neighbors, this.ocean)
        ) {
            LayerRandom random = this.getRandom(x, z);
            if (random.nextInt(this.oceanChance) == (this.betaShape ? this.oceanChance - 1 : 0)) {
                ExtendedHolder<Biome> sampledOcean = getMatching(this.resolvedBiomeSpecificOceans, base);
                return sampledOcean == null ? this.resolvedOcean : sampledOcean;
            }
        }
        return base;
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedHolder<Biome>> biomes) {
        biomes.addAll(this.resolvedPossibleBiomes);
    }

    @Override
    protected void bindOwnBiomes(ExtendedBiomeResolver biomeResolver) {
        this.resolvedOcean = biomeResolver.resolve(this.ocean);
        this.resolvedLand = biomeResolver.resolve(this.land);
        this.resolvedBiomeSpecificOceans = new LinkedHashMap<>();
        this.resolvedPossibleBiomes = new HashSet<>();
        this.resolvedPossibleBiomes.add(this.resolvedOcean);
        this.resolvedPossibleBiomes.add(this.resolvedLand);
        for (Map.Entry<ExtendedIdentifier, ExtendedIdentifier> entry : this.biomeSpecificOceans.entrySet()) {
            ExtendedHolder<Biome> key = biomeResolver.resolve(entry.getKey());
            ExtendedHolder<Biome> value = biomeResolver.resolve(entry.getValue());
            this.resolvedBiomeSpecificOceans.put(entry.getKey(), value);
            this.resolvedPossibleBiomes.add(key);
            this.resolvedPossibleBiomes.add(value);
        }
    }
}
