package mod.bluestaggo.modernerbeta.level.biome.injection.injector;

import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionContext;
import mod.bluestaggo.modernerbeta.level.biome.injection.InjectionNeeds;
import mod.bluestaggo.modernerbeta.level.biome.injection.predicates.InjectionPredicate;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.EnumSet;
import java.util.Optional;

public record PredicateBiomeInjector(
    InjectionPredicate predicate,
    BiomeInjector onMatch,
    Optional<BiomeInjector> otherwise
) implements BiomeInjector {
    public static final com.mojang.serialization.MapCodec<PredicateBiomeInjector> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            InjectionPredicate.BASE_CODEC.fieldOf("predicate").forGetter(PredicateBiomeInjector::predicate),
            BiomeInjector.TYPE_CODEC.fieldOf("onMatch").forGetter(PredicateBiomeInjector::onMatch),
            BiomeInjector.TYPE_CODEC.codec().optionalFieldOf("otherwise").forGetter(PredicateBiomeInjector::otherwise)
        ).apply(instance, PredicateBiomeInjector::new)
    );

    public PredicateBiomeInjector(InjectionPredicate predicate, BiomeInjector onMatch) {
        this(predicate, onMatch, Optional.empty());
    }

    public PredicateBiomeInjector(InjectionPredicate predicate, BiomeInjector onMatch, BiomeInjector otherwise) {
        this(predicate, onMatch, Optional.of(otherwise));
    }

    @Override
    public BiomeInjectorType<?> getType() {
        return BiomeInjectorType.PREDICATE;
    }

    @Override
    public Holder<Biome> apply(BiomeInjectionContext context, int biomeX, int biomeY, int biomeZ) {
        if (predicate.shouldApply(context)) {
            return onMatch.apply(context, biomeX, biomeY, biomeZ);
        } else if (otherwise.isPresent()) {
            return otherwise.get().apply(context, biomeX, biomeY, biomeZ);
        }

        return null;
    }

    @Override
    public EnumSet<InjectionNeeds> needs() {
        EnumSet<InjectionNeeds> needs = EnumSet.noneOf(InjectionNeeds.class);

        needs.addAll(predicate.needs());
        needs.addAll(onMatch.needs());

        otherwise.ifPresent(injector ->
                needs.addAll(injector.needs()));

        return needs;
    }
}
