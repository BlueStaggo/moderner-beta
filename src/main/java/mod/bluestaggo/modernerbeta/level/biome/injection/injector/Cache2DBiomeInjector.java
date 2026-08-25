package mod.bluestaggo.modernerbeta.level.biome.injection.injector;

import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionContext;
import mod.bluestaggo.modernerbeta.level.biome.injection.InjectionNeeds;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkCache;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Cache2DBiomeInjector implements BiomeInjector {
    private static final int CACHE_CAPACITY = 64;

    public static final com.mojang.serialization.MapCodec<Cache2DBiomeInjector> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            BiomeInjector.TYPE_CODEC.fieldOf("injector").forGetter(i -> i.injector)
        ).apply(instance, Cache2DBiomeInjector::new)
    );

    private final BiomeInjector injector;
    private final ChunkCache<ConcurrentMap<CacheKey, Optional<Holder<Biome>>>> biomeCache;

    public Cache2DBiomeInjector(BiomeInjector injector) {
        this.injector = injector;
        this.biomeCache = new ChunkCache<>(
            "biome_injector_2d",
            CACHE_CAPACITY,
            (chunkX, chunkZ) -> new ConcurrentHashMap<>()
        );
    }

    @Override
    public BiomeInjectorType<?> getType() {
        return BiomeInjectorType.CACHE_2D;
    }

    @Override
    public void initIfNeeded() {
        this.injector.initIfNeeded();
    }

    @Override
    public void clear() {
        this.biomeCache.clear();
        this.injector.clear();
    }

    @Override
    public Holder<Biome> apply(BiomeInjectionContext context, int biomeX, int biomeY, int biomeZ) {
        CacheKey key = new CacheKey(
            context.biomeSource,
            context.getBiome(),
            biomeX & 3,
            biomeZ & 3,
            mask(context.getFulfillableNeeds())
        );

        return this.biomeCache
            .get(biomeX >> 2, biomeZ >> 2)
            .computeIfAbsent(
                key,
                ignored -> Optional.ofNullable(this.injector.apply(context, biomeX, 0, biomeZ))
            )
            .orElse(null);
    }

    @Override
    public Set<Holder<Biome>> getPossibleBiomes() {
        return this.injector.getPossibleBiomes();
    }

    @Override
    public EnumSet<InjectionNeeds> needs() {
        return this.injector.needs();
    }

    private static int mask(EnumSet<InjectionNeeds> needs) {
        int mask = 0;

        for (InjectionNeeds need : needs) {
            mask |= 1 << need.ordinal();
        }

        return mask;
    }

    private record CacheKey(
        ModernBetaBiomeSource biomeSource,
        Holder<Biome> inputBiome,
        int localBiomeX,
        int localBiomeZ,
        int needs
    ) {
    }
}
