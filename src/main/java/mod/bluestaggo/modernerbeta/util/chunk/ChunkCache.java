package mod.bluestaggo.modernerbeta.util.chunk;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.util.math.ChunkPos;

import java.util.concurrent.ExecutionException;

/**
 * Generic thread-safe cache for anything that outputs T given pair of integer chunk coordinates.
 */
public class ChunkCache<T> {
    public static final int DEFAULT_SIZE = 512;

    private final LoadingCache<Long, T> cache;
    
    public ChunkCache(ChunkFunc<T> chunkFunc, int capacity) {
        this.cache = CacheBuilder.newBuilder()
            .maximumSize(capacity)
            .build(CacheLoader.from(chunkFunc::apply));
    }

    public ChunkCache(ChunkFunc<T> chunkFunc) {
        this(chunkFunc, DEFAULT_SIZE);
    }
    
    public T get(int chunkX, int chunkZ) {
        long key = ChunkPos.toLong(chunkX, chunkZ);
        try {
            return this.cache.get(key);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @FunctionalInterface
    public interface ChunkFunc<T> {
        T apply(int chunkX, int chunkZ);

        default T apply(long coord) {
            return this.apply(ChunkPos.getPackedX(coord), ChunkPos.getPackedZ(coord));
        }
    }
}
