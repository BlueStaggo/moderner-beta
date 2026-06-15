package mod.bluestaggo.modernerbeta.level.biome.injection.predicates;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionContext;
import mod.bluestaggo.modernerbeta.util.VersionCompat;

public record OutOfBoundsInjectionPredicate(int margin) implements InjectionPredicate {
    public static final com.mojang.serialization.MapCodec<OutOfBoundsInjectionPredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            Codec.INT.fieldOf("margin").forGetter(OutOfBoundsInjectionPredicate::margin)
        ).apply(instance, OutOfBoundsInjectionPredicate::new)
    );

    @Override
    public InjectionPredicateType<?> getType() {
        return InjectionPredicateType.OUT_OF_BOUNDS;
    }

    @Override
    public boolean shouldApply(BiomeInjectionContext context) {
        return context.borderLocation.enabled() &&
                !context.borderLocation.containsPoint(context.getX(), context.getZ(), this.margin);
    }
}
