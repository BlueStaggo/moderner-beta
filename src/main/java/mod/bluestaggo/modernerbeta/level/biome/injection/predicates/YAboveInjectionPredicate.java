package mod.bluestaggo.modernerbeta.level.biome.injection.predicates;

import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionContext;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.world.level.levelgen.VerticalAnchor;

public record YAboveInjectionPredicate(VerticalAnchor anchor) implements InjectionPredicate {
    public static final com.mojang.serialization.MapCodec<YAboveInjectionPredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            VerticalAnchor.CODEC.fieldOf("anchor").forGetter(YAboveInjectionPredicate::anchor)
        ).apply(instance, YAboveInjectionPredicate::new)
    );

    @Override
    public InjectionPredicateType<?> getType() {
        return InjectionPredicateType.Y_ABOVE;
    }

    @Override
    public boolean shouldApply(BiomeInjectionContext context) {
        return context.getY() >= this.anchor.resolveY(context.context);
    }
}
