package mod.bluestaggo.modernerbeta.registry;

import net.minecraft.registry.Registry;

public interface RegistryBuilder<T> {
    RegistryBuilder<T> synced();
    RegistryBuilder<T> optional();
    Registry<T> build();
}
