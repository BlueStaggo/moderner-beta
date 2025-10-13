package mod.bluestaggo.modernerbeta.util.chunk;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import org.apache.commons.lang3.function.TriFunction;

import java.util.concurrent.locks.StampedLock;

/*
 * Generic threadsafe(???) cache for anything that outputs T given a world context and a pair of integer chunk coordinates.
 * 
 */
public class WorldChunkCache<T> {
    public static final int DEFAULT_SIZE = 512;
    public static final boolean DEFAULT_EVICT = true;

    @SuppressWarnings("unused")
    private final String name;
    private final int capacity;
    private final boolean evictOldChunks;

    private final TriFunction<LevelHeightAccessor, Integer, Integer, T> chunkFunc;
    private final Long2ObjectLinkedOpenHashMap<T> chunkMap;

    private final StampedLock lock;

    public WorldChunkCache(String name, int capacity, boolean evictOldChunks, TriFunction<LevelHeightAccessor, Integer, Integer, T> chunkFunc) {
        this.name = name;
        this.capacity = capacity;
        this.evictOldChunks = evictOldChunks;

        this.chunkFunc = chunkFunc;
        this.chunkMap = new Long2ObjectLinkedOpenHashMap<>(capacity);

        this.lock = new StampedLock();
    }

    public WorldChunkCache(String name, int capacity, TriFunction<LevelHeightAccessor, Integer, Integer, T> chunkFunc) {
        this(name, capacity, DEFAULT_EVICT, chunkFunc);
    }

    public WorldChunkCache(String name, TriFunction<LevelHeightAccessor, Integer, Integer, T> chunkFunc) {
        this(name, DEFAULT_SIZE, DEFAULT_EVICT, chunkFunc);
    }
    
    public void clear() {
        long stamp = this.lock.writeLock();
        try {
            this.chunkMap.clear();
            this.chunkMap.trim();
        } finally {
            this.lock.unlock(stamp);
        }
    }
    
    public T get(LevelHeightAccessor world, int chunkX, int chunkZ) {
        T chunk;
        
        long key = ChunkPos.asLong(chunkX, chunkZ);
        long stamp = this.lock.readLock();
        
        try {
            while ((chunk = this.chunkMap.get(key)) == null) {
                // Attempt to upgrade read lock to write lock w/o blocking
                long writeStamp = this.lock.tryConvertToWriteLock(stamp);
                
                // Write lock, if:
                // => lock upgrade succeeds w/o blocking
                // => blocked write is acquired anyway (see below)
                if (writeStamp != 0) {
                    stamp = writeStamp;
                    chunk = this.createChunk(key, world, chunkX, chunkZ);
                    
                    break;
                }
                
                // Lock upgrade failed so use blocking write lock.
                this.lock.unlockRead(stamp);
                stamp = this.lock.writeLock();
            }
        } finally {
            this.lock.unlock(stamp);
        }
        
        return chunk;
    }
    
    private T createChunk(long key, LevelHeightAccessor world, int chunkX, int chunkZ) {
        // Ensure cache size remains below capacity
        if (this.evictOldChunks && this.chunkMap.size() >= this.capacity) {
            this.chunkMap.removeFirst();
        }
        
        T chunk = this.chunkFunc.apply(world, chunkX, chunkZ);
        this.chunkMap.put(key, chunk);
        
        return chunk;
    }
}
