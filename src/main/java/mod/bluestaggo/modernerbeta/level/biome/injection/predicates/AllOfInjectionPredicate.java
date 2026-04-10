package mod.bluestaggo.modernerbeta.level.biome.injection.predicates;

import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionContext;
import mod.bluestaggo.modernerbeta.util.VersionCompat;

import java.util.ArrayList;
import java.util.List;

public record AllOfInjectionPredicate(List<InjectionPredicate> terms) implements InjectionPredicate {
    public static final com.mojang.serialization.MapCodec<AllOfInjectionPredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            InjectionPredicate.BASE_CODEC.codec().listOf().fieldOf("terms").forGetter(AllOfInjectionPredicate::terms)
        ).apply(instance, AllOfInjectionPredicate::new)
    );

    @Override
    public InjectionPredicate and(InjectionPredicate other) {
        List<InjectionPredicate> terms = new ArrayList<>(this.terms);
        terms.add(other);
        return new AllOfInjectionPredicate(terms);
    }

    @Override
    public InjectionPredicateType<?> getType() {
        return InjectionPredicateType.ALL_OF;
    }

    @Override
    public boolean shouldApply(BiomeInjectionContext context) {
        for (InjectionPredicate term : this.terms) {
            if (!term.shouldApply(context)) {
                return false;
            }
        }
        return true;
    }
}
