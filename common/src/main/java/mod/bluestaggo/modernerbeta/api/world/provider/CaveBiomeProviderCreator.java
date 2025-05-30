package mod.bluestaggo.modernerbeta.api.world.provider;

import mod.bluestaggo.modernerbeta.api.world.cavebiome.CaveBiomeProvider;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.biome.Biome;

@FunctionalInterface
public interface CaveBiomeProviderCreator {
    CaveBiomeProvider apply(ModernBetaSettings settings, RegistryEntryLookup<Biome> biomeRegistry, long seed);
}
