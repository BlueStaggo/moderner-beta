package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeIds;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.LayerTarget;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.predicates.BiomePredicate;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class ConditionalOverlayLayer extends SingleParentLayer {
    public static final com.mojang.serialization.MapCodec<ConditionalOverlayLayer> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(instance.group(
                BiomePredicate.BASE_CODEC.fieldOf("predicate").forGetter(layer -> layer.predicate),
                LayerTarget.CODEC.fieldOf("onMatch").forGetter(layer -> layer.onMatch),
                LayerTarget.CODEC.fieldOf("otherwise").forGetter(layer -> layer.otherwise)
            ))
            .apply(instance, ConditionalOverlayLayer::new)
    );

    private final BiomePredicate predicate;
    private final LayerTarget onMatch;
    private final LayerTarget otherwise;
    private transient LayerTarget.Configured onMatchConfigured;
    private transient LayerTarget.Configured otherwiseConfigured;

    public ConditionalOverlayLayer(String id, long seed, String parent, BiomePredicate predicate, LayerTarget onMatch, LayerTarget otherwise) {
        super(id, seed, parent);
        this.predicate = predicate;
        this.onMatch = onMatch;
        this.otherwise = otherwise;
    }

    public static ConditionalOverlayLayer mushroomIslands() {
        return new ConditionalOverlayLayer(
            "land", 5, "land",
            BiomePredicate.of(ExtendedBiomeIds.OCEAN)
                .and(BiomePredicate.diagonalInterior())
                .and(BiomePredicate.oneIn(100)),
            LayerTarget.biome(ExtendedBiomeIds.MUSHROOM_ISLAND), LayerTarget.none()
        );
    }

    @Override
    public void configure(Function<String, Layer> layerMap) {
        super.configure(layerMap);

        this.onMatchConfigured = this.onMatch.configure(layerMap);
        this.otherwiseConfigured = this.otherwise.configure(layerMap);

        if (this.onMatchConfigured.isEquivalentToOrNull(this.parentLayer)) {
            this.onMatchConfigured = null;
        }
        if (this.otherwiseConfigured.isEquivalentToOrNull(this.parentLayer)) {
            this.otherwiseConfigured = null;
        }
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.CONDITIONAL_OVERLAY;
    }

    @Override
    protected List<Layer> getParents() {
        ImmutableList.Builder<Layer> parents = ImmutableList.builder();
        parents.add(this.parentLayer);
        if (this.onMatchConfigured != null) {
            this.onMatchConfigured.asLayer().ifPresent(parents::add);
        }
        if (this.otherwiseConfigured != null) {
            this.otherwiseConfigured.asLayer().ifPresent(parents::add);
        }
        return parents.build();
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedIdentifier> biomes) {
        if (this.onMatchConfigured != null && !(this.onMatchConfigured instanceof LayerTarget.Configured.OfLayer)) {
            this.onMatchConfigured.addPossibleBiomes(biomes);
        }
        if (this.otherwiseConfigured != null && !(this.otherwiseConfigured instanceof LayerTarget.Configured.OfLayer)) {
            this.otherwiseConfigured.addPossibleBiomes(biomes);
        }
    }

    @Override
    protected ExtendedIdentifier generate(int x, int z) {
        ExtendedIdentifier biome = this.parentLayer.sample(x, z);
        LayerTarget.Configured target = this.predicate.matches(biome, this.parentLayer, Suppliers.memoize(() -> this.getRandom(x, z)), x, z)
            ? this.onMatchConfigured : this.otherwiseConfigured;
        if (target == null) {
            return biome;
        }

        ExtendedIdentifier output = target.sample(x, z);
        if (ExtendedBiomeIds.NULL.equals(output)) {
            return biome;
        }

        return output;
    }
}
