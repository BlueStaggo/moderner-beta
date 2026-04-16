package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.predicates;

import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeIds;
import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.LayerRandom;
import net.minecraft.world.level.biome.Biome;

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
            .and(CodecUtil.set(ExtendedBiomeIds.CODEC).listOf().fieldOf("categories").forGetter(predicate -> predicate.categories))
            .apply(instance, CategorizedNeighborBiomePredicate::new)
    );

    private final List<Set<ExtendedHolder<Biome>>> categories;
    private transient final Map<ExtendedHolder<Biome>, Set<ExtendedHolder<Biome>>> mapToCategories;
    private transient Set<ExtendedHolder<Biome>> currentCategory;

    protected CategorizedNeighborBiomePredicate(int requiredCount, boolean diagonal, List<Set<ExtendedHolder<Biome>>> categories) {
        super(requiredCount, diagonal);
        this.categories = categories;
        this.mapToCategories = new HashMap<>();
        for (Set<ExtendedHolder<Biome>> category : categories) {
            for (ExtendedHolder<Biome> biome : category) {
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
    protected boolean neighborMatches(ExtendedHolder<Biome> centre, ExtendedHolder<Biome> neighbor, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z, int nx, int nz) {
        Set<ExtendedHolder<Biome>> category = this.mapToCategories.get(centre);
        return category != null && category.contains(neighbor);
    }
}
