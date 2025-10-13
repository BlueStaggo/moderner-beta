package mod.bluestaggo.modernerbeta.registry;

import net.minecraft.core.Registry;

public interface IRegistryBuilder<T> {
    IRegistryBuilder<T> synced();
    IRegistryBuilder<T> optional();
    Registry<T> build();
}
