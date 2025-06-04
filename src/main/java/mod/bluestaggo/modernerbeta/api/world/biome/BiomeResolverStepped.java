package mod.bluestaggo.modernerbeta.api.world.biome;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.world.biome.Biome;

public interface BiomeResolverStepped {
    RegistryEntry<Biome> getBiomeForStep(int biomeX, int biomeY, int biomeZ, int step);
    int getStepCount();
    Text getStepName(int step);

    default Text getBiomeNameForStep(int biomeX, int biomeY, int biomeZ, int step) {
        return Text.literal(this.getBiomeForStep(biomeX, biomeY, biomeZ, step)
            .getKey().map(key -> key.getValue().toString()).orElse("[unregistered]"));
    }
}
