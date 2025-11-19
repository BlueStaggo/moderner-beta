package mod.bluestaggo.modernerbeta.level.cavebiome.provider;

import mod.bluestaggo.modernerbeta.api.level.cavebiome.CaveBiomeProvider;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;

public class CaveBiomeProviderNone extends CaveBiomeProvider {
    public CaveBiomeProviderNone(ModernBetaSettings settings, HolderGetter<Biome> biomeRegistry, long seed) {
        super(settings, biomeRegistry, seed);
    }

    @Override
    public Holder<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
        return null;
    }
}
