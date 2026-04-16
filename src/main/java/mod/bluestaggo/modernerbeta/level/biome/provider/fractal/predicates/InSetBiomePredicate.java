package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.predicates;

import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeIds;
import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.LayerRandom;
import net.minecraft.world.level.biome.Biome;

import java.util.Set;
import java.util.function.Supplier;

public record InSetBiomePredicate(Set<ExtendedHolder<Biome>> biomes) implements BiomePredicate {
    public static final com.mojang.serialization.MapCodec<InSetBiomePredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            CodecUtil.set(ExtendedBiomeIds.CODEC).fieldOf("biomes").forGetter(predicate -> predicate.biomes)
        ).apply(instance, InSetBiomePredicate::new)
    );

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.IN_SET;
    }

    @Override
    public boolean matches(ExtendedHolder<Biome> biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        return this.biomes.contains(biome);
    }
}
