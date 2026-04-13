package mod.bluestaggo.modernerbeta.level.biome.injection.injector;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionContext;
import mod.bluestaggo.modernerbeta.level.biome.injection.InjectionNeeds;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.EnumSet;
import java.util.Map;

public record ReplaceBiomeInjector(Map<Holder<Biome>, Holder<Biome>> replacements) implements BiomeInjector {
    public static final com.mojang.serialization.MapCodec<ReplaceBiomeInjector> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            Codec.unboundedMap(Biome.CODEC, Biome.CODEC).fieldOf("replacements").forGetter(ReplaceBiomeInjector::replacements)
        ).apply(instance, ReplaceBiomeInjector::new)
    );

    @Override
    public BiomeInjectorType<?> getType() {
        return BiomeInjectorType.REPLACE;
    }

    @Override
    public Holder<Biome> apply(BiomeInjectionContext context, int biomeX, int biomeY, int biomeZ) {
        Holder<Biome> biome = context.getBiome();
        if (replacements.containsKey(biome)) {
            return replacements.get(biome);
        }

        return null;
    }

    @Override
    public EnumSet<InjectionNeeds> needs() {
        return EnumSet.of(InjectionNeeds.BIOMES);
    }
}
