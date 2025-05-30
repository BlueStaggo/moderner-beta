package mod.bluestaggo.modernerbeta.api.world.provider;

import mod.bluestaggo.modernerbeta.api.world.cavebiome.CaveBiomeProvider;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentType;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.biome.Biome;

import java.util.Arrays;
import java.util.List;

public record CaveBiomeProviderType<T extends CaveBiomeProvider>(
    Constructor<T> constructor,
    List<SettingsComponentType<?>> requiredSettingsComponents
) implements ProviderType {
    public CaveBiomeProviderType(Constructor<T> constructor, SettingsComponentType<?>... requiredSettingsComponents) {
        this(constructor, Arrays.asList(requiredSettingsComponents));
    }

    public T apply(ModernBetaSettings settings, RegistryEntryLookup<Biome> biomeRegistry, long seed) {
        return constructor.apply(settings, biomeRegistry, seed);
    }

    public interface Constructor<T extends CaveBiomeProvider> {
        T apply(ModernBetaSettings settings, RegistryEntryLookup<Biome> biomeRegistry, long seed);
    }
}
