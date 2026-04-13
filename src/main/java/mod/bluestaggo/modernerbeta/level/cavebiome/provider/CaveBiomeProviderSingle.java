package mod.bluestaggo.modernerbeta.level.cavebiome.provider;

import mod.bluestaggo.modernerbeta.api.level.cavebiome.CaveBiomeProvider;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import java.util.Set;

public class CaveBiomeProviderSingle extends CaveBiomeProvider {
    private final ResourceKey<Biome> biome;
    
    public CaveBiomeProviderSingle(ModernBetaSettings settings, HolderGetter<Biome> biomeRegistry, long seed) {
        super(settings, biomeRegistry, seed);

        this.biome = ResourceKey.create(Registries.BIOME, this.settings.getOrThrow(SettingsComponentTypes.SINGLE_BIOME));
    }

    @Override
    public Holder<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
        return this.biomeRegistry.getOrThrow(this.biome);
    }
    
    @Override
    public Set<Holder<Biome>> getBiomes() {
        return Set.of(this.biomeRegistry.getOrThrow(this.biome));
    }
}
