package mod.bluestaggo.modernerbeta.level.biome.injection.injector;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionContext;
import mod.bluestaggo.modernerbeta.level.biome.injection.InjectionNeeds;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkCache;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;

import java.util.EnumSet;

public class Cache2DBiomeInjector implements BiomeInjector {
    public static final com.mojang.serialization.MapCodec<Cache2DBiomeInjector> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            BiomeInjector.TYPE_CODEC.fieldOf("injector").forGetter(i -> i.injector)
        ).apply(instance, Cache2DBiomeInjector::new)
    );

    private final BiomeInjector injector;
    private ChunkCache<Int2ObjectMap<Holder<Biome>>> biomeCache;

    public Cache2DBiomeInjector(BiomeInjector injector) {
        this.injector = injector;
    }

    @Override
    public BiomeInjectorType<?> getType() {
        return BiomeInjectorType.CACHE_2D;
    }

    @Override
    public void initIfNeeded() {
        if (this.biomeCache == null) {
            final int capacity = 4 * 4;

            this.biomeCache = new ChunkCache<>(
                "biome_injector_2d_cache",
                (chunkX, chunkZ) -> new Int2ObjectArrayMap<>(capacity)
            );
        }
    }

    @Override
    public Holder<Biome> apply(BiomeInjectionContext context, int biomeX, int biomeY, int biomeZ) {
        ChunkPos chunkPos = context.getChunkPos();
        Int2ObjectMap<Holder<Biome>> lookup = this.biomeCache.get(chunkPos.x, chunkPos.z);

        int localBiomeX = biomeX & 3;
        int localBiomeZ = biomeZ & 3;

        int pos = localBiomeX << 2 | localBiomeZ;
        return lookup.computeIfAbsent(pos, p -> this.injector.apply(context, biomeX, 16, biomeZ));
    }

    @Override
    public EnumSet<InjectionNeeds> needs() {
        return this.injector.needs();
    }
}
