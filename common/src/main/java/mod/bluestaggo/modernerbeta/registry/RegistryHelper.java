package mod.bluestaggo.modernerbeta.registry;

import net.minecraft.registry.*;
import net.minecraft.util.Identifier;

public interface RegistryHelper {
    <T> RegistryBuilder<T> createSimple(RegistryKey<Registry<T>> key);
    <T> RegistryBuilder<T> createDefaulted(RegistryKey<Registry<T>> key, Identifier defaultKey);
}
