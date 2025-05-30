package mod.bluestaggo.modernerbeta.api.world.provider;

import mod.bluestaggo.modernerbeta.api.world.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentType;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.biome.Biome;

import java.util.Arrays;
import java.util.List;

public record BiomeProviderType<T extends BiomeProvider>(
    Constructor<T> constructor,
    Class<T> providerClass,
    List<SettingsComponentType<?>> requiredSettingsComponents
) implements ProviderType {
    public BiomeProviderType(Constructor<T> constructor, Class<T> providerClass, SettingsComponentType<?>... requiredSettingsComponents) {
        this(constructor, providerClass, Arrays.asList(requiredSettingsComponents));
    }

    public T apply(ModernBetaSettings settings, RegistryEntryLookup<Biome> biomeRegistry, long seed) {
        return constructor.apply(settings, biomeRegistry, seed);
    }

    public interface Constructor<T extends BiomeProvider> {
        T apply(ModernBetaSettings settings, RegistryEntryLookup<Biome> biomeRegistry, long seed);
    }
}
