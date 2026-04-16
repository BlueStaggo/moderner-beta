package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.predicates;

import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.LayerRandom;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.UnknownNullability;

import java.util.function.Supplier;

public class UniqueNeighborBiomePredicate extends NeighborComparisonPredicate {
    public static final com.mojang.serialization.MapCodec<UniqueNeighborBiomePredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillNeighborComparisonFields(instance)
            .apply(instance, UniqueNeighborBiomePredicate::new)
    );

    public UniqueNeighborBiomePredicate(int requiredCount, boolean diagonal) {
        super(requiredCount, diagonal);
    }

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.UNIQUE_NEIGHBOR;
    }

    @Override
    protected boolean neighborMatches(ExtendedHolder<Biome> centre, @UnknownNullability ExtendedHolder<Biome> neighbor, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z, int nx, int nz) {
        return !neighbor.equals(centre);
    }
}
