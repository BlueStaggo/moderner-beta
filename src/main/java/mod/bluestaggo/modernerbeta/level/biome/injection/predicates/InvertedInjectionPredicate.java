package mod.bluestaggo.modernerbeta.level.biome.injection.predicates;

import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionContext;
import mod.bluestaggo.modernerbeta.level.biome.injection.InjectionNeeds;
import mod.bluestaggo.modernerbeta.util.VersionCompat;

import java.util.EnumSet;

public record InvertedInjectionPredicate(InjectionPredicate term) implements InjectionPredicate {
    public static final com.mojang.serialization.MapCodec<InvertedInjectionPredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            InjectionPredicate.BASE_CODEC.fieldOf("term").forGetter(predicate -> predicate.term)
        ).apply(instance, InvertedInjectionPredicate::new)
    );

    @Override
    public InjectionPredicateType<?> getType() {
        return InjectionPredicateType.INVERTED;
    }

    @Override
    public boolean shouldApply(BiomeInjectionContext context) {
        return !this.term.shouldApply(context);
    }

    @Override
    public EnumSet<InjectionNeeds> needs() {
        return this.term.needs();
    }
}
