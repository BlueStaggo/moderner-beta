package mod.bluestaggo.modernerbeta.world.biome.injector;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

@FunctionalInterface
public interface BiomeInjectionResolver {
    public static final BiomeInjectionResolver DEFAULT = (biomeX, biomeY, biomeZ) -> null;
    
    public Holder<Biome> apply(int biomeX, int biomeY, int biomeZ);
}
