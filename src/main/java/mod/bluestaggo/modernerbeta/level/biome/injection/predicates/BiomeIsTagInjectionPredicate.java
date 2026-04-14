package mod.bluestaggo.modernerbeta.level.biome.injection.predicates;

import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionContext;
import mod.bluestaggo.modernerbeta.level.biome.injection.InjectionNeeds;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.EnumSet;

public record BiomeIsTagInjectionPredicate(TagKey<Biome> tag) implements InjectionPredicate {
    public static final com.mojang.serialization.MapCodec<BiomeIsTagInjectionPredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            TagKey.codec(Registries.BIOME).fieldOf("tag").forGetter(BiomeIsTagInjectionPredicate::tag)
        ).apply(instance, BiomeIsTagInjectionPredicate::new)
    );

    @Override
    public InjectionPredicateType<?> getType() {
        return InjectionPredicateType.BIOME_IS_TAG;
    }

    @Override
    public boolean shouldApply(BiomeInjectionContext context) {
        return context.getBiome().is(tag);
    }

    @Override
    public EnumSet<InjectionNeeds> needs() {
        return EnumSet.of(InjectionNeeds.BIOMES);
    }
}
