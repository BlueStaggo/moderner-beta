package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.predicates;

import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.LayerRandom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CategorizedNeighborBiomePredicate extends NeighborComparisonPredicate {
    public static final com.mojang.serialization.MapCodec<CategorizedNeighborBiomePredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillNeighborComparisonFields(instance)
            .and(CodecUtil.set(ExtendedIdentifier.CODEC).listOf().fieldOf("categories").forGetter(predicate -> predicate.categories))
            .apply(instance, CategorizedNeighborBiomePredicate::new)
    );

    private final List<Set<ExtendedIdentifier>> categories;
    private transient final Map<ExtendedIdentifier, Set<ExtendedIdentifier>> mapToCategories;
    private transient Set<ExtendedIdentifier> currentCategory;

    protected CategorizedNeighborBiomePredicate(int requiredCount, boolean diagonal, List<Set<ExtendedIdentifier>> categories) {
        super(requiredCount, diagonal);
        this.categories = categories;
        this.mapToCategories = new HashMap<>();
        for (Set<ExtendedIdentifier> category : categories) {
            for (ExtendedIdentifier biome : category) {
                this.mapToCategories.compute(biome, (k, v) -> v == null
                    ? category
                    : Stream.concat(v.stream(), category.stream()).collect(Collectors.toSet()));
            }
        }
    }

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.CATEGORIZED_NEIGHBOR;
    }

    @Override
    protected boolean neighborMatches(ExtendedIdentifier centre, ExtendedIdentifier neighbor, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z, int nx, int nz) {
        Set<ExtendedIdentifier> category = this.mapToCategories.get(centre);
        return category != null && category.contains(neighbor);
    }
}
