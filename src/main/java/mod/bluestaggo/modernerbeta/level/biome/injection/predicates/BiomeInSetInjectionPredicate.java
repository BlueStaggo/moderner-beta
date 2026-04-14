package mod.bluestaggo.modernerbeta.level.biome.injection.predicates;

import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionContext;
import mod.bluestaggo.modernerbeta.level.biome.injection.InjectionNeeds;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.EnumSet;
import java.util.Set;

public record BiomeInSetInjectionPredicate(Set<Holder<Biome>> biomes) implements InjectionPredicate {
    public static final com.mojang.serialization.MapCodec<BiomeInSetInjectionPredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            CodecUtil.set(Biome.CODEC).fieldOf("biomes").forGetter(predicate -> predicate.biomes)
        ).apply(instance, BiomeInSetInjectionPredicate::new)
    );

    @SafeVarargs
    public BiomeInSetInjectionPredicate(Holder<Biome>... biomes) {
        this(Set.of(biomes));
    }

    @Override
    public InjectionPredicateType<?> getType() {
        return InjectionPredicateType.BIOME_IN_SET;
    }

    @Override
    public boolean shouldApply(BiomeInjectionContext context) {
        return biomes.contains(context.getBiome());
    }

    @Override
    public EnumSet<InjectionNeeds> needs() {
        return EnumSet.of(InjectionNeeds.BIOMES);
    }
}
