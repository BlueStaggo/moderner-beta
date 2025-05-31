package mod.bluestaggo.modernerbeta.world.cavebiome.provider;

import mod.bluestaggo.modernerbeta.api.world.cavebiome.CaveBiomeProvider;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class CaveBiomeProviderNone extends CaveBiomeProvider {
    public CaveBiomeProviderNone(ModernBetaSettings settings, RegistryEntryLookup<Biome> biomeRegistry, long seed) {
        super(settings, biomeRegistry, seed);
    }

    @Override
    public RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
        return null;
    }
}
