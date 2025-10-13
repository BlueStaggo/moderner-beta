package mod.bluestaggo.modernerbeta.api.world.provider;

import mod.bluestaggo.modernerbeta.api.world.cavebiome.CaveBiomeProvider;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentType;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.function.Supplier;

public record CaveBiomeProviderType<T extends CaveBiomeProvider>(
    Constructor<T> constructor,
    Supplier<List<SettingsComponentType<?>>> requiredSettingsComponents
) implements ProviderType {
    public T apply(ModernBetaSettings settings, HolderGetter<Biome> biomeRegistry, long seed) {
        return constructor.apply(settings, biomeRegistry, seed);
    }

    public interface Constructor<T extends CaveBiomeProvider> {
        T apply(ModernBetaSettings settings, HolderGetter<Biome> biomeRegistry, long seed);
    }
}
