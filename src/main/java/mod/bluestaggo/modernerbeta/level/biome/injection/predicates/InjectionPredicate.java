package mod.bluestaggo.modernerbeta.level.biome.injection.predicates;

import com.mojang.serialization.MapCodec;
import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionContext;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;

import java.util.List;

public interface InjectionPredicate {
     MapCodec<InjectionPredicate> BASE_CODEC = ModernBetaRegistries.INJECTION_PREDICATE.byNameCodec()
        .dispatchMap("condition", InjectionPredicate::getType, InjectionPredicateType::codec);

    default InjectionPredicate and(InjectionPredicate other) {
        return new AllOfInjectionPredicate(List.of(this, other));
    }

    default InjectionPredicate or(InjectionPredicate other) {
        return new AnyOfInjectionPredicate(List.of(this, other));
    }

    default InjectionPredicate invert() {
        return new InvertedInjectionPredicate(this);
    }

    InjectionPredicateType<?> getType();

    boolean shouldApply(BiomeInjectionContext context);
}
