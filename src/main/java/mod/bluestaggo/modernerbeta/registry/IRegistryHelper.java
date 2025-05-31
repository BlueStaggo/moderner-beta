package mod.bluestaggo.modernerbeta.registry;

import net.minecraft.registry.*;
import net.minecraft.util.Identifier;

public interface IRegistryHelper {
    <T> IRegistryBuilder<T> createSimple(RegistryKey<Registry<T>> key);
    <T> IRegistryBuilder<T> createDefaulted(RegistryKey<Registry<T>> key, Identifier defaultKey);
}
