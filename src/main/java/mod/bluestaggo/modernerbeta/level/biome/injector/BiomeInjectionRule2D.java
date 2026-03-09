package mod.bluestaggo.modernerbeta.level.biome.injector;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

public interface BiomeInjectionRule2D extends BiomeInjectionRule {
    @Override
    default Holder<Biome> apply(int biomeX, int biomeY, int biomeZ) {
        return apply(biomeX, biomeZ);
    }

    Holder<Biome> apply(int biomeX, int biomeZ);
}
