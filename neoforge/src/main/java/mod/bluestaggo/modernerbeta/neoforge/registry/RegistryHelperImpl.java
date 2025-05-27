package mod.bluestaggo.modernerbeta.neoforge.registry;

import mod.bluestaggo.modernerbeta.registry.IRegistryBuilder;
import mod.bluestaggo.modernerbeta.registry.IRegistryHelper;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.neoforged.neoforge.registries.NewRegistryEvent;

public record RegistryHelperImpl(NewRegistryEvent event) implements IRegistryHelper {
    @Override
    public <T> IRegistryBuilder<T> createSimple(RegistryKey<Registry<T>> key) {
        net.neoforged.neoforge.registries.RegistryBuilder<T> registryBuilder = new net.neoforged.neoforge.registries.RegistryBuilder<>(key);
        return new RegistryBuilderImpl<>(event, registryBuilder);
    }

    @Override
    public <T> IRegistryBuilder<T> createDefaulted(RegistryKey<Registry<T>> key, Identifier defaultKey) {
        net.neoforged.neoforge.registries.RegistryBuilder<T> registryBuilder = new net.neoforged.neoforge.registries.RegistryBuilder<>(key).defaultKey(defaultKey);
        return new RegistryBuilderImpl<>(event, registryBuilder);
    }
}
