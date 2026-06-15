package mod.bluestaggo.modernerbeta.level.biome.injection.handler;

import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionRule;
import mod.bluestaggo.modernerbeta.level.biome.injection.InjectionNeeds;
import net.minecraft.core.Holder;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class CachedBiomeInjectionHandler implements BiomeInjectionHandler {
    private final BiomeInjectionHandler baseHandler;

    public CachedBiomeInjectionHandler(BiomeInjectionHandler baseHandler) {
        this.baseHandler = baseHandler;
    }

    @Override
    public @Nullable Holder<Biome> getBiome(
        LevelHeightAccessor level,
        int biomeX, int biomeY, int biomeZ,
        BiomeInjectionRule.Step step,
        EnumSet<InjectionNeeds> ableToFulfill,
        boolean shouldDelegate
    ) {
        return null;
    }

    @Override
    public @NotNull List<BiomeInjectionRule> getRulesForStep(
        BiomeInjectionRule.Step step,
        EnumSet<InjectionNeeds> ableToFulfill
    ) {
        return this.baseHandler.getRulesForStep(step, ableToFulfill);
    }
}
