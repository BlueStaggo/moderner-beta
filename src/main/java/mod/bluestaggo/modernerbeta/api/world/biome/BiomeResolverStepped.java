package mod.bluestaggo.modernerbeta.api.world.biome;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.biome.Biome;

public interface BiomeResolverStepped {
    Holder<Biome> getBiomeForStep(int biomeX, int biomeY, int biomeZ, int step);
    int getStepCount();
    Component getStepName(int step);

    default Component getBiomeNameForStep(int biomeX, int biomeY, int biomeZ, int step) {
        return Component.literal(this.getBiomeForStep(biomeX, biomeY, biomeZ, step)
            .unwrapKey().map(key -> key.location().toString()).orElse("[unregistered]"));
    }
}
