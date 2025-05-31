package mod.bluestaggo.modernerbeta.registry;

import net.minecraft.registry.Registry;

public interface IRegistryBuilder<T> {
    IRegistryBuilder<T> synced();
    IRegistryBuilder<T> optional();
    Registry<T> build();
}
