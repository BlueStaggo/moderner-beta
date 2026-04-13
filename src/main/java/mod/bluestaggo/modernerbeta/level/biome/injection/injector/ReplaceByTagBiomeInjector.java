package mod.bluestaggo.modernerbeta.level.biome.injection.injector;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionContext;
import mod.bluestaggo.modernerbeta.level.biome.injection.InjectionNeeds;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.EnumSet;
import java.util.Map;

public record ReplaceByTagBiomeInjector(Map<TagKey<Biome>, Holder<Biome>> replacements) implements BiomeInjector {
    public static final com.mojang.serialization.MapCodec<ReplaceByTagBiomeInjector> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            Codec.unboundedMap(TagKey.codec(Registries.BIOME), Biome.CODEC).fieldOf("replacements")
                    .forGetter(ReplaceByTagBiomeInjector::replacements)
        ).apply(instance, ReplaceByTagBiomeInjector::new)
    );

    @Override
    public BiomeInjectorType<?> getType() {
        return BiomeInjectorType.REPLACE_BY_TAG;
    }

    @Override
    public Holder<Biome> apply(BiomeInjectionContext context, int biomeX, int biomeY, int biomeZ) {
        Holder<Biome> biome = context.getBiome();
        for (Map.Entry<TagKey<Biome>, Holder<Biome>> entry : replacements.entrySet()) {
            if (biome.is(entry.getKey())) {
                return entry.getValue();
            }
        }

        return null;
    }

    @Override
    public EnumSet<InjectionNeeds> needs() {
        return EnumSet.of(InjectionNeeds.BIOMES);
    }
}
