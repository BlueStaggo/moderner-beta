package mod.bluestaggo.modernerbeta.api.world.provider;

import mod.bluestaggo.modernerbeta.settings.SettingsComponentType;

import java.util.List;
import java.util.function.Supplier;

public interface ProviderType {
    Supplier<List<SettingsComponentType<?>>> requiredSettingsComponents();
}
