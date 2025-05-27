package mod.bluestaggo.modernerbeta.fabric.registry;

import com.mojang.serialization.Lifecycle;
import mod.bluestaggo.modernerbeta.registry.RegistryBuilder;
import mod.bluestaggo.modernerbeta.registry.RegistryHelper;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;

public class RegistryHelperImpl implements RegistryHelper {
    private <T> RegistryBuilder<T> from(MutableRegistry<T> registry) {
        return new RegistryBuilderImpl<>(registry);
    }

    @Override
    public <T> RegistryBuilder<T> createSimple(RegistryKey<Registry<T>> key) {
        return from(new SimpleRegistry<>(key, Lifecycle.stable(), false));
    }

    @Override
    public <T> RegistryBuilder<T> createDefaulted(RegistryKey<Registry<T>> key, Identifier defaultKey) {
        return from(new SimpleDefaultedRegistry<>(defaultKey.toString(), key, Lifecycle.stable(), false));
    }
}
