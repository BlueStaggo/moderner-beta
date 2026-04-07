package mod.bluestaggo.modernerbeta.level.biome.injector.rule;

import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.biome.injector.BiomeInjectionContext;
import mod.bluestaggo.modernerbeta.level.biome.injector.BiomeInjectionRule;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.EnumSet;

public class CaveInjectionRule implements BiomeInjectionRule {
    private final ModernBetaBiomeSource biomeSource;

    private final int caveDepthThreshold;

    public CaveInjectionRule(ModernBetaBiomeSource biomeSource, int caveDepthThreshold) {
        this.biomeSource = biomeSource;
        this.caveDepthThreshold = caveDepthThreshold;
    }

    @Override
    public Holder<Biome> apply(int biomeX, int biomeY, int biomeZ) {
        return this.biomeSource.getCaveBiome(biomeX, biomeY, biomeZ);
    }

    @Override
    public boolean applyWhen(BiomeInjectionContext context) {
        return context.getY() >= context.worldMinY && context.getY() + caveDepthThreshold < context.minHeight;
    }

    @Override
    public EnumSet<Step> applicableSteps() {
        return EnumSet.of(Step.PRE);
    }
}
