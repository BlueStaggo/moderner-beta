package mod.bluestaggo.modernerbeta.level.biome.injection.predicates;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionContext;
import mod.bluestaggo.modernerbeta.level.biome.injection.InjectionNeeds;
import mod.bluestaggo.modernerbeta.util.VersionCompat;

import java.util.EnumSet;

public record BelowSurfaceInjectionPredicate(int threshold) implements InjectionPredicate {
    public static final com.mojang.serialization.MapCodec<BelowSurfaceInjectionPredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            Codec.INT.fieldOf("threshold").forGetter(BelowSurfaceInjectionPredicate::threshold)
        ).apply(instance, BelowSurfaceInjectionPredicate::new)
    );
    
    @Override
    public InjectionPredicateType<?> getType() {
        return InjectionPredicateType.BELOW_SURFACE;
    }

    @Override
    public boolean shouldApply(BiomeInjectionContext context) {
        return context.getY() >= context.worldMinY && context.getY() + threshold < context.minHeight;
    }

    @Override
    public EnumSet<InjectionNeeds> needs() {
        return EnumSet.of(InjectionNeeds.HEIGHTS);
    }
}
