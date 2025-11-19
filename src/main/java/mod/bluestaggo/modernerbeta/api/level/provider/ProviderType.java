package mod.bluestaggo.modernerbeta.api.level.provider;

import mod.bluestaggo.modernerbeta.settings.SettingsComponentType;

import java.util.List;
import java.util.function.Supplier;

public interface ProviderType {
    Supplier<List<SettingsComponentType<?>>> requiredSettingsComponents();
}
