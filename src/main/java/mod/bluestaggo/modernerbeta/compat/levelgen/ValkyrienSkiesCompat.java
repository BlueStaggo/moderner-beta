package mod.bluestaggo.modernerbeta.compat.levelgen;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ValkyrienSkiesCompat implements LevelGenCompatHelper {
    private final Object chunkAllocator;
    private final Method isChunkInShipyardMethod;

    public ValkyrienSkiesCompat() {
        try {
            Class<?> allocatorClass = Class.forName("org.valkyrienskies.mod.common.VS2ChunkAllocator");
            Field instanceField = allocatorClass.getField("INSTANCE");
            this.chunkAllocator = instanceField.get(null);
            this.isChunkInShipyardMethod = allocatorClass.getMethod("isChunkInShipyardCompanion", int.class, int.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialise Valkyrien Skies compatibility!", e);
        }
    }

    @Override
    public boolean skipGeneratingChunk(int x, int z) {
        try {
            return (boolean) isChunkInShipyardMethod.invoke(chunkAllocator, x, z);
        } catch (Exception ignored) {}

        return false;
    }
}
