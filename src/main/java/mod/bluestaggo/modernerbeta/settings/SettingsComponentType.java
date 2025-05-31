package mod.bluestaggo.modernerbeta.settings;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;

import java.util.Map;

public record SettingsComponentType<T>(Codec<T> codec, T defaultValue) {
    public static final Codec<SettingsComponentType<?>> CODEC
        = Codec.lazyInitialized(ModernBetaRegistries.SETTINGS_COMPONENT_TYPE::getCodec);
    public static final Codec<Map<SettingsComponentType<?>, Object>> TYPE_TO_VALUE_MAP_CODEC
        = Codec.dispatchedMap(CODEC, SettingsComponentType::codec);
}
