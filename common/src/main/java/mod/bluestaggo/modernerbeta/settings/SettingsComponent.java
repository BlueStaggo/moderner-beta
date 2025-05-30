package mod.bluestaggo.modernerbeta.settings;

public record SettingsComponent<T>(SettingsComponentType<T> type, T value) {
}
