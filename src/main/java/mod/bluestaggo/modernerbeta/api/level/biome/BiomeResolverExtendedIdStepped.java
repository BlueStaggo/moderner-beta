package mod.bluestaggo.modernerbeta.api.level.biome;

import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import net.minecraft.world.level.biome.Biome;

public interface BiomeResolverExtendedIdStepped extends BiomeResolverExtendedId, BiomeResolverStepped {
    ExtendedHolder<Biome> getExtendedBiomeIdForStep(int biomeX, int biomeY, int biomeZ, int step);
}
