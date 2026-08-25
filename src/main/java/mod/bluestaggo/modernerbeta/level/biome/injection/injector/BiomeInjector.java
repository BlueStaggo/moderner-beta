package mod.bluestaggo.modernerbeta.level.biome.injection.injector;

import com.mojang.serialization.MapCodec;
import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionContext;
import mod.bluestaggo.modernerbeta.level.biome.injection.InjectionNeeds;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.EnumSet;
import java.util.Set;

public interface BiomeInjector {
    MapCodec<BiomeInjector> TYPE_CODEC = ModernBetaRegistries.BIOME_INJECTOR.byNameCodec()
        .dispatchMap(BiomeInjector::getType, BiomeInjectorType::codec);

    BiomeInjectorType<?> getType();

    default void initIfNeeded() {
    }

    default void clear() {
    }

    Holder<Biome> apply(BiomeInjectionContext context, int biomeX, int biomeY, int biomeZ);

    Set<Holder<Biome>> getPossibleBiomes();

    default EnumSet<InjectionNeeds> needs() {
        return EnumSet.noneOf(InjectionNeeds.class);
    }
}
