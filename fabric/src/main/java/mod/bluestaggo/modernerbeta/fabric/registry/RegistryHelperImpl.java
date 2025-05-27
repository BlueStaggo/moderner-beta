package mod.bluestaggo.modernerbeta.fabric.registry;

import com.mojang.serialization.Lifecycle;
import mod.bluestaggo.modernerbeta.registry.IRegistryBuilder;
import mod.bluestaggo.modernerbeta.registry.IRegistryHelper;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;

public class RegistryHelperImpl implements IRegistryHelper {
    private <T> IRegistryBuilder<T> from(MutableRegistry<T> registry) {
        return new RegistryBuilderImpl<>(registry);
    }

    @Override
    public <T> IRegistryBuilder<T> createSimple(RegistryKey<Registry<T>> key) {
        return from(new SimpleRegistry<>(key, Lifecycle.stable(), false));
    }

    @Override
    public <T> IRegistryBuilder<T> createDefaulted(RegistryKey<Registry<T>> key, Identifier defaultKey) {
        return from(new SimpleDefaultedRegistry<>(defaultKey.toString(), key, Lifecycle.stable(), false));
    }
}
