package mod.bluestaggo.modernerbeta.api.level.provider;

import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentType;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaChunkGenerator;

import java.util.List;
import java.util.function.Supplier;

public record ChunkProviderType<T extends ChunkProvider>(
    Constructor<T> constructor,
    Supplier<List<SettingsComponentType<?>>> requiredSettingsComponents
) implements ProviderType {
    public T apply(ModernBetaChunkGenerator chunkGenerator, long seed) {
        return constructor.apply(chunkGenerator, seed);
    }

    public interface Constructor<T extends ChunkProvider> {
        T apply(ModernBetaChunkGenerator chunkGenerator, long seed);
    }
}
