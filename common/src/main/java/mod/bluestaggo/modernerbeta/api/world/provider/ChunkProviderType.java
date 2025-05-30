package mod.bluestaggo.modernerbeta.api.world.provider;

import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentType;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;

import java.util.Arrays;
import java.util.List;

public record ChunkProviderType<T extends ChunkProvider>(
    Constructor<T> constructor,
    List<SettingsComponentType<?>> requiredSettingsComponents
) implements ProviderType {
    public ChunkProviderType(Constructor<T> constructor, SettingsComponentType<?>... requiredSettingsComponents) {
        this(constructor, Arrays.asList(requiredSettingsComponents));
    }

    public T apply(ModernBetaChunkGenerator chunkGenerator, long seed) {
        return constructor.apply(chunkGenerator, seed);
    }

    public interface Constructor<T extends ChunkProvider> {
        T apply(ModernBetaChunkGenerator chunkGenerator, long seed);
    }
}
