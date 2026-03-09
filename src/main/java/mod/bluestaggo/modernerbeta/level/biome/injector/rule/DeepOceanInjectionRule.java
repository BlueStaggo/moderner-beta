package mod.bluestaggo.modernerbeta.level.biome.injector.rule;

import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

public class DeepOceanInjectionRule extends OceanInjectionRule {
    public DeepOceanInjectionRule(ModernBetaBiomeSource biomeSource, int seaLevel, int oceanDepthThreshold) {
        super(biomeSource, seaLevel, oceanDepthThreshold);
    }

    @Override
    public Holder<Biome> apply(int biomeX, int biomeZ) {
        return this.biomeSource.getDeepOceanBiome(biomeX, biomeZ);
    }
}
