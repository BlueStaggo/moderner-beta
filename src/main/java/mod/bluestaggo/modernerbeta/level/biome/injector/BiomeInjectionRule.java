package mod.bluestaggo.modernerbeta.level.biome.injector;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.EnumSet;

public interface BiomeInjectionRule {
    Holder<Biome> apply(int biomeX, int biomeY, int biomeZ);
    boolean applyWhen(BiomeInjectionContext context);
    EnumSet<Step> applicableSteps();

    enum Step {
        PRE,  // Injects before surface generation step.
        POST, // Injects after surface generation step.
        ALL   // Injects for structure generation, spawn location.
    }
}
