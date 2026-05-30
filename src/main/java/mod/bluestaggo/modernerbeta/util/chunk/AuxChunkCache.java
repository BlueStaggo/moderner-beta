package mod.bluestaggo.modernerbeta.util.chunk;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.world.level.ChunkPos;

import java.util.concurrent.locks.StampedLock;

/*
 * Generic threadsafe(???) cache for anything that outputs T given an auxiliary context and a pair of integer chunk coordinates.
 * 
 */
public class AuxChunkCache<A, T> {
    public static final int DEFAULT_SIZE = 512;
    public static final boolean DEFAULT_EVICT = true;

    @SuppressWarnings("unused")
    private final String name;
    private final int capacity;
    private final boolean evictOldChunks;

    private final AuxIntFunction<A, T> chunkFunc;
    private final Long2ObjectLinkedOpenHashMap<T> chunkMap;

    private final StampedLock lock;

    public AuxChunkCache(String name, int capacity, boolean evictOldChunks, AuxIntFunction<A, T> chunkFunc) {
        this.name = name;
        this.capacity = capacity;
        this.evictOldChunks = evictOldChunks;

        this.chunkFunc = chunkFunc;
        this.chunkMap = new Long2ObjectLinkedOpenHashMap<>(capacity);

        this.lock = new StampedLock();
    }

    public AuxChunkCache(String name, int capacity, AuxIntFunction<A, T> chunkFunc) {
        this(name, capacity, DEFAULT_EVICT, chunkFunc);
    }

    public AuxChunkCache(String name, AuxIntFunction<A, T> chunkFunc) {
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
    
    public T get(A aux, int chunkX, int chunkZ) {
        T chunk;
        
        long key = ChunkPos.pack(chunkX, chunkZ);
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
                    chunk = this.createChunk(key, aux, chunkX, chunkZ);
                    
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
    
    private T createChunk(long key, A aux, int chunkX, int chunkZ) {
        // Ensure cache size remains below capacity
        if (this.evictOldChunks && this.chunkMap.size() >= this.capacity) {
            this.chunkMap.removeFirst();
        }
        
        T chunk = this.chunkFunc.apply(aux, chunkX, chunkZ);
        this.chunkMap.put(key, chunk);
        
        return chunk;
    }
    
    @FunctionalInterface
    public interface AuxIntFunction<A, T> {
        T apply(A aux, int i, int j);
    }
}
