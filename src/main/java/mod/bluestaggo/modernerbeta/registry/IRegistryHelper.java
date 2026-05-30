package mod.bluestaggo.modernerbeta.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

public interface IRegistryHelper {
    <T> IRegistryBuilder<T> createSimple(ResourceKey<Registry<T>> key);
    <T> IRegistryBuilder<T> createDefaulted(ResourceKey<Registry<T>> key, Identifier defaultKey);
}
