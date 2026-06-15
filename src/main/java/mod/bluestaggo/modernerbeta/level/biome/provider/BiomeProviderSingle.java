package mod.bluestaggo.modernerbeta.level.biome.provider;

import mod.bluestaggo.modernerbeta.api.level.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;

import java.util.Set;

public class BiomeProviderSingle extends BiomeProvider {
    private final Holder<Biome> biome;
    
    public BiomeProviderSingle(ModernBetaSettings settings, HolderGetter<Biome> biomeRegistry, long seed) {
        super(settings, biomeRegistry, seed);

        this.biome = this.settings.getOrThrow(SettingsComponentTypes.SINGLE_BIOME);
    }

    @Override
    public Holder<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
        return this.biome;
    }
    
    @Override
    public Set<Holder<Biome>> getBiomes() {
        return Set.of(this.biome);
    }
}
