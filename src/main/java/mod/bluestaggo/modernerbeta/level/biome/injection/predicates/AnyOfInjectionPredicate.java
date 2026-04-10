package mod.bluestaggo.modernerbeta.level.biome.injection.predicates;

import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionContext;
import mod.bluestaggo.modernerbeta.util.VersionCompat;

import java.util.ArrayList;
import java.util.List;

public record AnyOfInjectionPredicate(List<InjectionPredicate> terms) implements InjectionPredicate {
    public static final com.mojang.serialization.MapCodec<AnyOfInjectionPredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            InjectionPredicate.BASE_CODEC.codec().listOf().fieldOf("terms").forGetter(AnyOfInjectionPredicate::terms)
        ).apply(instance, AnyOfInjectionPredicate::new)
    );

    @Override
    public InjectionPredicate or(InjectionPredicate other) {
        List<InjectionPredicate> terms = new ArrayList<>(this.terms);
        terms.add(other);
        return new AnyOfInjectionPredicate(terms);
    }

    @Override
    public InjectionPredicateType<?> getType() {
        return InjectionPredicateType.ANY_OF;
    }

    @Override
    public boolean shouldApply(BiomeInjectionContext context) {
        for (InjectionPredicate term : this.terms) {
            if (term.shouldApply(context)) {
                return true;
            }
        }
        return false;
    }
}
