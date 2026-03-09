package mod.bluestaggo.modernerbeta.level.biome.injector.rule;

import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.biome.injector.BiomeInjectionContext;
import mod.bluestaggo.modernerbeta.level.biome.injector.BiomeInjectionRule2D;
import mod.bluestaggo.modernerbeta.settings.component.WorldBorderLocation;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.EnumSet;

public class OutOfBoundsInjectionRule implements BiomeInjectionRule2D {
    private final ModernBetaBiomeSource biomeSource;

    private final WorldBorderLocation worldBorderLocation;

    public OutOfBoundsInjectionRule(ModernBetaBiomeSource biomeSource, WorldBorderLocation worldBorderLocation) {
        this.biomeSource = biomeSource;
        this.worldBorderLocation = worldBorderLocation;
    }

    @Override
    public Holder<Biome> apply(int biomeX, int biomeZ) {
        return this.biomeSource.getOutOfBoundsBiome();
    }

    @Override
    public boolean applyWhen(BiomeInjectionContext context) {
        return !worldBorderLocation.containsPoint(context.getX(), context.getZ(), 4);
    }

    @Override
    public EnumSet<Step> applicableSteps() {
        return EnumSet.of(Step.PRE, Step.POST);
    }
}
