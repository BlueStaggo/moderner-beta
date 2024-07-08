package mod.bluestaggo.modernerbeta.world.biome.provider;

import mod.bluestaggo.modernerbeta.api.world.biome.BiomeProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.util.List;

public class BiomeProviderSingle extends BiomeProvider {
    private final RegistryKey<Biome> biomeKey;
    
    public BiomeProviderSingle(NbtCompound settings, RegistryEntryLookup<Biome> biomeRegistry, long seed) {
        super(settings, biomeRegistry, seed);
        
        this.biomeKey = RegistryKey.of(RegistryKeys.BIOME, new Identifier(this.settings.singleBiome));
    }

    @Override
    public RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
        return this.biomeRegistry.getOrThrow(this.biomeKey);
    }
    
    @Override
    public List<RegistryEntry<Biome>> getBiomes() {
        return List.of(this.biomeRegistry.getOrThrow(this.biomeKey));
    }
}
