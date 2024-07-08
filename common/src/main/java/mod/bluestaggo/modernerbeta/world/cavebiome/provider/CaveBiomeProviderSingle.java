package mod.bluestaggo.modernerbeta.world.cavebiome.provider;

import mod.bluestaggo.modernerbeta.api.world.cavebiome.CaveBiomeProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.util.List;

public class CaveBiomeProviderSingle extends CaveBiomeProvider {
    private final RegistryKey<Biome> biome;
    
    public CaveBiomeProviderSingle(NbtCompound settings, RegistryEntryLookup<Biome> biomeRegistry, long seed) {
        super(settings, biomeRegistry, seed);

        this.biome = RegistryKey.of(RegistryKeys.BIOME, Identifier.of(this.settings.singleBiome));
    }

    @Override
    public RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
        return this.biomeRegistry.getOrThrow(this.biome);
    }
    
    @Override
    public List<RegistryEntry<Biome>> getBiomes() {
        return List.of(this.biomeRegistry.getOrThrow(this.biome));
    }
}
