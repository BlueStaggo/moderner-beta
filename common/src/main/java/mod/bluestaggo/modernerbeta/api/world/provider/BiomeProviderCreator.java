package mod.bluestaggo.modernerbeta.api.world.provider;

import mod.bluestaggo.modernerbeta.api.world.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.util.function.TriFunction;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.biome.Biome;

public record BiomeProviderCreator<T extends BiomeProvider>(TriFunction<NbtCompound, RegistryEntryLookup<Biome>, Long, T> constructor, Class<T> providerClass) {
    public T apply(NbtCompound settings, RegistryEntryLookup<Biome> biomeRegistry, long seed) {
        return constructor.apply(settings, biomeRegistry, seed);
    }
}
