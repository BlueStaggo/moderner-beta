package mod.bluestaggo.modernerbeta.api.world.provider;

import mod.bluestaggo.modernerbeta.api.world.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentType;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.function.Supplier;

public record BiomeProviderType<T extends BiomeProvider>(
    Constructor<T> constructor,
    Class<T> providerClass,
    Supplier<List<SettingsComponentType<?>>> requiredSettingsComponents
) implements ProviderType {
    public T apply(ModernBetaSettings settings, HolderGetter<Biome> biomeRegistry, long seed) {
        return constructor.apply(settings, biomeRegistry, seed);
    }

    public interface Constructor<T extends BiomeProvider> {
        T apply(ModernBetaSettings settings, HolderGetter<Biome> biomeRegistry, long seed);
    }
}
