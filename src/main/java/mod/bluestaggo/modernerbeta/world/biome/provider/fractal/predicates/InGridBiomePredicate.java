package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerRandom;

import java.util.function.Supplier;

public record InGridBiomePredicate(int size, int spacing, int offset) implements BiomePredicate {
    public static final com.mojang.serialization.MapCodec<InGridBiomePredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            Codec.INT.fieldOf("size").orElse(0).forGetter(InGridBiomePredicate::size),
            Codec.INT.fieldOf("spacing").orElse(0).forGetter(InGridBiomePredicate::spacing),
            Codec.INT.fieldOf("offset").forGetter(InGridBiomePredicate::offset)
        ).apply(instance, InGridBiomePredicate::new)
    );

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.IN_GRID;
    }

    @Override
    public boolean matches(ExtendedBiomeId biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        return Math.floorMod(x - this.offset, this.size + this.spacing) <= this.size
            && Math.floorMod(z - this.offset, this.size + this.spacing) <= this.size;
    }
}
