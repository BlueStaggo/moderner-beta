package mod.bluestaggo.modernerbeta.level.biome.injection.handler;

import mod.bluestaggo.modernerbeta.api.level.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProviderNoise;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionContext;
import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionRule;
import mod.bluestaggo.modernerbeta.level.biome.injection.InjectionNeeds;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaChunkGenerator;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkHeightmap;
import net.minecraft.core.Holder;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class SimpleBiomeInjectionHandler implements BiomeInjectionHandler {
    private final ModernBetaChunkGenerator modernBetaChunkGenerator;
    private final ModernBetaBiomeSource modernBetaBiomeSource;

    private final List<BiomeInjectionRule> allRules;
    private final ThreadLocal<BiomeInjectionContext> context;

    public SimpleBiomeInjectionHandler(ModernBetaChunkGenerator modernBetaChunkGenerator, ModernBetaBiomeSource modernBetaBiomeSource) {
        this.modernBetaChunkGenerator = modernBetaChunkGenerator;
        this.modernBetaBiomeSource = modernBetaBiomeSource;

        ModernBetaSettings settingsBiome = this.modernBetaBiomeSource.getBiomeSettings()
                .mapPreset(modernBetaBiomeSource.getPresetRegistry(), ModernBetaSettingsPreset::biomeSettings);

        this.allRules = settingsBiome.getOrDefault(SettingsComponentTypes.BIOME_INJECTION_RULES);

        this.context = ThreadLocal.withInitial(() -> {
            BiomeInjectionContext instance = new BiomeInjectionContext(this.modernBetaChunkGenerator, this.modernBetaBiomeSource);
            instance.setupContext();
            return instance;
        });
    }

    private @Nullable Holder<Biome> delegateBiome(
        LevelHeightAccessor level,
        int biomeX, int biomeY, int biomeZ,
        BiomeInjectionRule.Step step,
        EnumSet<InjectionNeeds> ableToFulfill,
        boolean base
    ) {
        BiomeInjectionRule.Step curStep = step;
        if (curStep == BiomeInjectionRule.Step.ALL)
            //? if >=26.3 {
            /*return this.modernBetaBiomeSource.createResolver(null).getNoiseBiome(biomeX, biomeY, biomeZ);
            *///? } else {
            return this.modernBetaBiomeSource.getNoiseBiome(biomeX, biomeY, biomeZ, null);
            //? }

        int prevStepOrdinal = curStep.ordinal() - 1;

        if (prevStepOrdinal >= 0) {
            curStep = BiomeInjectionRule.Step.values()[prevStepOrdinal];
            return this.getBiome(level, biomeX, biomeY, biomeZ, curStep, ableToFulfill, true);
        }

        if (base) {
            //? if >=26.3 {
            /*return this.modernBetaBiomeSource.createResolver(null).getNoiseBiome(biomeX, biomeY, biomeZ);
            *///? } else {
            return this.modernBetaBiomeSource.getNoiseBiome(biomeX, biomeY, biomeZ, null);
            //? }
        }

        return null;
    }

    @Override
    public @Nullable Holder<Biome> getBiome(
        LevelHeightAccessor level,
        int biomeX, int biomeY, int biomeZ,
        BiomeInjectionRule.Step step,
        EnumSet<InjectionNeeds> ableToFulfill,
        boolean shouldDelegate
    ) {
        List<BiomeInjectionRule> rules = this.getRulesForStep(step, ableToFulfill);
        if (rules.isEmpty()) {
            return null;
        }

        BiomeInjectionContext context = this.setupContext(level, biomeX, biomeY, biomeZ, ableToFulfill);

        Holder<Biome> biome = null;

        for (BiomeInjectionRule rule : rules) {
            if (step != BiomeInjectionRule.Step.ALL && step != rule.stepFor())
                continue;

            if (!rule.canFulfill(ableToFulfill))
                continue;

            if (rule.needs().contains(InjectionNeeds.BIOMES) && context.getBiome() == null) {
                if (!shouldDelegate)
                    throw new IllegalStateException("Injector needs a biome, we do not have one, and are unable to delegate.");

                context.setBiome(this.delegateBiome(level, biomeX, biomeY, biomeZ, step, ableToFulfill, true));
            }

            rule.initIfNeeded();
            Holder<Biome> result = rule.apply(context, biomeX, biomeY, biomeZ);
            if (result != null) {
                context.setBiome(result);
                biome = result;
            }
        }

        if (biome == null && shouldDelegate)
            return this.delegateBiome(level, biomeX, biomeY, biomeZ, step, ableToFulfill, false);

        return biome;
    }

    @Override
    public @NotNull List<BiomeInjectionRule> getRulesForStep(
        BiomeInjectionRule.Step step,
        EnumSet<InjectionNeeds> ableToFulfill
    ) {
        if (ableToFulfill.contains(InjectionNeeds.CLIMATE) &&
                !(this.modernBetaBiomeSource.getBiomeProvider() instanceof ClimateSampler)) {
            ableToFulfill.remove(InjectionNeeds.CLIMATE);
        }

        return this.allRules.stream()
            .filter(rule -> step == BiomeInjectionRule.Step.ALL || step == rule.stepFor())
            .toList();
    }

    private BiomeInjectionContext setupContext(
        LevelHeightAccessor level,
        int biomeX, int biomeY, int biomeZ,
        EnumSet<InjectionNeeds> ableToFulfill
    ) {
        BiomeInjectionContext context = this.context.get();

        if (ableToFulfill.contains(InjectionNeeds.HEIGHTS)) {
            int topHeight = this.sampleTopHeight(level, biomeX, biomeZ);
            int minHeight = this.sampleMinHeight(level, biomeX, biomeZ);

            context.setHeights(topHeight, minHeight);
        }

        return context
            .setBiome(null)
            .setFulfillableNeeds(ableToFulfill)
            .setPosition((biomeX << 2) + 2, biomeY << 2, (biomeZ << 2) + 2)
            .setupWorldGenContext(level);
    }

    private int sampleTopHeight(LevelHeightAccessor level, int biomeX, int biomeZ) {
        int x = (biomeX << 2) + 2;
        int z = (biomeZ << 2) + 2;

        return this.modernBetaChunkGenerator.getHeight(x, z, Heightmap.Types.OCEAN_FLOOR_WG, level);
    }

    private int sampleFloorHeight(LevelHeightAccessor level, int biomeX, int biomeZ) {
        int x = (biomeX << 2) + 2;
        int z = (biomeZ << 2) + 2;

        ChunkProvider chunkProvider = this.modernBetaChunkGenerator.getChunkProvider();

        return chunkProvider instanceof ChunkProviderNoise chunkProviderNoise ?
            chunkProviderNoise.getHeight(level, x, z, ChunkHeightmap.Type.SURFACE_FLOOR) :
            chunkProvider.getHeight(level, x, z, Heightmap.Types.OCEAN_FLOOR_WG);
    }

    private int sampleMinHeight(LevelHeightAccessor level, int centerBiomeX, int centerBiomeZ) {
        int minHeight = Integer.MAX_VALUE;

        for (int localBiomeX = -1; localBiomeX <= 1; ++localBiomeX) {
            for (int localBiomeZ = -1; localBiomeZ <= 1; ++localBiomeZ) {
                int biomeX = centerBiomeX + localBiomeX;
                int biomeZ = centerBiomeZ + localBiomeZ;

                minHeight = Math.min(minHeight, this.sampleFloorHeight(level, biomeX, biomeZ));
            }
        }

        return minHeight;
    }
}
