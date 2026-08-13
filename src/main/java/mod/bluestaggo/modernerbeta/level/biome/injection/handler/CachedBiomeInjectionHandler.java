package mod.bluestaggo.modernerbeta.level.biome.injection.handler;

import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionRule;
import mod.bluestaggo.modernerbeta.level.biome.injection.InjectionNeeds;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkCache;
import net.minecraft.core.Holder;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CachedBiomeInjectionHandler implements BiomeInjectionHandler {
    private static final int CACHE_CAPACITY = 64;

    private final BiomeInjectionHandler baseHandler;
    private final Map<BiomeInjectionRule.Step, ChunkCache<ConcurrentMap<CacheKey, Optional<Holder<Biome>>>>> stageCaches;

    public CachedBiomeInjectionHandler(BiomeInjectionHandler baseHandler) {
        this.baseHandler = baseHandler;
        this.stageCaches = new EnumMap<>(BiomeInjectionRule.Step.class);

        for (BiomeInjectionRule.Step step : BiomeInjectionRule.Step.values()) {
            this.stageCaches.put(step, new ChunkCache<>(
                "biome_injection_" + step.name(),
                CACHE_CAPACITY,
                (chunkX, chunkZ) -> new ConcurrentHashMap<>()
            ));
        }
    }

    @Override
    public @Nullable Holder<Biome> getBiome(
        LevelHeightAccessor level,
        int biomeX, int biomeY, int biomeZ,
        BiomeInjectionRule.Step step,
        EnumSet<InjectionNeeds> ableToFulfill,
        boolean shouldDelegate
    ) {
        EnumSet<InjectionNeeds> needs = ableToFulfill.clone();
        CacheKey key = new CacheKey(biomeX & 3, biomeY, biomeZ & 3, mask(needs), shouldDelegate);
        ConcurrentMap<CacheKey, Optional<Holder<Biome>>> cache = this.stageCaches
            .get(step)
            .get(biomeX >> 2, biomeZ >> 2);

        return cache.computeIfAbsent(
            key,
            ignored -> Optional.ofNullable(this.getUncachedBiome(
                level,
                biomeX, biomeY, biomeZ,
                step,
                needs,
                shouldDelegate
            ))
        ).orElse(null);
    }

    private @Nullable Holder<Biome> getUncachedBiome(
        LevelHeightAccessor level,
        int biomeX, int biomeY, int biomeZ,
        BiomeInjectionRule.Step step,
        EnumSet<InjectionNeeds> ableToFulfill,
        boolean shouldDelegate
    ) {
        Holder<Biome> biome = this.baseHandler.getBiome(
            level,
            biomeX, biomeY, biomeZ,
            step,
            ableToFulfill,
            shouldDelegate
        );

        if (biome == null && shouldDelegate && step == BiomeInjectionRule.Step.POST) {
            biome = this.getBiome(
                level,
                biomeX, biomeY, biomeZ,
                BiomeInjectionRule.Step.PRE,
                ableToFulfill,
                true
            );
        }

        return biome;
    }

    @Override
    public @NotNull List<BiomeInjectionRule> getRulesForStep(
        BiomeInjectionRule.Step step,
        EnumSet<InjectionNeeds> ableToFulfill
    ) {
        return this.baseHandler.getRulesForStep(step, ableToFulfill.clone());
    }

    @Override
    public void clear() {
        this.stageCaches.values().forEach(ChunkCache::clear);
        this.baseHandler.clear();
    }

    private static int mask(EnumSet<InjectionNeeds> needs) {
        int mask = 0;

        for (InjectionNeeds need : needs) {
            mask |= 1 << need.ordinal();
        }

        return mask;
    }

    private record CacheKey(int localBiomeX, int biomeY, int localBiomeZ, int needs, boolean shouldDelegate) {
    }
}
