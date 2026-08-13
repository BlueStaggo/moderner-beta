package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.predicates;

import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.LayerRandom;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class CategorizedNeighborBiomePredicate extends NeighborComparisonPredicate {
    public static final com.mojang.serialization.MapCodec<CategorizedNeighborBiomePredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillNeighborComparisonFields(instance)
            .and(CodecUtil.set(ExtendedIdentifier.CODEC).listOf().fieldOf("categories").forGetter(predicate -> predicate.categories))
            .apply(instance, CategorizedNeighborBiomePredicate::new)
    );

    private final List<Set<ExtendedIdentifier>> categories;

    protected CategorizedNeighborBiomePredicate(int requiredCount, boolean diagonal, List<Set<ExtendedIdentifier>> categories) {
        super(requiredCount, diagonal);
        this.categories = categories;
    }

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.CATEGORIZED_NEIGHBOR;
    }

    @Override
    protected boolean neighborMatches(ExtendedHolder<Biome> centre, ExtendedHolder<Biome> neighbor, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z, int nx, int nz) {
        return this.categories.stream()
            .filter(category -> category.stream().anyMatch(centre::is))
            .flatMap(Set::stream)
            .anyMatch(neighbor::is);
    }
}
