package mod.bluestaggo.modernerbeta.api.world.provider;

import mod.bluestaggo.modernerbeta.api.world.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.util.function.TriFunction;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.biome.Biome;

public record BiomeProviderCreator<T extends BiomeProvider>(TriFunction<ModernBetaSettings, RegistryEntryLookup<Biome>, Long, T> constructor, Class<T> providerClass) {
    public T apply(ModernBetaSettings settings, RegistryEntryLookup<Biome> biomeRegistry, long seed) {
        return constructor.apply(settings, biomeRegistry, seed);
    }
}
