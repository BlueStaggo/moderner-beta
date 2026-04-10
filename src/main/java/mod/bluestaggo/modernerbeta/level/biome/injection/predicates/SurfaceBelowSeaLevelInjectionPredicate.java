package mod.bluestaggo.modernerbeta.level.biome.injection.predicates;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionContext;
import mod.bluestaggo.modernerbeta.util.VersionCompat;

public record SurfaceBelowSeaLevelInjectionPredicate(int threshold) implements InjectionPredicate {
    public static final com.mojang.serialization.MapCodec<SurfaceBelowSeaLevelInjectionPredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            Codec.INT.fieldOf("threshold").forGetter(SurfaceBelowSeaLevelInjectionPredicate::threshold)
        ).apply(instance, SurfaceBelowSeaLevelInjectionPredicate::new)
    );

    @Override
    public InjectionPredicateType<?> getType() {
        return InjectionPredicateType.SURFACE_BELOW_SEA_LEVEL;
    }

    @Override
    public boolean shouldApply(BiomeInjectionContext context) {
        return context.topHeight < context.chunkGenerator.getSeaLevel() - threshold;
    }
}
