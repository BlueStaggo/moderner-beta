package mod.bluestaggo.modernerbeta.api.world.provider;

import mod.bluestaggo.modernerbeta.settings.SettingsComponentType;

import java.util.List;

public interface ProviderType {
    List<SettingsComponentType<?>> requiredSettingsComponents();
}
