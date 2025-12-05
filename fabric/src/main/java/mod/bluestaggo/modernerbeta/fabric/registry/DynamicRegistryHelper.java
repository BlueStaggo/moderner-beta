package mod.bluestaggo.modernerbeta.fabric.registry;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.fabric.mixin.DynamicRegistriesImplAccessor;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.impl.registry.sync.DynamicRegistriesImpl;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;

import java.util.Objects;

public class DynamicRegistryHelper {
    public static <T> void register(ModernerBeta.CustomDynamicRegistry<T> registry) {
        if (registry.insertAfter() == null) {
            DynamicRegistries.register(registry.key(), registry.codec());
            return;
        }

        register(registry.key(), registry.insertAfter(), registry.codec());
    }

    @SuppressWarnings("UnstableApiUsage")
    public static <T> RegistryDataLoader.RegistryData<T> register(
        ResourceKey<? extends Registry<T>> key,
        ResourceKey<? extends Registry<?>> insertAfter,
        Codec<T> codec
    ) {
        Objects.requireNonNull(key, "Registry key cannot be null");
        Objects.requireNonNull(codec, "Codec cannot be null");

        if (!DynamicRegistriesImpl.DYNAMIC_REGISTRY_KEYS.add(key)) {
            throw new IllegalArgumentException("Dynamic registry " + key + " has already been registered!");
        }

        var entry = new RegistryDataLoader.RegistryData<>(key, codec /*? >=1.21 {*/, false/*?}*/);
        var dynamicRegistries = DynamicRegistriesImplAccessor.getDynamicRegistries();
        int index = -1;

        if (insertAfter != null) {
            index = dynamicRegistries.stream()
                .filter(data -> data.key() == insertAfter)
                .map(dynamicRegistries::indexOf)
                .findFirst()
                .orElse(-1);
        }

        if (index == -1)
            throw new RuntimeException("Could not find dynamic registry " + insertAfter + " in dynamic registries");

        dynamicRegistries.add(index + 1, entry);
        DynamicRegistriesImpl.FABRIC_DYNAMIC_REGISTRY_KEYS.add(key);
        return entry;
    }
}
