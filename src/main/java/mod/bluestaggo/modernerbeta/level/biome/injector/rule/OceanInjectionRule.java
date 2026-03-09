package mod.bluestaggo.modernerbeta.level.biome.injector.rule;

import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.biome.injector.BiomeInjectionContext;
import mod.bluestaggo.modernerbeta.level.biome.injector.BiomeInjectionRule2D;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.EnumSet;

public class OceanInjectionRule implements BiomeInjectionRule2D {
    protected final ModernBetaBiomeSource biomeSource;

    private final int seaLevel;
    private final int oceanDepthThreshold;

    public OceanInjectionRule(ModernBetaBiomeSource biomeSource, int seaLevel, int oceanDepthThreshold) {
        this.biomeSource = biomeSource;
        this.seaLevel = seaLevel;
        this.oceanDepthThreshold = oceanDepthThreshold;
    }

    @Override
    public Holder<Biome> apply(int biomeX, int biomeZ) {
        return this.biomeSource.getOceanBiome(biomeX, biomeZ);
    }

    @Override
    public boolean applyWhen(BiomeInjectionContext context) {
        return context.topHeight < seaLevel - oceanDepthThreshold;
    }

    @Override
    public EnumSet<Step> applicableSteps() {
        return EnumSet.of(Step.POST);
    }
}
