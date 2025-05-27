package mod.bluestaggo.modernerbeta.api.world.provider;

import mod.bluestaggo.modernerbeta.api.world.cavebiome.CaveBiomeProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.biome.Biome;

@FunctionalInterface
public interface CaveBiomeProviderCreator {
    CaveBiomeProvider apply(NbtCompound settings, RegistryEntryLookup<Biome> biomeRegistry, long seed);
}
